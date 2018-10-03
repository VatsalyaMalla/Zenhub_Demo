/* 
 * Licensed Materials - Property of IBM
 * 6949 - XXX  Copyright IBM Corp. 1992, 1993 All Rights Reserved
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.BMSSOAuth;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.Utils.URLUtility;
import com.ibm.mobilefirst.authentication.AKCustomAuthConfigManager;
import com.ibm.mobilefirst.authentication.AKLoginModule;
import com.ibm.mobilefirst.authentication.AuthMethodSystem;
import com.ibm.mobilefirst.authentication.AuthMethodToken;
import com.ibm.mobilefirst.authentication.TokenManager;
import com.ibm.mobilefirst.authentication.constants.AKAuthenticationConfigConstants;
import com.ibm.mobilefirst.authentication.constants.AKCustomAuthWrapperConstants;
import com.ibm.mobilefirst.authentication.exception.ConfigurationException;
import com.ibm.mobilefirst.authentication.exception.ValidationException;
import com.worklight.server.auth.api.UserIdentity;

@SuppressWarnings("unused")
public class BMSSOLoginModule extends AKLoginModule {

	private static final Logger LOGGER = Logger.getLogger(BMSSOLoginModule.class.getName());
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(BMSSOLoginModule.class.getName());
	private String sessionId = "";
	private long idUser;
	private String accessToken = "";
	private String uName = "";
	private Map<String, String> userAttributes = new HashMap<>();
	
	/**
	 * Method to invoke the authentication module of Bluemix SSO Login Service
	 * 
	 * @param authenticationData
	 * @return boolean
	 */
	@Override
	public boolean login(Map<String, Object> authenticationData) {

		boolean validated = false;
		sessionId = (String) authenticationData.get(AKCustomAuthWrapperConstants.SESSION_ID);
		String username = (String) authenticationData.get(AKCustomAuthWrapperConstants.USERNAME);
		String password = (String) authenticationData.get("password");
		try {
			validated = validateCredentials(username, password);
		} catch (Exception e) {
			printException(e);
		}

		LOGGER.info("Credentials are validated and result is returned as : " + validated);

		return validated;
	}

	/**
	 * Method to validate the credentials provided by the app
	 * 
	 * @param username
	 * @param pass
	 * @return
	 * @throws ValidationException
	 * @throws ConfigurationException
	 */
	protected boolean validateCredentials(String username, String pass)
			throws ValidationException, ConfigurationException {

		boolean valid = false;

		try {
			LOGGER.info("Credential are obtained to proceed with validation.");

			HttpClient httpclient=null;
			LOGGER.severe("User calling logout before signing in");
			LOGGER.severe("Removing httpclient session");
			TokenManager.removeHttpclientUser(username);
			LOGGER.severe("Creating new httpclient session");
			TokenManager.setHttpclientUser(username, HttpClients.custom().build());
			
			httpclient = TokenManager.getHttpclientUser(username);
			LOGGER.severe("HTTP client new obj" +httpclient);
			
			String loginUrl = URLUtility.getRequiredUrl(AKAuthenticationConfigConstants.CLIENT_URL);
			LOGGER.severe("HTTP Login URL >>"+loginUrl);
			HttpPost httpPost = new HttpPost(loginUrl);
			JSONObject reqObj = new JSONObject();
			reqObj.put(BMSSOConstants.USERNAME, username);
			reqObj.put(BMSSOConstants.PASSCODE, pass);
			StringEntity input = new StringEntity(reqObj.toString());
			input.setContentType("application/json");
			httpPost.setEntity(input);
			HttpResponse response = httpclient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200){
				valid = true;
				HttpEntity entity = response.getEntity();
				JSONObject object = (JSONObject) new JSONParser().parse(EntityUtils.toString(entity));
				if(null != object){
					accessToken = (String) object.get("TOKEN");
					idUser = (long) object.get("ID_USER");
					uName = (String) object.get("USERNAME");
					LOGGER.severe("Token:>>"+accessToken+"<<");
					LOGGER.severe("idUser:>>"+idUser+"<<");
				}else{
					throw new NullPointerException();
				}
				LOGGER.severe("userId "+idUser+ " with token "+accessToken);
				long expiryTime = new Long(28800);
				
				Calendar startTime = Calendar.getInstance();
				Calendar endTime = Calendar.getInstance();
				endTime.add(Calendar.SECOND,(int)expiryTime);

				if(expiryTime == -1){
					endTime.add(Calendar.HOUR, 24);
				}else{
					endTime.add(Calendar.SECOND, (int)expiryTime);
				}
				
				AuthMethodToken authMethodToken = TokenManager.createAuthMethodToken(accessToken, startTime.getTime(),
						endTime.getTime());
				AuthMethodSystem authMethodSystem = TokenManager.createAuthMethodSystem(username, pass,
						"BMSSOLoginModule", authMethodToken); //AKCustomAuthConfigManager.getBMSSOAuthClass()
				TokenManager.addToken(sessionId, authMethodSystem);
				LOGGER.severe("Token is successfully generated :" + valid);
			}else{
				throw new ConfigurationException("Username or password is invalid");
			}
			return valid;
		} catch (Exception ex) {
			printException(ex);
			LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
			throw new ConfigurationException(BMSSOConstants.LOGIN_ISSUE, ex);
		}
	}

	/**
	 * Method to renew the token
	 * 
	 * @param sessionId
	 * @param authMethodSystem
	 * @return
	 * @throws ValidationException
	 */
	public boolean renewToken(String sessionId, AuthMethodSystem authMethodSystem)
			throws ValidationException, ConfigurationException {
		LOGGER.info("Token renewed for session : " + sessionId);
		this.sessionId = sessionId;
		return validateCredentials(authMethodSystem.getUsername(), authMethodSystem.getPassword());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.worklight.server.auth.api.WorkLightAuthLoginModule#createIdentity(
	 * java.lang.String)
	 */
	@Override
	public UserIdentity createIdentity(String loginModule) {
		AuthMethodSystem authMethodSystem = TokenManager.getAuthMethodSystem(sessionId);
		Map<String, Object> customAttributes = new HashMap<>();
		customAttributes.put("AuthenticationDate", new Date());
		customAttributes.put("token", accessToken);
		customAttributes.put("userId", idUser);
		customAttributes.put("userName", uName);
		return new UserIdentity(loginModule, authMethodSystem.getUsername(), null, null, customAttributes,
				authMethodSystem.getPassword());
	}

	/**public static void main(String[] args) {
		 Map<String, Object> authenticationData =  new HashMap<>();
		BMSSOLoginModule mm = new BMSSOLoginModule();
		authenticationData.put("sessionId", "snkn");
		authenticationData.put("username", "testInspector");
		authenticationData.put("password", "6hPAgxFG");
		boolean valid = mm.login(authenticationData);
		System.out.println("valid >>>>>> "+valid);
	}**/
	 void printException(Exception e){

		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    LOGGER.severe(sb.toString());
	}
}

package com.ibm.mobilefirst.authentication;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.HttpClient;

import com.ibm.mobilefirst.authentication.constants.AKCustomAuthWrapperConstants;
import com.worklight.oauth.util.StringUtils;

public class TokenManagerForUserId {


	private static final Logger LOGGER = Logger.getLogger(TokenManager.class.getName());

	/*
	 * Variable to hold the mapping of client and MFP tokens
	 */
	private static Map<String, AuthMethodSystem> tokenBuffer = new HashMap<>();
	private static Map<String, String> tokenUser = new HashMap<>();
	private static HttpClient httpclient = null;
	
	private static Map<String, HttpClient> httpclientUser = new HashMap<>();
	
	private TokenManagerForUserId() {
		LOGGER.severe("private constructor is invoked");
	}
	
	
	public static HttpClient getHttpclient() {
		return httpclient;
	}
	
	public static void setHttpclient(HttpClient httpclient) {
		TokenManagerForUserId.httpclient = httpclient ;
	}
	
	public static HttpClient getHttpclientUser(String user) {
		return httpclientUser.get(user);
	}
	
	public static void removeHttpclientUser(String user) {
		httpclientUser.remove(user);
	}


	public static void setHttpclientUser(String user, HttpClient httpclient) {
		TokenManagerForUserId.httpclientUser.put(user, httpclient) ;
	}

	/**
	 * Method to get the token for a given MFP request
	 * 
	 * @param request
	 * @return token String, that matches the sessionId for the request
	 */
	public static String getToken(HttpServletRequest request) {
		String sessionid = "";

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("WL_PERSISTENT_COOKIE".equalsIgnoreCase(cookie.getName())) {
				sessionid = cookie.getValue();
			}
		}

		LOGGER.info("Token requested from API generated token for the session " + sessionid + " is : "
				+ getToken(sessionid));
		return getToken(sessionid);
	}

	public static String getUser(HttpServletRequest request) {
		String sessionid = "";

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("WL_PERSISTENT_COOKIE".equalsIgnoreCase(cookie.getName())) {
				sessionid = cookie.getValue();
			}
		}

		LOGGER.info("Token requested from API generated token for the session " + sessionid + " is : "
				+ getUser(sessionid));
		return getUser(sessionid);
	}
	/**
	 * Method to create the AuthToken for a given MFP request with userKey
	 * 
	 * @param userKey,
	 *            userToken, startTime, endTime
	 * @return AuthMethodToken
	 */
	public static AuthMethodToken createAuthMethodToken(String userKey, String userToken, Date startTime,
			Date endTime) {

		String tokenStr = userKey + AKCustomAuthWrapperConstants.TOKEN_MANAGER_DELIMITER + userToken;

		return new AuthMethodToken(tokenStr, startTime, endTime);
	}

	/**
	 * Method to create the AuthToken for a given MFP request without userKey
	 * 
	 * @param userToken,
	 *            startTime, endTime
	 * @return AuthMethodToken
	 */
	public static AuthMethodToken createAuthMethodToken(String userToken, Date startTime, Date endTime) {
		return new AuthMethodToken(userToken, startTime, endTime);
	}

	/**
	 * Method to create the AuthMethodSystem for a given MFP request without
	 * userKey
	 * 
	 * @param userToken,
	 *            startTime, endTime
	 * @return AuthMethodSystem
	 */
	public static AuthMethodSystem createAuthMethodSystem(String username, String password, String systemName,
			AuthMethodToken authMethodToken) {
		return new AuthMethodSystem(username, password, systemName, authMethodToken);
	}

	/**
	 * Method to get the token for given sessionId
	 * 
	 * @param sessionId
	 * @return token value
	 */
	public static String getToken(String sessionId) {

		if (StringUtils.isEmpty(sessionId)) {
			LOGGER.severe("Login Sessionid not found : " + sessionId);
			return null;
		}

		String tokenVal;
		if (tokenBuffer.get(sessionId) != null) {
			AuthMethodToken authMethodToken = tokenBuffer.get(sessionId).getAuthMethodToken();
			tokenVal = authMethodToken.getTokenString();
		} else {
			LOGGER.severe("Login Sessionid not found");
			return null;
		}

		if (!isTokenValid(sessionId)) {
			tokenVal = renewToken(sessionId, tokenBuffer.get(sessionId));
			LOGGER.info("renewed Token : " + tokenVal);
		}
		return tokenVal;

	}
	
	public static String getUser(String sessionId) {

		if (StringUtils.isEmpty(sessionId)) {
			LOGGER.severe("Login Sessionid not found : " + sessionId);
			return null;
		}

		String userName;
		if (tokenBuffer.get(sessionId) != null) {
			userName = tokenBuffer.get(sessionId).getUsername();
		} else {
			LOGGER.severe("Login Sessionid not found");
			return null;
		}

		return userName;

	}

	/**
	 * Method to add Token to tokenBuffer
	 * 
	 * @param sessionId
	 * @param AuthMethodSystem
	 */
	public static void addToken(String sessionId, AuthMethodSystem authMethodSystem) {

		tokenBuffer.put(sessionId, authMethodSystem);
		LOGGER.fine("Generated token is buffered!");
	}
	
	/**
	 * Method to remove the token in case of session invalidated
	 * 
	 * @param sessionId
	 */
	public static void removeToken(String sessionId) {
		if (tokenBuffer.containsKey(sessionId)) {
			tokenBuffer.remove(sessionId);
		}
	}
	public static void addUserToken(String user, String token) {

		tokenUser.put(user, token);
		LOGGER.fine("Generated token is buffered!");
	}
	public static void addUserId(String user, String idUser) {

		tokenUser.put(user, idUser);
		LOGGER.fine("Generated userId is buffered!");
	}
	public static String getUserToken(String user) {
		
		LOGGER.fine("Returning the Token");
		return tokenUser.get(user);
		
	}
	public static String getUserId(String user) {
		
		LOGGER.fine("Returning the userId");
		return tokenUser.get(user);
		
	}
	public static void removeUserToken(String user, String token) {

		tokenUser.remove(user);
		LOGGER.fine("Generated token is buffered!");
	}
	
	public static void removeUserId(String user, String userId) {

		tokenUser.remove(user);
		LOGGER.fine("Generated userId is buffered!");
	}
	/**
	 * Method to validate the AuthToken
	 * 
	 * @param authMethodToken
	 * @return
	 */
	public static boolean isTokenValid(String sessionId) {
		boolean validToken = false;

		AuthMethodSystem authMethodSystem = getAuthMethodSystem(sessionId);
		if (authMethodSystem == null)
			return validToken;

		AuthMethodToken authMethodToken = authMethodSystem.getAuthMethodToken();
		Calendar calendar = Calendar.getInstance();

		Date currentTime = calendar.getTime();
		validToken = compareTokenTime(currentTime, authMethodToken.getEndTime());

		return validToken;
	}

	/**
	 * Method to compare 2 given time
	 * 
	 * @param df
	 * @param currentTime
	 * @param sorEndTime
	 * @return true, if currentTime < SOREndTime, else return false
	 */
	private static boolean compareTokenTime(Date currentTime, Date sorEndTime) {

		Calendar oldCal = Calendar.getInstance();
		Calendar newCal = Calendar.getInstance();

		oldCal.setTime(currentTime);
		newCal.setTime(sorEndTime);

		if (oldCal.before(newCal)) {
			return true;
		}

		return false;
	}

	/**
	 * Method to renew the token for a given authMethodSystem
	 * 
	 * @param authMethodSystem
	 * @param authMethodToken
	 * @return
	 */
	public static String renewToken(String sessionId, AuthMethodSystem authMethodSystem) {
		try {

			String className = authMethodSystem.getSystemName();
			Class cls = Class.forName(className);
			Object obj = cls.newInstance();

			Method method = cls.getDeclaredMethod("renewToken", String.class, AuthMethodSystem.class);
			Boolean renewalStatus = (Boolean) method.invoke(obj, sessionId, authMethodSystem);
			if (renewalStatus) {
				Calendar calendar = Calendar.getInstance();
				Date currentTime = calendar.getTime();
				LOGGER.info("Renew token invoked for current time :" + currentTime);
				return tokenBuffer.get(sessionId).getAuthMethodToken().getTokenString();

			}

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Method to get the corresponding AuthMethodSystem Object for a given MFP
	 * session
	 * 
	 * @param sessionId
	 * @return AuthMethodSystem
	 */
	public static AuthMethodSystem getAuthMethodSystem(String sessionId) {
		if (tokenBuffer.get(sessionId) != null) {
			return tokenBuffer.get(sessionId);
		} else {
			LOGGER.severe("Login Sessionid not found : ");
			return null;
		}
	}

	/**
	 * Method to add userAttributes to AuthMethodSystem Object for a given MFP
	 * session
	 * 
	 * @param sessionId,
	 *            userAttributes
	 */
	public static void addUserAttributes(String sessionId, Map<String, String> userAttributes) {
		LOGGER.info("UserAttributes are added!");
		AuthMethodSystem authMethodSystem = getAuthMethodSystem(sessionId);
		if(null!=authMethodSystem){
			authMethodSystem.addUserAttributes(userAttributes);
		}
	}



}

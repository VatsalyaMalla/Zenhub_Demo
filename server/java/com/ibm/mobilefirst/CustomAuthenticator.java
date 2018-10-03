/**
 * @author Amit Silvadasan(amit.silvadasan@in.ibm.com)
 */

package com.ibm.mobilefirst;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.worklight.server.auth.api.AuthenticationResult;
import com.worklight.server.auth.api.AuthenticationStatus;
import com.worklight.server.auth.api.MissingConfigurationOptionException;
import com.worklight.server.auth.api.UserIdentity;
import com.worklight.server.auth.api.WorkLightAuthenticator;


public class CustomAuthenticator implements WorkLightAuthenticator{
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CustomAuthenticator.class.getName());
    private Map<String, Object> authenticationData = null;
	
	public void init(Map<String, String> options) throws MissingConfigurationOptionException {
		logger.info("Inside init() of CustomAuthenticator");
		authenticationData = new HashMap<String, Object>();
	}

	/**
	 * Method to processRequest with credentials provided by the app
	 * 
	 * @param request
	 * @param response
	 * @param isAccessToProtectedResource
	 * @return AuthenticationResult
	 * @throws IOException,
	 *             ServletException
	 */
	public AuthenticationResult processRequest(HttpServletRequest request, HttpServletResponse response, boolean isAccessToProtectedResource) throws IOException,	ServletException {
		if (request.getRequestURI().contains("my_custom_auth_request_url")){
			logger.fine("Request is valid to process for authentication:" + request.getRequestURI());
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			if (null != username && null != password && username.length() > 0 && password.length() > 0){
				logger.info("Userename received for Authentication is : " + username);
				authenticationData.put("username", username);
				authenticationData.put("password", password);
				return AuthenticationResult.createFrom(AuthenticationStatus.SUCCESS);
			} else {
				logger.warning("Username or password received is null or empty.");
				response.setContentType("application/json; charset=UTF-8");
				response.setHeader("Cache-Control", "no-cache, must-revalidate");
				response.getWriter().print("{\"authStatus\":\"required\", \"errorMessage\":\"Please enter username and password\"}");
				return AuthenticationResult.createFrom(AuthenticationStatus.CLIENT_INTERACTION_REQUIRED);
			}
		} 
		
		if (!isAccessToProtectedResource) 
			return AuthenticationResult.createFrom(AuthenticationStatus.REQUEST_NOT_RECOGNIZED);
		
		response.setContentType("application/json; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.getWriter().print("{\"authStatus\":\"required\"}");
		return AuthenticationResult.createFrom(AuthenticationStatus.CLIENT_INTERACTION_REQUIRED);
	}

	/**
	 * method to customize the response changeResponseOnSuccess
	 * 
	 * @param request
	 * @param response
	 * @return boolean value
	 * @throws IOException
	 * 
	 */
	public boolean changeResponseOnSuccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getRequestURI().contains("my_custom_auth_request_url")){
			response.setContentType("application/json; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.getWriter().print("{\"authStatus\":\"complete\"}");
			return true;
		}
		return false;
	}

	/**
	 * Method to handle failed Authentication processAuthenticationFailure
	 * 
	 * @param request
	 * @param response
	 * @param errorMessage
	 * @return AuthenticationResult
	 * @throws IOException,
	 *             ServletException
	 * 
	 */
	public AuthenticationResult processAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
			String errorMessage) throws IOException, ServletException {

		response.setContentType("application/json; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.getWriter().print("{\"authStatus\":\"required\", \"errorMessage\":\"" + errorMessage + "\"}");
		return AuthenticationResult.createFrom(AuthenticationStatus.CLIENT_INTERACTION_REQUIRED);
	}

	/**
	 * Method to process the request which is Authenticated
	 * processRequestAlreadyAuthenticated
	 * 
	 * @param request
	 * @param response
	 * @return AuthenticationResult
	 * @throws IOException,
	 *             ServletException
	 * 
	 */
	public AuthenticationResult processRequestAlreadyAuthenticated(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return AuthenticationResult.createFrom(AuthenticationStatus.REQUEST_NOT_RECOGNIZED);
	}

	/**
	 * getAuthenticationData
	 * 
	 * @return Map
	 * 
	 */
	public Map<String, Object> getAuthenticationData() {
		logger.info("getAuthenticationData");
		return authenticationData;
	}
	
	/**
	 * Method to fetch the request to process getRequestToProceed
	 * 
	 * @param request
	 * @param response
	 * @param userIdentity
	 * @return AuthenticationResult
	 * @throws IOException
	 * 
	 */
	public HttpServletRequest getRequestToProceed(HttpServletRequest request, HttpServletResponse response, UserIdentity userIdentity)	throws IOException {
		return null;
	}

	/**
	 * Method to clone the data
	 * 
	 * @return WorkLightAuthenticator
	 * @throws CloneNotSupportedException
	 * 
	 */
	@Override
    public WorkLightAuthenticator clone() throws CloneNotSupportedException {
        CustomAuthenticator otherAuthenticator = (CustomAuthenticator) super.clone();
        otherAuthenticator.authenticationData = new HashMap<String, Object>(authenticationData);
        return otherAuthenticator;
    }

}

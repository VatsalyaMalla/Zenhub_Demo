/* 
 * Licensed Materials - Property of IBM
 * 6949 - XXX  Copyright IBM Corp. 1992, 1993 All Rights Reserved
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.mobilefirst.authentication.constants.AKCustomAuthWrapperConstants;
import com.worklight.server.auth.api.AuthenticationResult;
import com.worklight.server.auth.api.AuthenticationStatus;
import com.worklight.server.auth.api.MissingConfigurationOptionException;
import com.worklight.server.auth.api.UserIdentity;
import com.worklight.server.auth.api.WorkLightAuthenticator;

public class AKAuthenticator implements WorkLightAuthenticator {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AKAuthenticator.class.getName());
	private Map<String, Object> authenticationData = new HashMap<>();

	

	/*
	 * String to hold the token
	 */
	private String token;
	/*
	 * String to hold the login userid
	 */
	String username;
	/*
	 * String to hold the login password
	 */
	String password;
	
	public void init(Map<String, String> options) throws MissingConfigurationOptionException {

		LOGGER.severe("Inside init() of Authenticator");
		authenticationData = new HashMap<>();
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
	public AuthenticationResult processRequest(HttpServletRequest request, HttpServletResponse response,
			boolean isAccessToProtectedResource) throws IOException, ServletException {

		if (request.getRequestURI().contains(AKCustomAuthWrapperConstants.AUTH_REQUEST_URI)) {
			LOGGER.fine("Request is valid to process for authentication:" + request.getRequestURI());
			username = request.getParameter(AKCustomAuthWrapperConstants.USERNAME);
			password = request.getParameter("password");

			if (null != username && null != password && username.length() > 0 && password.length() > 0) {
				authenticationData.put(AKCustomAuthWrapperConstants.USERNAME, username);
				authenticationData.put("password", password);
				getToken(request);
				return AuthenticationResult.createFrom(AuthenticationStatus.SUCCESS);
			} else {
				LOGGER.warning("Username or password received is null or empty.");

				setResponse(response, "extReq", "");

				return AuthenticationResult.createFrom(AuthenticationStatus.CLIENT_INTERACTION_REQUIRED);
			}
		}

		if (!isAccessToProtectedResource)
			return AuthenticationResult.createFrom(AuthenticationStatus.REQUEST_NOT_RECOGNIZED);
		setResponse(response, "norReq", "");
		return AuthenticationResult.createFrom(AuthenticationStatus.CLIENT_INTERACTION_REQUIRED);
	}

	/*
	 * Retrieves the token
	 * 
	 * @param request
	 * @return
	 */
	private String getToken(HttpServletRequest request) {
		String token1 = "";
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			LOGGER.severe("cookie.getName() :" + cookie.getName());
			if ("WL_PERSISTENT_COOKIE".equalsIgnoreCase(cookie.getName())) {
				token1 = cookie.getValue();
			}
		}
		this.token = token1;
        LOGGER.severe("token value :" + token);
		authenticationData.put(AKCustomAuthWrapperConstants.SESSION_ID, this.token);
		return token;

	}

	/*
	 * Method to setResponse
	 * 
	 * @param response
	 * @param replacer
	 * @param errorMessage
	 * @throws IOException
	 */
	private void setResponse(HttpServletResponse response, String replacer, String errorMessage) throws IOException {
		String replacer1;
		if (replacer == "extReq")
			replacer1 = "{\"authStatus\":\"required\", \"errorMessage\":\"Please enter username and password\"}";
		else if (replacer == "norReq")
			replacer1 = "{\"authStatus\":\"required\"}";
		else if (replacer == "orReq")
			replacer1 = "{\"authStatus\":\"required\", \"errorMessage\":\"" + errorMessage + "\"}";
		else
			replacer1 = "{\"authStatus\":\"complete\"}";
		response.setContentType("application/json; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.getWriter().print(replacer1);

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
	public boolean changeResponseOnSuccess(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (request.getRequestURI().contains(AKCustomAuthWrapperConstants.AUTH_REQUEST_URI)) {
			setResponse(response, "complete", "");
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
		setResponse(response, "orReq", errorMessage);
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
	public AuthenticationResult processRequestAlreadyAuthenticated(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		return AuthenticationResult.createFrom(AuthenticationStatus.REQUEST_NOT_RECOGNIZED);
	}

	/**
	 * getAuthenticationData
	 * 
	 * @return Map
	 * 
	 */
	public Map<String, Object> getAuthenticationData() {

		Map<String, Object> authenticationData1 = new HashMap<>();
		authenticationData1.put(AKCustomAuthWrapperConstants.USERNAME, username);
		authenticationData1.put("password", password);
		authenticationData1.put(AKCustomAuthWrapperConstants.SESSION_ID, token);
		return authenticationData1;
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
	public HttpServletRequest getRequestToProceed(HttpServletRequest request, HttpServletResponse response,
			UserIdentity userIdentity) throws IOException {
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
		AKAuthenticator otherAuthenticator = (AKAuthenticator) super.clone();
		otherAuthenticator.authenticationData = new HashMap<>(authenticationData);
		return otherAuthenticator;
	}

}

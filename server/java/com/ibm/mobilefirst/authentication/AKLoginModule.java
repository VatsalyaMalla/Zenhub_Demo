/* 
 * Licensed Materials - Property of IBM
 * 6949 - XXX  Copyright IBM Corp. 1992, 1993 All Rights Reserved
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * @author Priyanka Kamble(priykamb@in.ibm.com)
 *
 */

package com.ibm.mobilefirst.authentication;

import java.util.Map;
import java.util.logging.Logger;

import com.worklight.server.auth.api.MissingConfigurationOptionException;
import com.worklight.server.auth.api.WorkLightAuthLoginModule;
import com.worklight.server.auth.api.WorkLightLoginModuleBase;

public abstract class AKLoginModule implements WorkLightAuthLoginModule {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(AKLoginModule.class.getName());

	String sessionId = "";

	@Override
	public WorkLightLoginModuleBase clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * Method to get Initialization parameters Get the init parameters from App
	 * via authenticationData Map or read the properties file to get the init
	 * parameters
	 */
	@Override
	public void init(Map<String, String> arg0) throws MissingConfigurationOptionException {
		LOGGER.info(" init() initialised");
	}

	/**
	 * Method to invoke the authentication against the SOR auth server
	 * 
	 */
	@Override
	public abstract boolean login(Map<String, Object> authenticationData);

	/**
	 * Method to invalidate the session once the user logs out Remove the token
	 * from tokenmanager for the logged out session
	 */
	@Override
	public void logout() {
		LOGGER.severe("Logout called");
		TokenManager.removeToken(sessionId);
	}

	/**
	 * Method to invalidate the session once the user aborts a session Remove
	 * the token from tokenmanager for the aborted session
	 */
	@Override
	public void abort() {
		LOGGER.severe("Abort called");
		TokenManager.removeToken(sessionId);
	}

}

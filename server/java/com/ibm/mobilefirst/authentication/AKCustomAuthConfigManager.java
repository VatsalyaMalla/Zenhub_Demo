/* 
 * Licensed Materials - Property of IBM
 * 6949 - XXX  Copyright IBM Corp. 1992, 1993 All Rights Reserved
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.authentication;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.mobilefirst.authentication.constants.AKAuthenticationConfigConstants;


/**
 * Class to read the configuration property file and load the data based on same
 * 
 * @author Priyanka Kamble(priykamb@in.ibm.com)
 *
 */
public class AKCustomAuthConfigManager {
	private static final Logger LOGGER = Logger.getLogger(AKCustomAuthConfigManager.class.getName());

	/*
	 * Variable to hold the inputStream
	 */

	private static InputStream inputStr = null;

	/*
	 * Variable to read the properties file
	 */

	private static Properties prop = null;

	/*
	 * Default Constructor
	 */
	private AKCustomAuthConfigManager() {

	}


	/**
	 * Method to read properties
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 */
	 static {
		if (prop == null) {
			prop = new Properties();
		}
		try {
			inputStr = new FileInputStream(
					AKAuthenticationConfigConstants.APP_CONFIG_FILE);
			prop.load(inputStr);
			LOGGER.info("Properties file "+AKAuthenticationConfigConstants.APP_CONFIG_FILE+" is read and loaded");
		} catch (Exception e) {

			throw new IllegalArgumentException("Error loading Configuration ["
					+ AKAuthenticationConfigConstants.APP_CONFIG_FILE + "], message=" + e.getClass().getName() + ":"
					+ e.getMessage()+ e);
		}

	}
	 
	/**
	 * Method to get the Sterling Authentication Method
	 * 
	 * @return String
	 */
	public static String getSterlingAuthClass() {
		return prop.getProperty(AKAuthenticationConfigConstants.STERLING_AUTH_CLASS);
	}

	/**
	 * Method to get the WCS Authentication Method
	 * 
	 * @return String
	 */
	public static String getWCSAuthClass() {
		return prop.getProperty(AKAuthenticationConfigConstants.WCS_AUTH_CLASS);
	}

	/**
	 * Method to get the Kerberos Authentication Method
	 * 
	 * @return String
	 */
	public static String getKerberosAuthClass() {
		return prop.getProperty(AKAuthenticationConfigConstants.KERBEROS_AUTH_CLASS);
	}

	/**
	 * Method to get the SAP Authentication Method
	 * 
	 * @return String
	 */
	public static String getSAPAuthClass() {
		return prop.getProperty(AKAuthenticationConfigConstants.SAP_AUTH_CLASS);
	}

	/**
	 * Method to get the Sterling Token EndPointUrl
	 * 
	 * @return String
	 */
	public static String getEndpointUrlProperty() {
		return prop.getProperty(AKAuthenticationConfigConstants.STERLING_TOKEN_ENDPOINT);
	}

	/**
	 * Method to get the BMSSO Authentication Method
	 * 
	 * @return String
	 */ 
	public static String getBMSSOAuthClass() {
		return prop.getProperty(AKAuthenticationConfigConstants.BMSSO_AUTH_CLASS);
	}

	/**
	 * Method to get the BMSSO Token EndPointUrl
	 * 
	 * @return String
	 */
	public static String getBMSSOEndUrlProperty() {
		return prop.getProperty(AKAuthenticationConfigConstants.BMSSO_TOKEN_ENDPOINT_URL);
	}
	
	/**
	 * Method to get the Client Token EndPointUrl
	 * 
	 * @return String
	 */
	public static String getClientEndUrlProperty() {
		String server = prop.getProperty(AKAuthenticationConfigConstants.CLIENT_SEREVR);
		String clientURL = prop.getProperty(AKAuthenticationConfigConstants.CLIENT_TOKEN_URL);
		if(server!=null && clientURL!=null){
			return server+clientURL;
		}else{
			return null;
		}
	}
	
	/**
	 * Method to get the Client Token EndPointUrl
	 * 
	 * @return String
	 */
	public static String getClientLogoutURL() {
		String server = prop.getProperty(AKAuthenticationConfigConstants.CLIENT_SEREVR);
		String clientURL = prop.getProperty(AKAuthenticationConfigConstants.CLIENT_URL_POST_LOGOUT);
		if(server!=null && clientURL!=null){
			return server+clientURL;
		}else{
			return null;
		}
	}
	
	/**
	 * Method to get the Client Token Expiry Time
	 * 
	 * @return String
	 */
	public static String getClientTokenExpiryTime() {
		
		return prop.getProperty(AKAuthenticationConfigConstants.TOKEN_EXPIRY_TIME);
	}
	
	/**
	 * Method to get the BMSSO ClientSecretProperty
	 * 
	 * @return String
	 */
	public static String getBMSSOClientSecretProperty() {
		return prop.getProperty(AKAuthenticationConfigConstants.CLIENT_SECRET);
	}
	
	/**
	 * Method to get the BMSSO ClientId
	 * 
	 * @return String
	 */
	public static String getBMSSOClientIdProperty() {
		return prop.getProperty(AKAuthenticationConfigConstants.CLIENT_ID);
	}

	/**
	 * Method to get the BMSSO ProfileEndpointUrl
	 * 
	 * @return String 
	 */
	public static String getBMSSOProfileEndpointUrlProperty() {
		return prop.getProperty(AKAuthenticationConfigConstants.BMSSO_PROFILE_ENDPOINT);
	}

	/**
	 * Method to get the LDAP Authentication Method
	 * 
	 * @return String
	 */
	public static String getLDAPAuthClass() {
		return prop.getProperty(AKAuthenticationConfigConstants.LDAP_AUTH_CLASS);
	}

	/**
	 * Method to get the LDAP InitialContextFactory
	 * 
	 * @return String
	 */
	public static String getInitialContextFactoryProperty() {
		return prop.getProperty(AKAuthenticationConfigConstants.LDAP_INITIAL_CONTEXT_FACTORY);
	}

	/**
	 * Method to get the LDAP InitialContextFactory
	 * 
	 * @return String
	 */
	public static String getSecurityAuthenticationProperty() {
		return prop.getProperty(AKAuthenticationConfigConstants.LDAP_SECURITY_AUTHENTICATION);
	}

	/**
	 * Method to get the LDAP InitialContextFactory
	 * 
	 * @return String
	 */
	public static String getProviderUrlProperty() {
		return prop.getProperty(AKAuthenticationConfigConstants.LDAP_PROVIDER_URL);
	}

	/**
	 * Method to get the properties file
	 * 
	 * @return String
	 */
	public static String getProperty(String propertyString) {
		return prop.getProperty(propertyString);
	}

}

/* 
 * Licensed Materials - Property of IBM
 * 6949 - XXX  Copyright IBM Corp. 1992, 1993 All Rights Reserved
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.BMSSOAuth;

/**
 * @author Priyanka Kamble(priykamb@in.ibm.com) class to maintains the BMSSO
 *         constants.
 */
public class BMSSOConstants {
	public static final String GRANT_TYPE = "grant_type";
	public static final String SCOPE = "scope";
	public static final String USERNAME = "username";
	public static final String PASSCODE = "password";

	public static final String CLIENT_ID = "CLIENTID";
	public static final String CLIENT_SECRET = "CLIENTTOKEN";
	public static final String USERTOKEN = "USERTOKEN";
	
	public static final String REFRESH_TOKEN ="refresh_token";
	
	public static final String ACCESS_TOKEN = "user_token";
	public static final String EXPIRES_IN = "expires_in";
	
	public static final String PROFILE_FIRSTNAME = "firstName";
	public static final String PROFILE_LASTNAME = "lastName";
	public static final String PROFILE_DISPLAYNAME = "userDisplayName";
	public static final String PROFILE_NAME = "name";
	public static final String PROFILE_USERNAME = "username";

	public static final String PROFILE_UNIQUEID = "userUniqueID";
	public static final String PROFILE_EMAIL = "email";
	
	public static final String LOGIN_ISSUE = "AKCC_CUSTAUTH_BMSSO_001";
	public static final String LOGIN_GENERICEXCEPTION = "AKCC_CUSTAUTH_BMSSO_002";

	private BMSSOConstants() {
	}
}

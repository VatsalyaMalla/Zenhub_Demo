/* 
 * Licensed Materials - Property of IBM
 * 6949 - XXX  Copyright IBM Corp. 1992, 1993 All Rights Reserved
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.authentication.constants;

/**
 * @author Priyanka Kamble(priykamb@in.ibm.com)
 *
 */

/**
 * class to maintains the Authentication constants.
 */

public class AKAuthenticationConfigConstants {

	public static final String APP_CONFIG_FILE = "expertfactory.client.properties";
	
	public static final String STERLING_AUTH_CLASS = "com.ibm.mobilefirst.framework.sterlingAuth.SterlingLoginModule";

	public static final String STERLING_TOKEN_ENDPOINT = "sterlingAuthMethod.tokenEndpoint";

	public static final String STERLING_EXPIRYTIME_INMINUTES = "sterlingAuthMethod.expiryTimeinminutes";

	public static final String WCS_AUTH_CLASS = "com.ibm.mobilefirst.framework.WCSAuth.WCSLoginModule";

	public static final String WCS_EXPIRYTIME_INMINUTES = "wcsAuthMethod.expiryTimeinminutes";

	public static final String WCS_TOKEN_ENDPOINT = "wcsAuthMethod.tokenEndpoint";

	public static final String SAP_AUTH_CLASS = "scom.ibm.mobilefirst.framework.sapAuth.sapAuthLoginModule";

	public static final String BMSSO_AUTH_CLASS = "com.ibm.mobilefirst.BMSSOAuth.BMSSOLoginModule";
    public static final String BMSSO_TOKEN_ENDPOINT_URL = "ionicssoAuthMethod.tokenendpointurl";
	
	public static final String CLIENT_SEREVR = "SERVER";
	public static final String CLIENT_URL = "client_url";
	public static final String TOKEN_EXPIRY_TIME = "TOKEN_EXPIRY_TIME";
	
	public static final String CLIENT_TOKEN_URL = "CLIENT_URL_POST_LOGIN";
	public static final String CLIENT_URL_POST_LOGOUT = "CLIENT_URL_POST_LOGOUT";
	

	public static final String CLIENT_SECRET = "CLIENTTOKEN";
	
	
	public static final String CLIENT_ID = "CLIENTID";
	
	public static final String BMSSO_EXPIRYTIME_INMINUTES = "AuthMethod.expiryTimeinminutes";

	public static final String BMSSO_PROFILE_ENDPOINT = "bmssoAuthMethod.profileendpointurl";

	public static final String KERBEROS_TOKEN_ENDPOINT = "kerberosAuthMethod.tokenEndpoint";

	public static final String KERBEROS_EXPIRYTIME_INMINUTES = "kerberosAuthMethod.expiryTimeinminutes";

	public static final String KERBEROS_AUTH_CLASS = "com.ibm.mobilefirst.framework.kerberosAuth.kerberosLoginModule";

	public static final String LDAP_AUTH_CLASS = "com.ibm.mobilefirst.framework.LDAPAuth.LDAPLoginModule";

	public static final String LDAP_EXPIRYTIME_INMINUTES = "ldapAuthMethod.expiryTimeinminutes";

	public static final String LDAP_INITIAL_CONTEXT_FACTORY = "ldapAuthMethod.initialContextFactory";

	public static final String LDAP_SECURITY_AUTHENTICATION = "ldapAuthMethod.securityAuthentication";

	public static final String LDAP_PROVIDER_URL = "ldapAuthMethod.providerUrl";
	
	public static final String LDAP_USER_SEARCHBASE = "ldapAuthMethod.userSearchBase";
	
	public static final String LDAP_SEARCH_BASE = "ldapAuthMethod.searchBase";
	
	public static final String LDAP_SEARCH_FILTER = "ldapAuthMethod.searchFilter";
	private AKAuthenticationConfigConstants() {

	}

}

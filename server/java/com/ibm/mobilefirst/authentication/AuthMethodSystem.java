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

import java.util.HashMap;
import java.util.Map;

public class AuthMethodSystem {

	private String username;

	private String password;

	private String systemName;

	private AuthMethodToken authMethodToken = null;

	private Map<String, String> userAttributes = new HashMap<>();

	public AuthMethodSystem(String username, String password, String systemName, AuthMethodToken authMethodToken) {
		super();
		this.username = username;
		this.password = password;
		this.systemName = systemName;
		this.authMethodToken = authMethodToken;
	}

	public AuthMethodSystem(String username, String password, String systemName, AuthMethodToken authMethodToken,
			Map<String, String> userAttributes) {
		super();
		this.username = username;
		this.password = password;
		this.systemName = systemName;
		this.authMethodToken = authMethodToken;
		this.userAttributes = userAttributes;
	}

	public AuthMethodToken getAuthMethodToken() {
		return authMethodToken;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getSystemName() {
		return systemName;
	}

	public Map<String, String> getUserAttributes() {
		return userAttributes;
	}

	public void addUserAttributes(Map<String, String> userAttributes) {
		this.userAttributes.putAll(userAttributes);
	}

}

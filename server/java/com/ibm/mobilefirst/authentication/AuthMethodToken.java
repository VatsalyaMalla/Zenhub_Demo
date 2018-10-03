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

import java.util.Date;

public class AuthMethodToken {

	private String tokenString;

	private Date startTime = null;

	private Date endTime = null;

	AuthMethodToken(String tokenString, Date startTime, Date endTime) {
		super();
		this.tokenString = tokenString;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getTokenString() {
		return tokenString;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}
}
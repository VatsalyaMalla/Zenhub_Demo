/* 
 * Licensed Materials - Property of IBM
 * 6949 - XXX  Copyright IBM Corp. 1992, 1993 All Rights Reserved
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

 
package com.ibm.mobilefirst.authentication.exception;


/**
 * 
 * class to generate and handle Configuration Exceptions
 */
public class ConfigurationException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public ConfigurationException(String exceptionCode, Throwable cause) {
		super(exceptionCode, cause);
	}

	public ConfigurationException(String exceptionCode) {
		super(exceptionCode);
	}

}
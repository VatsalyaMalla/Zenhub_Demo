/* 
 * Licensed Materials - Property of IBM
 * 6949 - XXX  Copyright IBM Corp. 1992, 1993 All Rights Reserved
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */ 

package com.ibm.mobilefirst.authentication.exception;


/**
 * 
 * Custom class to generate Validation Exception
 */

public class ValidationException extends Exception {

	
	private static final long serialVersionUID = 1L;
	
	public ValidationException(String exceptionCode, Throwable cause) {
		super(exceptionCode, cause);
	}

	public ValidationException(String exceptionCode) {
		super(exceptionCode);
	}
}

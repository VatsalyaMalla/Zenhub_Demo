/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2016. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.exception;

public class TecnicasException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	private int statusCode;
	private String errorMessage;
	private transient Object errorDetailedMessage;

	public TecnicasException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
		this.errorMessage = message;

	}

	public TecnicasException(String errorCode, int statusCode, Object error) {
		this.errorCode = errorCode;
		this.statusCode = statusCode;
		errorDetailedMessage = error;

	}

	public TecnicasException(Throwable exception, String errorCode, int statusCode, String message, Object error) {
		super(message, exception);
		this.errorCode = errorCode;
		this.statusCode = statusCode;
		this.errorMessage = message;
		errorDetailedMessage = error;

	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Object getErrorDetailedMessage() {
		return errorDetailedMessage;
	}

	@Override
	public String toString() {
		return "TecnicasException [errorCode=" + errorCode + ", statusCode=" + statusCode + ", errorMessage=" + errorMessage + ", errorDetailedMessage=" + errorDetailedMessage
				+ "]";
	}

}

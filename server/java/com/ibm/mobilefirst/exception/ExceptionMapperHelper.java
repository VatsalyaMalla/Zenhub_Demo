/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2017. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.ibm.mobilefirst.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONArray;

import com.google.gson.Gson;
import com.ibm.mobilefirst.beans.BaseResponse;
import com.ibm.mobilefirst.beans.ErrorElement;

public class ExceptionMapperHelper {
	static Logger logger = Logger.getLogger(ExceptionMapperHelper.class.getName());
	static final String GENERAL_MESSAGE = "An unexpected error occurred.";

	private ExceptionMapperHelper() {
	}

	public static String toErrorJsonString(String code, String message) {
		// Create a general error message
		ErrorElement errorResponse = new ErrorElement();
		List<ErrorElement> errorList = new ArrayList<>();
		BaseResponse baseResponse = new BaseResponse();
		errorResponse.setMessage(message);
		errorResponse.setId(code);
		errorResponse.setLevel(com.Utils.ServiceConstants.LEVEL_ERROR);
		errorList.add(errorResponse);
		baseResponse.setErrors(errorList);
		baseResponse.setData(new JSONArray());

		return new Gson().toJson(baseResponse);
	}

	public static String toErrorJsonString(BaseResponse base) {
		// Create a general error message from Base JSON
		return new Gson().toJson(base);
	}

	public static String toJsonString(TecnicasException e) {
		logger.severe("e.getErrorDetailedMessage()" + e.getErrorDetailedMessage().toString());
		if (e.getErrorDetailedMessage() instanceof BaseResponse) {
			logger.severe(" e.getErrorDetailedMessage() true");
			return toErrorJsonString((BaseResponse) e.getErrorDetailedMessage());
		} else if (e.getErrorDetailedMessage() instanceof String) {
			return toErrorJsonString(com.Utils.ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, (String) e.getErrorDetailedMessage());
		} else {
			return toErrorJsonString(com.Utils.ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage());
		}
	}

	public static String toJsonString(RuntimeException e) {
		return toErrorJsonString(com.Utils.ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, GENERAL_MESSAGE + " " + e.getMessage());
	}

}

/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2017. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.ibm.mobilefirst.industrial.materialsinspect.api.notes;

import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ibm.mobilefirst.exception.ExceptionMapperHelper;
import com.ibm.mobilefirst.exception.TecnicasException;

/**
 * @author jacob
 *
 */
@Provider
public class TecnicasSystemExceptionMapper implements ExceptionMapper<TecnicasException> {

	static Logger logger = Logger.getLogger(TecnicasSystemExceptionMapper.class.getName());

	@Override
	public Response toResponse(TecnicasException ex) {
		String jsonString = ExceptionMapperHelper.toJsonString(ex);
		ResponseBuilder resBuilder;
		try {
			resBuilder = Response.status(ex.getStatusCode());
		} catch (IllegalArgumentException e) {
			printException(e, "toResponse");
			logger.info("Cannot sent HTTP status " + ex.getStatusCode() + ". Sending 500 instead.");
			resBuilder = Response.status(500);
		}
		return resBuilder.type(MediaType.APPLICATION_JSON).entity(jsonString).build();
	}

	void printException(Exception e, String methodName) {
		logger.severe("Exception in " + methodName);
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append("\n");
		}
		logger.severe(sb.toString());
	}

}

package com.ibm.mobilefirst.industrial.materialsinspect.api.visit.exception;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.response.APIResponseBuilder;

public class AdapterExceptionHandler {
	static Logger logger = Logger.getLogger(AdapterExceptionHandler.class.getName());
	
	public static String getSORData(String connectionURL,String token) throws ParseException {
		HttpResponse httpResponse =null;
		HttpClient httpClient=null;
		httpClient = HttpClientBuilder.create().build();
		String responseBody ="";
		HttpGet httpGet = new HttpGet(connectionURL);
		httpGet.addHeader("language","US");
		httpGet.addHeader("Authorization",token);
		httpGet.addHeader("Content-Type","application/json");
		httpGet.addHeader("Cache-Control","no-cache,no-store");
		httpGet.addHeader("Pragma","no-cache");
		try {
			httpResponse = httpClient.execute(httpGet);
			logger.severe("Response Code " +httpResponse.getStatusLine().getStatusCode());
			HttpEntity entityBody = httpResponse.getEntity();
			if(entityBody!=null)
				try {
					responseBody = EntityUtils.toString(entityBody);
				} catch (IOException e) {
					printExceptions(e,"getSORData");
			}
		} catch (ClientProtocolException e) {
			printExceptions(e,"getSORData");
		}catch(UnknownHostException uhe){
			printExceptions(uhe,"Unknow host exception");
			AdapterExceptionHandler adapterExceptionHandler = new AdapterExceptionHandler();
			responseBody=adapterExceptionHandler.errorJSON();
		}catch (IOException e) {
			printExceptions(e,"getSORData");
			AdapterExceptionHandler adapterExceptionHandler = new AdapterExceptionHandler();
			responseBody=adapterExceptionHandler.errorJSON();
		}
		return responseBody;
	}
	public Response getSORDataExcpetion(String sorOutputString, String apiVersion){
		Response response = null;
		JSONObject sorResponseJSON;
		String errorMessagefromJSON ="";
		try {
			sorResponseJSON = (JSONObject) JSONValue.parse(sorOutputString);
			errorMessagefromJSON = (String) sorResponseJSON.get("ExceptionMessage");
			int firstIndex = errorMessagefromJSON.indexOf(":");
			String errorCodeStr = errorMessagefromJSON.substring(0, firstIndex);
			String errorMessage = errorMessagefromJSON.substring(firstIndex+1,errorMessagefromJSON.length());
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					errorCodeStr, errorMessage,
					ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					"1000", errorMessagefromJSON,
					ServiceConstants.LEVEL_ERROR);
			printExceptions(e,"getSORDataExcpetion");
		}
		return response; 
	}
	
static void printExceptions(Exception e,String methodName) {
		
		logger.severe("Exception in "+methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}
	void printException(Exception e,String methodName) {
	
	logger.severe("Exception in "+methodName);
	StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : e.getStackTrace()) {
        sb.append(element.toString());
        sb.append("\n");
    }
    logger.severe(sb.toString());
 }
	
	public Response getSORDataExcpetionForAuthorization(String sorOutputString,
			String apiVersion) {
		Response response = null;
		JSONObject sorResponseJSON;
		String errorMessagefromJSON ="";
		String code = "";
		try {
			sorResponseJSON = (JSONObject) JSONValue.parse(sorOutputString);
			errorMessagefromJSON = (String) sorResponseJSON.get("Message");
			if(errorMessagefromJSON.contains("Authorization")){
				code = "403";
				errorMessagefromJSON = "Authorization not valid.Please login again.";
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						403,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						code, errorMessagefromJSON,
						ServiceConstants.LEVEL_ERROR);
			}
		} catch (Exception e) {
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					"500", errorMessagefromJSON,
					ServiceConstants.LEVEL_ERROR);
			printExceptions(e,"getSORDataExcpetionForAuthorization");
		}
		return response; 
	}
	public String errorJSON(){
		JSONObject errorJSONObject = new JSONObject();
		errorJSONObject.put("ExceptionType" , "Unknown Host Exception VPN Service is not availalbe");
		errorJSONObject.put("ExceptionMessage" , "500:VPN Service is not availalbe");
		return errorJSONObject.toString();
	}
}

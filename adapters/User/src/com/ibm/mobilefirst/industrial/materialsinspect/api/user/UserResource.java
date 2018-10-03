/*
ç *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.industrial.materialsinspect.api.user;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.Utils.URLUtility;
import com.Utils.ValidateUtility;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.user.UserAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.user.common.factory.UserAdapterFactory;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.response.APIResponseBuilder;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;


@Path("")
public class UserResource {
	/*
	 * For more info on JAX-RS see
	 * https://jsr311.java.net/nonav/releases/1.1/index.html
	 */
	// Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(UserResource.class.getName());
	@Context HttpServletRequest request;
	// Define the server api to be able to perform server operations
	WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
    
	@GET
	@Produces("application/json")
	//@OAuthSecurity(enabled=false)
	@OAuthSecurity(scope="default")
	@Path("/users/{user_id}")
	public Response getUserById(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("0") @HeaderParam("offset") Integer offset,
			@DefaultValue("0") @HeaderParam("limit") Integer limit,
			@PathParam("user_id") String userId) {
		UserAdapter adapter = UserAdapterFactory
				.getUserAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		logger.severe("authToken in UserResource :" + authToken);
		Response response = adapter.getUserById(contentType, acceptLanguage,
				userAgent, apiVersion, offset, limit, userId,authToken);
		return response;
	}

	@GET
	@Produces("application/json")
	@Path("/users/{user_id}/requests")
	@OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	public Response getRequestById(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@HeaderParam("LoggedIn-User") String loggedInUser,
			@DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("0") @HeaderParam("offset") Integer offset,
			@DefaultValue("0") @HeaderParam("limit") Integer limit,
			@PathParam("user_id") String userId) {

		UserAdapter adapter = UserAdapterFactory
				.getUserAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.getRequestById(contentType, acceptLanguage,
				userAgent, loggedInUser, apiVersion, offset, limit,userId,authToken);
		return response;
	}

	@GET
	@Produces("application/json")
	@Path("/users/{user_id}/visitskeleton")
	@OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	public Response getVisitSkeleton(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@HeaderParam("LoggedIn-User") String loggedInUser,
			@DefaultValue("0.1") @HeaderParam("Api-Version") String apiVersion,
			@PathParam("user_id") String userId,
			@QueryParam("status") String status) {

		UserAdapter adapter = UserAdapterFactory
				.getUserAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.getVisitSkeleton(contentType, acceptLanguage,
				userAgent, loggedInUser, apiVersion, userId, status,authToken);
		return response;
	}

	@POST
	@Produces("application/json")
    @OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	@Path("/refuse")
	public Response notificationRefuse(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@DefaultValue("0.1") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("") String request) {
		UserAdapter adapter = UserAdapterFactory
				.getUserAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.notificationRefuse(contentType,
				acceptLanguage, userAgent, apiVersion, request,authToken);
		return response;
	}
	
	@POST
	@Produces("application/json")
    @OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	@Path("/visitrefuse")
	public Response notificationVisitRefuse(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@DefaultValue("0.1") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("") String request) {
		UserAdapter adapter = UserAdapterFactory
				.getUserAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.notificationVisitRefuse(contentType,
				acceptLanguage, userAgent, apiVersion, request,authToken);
		return response;
	}
	
	@POST
	@Produces("application/json")
	@OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	@Path("/accept")
	public Response notificationAccept(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@DefaultValue("0.1") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("") String request) {

		UserAdapter adapter = UserAdapterFactory
				.getUserAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.notificationAccept(contentType,
				acceptLanguage, userAgent, apiVersion, request,authToken);
		return response;
	}
	
	@POST
	@Produces("application/json")
	@OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	@Path("/users/{user_id}/visits")
	public Response postVisit(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@DefaultValue("0.1") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("") String request,@PathParam("user_id") String userId) {

		UserAdapter adapter = UserAdapterFactory
				.getUserAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.postVisit(contentType,
				acceptLanguage, userAgent, apiVersion, request,userId,authToken);
		return response;
	}
	
	@POST
	@Produces("application/json")
	@Path("/users/username")
	@OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	public JSONObject getUserIdByName(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("0") @HeaderParam("offset") Integer offset,
			@DefaultValue("0") @HeaderParam("limit") Integer limit,
			@DefaultValue("") String request) {
		    long userIdentity;
	    	String userId="";
	    	JSONObject dataObj = new JSONObject();
	    	JSONObject responseObj = new JSONObject();
	    	List<JSONObject> dataList = new ArrayList<>();
	    	JSONObject finalObject = new JSONObject();
	    	JSONArray dataArray = new JSONArray();
			JSONObject dataObject = new JSONObject();
			JSONObject jsonRequest = new JSONObject();
    	    
    	try{
    		logger.severe("Attributes in UserResource in getUserIdByName:" + api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes());
    		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
    		userIdentity = (long) attributesValue.get("userId");
    		logger.severe("userIdentity "+ userIdentity);
    		userId = Long.toString(userIdentity);
    		boolean requestValidation = ValidateUtility.validateIds(request);
			if (requestValidation) {
				finalObject.put("data", new JSONArray());
    			responseObj.put("message", "Login name cannot be empty.");
    			responseObj.put("id", "500");
    			responseObj.put("level", "ERROR");
    			dataList.add(responseObj);
        		finalObject.put("errors", dataList);
        		return finalObject;
			}
    		dataObject = (JSONObject) JSONValue.parse(request);
			dataArray = (JSONArray) dataObject.get("data");
			jsonRequest = (JSONObject) dataArray.get(0);
			String loginUserName = (String) jsonRequest.get("loginUserName");
    		logger.severe("userId "+ userId+" in getUserIdByName.");
    		if(null!=userId && !userId.isEmpty()){
    			dataObj.put("userId", userId);
        		responseObj.put("response", dataObj);
        		responseObj.put("status", "Success");
        		responseObj.put("errors", new JSONArray());
        		dataList.add(responseObj);
        		finalObject.put("data", dataList);
        		finalObject.put("errors", new JSONArray());
        		return responseObj;
    		}else{
    			finalObject.put("data", new JSONArray());
    			responseObj.put("message", "UserId is empty.");
    			responseObj.put("id", "400");
    			responseObj.put("level", "ERROR");
    			dataList.add(responseObj);
        		finalObject.put("errors", dataList);
    		}
	    	}catch(Exception e){
	    		printException(e,"getUserIdByName");
	    	}
		    return finalObject;
	}
	
	@POST
    @Path("/logout")
	@Produces("application/json")
	@Consumes("application/json")
	//@OAuthSecurity(enabled=false)
	@OAuthSecurity(scope="default")
	public Response postLogout(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("0") @HeaderParam("offset") Integer offset,
			@DefaultValue("0") @HeaderParam("limit") Integer limit,@DefaultValue("") String request) {
    	JSONObject responseObject = new JSONObject();
    	JSONObject sorRequestObject = new JSONObject();
    	JSONArray dataArray = new JSONArray();
		JSONObject iosRequest = new JSONObject();
    	Response response = null;
    	List<JSONObject> data = new ArrayList<>();
    	JSONObject finalObject = new JSONObject();
		try{
			String url = URLUtility.getRequiredUrl(ServiceConstants.POST_LOGOUT);
			com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
			String token = (String) attributesValue.get("token");
			String uName = (String) attributesValue.get("userName");
	    	String authToken = URLUtility.getAuthToken(token,uName);
			JSONObject requestObject = (JSONObject) JSONValue.parse(request);
			dataArray = (JSONArray) requestObject.get("data");
			iosRequest = (JSONObject) dataArray.get(0);
			String user = (String) iosRequest.get("userName");
			sorRequestObject.put("USERNAME",user);
			String message = URLUtility.getSORDataWithHeaderParamsForLogout(url, sorRequestObject.toString(),authToken);
			JSONObject jsonRequest = (JSONObject) JSONValue.parse(message);
			String code = (String) jsonRequest.get("id");
			if("200".equalsIgnoreCase(code)){
				responseObject.put("message", "Logout successful");
				responseObject.put("id", code);
				data.add(responseObject);
				finalObject.put("data", data);
				finalObject.put("errors", new JSONArray());
			}else if("403".equalsIgnoreCase(code)){
				responseObject.put("message", "Authorization is not valid.Please login again.");
				responseObject.put("id", code);
				data.add(responseObject);
				finalObject.put("data", new JSONArray());
				finalObject.put("errors", data);
			}else{
				responseObject.put("message", "Logout failed.");
				responseObject.put("id", code);
				data.add(responseObject);
				finalObject.put("data", new JSONArray());
				finalObject.put("errors", data);
			}
			int responseCode = Integer.parseInt(code);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode,
					ServiceConstants.SUCCESS_MESSAGE,responseObject);
		}catch(Exception e){
			printException(e,"postLogout");
		}
		return response;
	}
	
	@GET
	@Produces("application/json")
	@Path("/users/{user_id}/{visit_id}/visits")
	@OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	public Response getSingleVisitById(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@HeaderParam("LoggedIn-User") String loggedInUser,
			@DefaultValue("0.1") @HeaderParam("Api-Version") String apiVersion,
			@PathParam("user_id") String userId,
			@PathParam("visit_id") String visitId,
			@QueryParam("status") String status) {

		UserAdapter adapter = UserAdapterFactory
				.getUserAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.getSingleVisitById(contentType, acceptLanguage,
				userAgent, loggedInUser, apiVersion, userId,visitId, status,authToken);
		return response;

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

}

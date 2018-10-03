/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.industrial.materialsinspect.api.notification;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.json.simple.JSONValue;
import org.json.simple.JSONObject;

import com.worklight.adapters.rest.api.PushAPI;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.adapters.rest.api.push.INotification;
import com.worklight.core.auth.OAuthSecurity;

@Path("")
public class NotificationResource {
	/*
	 * For more info on JAX-RS see https://jsr311.java.net/nonav/releases/1.1/index.html
	 */
		
	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(NotificationResource.class.getName());

    //Define the server api to be able to perform server operations
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
    @Context HttpServletRequest request;
    
    @POST
	@Produces("application/json")
	@Path("/pushAdapter/submitNotification")
	@OAuthSecurity(enabled=false)
	public JSONObject sendNotification(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser,
			@DefaultValue("") String notificationRequest) {
    	
    	JSONObject requestObject = (JSONObject) JSONValue.parse(notificationRequest);
    	logger.severe("Notification request : " + requestObject);
    	String remoteHost = request.getRemoteHost();
    	String remoteIp = request.getRemoteAddr();
    	logger.severe("Client host and IP : " + remoteHost +" "+ remoteIp);
    	JSONObject response = new JSONObject();
		JSONObject dataObject = new JSONObject();
        try{
        	if(requestObject.isEmpty()){
				logger.severe("Notification request is empty.");
				response.put("id", 500);
				response.put("message", "Notification request is empty.");
				response.put("level", "error");
				return response;
			}
        	dataObject = (JSONObject) JSONValue.parse(notificationRequest);
        	logger.severe("Push notification request json : " + dataObject);
        	String userId = (String) dataObject.get("userId");
        	String notificationText = (String) dataObject.get("notificationText");
        	logger.severe("userId : " + userId);
        	logger.severe("notificationText : " + notificationText);
	    	PushAPI pushApi = api.getPushAPI();
	    	INotification notification = pushApi.buildNotification();
	    	notification.getMessage().setAlert(notificationText);
	    	notification.getTarget().setUserIds(userId);
	    	notification.getSettings().getAPNS().setBadge(0);
			notification.getSettings().getAPNS().setSound("default");
	    	pushApi.sendMessage(notification, "materialsinspect");
	    	response.put("id", 200);
			response.put("message", "Notification sent successfully.");
			response.put("level", "info");
	        }catch(Exception e){
	        	printException(e,"sendNotification");
	        	response.put("id", 500);
				response.put("message", e.getMessage());
				response.put("level", "error");
				return response;
	        }
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

package com.ibm.mobilefirst.industrial.materialsinspect.adapter.user;

import javax.ws.rs.core.Response;

public interface UserAdapter {
	
	public abstract Response getUserById(String contentType, String acceptLanguage, String userAgent, String apiVersion,
			Integer offset, Integer limit, String userId,String token);

	public abstract Response getRequestById(String contentType, String acceptLanguage, String userAgent, String loggedInUser, String apiVersion,
			Integer offset, Integer limit, String userId,String token);

	public abstract Response postVisit(String contentType, String acceptLanguage, String userAgent, String apiVersion, 
			String request,String userId,String token);

	public abstract Response notificationRefuse(String contentType,String acceptLanguage, String userAgent, String apiVersion,
			String request,String token);

	public abstract Response notificationAccept(String contentType,String acceptLanguage, String userAgent, String apiVersion,
			String request,String token);

	public abstract Response getSingleVisitById(String contentType,String acceptLanguage, String userAgent, String loggedInUser,
			String apiVersion, String userId,String visitId, String status, String authToken);

	public abstract Response getVisitSkeleton(String contentType,String acceptLanguage, String userAgent, String loggedInUser,
			String apiVersion, String userId, String status, String authToken);

	public abstract Response notificationVisitRefuse(String contentType,String acceptLanguage, String userAgent, String apiVersion,
			String request, String authToken);
	
}

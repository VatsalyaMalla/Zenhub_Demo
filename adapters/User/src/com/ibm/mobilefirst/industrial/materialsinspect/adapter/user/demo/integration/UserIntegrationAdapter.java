package com.ibm.mobilefirst.industrial.materialsinspect.adapter.user.demo.integration;

import javax.ws.rs.core.Response;

import com.ibm.mobilefirst.industrial.materialsinspect.adapter.user.UserAdapter;
 
public class UserIntegrationAdapter implements UserAdapter {

	@Override
	public Response getUserById(String contentType, String acceptLanguage, String userAgent, String apiVersion,
			Integer offset, Integer limit, String userId,String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getRequestById(String contentType, String acceptLanguage, String loggedInUser, String userAgent, String apiVersion,
			Integer offset, Integer limit, String userId,String token) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Response postVisit(String contentType, String acceptLanguage,
			String userAgent, String apiVersion, String request,String userId,String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response notificationRefuse(String contentType,
			String acceptLanguage, String userAgent, String apiVersion,
			String request,String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response notificationAccept(String contentType,
			String acceptLanguage, String userAgent, String apiVersion,
			String request,String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getSingleVisitById(String contentType,
			String acceptLanguage, String userAgent, String loggedInUser,
			String apiVersion, String userId,String visitId, String status, String authToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getVisitSkeleton(String contentType, String acceptLanguage,
			String userAgent, String loggedInUser, String apiVersion,
			String userId, String status, String authToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response notificationVisitRefuse(String contentType,
			String acceptLanguage, String userAgent, String apiVersion,
			String request, String authToken) {
		// TODO Auto-generated method stub
		return null;
	}


}

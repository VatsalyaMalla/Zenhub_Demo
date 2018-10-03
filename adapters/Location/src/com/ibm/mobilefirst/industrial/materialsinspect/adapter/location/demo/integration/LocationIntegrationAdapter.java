package com.ibm.mobilefirst.industrial.materialsinspect.adapter.location.demo.integration;

import javax.ws.rs.core.Response;

import com.ibm.mobilefirst.industrial.materialsinspect.adapter.location.LocationAdapter;
 
public class LocationIntegrationAdapter implements LocationAdapter {

	
	@Override
	public Response getItemsByLocationId(String contentType, String acceptLanguage, String apiVersion, String loggerUserId,
			String userAgent, String locationId, Integer offset, Integer limit,String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getMediaDescriptionByLocationId(String contentType, String acceptLanguage, String language,String apiVersion,
			String userAgent, String locationId,String userId, String loggedInUser, Integer offset, Integer limit,String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateNcr(String contentType, String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String ncrData,String token) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Response updatePunchListItem(String contentType, String acceptLanguage, String apiVersion,
			String loggedInUser, String userAgent, String punchList,String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateIncident(String contentType, String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String incident,String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getReportsDescriptionByLocationId(String contentType, String acceptLanguage, String apiVersion,
			String loggedInUser, String userAgent, String locationId,String userId, Integer offset, Integer limit,String token){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getSingleNcrsByLocationId(String contentType,
			String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String userId, String visitId,String locationId, Integer offset,
			Integer limit, String authToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getSinglePunchListItemByLocationId(String contentType,
			String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String userId, String visitId, Integer offset,
			Integer limit, String authToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getSingleIncidentByLocationId(String contentType,
			String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String userId,String visitId, Integer offset, Integer limit,
			String authToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getLocationByIdFromSingleSource(String contentType,
			String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String locationId, String authToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateNewNcr(String contentType, String acceptLanguage,
			String apiVersion, String loggedInUser, String userAgent,
			String ncrData, String authToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateNewIncident(String contentType,
			String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String ncrData, String authToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateNewPunchListItems(String contentType,
			String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String ncrData, String authToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getNewMediaDescriptionByLocationId(String contentType,
			String acceptLanguage, String language, String apiVersion,
			String loggedInUser, String userAgent, String locationId,
			Integer offset, Integer limit, String authToken) {
		// TODO Auto-generated method stub
		return null;
	}

}

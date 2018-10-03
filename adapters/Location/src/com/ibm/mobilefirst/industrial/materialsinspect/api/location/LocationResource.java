/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
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

import com.Utils.URLUtility;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.location.LocationAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.location.common.factory.LocationAdapterFactory;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.constants.ServiceConstants;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("")
public class LocationResource {

	// Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(LocationResource.class.getName());

	// Define the server api to be able to perform server operations
	WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
	@Context
	HttpServletRequest request;

	@GET
	@Produces("application/json")
	@Path("/locations/{location_id}")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response getLocationByIdFromSingleSource(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @PathParam("location_id") String locationId) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		return adapter.getLocationByIdFromSingleSource(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, locationId, authToken);
	}

	@GET
	@Produces("application/json")
	@Path("/locations/{location_id_list}/items")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response getItemsByLocationId(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @PathParam("location_id_list") String locationId,
			@DefaultValue("0") @QueryParam("offset") Integer offset, @DefaultValue("0") @QueryParam("limit") Integer limit) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.getItemsByLocationId(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, locationId, offset, limit, authToken);
		return response;
	}

	@GET
	@Produces("application/json")
	@Path("/locations/{user_id}/{visit_id}/{location_id}/ncrs")
	//@OAuthSecurity(enabled=false)
	@OAuthSecurity(scope = "default")
	public Response getSingleNcrsByLocationId(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @PathParam("user_id") String userId, @PathParam("visit_id") String visitId,
			@PathParam("location_id") String locationId, @DefaultValue("0") @QueryParam("offset") Integer offset, @DefaultValue("0") @QueryParam("limit") Integer limit) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.getSingleNcrsByLocationId(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, userId, visitId, locationId, offset, limit,
				authToken);
		return response;
	}

	@POST
	@Produces("application/json")
	@Path("/locations/updatencrs")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response updateNcr(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage, @HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @DefaultValue("") String ncrData) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.updateNcr(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, ncrData, authToken);
		return response;
	}

	@POST
	@Produces("application/json")
	@Path("/locations/updatenewncrs")
	@OAuthSecurity(scope = "default")
	//@OAuthSecurity(enabled=false)
	public Response updateNewNcr(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @DefaultValue("") String ncrData) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		return adapter.updateNewNcr(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, ncrData, authToken);
	}

	@POST
	@Produces("application/json")
	@Path("/locations/updatenewincidents")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response updateNewIncident(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @DefaultValue("") String ncrData) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		return adapter.updateNewIncident(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, ncrData, authToken);
	}

	@POST
	@Produces("application/json")
	@Path("/locations/updatenewpunchlistitems")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response updateNewPunchListItems(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @DefaultValue("") String ncrData) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		return adapter.updateNewPunchListItems(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, ncrData, authToken);
	}

	@GET
	@Produces("application/json")
	@Path("/locations/{user_id}/{visit_id}/punchListItems")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response getSinglePunchListItemByLocationId(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @PathParam("user_id") String userId, @PathParam("visit_id") String visitId,
			@DefaultValue("0") @QueryParam("offset") Integer offset, @DefaultValue("0") @QueryParam("limit") Integer limit) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.getSinglePunchListItemByLocationId(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, userId, visitId, offset, limit, authToken);
		return response;
	}

	@POST
	@Produces("application/json")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	@Path("/locations/updatepunchListItems")
	public Response updatePunchListItem(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @DefaultValue("") String punchListItem) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.updatePunchListItem(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, punchListItem, authToken);
		return response;
	}

	@GET
	@Produces("application/json")
	@Path("/locations/{user_id}/{visit_id}/incidents")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response getSingleIncidentByLocationId(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @PathParam("user_id") String userId, @PathParam("visit_id") String visitId,
			@DefaultValue("0") @QueryParam("offset") Integer offset, @DefaultValue("0") @QueryParam("limit") Integer limit) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.getSingleIncidentByLocationId(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, userId, visitId, offset, limit, authToken);
		return response;
	}

	@POST
	@Produces("application/json")
	@Path("/locations/updateincidents")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response updateIncident(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @DefaultValue("") String incident) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.updateIncident(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, incident, authToken);
		return response;

	}

	@GET
	@Produces("application/json")
	@Path("/locations/{location_id_list}/{user_id}/mediaItems")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response getMediaDescriptionByLocationId(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("Language") String language, @HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName,
			@DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion, @DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser,
			@PathParam("location_id_list") String locationId, @PathParam("user_id") String userId, @DefaultValue("0") @QueryParam("offset") Integer offset,
			@DefaultValue("0") @QueryParam("limit") Integer limit) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.getMediaDescriptionByLocationId(contentType, acceptLanguage, language, apiVersion, loggedInUser, userAgent, locationId, userId, offset, limit,
				authToken);
		return response;

	}
	
	@GET
	@Produces("application/json")
	@Path("/locations/{location_id_list}/mediaItems")
	@OAuthSecurity(scope = "default")
	//@OAuthSecurity(enabled=false)
	public Response getNewMediaDescriptionByLocationId(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("Language") String language, @HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName,
			@DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion, @DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser,
			@PathParam("location_id_list") String locationId, @DefaultValue("0") @QueryParam("offset") Integer offset,
			@DefaultValue("0") @QueryParam("limit") Integer limit) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.getNewMediaDescriptionByLocationId(contentType, acceptLanguage, language, apiVersion, loggedInUser, userAgent, locationId, offset, limit,
				authToken);
		return response;

	}

	@GET
	@Produces("application/json")
	@Path("/locations/{location_id_list}/{user_id}/reports")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response getReportsDescriptionByLocationId(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @PathParam("location_id_list") String locationId, @PathParam("user_id") String userId,
			@DefaultValue("0") @QueryParam("offset") Integer offset, @DefaultValue("0") @QueryParam("limit") Integer limit) {

		LocationAdapter adapter = LocationAdapterFactory.getLocationAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.getReportsDescriptionByLocationId(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, locationId, userId, offset, limit,
				authToken);
		return response;

	}

}

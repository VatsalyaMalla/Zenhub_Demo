/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.industrial.materialsinspect.api.notes;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.Utils.URLUtility;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.notes.NotesAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.notes.common.factory.NotesAdapterFactory;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.constants.ServiceConstants;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("")
public class NotesResource {

	// Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(NotesResource.class.getName());

	// Define the server api to be able to perform server operations
	WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
	@Context
	HttpServletRequest request;

	@GET
	@Produces("application/json")
	@Path("/notes/validUsers/{location_id}")
	@OAuthSecurity(scope = "default")
	//@OAuthSecurity(enabled=false)
	public Response getValidUsersByLocationId(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @PathParam("location_id") String locationId) {

		NotesAdapter adapter = NotesAdapterFactory.getNotesAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		return adapter.getNotesId(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, locationId, authToken);
	}

	@GET
	@Produces("application/json")
	@Path("/notes/{location_id}")
	@OAuthSecurity(scope = "default")
	//@OAuthSecurity(enabled=false)
	public Response getNotesForLocation(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @PathParam("location_id") String locationId) {

		NotesAdapter adapter = NotesAdapterFactory.getNotesAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		return adapter.getNotesForLocation(contentType, acceptLanguage, apiVersion, loggedInUser, userAgent, locationId, authToken);

	}

	@POST
	@Produces("application/json")
	@Path("/notes/postNotes")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response postNotes(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage, @HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @DefaultValue("") String notesData) {
		NotesAdapter adapter = NotesAdapterFactory.getNotesAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		return adapter.postNotes(contentType, acceptLanguage, userAgent, apiVersion, loggedInUser, notesData, authToken);
	}

	@PUT
	@Produces("application/json")
	@Path("/notes/updateNotes")
	@OAuthSecurity(scope = "default")
	// @OAuthSecurity(enabled=false)
	public Response updateNotes(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @DefaultValue("") String notesData) {

		NotesAdapter adapter = NotesAdapterFactory.getNotesAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		return adapter.notesUpdate(contentType, acceptLanguage, userAgent, apiVersion, loggedInUser, notesData, authToken);
	}

	@DELETE
	@Produces("application/json")
	@Path("/deleteNotes/{note_id}")
	@OAuthSecurity(scope = "default")
	//@OAuthSecurity(enabled = false)
	public Response deleteNotes(@HeaderParam("Content-Type") String contentType, @HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent, @HeaderParam("UserName") String userName, @DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser, @PathParam("note_id") String idNote) {
		NotesAdapter adapter = NotesAdapterFactory.getNotesAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		logger.severe("deleteNotesUrl :" + "");
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		return adapter.deleteNotes(contentType, acceptLanguage, userAgent, apiVersion, loggedInUser, idNote, authToken);

	}

}

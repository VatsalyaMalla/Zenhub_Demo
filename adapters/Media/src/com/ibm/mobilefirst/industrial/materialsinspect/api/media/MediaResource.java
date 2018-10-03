/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.industrial.materialsinspect.api.media;

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
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.media.MediaAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.media.common.factory.MediaAdapterFactory;
import com.ibm.mobilefirst.industrial.materialsinspect.api.media.constants.ServiceConstants;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("")
public class MediaResource {
	/*
	 * For more info on JAX-RS see https://jsr311.java.net/nonav/releases/1.1/index.html
	 */
		
	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(MediaResource.class.getName());

    //Define the server api to be able to perform server operations
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
    @Context HttpServletRequest request;

    @GET
   	@Produces("application/json")
   	@Path("/medias/{media_id}/{media_type}")
    @OAuthSecurity(scope="default")
    //@OAuthSecurity(enabled=false)
    public Response getMediaFileByMediaPath(
    		@HeaderParam ("Content-Type") String contentType,
	    	@HeaderParam ("Accept-Language") String acceptLanguage,
	    	@HeaderParam ("User-Agent") String userAgent,
	    	@HeaderParam("UserName") String userName,
	    	@DefaultValue ("0.1") @HeaderParam ("Api-Version") String apiVersion,
	    	@PathParam ("media_id") String mediaId,
	    	@PathParam ("media_type") String mediaType,
    		@DefaultValue ("0") @QueryParam ("offset") Integer offset, 
	    	@DefaultValue ("0") @QueryParam ("limit") Integer limit){
    	MediaAdapter adapter = MediaAdapterFactory.getMediaAdapter(ServiceConstants.DATA_ADAPTER_NAME);
    	com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
        Response response = adapter.getMediaFileByMediaPath(contentType, acceptLanguage, apiVersion, userAgent, mediaId,mediaType, offset,limit,authToken);
        return response;
    }
    
    @POST
	@Produces("application/json")
	@Path("/medias/uploadmedia/{user_id}/{visit_id}")
	@OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	public Response updateMedia(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser,
			@PathParam ("user_id") String userId,
	    	@PathParam ("visit_id") String visitId,
			@DefaultValue("") String mediaData) {

    	MediaAdapter adapter = MediaAdapterFactory.getMediaAdapter(ServiceConstants.DATA_ADAPTER_NAME);
    	com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.updateMedia(contentType, acceptLanguage,userAgent, apiVersion,loggedInUser,userId,visitId,mediaData,authToken);
		return response;
	}
    
    @POST
	@Produces("application/json")
	@Path("/medias/ncrmedia/{user_id}/{visit_id}")
	@OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	public Response ncrMedia(
			@HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept-Language") String acceptLanguage,
			@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("UserName") String userName,
			@DefaultValue("1.0.0") @HeaderParam("Api-Version") String apiVersion,
			@DefaultValue("1.0.0") @HeaderParam("LoggedIn-User") String loggedInUser,
			@PathParam ("user_id") String userId,
	    	@PathParam ("visit_id") String visitId,
			@DefaultValue("") String mediaData) {

    	MediaAdapter adapter = MediaAdapterFactory.getMediaAdapter(ServiceConstants.DATA_ADAPTER_NAME);
    	com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
		Response response = adapter.ncrMedia(contentType, acceptLanguage,userAgent, apiVersion,loggedInUser,userId,visitId,mediaData,authToken);
		return response;
	}
		
}

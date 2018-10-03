/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.industrial.materialsinspect.api.visit;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.Utils.URLUtility;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.visit.VisitAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.visit.common.factory.VisitAdapterFactory;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.constants.ServiceConstants;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("")
public class VisitResource {
	/*
	 * For more info on JAX-RS see https://jsr311.java.net/nonav/releases/1.1/index.html
	 */
	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(VisitResource.class.getName());

    //Define the server api to be able to perform server operations
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
    @Context HttpServletRequest request;
    
	@GET
	@Produces("application/json")
	@Path("/locations/{locationId}/supportTeams")
	@OAuthSecurity(scope="default")
	//@OAuthSecurity(enabled=false)
	public Response getSupportTeamMembersByVisitId(	
		@HeaderParam ("Content-Type") String contentType,
		@HeaderParam ("Accept-Language") String acceptLanguage,
		@HeaderParam ("User-Agent") String userAgent,
		@HeaderParam("UserName") String userName,
		@HeaderParam ("LoggedIn-User") String loggedInUser,		
		@DefaultValue ("1.0.0") @HeaderParam ("Api-Version") String apiVersion,
		@DefaultValue ("0") @HeaderParam ("offset") Integer offset,
		@DefaultValue ("0") @HeaderParam ("limit") Integer limit,
		@PathParam ("locationId") String locationId){

		VisitAdapter adapter = VisitAdapterFactory.getVisitAdapter(ServiceConstants.DATA_ADAPTER_NAME);
		com.ibm.json.java.JSONObject attributesValue = api.getSecurityAPI().getSecurityContext().getUserIdentity().getAttributes();
		String token = (String) attributesValue.get("token");
		String uName = (String) attributesValue.get("userName");
    	String authToken = URLUtility.getAuthToken(token,uName);
	    Response response = adapter.getSupportTeamMembersByVisitId(contentType, acceptLanguage, userAgent, loggedInUser, apiVersion, offset, limit, locationId,authToken);
	    return response;
		
	}
}

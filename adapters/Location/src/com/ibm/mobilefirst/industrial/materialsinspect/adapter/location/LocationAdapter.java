package com.ibm.mobilefirst.industrial.materialsinspect.adapter.location;

import javax.ws.rs.core.Response;

public interface LocationAdapter {


	Response getItemsByLocationId(String contentType, String acceptLanguage, String apiVersion, String userAgent, String loggedInUser,
			String locationId, Integer offset, Integer limit,String token);

	Response getMediaDescriptionByLocationId(String contentType, String acceptLanguage, String language,String apiVersion,
			String userAgent, String locationId,String userId, String loggedInUser, Integer offset, Integer limit,String token);

	Response updateNcr(String contentType, String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String ncrData,String token);

	Response updatePunchListItem(String contentType, String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String punchList,String token);

	Response updateIncident(String contentType, String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String incident,String token);

	Response getReportsDescriptionByLocationId(String contentType, String acceptLanguage, String apiVersion,
			String loggedInUser, String userAgent, String locationId,String userId, Integer offset, Integer limit,String token);

	Response getSingleNcrsByLocationId(String contentType,String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String userId, String visitId,String locationId, Integer offset,
			Integer limit, String authToken);

	Response getSinglePunchListItemByLocationId(String contentType,String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String userId, String visitId, Integer offset,
			Integer limit, String authToken);

	Response getSingleIncidentByLocationId(String contentType,String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String userId,String visitId, Integer offset, Integer limit,
			String authToken);

	Response getLocationByIdFromSingleSource(String contentType,String acceptLanguage, String apiVersion, String loggedInUser,
			String userAgent, String locationId, String authToken);

	Response updateNewNcr(String contentType, String acceptLanguage,String apiVersion, String loggedInUser, String userAgent,
			String ncrData, String authToken);

	Response updateNewIncident(String contentType, String acceptLanguage,String apiVersion, String loggedInUser, String userAgent,
			String ncrData, String authToken);

	Response updateNewPunchListItems(String contentType, String acceptLanguage,String apiVersion, String loggedInUser, String userAgent,
			String ncrData, String authToken);

	Response getNewMediaDescriptionByLocationId(String contentType,String acceptLanguage, String language, String apiVersion,String loggedInUser, String userAgent, String locationId,
			Integer offset, Integer limit, String authToken);

}

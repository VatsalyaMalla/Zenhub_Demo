package com.ibm.mobilefirst.industrial.materialsinspect.adapter.media;

import javax.ws.rs.core.Response;

public interface MediaAdapter {

	Response getMediaFileByMediaPath(String contentType, String acceptLanguage, String apiVersion, String userAgent,
			String mediaId,String mediaType, Integer offset, Integer limit,String token);

	Response updateMedia(String contentType, String acceptLanguage,
			String userAgent, String apiVersion, String loggedInUser,String userId,String visitId,
			String mediaData, String token);

	Response ncrMedia(String contentType, String acceptLanguage,
			String userAgent, String apiVersion, String loggedInUser,
			String userId, String visitId, String mediaData, String authToken);
	
}

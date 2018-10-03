package com.ibm.mobilefirst.industrial.materialsinspect.adapter.media.demo.integration;

import javax.ws.rs.core.Response;

import com.ibm.mobilefirst.industrial.materialsinspect.adapter.media.MediaAdapter;

 
public class MediaIntegrationAdapter implements MediaAdapter {

	@Override
	public Response getMediaFileByMediaPath(String contentType, String acceptLanguage, String apiVersion,
			String userAgent, String mediaId,String mediaType ,Integer offset, Integer limit,String token) {
		return null;
	}

	@Override
	public Response updateMedia(String contentType, String acceptLanguage,
			String userAgent, String apiVersion, String loggedInUser,String userId,String visitId,
			String mediaData, String token) {
		return null;
	}

	@Override
	public Response ncrMedia(String contentType, String acceptLanguage,
			String userAgent, String apiVersion, String loggedInUser,
			String userId, String visitId, String mediaData, String authToken) {
		return null;
	}

}

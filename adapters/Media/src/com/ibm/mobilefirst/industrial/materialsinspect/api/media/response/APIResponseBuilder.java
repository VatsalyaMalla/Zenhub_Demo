package com.ibm.mobilefirst.industrial.materialsinspect.api.media.response;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.core.Response;


public class APIResponseBuilder {
	
	public static Response sendSuccessResponse(String apiVersion, int code,String msg,Object jsonData) throws IOException {
		
		Response response = Response.status(code).entity(jsonData).build();
		
		List appVer = new ArrayList();
		List apiVer= new ArrayList();
		List msgs= new ArrayList();
		List ststus= new ArrayList();
		List codes= new ArrayList();
		
		Map map = response.getMetadata();
		
		apiVer.add(apiVersion);
		msgs.add(msg);
		ststus.add(code + msg);
		codes.add(code);
		
		map.put("Api-Version",apiVer);
		map.put("App-Version",appVer);
		map.put("Message",msgs);
		map.put("Status Code",ststus);
		map.put("Status",codes);
		
		return response;
	
	}
	
	public static Response sendFailResponse(String apiVersion, int resCode, String message, String id, String msg, String level) {
		
		JSONObject error = new JSONObject();
		JSONObject errorObject = new JSONObject();
		error.put("message", msg);
		error.put("id", id);
		error.put("level", level);
		JSONArray ar = new JSONArray();
		ar.add(error);
		errorObject.put("errors", ar);
		errorObject.put("data", new JSONArray());
		Response response = Response.status(resCode).entity(errorObject).build();
		
		List appVer = new ArrayList();
		List apiVer= new ArrayList();
		List msgs= new ArrayList();
		List status= new ArrayList();
		List codes= new ArrayList();
		
		Map map = response.getMetadata();
		
		apiVer.add(apiVersion);
		msgs.add(message);
		status.add(resCode + message);
		codes.add(resCode);
		
		map.put("Api-Version",apiVer);
		map.put("App-Version",appVer);
		map.put("Message",msgs);
		map.put("Status Code",codes);
		map.put("Status",status);
		
		return response;
	
	}
	
	public static Response sendFailResponse(String apiVersion, String appVersion, int resCode, String message, String id, String msg, 
			String level, String respDesc) {
		
			JSONObject error = new JSONObject();
			JSONArray data = new JSONArray();
			error.put("message", msg);
			error.put("id", id);
			error.put("level", level);
			JSONArray ar = new JSONArray();
			ar.add(error);
			
			JSONObject errorResp = new JSONObject();
			errorResp.put("errors", ar);
			errorResp.put(respDesc, data);
			Response response = Response.status(resCode).entity(errorResp).build();
			
			List appVer = new ArrayList();
			List apiVer= new ArrayList();
			List msgs= new ArrayList();
			List status= new ArrayList();
			List codes= new ArrayList();
			
			Map map = response.getMetadata();
			
			apiVer.add(apiVersion);
			appVer.add(appVersion);
			msgs.add(message);
			status.add(resCode + message);
			codes.add(resCode);
			
			map.put("Api-Version",apiVer);
			map.put("App-Version",appVer);
			map.put("Message",msgs);
			map.put("Status Code",codes);
			map.put("Status",status);
		
		return response;
	
	}

}

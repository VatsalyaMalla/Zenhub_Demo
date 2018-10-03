package com.ibm.mobilefirst.industrial.materialsinspect.api.user;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.Utils.URLUtility;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.user.demo.data.UserDataAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.constants.ServiceConstants;

public class UserAdapterLogicExtension {
	static Logger logger = Logger.getLogger(UserAdapterLogicExtension.class.getName());
	
	public static List<JSONObject> getUserInfoList(UserByUserIdSORData[] userObj){
		List<JSONObject> userInfo = new ArrayList<>();
		JSONObject dataObject;
		try{
			for(int i=0;i<userObj.length;i++){
				dataObject = new JSONObject();
				dataObject.put(ServiceConstants.FIRST_NAME,null!= userObj[i].getName()?userObj[i].getName():null);
				dataObject.put(ServiceConstants.LAST_NAME, null!= userObj[i].getSurName()?userObj[i].getSurName():null);
				dataObject.put(ServiceConstants.PHONE,null!= userObj[i].getPhone()?userObj[i].getPhone():null );
				dataObject.put(ServiceConstants.PHOTO, null!= userObj[i].getPhoto()?userObj[i].getPhoto():null);
				dataObject.put(ServiceConstants.USER_ID,userObj[i].getId_user().toString());
				dataObject.put(ServiceConstants.EMAIL, null!= userObj[i].getEmail()?userObj[i].getEmail():null);
				dataObject.put(ServiceConstants.COMPANY, ServiceConstants.IBM);
				dataObject.put(ServiceConstants.ROLE, ServiceConstants.INSPECTOR);
				userInfo.add(dataObject);
				}
		}catch(Exception e){
			printExceptions(e,ServiceConstants.GET_USER_INFO_LIST);
		}
		return userInfo;
	}
	
	
	public static List<JSONObject> getUserObjectForRequestById(UserRequestByIdSORData[] userObjGetRequestByID){
		List<JSONObject> userRequestsResp = new ArrayList<>();
		JSONObject responseStructure;
		String proposedDate;
		String eventPlannedDate;
		try{
			for(int i=0;i<userObjGetRequestByID.length;i++){
				logger.severe("userObjGetRequestByID.length :" + userObjGetRequestByID.length);
				responseStructure = new JSONObject();
				responseStructure.put(ServiceConstants.COMMENTS, null);
				responseStructure.put(ServiceConstants.DATE, userObjGetRequestByID[i].getEVENT_PLANNED_DATE()); 
				responseStructure.put(ServiceConstants.END_DATE, userObjGetRequestByID[i].getEVENT_END_DATE());
				responseStructure.put(ServiceConstants.STREET_ADDRESS, userObjGetRequestByID[i].getLOCATION_ADDRESS()); 
				responseStructure.put(ServiceConstants.DESCRIPTOR_2_VALUE, userObjGetRequestByID[i].getEXPEDITION_LEVEL());
				responseStructure.put(ServiceConstants.DISPLAY_NAME, userObjGetRequestByID[i].getCODE());
				responseStructure.put(ServiceConstants.MANUFACTURER_NAME, userObjGetRequestByID[i].getMANUFACTURER_NAME());
				responseStructure.put(ServiceConstants.DURATION_UNITS, "days"); // Hardcoded
				responseStructure.put(ServiceConstants.EXPECTED_DURATION, userObjGetRequestByID[i].getDURATION());
				responseStructure.put(ServiceConstants.DESCRIPTOR_2_LABEL, ServiceConstants.EXPEDITING_LEVEL);
				responseStructure.put(ServiceConstants.VENDOR_NAME, userObjGetRequestByID[i].getVENDOR_NAME());
				responseStructure.put(ServiceConstants.VISIT_ID, userObjGetRequestByID[i].getID_EVENT());
				responseStructure.put(ServiceConstants.DESCRIPTOR_1_LABEL, ServiceConstants.INSPECTION_LEVEL);
				responseStructure.put(ServiceConstants.DESCRIPTOR_1_VALUE, userObjGetRequestByID[i].getINSPECTION_LEVEL());
				responseStructure.put(ServiceConstants.STATUS, userObjGetRequestByID[i].getSTATE());
				responseStructure.put(ServiceConstants.PROPOSED_DATE,userObjGetRequestByID[i].getPROPOSED_DATE());
				userRequestsResp.add(responseStructure);
				}
		}catch(Exception e){
			printExceptions(e,ServiceConstants.USER_OBJECT_FOR_REQUEST);
		}
		return userRequestsResp;
	}
	private static String getDate(String proposedDate) {
		String proposedDateTrim=null;
		try{
			if(null!=proposedDate && !proposedDate.isEmpty()){
				proposedDateTrim = proposedDate.substring(0, 10);
			}
		}catch(Exception e){
			printExceptions(e,"getDate");
		}
		return proposedDateTrim;
	}

	public JSONObject sendNotificationVisitRefuseRequest(JSONArray sorResponseList,
			JSONObject dataObject,String token) {
		JSONObject responseMessage = new JSONObject();
		JSONObject data = new JSONObject();
		List<JSONObject> dataList = new ArrayList<>();
		JSONObject finalObject = new JSONObject();
		JSONObject sorRequestjson = new JSONObject();
		try{
			String iosVisit = (String) dataObject.get("visitId");
			for(int i=0;i<sorResponseList.size();i++){
				JSONObject sorObjectForRefuse = (JSONObject) sorResponseList.get(i);
				Object sorIdevent=sorObjectForRefuse.get("ID_EVENT");
				Object sorIdForm = sorObjectForRefuse.get("ID_FORM");
				if(null !=iosVisit && null!=sorIdevent.toString()){
					if(iosVisit.equalsIgnoreCase(sorIdevent.toString())){
						String iosProposedDate = (String) dataObject.get("proposedDate");
						String iosComments = (String) dataObject.get("comments");
						sorRequestjson.put("PROPOSED_DATE", iosProposedDate);
						sorRequestjson.put("REASON", iosComments);
						sorRequestjson.put("ID_EVENT", sorIdevent.toString());
						sorRequestjson.put("ID_FORM", sorIdForm.toString());
						logger.severe("SORrequestjson value after update :" + sorRequestjson);
						String refuseResponse = URLUtility.getSORDataWithHeaderParamsForReschedulePUT(URLUtility.getRequiredUrl(ServiceConstants.RESHCEDULE_VISIT_REQUEST_URL),sorRequestjson.toString(),token); 
						refuseResponse=setEmptyMessageResponse(refuseResponse);
						if(null!=refuseResponse && !refuseResponse.isEmpty()){
							responseMessage = (JSONObject) JSONValue.parse(refuseResponse);
						}
						String code = (String) responseMessage.get("id");
						if("200".equalsIgnoreCase(code)){
							data.put("message", "SOR updated successfully for notification refuse.");
							data.put("id", "200");
							dataList.add(data);
							finalObject.put("data", dataList);
							finalObject.put("errors", new JSONArray());
						}else if("403".equalsIgnoreCase(code)){
							data.put("message", "Authorization not valid.Please log in again.");
							data.put("id", "403");
							data.put("level", "ERROR");
							dataList.add(data);
							finalObject.put("data", new JSONArray());
							finalObject.put("errors", dataList);
						}else{
							data.put("message", "SOR update failed for notification refuse.");
							data.put("id", code);
							data.put("level", "ERROR");
							dataList.add(data);
							finalObject.put("data", new JSONArray());
							finalObject.put("errors", dataList);
						}
					}
				}
			}
		}catch(Exception e){
			printException(e,"sendNotificationRefuseRequest");
		}
		return finalObject;
	}

	public JSONObject sendNotificationRefuseRequest(JSONArray sorResponseList,
			JSONObject dataObject,String token) {
		JSONObject responseMessage = new JSONObject();
		JSONObject data = new JSONObject();
		List<JSONObject> dataList = new ArrayList<>();
		JSONObject finalObject = new JSONObject();
		try{
			String iosVisit = (String) dataObject.get("visitId");
			for(int i=0;i<sorResponseList.size();i++){
				JSONObject sorObjectForRefuse = (JSONObject) sorResponseList.get(i);
				Object sorIdevent=sorObjectForRefuse.get("ID_EVENT");
				if(null !=iosVisit && null!=sorIdevent.toString()){
					if(iosVisit.equalsIgnoreCase(sorIdevent.toString())){
						String iosProposedDate = (String) dataObject.get("proposedDate");
						String iosComments = (String) dataObject.get("comments");
						sorObjectForRefuse.put("PROPOSED_DATE", iosProposedDate);
						sorObjectForRefuse.put("REASON", iosComments);
						logger.severe("SORObjectForRefuse value after update :" + sorObjectForRefuse);
						String refuseResponse = URLUtility.getSORDataWithHeaderParamsForPost(URLUtility.getRequiredUrl(ServiceConstants.RESHCEDULE_REQUEST_URL),sorObjectForRefuse.toString(),token);
						refuseResponse=setEmptyMessageResponse(refuseResponse);
						if(null!=refuseResponse && !refuseResponse.isEmpty()){
							responseMessage = (JSONObject) JSONValue.parse(refuseResponse);
						}
						String code = (String) responseMessage.get("id");
						if("200".equalsIgnoreCase(code)){
							data.put("message", "SOR updated successfully for notification refuse.");
							data.put("id", "200");
							dataList.add(data);
							finalObject.put("data", dataList);
							finalObject.put("errors", new JSONArray());
						}else if("403".equalsIgnoreCase(code)){
							data.put("message", "Authorization not valid.Please log in again.");
							data.put("id", "403");
							data.put("level", "ERROR");
							dataList.add(data);
							finalObject.put("data", new JSONArray());
							finalObject.put("errors", dataList);
						}else{
							data.put("message", "SOR update failed for notification refuse.");
							data.put("id", code);
							data.put("level", "ERROR");
							dataList.add(data);
							finalObject.put("data", new JSONArray());
							finalObject.put("errors", dataList);
						}
					}
				}
			}
		}catch(Exception e){
			printException(e,"sendNotificationRefuseRequest");
		}
		return finalObject;
	}
	public JSONObject sendNotificationAcceptRequest(JSONArray sorResponseList,JSONObject dataObject,String token){
		String iosVisit = (String) dataObject.get("visitId");
		JSONObject jsonRequest = new JSONObject();
		JSONObject data = new JSONObject();
		List<JSONObject> dataList = new ArrayList<>();
		JSONObject finalObject = new JSONObject();
		try{
			for(int i=0;i<sorResponseList.size();i++){
				JSONObject sorObjectForAccept = (JSONObject) sorResponseList.get(i);
				Object sorIdevent=sorObjectForAccept.get("ID_EVENT");
				if(null !=iosVisit && null!=sorIdevent.toString()){
					if(iosVisit.equalsIgnoreCase(sorIdevent.toString())){
						String iosDate = (String) dataObject.get("date");
						sorObjectForAccept.put("EVENT_PLANNED_DATE", iosDate);
						logger.severe("sorObjectForAccept value after update :" + sorObjectForAccept);
						String acceptResponse = URLUtility.getSORDataWithHeaderParamsForPost(URLUtility.getRequiredUrl(ServiceConstants.ACCEPT_REQUEST_URL),sorObjectForAccept.toString(),token);
						acceptResponse=setEmptyMessageResponse(acceptResponse);
						if(null!=acceptResponse && !acceptResponse.isEmpty()){
						jsonRequest = (JSONObject) JSONValue.parse(acceptResponse);
						}
						String code = (String) jsonRequest.get("id");
						if("200".equalsIgnoreCase(code)){
							data.put("message", "SOR updated successfully for notification accept.");
							data.put("id", "200");
							dataList.add(data);
							finalObject.put("data", dataList);
							finalObject.put("errors", new JSONArray());
						}else if("403".equalsIgnoreCase(code)){
							data.put("message", "Authorization not valid.Please log in again.");
							data.put("id", "403");
							data.put("level", "ERROR");
							dataList.add(data);
							finalObject.put("data", new JSONArray());
							finalObject.put("errors", dataList);
						}else{
							data.put("message", "SOR update failed for notification accept.");
							data.put("id", code);
							data.put("level", "ERROR");
							dataList.add(data);
							finalObject.put("data", new JSONArray());
							finalObject.put("errors", dataList);
						}
					}else{
						logger.severe("Visit id's are not equal");
					}
				}else{
					logger.info("Visit id in iOS request or SOR is empty.");
				}
			}
		}catch(Exception e){
			printException(e,"sendNotificationAcceptRequest");
		}
		return finalObject;
	}
	
	public String setEmptyMessageResponse(String response){
		try{
			if(null==response || response.isEmpty()){
				response = ServiceConstants.RESPONSE_MESSAGE_EMPTY;
			}
		}catch(Exception e){
			printException(e,"setEmptyMessageResponse");
		}
		return response;
	}
	void printException(Exception e,String methodName) {
		logger.severe("Exception in "+methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}
	
	static void printExceptions(Exception e,String methodName) {
		logger.severe("Exception in "+methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}
}

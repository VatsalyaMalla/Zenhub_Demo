package com.ibm.mobilefirst.industrial.materialsinspect.adapter.visit.demo.data;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Utils.URLUtility;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.visit.VisitAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.ContactVendorSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.CoordinatorExpeditionSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.CoordinatorInspectionSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.StaffElement;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.Vendor;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.VisitResource;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.VisitTeamMembersSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.response.APIResponseBuilder;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.exception.AdapterExceptionHandler;


public class VisitDataAdapter implements VisitAdapter {

	static Logger logger = Logger.getLogger(VisitResource.class.getName());

	AdapterExceptionHandler  adapterExceptionHandler = new AdapterExceptionHandler();
	
	@Override
	public Response getSupportTeamMembersByVisitId(String contentType, String acceptLanguage, String userAgent, String loggedInUser,
			String apiVersion, Integer offsetValue, Integer limitValue, String locationId,String token) {
		Response response = null;
		List<JSONObject> visitTeamMembersResp = new ArrayList<>();
		JSONObject result = new JSONObject();
		JSONObject visitResponse;
		List<JSONObject> teamMembersArray = new ArrayList<>();
		
		try{	
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			String[] locations = locationId.split(ServiceConstants.COMMA);
            for(int i = 0; i<locations.length; i++){
            	teamMembersArray = new ArrayList<>();
            	visitResponse = new JSONObject();
				String sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.SUPPORTING_TEAMS)+locations[i],token); 
				if(sorOutputString.contains(ServiceConstants.EXCEPTION_TYPE)){
					response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
					return response;
				}else if(sorOutputString.contains("ClassName")){
					response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
					return response;
				}
				VisitTeamMembersSORData visitTeamObj = mapper.readValue(sorOutputString, VisitTeamMembersSORData.class);
				CoordinatorExpeditionSORData expeditionSORObj= visitTeamObj.getCoordinator_expedition();
				ContactVendorSORData contVendorObj = visitTeamObj.getContact_vendor();
				CoordinatorInspectionSORData inspectionSORObj = visitTeamObj.getCoordinator_inspection();
				StaffElement[] staffElementObj = visitTeamObj.getStaff();
				Vendor vendorObj=visitTeamObj.getVendor();
				getTeamArrayFormStaff(staffElementObj,teamMembersArray);
				getTeamArrayFromVendor(vendorObj,teamMembersArray);
				getTeamArrayFromExpedition(expeditionSORObj,teamMembersArray);
				getTeamArrayFromInspection(inspectionSORObj,teamMembersArray);
				getTeamArrayFromContact(contVendorObj,teamMembersArray);
			    
			    visitResponse.put(ServiceConstants.TEAM_MEMBERS, teamMembersArray);
			    visitResponse.put(ServiceConstants.ASSOCIATED_LOCATION_ID,locations[i]);
			    visitTeamMembersResp.add(visitResponse);
			}
            
            Integer offset = getOffsetValue(offsetValue, visitTeamMembersResp);
            Integer limit = getLimitValue(limitValue, visitTeamMembersResp);

			try {
				visitTeamMembersResp = visitTeamMembersResp.subList(offset, offset+ limit);
			} catch (Exception e) {
				printException(e,ServiceConstants.RESPONSE_LIST_OF_VISIT);
				limit = visitTeamMembersResp.size();
				visitTeamMembersResp = visitTeamMembersResp.subList(offset, limit);

			}
			
			JSONObject pagination = new JSONObject();
			pagination.put(ServiceConstants.SIZE,limit);
			pagination.put(ServiceConstants.OFFSET,offset);
			
			result.put(ServiceConstants.PAGINATION, pagination);
			result.put(ServiceConstants.DATA, visitTeamMembersResp);
			result.put(ServiceConstants.ERRORS, new JSONArray());
			
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK,
					ServiceConstants.SUCCESS_MESSAGE,result);
		}catch(UnknownHostException uhe){
			printException(uhe,ServiceConstants.METHOD_NAME);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
					ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e,ServiceConstants.METHOD_NAME);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(),
					ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}
	
	private Integer getLimitValue(Integer limit, List<JSONObject> visitTeamMembersResp) {
		return (limit == -1 || limit == 0) ? visitTeamMembersResp.size() : limit;
	}

	private Integer getOffsetValue(Integer offset, List<JSONObject> visitTeamMembersResp) {
		return (offset == -1 || offset>=visitTeamMembersResp.size()) ? 0 : offset;
	}

	private void getTeamArrayFromContact(ContactVendorSORData contVendorObj,
			List<JSONObject> teamMembersArray) {
		try{
			if(null!=contVendorObj){
				
				JSONObject contactVendorObject = new JSONObject();
				contactVendorObject.put(ServiceConstants.EMAIL, null!=contVendorObj.getEMAIL()?contVendorObj.getEMAIL():null);
				contactVendorObject.put(ServiceConstants.FIRST_NAME, null!=contVendorObj.getNAME()?contVendorObj.getNAME():null);
				contactVendorObject.put(ServiceConstants.LAST_NAME, null);
				contactVendorObject.put(ServiceConstants.PHONE, null!=contVendorObj.getPHONE()?contVendorObj.getPHONE():null);
				contactVendorObject.put(ServiceConstants.ROLE, ServiceConstants.CONTACT_VENDOR);
				contactVendorObject.put(ServiceConstants.DISPLAY_ORDER, ServiceConstants.THREE);
				contactVendorObject.put(ServiceConstants.USER_ID, null);
				contactVendorObject.put(ServiceConstants.PHOTO, null);
				contactVendorObject.put(ServiceConstants.COMPANY, null);
		    teamMembersArray.add(contactVendorObject);
			}
		}catch(Exception e){
			printException(e,ServiceConstants.TEAM_ARRAY_FROM_CONTACT);
		}
		
	}

	private void getTeamArrayFromInspection(
			CoordinatorInspectionSORData inspectionSORObj,
			List<JSONObject> teamMembersArray) {
		try{
			if(null!=inspectionSORObj){
				JSONObject inspectionObject = new JSONObject();
				inspectionObject.put(ServiceConstants.EMAIL, null!=inspectionSORObj.getEMAIL()?inspectionSORObj.getEMAIL():null);
				inspectionObject.put(ServiceConstants.FIRST_NAME, null!=inspectionSORObj.getNAME()?inspectionSORObj.getNAME():null);
				inspectionObject.put(ServiceConstants.LAST_NAME, null!=inspectionSORObj.getSURNAME()?inspectionSORObj.getSURNAME():null);
				inspectionObject.put(ServiceConstants.PHONE, null!=inspectionSORObj.getPHONE()?inspectionSORObj.getPHONE():null);
				inspectionObject.put(ServiceConstants.ROLE, ServiceConstants.COORDINATOR_INSPECTION);
				inspectionObject.put(ServiceConstants.DISPLAY_ORDER, ServiceConstants.ONE);
				inspectionObject.put(ServiceConstants.USER_ID, inspectionSORObj.getID_USER().toString());
				inspectionObject.put(ServiceConstants.PHOTO, null!=inspectionSORObj.getPHOTO()?inspectionSORObj.getPHOTO():null);
				inspectionObject.put(ServiceConstants.COMPANY, null);
			    teamMembersArray.add(inspectionObject);
			}
		}catch(Exception e){
			printException(e,ServiceConstants.TEAM_ARRAY_FROM_INSPECTION);
		}
	}

	private void getTeamArrayFromExpedition(
			CoordinatorExpeditionSORData expeditionSORObj,
			List<JSONObject> teamMembersArray) {
		try{
			if(null!=expeditionSORObj){
				JSONObject expeditionObject = new JSONObject();
				expeditionObject.put(ServiceConstants.EMAIL, null!=expeditionSORObj.getEMAIL()?expeditionSORObj.getEMAIL():null);
				expeditionObject.put(ServiceConstants.FIRST_NAME, null!=expeditionSORObj.getNAME()?expeditionSORObj.getNAME():null);
				expeditionObject.put(ServiceConstants.LAST_NAME, null!=expeditionSORObj.getSURNAME()?expeditionSORObj.getSURNAME():null);
				expeditionObject.put(ServiceConstants.PHONE, null!=expeditionSORObj.getPHONE()?expeditionSORObj.getPHONE():null);
				expeditionObject.put(ServiceConstants.ROLE, ServiceConstants.OFFICE_EXPEDITOR);
				expeditionObject.put(ServiceConstants.DISPLAY_ORDER, ServiceConstants.TWO);
				expeditionObject.put(ServiceConstants.USER_ID, expeditionSORObj.getID_USER().toString());
				expeditionObject.put(ServiceConstants.PHOTO, null!=expeditionSORObj.getPHOTO()?expeditionSORObj.getPHOTO():null);
				expeditionObject.put(ServiceConstants.COMPANY, null);
			    teamMembersArray.add(expeditionObject);
			}
		}catch(Exception e){
			printException(e,ServiceConstants.TEAM_ARRAY_FROM_EXPEDITION);
		}
	}

	private void getTeamArrayFromVendor(Vendor vendorObj,
			List<JSONObject> teamMembersArray) {
		try{
			if(null!=vendorObj){
			JSONObject teamMembersVendorObject = new JSONObject();
			teamMembersVendorObject.put(ServiceConstants.EMAIL, null!=vendorObj.getEmail()?vendorObj.getEmail():null);
			teamMembersVendorObject.put(ServiceConstants.FIRST_NAME, null!=vendorObj.getName()?vendorObj.getName():null);
			teamMembersVendorObject.put(ServiceConstants.LAST_NAME, null);
			teamMembersVendorObject.put(ServiceConstants.PHONE, null!=vendorObj.getPhone()?vendorObj.getPhone():null);
			teamMembersVendorObject.put(ServiceConstants.ROLE, ServiceConstants.VENDOR);
			teamMembersVendorObject.put(ServiceConstants.USER_ID, vendorObj.getId_vendor().toString());
			teamMembersVendorObject.put(ServiceConstants.PHOTO, null);
			teamMembersVendorObject.put(ServiceConstants.COMPANY, null);
		    teamMembersArray.add(teamMembersVendorObject);
			}
		}catch(Exception e){
			printException(e,ServiceConstants.TEAM_ARRAY_FROM_VENDOR);
		}
	}

	private void getTeamArrayFormStaff(StaffElement[] staffElementObj,
			List<JSONObject> teamMembersArray) {
		JSONObject teamMembersObject;
		try{
			if(null!=staffElementObj && staffElementObj.length!=0){
				for(int j=0;j<staffElementObj.length;j++){
				    teamMembersObject = new JSONObject();
				    teamMembersObject.put(ServiceConstants.EMAIL, setNullStringValue(staffElementObj[j].getEmail()));
				    teamMembersObject.put(ServiceConstants.FIRST_NAME, setEmptyStringValue(staffElementObj[j].getName()));
				    teamMembersObject.put(ServiceConstants.LAST_NAME, null);
				    teamMembersObject.put(ServiceConstants.PHONE, setEmptyStringValue(staffElementObj[j].getPhone()));
				    teamMembersObject.put(ServiceConstants.ROLE,setEmptyStringValue(staffElementObj[j].getPosition()));
				    teamMembersObject.put(ServiceConstants.DISPLAY_ORDER, ServiceConstants.FOUR);
				    teamMembersObject.put(ServiceConstants.USER_ID, staffElementObj[j].getId().toString());
				    teamMembersObject.put(ServiceConstants.COMPANY, null);
				    teamMembersObject.put(ServiceConstants.PHOTO, setNullStringValue(staffElementObj[j].getPhoto()));
				    teamMembersArray.add(teamMembersObject);
				}
		 }
		}catch(Exception e){
			printException(e,ServiceConstants.TEAM_ARRAY_FROM_STAFF);
		}
	}
	private String setNullStringValue(String value){
		String s=null;
		try{
			if(null!=value && !value.isEmpty()){
				s=value;
			}
		}catch(Exception e){
			printException(e,ServiceConstants.SET_NULL);
		}
		return s;
	}
	
	private String setEmptyStringValue(String value){
		String s="";
		try{
			if(null!=value && !value.isEmpty()){
				s=value;
			}
		}catch(Exception e){
			printException(e,ServiceConstants.SET_EMPTY);
		}
		return s;
	}

	void printException(Exception e,String methodName) {
		logger.severe(ServiceConstants.EXCEPTION+ methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}
}
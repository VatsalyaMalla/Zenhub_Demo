

package com.ibm.mobilefirst.industrial.materialsinspect.adapter.user.demo.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.Utils.URLUtility;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.user.UserAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.Answer;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.ListCards;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.ListCategories;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.ListPossibleAnswers;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.ListQuestions;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.ListSections;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.PossibleSections;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.PostVisitforUser;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.SectionTemplate;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.UserAdapterLogicExtension;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.UserByUserIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.UserRequestByIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.VisitSkeleton;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.VisitsByIDBean;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.exception.AdapterExceptionHandler;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.response.APIResponseBuilder;

public class UserDataAdapter implements UserAdapter {
	static Logger logger = Logger.getLogger(UserDataAdapter.class.getName());
	static Properties properties = null;
	AdapterExceptionHandler  adapterExceptionHandler= new AdapterExceptionHandler();
	
	@Override
	public Response getUserById(String contentType, String acceptLanguage, String userAgent, String apiVersion,
			Integer offset, Integer limit, String userId,String token) {
		Response response = null;
		JSONObject result = new JSONObject();
		List<JSONObject> userInfoList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try{	
			if(null == userId || userId.isEmpty()){
				response = APIResponseBuilder.sendFailResponse(
						apiVersion, ServiceConstants.USER_ID_CHECK_CODE, ServiceConstants.SUCCESS_MESSAGE, 
						ServiceConstants.ERROR_CODE_NODATAFOUND, ServiceConstants.ERROR_USER_NOTFOUND,
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			String sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.USER_INFO_URL)+userId,token); 
			if(sorOutputString.contains(ServiceConstants.EXCEPTION_TYPE)){
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
				return response;
			}else if(sorOutputString.contains("ClassName")){
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
				return response;
			}
			UserByUserIdSORData[] userObj = mapper.readValue(sorOutputString, UserByUserIdSORData[].class);
			if(null==userObj || userObj.length==0){
				response = APIResponseBuilder.sendFailResponse(
						apiVersion, ServiceConstants.USER_ID_CHECK_CODE, ServiceConstants.SUCCESS_MESSAGE, 
						ServiceConstants.ERROR_CODE_NODATAFOUND, ServiceConstants.USER_INFO_EMPTY,
						ServiceConstants.LEVEL_ERROR);
				return response;
				}else{
					userInfoList = UserAdapterLogicExtension.getUserInfoList(userObj);
				}
			if(null==userInfoList || userInfoList.isEmpty()){
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						400,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						"400", "SOR data issue in getUserById",
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			result.put(ServiceConstants.DATA, userInfoList);
			result.put(ServiceConstants.ERRORS, new JSONArray());
			
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK,
					ServiceConstants.SUCCESS_MESSAGE,result);
		}catch(UnknownHostException uhe){
			    printException(uhe,ServiceConstants.GET_USER);
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						ServiceConstants.ERROR_CODE_ERROR,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
						ServiceConstants.LEVEL_ERROR);
		}catch(Exception e) {
			printException(e,ServiceConstants.GET_USER);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.USER_ID_CHECK_MESSAGE, ServiceConstants.SOR_USERID_MESSAGE,
					ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}
	
	@Override
	public Response getRequestById(String contentType, String acceptLanguage, String loggedInUser, String userAgent, String apiVersion,
			Integer offset, Integer limit, String userId,String token) {

		Response response = null;
		JSONObject result = new JSONObject();
		JSONObject pagination = new JSONObject();
		List<JSONObject> userRequestsResp = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try{
			String sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_1)+userId+URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_2),token); 
			if(sorOutputString.contains(ServiceConstants.EXCEPTION_TYPE)){
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
				return response;
			}else if(sorOutputString.contains("ClassName")){
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
				return response;
			}
			UserRequestByIdSORData[] userObjGetRequestByID = mapper.readValue(sorOutputString, UserRequestByIdSORData[].class);
			if(null != userObjGetRequestByID && userObjGetRequestByID.length!=0){
				logger.severe("Inside . . .");
				userRequestsResp = UserAdapterLogicExtension.getUserObjectForRequestById(userObjGetRequestByID);
			} 
			logger.severe("userObjGetRequestByID value :" + userObjGetRequestByID);
			
			if(limit == -1 || limit == 0)
				limit = userRequestsResp.size();

			try {
				userRequestsResp = userRequestsResp.subList(offset, offset+ limit);
			} catch (Exception e) {
				printException(e,"Exception in nested try block of getRequestById");
				limit = userRequestsResp.size();
				userRequestsResp = userRequestsResp.subList(offset, limit);
			}
			pagination.put(ServiceConstants.SIZE,limit);
			pagination.put(ServiceConstants.OFFSET,offset);
				
			result.put(ServiceConstants.DATA, userRequestsResp);
			result.put(ServiceConstants.ERRORS, new JSONArray());
			result.put(ServiceConstants.PAGINATION, pagination);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK,
					ServiceConstants.SUCCESS_MESSAGE,result);
		}catch(UnknownHostException uhe){
		    printException(uhe,ServiceConstants.GET_USER);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
					ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e,ServiceConstants.REQUEST_BY_ID);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.USER_ID_CHECK_MESSAGE, e.getMessage(),
					ServiceConstants.LEVEL_ERROR);
		}
		return response;		
	}
	private List<JSONObject> getCategoriesList(
			ListCategories[] catagories,List<JSONObject> catagoriesJSONArray,String visitId) {
		try{
			if(null!=catagories && catagories.length!=0){
				List<JSONObject> activitiesJSONArray = new ArrayList<>();
				for(int catagoriesLoopVar=0;catagoriesLoopVar<catagories.length;catagoriesLoopVar++){
				JSONObject catagoriesJSONObject = new JSONObject();
				String displayOrder = catagories[catagoriesLoopVar].getDisplayOrder();
				int intDisplayOrder = getIntegerValue(displayOrder);
				String type = catagories[catagoriesLoopVar].getType();
				if(!("NCR".equalsIgnoreCase(type) || "Incidents".equalsIgnoreCase(type)|| "Punch List".equalsIgnoreCase(type))){
					catagoriesJSONObject.put("displayName", catagories[catagoriesLoopVar].getDisplayName());
					catagoriesJSONObject.put("displayOrder",intDisplayOrder);
					catagoriesJSONObject.put("categoryId",catagories[catagoriesLoopVar].getExternalId());
					ListCards[] activitiesListObject = catagories[catagoriesLoopVar].getListCards();
					setActivitiesArray(activitiesListObject,activitiesJSONArray,catagoriesJSONArray,catagoriesJSONObject,visitId);
				}
			}
		}
		}catch(Exception e){
			printException(e,"getCategoriesList");
		}
		return catagoriesJSONArray;
	}


	private void setActivitiesArray(ListCards[] activitiesListObject,
			List<JSONObject> activitiesJSONArray,
			List<JSONObject> catagoriesJSONArray,JSONObject catagoriesJSONObject,String visitId) {
		try{
			if(null!=activitiesListObject && activitiesListObject.length!=0){
				 activitiesJSONArray = setActivitesforCatagories(activitiesListObject,visitId);
				 catagoriesJSONObject.put("activities", activitiesJSONArray);
				 catagoriesJSONArray.add(catagoriesJSONObject);
			}else{
				 catagoriesJSONObject.put("activities", new JSONArray());
				 catagoriesJSONArray.add(catagoriesJSONObject);
			}
		}catch(Exception e){
			printException(e,"setActivitiesArray");
		}
		
	}
	private int getIntegerValue(String stringValue) {
		int intValue=0;
		try{
			if(null!=stringValue && !stringValue.isEmpty()){
				intValue = Integer.parseInt(stringValue);
				}
		}catch(Exception e){
			printException(e,"getIntegerValue");
		}
		return intValue;
	}

	@SuppressWarnings("unchecked")
	public List<JSONObject> setActivitesforCatagories(ListCards[] activitiesListObject,String visitId){
		List<JSONObject> activitesJSONArray = new ArrayList<>();
		JSONObject activitesJSONObject;
		boolean isDuplicable;
		boolean isRequired;
		boolean userCreated;
		for(int activitiesLoopVar=0;activitiesLoopVar<activitiesListObject.length;activitiesLoopVar++){
			activitesJSONObject = new JSONObject();
			activitesJSONObject.put(ServiceConstants.ADDITIONAL_INFORMATION,""!= activitiesListObject[activitiesLoopVar].getAdditionalInformation()?activitiesListObject[activitiesLoopVar].getAdditionalInformation():null);
			SectionTemplate[] sectionTemplate = activitiesListObject[activitiesLoopVar].getSectionTemplate();
			setSectionTemplate(sectionTemplate,activitesJSONObject,visitId);
			String table = activitiesListObject[activitiesLoopVar].getIsTable();
			//Section Template - Array of String - End
			activitesJSONObject.put("displayName",null!= activitiesListObject[activitiesLoopVar].getDisplayName()?activitiesListObject[activitiesLoopVar].getDisplayName():null);
			String displayOrder = activitiesListObject[activitiesLoopVar].getDisplayOrder();
			int intDisplayOrder = getIntegerValue(displayOrder);
			String duplicable = activitiesListObject[activitiesLoopVar].getIsDuplicable();
			isDuplicable = getRequiredBoolean(duplicable); 
			String userCreatedString = activitiesListObject[activitiesLoopVar].getUserCreated();
			userCreated = getRequiredBoolean(userCreatedString);
			String required = activitiesListObject[activitiesLoopVar].getIsRequired();
			isRequired = getRequiredBoolean(required);
			boolean isSkippable = getRequiredBoolean(activitiesListObject[activitiesLoopVar].getIsSkippable());
			setTypeValue(table,activitesJSONObject);
			activitesJSONObject.put("displayOrder",intDisplayOrder);
			activitesJSONObject.put("displayType",""!= activitiesListObject[activitiesLoopVar].getDisplayType()?activitiesListObject[activitiesLoopVar].getDisplayType():null);
			Object isModifiedField = activitiesListObject[activitiesLoopVar].getIsModified();
			boolean isModified = getIsModifiedValue(isModifiedField);
			activitesJSONObject.put("isModified",isModified);
			activitesJSONObject.put("duplicable", isDuplicable);
			activitesJSONObject.put("userCreated", userCreated);
			activitesJSONObject.put("isSkippable", isSkippable);
			activitesJSONObject.put("status", ""!=activitiesListObject[activitiesLoopVar].getState()?activitiesListObject[activitiesLoopVar].getState():null);
			activitesJSONObject.put("reason", ""!=activitiesListObject[activitiesLoopVar].getReason()?activitiesListObject[activitiesLoopVar].getReason():null);
			activitesJSONObject.put(ServiceConstants.REQUIRED,isRequired);
			PossibleSections[] possibleSections = activitiesListObject[activitiesLoopVar].getPossibleSections();
			List<JSONObject> possibleSectionsList = getPossibleSectionsList(possibleSections);
			//Possible Sections - Array of String - Starts
			setPossibleSections(possibleSectionsList,activitesJSONObject);
			//Possible Sections - Array of String - End
			activitesJSONObject.put("activityId", null!=activitiesListObject[activitiesLoopVar].getExternalId()?activitiesListObject[activitiesLoopVar].getExternalId():"");
			activitesJSONObject.put("parentCardId", ""!=activitiesListObject[activitiesLoopVar].getParentActivityId()?activitiesListObject[activitiesLoopVar].getParentActivityId():null);
			//Associated Item IDs - Array
			ListSections[] listSections = activitiesListObject[activitiesLoopVar].getListSections();
			List<String> associatedIds= getAssociatedItemsIds(listSections);
			activitesJSONObject.put("associatedItemsId", null!=associatedIds?associatedIds:new JSONArray());
			ListSections[] associatedSectionsListObject  = activitiesListObject[activitiesLoopVar].getListSections();
			setAssociatedSections(associatedSectionsListObject,activitesJSONObject,table,possibleSections);
			activitesJSONArray.add(activitesJSONObject);
		}
		
		return activitesJSONArray;
	}
	
	private boolean getIsModifiedValue(Object isModifiedField) {
		boolean isModified=false;
		try{
			if(null!=isModifiedField && !isModifiedField.toString().isEmpty()){
				if("1".equalsIgnoreCase(isModifiedField.toString())){
					isModified = true;
				}
			}
		}catch(Exception e){
			printException(e,"getIsModifiedValue");
		}
		return isModified;
	}

	private void setAssociatedSections(
			ListSections[] associatedSectionsListObject,
			JSONObject activitesJSONObject,String table,PossibleSections[] possibleSections) {
		List<JSONObject> associatedSectionJSONArray = new ArrayList<>();
		try{
			if(null==associatedSectionsListObject || associatedSectionsListObject.length==0){
				activitesJSONObject.put("associatedSections",new JSONArray());
			}else{
				associatedSectionJSONArray  = setAssociatedSectionsListforActivities(associatedSectionsListObject,table,possibleSections);
				activitesJSONObject.put("associatedSections",associatedSectionJSONArray);
		}
		}catch(Exception e){
			printException(e,"setAssociatedSections");
		}
	}
	// Get possible sections list inside activities
	private void setPossibleSections(
			List<JSONObject> possibleSectionsList,
			JSONObject activitesJSONObject) {
		try{
			if(null!=possibleSectionsList && !possibleSectionsList.isEmpty()){
            	activitesJSONObject.put("possibleSections", possibleSectionsList); // array activitiesListObject[activitiesLoopVar].getPossibleSections()
            }else{
            	activitesJSONObject.put("possibleSections",new JSONArray());
            }
		}catch(Exception e){
			printException(e,"setPossibleSections");
		}
		
	}
	private void setTypeValue(String table,
			JSONObject activitesJSONObject) {
		try{
			if(table=="true"){
				activitesJSONObject.put("type","table");
			}else if(table=="false"){
				activitesJSONObject.put("type",null);
			}
		}catch(Exception e){
				printException(e,"setSectionTemplate");
			}
		}
	private void setSectionTemplate(SectionTemplate[] sectionTemplate,
			JSONObject activitesJSONObject,String visitId) {
		try{
			if(null!=sectionTemplate && sectionTemplate.length!=0){
				JSONObject sectionTemplateOutput = createSectionTemplateResponse(sectionTemplate,visitId);
				activitesJSONObject.put("sectionTemplate", sectionTemplateOutput);
			}else{
				activitesJSONObject.put("sectionTemplate", new JSONObject());
			}
		}catch(Exception e){
			printException(e,"setSectionTemplate");
		}
	}
	private boolean getRequiredBoolean(String stringValue) {
		boolean isStringValue=false;
		try{
			if(stringValue=="true"){
				isStringValue = true;
			}else{
				isStringValue = false;
			}
		}catch(Exception e){
			printException(e,"getRequiredBoolean");
		}
		
		return isStringValue;
	}


	public List<JSONObject> getPossibleSectionsList(PossibleSections[] possibleSections){
		List<JSONObject> possibleSectionsList = new ArrayList<>();
		JSONObject setPossibleSectionsObject = new JSONObject();
		try{
			for(int c=0;c<possibleSections.length;c++){
				setPossibleSectionsObject = new JSONObject();
				setPossibleSectionsObject.put("displayLabel", possibleSections[c].getDisplayLabel());
				String displayOrder = possibleSections[c].getDisplayOrder();
				int intDisplayOrder = getIntegerValue(displayOrder);
				setPossibleSectionsObject.put("displayOrder", intDisplayOrder);
				String isRequired = possibleSections[c].getIsSelected();
				boolean required = getRequiredBoolean(isRequired);
				setPossibleSectionsObject.put("isRequired", required);
				possibleSectionsList.add(setPossibleSectionsObject);
			}
		}catch(Exception e){
			printException(e,"getPossibleSectionsList");
		}
		return possibleSectionsList;
	}
	
		// Get associatedItemIds key
		public List<String> getAssociatedItemsIds(ListSections[] listSectionObject){
			List<String> itemsIdList =  new ArrayList<>();
			try{
				for(int i=0;i<listSectionObject.length;i++){
					ListQuestions[] listQuestions = listSectionObject[i].getListQuestions();
					for(int j=0;j<listQuestions.length;j++){
						String type = listQuestions[j].getType();
						if("items".equalsIgnoreCase(type)){
							ListPossibleAnswers[] listPossibleAnswers = listQuestions[j].getListPossibleAnswers();
							itemsIdList = getItemsIdList(listPossibleAnswers,itemsIdList);
						}
					}
				}
			}catch(Exception e){
				printException(e,"getAssociatedItemsIds");
			}
			return itemsIdList;
		}
		
		private List<String> getItemsIdList(ListPossibleAnswers[] listPossibleAnswers,List<String> itemsIdList) {
			try{
				for(int k=0;k<listPossibleAnswers.length;k++){
					String displayLabel = listPossibleAnswers[k].getDisplayLabel();
					itemsIdList.add(displayLabel);
				}
			}catch(Exception e){
				printException(e,"getItemsIdList");
			}
			return itemsIdList;
		}


		public JSONObject createSectionTemplateResponse(SectionTemplate[] listSectionObject,String visitId){
			JSONObject associatedQuestionsJSONObject = new JSONObject();
			JSONObject setSectionJSONObject = new JSONObject();
			List<JSONObject> possibleAnswersArray;
			List<JSONObject> associatedQuestions = new ArrayList<>();
			try{
				for(int k=0;k<listSectionObject.length;k++){
					associatedQuestionsJSONObject = new JSONObject();
					possibleAnswersArray=new ArrayList<>();
					setSectionJSONObject = new JSONObject();
					String sectionRequired = listSectionObject[k].getIsRequired();
					boolean sectionIsRequired = getRequiredBoolean(sectionRequired);
					String diplayOrd = listSectionObject[k].getDisplayOrder();
					int intDisplayOrd = new Double(diplayOrd).intValue();
					setSectionJSONObject.put("defaultValue",""!=listSectionObject[k].getDefaultValue()?listSectionObject[k].getDefaultValue():null);
					setSectionJSONObject.put("displayLabel",null!=listSectionObject[k].getDisplayLabel()?listSectionObject[k].getDisplayLabel():""); 
					setSectionJSONObject.put("displayOrder",intDisplayOrd);// pending
					setSectionJSONObject.put("ghostValue",listSectionObject[k].getGhostValue());
					setSectionJSONObject.put("questionId",""!=listSectionObject[k].getQuestionId()?listSectionObject[k].getQuestionId()+visitId:null);
					setSectionJSONObject.put("required",sectionIsRequired);
					setSectionJSONObject.put("type",null!=listSectionObject[k].getType()?listSectionObject[k].getType():"");
					setSectionJSONObject.put("units",""!=listSectionObject[k].getUnits()?listSectionObject[k].getUnits():null);
					setSectionJSONObject.put("isEditable",getRequiredBoolean(listSectionObject[k].getIsEditable()));
					setSectionJSONObject.put("dependent",""!=listSectionObject[k].getDependent()?listSectionObject[k].getDependent():null);
					setSectionJSONObject.put("dependOn",""!=listSectionObject[k].getDependOn()?listSectionObject[k].getDependOn():null);
					setSectionJSONObject.put("condition",""!=listSectionObject[k].getCondition()?listSectionObject[k].getCondition():null);
					Answer answer = listSectionObject[k].getAnswer();
					setAnswerObject(answer,setSectionJSONObject);
					ListPossibleAnswers[] possibleAnswersSORObject = listSectionObject[k].getListPossibleAnswers();
					setPossibleAnswersForVisit(possibleAnswersSORObject,setSectionJSONObject,possibleAnswersArray);
					setSectionJSONObject.put("additionalInformation",""!=listSectionObject[k].getAdditionalInformation()?listSectionObject[k].getAdditionalInformation():null);
					associatedQuestions.add(setSectionJSONObject);
				}
				if(null==associatedQuestions || associatedQuestions.isEmpty()){
					return new JSONObject();
				}
				associatedQuestionsJSONObject.put("parentActivityId", null);
				associatedQuestionsJSONObject.put("associatedQuestions", associatedQuestions);
			}catch(Exception e){
				printException(e,"createSectionTemplateResponse");
			}
			return associatedQuestionsJSONObject;
		}
		private void setPossibleAnswersForVisit(
				ListPossibleAnswers[] possibleAnswersSORObject,
				JSONObject setSectionJSONObject,
				List<JSONObject> possibleAnswersArray) {
			try{
				if(null!=possibleAnswersSORObject && possibleAnswersSORObject.length!=0){
					possibleAnswersArray = getPossibleAnswersForSectionTemplate(possibleAnswersSORObject);
					setSectionJSONObject.put("possibleAnswers",possibleAnswersArray);
				}else{
					setSectionJSONObject.put("possibleAnswers",new JSONArray());
				}
			}catch(Exception e){
				printException(e,"setPossibleAnswersForVisit");
			}
			
		}
		private void setAnswerObject(Answer answer,
				JSONObject setSectionJSONObject) {
			try{
				if(null!=answer){
					String answerString = getCommaSeparatedAnswers(answer);
					setSectionJSONObject.put("answer",answerString); // pending
				}else{
					setSectionJSONObject.put("answer",null);
				}
			}catch(Exception e){
				printException(e,"setAnswerObject");
			}
			
		}
		public List<JSONObject> getPossibleAnswersForSectionTemplate(ListPossibleAnswers[] possibleAnswersSORObject){
			List<JSONObject> possibleAnswers=new ArrayList<>();
			boolean possibleIsRequired;
			JSONObject possibleAnswersJSONObject;
			try{
				for(int n=0;n<possibleAnswersSORObject.length;n++){
					possibleAnswersJSONObject = new JSONObject();
					String displayOrder = possibleAnswersSORObject[n].getDisplayOrder();
					int intDisplayOrder = getIntegerValue(displayOrder);
					String possibleRequired = possibleAnswersSORObject[n].getIsRequired();
					possibleIsRequired = getRequiredBoolean(possibleRequired); 
					possibleAnswersJSONObject.put("displayLabel",null!=possibleAnswersSORObject[n].getDisplayLabel()? possibleAnswersSORObject[n].getDisplayLabel():"");
					possibleAnswersJSONObject.put("displayOrder",intDisplayOrder);
					possibleAnswersJSONObject.put("value",null!=possibleAnswersSORObject[n].getValue()? possibleAnswersSORObject[n].getValue():"" );
					possibleAnswersJSONObject.put("isRequired",possibleIsRequired);
					possibleAnswers.add(possibleAnswersJSONObject);
					
				}
			}catch(Exception e){
				printException(e,"getPossibleAnswersForSectionTemplate");
			}
			return possibleAnswers;
		}
	
	public String getCommaSeparatedAnswers(Answer answer){
		String resultAnswers=null;
		try{
			String[] answerArray = answer.getValue();
			if (answerArray.length > 0) {
	            StringBuilder sb = new StringBuilder();

	            for (String s : answerArray) {
	                sb.append(s).append(",");
	            }
	            resultAnswers = sb.deleteCharAt(sb.length() - 1).toString();
	        }
		}catch(Exception e){
			printException(e,"getCommaSeparatedAnswers");
		}
		return resultAnswers;
	}
	@SuppressWarnings("unchecked")
	public List<JSONObject> setAssociatedSectionsListforActivities(ListSections[] associatedSectionsListObject,String table,PossibleSections[] possibleSections){
		List<JSONObject> associatedSectionsJSONArray = new ArrayList<>();
		JSONObject associatedSectionsJSONObject = new JSONObject();
		List<JSONObject> associatedQuestionsJSONArray = new ArrayList<>();
		try{
			for(int sectionsLoopVar=0;sectionsLoopVar<associatedSectionsListObject.length;sectionsLoopVar++){
				associatedSectionsJSONObject = new JSONObject();
				String displayOrder = associatedSectionsListObject[sectionsLoopVar].getDisplayOrder();
				int intDisplayOrder = getIntegerValue(displayOrder);
				ListQuestions[] associatedQuestionsListObject  = associatedSectionsListObject[sectionsLoopVar].getListQuestions();
				String displayName = associatedSectionsListObject[sectionsLoopVar].getDisplayName();
				displayName = getDisplayName(displayName,table,associatedQuestionsListObject,possibleSections);
				associatedSectionsJSONObject.put("displayName",displayName);
				associatedSectionsJSONObject.put("displayOrder",intDisplayOrder);
				associatedSectionsJSONObject.put("sectionId", null!=associatedSectionsListObject[sectionsLoopVar].getExternalId()?associatedSectionsListObject[sectionsLoopVar].getExternalId():null);
				if(null==associatedQuestionsListObject || associatedQuestionsListObject.length==0){
					associatedSectionsJSONObject.put("associatedQuestions", null);
				}else{
					associatedQuestionsJSONArray  = setAssociatedQuestionsforAssociatedSections(associatedQuestionsListObject);
					associatedSectionsJSONObject.put("associatedQuestions", associatedQuestionsJSONArray); // Possible Answer Array list of jsosn
				}
				associatedSectionsJSONArray.add(associatedSectionsJSONObject);
			}	
		}catch(Exception e){
			printException(e,"setAssociatedSectionsListforActivities");
		}
		return associatedSectionsJSONArray;
	}	
	private String getDisplayName(String displayName, String table,ListQuestions[] associatedQuestionsListObject,PossibleSections[] possibleSections) {
		String disName=null;
		try{
			if(null!=displayName && !displayName.isEmpty()){
				return displayName;
			}else if(table=="true"){
				disName = setDisplayName(associatedQuestionsListObject,possibleSections);
			}
			
		}catch(Exception e){
			printException(e,"getDisplayName");
		}
		return disName;
	}
	private String setDisplayName(ListQuestions[] associatedQuestionsListObject,PossibleSections[] possibleSections) {
		String displayName=null;
		try{
			for(int i=0;i<associatedQuestionsListObject.length;i++){
				String type = associatedQuestionsListObject[i].getType();
				Answer answer = associatedQuestionsListObject[i].getAnswer();
				if("informational".equalsIgnoreCase(type)){
					displayName = getDisplayNameFromPossibleSections(possibleSections,answer);
				}
			}
		}catch(Exception e){
			printException(e,"setDisplayName");
		}
		return displayName;
	}
	private String getDisplayNameFromPossibleSections(PossibleSections[] possibleSections,Answer answer) {
		String name = null;
		try{
			if(null!=answer && null!=possibleSections){
				name = compareValues(answer,possibleSections);
			}
		}catch(Exception e){
			printException(e,"getDisplayNameFromPossibleSections");
		}
		return name;
	}
	private String compareValues(Answer answer,
			PossibleSections[] possibleSections) {
		String name = null;
		List<String> displayLabel = new ArrayList<>();
		try{
			String[] answerValue = answer.getValue();
			for(int i=0;i<answerValue.length;i++){
				for(int j=0;j<possibleSections.length;j++){
					if(possibleSections[j].getInternalId().equalsIgnoreCase(answerValue[i])){
						displayLabel.add(possibleSections[j].getDisplayLabel());
					}
				}
			}
			name = getValues(name,displayLabel);
		}catch(Exception e){
			printException(e,"compareValues");
		}
		return name;
	}
	private String getValues(String name, List<String> displayLabel) {
		try{
			if (!displayLabel.isEmpty()) {
	            StringBuilder sb = new StringBuilder();

	            for (String s : displayLabel) {
	                sb.append(s).append(",");
	            }
	            
	           name  = sb.deleteCharAt(sb.length() - 1).toString();
	        }
		}catch(Exception e){
			printException(e,"getValues");
		}
		return name;
	}
	
	@SuppressWarnings("unchecked")
	public List<JSONObject> setAssociatedQuestionsforAssociatedSections(ListQuestions[] associatedQuestionsListObject){
		List<JSONObject> associatedQuestionsJSONArray = new ArrayList<>();
		JSONObject associatedQuestionsJSONObject;
		boolean associatedIsRequired;
		try{
			for(int questionsLoopVar=0;questionsLoopVar<associatedQuestionsListObject.length;questionsLoopVar++){
				List<JSONObject> possibleAnswersJSONArray;
				String displayOrd = associatedQuestionsListObject[questionsLoopVar].getDisplayOrder();
				associatedQuestionsJSONObject = new JSONObject();
				int i = getIntFromDouble(displayOrd);
				String associatedRequired = associatedQuestionsListObject[questionsLoopVar].getIsRequired();
				associatedIsRequired = getRequiredBoolean(associatedRequired); 
				associatedQuestionsJSONObject.put("defaultValue",null!= associatedQuestionsListObject[questionsLoopVar].getDefaultValue()?associatedQuestionsListObject[questionsLoopVar].getDefaultValue():null);
				associatedQuestionsJSONObject.put("displayLabel", associatedQuestionsListObject[questionsLoopVar].getDisplayLabel());
				associatedQuestionsJSONObject.put("displayOrder", i);
				associatedQuestionsJSONObject.put("ghostValue", associatedQuestionsListObject[questionsLoopVar].getGhostValue());
				associatedQuestionsJSONObject.put("questionId", associatedQuestionsListObject[questionsLoopVar].getExternalId());
				associatedQuestionsJSONObject.put("required", associatedIsRequired);
				associatedQuestionsJSONObject.put("type", associatedQuestionsListObject[questionsLoopVar].getType());
				associatedQuestionsJSONObject.put("units", null!=associatedQuestionsListObject[questionsLoopVar].getUnits()?associatedQuestionsListObject[questionsLoopVar].getUnits():null);
				associatedQuestionsJSONObject.put("dependent", null!=associatedQuestionsListObject[questionsLoopVar].getExternaDependent()?associatedQuestionsListObject[questionsLoopVar].getExternaDependent():null);
				associatedQuestionsJSONObject.put("dependOn", null!=associatedQuestionsListObject[questionsLoopVar].getExternalDependOn()?associatedQuestionsListObject[questionsLoopVar].getExternalDependOn():null);
				associatedQuestionsJSONObject.put("isEditable", getRequiredBoolean(associatedQuestionsListObject[questionsLoopVar].getIsEditable()));
				associatedQuestionsJSONObject.put("condition", associatedQuestionsListObject[questionsLoopVar].getCondition());
				Answer answer = associatedQuestionsListObject[questionsLoopVar].getAnswer();
				setAnswerObject(answer,associatedQuestionsJSONObject);
				ListPossibleAnswers[] possibleAnswersListObject  = associatedQuestionsListObject[questionsLoopVar].getListPossibleAnswers();
				if(null==possibleAnswersListObject || possibleAnswersListObject.length==0){
					associatedQuestionsJSONObject.put("possibleAnswers", new JSONArray());
				}else{
					possibleAnswersJSONArray  = setPossibleAnswersforAssociatedSections(possibleAnswersListObject);
					associatedQuestionsJSONObject.put("possibleAnswers", possibleAnswersJSONArray);
				}
				associatedQuestionsJSONObject.put("additionalInformation", associatedQuestionsListObject[questionsLoopVar].getAdditionalInformation());	
				associatedQuestionsJSONArray.add(associatedQuestionsJSONObject);
			}
		}catch(Exception e){
			printException(e,"setAssociatedQuestionsforAssociatedSections");
		}
		return associatedQuestionsJSONArray; 
	}	
	
	
	private int getIntFromDouble(String displayOrd) {
		int i=0;
		try{
			if(null!=displayOrd && !displayOrd.isEmpty()){
				i= new Double(displayOrd).intValue();
			}
		}catch(Exception e){
			printException(e,"getIntFromDouble");
		}
		return i;
	}
	@SuppressWarnings("unchecked")
	public List<JSONObject> setPossibleAnswersforAssociatedSections(ListPossibleAnswers[] possibleAnswersListObject){
		List<JSONObject> possibleAnswersJSONArray = new ArrayList<>();
		JSONObject possibleAnswersJSONObject = new JSONObject();
		boolean answerIsRequired;
		try{
			for(int answersLoopVar=0;answersLoopVar<possibleAnswersListObject.length;answersLoopVar++){
				possibleAnswersJSONObject = new JSONObject();
				String answerRequired=possibleAnswersListObject[answersLoopVar].getIsRequired();
				answerIsRequired = getRequiredBoolean(answerRequired); 
				String disOrd = possibleAnswersListObject[answersLoopVar].getDisplayOrder();
				int intDisOrd = new Double(disOrd).intValue();
				possibleAnswersJSONObject.put("displayLabel", null!=possibleAnswersListObject[answersLoopVar].getDisplayLabel()?possibleAnswersListObject[answersLoopVar].getDisplayLabel():"");
				possibleAnswersJSONObject.put("displayOrder",intDisOrd);
				possibleAnswersJSONObject.put("value", null!=possibleAnswersListObject[answersLoopVar].getValue()?possibleAnswersListObject[answersLoopVar].getValue():"");
				possibleAnswersJSONObject.put("isRequired",answerIsRequired); // pending
				possibleAnswersJSONArray.add(possibleAnswersJSONObject);
			}
		}catch(Exception e){
			printException(e,"setPossibleAnswersforAssociatedSections");
		}
		
		return possibleAnswersJSONArray;
	}
	@Override
	public Response postVisit(String contentType, String acceptLanguage,
			String userAgent, String apiVersion, String request, String userId,String token) {

		PostVisitforUser postVisitforUser = new PostVisitforUser();
		Response response = null;
		try {     
			response = (Response) postVisitforUser.postVisit(contentType, acceptLanguage, userAgent, apiVersion, request, userId,token);
		}  catch(Exception e){
			printException(e,"postVisit");
		}
		return response;
	}
	
	public String getSORDataWithHeaderParams(String url,String token){
		String output;
		URL obj;
		StringBuilder sb = new StringBuilder();
		try {
			obj = new URL(url);
		HttpURLConnection con;
		
			con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		con.setRequestProperty("language","US");
		con.setRequestProperty("Authorization",token);
		con.setRequestProperty("Cache-Control","no-cache,no-store");
		con.setRequestProperty("Pragma","no-cache");
		con.setDoOutput(true);
		int responseCode = con.getResponseCode();
		logger.severe("\nSending 'GET' request to URL : " + url);
		logger.severe("Response Code : " + responseCode);
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
          while ((output = br.readLine()) != null)
          {
              sb.append(output + '\n');
             logger.info("line " + output);
          }
		} catch (MalformedURLException e) {
			printException(e,"getSORDataWithHeaderParams - MalformedURLException");
		}
		catch (IOException e) {
			printException(e,"getSORDataWithHeaderParams - IOException");
		}
        return sb.toString();
	}

	@Override
	public Response notificationRefuse(String contentType,
			String acceptLanguage, String userAgent, String apiVersion,
			String request,String token) {
			Response response = null;
			String iosUserId;
			String sorOutputString ="";
			JSONObject responseMessage = new JSONObject();
			JSONParser parser = new JSONParser();
			JSONArray sorResponseList;
			JSONArray dataArray = new JSONArray();
			JSONObject dataObject = new JSONObject();
			int code = 0;
		try{
			if(null==request || request.isEmpty()){
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						ServiceConstants.ERROR_CODE_ERROR,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.USER_ID_CHECK_MESSAGE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY,
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			JSONObject jsonRequest = (JSONObject) JSONValue.parse(request);
			dataArray = (JSONArray) jsonRequest.get("data");
			dataObject = (JSONObject) dataArray.get(0);
			logger.severe("refuse request :" + dataObject);
			iosUserId = (String) dataObject.get("userId");
			sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_1)+iosUserId+URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_2),token);
			if(sorOutputString.contains("ExceptionType")){
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
				return response;
			}else if(sorOutputString.contains("ClassName")){
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
				return response;
			}
			sorResponseList = (JSONArray) parser.parse(sorOutputString);
			if(null==sorResponseList || sorResponseList.isEmpty()){
				logger.info("User object is empty for the given user id.");
			}else{
				responseMessage = new UserAdapterLogicExtension().sendNotificationRefuseRequest(sorResponseList,dataObject,token);
			}
			if(null==responseMessage || responseMessage.isEmpty()){
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						400,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						"400", "SOR data issue in notificationRefuse",
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			List<JSONObject> data = (List<JSONObject>) responseMessage.get("data");
			if(!data.isEmpty()){
				code = 200;
			}else{
				List<JSONObject> errorList = (List<JSONObject>) responseMessage.get("errors");
				JSONObject errorObject = errorList.get(0);
				String id = (String) errorObject.get("id");
				code = Integer.parseInt(id);
			}
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, code,
					ServiceConstants.SUCCESS_MESSAGE,responseMessage);
		}catch(UnknownHostException uhe){
		    printException(uhe,ServiceConstants.GET_USER);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
					ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e,"notificationRefuse");
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.USER_ID_CHECK_MESSAGE, e.getMessage(),
					ServiceConstants.LEVEL_ERROR);
		}		
		return response;
	}
	
	@Override
	public Response notificationVisitRefuse(String contentType,
			String acceptLanguage, String userAgent, String apiVersion,
			String request, String token) {
		Response response = null;
		String iosUserId;
		String sorOutputString ="";
		JSONObject responseMessage = new JSONObject();
		JSONParser parser = new JSONParser();
		JSONArray sorResponseList;
		JSONArray dataArray = new JSONArray();
		JSONObject dataObject = new JSONObject();
		int code = 0;
		try{
			if(null==request || request.isEmpty()){
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						ServiceConstants.ERROR_CODE_ERROR,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.USER_ID_CHECK_MESSAGE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY,
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			JSONObject jsonRequest = (JSONObject) JSONValue.parse(request);
			dataArray = (JSONArray) jsonRequest.get("data");
			dataObject = (JSONObject) dataArray.get(0);
			logger.severe("visitrefuse request :" + dataObject);
			iosUserId = (String) dataObject.get("userId");
			sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_1)+iosUserId+URLUtility.getRequiredUrl(ServiceConstants.IS_REQUEST_N),token);
			if(sorOutputString.contains("ExceptionType")){
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
				return response;
			}else if(sorOutputString.contains("ClassName")){
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
				return response;
			}
			sorResponseList = (JSONArray) parser.parse(sorOutputString);
			if(null==sorResponseList || sorResponseList.isEmpty()){
				logger.info("User object is empty for the given user id.");
			}else{
				responseMessage = new UserAdapterLogicExtension().sendNotificationVisitRefuseRequest(sorResponseList,dataObject,token);
			}
			if(null==responseMessage || responseMessage.isEmpty()){
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						400,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						"400", "SOR data issue in notificationRefuse",
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			List<JSONObject> data = (List<JSONObject>) responseMessage.get("data");
			if(!data.isEmpty()){
				code = 200;
			}else{
				List<JSONObject> errorList = (List<JSONObject>) responseMessage.get("errors");
				JSONObject errorObject = errorList.get(0);
				String id = (String) errorObject.get("id");
				code = Integer.parseInt(id);
			}
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, code,
					ServiceConstants.SUCCESS_MESSAGE,responseMessage);
		}catch(UnknownHostException uhe){
		    printException(uhe,ServiceConstants.GET_USER);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
					ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e,"notificationRefuse");
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.USER_ID_CHECK_MESSAGE, e.getMessage(),
					ServiceConstants.LEVEL_ERROR);
		}		
		return response;
	}
	@Override
	public Response notificationAccept(String contentType,
			String acceptLanguage, String userAgent, String apiVersion,
			String request,String token) {
								Response response = null;
								String iosUserId;
								JSONObject responseMessage = new JSONObject();
								JSONParser parser = new JSONParser();
								JSONArray sorResponseList = new JSONArray();
								JSONArray dataArray = new JSONArray();
								JSONObject dataObject = new JSONObject();
								try{
									if(null==request || request.isEmpty()){
										response = APIResponseBuilder.sendFailResponse(apiVersion,
												ServiceConstants.ERROR_CODE_ERROR,
												ServiceConstants.EMPTY_REQUEST_MESSAGE,
												ServiceConstants.USER_ID_CHECK_MESSAGE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY,
												ServiceConstants.LEVEL_ERROR);
									
										return response;
									}
									JSONObject jsonRequest = (JSONObject) JSONValue.parse(request);
									dataArray = (JSONArray) jsonRequest.get("data");
									dataObject = (JSONObject) dataArray.get(0);
									logger.severe("accept request :" + dataObject);
									iosUserId = (String) dataObject.get("userId");
									String sorOutputString = adapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_1)+iosUserId+URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_2),token); 
									if(sorOutputString.contains("ExceptionType")){
										response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
										return response;
									}else if(sorOutputString.contains("ClassName")){
										response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
										return response;
									}
									sorResponseList = (JSONArray) parser.parse(sorOutputString);
									if(null==sorResponseList || sorResponseList.isEmpty()){
										logger.info("User object is empty for the given user id.");
									}else{
										responseMessage = new UserAdapterLogicExtension().sendNotificationAcceptRequest(sorResponseList,dataObject,token);
									}
									if(null==responseMessage || responseMessage.isEmpty()){
										response = APIResponseBuilder.sendFailResponse(apiVersion,
												400,
												ServiceConstants.ERROR_MESSAGE_ERROR,
												"400", "SOR data issue in notificationAccept",
												ServiceConstants.LEVEL_ERROR);
										return response;
									}
									response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK,
											ServiceConstants.SUCCESS_MESSAGE,responseMessage);
								}catch(UnknownHostException uhe){
								    printException(uhe,ServiceConstants.GET_USER);
									response = APIResponseBuilder.sendFailResponse(apiVersion,
											ServiceConstants.ERROR_CODE_ERROR,
											ServiceConstants.ERROR_MESSAGE_ERROR,
											ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
											ServiceConstants.LEVEL_ERROR);
								} catch (Exception e) {
									printException(e,"notificationAccept");
									response = APIResponseBuilder.sendFailResponse(apiVersion,
											ServiceConstants.ERROR_CODE_ERROR,
											ServiceConstants.ERROR_MESSAGE_ERROR,
											ServiceConstants.USER_ID_CHECK_MESSAGE, e.getMessage(),
											ServiceConstants.LEVEL_ERROR);
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
	
	private List<JSONObject> setResponse(JSONObject finalObject,
			List<JSONObject> userVisitsResp) {
		try{
			if(null!=finalObject && !finalObject.isEmpty()){
				userVisitsResp.add(finalObject);
			}
		}catch(Exception e){
			printException(e,"setResponse");
		}
		return userVisitsResp;
	}

	@Override
	public Response getSingleVisitById(String contentType,
			String acceptLanguage, String userAgent, String loggedInUser,
			String apiVersion, String userId,String visitId, String status, String token) {
		
		logger.severe("System.currentTimeMillis() at start of singleVisits " +URLUtility.getTime());
		Response response = null;
		JSONObject result = new JSONObject();
		List<JSONObject> userVisitsResp = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try{
			String sorOutputStringForVisits = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.SINGLE_VISIT)+visitId,token); 
			if(sorOutputStringForVisits.contains(ServiceConstants.EXCEPTION_TYPE)){
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputStringForVisits,apiVersion);
				return response;
			}else if(sorOutputStringForVisits.contains("ClassName")){
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringForVisits, apiVersion);
				return response;
			}
			userVisitsResp = getSingleVisitsForPerformance(sorOutputStringForVisits,mapper,userId,token,userVisitsResp,visitId);
			if(userVisitsResp.isEmpty()){
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						400,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						"400", "SOR data issue in getSingleVisitById",
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			result.put("data", userVisitsResp);
			result.put("errors", new JSONArray());
			
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK,
					ServiceConstants.SUCCESS_MESSAGE,result);
			logger.severe("System.currentTimeMillis() at end of singleVisits " +URLUtility.getTime());
		}catch(UnknownHostException uhe){
		    printException(uhe,ServiceConstants.GET_USER);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
					ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e,"getSingleVisitById");
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.USER_ID_CHECK_MESSAGE, e.getMessage(),
					ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}
	private List<JSONObject> getSingleVisitsForPerformance(
			String sorOutputStringForVisits, ObjectMapper mapper,
			String userId, String token, List<JSONObject> userVisitsResp,
			String visitId) {
		try{
			if(null!=sorOutputStringForVisits && !sorOutputStringForVisits.isEmpty()){
				VisitsByIDBean visitsAPIData = mapper.readValue(sorOutputStringForVisits,VisitsByIDBean.class);
				userVisitsResp = getResponseForSingleVisit(visitsAPIData,userVisitsResp,userId,token,visitId);
			}
		}catch(Exception e){
			printException(e,"getSingleVisitsForPerformance");
		}
		return userVisitsResp;
	}

	private List<JSONObject> getResponseForSingleVisit(
			VisitsByIDBean visitsAPIData, List<JSONObject> userVisitsResp,
			String userId, String token,String visitId) {
		try{
			if(null!=visitsAPIData){
				JSONObject finalObject = getSORDataforSingleVisitById(visitsAPIData,userId,token,visitId);
				userVisitsResp = setResponse(finalObject,userVisitsResp);
			 }
		}catch(Exception e){
			printException(e,"getResponseForSingleVisit");
		}
		return userVisitsResp;
	}

	private JSONObject getSORDataforSingleVisitById(
			VisitsByIDBean visitsAPIData, String userId, String token,String visitId) {
		JSONObject dataObject = new JSONObject();
		try{
			List<String> associatedMediaFilesIds;
			JSONObject retrieveItemsIdsFromLocation;
			List<JSONObject> catagoriesJSONArray = new ArrayList<>();
					dataObject.put("date",visitsAPIData.getPlanned_Date());
					dataObject.put("formId",visitsAPIData.getInternalId());
					dataObject.put("endDate",visitsAPIData.getEndDate());
					dataObject.put("descriptor2Value",null!=visitsAPIData.getDescriptor1Value()?visitsAPIData.getDescriptor1Value():null);
					dataObject.put("displayName", null!=visitsAPIData.getDisplayName()?visitsAPIData.getDisplayName():null);
					dataObject.put("manufacturerName",visitsAPIData.getManufacturerName());
					dataObject.put("descriptor2Label", null!=visitsAPIData.getDescriptor1Label()?visitsAPIData.getDescriptor1Label():null);
					dataObject.put("type", null!=visitsAPIData.getType()?visitsAPIData.getType():null);
					dataObject.put("vendorName",visitsAPIData.getVendorName());
					dataObject.put("visitId", visitId);
					dataObject.put("locationId",visitsAPIData.getLocationId());
					dataObject.put("streetAddress",visitsAPIData.getStreetAddress());
					dataObject.put("associatedMediaItemsId",visitsAPIData.getAssociatedMediaItemsId());
					dataObject.put("descriptor1Label",null!= visitsAPIData.getDescriptor2Label()?visitsAPIData.getDescriptor2Label():null);
		  			dataObject.put("descriptor1Value",null!= visitsAPIData.getDescriptor2Value()?visitsAPIData.getDescriptor2Value():null);
					dataObject.put("status",visitsAPIData.getState());
					dataObject.put("totalDuration",visitsAPIData.getTotalDuration());
					dataObject.put("syncStatus",null!= visitsAPIData.getSyncStatus()?visitsAPIData.getSyncStatus():null);
					ListCategories[] catagories = visitsAPIData.getListCategories();
					catagoriesJSONArray = getCategoriesList(catagories,catagoriesJSONArray,visitId);
					if(null!=catagoriesJSONArray && !catagoriesJSONArray.isEmpty()){
						dataObject.put("categories", catagoriesJSONArray);
					}else{
						dataObject.put("categories", new JSONArray());
					}
		}catch(Exception e){
			printException(e,"getSORDataforSingleVisitById");
		}
		return dataObject;
	}

	@Override
	public Response getVisitSkeleton(String contentType, String acceptLanguage,
			String userAgent, String loggedInUser, String apiVersion,
			String userId, String status, String token) {
		Response response = null;
		JSONObject result = new JSONObject();
		List<JSONObject> visitSkeletonList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try{
			String sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.VISIT_SKELETON),token); 
			if(sorOutputString.contains(ServiceConstants.EXCEPTION_TYPE)){
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
				return response;
			}else if(sorOutputString.contains("ClassName")){
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
				return response;
			}
			VisitSkeleton[] visitSkeleton = mapper.readValue(sorOutputString, VisitSkeleton[].class);
			
			visitSkeletonList = getVisitSkeletons(visitSkeleton,visitSkeletonList);
			result.put(ServiceConstants.DATA, visitSkeletonList);
			result.put(ServiceConstants.ERRORS, new JSONArray());
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK,
					ServiceConstants.SUCCESS_MESSAGE,result);
		}catch(UnknownHostException uhe){
		    printException(uhe,"getVisitSkeleton");
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
					ServiceConstants.LEVEL_ERROR);
		}catch(Exception e) {
			printException(e,"getVisitSkeleton");
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.USER_ID_CHECK_MESSAGE, ServiceConstants.SOR_USERID_MESSAGE,
					ServiceConstants.LEVEL_ERROR);
		}
	return response;
	}

	private List<JSONObject> getVisitSkeletons(VisitSkeleton[] visitSkeleton,
			List<JSONObject> visitSkeletonList) {
		JSONObject dataObject = new JSONObject();
		try{
			if(null!=visitSkeleton && visitSkeleton.length!=0){
				for(int i=0;i<visitSkeleton.length;i++){
					dataObject = new JSONObject();
					dataObject.put("visitId", visitSkeleton[i].getEventId());
					dataObject.put("locationId", visitSkeleton[i].getLocationId());
					dataObject.put("displayName", visitSkeleton[i].getVisitName());
					dataObject.put("status", visitSkeleton[i].getStatus());
					dataObject.put("vendorName", visitSkeleton[i].getVendorName());
					dataObject.put("descriptor1Label", "Inspection Level");
					dataObject.put("descriptor1Value", visitSkeleton[i].getInspectionLevel());
					dataObject.put("descriptor2Label", "Expediting Level");
					dataObject.put("descriptor2Value", visitSkeleton[i].getExpeditionLevel());
					dataObject.put("manufacturerName", visitSkeleton[i].getSubVendorName());
					dataObject.put("streetAddress", visitSkeleton[i].getAddress());
					dataObject.put("date", visitSkeleton[i].getPlannedDate());
					dataObject.put("endDate", visitSkeleton[i].getEndDate());
					dataObject.put("type", visitSkeleton[i].getType());
					visitSkeletonList.add(dataObject);
				}
			}
		}catch(Exception e){
			printException(e,"getVisitSkeleton");
		}
		return visitSkeletonList;
	}

}


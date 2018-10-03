package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.exception.AdapterExceptionHandler;

public class SingleNCRData {
	static Logger logger = Logger.getLogger(SingleNCRData.class.getName());
	AdapterExceptionHandler  adapterExceptionHandler= new AdapterExceptionHandler();

	public List<JSONObject> getSingleNCR(String locationId,
			String token, ObjectMapper mapper, List<JSONObject> locationResp,
			String userId, String visitId,String sorOutputStringForVisits) {
		try{
			locationResp = getlocationResp(sorOutputStringForVisits,locationId,token,mapper,locationResp,visitId);
		}catch(Exception e){
			printException(e, "getSingleNCR");
		}
		return locationResp;
	}
	
private List<JSONObject> getlocationResp(String sorOutputStringForVisits,
			String locationId, String token, ObjectMapper mapper,
			List<JSONObject> locationResp,String visitId) {
		try{
			if(null!=sorOutputStringForVisits && !sorOutputStringForVisits.isEmpty()){
				VisitsByIDBean visitsAPIData = mapper.readValue(sorOutputStringForVisits,VisitsByIDBean.class);
				if(null!=visitsAPIData){
					locationResp = getSingleNCRResponse(visitsAPIData,locationId,locationResp,visitId,token);
				}
			}
		}catch(Exception e){
			printException(e, "getlocationResp");
		}
		return locationResp;
	}

public List<JSONObject> getSingleNCRResponse(VisitsByIDBean visitsAPIData,String locationId,List<JSONObject> dataObjectList,String visitId,String token){
	
	try{
		ListCategories[] categoriesList = visitsAPIData.getListCategories();
		String formId = visitsAPIData.getInternalId();
		dataObjectList = executeSingleCategoriesList(categoriesList,locationId,visitId,dataObjectList,token,formId);
		
	}catch(Exception e){
		printException(e,"getNCRRepsonse");
	}
	return dataObjectList;
}

private List<JSONObject> executeSingleCategoriesList(ListCategories[] categoriesList,
		String locationId, String visitId, List<JSONObject> dataObjectList,
		String token,String formId) {
	String ncrItemId = ServiceConstants.NCR_ID;
	try{
		for(int i=0;i<categoriesList.length;i++){
			String typeId = categoriesList[i].getTypeId();
			if(typeId.equalsIgnoreCase(ncrItemId)){
				ListCards[] cardList = categoriesList[i].getListCards();
				dataObjectList = executeSingleCardLogic(cardList,locationId,visitId,dataObjectList,token,formId);
				}
			}
	}catch(Exception e){
		printException(e,"executeSingleCategoriesList");
	}
	return dataObjectList;
}

private List<JSONObject> executeSingleCardLogic(ListCards[] cardList,
		String locationId, String visitId, List<JSONObject> dataObjectList,
		String token,String formId) {
	try{
		for(int j=0;j<cardList.length;j++){
			String extId = cardList[j].getInternalId();
			String createdDate = cardList[j].getOpenDate();
			String resolvedDate = cardList[j].getClosedDate();
			String displayName = cardList[j].getDisplayName();
			String displayOrder = cardList[j].getDisplayOrder();
			ListSections[] sectionList = cardList[j].getListSections();
			dataObjectList = executeSingleSectionLogic(sectionList,extId,locationId,visitId,dataObjectList,displayName,createdDate,resolvedDate,displayOrder,formId);
			}
	}catch(Exception e){
		printException(e,"executeSingleCardLogic");
	}
	return dataObjectList;
}

private List<JSONObject> executeSingleSectionLogic(ListSections[] sectionList,
		String ncrId, String locationId, String visitId,
		List<JSONObject> dataObjectList, String displayName,String createdDate,String resolvedDate,String displayOrder,String formId) {
	try{
		dataObjectList = getSingleQuestionValues(sectionList,dataObjectList,ncrId,locationId,visitId,createdDate,resolvedDate,displayOrder,formId,displayName);
	}catch(Exception e){
		printException(e,"executeSingleSectionLogic");
	}
	return dataObjectList;
}

private List<JSONObject> getSingleQuestionValues(ListSections[] sectionList,
		List<JSONObject> dataObjectList, String ncrId, String locationId,
		String visitId,String createdDate,String resolvedDate,String displayOrder,String formId,String displayName) {
	JSONObject dataObject;
	String status="";
	String reason="";
	String ncrDescription="";
	String estimatedClosingDate="";
	String otherFactors="";
	List<String> associatedItemId=new ArrayList<>();
	String inspectorComments="";
	String reasonForRejection="";
	String correctiveActionForVendor="";
	String correctiveActionForEngineering="";
	List<String> photos=new ArrayList<>();
	boolean valOfVendor = false;
	boolean valOfEngineering=false;
	Map<String,String> questionsKeyMap = getQuestionsMap();
	try{

		for(int k=0;k<sectionList.length;k++){
			ListQuestions[] listQuestions = sectionList[k].getListQuestions();
			for(int l=0;l<listQuestions.length;l++){
				String questionId = listQuestions[l].getQuestionId();
				Answer answer = listQuestions[l].getAnswer();
				ListPossibleAnswers[] listPossibleAnswers = listQuestions[l].getListPossibleAnswers();
				int startIndex = questionId.length(); 
				int index = questionId.lastIndexOf(ServiceConstants.SEPERATOR);
				int lastIndex = index+1;
				String questIdPattern = questionId.substring(lastIndex, startIndex);
				String pattern = questionsKeyMap.get(questIdPattern);
				switch (pattern) {
				 case ServiceConstants.QUESTION0:  
					 associatedItemId = getListOfStrings(answer);
					 break;  
				 case ServiceConstants.QUESTION1:  
					 status = getCommaValues(answer);
					 break; 
				 case ServiceConstants.QUESTION2:  
					 reason = getPossibleValues(answer,listPossibleAnswers);
					 break; 
				 case ServiceConstants.QUESTION3:  
					 ncrDescription = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION4:  
					 otherFactors = getPossibleValues(answer,listPossibleAnswers);
					 break;
				 case ServiceConstants.QUESTION5:  
					 inspectorComments = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION6:  
					 estimatedClosingDate = getCommaValues(answer);
					 break;	 
				 case ServiceConstants.QUESTION7:  
					 photos = getListOfStrings(answer);
					 break;
				 case ServiceConstants.QUESTION8:  
					 reasonForRejection = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION9:  
					 correctiveActionForVendor = getActionValues(answer);
					 break;
				 case ServiceConstants.QUESTION10:  
					 valOfVendor = getBooleanValues(answer,listPossibleAnswers);
					 break;
				 case ServiceConstants.QUESTION11:  
					 correctiveActionForEngineering = getActionValues(answer);
					 break;
				 case ServiceConstants.QUESTION12:  
					 valOfEngineering = getBooleanValues(answer,listPossibleAnswers);
					 break;
				 default:  
				     logger.info(ServiceConstants.QUESTION_NOT_FOUND);  
				 }  
}
			dataObject = new JSONObject();
			dataObject.put("comments", null);
			dataObject.put("createdDate", createdDate);
			dataObject.put("estimatedClosingDate", estimatedClosingDate);
			dataObject.put("photos", photos);
			dataObject.put("reasonForRejection", reasonForRejection); 
			dataObject.put("correctiveActionByVendor", correctiveActionForVendor);
			dataObject.put("correctiveActionByEngineering", correctiveActionForEngineering);
			dataObject.put("isVerifiedByVendor",valOfVendor);
			dataObject.put("isVerifiedByEngineering",valOfEngineering);
			dataObject.put("createdDuringVisitId", null);
			dataObject.put("locationId", locationId);
			dataObject.put("ncrDescription", ncrDescription);
			dataObject.put("ncrId", ncrId);
			dataObject.put("reason", reason);
			dataObject.put("resolvedDate",resolvedDate);
			dataObject.put("resolvedDuringVisitId", null);
			dataObject.put("syncStatus", null); 
			dataObject.put("displayName", displayName); 
			dataObject.put("status", status);
			dataObject.put("otherFactors",otherFactors);
			dataObject.put("associatedItemsId",associatedItemId);
			dataObject.put("correctiveActions", new JSONArray());
			dataObject.put("inspectorComments",inspectorComments);
			dataObject.put("visitId",visitId);
			dataObject.put("formId", formId);
			dataObject.put("displayOrder", getIntegerValue(displayOrder));
			dataObjectList.add(dataObject);
}
	
	}catch(Exception e){
		printException(e,"executeSingleQuestionLogic");
	}
	return dataObjectList;
}

private boolean getBooleanValues(Answer answer,ListPossibleAnswers[] listPossibleAnswers) {
	
	boolean valOfVendor = false;
	String isCorrectiveActionForVendor="";
	List<String> isVendorActionCorrectiveList = new ArrayList<>();
	try{
		String[] answerArray = answer.getValue();
		for(int y=0;y<answerArray.length;y++){
			String answerValue = answerArray[y];
			for(int z=0;z<listPossibleAnswers.length;z++){
				String value = listPossibleAnswers[z].getValue();
				if(value.equalsIgnoreCase(answerValue)){
					String displayLabel = listPossibleAnswers[z].getDisplayLabel();
					isVendorActionCorrectiveList.add(displayLabel);
				}
			}
		}
		if (!isVendorActionCorrectiveList.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            for (String s : isVendorActionCorrectiveList) {
                sb.append(s).append(",");
            }

            isCorrectiveActionForVendor = sb.deleteCharAt(sb.length() - 1).toString();
            if("YES".equalsIgnoreCase(isCorrectiveActionForVendor)){
            	valOfVendor=true;
            }else{
            	valOfVendor=false;
            }
        }
	
	}catch(Exception e){
		printException(e,"getBooleanValues");
	}
	return valOfVendor;
}

private String getActionValues(Answer answer) {
	String correctiveActionForVendor="";
	List<String> vendorActionCorrectiveList = new ArrayList<>();
	try{
		String[] answerArray = answer.getValue();
		for(int x=0;x<answerArray.length;x++){
			String answerValue = answerArray[x];
			vendorActionCorrectiveList.add(answerValue);
		}
		if (!vendorActionCorrectiveList.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            for (String s : vendorActionCorrectiveList) {
                sb.append(s).append(",");
            }
            correctiveActionForVendor = sb.deleteCharAt(sb.length() - 1).toString();
        }
	}catch(Exception e){
		printException(e,"getActionValues");
	}
	return correctiveActionForVendor;
}

private String getPossibleValues(Answer answer,
		ListPossibleAnswers[] listPossibleAnswers) {
	String reason = "";
	try{
		List<String> dislayLabelList = new ArrayList<>();
		String[] answerArray = answer.getValue();
		for(int m=0;m<answerArray.length;m++){
			String answerValue = answerArray[m];
			for(int n=0;n<listPossibleAnswers.length;n++){
				String value = listPossibleAnswers[n].getValue();
				if(value.equalsIgnoreCase(answerValue)){
					String displayLabel = listPossibleAnswers[n].getDisplayLabel();
					dislayLabelList.add(displayLabel);
				}
			}
		}
		reason = getValues(dislayLabelList,reason);
		
	}catch(Exception e){
		printException(e,"getPossibleValues");
	}
	return reason;
}

private String getValues(List<String> dislayLabelList,String reason) {
    try{
    	if (!dislayLabelList.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            for (String s : dislayLabelList) {
                sb.append(s).append(",");
            }
            
           reason  = sb.deleteCharAt(sb.length() - 1).toString();
        }
    }catch(Exception e){
    	printException(e,"getPossibleValues");
    }
	return reason;
}

private List<String> getListOfStrings(Answer answer) {
	List<String> associatedItemId = new ArrayList<>();
	try{
		String[] answerArray = answer.getValue();
		for(int o=0;o<answerArray.length;o++){
			String value = answerArray[o];
			associatedItemId.add(value);
		}
	}catch(Exception e){
		printException(e,"getListOfStrings");
	}
	return associatedItemId;
}

private String getCommaValues(Answer answer) {
	String status = "";
	try{
		if(null!=answer){
			String[] answerArray = answer.getValue();
			if (answerArray.length > 0) {
	            StringBuilder sb = new StringBuilder();

	            for (String s : answerArray) {
	                sb.append(s).append(",");
	            }
	            status = sb.deleteCharAt(sb.length() - 1).toString();
	        }
		}
	}catch(Exception e){
		printException(e,"getCommaValues");
	}
	return status;
}
void printException(Exception e,String methodName) {
		
		logger.severe(ServiceConstants.EXCEPTION_IN+methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}

public static Map<String, String>  getQuestionsMap(){
	Map<String, String> questionsKeyMap=new HashMap<>();
	questionsKeyMap.put("Question0", "Question0");
	questionsKeyMap.put("Question1","Question1");
	questionsKeyMap.put("Question2","Question2");
	questionsKeyMap.put("Question3","Question3");
	questionsKeyMap.put("Question4","Question4");
	questionsKeyMap.put("Question5","Question5");
	questionsKeyMap.put("Question6","Question6");
	questionsKeyMap.put("Question7","Question7");
	questionsKeyMap.put("Question8","Question8");
	questionsKeyMap.put("Question9","Question9");
	questionsKeyMap.put("Question10","Question10");
	questionsKeyMap.put("Question11","Question11");
	questionsKeyMap.put("Question12","Question12");
	questionsKeyMap.put("Question13","Question13");
	return questionsKeyMap;
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
}

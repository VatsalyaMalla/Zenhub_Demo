package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Utils.URLUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.exception.AdapterExceptionHandler;

public class SingleIncidentsData {
	static Logger logger = Logger.getLogger(SingleIncidentsData.class.getName());
	AdapterExceptionHandler  adapterExceptionHandler= new AdapterExceptionHandler();
	public List<JSONObject> getSingleIncidents(String sorOutputString,
			String token, ObjectMapper mapper, String visitId,
			List<JSONObject> incidentResp, String userId) {
		try{
			if(null!=sorOutputString && !sorOutputString.isEmpty()){
				VisitsByIDBean visitsAPIData = mapper.readValue(sorOutputString,VisitsByIDBean.class);
				if(null!=visitsAPIData){
					incidentResp = getSingleIncidentsResponse(visitsAPIData,incidentResp,visitId,mapper,token);
				}
			}
		}catch(Exception e){
			printException(e,"getSingleIncidents");
		}
		return incidentResp;
	}
	
private List<JSONObject> getSingleIncidentsResponse(
			VisitsByIDBean visitsAPIData, List<JSONObject> dataObjectList,
			String visitId, ObjectMapper mapper, String token) {
	String incidentId = ServiceConstants.INCIDENT_ID;
	ListCategories[] categoriesList = visitsAPIData.getListCategories();
	String locationId = visitsAPIData.getLocationId();
	String formId = visitsAPIData.getInternalId();
		try{
			dataObjectList = getCategoriesForSingleIncidents(incidentId,locationId,dataObjectList,visitId,categoriesList,formId);
		
		}catch(Exception e){
			printException(e,"getSingleIncidentsResponse");
		}
		return dataObjectList;
	}

private List<JSONObject> getCategoriesForSingleIncidents(String incidentId,
		String locationId, List<JSONObject> dataObjectList, String visitId,
		ListCategories[] categoriesList,String formId) {
	try{

		for(int i=0;i<categoriesList.length;i++){
			String typeId = categoriesList[i].getTypeId();
			if(typeId.equalsIgnoreCase(incidentId)){
				ListCards[] cardList = categoriesList[i].getListCards();
				dataObjectList = getSectionsForSingleIncidents(locationId,dataObjectList,visitId,cardList,formId);
			}
		}
	}catch(Exception e){
		printException(e,"getCategoriesForSingleIncidents");
	}
	return dataObjectList;
}

private List<JSONObject> getSectionsForSingleIncidents(String locationId,
		List<JSONObject> dataObjectList, String visitId, ListCards[] cardList,String formId) {
	try{
		for(int j=0;j< cardList.length;j++){
			ListSections[] sectionList = cardList[j].getListSections();
			dataObjectList = getQuestionsForSingleIncidents(sectionList,locationId,dataObjectList,visitId,formId);
		}
	}catch(Exception e){
		printException(e,"getSectionsForSingleIncidents");
	}
	return dataObjectList;
}

private List<JSONObject> getQuestionsForSingleIncidents(
		ListSections[] sectionList, String locationId,
		List<JSONObject> dataObjectList, String visitId,String formId) {
	JSONObject dataObject = new JSONObject();
	String incidentDescription="";
	String status="";
	String personResponsible="";
	String correctiveAction="";
	boolean critBool = false;
	List<String> photos=null;
	String references="";
	String incidentType="";
	List<String> associatedItemId=new ArrayList<>();
	String additionalComments="";
	String hoursSpent="";
	String closingDate="";
	Map<String,String> questionsKeyMap = getQuestionsMap();
	try{

		for(int k=0;k<sectionList.length;k++){
			String extSecId = sectionList[k].getInternalId();
			String displayName = sectionList[k].getDisplayName();
			String createdDate = sectionList[k].getOpenDate();
			String resolvedDate = sectionList[k].getClosedDate();
			ListQuestions[] listQuestions = sectionList[k].getListQuestions();
			for(int l=0;l<listQuestions.length;l++){
				photos=new ArrayList<>();
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
					 references = getCommaValues(answer);
					 break; 
				 case ServiceConstants.QUESTION2:  
					 incidentDescription = getCommaValues(answer);
					 break; 
				 case ServiceConstants.QUESTION3:  
					 correctiveAction = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION4:  
					 personResponsible = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION5:  
					 incidentType = getPossibleValues(answer,listPossibleAnswers);
					 break; 	 
				 case ServiceConstants.QUESTION6:  
					 closingDate = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION7:  
					 additionalComments = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION8:  
					 status = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION9:  
					 hoursSpent = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION10:  
					 critBool = getBooleanValues(answer,listPossibleAnswers);
					 break;
				 case ServiceConstants.QUESTION11:
					 photos = getListOfStrings(answer);
					 break;
				 default:  
				     logger.info(ServiceConstants.QUESTION_NOT_FOUND);  
				 }
		}
		dataObject = new JSONObject();
		dataObject.put("createdDate", createdDate);
		dataObject.put("createdDuringVisitId", null);
		dataObject.put("incidentDescription",null!=incidentDescription?incidentDescription:null);
		dataObject.put("locationId", locationId);
		dataObject.put("incidentId", null!=extSecId?extSecId:"");
		dataObject.put("resolvedDate", resolvedDate);
		dataObject.put("resolvedDuringVisitId", null);// no mapping as per GEO
		dataObject.put("status", null!=status?status:null);
		dataObject.put("syncStatus", null);
		dataObject.put("photos", photos);
		dataObject.put("displayName", displayName);
		dataObject.put("incidentType", incidentType);
		dataObject.put("correctiveAction", null!=correctiveAction?correctiveAction:null);
		dataObject.put("personResponsible", null!=personResponsible?personResponsible:null);
		dataObject.put("critical",critBool);
		dataObject.put("associatedItemsId", null!=associatedItemId?associatedItemId:new JSONArray());
		dataObject.put("references",null!=references?references:null);
		dataObject.put("additionalComments",null!=additionalComments?additionalComments:null);
		dataObject.put("hoursSpent",null!=hoursSpent?hoursSpent:null);
		dataObject.put("closingDate",closingDate);
		dataObject.put("visitId",visitId);
		dataObject.put("formId",formId);
		dataObjectList.add(dataObject);
}
	
	}catch(Exception e){
		printException(e,"getQuestionsForSingleIncidents");
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

void printException(Exception e,String methodName) {
		
		logger.severe(ServiceConstants.EXCEPTION_IN+methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}

}

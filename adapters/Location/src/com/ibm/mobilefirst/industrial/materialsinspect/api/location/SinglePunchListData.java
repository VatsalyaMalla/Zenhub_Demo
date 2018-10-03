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
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.location.demo.data.LocationDataAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.exception.AdapterExceptionHandler;

public class SinglePunchListData {
	static Logger logger = Logger.getLogger(SinglePunchListData.class.getName());
	AdapterExceptionHandler  adapterExceptionHandler= new AdapterExceptionHandler();

	public List<JSONObject> getSinglePunchList(ObjectMapper mapper,
			String sorOutputString, String visitId, String userId,
			String token, List<JSONObject> punchResp) {
		try{
			if(null!=sorOutputString && !sorOutputString.isEmpty()){
				VisitsByIDBean visitsAPIData = mapper.readValue(sorOutputString,VisitsByIDBean.class);
				if(null!=visitsAPIData){
					punchResp = getSinglePunchListItemsResponse(visitsAPIData,punchResp,visitId,mapper,token);
				}
			}
		}catch(Exception e){
			printException(e,"getSinglePunchList");
		}
		return punchResp;
	}
private List<JSONObject> getSinglePunchListItemsResponse(
			VisitsByIDBean visitsAPIData, List<JSONObject> dataObjectList,
			String visitId, ObjectMapper mapper, String token) {
		String punchListItemId = ServiceConstants.PUNCH_LIST_ID;
		ListCategories[] categoriesList = visitsAPIData.getListCategories();
		String locationId = visitsAPIData.getLocationId();
		String formId = visitsAPIData.getInternalId();
		try{
			dataObjectList = getCategoriesForSinglePunchList(categoriesList,locationId,dataObjectList,visitId,mapper,token,punchListItemId,formId);
		}catch(Exception e){
			printException(e,"getSinglePunchListItemsResponse");
		}
		return dataObjectList;
	}
private List<JSONObject> getCategoriesForSinglePunchList(
		ListCategories[] categoriesList, String locationId,
		List<JSONObject> dataObjectList, String visitId, ObjectMapper mapper,
		String token, String punchListItemId,String formId) {
	try{
		logger.severe("token for single punchList" + token);
		for(int i=0;i<categoriesList.length;i++){
			String typeId = categoriesList[i].getTypeId();
			if(typeId.equalsIgnoreCase(punchListItemId)){
				ListCards[] cardList = categoriesList[i].getListCards();
				dataObjectList = getCardsForSinglePunchList(locationId,dataObjectList,visitId,token,cardList,formId);				
				}
		}
	}catch(Exception e){
		printException(e,"getCategoriesForSinglePunchList");
	}
	return dataObjectList;
}
private List<JSONObject> getCardsForSinglePunchList(String locationId,
		List<JSONObject> dataObjectList, String visitId, String token,
		ListCards[] cardList,String formId) {
	try{
		for(int j=0;j< cardList.length;j++){
			ListSections[] sectionList = cardList[j].getListSections();
			dataObjectList = getSectionsForSinglePunchList(locationId,dataObjectList,visitId,sectionList,formId);
		}
	}catch(Exception e){
		printException(e,"getCardsForSinglePunchList");
	}
	return dataObjectList;
}
private List<JSONObject> getSectionsForSinglePunchList(String locationId,
		List<JSONObject> dataObjectList, String visitId,
		ListSections[] sectionList,String formId) {
	JSONObject dataObject = new JSONObject();
	String itemDescription="";
	String status="";
	String personResponsible="";
	String correctiveAction="";
	boolean criticBool = false;
	String references="";
	List<String> associatedItemId=new ArrayList<>();
	List<String> photos=null;
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
					 itemDescription = getCommaValues(answer);
					 break;  
				 case ServiceConstants.QUESTION1:  
					 references = getCommaValues(answer);
					 break; 
				 case ServiceConstants.QUESTION2:  
					 associatedItemId = getListOfStrings(answer);
					 break; 
				 case ServiceConstants.QUESTION3:  
					 correctiveAction = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION4:  
					 personResponsible = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION5:  
					 closingDate = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION6:  
					 additionalComments = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION7:  
					 status = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION8:  
					 hoursSpent = getCommaValues(answer);
					 break;
				 case ServiceConstants.QUESTION9:  
					 criticBool = getBooleanValues(answer,listPossibleAnswers);
					 break;
				 case ServiceConstants.QUESTION10:
					 photos = getListOfStrings(answer);
					 break;
				 default:  
				     logger.info(ServiceConstants.QUESTION_NOT_FOUND);  
				 }
			}
			dataObject = new JSONObject();
			dataObject.put("createdDate", createdDate);
			dataObject.put("resolvedDate", resolvedDate); 
			dataObject.put("resolvedDuringVisitId", null);// no mapping as per GEO
			dataObject.put("references",null!=references?references:null);
			dataObject.put("additionalComments",null!=additionalComments?additionalComments:null);
			dataObject.put("hoursSpent",null!=hoursSpent?hoursSpent:null);
			dataObject.put("closingDate",closingDate);
			dataObject.put("photos", photos);
			dataObject.put("createdDuringVisitId", null);
			dataObject.put("associatedItemsId", null!=associatedItemId?associatedItemId:new JSONArray());
			dataObject.put("critical", criticBool);
			dataObject.put("displayName", displayName);
			dataObject.put("personResponsible", null!=personResponsible?personResponsible:null);
			dataObject.put("locationId", locationId);
			dataObject.put("correctiveAction", null!=correctiveAction?correctiveAction:null);
			dataObject.put("itemDescription",null!=itemDescription?itemDescription:null);
			dataObject.put("punchListItemId", null!=extSecId?extSecId:null);
			dataObject.put("syncStatus", null);
			dataObject.put("status", null!=status?status:null);
			dataObject.put("visitId",visitId);
			dataObject.put("formId",formId);
			dataObjectList.add(dataObject);
}
	}catch(Exception e){
		printException(e,"getSectionsForSinglePunchList");
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
	return questionsKeyMap;
}
}

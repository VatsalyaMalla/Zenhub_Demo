package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.ibm.mobilefirst.industrial.materialsinspect.api.location.constants.ServiceConstants;
import com.ibm.mobilefirst.update.UpdateListPossibleAnswers;
import com.ibm.mobilefirst.update.UpdateListSections;
import com.ibm.mobilefirst.update.UpdateListQuestions;
import com.ibm.mobilefirst.update.UpdateAnswer;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.Utils.URLUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mobilefirst.update.UpdateSORDataStructure;

public class UpdateQuestions {
	static Logger logger = Logger.getLogger(UpdateQuestions.class.getName());
	
	public JSONObject question1(String iosStatus,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosStatus.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question1");
		}
		return answer;
	}
	
	public JSONObject question4(List<String> iosOtherFactorsList,JSONObject answer,List<JSONObject> possibleAnswers){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			for(int y=0;y<iosOtherFactorsList.size();y++){
				String iosOtherFactors = iosOtherFactorsList.get(y);
				for(int i=0;i<possibleAnswers.size();i++){
					JSONObject jsonObj = possibleAnswers.get(i);
					String sorDisplayLabel = (String) jsonObj.get("displayLabel");
					if(sorDisplayLabel.equalsIgnoreCase(iosOtherFactors)){
						String displayValue = (String) jsonObj.get("value");
						answerArray.add(displayValue);
					}
				}
			}
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question4");
		}
		return answer;
	}
	
	public JSONObject question2(List<String> iosReasonList,JSONObject answer,List<JSONObject> possibleAnswers){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			for(int y=0;y<iosReasonList.size();y++){
				String iosReason = iosReasonList.get(y);
				for(int i=0;i<possibleAnswers.size();i++){
					JSONObject jsonObj = possibleAnswers.get(i);
					String sorDisplayLabel = (String) jsonObj.get("displayLabel");
					if(sorDisplayLabel.equalsIgnoreCase(iosReason)){
						String displayValue = (String) jsonObj.get("value");
						answerArray.add(displayValue);
					}
				}
			}
			
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question2");
		}
		return answer;
	}
	
	public JSONObject question0(List<String> associatedItems,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray.addAll(associatedItems);
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question0");
		}
		return answer;
	}
	
	public JSONObject question5(String iosInspectorComments,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosInspectorComments.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question5");
		}
		return answer;
	}
	
	public JSONObject question6(String iosPhotos,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosPhotos.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question6");
		}
		return answer;
	}
	
	public JSONObject question7(String iosReasonForRejection,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosReasonForRejection.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question7");
		}
		return answer;
	}
	public JSONObject question8(String ioscorrectiveActionsByVendor,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(ioscorrectiveActionsByVendor.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question8");
		}
		return answer;
	}
	public JSONObject question9(Object isVerifiedByVendor,JSONObject answer,List<JSONObject> listPossibleAnswers){
		JSONObject possibleAnswersObject = new JSONObject();
		String displayLabel;
		String possibleAnswersValue="";
		try{
			if(isVerifiedByVendor.toString().equalsIgnoreCase("1")){
				for(int i=0;i<listPossibleAnswers.size();i++){
					possibleAnswersObject = listPossibleAnswers.get(i);
					displayLabel = (String) possibleAnswersObject.get("displayLabel");
					if("YES".equalsIgnoreCase(displayLabel)){
						possibleAnswersValue = (String) possibleAnswersObject.get("value");
					}
				}
			}else if(isVerifiedByVendor.toString().equalsIgnoreCase("0")){
				for(int i=0;i<listPossibleAnswers.size();i++){
					possibleAnswersObject = listPossibleAnswers.get(i);
					displayLabel = (String) possibleAnswersObject.get("displayLabel");
					if("NO".equalsIgnoreCase(displayLabel)){
						possibleAnswersValue = (String) possibleAnswersObject.get("value");
					}
				}
			}
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(possibleAnswersValue.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question9");
		}
		return answer;
	}
	public JSONObject question10(String ioscorrectiveActionsByEngineering,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(ioscorrectiveActionsByEngineering.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question10");
		}
		return answer;
	}
	public JSONObject question11(Object isVerifiedByEngineering,JSONObject answer,List<JSONObject> listPossibleAnswers){
		JSONObject possibleAnswersObject = new JSONObject();
		String displayLabel;
		String possibleAnswersValue="";;
		try{
			if(isVerifiedByEngineering.toString().equalsIgnoreCase("1")){
				for(int i=0;i<listPossibleAnswers.size();i++){
					possibleAnswersObject = listPossibleAnswers.get(i);
					displayLabel = (String) possibleAnswersObject.get("displayLabel");
					if("YES".equalsIgnoreCase(displayLabel)){
						possibleAnswersValue = (String) possibleAnswersObject.get("value");
					}
				}
			}else if(isVerifiedByEngineering.toString().equalsIgnoreCase("0")){
				for(int i=0;i<listPossibleAnswers.size();i++){
					possibleAnswersObject = listPossibleAnswers.get(i);
					displayLabel = (String) possibleAnswersObject.get("displayLabel");
					if("NO".equalsIgnoreCase(displayLabel)){
						possibleAnswersValue = (String) possibleAnswersObject.get("value");
					}
				}
			}
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(possibleAnswersValue.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question11");
		}
		return answer;
	}
	public JSONObject question3(String iosDescription, JSONObject answer) {
		String ncrDescritpionString="";
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosDescription.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e, "question3");
		}
		return answer;
	}
	
	public void createNewNCR(){
		try{
			JSONParser parser = new JSONParser();
		  Object obj = parser.parse(new FileReader("UpdateNCR.json"));
		  JSONObject ncrCardObject = (JSONObject)obj;
		  List<JSONObject> cardListArray = new ArrayList<JSONObject>();
		  
		}catch(Exception e){
			printException(e, "createNewNCR");
		}
	}

	public static JSONObject getCategoryList(JSONObject jsonRequest,String categoryId,int cardListSize,int updateType,String url,Map<String,String> questionsKeyMap,String token) {
		int newCardId;
		String newQuestionId;
		List<String> value;
		String sectionId;
		JSONObject listQuestions;
		String cardId;
		String jsonRequestValue;
		JSONObject newCardSORObject = new JSONObject();
		List<JSONObject> categoriesSORList = new ArrayList<>();
		List<JSONObject> cardSORList = new ArrayList<>();
		  try{
			  //New logic start
		   	  String newCardSORString = URLUtility.getSORDataWithHeaderParams(url,token);
		   	  newCardSORObject =(JSONObject) JSONValue.parse(newCardSORString);
		   	  String displayName = (String) jsonRequest.get("displayName");
		   	  categoriesSORList = (List<JSONObject>) newCardSORObject.get("listCategories");
		   	  cardSORList = (List<JSONObject>) categoriesSORList.get(0).get("listCards");
		   	  List<JSONObject> sectionList = (List<JSONObject>) cardSORList.get(0).get("listSections");
			  List<JSONObject> listQuestionsObj = (List<JSONObject>) sectionList.get(0).get("listQuestions");
			  newCardId = cardListSize;
			  cardId = categoryId+"-Card"+newCardId;
			  cardSORList.get(0).put("cardId", cardId);
			  cardSORList.get(0).put("displayOrder", cardListSize);
			  sectionId = cardId+"-Section0";
			  sectionList.get(0).put("sectionId", sectionId);
			  for(int j=0;j<listQuestionsObj.size();j++){
				  listQuestions = listQuestionsObj.get(j);
				  newQuestionId = sectionId+"-Question"+j;
				  int endIndex = newQuestionId.length();
				  listQuestions.put("questionId", newQuestionId);
				  int startIndex = newQuestionId.lastIndexOf("-");
				  startIndex = startIndex +1;
				  String questionNumberString = newQuestionId.substring(startIndex, endIndex);
				  if(questionsKeyMap.containsKey(questionNumberString)){
					  String questionNumberAttribute = questionsKeyMap.get(questionNumberString);
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
						  value.clear();
						  if(questionNumberAttribute.equalsIgnoreCase("associatedItemsId") || questionNumberAttribute.equalsIgnoreCase("photos")){
							  if(null!=jsonRequest.get(questionNumberAttribute)){
								  List<String> associatedItemsId = (List<String>) jsonRequest.get(questionNumberAttribute);
								  if(null!=associatedItemsId && !associatedItemsId.isEmpty()){
									  for(String id:associatedItemsId){
										  value.add(id);
								  		}
								  }
							  }
						  }else if (questionNumberAttribute.equalsIgnoreCase("isVerifiedByVendor") || questionNumberAttribute.equalsIgnoreCase("isVerifiedByEngineering") || questionNumberAttribute.equalsIgnoreCase("critical")){
							  value = setValueForVerification(jsonRequest,listQuestions,questionNumberAttribute);
						  }else if(questionNumberAttribute.equalsIgnoreCase("reason") || questionNumberAttribute.equalsIgnoreCase("otherFactors")){
							  List<String> iOSRequestList = new ArrayList<>();
							  jsonRequestValue = (String) jsonRequest.get(questionNumberAttribute);
							  if(null!=jsonRequestValue && !jsonRequestValue.isEmpty()){
								  iOSRequestList = Arrays.asList(jsonRequestValue.split("\\s*,\\s*"));
							  }
							  for(int o=0;o<iOSRequestList.size();o++){
							  value = setValueForDisplayLabel(listQuestions,iOSRequestList.get(o),value);
							  }
						  } else{
							  jsonRequestValue = (String) jsonRequest.get(questionNumberAttribute);
							  if(null!=jsonRequestValue && !jsonRequestValue.isEmpty()){
								  value = Arrays.asList(jsonRequestValue.split("\\s*,\\s*"));
							  }
						  }
						  answer.put("value",value);
						  listQuestions.put("answer", answer);
				  }
				  
			  }
			  setDisplayName(updateType,cardSORList.get(0),displayName);
		    
		  }catch(Exception e){
			  printExceptions(e, "getCategoriesForUpdateNCR");
		  }
			  return cardSORList.get(0);
	}
	
	private static void setDisplayName(int updateType, JSONObject jsonObject,String displayName) {
		try{
			if(updateType==1){
				jsonObject.put("displayName", displayName);
			}
		}catch(Exception e){
			printExceptions(e, "setDisplayName");
		}
		
	}

	private static void setDefaultValues(String questionNumberAttribute,
			JSONObject jsonRequest,List<String> value) {
		try{
			if(!(questionNumberAttribute.equalsIgnoreCase("reason") || questionNumberAttribute.equalsIgnoreCase("otherFactors") || questionNumberAttribute.equalsIgnoreCase("isVerifiedByVendor") || questionNumberAttribute.equalsIgnoreCase("isVerifiedByEngineering") || questionNumberAttribute.equalsIgnoreCase("critical") || questionNumberAttribute.equalsIgnoreCase("associatedItemsId") || questionNumberAttribute.equalsIgnoreCase("photos"))){
				String jsonRequestValue = (String) jsonRequest.get(questionNumberAttribute);
				  if(null!=jsonRequestValue && !jsonRequestValue.isEmpty()){
					  value = Arrays.asList(jsonRequestValue.split("\\s*,\\s*"));
				  }
			}
		}catch(Exception e){
			printExceptions(e, "setDefaultValues");
		}
	}

	private static void setCommaSeparatedValues(String questionNumberAttribute,
			JSONObject jsonRequest, List<String> value,JSONObject listQuestions) {
        try{
        	if(questionNumberAttribute.equalsIgnoreCase("reason") || questionNumberAttribute.equalsIgnoreCase("otherFactors")){
				  List<String> iOSRequestList = new ArrayList<>();
				  String jsonRequestValue = (String) jsonRequest.get(questionNumberAttribute);
				  if(null!=jsonRequestValue && !jsonRequestValue.isEmpty()){
					  iOSRequestList = Arrays.asList(jsonRequestValue.split("\\s*,\\s*"));
				  }
				  for(int o=0;o<iOSRequestList.size();o++){
				  value = setValueForDisplayLabel(listQuestions,iOSRequestList.get(o),value);
				  }
        	} 
        }catch(Exception e){
        	printExceptions(e, "setCommaSeparatedValues");
        }
	}

	private static void setBooleanValues(String questionNumberAttribute,
			JSONObject jsonRequest, JSONObject listQuestions,List<String> value) {
		try{
			if (questionNumberAttribute.equalsIgnoreCase("isVerifiedByVendor") || questionNumberAttribute.equalsIgnoreCase("isVerifiedByEngineering") || questionNumberAttribute.equalsIgnoreCase("critical")){
				  value = setValueForVerification(jsonRequest,listQuestions,questionNumberAttribute);
			}
		}catch(Exception e){
			printExceptions(e, "setBooleanValues");
		}
		
	}

	private static void setItemsId(String questionNumberAttribute,
			JSONObject jsonRequest, List<String> value) {
		try{
			if(questionNumberAttribute.equalsIgnoreCase("associatedItemsId") || questionNumberAttribute.equalsIgnoreCase("photos")){
				  if(null!=jsonRequest.get("associatedItemsId")){
					  List<String> associatedItemsId = (List<String>) jsonRequest.get("associatedItemsId");
					  if(null!=associatedItemsId && !associatedItemsId.isEmpty()){
						  for(String id:associatedItemsId){
							  value.add(id);
					  		}
					  }
				  }
				  if(null!=jsonRequest.get("photos")){
					  List<String> photos = (List<String>) jsonRequest.get("photos");
					  if(null!=photos && !photos.isEmpty()){
						  for(String photo:photos){
							  value.add(photo);
					  		}
					  } 
				  }
			  }
		}catch(Exception e){
			printExceptions(e, "setItemsId");
		}
		
	}

	private static List<String> setValueForVerification( JSONObject jsonRequest,JSONObject listQuestions,String questionNumberAttribute){
		List<String> value=null;
		Object isVerifiedByVendor;
		String possibleValueVendor;
		List<JSONObject> listPossibleAnswers;
		if(jsonRequest.get(questionNumberAttribute)!=null){
			  isVerifiedByVendor = jsonRequest.get(questionNumberAttribute);  
			  if(isVerifiedByVendor.toString().equalsIgnoreCase("1")){
				  listPossibleAnswers = (List<JSONObject>) listQuestions.get("ListPossibleAnswers");
				  for (int i = 0; i < listPossibleAnswers.size(); i++) {
					JSONObject jsonObj = listPossibleAnswers.get(i);
					if(jsonObj.get("displayLabel").equals("YES")){
						possibleValueVendor = (String) jsonObj.get("value");
						  value = Arrays.asList(possibleValueVendor.split("\\s*,\\s*"));
					} 
				}
			  }	else if(isVerifiedByVendor.toString().equalsIgnoreCase("0")){
				  listPossibleAnswers = (List<JSONObject>) listQuestions.get("ListPossibleAnswers");
				  for (int i = 0; i < listPossibleAnswers.size(); i++) {
					JSONObject jsonObj = listPossibleAnswers.get(i);
					if(jsonObj.get("displayLabel").equals("NO")){
						possibleValueVendor = (String) jsonObj.get("value");
						  value = Arrays.asList(possibleValueVendor.split("\\s*,\\s*"));
					} 
				}
			  }
		}
		return value;
	}
	
	private static List<String> setValueForDisplayLabel(JSONObject listQuestions,String iOSRequestDisplayName,List<String> value){
		List<JSONObject> listPossibleAnswers;
		listPossibleAnswers = (List<JSONObject>) listQuestions.get("ListPossibleAnswers");
		for(int r=0;r<listPossibleAnswers.size();r++){
			JSONObject jsonObj = listPossibleAnswers.get(r);
			String sorDisplayLabel = (String) jsonObj.get("displayLabel");
			if(sorDisplayLabel.equalsIgnoreCase(iOSRequestDisplayName)){
				String displayValue = (String) jsonObj.get("value");
				value.add(displayValue);
			}
		}
		return value;
	}

	private static JSONObject getSectionsList(JSONObject jsonRequest) {
		    List<JSONObject> sectionList = new ArrayList<>();
		    List<JSONObject> questionList = new ArrayList<>();
		    JSONObject sectionObject = new JSONObject();
		    
		try{
			sectionObject.put("internalId", null);
			sectionObject.put("sectionId", null);
			sectionObject.put("displayName","");
			sectionObject.put("displayOrder",0);
			questionList = getQuestionList(jsonRequest);
			sectionObject.put("listQuestions",null);
		}catch(Exception e){
			printExceptions(e, "getSectionsList");
		}
		return sectionObject;
	}

	private static List<JSONObject> getQuestionList(JSONObject jsonRequest) {
		JSONObject questionObject = new JSONObject();
		JSONObject answerObject = new JSONObject();
		List<JSONObject> questionList = new ArrayList<>();
		List<JSONObject> listPossibleAnswers = new ArrayList<>();
		try{
		for(int i=1;i<13;i++){
			questionObject = new JSONObject();
			questionObject.put("internalId",null);
			questionObject.put("internalIdCFG", 21);
			questionObject.put("questionId", "-Question"+i);
			questionObject.put("defaultValue", "");
			questionObject.put("additionalInformation", "");
			questionObject.put("displayLabel", "Items");
			questionObject.put("displayOrder", 0);
			questionObject.put("ghostValue", "");
			questionObject.put("infoQuestion","");
			questionObject.put("isRequired", false);
			questionObject.put("typeId",201609130);
			questionObject.put("type", "shortText");
			questionObject.put("units", "");
			questionObject.put("dependent","");
			questionObject.put("dependOn", "");
			questionObject.put("condition", "");
			listPossibleAnswers = getPossibleAnswersList(jsonRequest,i);
			questionObject.put("ListPossibleAnswers", listPossibleAnswers);
			answerObject = getAnswerObject(jsonRequest,i);
			questionObject.put("answer", answerObject);
			questionList.add(questionObject);
		}
		}catch(Exception e){
			printExceptions(e, "getQuestionList");
		  }
		return questionList;
	}

	private static List<JSONObject> getPossibleAnswersList(JSONObject jsonRequest,int i) {
		List<JSONObject> listPossibleAnswers = new ArrayList<>();
		JSONObject possibleAnswersObject = new JSONObject();
		try{
		possibleAnswersObject.put("displayLabel", "");
		possibleAnswersObject.put("displayOrder",0);
		possibleAnswersObject.put("value","");
		possibleAnswersObject.put("isRequired", false);
		listPossibleAnswers.add(possibleAnswersObject);
		}catch(Exception e){
			printExceptions(e, "getPossibleAnswersList");
		}
		return listPossibleAnswers;
	}

	private static JSONObject getAnswerObject(JSONObject jsonRequest,int i) {
		JSONObject answerObject = new JSONObject();
		List<String> answerValue = new ArrayList<>();
		String status="";
		String[] statusArray;
		try{
		answerObject.put("typeId", "");
		answerObject.put("type","");
		if(i==1){
			status = (String) jsonRequest.get("status");
			statusArray = status.split(",");
			for(int j=0;j<statusArray.length;j++){
				answerValue.add(statusArray[j]);
			}
			answerObject.put("value", answerValue);
		}
		
		}catch(Exception e){
			printExceptions(e, "getAnswerObject");
		  }
		return answerObject;
	}
	
	
	public Map<String, String>  updateIncidentMap(){
		Map<String, String> questionsKeyMap=new HashMap<>();
		questionsKeyMap.put("Question0", "associatedItemIds");
		questionsKeyMap.put("Question1","references");
		questionsKeyMap.put("Question2","ncrDescription");
		questionsKeyMap.put("Question3","correctiveAction");
		questionsKeyMap.put("Question4","personResponsible");
		questionsKeyMap.put("Question5","closingDate");
		questionsKeyMap.put("Question6","additionalComments");
		questionsKeyMap.put("Question7","status");
		questionsKeyMap.put("Question8","hoursSpent");
		questionsKeyMap.put("Question9","critical"); // object
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

static void printExceptions(Exception e,String methodName) {
	
	logger.severe(ServiceConstants.EXCEPTION_IN+methodName);
	StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : e.getStackTrace()) {
        sb.append(element.toString());
        sb.append("\n");
    }
    logger.severe(sb.toString());
}
}

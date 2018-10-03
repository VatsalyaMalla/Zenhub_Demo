package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.ibm.mobilefirst.industrial.materialsinspect.api.location.constants.ServiceConstants;
import com.ibm.mobilefirst.update.UpdateListSections;

public class UpdateIncidentsQuestions {

	static Logger logger = Logger.getLogger(UpdateIncidentsQuestions.class.getName());
	
	public JSONObject question1(String iosReferences,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosReferences.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e,"question1");
		}
		return answer;
	}
	
	public JSONObject question4(String iosPersonResponsible,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosPersonResponsible.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e,"question4");
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
			printException(e,"question0");
		}
		return answer;
	}
	
	public JSONObject question5(String iosClosingDate,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosClosingDate.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e,"question5");
		}
		return answer;
	}
	public JSONObject question6(String iosAdditionalComments,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosAdditionalComments.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e,"question6");
		}
		return answer;
	}
	public JSONObject question7(String iosStatus,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosStatus.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e,"question7");
		}
		return answer;
	}
	public JSONObject question8(String iosHoursSpent,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosHoursSpent.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e,"question8");
		}
		return answer;
	}
	public JSONObject question9(Object iosCritical,JSONObject answer,List<JSONObject> listPossibleAnswers){
		JSONObject possibleAnswersObject = new JSONObject();
		String displayLabel;
		String possibleAnswersValue="";
		try{
			if(iosCritical.toString().equalsIgnoreCase("1")){
				for(int i=0;i<listPossibleAnswers.size();i++){
					possibleAnswersObject = listPossibleAnswers.get(i);
					displayLabel = (String) possibleAnswersObject.get("displayLabel");
					if("YES".equalsIgnoreCase(displayLabel)){
						possibleAnswersValue = (String) possibleAnswersObject.get("value");
					}
				}
			}else if(iosCritical.toString().equalsIgnoreCase("0")){
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
			printException(e,"question9");
		}
		return answer;
	}
	public JSONObject question2(String iosIncidentDescription,JSONObject answer){
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosIncidentDescription.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e,"question2");
		}
		return answer;
	}

	public JSONObject question3(String iosCorrectiveAction, JSONObject answer) {
		try{
			List<String> answerArray = (List<String>) answer.get("value");
			answerArray.clear();
			answerArray = Arrays.asList(iosCorrectiveAction.split("\\s*,\\s*"));
			answer.put("value",answerArray);
		}catch(Exception e){
			printException(e,"question3");
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
			printException(e,"createNewNCR");
		}
	}

	public static JSONObject getCategoryList(JSONObject jsonRequest) {
		
		logger.severe("Inside getCategoryList for update incidents.");
		JSONObject sectionsObject = new JSONObject();
		JSONObject cardListObject = new JSONObject();
		List<JSONObject> categoryList = new ArrayList<JSONObject>();
		String questionId;
		String references="";
		String status;
		List<String> value;
		String reason="";
		String description="";
		List<String> associatedItemsId;
		List<JSONObject> listPossibleAnswers=null;
		String otherFactors="";
		String inspectorComments="";
		JSONParser parse = new JSONParser();
		String photos="";
		String reasonForRejection="";
		String incidentDescription="";
		String correctiveAction="";
		String correctiveActionsByVendor="";
		Object isCritical;
		String correctiveActionsByEngineering="";
		boolean isVerifiedByEngineering=false;
		UpdateListSections[] listSections=null;
		JSONObject listPossibleAnswersObj = null;
		JSONObject listQuestions=null;
		String personResponsible="";
		String closingDate="";
		String additionalComments="";
		String hoursSpent="";
		String displayLabel="";
		String possibleAnswersValue ="";
		JSONObject sorJson = new JSONObject();
		  try{
			  Object obj = parse.parse(new FileReader("UpdateIncidents.json"));
			  sorJson = (JSONObject)obj;
			  List<JSONObject> sectionList = (List<JSONObject>) sorJson.get("listSections");
			  List<JSONObject> listQuestionsObj = (List<JSONObject>) sectionList.get(0).get("listQuestions");
			  for(int j=0;j<listQuestionsObj.size();j++){
				  listQuestions = listQuestionsObj.get(j);
				  questionId = (String) listQuestions.get("questionId");
				  if(questionId.endsWith("Question0")){
					  
					  associatedItemsId = (List<String>) jsonRequest.get("associatedItemIds");
					  if(null!=associatedItemsId && !associatedItemsId.isEmpty()){
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
						  for(String s:associatedItemsId){
							  value.add(s);
							  }
						  answer.put("value",value);
						  listQuestions.put("answer", answer);
					  }
					  
				  }else if(questionId.endsWith("Question1")){
					  
					  references = (String) jsonRequest.get("references");
					  if(null!=references && !references.isEmpty()){
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
					   value = Arrays.asList(references.split("\\s*,\\s*"));
					   answer.put("value",value);
					   listQuestions.put("answer", answer);
					  }
					  
				  }else if(questionId.endsWith("Question2")){
					  
					  incidentDescription = (String) jsonRequest.get("incidentDescription");
					  if(null!=incidentDescription && !incidentDescription.isEmpty()){
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
					  value = Arrays.asList(incidentDescription.split("\\s*,\\s*"));
					  answer.put("value",value);
					  listQuestions.put("answer", answer);
					  }
					  
				  }else if(questionId.endsWith("Question3")){
					  
					  correctiveAction = (String) jsonRequest.get("correctiveAction");
					  if(null!=correctiveAction && !correctiveAction.isEmpty()){
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
					  value = Arrays.asList(correctiveAction.split("\\s*,\\s*"));
					  answer.put("value",value);
					  listQuestions.put("answer", answer);
					  }
					  
				  }else if(questionId.endsWith("Question4")){
					  
					  personResponsible = (String) jsonRequest.get("personResponsible");
					  if(null!=personResponsible && !personResponsible.isEmpty()){
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
					  value = Arrays.asList(personResponsible.split("\\s*,\\s*"));
					  answer.put("value",value);
					  listQuestions.put("answer", answer);
					  }
					  
				  }else if(questionId.endsWith("Question5")){
					  
					  closingDate = (String) jsonRequest.get("closingDate");
					  if(null!=closingDate && !closingDate.isEmpty()){
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
					  value = Arrays.asList(closingDate.split("\\s*,\\s*"));
					  answer.put("value",value);
					  listQuestions.put("answer", answer);
					  }
					  
				  }else if(questionId.endsWith("Question6")){
					  
					  additionalComments = (String) jsonRequest.get("additionalComments");
					  if(null!=additionalComments && !additionalComments.isEmpty()){
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
					  value = Arrays.asList(additionalComments.split("\\s*,\\s*"));
					  answer.put("value",value);
					  listQuestions.put("answer", answer);
					  }
					  
				  }else if(questionId.endsWith("Question7")){
					  
					  status = (String) jsonRequest.get("status");
					  if(null!=status && !status.isEmpty()){
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
					  value = Arrays.asList(status.split("\\s*,\\s*"));
					  answer.put("value",value);
					  listQuestions.put("answer", answer);
					  }
					  
				  }else if(questionId.endsWith("Question8")){
					  
					  hoursSpent = (String) jsonRequest.get("hoursSpent");
					  if(null!=hoursSpent && !hoursSpent.isEmpty()){
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
					  value = Arrays.asList(hoursSpent.split("\\s*,\\s*"));
					  answer.put("value",value);
					  listQuestions.put("answer", answer);
					  }
					  
				  }else if(questionId.endsWith("Question9")){
					  
					  
					  if(null!=jsonRequest.get("critical")){
						  isCritical = jsonRequest.get("critical");
						  JSONObject answer = (JSONObject) listQuestions.get("answer");
						  value = (List<String>) answer.get("value");
						  if(isCritical.toString().equalsIgnoreCase("1")){
							  listPossibleAnswers = (List<JSONObject>) listQuestions.get("ListPossibleAnswers");
							  listPossibleAnswersObj = listPossibleAnswers.get(0);
							  displayLabel = (String) listPossibleAnswersObj.get("displayLabel"); 
							  if("YES".equalsIgnoreCase(displayLabel)){
								  possibleAnswersValue = (String) listPossibleAnswersObj.get("value");
								  value = Arrays.asList(possibleAnswersValue.split("\\s*,\\s*"));
							  }
						  }else if(isCritical.toString().equalsIgnoreCase("0")){
							  listPossibleAnswers = (List<JSONObject>) listQuestions.get("ListPossibleAnswers");
							  listPossibleAnswersObj = listPossibleAnswers.get(1);
							  displayLabel = (String) listPossibleAnswersObj.get("displayLabel"); 
							  if("NO".equalsIgnoreCase(displayLabel)){
								  possibleAnswersValue = (String) listPossibleAnswersObj.get("value");
								  value = Arrays.asList(possibleAnswersValue.split("\\s*,\\s*"));
							  }
						  }
						  answer.put("value",value);
						  listQuestions.put("answer", answer);
					  }
					 
				  }
			  }
		  }catch(Exception e){
			  printExceptions(e,"getCategoryList");
		  }
			  return sorJson;
	}

	private static JSONObject getSectionsList(JSONObject jsonRequest) {
		    List<JSONObject> sectionList = new ArrayList<JSONObject>();
		    List<JSONObject> questionList = new ArrayList<JSONObject>();
		    JSONObject sectionObject = new JSONObject();
		try{
			sectionObject.put("internalId", null);
			sectionObject.put("sectionId", null);
			sectionObject.put("displayName","");
			sectionObject.put("displayOrder",0);
			questionList = getQuestionList(jsonRequest);
			sectionObject.put("listQuestions",null);
		}catch(Exception e){
			printExceptions(e,"getSectionsList");
		}
		return sectionObject;
	}

	private static List<JSONObject> getQuestionList(JSONObject jsonRequest) {
		JSONObject questionObject = new JSONObject();
		JSONObject answerObject = new JSONObject();
		List<JSONObject> questionList = new ArrayList<JSONObject>();
		List<JSONObject> listPossibleAnswers = new ArrayList<JSONObject>();
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
			printExceptions(e,"getQuestionList");
		  }
		return questionList;
	}

	private static List<JSONObject> getPossibleAnswersList(JSONObject jsonRequest,int i) {
		List<JSONObject> listPossibleAnswers = new ArrayList<JSONObject>();
		JSONObject possibleAnswersObject = new JSONObject();
		try{
		possibleAnswersObject.put("displayLabel", "");
		possibleAnswersObject.put("displayOrder",0);
		possibleAnswersObject.put("value","");
		possibleAnswersObject.put("isRequired", false);
		listPossibleAnswers.add(possibleAnswersObject);
		}catch(Exception e){
			printExceptions(e,"getPossibleAnswersList");
		}
		return listPossibleAnswers;
	}

	private static JSONObject getAnswerObject(JSONObject jsonRequest,int i) {
		JSONObject answerObject = new JSONObject();
		List<String> answerValue = new ArrayList<String>();
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
			printExceptions(e,"getAnswerObject");
		  }
		return answerObject;
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

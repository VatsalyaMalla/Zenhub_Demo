package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.Utils.URLUtility;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.ibm.mobilefirst.beans.BaseResponse;
import com.ibm.mobilefirst.beans.ErrorElement;
import com.ibm.mobilefirst.exception.TecnicasException;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.constants.ServiceConstants;

public class UpdateNCR {

	static Logger logger = Logger.getLogger(UpdateNCR.class.getName());

	/**
	 * 
	 * @param ncrRequest
	 */
	public static String processUpdateNCR(String ncrRequest, String token) {

		String response = "";
		JSONArray dataArray = new JSONArray();
		JSONObject dataObject = new JSONObject();
		JSONObject jsonRequest = new JSONObject();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			dataObject = (JSONObject) JSONValue.parse(ncrRequest);
			dataArray = (JSONArray) dataObject.get("data");
			jsonRequest = (JSONObject) dataArray.get(0);
			logger.severe("JSONObject retrieved after successful parsing." + jsonRequest);
			String locationId = (String) jsonRequest.get("locationId");
			String visitId = (String) jsonRequest.get("visitId");
			String userId = (String) jsonRequest.get("userId");
			String visitStatus = (String) jsonRequest.get("visitStatus");
			String sorOutput = getUserVisitsSOR(userId, token);
			if (null != sorOutput && !sorOutput.isEmpty()) {
				UserRequestByIdSORData[] userObjGetRequestByID = mapper.readValue(sorOutput, UserRequestByIdSORData[].class);
				response = updateNCRS(userObjGetRequestByID, locationId, visitId, response, userId, token, jsonRequest, visitStatus);
			}
		} catch (Exception e) {
			printException(e, "processUpdateNCR");
		}
		return response;
	}

	private static String updateNCRS(UserRequestByIdSORData[] userObjGetRequestByID, String locationId, String visitId, String response, String userId, String token,
			JSONObject jsonRequest, String visitStatus) {
		try {
			if (null != userObjGetRequestByID && userObjGetRequestByID.length != 0) {
				for (int i = 0; i < userObjGetRequestByID.length; i++) {
					Object idEvent = userObjGetRequestByID[i].getID_EVENT();
					Object idForm = userObjGetRequestByID[i].getID_FORM();
					Object idLocation = userObjGetRequestByID[i].getID_LOCATION();
					if (idLocation.toString().equalsIgnoreCase(locationId) && idEvent.toString().equalsIgnoreCase(visitId)) {
						String formOutput = getSingleVisitData(idForm.toString(), idLocation.toString(), idEvent.toString(), userId, token);
						response = ncrUpdate(formOutput, idForm.toString(), idLocation.toString(), jsonRequest, token, response, visitStatus);
					}
				}
			}
		} catch (Exception e) {
			printException(e, "updateNCRS");
		}
		return response;
	}

	private static String ncrUpdate(String formOutput, String idForm, String idLocation, JSONObject jsonRequest, String token, String response, String visitStatus) {
		try {
			if (null != formOutput && !formOutput.isEmpty()) {
				JSONObject visitSORJson = (JSONObject) JSONValue.parse(formOutput);
				if (null != visitSORJson && !visitSORJson.isEmpty()) {
					response = sendRequestToUpdateNcr(visitSORJson, idForm, idLocation, jsonRequest, token, visitStatus);
				}
			}
		} catch (Exception e) {
			printException(e, "ncrUpdate");
		}
		return response;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public static String getUserVisitsSOR(String userId, String token) {
		String sorOutput = "";
		try {
			String url = URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL) + userId + URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_N);
			sorOutput = URLUtility.getSORDataWithHeaderParams(url, token);

		} catch (Exception e) {
			printException(e, "getUserVisitsSOR");
		}

		return sorOutput;
	}

	public static String getSingleVisitData(String idForm, String idLocation, String idEvent, String userId, String token) {
		String formOutput = "";
		try {
			String formURL = URLUtility.getRequiredUrl(ServiceConstants.GET_VISITS_URL_IDFORM) + idForm + URLUtility.getRequiredUrl(ServiceConstants.LOCATION) + idLocation
					+ URLUtility.getRequiredUrl(ServiceConstants.GET_VISITS_URL_IDEVENT) + idEvent + URLUtility.getRequiredUrl(ServiceConstants.GET_VISITS_URL_IDUSER) + userId;
			formOutput = URLUtility.getSORDataWithHeaderParamsWithLanguage(formURL, token);
		} catch (Exception e) {
			printException(e, "getSingleVisitData");
		}
		return formOutput;
	}

	/**
	 * 
	 * @param visitsAPIData
	 * @param locId
	 * @param id
	 * @param jsonRequest
	 */
	public static String sendRequestToUpdateNcr(JSONObject visitsAPIData, String locId, String id, JSONObject jsonRequest, String token, String visitStatus) {
		String response = "";
		String ncrItemId = ServiceConstants.NCR_ID;
		List<JSONObject> categoriesList = (List<JSONObject>) visitsAPIData.get("listCategories");
		response = getCategoriesForUpdateNCR(categoriesList, response, ncrItemId, jsonRequest, token, visitsAPIData, visitStatus);
		return response;
	}

	private static String getCategoriesForUpdateNCR(List<JSONObject> categoriesList, String response, String ncrItemId, JSONObject jsonRequest, String token,
			JSONObject visitsAPIData, String visitStatus) {
		try {
			for (int i = 0; i < categoriesList.size(); i++) {
				JSONObject listCategories = categoriesList.get(i);
				Object typeId = listCategories.get("typeId");
				String categoryId = (String) listCategories.get("categoryId");
				response = getIdForUpdateNCR(typeId, categoryId, ncrItemId, response, listCategories, jsonRequest, token, visitsAPIData, visitStatus);
			}
		} catch (Exception e) {
			printException(e, "getCategoriesForUpdateNCR");
		}
		return response;
	}

	private static String getIdForUpdateNCR(Object typeId, String categoryId, String ncrItemId, String response, JSONObject listCategories, JSONObject jsonRequest, String token,
			JSONObject visitsAPIData, String visitStatus) {

		JSONObject newCardObject = new JSONObject();
		UpdateQuestions updateNCRQuestions = new UpdateQuestions();
		boolean set = false;
		try {
			if (typeId.toString().equalsIgnoreCase(ncrItemId)) {
				List<JSONObject> cardListObj = (List<JSONObject>) listCategories.get("listCards");
				int cardListSize = cardListObj.size();
				String iosNCRId = (String) jsonRequest.get("ncrId");
				if (!(iosNCRId.startsWith("AppGenerated-NCR"))) {
					set = updateValuesForNCR(cardListObj, iosNCRId, set, jsonRequest, updateNCRQuestions);

				} else {
					String url = URLUtility.getRequiredUrl(ServiceConstants.NCR_CATEGORY_TEMPLATE);
					newCardObject = updateNCRQuestions.getCategoryList(jsonRequest, categoryId, cardListSize, 1, url, updateNCRMap(), token);
					logger.severe("newCardObject value : : :" + newCardObject);
					cardListObj.add(newCardObject);
					set = true;
				}
			}
			String requestUrlForUpdateNCR = URLUtility.getRequiredUrl(ServiceConstants.REQUEST_URL);
			if (set) {
				Object categoryInternal = listCategories.get("internalId");
				if (null != categoryInternal && "-1".equalsIgnoreCase(categoryInternal.toString())) {
					listCategories.put("internalId", null);
				}
				visitsAPIData.put("stateId", null);
				visitsAPIData.put("state", visitStatus);
				logger.severe("visitsAPIData for updateNCR :" + visitsAPIData);
				response = URLUtility.getSORDataWithHeaderParamsForUpdateNCR(requestUrlForUpdateNCR, visitsAPIData.toString(), token);
			}
		} catch (Exception e) {
			printException(e, "getIdForUpdateNCR");
		}
		return response;
	}

	private static boolean updateValuesForNCR(List<JSONObject> cardListObj, String iosNCRId, boolean set, JSONObject jsonRequest, UpdateQuestions updateNCRQuestions) {

		try {
			for (int j = 0; j < cardListObj.size(); j++) {
				JSONObject cardList = cardListObj.get(j);
				Object sorExtId = cardList.get("internalId");
				set = checkRequestIdForUpdateNCR(sorExtId.toString(), iosNCRId, cardList, updateNCRQuestions, jsonRequest, set);
			}
		} catch (Exception e) {
			printException(e, "updateValuesForNCR");
		}
		return set;
	}

	private static boolean checkRequestIdForUpdateNCR(String sorExtId, String iosNCRId, JSONObject cardList, UpdateQuestions updateNCRQuestions, JSONObject jsonRequest, boolean set) {

		try {
			if (null != sorExtId && !sorExtId.isEmpty()) {
				if (iosNCRId.equalsIgnoreCase(sorExtId.toString())) {
					logger.severe("sorNCRId and  iosNCRId are equal");
					String displayName = (String) jsonRequest.get("displayName");
					cardList.put("displayName", displayName);
					set = getQuestionUpdatesForNCR(set, cardList, jsonRequest, updateNCRQuestions);
				}
			}
		} catch (Exception e) {
			printException(e, "checkRequestIdForUpdateNCR");
		}
		return set;
	}

	private static boolean getQuestionUpdatesForNCR(boolean set, JSONObject cardList, JSONObject jsonRequest, UpdateQuestions updateNCRQuestions) {
		Map<String, String> questionsKeyMap = getQuestionsMap();
		Map<String, String> questionsKeyMapForFields = updateNCRMap();
		try {
			List<JSONObject> sectionListObj = (List<JSONObject>) cardList.get("listSections");
			for (int m = 0; m < sectionListObj.size(); m++) {
				JSONObject sectionList = sectionListObj.get(m);
				List<JSONObject> listQuestionsObj = (List<JSONObject>) sectionList.get("listQuestions");
				for (int p = 0; p < listQuestionsObj.size(); p++) {
					JSONObject listQuestions = listQuestionsObj.get(p);
					String questionId = (String) listQuestions.get("questionId");
					JSONObject answer = (JSONObject) listQuestions.get("answer");
					int startIndex = questionId.length();
					int index = questionId.lastIndexOf(ServiceConstants.SEPERATOR);
					int lastIndex = index + 1;
					String questIdPattern = questionId.substring(lastIndex, startIndex);
					String pattern = questionsKeyMap.get(questIdPattern);
					switch (pattern) {
					case ServiceConstants.QUESTION0:
						setItemsDescription(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION0));
						break;
					case ServiceConstants.QUESTION1:
						setStatusForNCR(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION1), cardList);
						break;
					case ServiceConstants.QUESTION2:
						setReason(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION2));
						break;
					case ServiceConstants.QUESTION3:
						setStatus(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION3));
						break;
					case ServiceConstants.QUESTION4:
						setReason(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION4));
						break;
					case ServiceConstants.QUESTION5:
						setStatus(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION5));
						break;
					case ServiceConstants.QUESTION6:
						setItemsDescription(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION6));
						break;
					case ServiceConstants.QUESTION7:
						setStatus(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION7));
						break;
					case ServiceConstants.QUESTION8:
						setStatus(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION8));
						break;
					case ServiceConstants.QUESTION9:
						setBoolean(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION9));
						break;
					case ServiceConstants.QUESTION10:
						setStatus(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION10));
						break;
					case ServiceConstants.QUESTION11:
						setBoolean(jsonRequest, answer, listQuestions, updateNCRQuestions, questionsKeyMapForFields.get(ServiceConstants.QUESTION11));
						break;
					default:
						logger.info(ServiceConstants.QUESTION_NOT_FOUND);
					}
				}
			}
			set = true;
		} catch (Exception e) {
			printException(e, "getQuestionUpdatesForNCR");
		}
		return set;
	}

	private static void setBoolean(JSONObject jsonRequest, JSONObject answer, JSONObject listQuestions, UpdateQuestions updateNCRQuestions, String iOSField) {
		Object isVerifiedByVendor = "";
		try {
			if (null != jsonRequest.get(iOSField)) {
				isVerifiedByVendor = jsonRequest.get(iOSField);
			}
			List<JSONObject> listPossibleAnswers = (List<JSONObject>) listQuestions.get("ListPossibleAnswers");
			answer = updateNCRQuestions.question9(isVerifiedByVendor, answer, listPossibleAnswers);
			listQuestions.put("answer", answer);
		} catch (Exception e) {
			printException(e, "setBoolean");
		}
	}

	private static void setReason(JSONObject jsonRequest, JSONObject answer, JSONObject listQuestions, UpdateQuestions updateNCRQuestions, String iOSField) {
		try {
			String iosReason = (String) jsonRequest.get(iOSField);

			if (null != iosReason && !iosReason.isEmpty()) {
				List<JSONObject> possibleAnswers = (List<JSONObject>) listQuestions.get("ListPossibleAnswers");
				List<String> iosReasonList = Arrays.asList(iosReason.split("\\s*,\\s*"));
				answer = updateNCRQuestions.question2(iosReasonList, answer, possibleAnswers);

				listQuestions.put("answer", answer);
			}
		} catch (Exception e) {
			printException(e, "setReason");
		}
	}

	private static void setStatus(JSONObject jsonRequest, JSONObject answer, JSONObject listQuestions, UpdateQuestions updateNCRQuestions, String iOSField) {
		try {
			String iosStatus = (String) jsonRequest.get(iOSField);

			if (null != iosStatus && !iosStatus.isEmpty()) {
				answer = updateNCRQuestions.question1(iosStatus, answer);
				listQuestions.put("answer", answer);
			}
		} catch (Exception e) {
			printException(e, "setStatus");
		}

	}

	private static void setStatusForNCR(JSONObject jsonRequest, JSONObject answer, JSONObject listQuestions, UpdateQuestions updateNCRQuestions, String iOSField,
			JSONObject cardList) {
		try {
			String iosStatus = (String) jsonRequest.get(iOSField);
			if (null != iosStatus && !iosStatus.isEmpty()) {
				if ("Closed".equalsIgnoreCase(iosStatus)) {
					cardList.put("state", iosStatus);
					cardList.put("stateId", null);
				} else {
					answer = updateNCRQuestions.question1(iosStatus, answer);
					listQuestions.put("answer", answer);
				}
			}
		} catch (Exception e) {
			printException(e, "setStatus");
		}

	}

	private static void setItemsDescription(JSONObject jsonRequest, JSONObject answer, JSONObject listQuestions, UpdateQuestions updateNCRQuestions, String iOSField) {
		try {
			List<String> associatedItems = (List<String>) jsonRequest.get(iOSField);
			if (null != associatedItems && !associatedItems.isEmpty()) {
				answer = updateNCRQuestions.question0(associatedItems, answer);
				listQuestions.put("answer", answer);
			}
		} catch (Exception e) {
			printException(e, "setItemsDescription");
		}
	}

	public static Map<String, String> updateNCRMap() {
		Map<String, String> questionsKeyMap = new HashMap<>();
		questionsKeyMap.put("Question0", "associatedItemsId");
		questionsKeyMap.put("Question1", "status");
		questionsKeyMap.put("Question2", "reason");
		questionsKeyMap.put("Question3", "ncrDescription");
		questionsKeyMap.put("Question4", "otherFactors");
		questionsKeyMap.put("Question5", "inspectorComments");
		questionsKeyMap.put("Question6", "photos");
		questionsKeyMap.put("Question7", "reasonForRejection");
		questionsKeyMap.put("Question8", "correctiveActionByVendor");
		questionsKeyMap.put("Question9", "isVerifiedByVendor");
		questionsKeyMap.put("Question10", "correctiveActionByEngineering");
		questionsKeyMap.put("Question11", "isVerifiedByEngineering");
		return questionsKeyMap;
	}

	static void printException(Exception e, String methodName) {

		logger.severe(ServiceConstants.EXCEPTION_IN + methodName);
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append("\n");
		}
		logger.severe(sb.toString());
	}

	public static Map<String, String> getQuestionsMap() {
		Map<String, String> questionsKeyMap = new HashMap<>();
		questionsKeyMap.put("Question0", "Question0");
		questionsKeyMap.put("Question1", "Question1");
		questionsKeyMap.put("Question2", "Question2");
		questionsKeyMap.put("Question3", "Question3");
		questionsKeyMap.put("Question4", "Question4");
		questionsKeyMap.put("Question5", "Question5");
		questionsKeyMap.put("Question6", "Question6");
		questionsKeyMap.put("Question7", "Question7");
		questionsKeyMap.put("Question8", "Question8");
		questionsKeyMap.put("Question9", "Question9");
		questionsKeyMap.put("Question10", "Question10");
		questionsKeyMap.put("Question11", "Question11");
		questionsKeyMap.put("Question12", "Question12");
		return questionsKeyMap;
	}

	public static String processUpdateNewNCR(String ncrRequest, String token) {
		List<JSONObject> iosDataArray = new ArrayList<>();
		JSONObject iosDataObject = new JSONObject();
		JSONObject iosJsonRequest = new JSONObject();
		JSONObject sorRequestObject = new JSONObject();
		List<JSONObject> sorRequestList = new ArrayList<>();
		List<String> reasonList = new ArrayList<>();
		List<String> otherFactorsList = new ArrayList<>();
		String response = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			iosDataObject = (JSONObject) JSONValue.parse(ncrRequest);
			logger.severe("JSONObject retrieved after successful parsing in new ncr update." + iosDataObject);
			iosDataArray = (JSONArray) iosDataObject.get("data");
			for (int i = 0; i < iosDataArray.size(); i++) {
				sorRequestObject = new JSONObject();
				iosJsonRequest = (JSONObject) iosDataArray.get(i);
				String internalId = (String) iosJsonRequest.get("ncrId");
				internalId = getInternalId(internalId);
				validateAction((String) iosJsonRequest.get("action"));

				List<String> associatedItemsId = (List<String>) iosJsonRequest.get("associatedItemsId");
				String reason = (String) iosJsonRequest.get("reason");
				reasonList = getListValue(reason);
				String otherFactors = (String) iosJsonRequest.get("otherFactors");
				otherFactorsList = getListValue(otherFactors);
				List<String> photos = (List<String>) iosJsonRequest.get("photos");
				String status = (String) iosJsonRequest.get("status");
				String reasonForRejection = (String) iosJsonRequest.get("reasonForRejection");
				String correctiveActionsByVendor = (String) iosJsonRequest.get("correctiveActionsByVendor");
				String correctiveActionsByEngineering = (String) iosJsonRequest.get("correctiveActionsByEngineering");
				String ncrDescription = (String) iosJsonRequest.get("ncrDescription");
				String inspectorComments = (String) iosJsonRequest.get("inspectorComments");
				String locationId = (String) iosJsonRequest.get("locationId");
				String visitId = (String) iosJsonRequest.get("visitId");
				String displayName = (String) iosJsonRequest.get("displayName");
				String createdDate = (String) iosJsonRequest.get("createdDate");
				String estimatedClosingDate = (String) iosJsonRequest.get("estimatedClosingDate");
				String resolvedDate = (String) iosJsonRequest.get("resolvedDate");
				Object isVerifiedByVendor = iosJsonRequest.get("isVerifiedByVendor");
				Object isVerifiedByEngineering = iosJsonRequest.get("isVerifiedByEngineering");
				String proposedCorrectiveAction = (String) iosJsonRequest.get("proposedCorrectiveAction");

				sorRequestObject.put("InternalID", null != internalId ? Integer.parseInt(internalId.toString()) : null);
				sorRequestObject.put("Items", associatedItemsId);
				sorRequestObject.put("Action", (String) iosJsonRequest.get("action"));
				sorRequestObject.put("Status", status);
				sorRequestObject.put("Reason", reasonList);
				sorRequestObject.put("OtherFactors", otherFactorsList);
				sorRequestObject.put("Description", ncrDescription);
				sorRequestObject.put("InspectorComments", inspectorComments);
				sorRequestObject.put("Photos", photos);
				sorRequestObject.put("ActivityName",displayName);
				sorRequestObject.put("RejectedReason", reasonForRejection);
				sorRequestObject.put("CorrectiveActionsVendor", correctiveActionsByVendor);
				sorRequestObject.put("CorrectiveActionsEngineering", correctiveActionsByEngineering);
				sorRequestObject.put("VendorVerified", null != isVerifiedByVendor ? Integer.parseInt(isVerifiedByVendor.toString()) : null);
				sorRequestObject.put("EngineeringVerified", null != isVerifiedByEngineering ? Integer.parseInt(isVerifiedByEngineering.toString()) : null);
				sorRequestObject.put("LocationID", locationId);
				sorRequestObject.put("EventID", visitId);
				sorRequestObject.put("CreateDate", createdDate);
				sorRequestObject.put("EstimatedClosingDate", estimatedClosingDate);
				sorRequestObject.put("ClosingDate", resolvedDate);
				sorRequestObject.put("DisplayName", displayName);
				sorRequestObject.put("CheckingAndCompliance", proposedCorrectiveAction);
				sorRequestList.add(sorRequestObject);
			}
			logger.severe("SORList retrieved after successful parsing in new ncr update." + sorRequestList);
			response = URLUtility.getSORDataWithHeaderParamsForUpdateNCR(URLUtility.getRequiredUrl(ServiceConstants.IOS_NCR), sorRequestList.toString(), token);
		} catch (TecnicasException te) {
			throw te;
		} catch (Exception e) {
			printException(e, "processUpdateNewNCR");
		}
		return response;
	}

	private static void validateAction(String action) {
		if (StringUtils.isEmpty(action)) {
			throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE,
					ServiceConstants.NULL_ACTION_ERROR));
		}
		if (!(StringUtils.equalsIgnoreCase(action, ServiceConstants.CREATE_CONSTANT) || StringUtils.equalsIgnoreCase(action, ServiceConstants.UPDATE_CONSTANT) || StringUtils
				.equalsIgnoreCase(action, ServiceConstants.DELETE_CONSTANT))) {
			throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE,
					ServiceConstants.INVALID_ACTION_ERROR));
		}
	}

	private static BaseResponse getErrorResponse(String code, String message) {
		ErrorElement errorResponse = new ErrorElement();
		List<ErrorElement> errorList = new ArrayList<>();
		BaseResponse baseResponse = new BaseResponse();
		errorResponse.setMessage(message);
		errorResponse.setId(code);
		errorResponse.setLevel(ServiceConstants.LEVEL_ERROR);
		errorList.add(errorResponse);
		baseResponse.setErrors(errorList);
		baseResponse.setData(new JSONArray());
		return baseResponse;
	}

	private static List<String> getListValue(String value) {
		List<String> listValue = new ArrayList<>();
		try {
			if (null != value && !value.isEmpty()) {
				listValue = Arrays.asList(value.split("\\s*,\\s*"));
			}
		} catch (Exception e) {
			printException(e, "getListValue");
		}
		return listValue;
	}

	private static String getInternalId(String internalId) {
		try {
			if (null != internalId && !internalId.isEmpty()) {
				if ((internalId.startsWith("AppGenerated"))) {
					internalId = null;
				}
			}
		} catch (Exception e) {
			printException(e, "getInternalId");
		}
		return internalId;
	}
}

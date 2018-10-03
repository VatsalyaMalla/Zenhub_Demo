package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

import java.util.ArrayList;
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

public class UpdatePunchListItems {

	static Logger logger = Logger.getLogger(UpdatePunchListItems.class.getName());

	/**
	 * 
	 * @param ncrRequest
	 */
	public static String processUpdatePunchListItems(String punchListItem, String token) {

		String userId;
		String locationId;
		String visitId;
		String response = "";
		JSONArray dataArray = new JSONArray();
		JSONObject dataObject = new JSONObject();
		JSONObject jsonRequest = new JSONObject();

		try {
			dataObject = (JSONObject) JSONValue.parse(punchListItem);
			dataArray = (JSONArray) dataObject.get("data");
			jsonRequest = (JSONObject) dataArray.get(0);
			logger.severe("User request json :" + jsonRequest);
			locationId = (String) jsonRequest.get("locationId");
			visitId = (String) jsonRequest.get("visitId");
			userId = (String) jsonRequest.get("userId");
			String visitStatus = (String) jsonRequest.get("visitStatus");
			logger.severe("visitId 1:" + visitId);
			logger.severe("locationId 1:" + locationId);
			String sorOutput = getUserVisitsSOR(userId, token);
			logger.severe("sorOutput value :" + sorOutput);
			if (null != sorOutput && !"".equals(sorOutput)) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

				UserRequestByIdSORData[] userObjGetRequestByID = mapper.readValue(sorOutput.toString(), UserRequestByIdSORData[].class);
				if (null == userObjGetRequestByID || userObjGetRequestByID.length == 0) {
					logger.severe("visit object for the given id is empty.");
				} else {
					for (int i = 0; i < userObjGetRequestByID.length; i++) {
						Object idEvent = (null != userObjGetRequestByID[i].getID_EVENT() ? userObjGetRequestByID[i].getID_EVENT() : "");
						Object idForm = (null != userObjGetRequestByID[i].getID_FORM() ? userObjGetRequestByID[i].getID_FORM() : "");
						Object idLocation = (null != userObjGetRequestByID[i].getID_LOCATION() ? userObjGetRequestByID[i].getID_LOCATION() : "");
						logger.severe("idEvent.toString() :" + idEvent.toString());
						logger.severe("idLocation.toString() :" + idLocation.toString());
						logger.severe("visitId :" + visitId);
						logger.severe("locationId :" + locationId);
						if (idLocation.toString().equalsIgnoreCase(locationId) && idEvent.toString().equalsIgnoreCase(visitId)) {
							String formOutput = getSingleVisitData(idForm.toString(), idLocation.toString(), idEvent.toString(), userId, token);
							if (null != formOutput && !formOutput.isEmpty()) {
								JSONObject visitSORJson = (JSONObject) JSONValue.parse(formOutput);
								if (null == visitSORJson || visitSORJson.isEmpty()) {
									logger.severe("VisitAPI data is empty for the obtained idForm,idLocation,idEvent and idUser.");
								} else {
									response = sendRequestToUpdatePunchListItems(visitSORJson, idForm.toString(), idLocation.toString(), jsonRequest, token, visitStatus);
								}
							} else {
								logger.severe("formOutput is empty or null.");
							}
						} else {
							logger.severe("Location id or visit id does not match.");
						}
					}
				}
			} else {
				logger.severe("Object value is empty.");
			}

		} catch (Exception e) {
			printException(e, "processUpdatePunchListItems");
		}
		logger.severe("response value :" + response);
		return response;
	}

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
	public static String sendRequestToUpdatePunchListItems(JSONObject visitsAPIData, String locId, String id, JSONObject jsonRequest, String token, String visitStatus) {
		JSONObject dataObject = new JSONObject();
		Object typeId;
		String typeIdString;
		String response = "";
		String punchListId = "2016091326";
		String questionId = "";
		String sorPunchListId = "";
		String iosStatus = "";
		String iosInspectorComments = "";
		String sectionId = "";
		String iosPunchListId = "";
		boolean set = false;
		String statusString = "";
		String iosReason = "";
		String iosDescription = "";
		String iosOtherFactors = "";
		String iosReferences = "";
		String iosIncidentDescription = "";
		String iosCorrectiveAction = "";
		String iosPersonResponsible = "";
		String iosClosingDate = "";
		String iosAdditionalComments = "";
		String iosHoursSpent = "";
		Object iosCritical;
		String iosItemDescription;
		List<String> associatedItems;
		List<JSONObject> cardListObj = new ArrayList<>();
		JSONObject listQuestions = new JSONObject();
		JSONObject sectionList = new JSONObject();
		JSONObject newCardObject = new JSONObject();
		JSONObject cardList = new JSONObject();
		int cardListSize;
		String categoryId;
		List<JSONObject> categoriesList = (List<JSONObject>) visitsAPIData.get("listCategories");
		try {
			for (int i = 0; i < categoriesList.size(); i++) {
				JSONObject listCategories = categoriesList.get(i);
				typeId = listCategories.get("typeId");
				categoryId = (String) listCategories.get("categoryId");
				if (typeId.toString().equalsIgnoreCase(punchListId)) {
					cardListObj = (List<JSONObject>) listCategories.get("listCards");
					cardListSize = cardListObj.size();
					iosPunchListId = (String) jsonRequest.get("punchListItemId");
					if (!(iosPunchListId.startsWith("AppGenerated"))) {
						for (int j = 0; j < cardListObj.size(); j++) {
							cardList = cardListObj.get(j);
							List<JSONObject> sectionListObj = (List<JSONObject>) cardList.get("listSections");
							for (int m = 0; m < sectionListObj.size(); m++) {
								sectionList = sectionListObj.get(m);
								Object extSecId = sectionList.get("internalId");
								if (iosPunchListId.equalsIgnoreCase(extSecId.toString())) {
									Object categoryInternal = listCategories.get("internalId");
									Object cardInternal = cardList.get("internalId");
									if (null != categoryInternal && "-1".equalsIgnoreCase(categoryInternal.toString())) {
										listCategories.put("internalId", null);
									}
									if (null != cardInternal && "-1".equalsIgnoreCase(cardInternal.toString())) {
										cardList.put("internalId", null);
									}

									List<JSONObject> listQuestionsObj = (List<JSONObject>) sectionList.get("listQuestions");
									for (int p = 0; p < listQuestionsObj.size(); p++) {
										listQuestions = listQuestionsObj.get(p);
										questionId = (String) listQuestions.get("questionId");
										if (questionId.endsWith("-Question2")) {
											associatedItems = (List<String>) jsonRequest.get("associatedItemsId");
											if (null != associatedItems && !associatedItems.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = UpdatePunchListItemsQuestions.question2(associatedItems, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question1")) {
											iosReferences = (String) jsonRequest.get("references");
											if (null != iosReferences && !iosReferences.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = UpdatePunchListItemsQuestions.question1(iosReferences, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question0")) {
											iosItemDescription = (String) jsonRequest.get("itemDescription");
											if (null != iosItemDescription && !iosItemDescription.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = UpdatePunchListItemsQuestions.question0(iosItemDescription, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question3")) {
											iosCorrectiveAction = (String) jsonRequest.get("correctiveAction");
											if (null != iosCorrectiveAction && !iosCorrectiveAction.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = UpdatePunchListItemsQuestions.question3(iosCorrectiveAction, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question4")) {
											iosPersonResponsible = (String) jsonRequest.get("personResponsible");
											if (null != iosPersonResponsible && !iosPersonResponsible.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = UpdatePunchListItemsQuestions.question4(iosPersonResponsible, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question5")) {
											iosClosingDate = (String) jsonRequest.get("closingDate");
											if (null != iosClosingDate && !iosClosingDate.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = UpdatePunchListItemsQuestions.question5(iosClosingDate, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question6")) {
											iosAdditionalComments = (String) jsonRequest.get("additionalComments");
											if (null != iosAdditionalComments && !iosAdditionalComments.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = UpdatePunchListItemsQuestions.question6(iosAdditionalComments, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question7")) {
											iosStatus = (String) jsonRequest.get("status");
											if (null != iosStatus && !iosStatus.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = UpdatePunchListItemsQuestions.question7(iosStatus, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question8")) {
											iosHoursSpent = (String) jsonRequest.get("hoursSpent");
											if (null != iosHoursSpent && !iosHoursSpent.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = UpdatePunchListItemsQuestions.question8(iosHoursSpent, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question9")) {
											if (null != jsonRequest.get("critical")) {
												iosCritical = jsonRequest.get("critical");
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												List<JSONObject> listPossibleAnswers = (List<JSONObject>) listQuestions.get("ListPossibleAnswers");
												answer = UpdatePunchListItemsQuestions.question9(iosCritical, answer, listPossibleAnswers);
												listQuestions.put("answer", answer);
											}
										}
									}
									set = true;
									break;
								} else {
									logger.info("iosIncidents id is different from sorIncidents id.");
								}
							}
						}
					} else {
						String url = URLUtility.getRequiredUrl(ServiceConstants.PUNCH_LIST_CATEGORY_TEMPLATE);
						newCardObject = UpdateQuestions.getCategoryList(jsonRequest, categoryId, cardListSize, 2, url, updatePunchListItemsMap(), token);
						List<JSONObject> sectionListForExistingCardList = (List<JSONObject>) newCardObject.get("listSections");
						JSONObject sectionListForExistingCardObject = sectionListForExistingCardList.get(0);
						Object internal = listCategories.get("internalId");

						if (cardListObj.size() > 0) {
							JSONObject sectionListForCardList = (JSONObject) cardListObj.get(0);
							List<JSONObject> secList = (List<JSONObject>) sectionListForCardList.get("listSections");
							int sectionSize = secList.size();
							int newSectionIdValue = sectionSize;
							String newSectionId = categoryId + "-" + "Card0" + "-Section" + newSectionIdValue;
							sectionListForExistingCardObject.put("sectionId", newSectionId);
							sectionListForExistingCardObject.put("displayOrder", sectionSize);
							sectionListForExistingCardObject.put("displayName", sectionSize + "-" + "PunchList");
							List<JSONObject> listQuestionList = (List<JSONObject>) sectionListForExistingCardObject.get("listQuestions");
							for (int z = 0; z < listQuestionList.size(); z++) {
								JSONObject questionObject = listQuestionList.get(z);
								String newQuestionId = newSectionId + "-Question" + z;
								questionObject.put("questionId", newQuestionId);
							}

							secList.add(sectionListForExistingCardObject);
							if (null != internal && "-1".equalsIgnoreCase(internal.toString())) {
								listCategories.put("internalId", null);
							}
							List<JSONObject> card = (List<JSONObject>) listCategories.get("listCards");
							JSONObject cardObject = card.get(0);
							Object internalId = cardObject.get("internalId");
							logger.severe("internalId value in punch :" + internalId);
							if (null != internalId && "-1".equalsIgnoreCase(internalId.toString())) {
								cardObject.put("internalId", null);
							}
							set = true;
						} else {
							if (null != internal && "-1".equalsIgnoreCase(internal.toString())) {
								listCategories.put("internalId", null);
							}
							String displayOrder = (String) sectionListForExistingCardObject.get("displayOrder");
							sectionListForExistingCardObject.put("displayName", displayOrder + "-" + "PunchList");
							cardListObj.add(newCardObject);
							set = true;
						}
					}
				} else {
					logger.severe("Type id does not match in update punchListItems.");
				}
			}
			String requestUrlForPunchListItems = URLUtility.getRequiredUrl(ServiceConstants.REQUEST_URL);
			if (set) {
				visitsAPIData.put("stateId", null);
				visitsAPIData.put("state", visitStatus);
				logger.severe("Before setting it in the request for update punchListItems :" + visitsAPIData);
				response = URLUtility.getSORDataWithHeaderParamsForUpdateNCR(requestUrlForPunchListItems, visitsAPIData.toString(), token);
			} else {
				logger.info("Update did not happen for the given id's in punch list items.");
			}
		} catch (Exception e) {
			printException(e, "sendRequestToUpdatePunchListItems");
		}

		return response;
	}

	public static Map<String, String> updatePunchListItemsMap() {
		Map<String, String> questionsKeyMap = new HashMap<>();
		questionsKeyMap.put("Question0", "itemDescription");
		questionsKeyMap.put("Question1", "references");
		questionsKeyMap.put("Question2", "associatedItemsId");
		questionsKeyMap.put("Question3", "correctiveAction");
		questionsKeyMap.put("Question4", "personResponsible");
		questionsKeyMap.put("Question5", "closingDate");
		questionsKeyMap.put("Question6", "additionalComments");
		questionsKeyMap.put("Question7", "status");
		questionsKeyMap.put("Question8", "hoursSpent");
		questionsKeyMap.put("Question9", "critical"); // object
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

	public static String processUpdateNewPunchListItems(String punchListItem, String token) {

		List<JSONObject> iosDataArray = new ArrayList<>();
		JSONObject iosDataObject = new JSONObject();
		JSONObject iosJsonRequest = new JSONObject();
		JSONObject sorRequestObject = new JSONObject();
		List<JSONObject> sorRequestList = new ArrayList<>();
		String response = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			iosDataObject = (JSONObject) JSONValue.parse(punchListItem);
			logger.severe("JSONObject retrieved after successful parsing in new punchList update." + iosDataObject);
			iosDataArray = (JSONArray) iosDataObject.get("data");
			for (int i = 0; i < iosDataArray.size(); i++) {
				sorRequestObject = new JSONObject();
				iosJsonRequest = (JSONObject) iosDataArray.get(i);
				String internalId = (String) iosJsonRequest.get("punchListItemId");
				internalId = getInternalId(internalId);
				validateAction((String) iosJsonRequest.get("action"));

				List<String> associatedItemsId = (List<String>) iosJsonRequest.get("associatedItemsId");
				String references = (String) iosJsonRequest.get("references");
				String itemDescription = (String) iosJsonRequest.get("itemDescription");
				String correctiveAction = (String) iosJsonRequest.get("correctiveAction");
				String personResponsible = (String) iosJsonRequest.get("personResponsible");
				String closingDate = (String) iosJsonRequest.get("closingDate");
				String additionalComments = (String) iosJsonRequest.get("additionalComments");
				String status = (String) iosJsonRequest.get("status");
				String hoursSpent = (String) iosJsonRequest.get("hoursSpent");
				String displayName = (String) iosJsonRequest.get("displayName");
				Object critical = iosJsonRequest.get("critical");
				String locationId = (String) iosJsonRequest.get("locationId");
				String visitId = (String) iosJsonRequest.get("visitId");
				String createdDate = (String) iosJsonRequest.get("createdDate");
				String resolvedDate = (String) iosJsonRequest.get("resolvedDate");
				List<String> photos = (List<String>) iosJsonRequest.get("photos");
				sorRequestObject.put("InternalID", null != internalId ? Integer.parseInt(internalId.toString()) : null);
				sorRequestObject.put("Items", associatedItemsId);
				sorRequestObject.put("References", references);
				sorRequestObject.put("CorrectiveAction", correctiveAction);
				sorRequestObject.put("TroubleDescription", itemDescription);
				sorRequestObject.put("Responsible", personResponsible);
				sorRequestObject.put("ClosingDate", closingDate);
				sorRequestObject.put("AdditionalComments", additionalComments);
				sorRequestObject.put("Status", status);
				sorRequestObject.put("Action", (String) iosJsonRequest.get("action"));
				sorRequestObject.put("HoursSpent", hoursSpent);
				sorRequestObject.put("Critical", null != critical ? Integer.parseInt(critical.toString()) : null);
				sorRequestObject.put("LocationID", locationId);
				sorRequestObject.put("EventID", visitId);
				sorRequestObject.put("CreateDate", createdDate);
				sorRequestObject.put("ResolvedDate", resolvedDate);
				sorRequestObject.put("ActivityName", displayName);
				sorRequestObject.put("Photos", photos);
				sorRequestList.add(sorRequestObject);
			}
			logger.severe("SORList retrieved after successful parsing in new punchList update." + sorRequestList);
			response = URLUtility.getSORDataWithHeaderParamsForUpdateNCR(URLUtility.getRequiredUrl(ServiceConstants.IOS_PUNCH_LIST), sorRequestList.toString(), token);
		} catch (TecnicasException te) {
			throw te;
		} catch (Exception e) {
			printException(e, "processUpdateNewNCR");
		}
		return response;
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
}

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

public class UpdateIncidents {

	static Logger logger = Logger.getLogger(UpdateIncidents.class.getName());

	/**
	 * 
	 * @param ncrRequest
	 */
	public static String processUpdateIncidents(String incidentRequest, String token) {

		logger.severe("Inside processUpdateIncidents.");
		String userId;
		String locationId;
		String visitId;
		String response = "";
		JSONArray dataArray = new JSONArray();
		JSONObject dataObject = new JSONObject();
		JSONObject jsonRequest = new JSONObject();

		try {
			dataObject = (JSONObject) JSONValue.parse(incidentRequest);
			dataArray = (JSONArray) dataObject.get("data");
			jsonRequest = (JSONObject) dataArray.get(0);
			logger.severe("jsonRequest in processUpdateIncidents :" + jsonRequest);
			locationId = (String) jsonRequest.get("locationId");
			visitId = (String) jsonRequest.get("visitId");
			userId = (String) jsonRequest.get("userId");
			String visitStatus = (String) jsonRequest.get("visitStatus");
			String sorOutput = getUserVisitsSOR(userId, token);
			logger.severe("sorOutput in processUpdateIncidents :" + sorOutput);
			if (null != sorOutput && !"".equals(sorOutput)) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

				UserRequestByIdSORData[] userObjGetRequestByID = mapper.readValue(sorOutput.toString(), UserRequestByIdSORData[].class);
				if (null == userObjGetRequestByID || userObjGetRequestByID.length == 0) {
					logger.info("visit object for the given id is empty.");
				} else {
					for (int i = 0; i < userObjGetRequestByID.length; i++) {
						Object idEvent = (null != userObjGetRequestByID[i].getID_EVENT() ? userObjGetRequestByID[i].getID_EVENT() : "");
						Object idForm = (null != userObjGetRequestByID[i].getID_FORM() ? userObjGetRequestByID[i].getID_FORM() : "");
						Object idLocation = (null != userObjGetRequestByID[i].getID_LOCATION() ? userObjGetRequestByID[i].getID_LOCATION() : "");
						if (idLocation.toString().equalsIgnoreCase(locationId) && idEvent.toString().equalsIgnoreCase(visitId)) {
							logger.severe("Location id and event id are matched for updateIncidents.");
							String formOutput = getSingleVisitData(idForm.toString(), idLocation.toString(), idEvent.toString(), userId, token);
							logger.severe("formOutput value for updateIncidents :" + formOutput);
							if (null != formOutput && !"".equals(formOutput)) {
								JSONObject visitSORJson = (JSONObject) JSONValue.parse(formOutput);
								if (null == visitSORJson || visitSORJson.isEmpty()) {
									logger.info("VisitAPI data is empty for the obtained idForm,idLocation,idEvent and idUser.");
								} else {
									response = sendRequestToUpdateIncidents(visitSORJson, idForm.toString(), idLocation.toString(), jsonRequest, token, visitStatus);
								}
							} else {
								logger.info("formOutput is empty or null.");
							}
						} else {
							logger.info("Location id or visit id does not match.");
						}
					}
				}
			} else {
				logger.info("Object value is empty.");
			}

		} catch (Exception e) {
			printException(e, "processUpdateIncidents");
		}
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
	public static String sendRequestToUpdateIncidents(JSONObject visitsAPIData, String locId, String id, JSONObject jsonRequest, String token, String visitStatus) {
		JSONObject dataObject = new JSONObject();
		Object typeId;
		String typeIdString;
		String response = "";
		String incidentsId = "2016091325";
		String questionId = "";
		String sorPunchListId = "";
		String iosStatus = "";
		String iosInspectorComments = "";
		boolean set = false;
		String sectionId = "";
		String iosIncidentstId = "";
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
		String cardName = "";
		Object iosCritical;
		int cardListSize;
		String categoryId;
		List<String> associatedItems;
		List<JSONObject> cardListObj = new ArrayList<>();
		JSONObject listQuestions = new JSONObject();
		JSONObject sectionList = new JSONObject();
		JSONObject newCardObject = new JSONObject();
		JSONObject cardList = new JSONObject();
		UpdateIncidentsQuestions updateIncidentsQuestions = new UpdateIncidentsQuestions();
		List<JSONObject> categoriesList = (List<JSONObject>) visitsAPIData.get("listCategories");
		try {
			String displayName = (String) jsonRequest.get("displayName");
			for (int i = 0; i < categoriesList.size(); i++) {
				JSONObject listCategories = categoriesList.get(i);
				typeId = (long) listCategories.get("typeId");
				categoryId = (String) listCategories.get("categoryId");
				if (typeId.toString().equalsIgnoreCase(incidentsId)) {
					cardListObj = (List<JSONObject>) listCategories.get("listCards");
					cardListSize = cardListObj.size();
					iosIncidentstId = (String) jsonRequest.get("incidentId");
					if (!(iosIncidentstId.startsWith("AppGenerated"))) {
						logger.severe("Id is not a new request for card creation in update incidents.");
						for (int j = 0; j < cardListObj.size(); j++) {
							logger.severe("cardListObj.size() for update incidents." + cardListObj.size());
							cardList = cardListObj.get(j);
							List<JSONObject> sectionListObj = (List<JSONObject>) cardList.get("listSections");
							logger.severe("sectionListObj.size() in update incidents :" + sectionListObj.size());
							for (int m = 0; m < sectionListObj.size(); m++) {
								sectionList = sectionListObj.get(m);
								sectionId = (String) sectionList.get("sectionId");
								Object extSecId = sectionList.get("internalId");
								logger.severe("sectionId in update incidents :" + sectionId);
								if (iosIncidentstId.equalsIgnoreCase(extSecId.toString())) {
									Object categoryInternal = listCategories.get("internalId");
									Object cardInternal = cardList.get("internalId");
									if (null != categoryInternal && "-1".equalsIgnoreCase(categoryInternal.toString())) {
										listCategories.put("internalId", null);
									}
									if (null != cardInternal && "-1".equalsIgnoreCase(cardInternal.toString())) {
										cardList.put("internalId", null);
									}
									logger.severe("sectionId of SOR and iOS matched in update incidents.");
									List<JSONObject> listQuestionsObj = (List<JSONObject>) sectionList.get("listQuestions");
									logger.severe("listQuestionsObj.size() in update incidents :" + listQuestionsObj.size());
									for (int p = 0; p < listQuestionsObj.size(); p++) {
										listQuestions = listQuestionsObj.get(p);
										questionId = (String) listQuestions.get("questionId");
										if (questionId.endsWith("-Question0")) {
											associatedItems = (List<String>) jsonRequest.get("associatedItemsId");
											if (null != associatedItems && !associatedItems.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = updateIncidentsQuestions.question0(associatedItems, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question1")) {
											iosReferences = (String) jsonRequest.get("references");
											if (null != iosReferences && !iosReferences.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = updateIncidentsQuestions.question1(iosReferences, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question2")) {
											iosIncidentDescription = (String) jsonRequest.get("incidentDescription");
											if (null != iosIncidentDescription && !iosIncidentDescription.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = updateIncidentsQuestions.question2(iosIncidentDescription, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question3")) {
											iosCorrectiveAction = (String) jsonRequest.get("correctiveAction");
											if (null != iosCorrectiveAction && !iosCorrectiveAction.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = updateIncidentsQuestions.question3(iosCorrectiveAction, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question4")) {
											iosPersonResponsible = (String) jsonRequest.get("personResponsible");
											if (null != iosPersonResponsible && !iosPersonResponsible.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = updateIncidentsQuestions.question4(iosPersonResponsible, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question5")) {
											iosClosingDate = (String) jsonRequest.get("closingDate");
											if (null != iosClosingDate && !iosClosingDate.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = updateIncidentsQuestions.question5(iosClosingDate, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question6")) {
											iosAdditionalComments = (String) jsonRequest.get("additionalComments");
											if (null != iosAdditionalComments && !iosAdditionalComments.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = updateIncidentsQuestions.question6(iosAdditionalComments, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question7")) {
											iosStatus = (String) jsonRequest.get("status");
											if (null != iosStatus && !iosStatus.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = updateIncidentsQuestions.question7(iosStatus, answer);
												listQuestions.put("answer", answer);
											}

										} else if (questionId.endsWith("-Question8")) {
											iosHoursSpent = (String) jsonRequest.get("hoursSpent");
											if (null != iosHoursSpent && !iosHoursSpent.isEmpty()) {
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												answer = updateIncidentsQuestions.question8(iosHoursSpent, answer);
												listQuestions.put("answer", answer);
											}
										} else if (questionId.endsWith("-Question9")) {
											if (null != jsonRequest.get("critical")) {
												iosCritical = jsonRequest.get("critical");
												JSONObject answer = (JSONObject) listQuestions.get("answer");
												List<JSONObject> listPossibleAnswers = (List<JSONObject>) listQuestions.get("ListPossibleAnswers");
												answer = updateIncidentsQuestions.question9(iosCritical, answer, listPossibleAnswers);
												listQuestions.put("answer", answer);
											}
										}
									}
									set = true;
									break;
								} else {
									logger.severe("iosIncidents id is different from sorIncidents id.");
								}
							}
						}
					} else {
						logger.severe("Inside else case to create new card in update incidents.");
						String url = URLUtility.getRequiredUrl(ServiceConstants.INCIDENT_CATEGORY_TEMPLATE);
						newCardObject = UpdateQuestions.getCategoryList(jsonRequest, categoryId, cardListSize, 3, url, updateIncidentMap(), token);
						List<JSONObject> sectionListForExistingCardList = (List<JSONObject>) newCardObject.get("listSections");
						JSONObject sectionListForExistingCardObject = sectionListForExistingCardList.get(0);
						Object internal = listCategories.get("internalId");

						if (cardListObj.size() > 0) {
							JSONObject sectionListForCardList = (JSONObject) cardListObj.get(0);
							List<JSONObject> secList = (List<JSONObject>) sectionListForCardList.get("listSections");
							int sectionSize = secList.size();
							int newSectionIdValue = sectionSize;
							sectionListForExistingCardObject.put("displayOrder", sectionSize);
							sectionListForExistingCardObject.put("displayName", displayName);
							String newSectionId = categoryId + "-" + "Card0" + "-Section" + newSectionIdValue;
							sectionListForExistingCardObject.put("sectionId", newSectionId);
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
							if (null != internalId && "-1".equalsIgnoreCase(internalId.toString())) {
								cardObject.put("internalId", null);
							}
							set = true;
						} else {
							if (null != internal && "-1".equalsIgnoreCase(internal.toString())) {
								listCategories.put("internalId", null);
							}
							sectionListForExistingCardObject.put("displayName", displayName);
							cardListObj.add(newCardObject);
							set = true;
						}
					}
				} else {
					logger.severe("Type id does not match in update incidents.");
				}

			}
			String requestUrlForIncidents = URLUtility.getRequiredUrl(ServiceConstants.REQUEST_URL);
			if (set) {
				visitsAPIData.put("stateId", null);
				visitsAPIData.put("state", visitStatus);
				logger.severe("Before setting it in the request for update incidents :" + visitsAPIData);
				response = URLUtility.getSORDataWithHeaderParamsForUpdateNCR(requestUrlForIncidents, visitsAPIData.toString(), token);
			} else {
				logger.info("Update did not happen for the id's in incidents.");
			}
		} catch (Exception e) {
			printException(e, "sendRequestToUpdateIncidents");
		}
		return response;
	}

	public static Map<String, String> updateIncidentMap() {
		Map<String, String> questionsKeyMap = new HashMap<String, String>();
		questionsKeyMap.put("Question0", "associatedItemsId");
		questionsKeyMap.put("Question1", "references");
		questionsKeyMap.put("Question2", "incidentDescription");
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

	public static String processUpdateNewIncidents(String incidentRequest, String token) {

		List<JSONObject> iosDataArray = new ArrayList<>();
		JSONObject iosDataObject = new JSONObject();
		JSONObject iosJsonRequest = new JSONObject();
		JSONObject sorRequestObject = new JSONObject();
		List<JSONObject> sorRequestList = new ArrayList<>();
		List<String> incidentTypeList = new ArrayList<>();
		String response = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			iosDataObject = (JSONObject) JSONValue.parse(incidentRequest);
			logger.severe("JSONObject retrieved after successful parsing in new incident update." + iosDataObject);
			iosDataArray = (JSONArray) iosDataObject.get("data");
			for (int i = 0; i < iosDataArray.size(); i++) {
				sorRequestObject = new JSONObject();
				iosJsonRequest = (JSONObject) iosDataArray.get(i);
				String internalId = (String) iosJsonRequest.get("incidentId");
				internalId = getInternalId(internalId);
				validateAction((String) iosJsonRequest.get("action"));

				List<String> associatedItemsId = (List<String>) iosJsonRequest.get("associatedItemsId");
				String incidentType = (String) iosJsonRequest.get("incidentType");
				incidentTypeList = getListValue(incidentType);
				String references = (String) iosJsonRequest.get("references");
				String incidentDescription = (String) iosJsonRequest.get("incidentDescription");
				String correctiveAction = (String) iosJsonRequest.get("correctiveAction");
				String personResponsible = (String) iosJsonRequest.get("personResponsible");
				String closingDate = (String) iosJsonRequest.get("closingDate");
				String additionalComments = (String) iosJsonRequest.get("additionalComments");
				String status = (String) iosJsonRequest.get("status");
				String hoursSpent = (String) iosJsonRequest.get("hoursSpent");
				Object critical = iosJsonRequest.get("critical");
				String locationId = (String) iosJsonRequest.get("locationId");
				String visitId = (String) iosJsonRequest.get("visitId");
				String createdDate = (String) iosJsonRequest.get("createdDate");
				String resolvedDate = (String) iosJsonRequest.get("resolvedDate");
				String displayName = (String) iosJsonRequest.get("displayName");
				List<String> photos = (List<String>) iosJsonRequest.get("photos");
				sorRequestObject.put("InternalID", null != internalId ? Integer.parseInt(internalId.toString()) : null);
				sorRequestObject.put("Items", associatedItemsId);
				sorRequestObject.put("Action", (String) iosJsonRequest.get("action"));
				sorRequestObject.put("References", references);
				sorRequestObject.put("CorrectiveAction", correctiveAction);
				sorRequestObject.put("TroubleDescription", incidentDescription);
				sorRequestObject.put("Responsible", personResponsible);
				sorRequestObject.put("IncidentType", incidentTypeList);
				sorRequestObject.put("ClosingDate", closingDate);
				sorRequestObject.put("AdditionalComments", additionalComments);
				sorRequestObject.put("Status", status);
				sorRequestObject.put("HoursSpent", hoursSpent);
				sorRequestObject.put("Photos", photos);
				sorRequestObject.put("Critical", null != critical ? Integer.parseInt(critical.toString()) : null);
				sorRequestObject.put("LocationID", locationId);
				sorRequestObject.put("EventID", visitId);
				sorRequestObject.put("CreateDate", createdDate);
				sorRequestObject.put("ResolvedDate", resolvedDate);
				sorRequestObject.put("ActivityName", displayName);
				sorRequestList.add(sorRequestObject);
			}
			logger.severe("SORList retrieved after successful parsing in new incident update." + sorRequestList);
			response = URLUtility.getSORDataWithHeaderParamsForUpdateNCR(URLUtility.getRequiredUrl(ServiceConstants.IOS_INCIDENT), sorRequestList.toString(), token);
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

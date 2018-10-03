package com.ibm.mobilefirst.industrial.materialsinspect.adapter.notes.demo.data;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.Utils.URLUtility;
import com.Utils.ValidateUtility;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.ibm.mobilefirst.beans.BaseResponse;
import com.ibm.mobilefirst.beans.ErrorElement;
import com.ibm.mobilefirst.exception.TecnicasException;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.notes.NotesAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.exception.AdapterExceptionHandler;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.DeleteNotesData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.DeleteNotesIOS;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.Error;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.LISTCOMMENT;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.LISTFROM;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.LISTUSER;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.ListCommentsIOS;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.ListUserIOS;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.LocationNotesIOS;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.NotesByLocationIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo.NotesUserByLocationId;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.response.APIResponseBuilder;

public class NotesDataAdapter implements NotesAdapter {
	static Logger logger = Logger.getLogger(NotesDataAdapter.class.getName());
	static Properties properties = null;
	AdapterExceptionHandler adapterExceptionHandler = new AdapterExceptionHandler();

	@Override
	public Response getNotesId(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String locationId, String token) {
		Response response = null;
		JSONObject result = new JSONObject();
		List<JSONObject> notesList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try {
			boolean idValidation = ValidateUtility.validateIds(locationId);
			if (idValidation) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.LOCATION_ID_CHECK_CODE, ServiceConstants.SUCCESS_MESSAGE,
						ServiceConstants.ERROR_CODE_NODATAFOUND, ServiceConstants.ERROR_LOCATION_NOTFOUND, ServiceConstants.LEVEL_ERROR);
				return response;
			}
			String sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.NOTES_ID_URL) + locationId, token);
			if (sorOutputString.contains(ServiceConstants.EXCEPTION_TYPE)) {
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
				return response;
			} else if (sorOutputString.contains(ServiceConstants.CLASS_NAME)) {
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
				return response;
			}
			NotesUserByLocationId[] notesObj = mapper.readValue(sorOutputString, NotesUserByLocationId[].class);
			if (null == notesObj || notesObj.length == 0) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.LOCATION_ID_CHECK_CODE, ServiceConstants.SUCCESS_MESSAGE,
						ServiceConstants.ERROR_CODE_NODATAFOUND, ServiceConstants.NOTES_INFO_EMPTY, ServiceConstants.LEVEL_ERROR);
				return response;
			} else {
				Object value = getNotesList(notesObj, apiVersion, locationId);
				if (value instanceof Response) {
					response = (Response) value;
					return response;
				}
				notesList = (List<JSONObject>) value;
			}
			if (null == notesList || notesList.isEmpty()) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, 400, ServiceConstants.ERROR_MESSAGE_ERROR, "400", "SOR data issue in getNotesId",
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			result.put(ServiceConstants.DATA, notesList);
			result.put(ServiceConstants.ERRORS, new JSONArray());

			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK, ServiceConstants.SUCCESS_MESSAGE, result);
		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.GET_NOTES);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_NOTES);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.USER_ID_CHECK_MESSAGE, ServiceConstants.SOR_LOCATIONID_MESSAGE, ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	public static Object getNotesList(NotesUserByLocationId[] notesObj, String apiVersion, String locationId) {
		List<JSONObject> userInfo = new ArrayList<>();
		JSONObject dataObject;
		Response response = null;
		try {
			for (int i = 0; i < notesObj.length; i++) {
				dataObject = new JSONObject();
				dataObject.put(ServiceConstants.ID, null != notesObj[i].getId() ? notesObj[i].getId().toString() : null);
				if (null == notesObj[i].getUserId() || notesObj[i].getUserId().toString().isEmpty()) {
					response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.LOCATION_ID_CHECK_CODE, ServiceConstants.SUCCESS_MESSAGE,
							ServiceConstants.ERROR_CODE_NODATAFOUND, ServiceConstants.NOTES_ID_EMPTY, ServiceConstants.LEVEL_ERROR);
					return response;
				}
				dataObject.put(ServiceConstants.USER_ID, null != notesObj[i].getUserId() ? notesObj[i].getUserId().toString() : null);
				dataObject.put(ServiceConstants.USER_NAME, null != notesObj[i].getUserName() ? notesObj[i].getUserName() : null);
				dataObject.put(ServiceConstants.LOCATION_ID, locationId);
				userInfo.add(dataObject);
			}
		} catch (Exception e) {
			printExceptions(e, ServiceConstants.GET_NOTES_LIST);
		}
		return userInfo;
	}

	static void printExceptions(Exception e, String methodName) {
		logger.severe("Exception in " + methodName);
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append("\n");
		}
		logger.severe(sb.toString());
	}

	void printException(Exception e, String methodName) {
		logger.severe("Exception in " + methodName);
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append("\n");
		}
		logger.severe(sb.toString());
	}

	@Override
	public Response getNotesForLocation(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String locationId, String token) {
		Response response = null;
		JSONObject result = new JSONObject();
		try {
			validateLocationId(locationId);
			String notesURL = URLUtility.getRequiredUrl(ServiceConstants.NOTES_LOCATION_ID_URL) + locationId;
			String sorOutputStringForNotes = AdapterExceptionHandler.getSORData(notesURL, token);
			if (StringUtils.isBlank(sorOutputStringForNotes)) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.EMPTY_SOR_RESPONSE));
			}
			if (sorOutputStringForNotes.contains(ServiceConstants.EXCEPTION_TYPE)) {
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputStringForNotes, apiVersion);
				return response;
			} else if (sorOutputStringForNotes.contains("ClassName")) {
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringForNotes, apiVersion);
				return response;
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			NotesByLocationIdSORData sorNotes = mapper.readValue(sorOutputStringForNotes, NotesByLocationIdSORData.class);
			if (sorNotes == null) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.NOTES_NOTFOUND));
			}
			List<LISTFROM> listFrom = sorNotes.getLISTFROM();
			List<LISTFROM> listTo = sorNotes.getLISTTO();
			List<LocationNotesIOS> locationNotesListTo = getLocationNotes(listTo);
			List<LocationNotesIOS> locationNotesListFrom = getLocationNotes(listFrom);
			List<LocationNotesIOS> locationNotes = new ArrayList<>(locationNotesListTo);
			locationNotes.addAll(locationNotesListFrom);
			result.put(ServiceConstants.DATA, locationNotes);
			result.put(ServiceConstants.ERRORS, new JSONArray());
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK, ServiceConstants.SUCCESS_MESSAGE, result);
			return response;
		} catch (TecnicasException te) {
			throw te;
		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.GET_NOTES);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);

		} catch (Exception e) {
			printException(e, ServiceConstants.GET_NOTES);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.USER_ID_CHECK_MESSAGE, ServiceConstants.GET_NOTES_ERROR, ServiceConstants.LEVEL_ERROR);
		}
		logger.info("exit className :" + this.getClass().getName() + "methodName:" + "getNotesForLocation with locationId " + locationId);
		return response;
	}

	private void validateLocationId(String locationId) {
		if (StringUtils.isBlank(locationId)) {
			throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE,
					ServiceConstants.ERROR_LOCATION_ID_NULL));
		}
		if (!Pattern.compile(ServiceConstants.LOCATION_ID_PATTERN).matcher(locationId).matches()) {
			throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE,
					ServiceConstants.ERROR_LOCATION_NOTFOUND));
		}

	}

	private List<LocationNotesIOS> getLocationNotes(List<LISTFROM> listFrom) {
		List<LocationNotesIOS> locationNotes = new ArrayList<>();
		if (listFrom != null) {
			for (int i = 0; i < listFrom.size(); i++) {
				LocationNotesIOS locationNote = new LocationNotesIOS();
				locationNote.setCreationDate(listFrom.get(i).getCREATIONDATE());
				locationNote.setLocationId(Integer.toString(listFrom.get(i).getIDLOCATION()));
				locationNote.setNoteId(Integer.toString(listFrom.get(i).getIDNOTE()));
				locationNote.setOwnerId(Integer.toString(listFrom.get(i).getIDUSER()));
				locationNote.setOwnerName(listFrom.get(i).getNAMEUSER());
				locationNote.setPlannedDate(listFrom.get(i).getPLANNEDDATE());
				locationNote.setLocationName(listFrom.get(i).getNAMELOCATION());
				locationNote.setStatus(listFrom.get(i).getMARKED() ? ServiceConstants.NOTES_STATUS_CLOSED : ServiceConstants.NOTES_STATUS_OPEN);
				locationNote.setNotesTitle(listFrom.get(i).getTITLE());
				locationNote.setNotesDescription(listFrom.get(i).getTEXT());
				locationNote.setPriority(listFrom.get(i).getPRIORITY());
				List<LISTUSER> listUsers = listFrom.get(i).getLISTUSERS();
				List<ListUserIOS> listUserIOS = new ArrayList<>();
				for (int j = 0; j < listUsers.size(); j++) {
					ListUserIOS listUser = new ListUserIOS();
					listUser.setId(Integer.toString(listUsers.get(j).getID()));
					listUser.setUserId(Integer.toString(listUsers.get(j).getIDUSER()));
					listUser.setUserName(listUsers.get(j).getNAMEUSER());
					listUserIOS.add(listUser);
				}
				locationNote.setUserList(listUserIOS);
				List<LISTCOMMENT> listComments = listFrom.get(i).getLISTCOMMENTS();
				List<ListCommentsIOS> listCommentsIOS = new ArrayList<>();
				for (int k = 0; k < listComments.size(); k++) {
					ListCommentsIOS listComment = new ListCommentsIOS();
					listComment.setCommentId(Integer.toString(listComments.get(k).getIDCOMMENT()));
					listComment.setCreationDate(listComments.get(k).getCREATIONDATE());
					listComment.setText(listComments.get(k).getTEXT());
					listComment.setUserId(Integer.toString(listComments.get(k).getIDUSER()));
					listComment.setUserName(listComments.get(k).getNAMEUSER());
					listComment.setNoteId(Integer.toString(listFrom.get(i).getIDNOTE()));
					listCommentsIOS.add(listComment);
				}
				locationNote.setListcomments(listCommentsIOS);
				locationNotes.add(locationNote);
			}
		}
		return locationNotes;

	}

	@Override
	public Response postNotes(String contentType, String acceptLanguage, String userAgent, String apiVersion, String loggedInUser, String notesData, String token) {
		Response response = null;
		List<JSONObject> sorRequestList = new ArrayList<>();
		JSONArray dataArray = new JSONArray();
		JSONObject dataObject = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try {
			boolean idValidation = ValidateUtility.validateIds(notesData);
			if (idValidation) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY, ServiceConstants.LEVEL_ERROR);
				return response;
			}
			dataObject = (JSONObject) JSONValue.parse(notesData);
			dataArray = (JSONArray) dataObject.get(ServiceConstants.DATA);
			logger.severe("Request for postNotes from iOS :" + dataArray);
			List<JSONObject> sorReq = getSORRequestList(dataArray, sorRequestList, ServiceConstants.POST);

			logger.severe("Request for postNotes to SOR :" + sorReq);
			response = sendIOSResponse(apiVersion, sorReq, token);
		} catch (Exception e) {
			printException(e, ServiceConstants.POSTING_NOTES);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	private Response sendIOSResponse(String apiVersion, List<JSONObject> sorReq, String token) {
		JSONObject iOSResponse = new JSONObject();
		Response response = null;
		JSONObject result = new JSONObject();
		List<JSONObject> error = new ArrayList<>();
		try {
			JSONObject output = URLUtility.getSORDataWithHeaderParamsForUploadMedia(URLUtility.getRequiredUrl(ServiceConstants.POST_NOTES), sorReq.toString(), token);
			String code = (String) output.get("id");
			if ("200".equalsIgnoreCase(code)) {
				String value = (String) output.get("value");
				if (StringUtils.isBlank(value)) {
					value = ServiceConstants.POST_NOTES_OUTPUT;
					iOSResponse.put("message", value);
					error.add(iOSResponse);
					result.put(ServiceConstants.DATA, new JSONArray());
					result.put(ServiceConstants.ERRORS, error);
					response = APIResponseBuilder.sendSuccessResponse(apiVersion, 500, ServiceConstants.SUCCESS_MESSAGE, result);
					return response;
				}
				JSONArray arrayOutput = (JSONArray) JSONValue.parse(value);
				List<JSONObject> resultList = getIOSResponse(arrayOutput);
				result.put(ServiceConstants.DATA, resultList);
				result.put(ServiceConstants.ERRORS, new JSONArray());
			} else if ("403".equalsIgnoreCase(code)) {
				iOSResponse.put("message", "Authorization is not valid.Please login again");
				iOSResponse.put("id", code);
				iOSResponse.put("level", "ERROR");
				error.add(iOSResponse);
				result.put(ServiceConstants.DATA, new JSONArray());
				result.put(ServiceConstants.ERRORS, error);
			} else {
				iOSResponse.put("message", "SOR update failed for post notes");
				iOSResponse.put("id", code);
				iOSResponse.put("level", "ERROR");
				error.add(iOSResponse);
				result.put(ServiceConstants.DATA, new JSONArray());
				result.put(ServiceConstants.ERRORS, error);
			}
			logger.severe("iOS response for postNotes :" + result);
			int responseCode = Integer.parseInt(code);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode, ServiceConstants.SUCCESS_MESSAGE, result);
		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.SEND_IOS_RESPONSE);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.SEND_IOS_RESPONSE);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	private List<JSONObject> getIOSResponse(JSONArray arrayOutput) {
		List<JSONObject> resultList = new ArrayList<>();
		try {
			for (int l = 0; l < arrayOutput.size(); l++) {
				JSONObject resultObject = new JSONObject();
				List<JSONObject> usrList = new ArrayList<>();
				List<JSONObject> cmntList = new ArrayList<>();
				JSONObject arrayObject = (JSONObject) arrayOutput.get(l);
				resultObject.put(ServiceConstants.ID_NOTE_IOS, null != arrayObject.get(ServiceConstants.ID_NOTE) ? arrayObject.get(ServiceConstants.ID_NOTE).toString() : null);
				resultObject.put(ServiceConstants.ID_USER_IOS, null != arrayObject.get(ServiceConstants.ID_USER) ? arrayObject.get(ServiceConstants.ID_USER).toString() : null);
				resultObject.put(ServiceConstants.NAME_USER_IOS, arrayObject.get(ServiceConstants.NAME_USER));
				resultObject.put(ServiceConstants.CREATION_DATE_IOS, arrayObject.get(ServiceConstants.CREATION_DATE));
				resultObject.put(ServiceConstants.PLANNED_DATE_IOS, arrayObject.get(ServiceConstants.PLANNED_DATE));
				resultObject.put(ServiceConstants.ID_LOCATION_IOS, null != arrayObject.get(ServiceConstants.ID_LOCATION) ? arrayObject.get(ServiceConstants.ID_LOCATION).toString()
						: null);
				resultObject.put(ServiceConstants.NAME_LOCATION_IOS, arrayObject.get(ServiceConstants.NAME_LOCATION));
				resultObject.put(ServiceConstants.APP_GENERATED_ID, arrayObject.get(ServiceConstants.ID_APP_GENERATED));
				boolean value = (boolean) arrayObject.get(ServiceConstants.MARKED);
				resultObject.put(ServiceConstants.MARKED_IOS, getStatus(value));
				resultObject.put(ServiceConstants.TITLE_IOS, arrayObject.get(ServiceConstants.TITLE));
				resultObject.put(ServiceConstants.PRIORITY_IOS, arrayObject.get(ServiceConstants.PRIORITY));
				resultObject.put(ServiceConstants.NOTES_DESCRIPTION_TEXT, arrayObject.get(ServiceConstants.TEXT));
				List<JSONObject> uList = (List<JSONObject>) arrayObject.get(ServiceConstants.LIST_USERS);
				List<JSONObject> uListResponse = getUserListResponse(uList, usrList);
				List<JSONObject> cList = (List<JSONObject>) arrayObject.get(ServiceConstants.LIST_COMMENTS);
				List<JSONObject> cListResponse = getCommentListResponse(cList, cmntList);
				resultObject.put(ServiceConstants.LIST_USERS_IOS, uListResponse);
				resultObject.put(ServiceConstants.LIST_COMMENTS_IOS, cListResponse);
				resultList.add(resultObject);
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_IOS_RESPONSE);
		}
		return resultList;
	}

	private String getStatus(boolean value) {
		String statusValue = null;
		try {
			if (value) {
				statusValue = ServiceConstants.CLOSED;
			} else {
				statusValue = ServiceConstants.OPEN;
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_STATUS);
		}
		return statusValue;
	}

	private List<JSONObject> getCommentListResponse(List<JSONObject> cList, List<JSONObject> cmntList) {
		try {
			if (null != cList) {
				for (int n = 0; n < cList.size(); n++) {
					JSONObject cObject = new JSONObject();
					cObject.put(ServiceConstants.ID_COMMENT_IOS, null != cList.get(n).get(ServiceConstants.ID_COMMENT) ? cList.get(n).get(ServiceConstants.ID_COMMENT).toString()
							: null);
					cObject.put(ServiceConstants.ID_USER_IOS, null != cList.get(n).get(ServiceConstants.ID_USER) ? cList.get(n).get(ServiceConstants.ID_USER).toString() : null);
					cObject.put(ServiceConstants.NAME_USER_IOS, cList.get(n).get(ServiceConstants.NAME_USER));
					cObject.put(ServiceConstants.TEXT_IOS, cList.get(n).get(ServiceConstants.TEXT));
					cObject.put(ServiceConstants.CREATION_DATE_IOS, cList.get(n).get(ServiceConstants.CREATION_DATE));
					cmntList.add(cObject);
				}
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_COMMENT_LIST_RESPONSE);
		}
		return cmntList;
	}

	private List<JSONObject> getUserListResponse(List<JSONObject> uList, List<JSONObject> usrList) {
		try {
			if (null != uList) {
				for (int m = 0; m < uList.size(); m++) {
					JSONObject uObject = new JSONObject();
					uObject.put(ServiceConstants.ID, null != uList.get(m).get(ServiceConstants.NEW_ID) ? uList.get(m).get(ServiceConstants.NEW_ID).toString() : null);
					uObject.put(ServiceConstants.ID_USER_IOS, null != uList.get(m).get(ServiceConstants.ID_USER) ? uList.get(m).get(ServiceConstants.ID_USER).toString() : null);
					uObject.put(ServiceConstants.NAME_USER_IOS, uList.get(m).get(ServiceConstants.NAME_USER));
					usrList.add(uObject);
				}
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_USER_LIST_RESPONSE);
		}
		return usrList;
	}

	private List<JSONObject> getSORRequestList(JSONArray dataArray, List<JSONObject> sorRequestList, String value) {
		try {
			for (int i = 0; i < dataArray.size(); i++) {
				List<JSONObject> userList = new ArrayList<>();
				boolean markedValue = false;
				List<JSONObject> commentList = new ArrayList<>();
				JSONObject sorRequest = new JSONObject();
				JSONObject jsonRequest = (JSONObject) dataArray.get(i);
				if (ServiceConstants.POST.equalsIgnoreCase(value)) {
					sorRequest.put(ServiceConstants.ID_NOTE, null);
					sorRequest.put(ServiceConstants.ID_APP_GENERATED, jsonRequest.get(ServiceConstants.NOTES_ID));
				} else {
					sorRequest.put(ServiceConstants.ID_NOTE, jsonRequest.get(ServiceConstants.NOTES_ID));
				}
				sorRequest.put(ServiceConstants.ID_USER, jsonRequest.get(ServiceConstants.OWNER_ID));
				sorRequest.put(ServiceConstants.NAME_USER, jsonRequest.get(ServiceConstants.OWNER_NAME));
				sorRequest.put(ServiceConstants.CREATION_DATE, jsonRequest.get(ServiceConstants.CREATION_DATE_IOS));
				sorRequest.put(ServiceConstants.PLANNED_DATE, jsonRequest.get(ServiceConstants.PLANNED_DATE_IOS));
				sorRequest.put(ServiceConstants.ID_LOCATION, jsonRequest.get(ServiceConstants.LOCATION_ID));
				sorRequest.put(ServiceConstants.NAME_LOCATION, jsonRequest.get(ServiceConstants.LOCATION_NAME));

				String boolVal = (String) jsonRequest.get(ServiceConstants.STATUS);
				if (boolVal.equalsIgnoreCase(ServiceConstants.CLOSED)) {
					markedValue = true;
				}
				sorRequest.put(ServiceConstants.MARKED, markedValue);
				sorRequest.put(ServiceConstants.TITLE, jsonRequest.get(ServiceConstants.NOTES_TITLE));
				sorRequest.put(ServiceConstants.PRIORITY, jsonRequest.get(ServiceConstants.PRIORITY_IOS));
				sorRequest.put(ServiceConstants.TEXT, jsonRequest.get(ServiceConstants.NOTES_DESCRIPTION));
				List<JSONObject> iOSUserList = (List<JSONObject>) jsonRequest.get(ServiceConstants.USER_LIST);
				List<JSONObject> listUsers = getIOSUserList(iOSUserList, userList);
				List<JSONObject> iOSCommentsList = (List<JSONObject>) jsonRequest.get(ServiceConstants.LIST_COMMENTS_IOS);
				List<JSONObject> listComments = getIOSCommentsList(iOSCommentsList, commentList);
				sorRequest.put(ServiceConstants.LIST_USERS, listUsers);
				sorRequest.put(ServiceConstants.LIST_COMMENTS, listComments);
				sorRequestList.add(sorRequest);
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_SOR_REQUEST_LIST);
		}
		return sorRequestList;
	}

	private List<JSONObject> getIOSCommentsList(List<JSONObject> iOSCommentsList, List<JSONObject> commentList) {
		JSONObject commentObject;
		try {
			if (null != iOSCommentsList) {
				for (int k = 0; k < iOSCommentsList.size(); k++) {
					commentObject = new JSONObject();
					commentObject.put(ServiceConstants.ID_COMMENT, iOSCommentsList.get(k).get("commentId"));
					commentObject.put(ServiceConstants.ID_USER, iOSCommentsList.get(k).get("userId"));
					commentObject.put(ServiceConstants.NAME_USER, iOSCommentsList.get(k).get("userName"));
					commentObject.put(ServiceConstants.TEXT, iOSCommentsList.get(k).get("text"));
					commentObject.put(ServiceConstants.CREATION_DATE, iOSCommentsList.get(k).get("creationDate"));
					commentList.add(commentObject);
				}
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_IOS_COMMENT_LIST);
		}
		return commentList;
	}

	private List<JSONObject> getIOSUserList(List<JSONObject> iOSUserList, List<JSONObject> userList) {
		JSONObject userObject;
		try {
			if (null != iOSUserList) {
				for (int j = 0; j < iOSUserList.size(); j++) {
					userObject = new JSONObject();
					userObject.put(ServiceConstants.NEW_ID, iOSUserList.get(j).get(ServiceConstants.ID));
					userObject.put(ServiceConstants.ID_USER, iOSUserList.get(j).get(ServiceConstants.USER_ID));
					userObject.put(ServiceConstants.NAME_USER, iOSUserList.get(j).get(ServiceConstants.USER_NAME));
					userList.add(userObject);
				}
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_IOS_USER_LIST);
		}
		return userList;
	}

	@Override
	public Response notesUpdate(String contentType, String acceptLanguage, String userAgent, String apiVersion, String loggedInUser, String notesData, String token) {
		Response response = null;
		List<JSONObject> sorRequestList = new ArrayList<>();
		JSONArray dataArray = new JSONArray();
		JSONObject dataObject = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		try {
			boolean idValidation = ValidateUtility.validateIds(notesData);
			if (idValidation) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY, ServiceConstants.LEVEL_ERROR);
				return response;
			}
			dataObject = (JSONObject) JSONValue.parse(notesData);
			dataArray = (JSONArray) dataObject.get(ServiceConstants.DATA);
			logger.severe("Request for updateNotes from iOS :" + dataArray);
			List<JSONObject> sorReq = getSORRequestList(dataArray, sorRequestList, ServiceConstants.PUT);
			logger.severe("Request for update notes to SOR :" + sorReq);
			String responseValue = URLUtility.getSORDataWithHeaderParamsForUpdateNCR(URLUtility.getRequiredUrl(ServiceConstants.UPDATE_NOTES_URL), sorReq.toString(), token);
			response = sendIOSResponseForUpdateNotes(apiVersion, responseValue);
		} catch (Exception e) {
			printException(e, ServiceConstants.UPDATE_NOTES);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	private Response sendIOSResponseForUpdateNotes(String apiVersion, String responseValue) {
		Response response = null;
		JSONObject resObject = new JSONObject();
		List<JSONObject> error = new ArrayList<>();
		JSONObject errorResponse = new JSONObject();
		try {
			if (responseValue.isEmpty()) {
				errorResponse.put("message", "No data from SOR response after update notes call.");
				errorResponse.put("id", "400");
				errorResponse.put("level", "ERROR");
				error.add(errorResponse);
				resObject.put(ServiceConstants.ERRORS, error);
				resObject.put(ServiceConstants.DATA, new JSONArray());
				response = APIResponseBuilder.sendSuccessResponse(apiVersion, 400, ServiceConstants.SUCCESS_MESSAGE, resObject);
				return response;
			}
			resObject = getResponseObject(responseValue, resObject, ServiceConstants.NOTES);
			JSONObject resObjectValue = (JSONObject) JSONValue.parse(responseValue);
			String code = (String) resObjectValue.get(ServiceConstants.ID);
			int responseCode = Integer.parseInt(code);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode, ServiceConstants.SUCCESS_MESSAGE, resObject);
		} catch (Exception e) {
			printException(e, ServiceConstants.SEND_IOS_RESPONSE_FOR_UPDATE_NOTES);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	private JSONObject getResponseObject(String result, JSONObject resObject, String item) {
		JSONObject errorResponse = new JSONObject();
		List<JSONObject> error = new ArrayList<>();
		JSONObject errorObject = new JSONObject();
		JSONObject successObject = new JSONObject();
		List<JSONObject> successObjectList = new ArrayList<>();
		try {
			resObject = (JSONObject) JSONValue.parse(result);
			String code = (String) resObject.get("id");
			if ("200".equalsIgnoreCase(code)) {
				successObjectList.add(resObject);
				successObject.put(ServiceConstants.DATA, successObjectList);
				successObject.put(ServiceConstants.ERRORS, new JSONArray());
				return successObject;
			} else if ("403".equalsIgnoreCase(code)) {
				errorResponse.put("message", "Authorization is not valid.Please login again.");
				errorResponse.put("id", "403");
				errorResponse.put("level", "ERROR");
				error.add(errorResponse);
				errorObject.put(ServiceConstants.ERRORS, error);
				errorObject.put(ServiceConstants.DATA, new JSONArray());
			} else {
				errorResponse.put("message", "Update " + item + " failed.");
				errorResponse.put("id", "400");
				errorResponse.put("level", "ERROR");
				error.add(errorResponse);
				errorObject.put(ServiceConstants.ERRORS, error);
				errorObject.put(ServiceConstants.DATA, new JSONArray());
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_RESPONSE_OBJECT_IN_UPDATE_NOTES);
		}
		return errorObject;
	}

	@Override
	public Response deleteNotes(String contentType, String acceptLanguage, String userAgent, String apiVersion, String loggedInUser, String idNote, String token) {
		Response response = null;
		DeleteNotesIOS deleteNotesResponse = new DeleteNotesIOS();
		List<Error> errorList = new ArrayList<>();
		DeleteNotesData deleteNotesData = new DeleteNotesData();
		List<DeleteNotesData> deleteNotesDataList = new ArrayList<>();
		try {
			if (StringUtils.isBlank(idNote)) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.INVALID_NOTE_ID));
			}
			String deleteNotesUrl = generateDeleteNotesURL(idNote);
			String sorOutputStringForDeleteNotes = deleteNotesFromSOR(URLUtility.getRequiredUrl(ServiceConstants.DELETE_NOTES_URL) + deleteNotesUrl, token);
			if (StringUtils.isBlank(sorOutputStringForDeleteNotes)) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.DELETE_NOTES_NO_RESPONSE));
			}
			if (sorOutputStringForDeleteNotes.contains(ServiceConstants.EXCEPTION_TYPE)) {
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputStringForDeleteNotes, apiVersion);
				return response;
			} else if (sorOutputStringForDeleteNotes.contains("ClassName")) {
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringForDeleteNotes, apiVersion);
				return response;
			}
			if (ServiceConstants.DELETE_NOTES_SOR_FALSE.equalsIgnoreCase(sorOutputStringForDeleteNotes)) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.DELETE_NOTES_SOR_FAIL));

			} else if (ServiceConstants.DELETE_NOTES_SOR_TRUE.equalsIgnoreCase(sorOutputStringForDeleteNotes)) {
				deleteNotesResponse.setErrors(errorList);
				deleteNotesData.setId(ServiceConstants.SUCCESS_ID);
				deleteNotesData.setLevel(ServiceConstants.SUCCESS_MESSAGE);
				deleteNotesData.setMessage(ServiceConstants.DELETE_NOTES_SUCCESS);
				deleteNotesDataList.add(deleteNotesData);
				deleteNotesResponse.setData(deleteNotesDataList);
				response = APIResponseBuilder.sendSuccessResponse(apiVersion, 200, ServiceConstants.SUCCESS_MESSAGE, deleteNotesResponse);
				return response;
			}
		} catch (TecnicasException te) {
			throw te;
		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.DELETE_NOTES);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.DELETE_NOTES);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.USER_ID_CHECK_MESSAGE, ServiceConstants.DELETE_NOTES_ERROR, ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	public String generateDeleteNotesURL(String idNote) {
		StringBuilder builder = new StringBuilder();
		if (idNote.contains(",")) {
			List<String> idNoteList = Arrays.asList(idNote.trim().split(","));
			for (int i = 0; i < idNoteList.size(); i++) {
				builder.append("idNote[").append(i).append("]=").append(idNoteList.get(i));
				if (i != idNoteList.size() - 1) {
					builder.append("&");
				}
			}
			return builder.toString();
		} else
			return builder.append("idNote[0]=").append(idNote).toString();
	}

	public String deleteNotesFromSOR(String url, String token) {
		String responseBody = "";
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpDelete deleteRequest = new HttpDelete(url);
			deleteRequest.setHeader("Authorization", token);
			deleteRequest.setHeader("language", "US");
			deleteRequest.setHeader("Content-Type", "application/json");
			deleteRequest.setHeader("Cache-Control","no-cache,no-store");
			deleteRequest.setHeader("Pragma","no-cache");
			logger.severe("connectionURL ::" + url);
			HttpResponse response = httpclient.execute(deleteRequest);
			if (response != null && response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
				HttpEntity entity = response.getEntity();
				responseBody = entity != null ? EntityUtils.toString(entity) : null;
				return responseBody;
			}
		} catch (ClientProtocolException e) {
			printException(e, ServiceConstants.DELETE_NOTES);
		} catch (UnknownHostException uhe) {
			printExceptions(uhe, "Unknow host exception");
			responseBody = adapterExceptionHandler.errorJSON();
		} catch (IOException e) {
			printException(e, ServiceConstants.DELETE_NOTES);
			responseBody = adapterExceptionHandler.errorJSON();
		}
		return responseBody;
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

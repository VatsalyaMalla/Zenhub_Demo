package com.ibm.mobilefirst.industrial.materialsinspect.adapter.location.demo.data;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
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
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.location.LocationAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.DocumentOrderByOrderIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.DocumentVendorByLocationIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.IOSLocationByLocationIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.LocationByLocationIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.LocationItemsByOrderIdAndLocationIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.MediaDescriptionList;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.ReportDescriptionByLocationIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.SerializedAttachmentFormSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.SingleIncidentsData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.SingleNCRData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.SinglePunchListData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.UpdateIncidents;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.UpdateNCR;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.UpdatePunchListItems;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.UserRequestByIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.exception.AdapterExceptionHandler;
import com.ibm.mobilefirst.industrial.materialsinspect.api.location.response.APIResponseBuilder;

public class LocationDataAdapter implements LocationAdapter {

	static Logger logger = Logger.getLogger(LocationDataAdapter.class.getName());
	AdapterExceptionHandler adapterExceptionHandler = new AdapterExceptionHandler();

	private Object getOrderId(LocationByLocationIdSORData obj) {
		Object orderId = "";
		try {
			if (null == obj) {
				logger.info("Object is null for the given location id.");
			} else {
				orderId = obj.getId_order();
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.ORDER_ID);
		}
		return orderId;
	}

	@Override
	public Response getItemsByLocationId(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String locationId, Integer offset,
			Integer limit, String token) {
		Response response = null;
		List<JSONObject> itemResp = new ArrayList<>();
		JSONObject result = new JSONObject();
		try {
			if (StringUtils.isBlank(locationId)) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY, ServiceConstants.LEVEL_ERROR);
				return response;
			}
			String[] locationIds = locationId.split(ServiceConstants.COMMA);
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			for (int i = 0; i < locationIds.length; i++) {
				String sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.LOCATION_BY_ID) + locationIds[i], token);
				if (sorOutputString.contains(ServiceConstants.EXCEPTION)) {
					response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
					return response;
				} else if (sorOutputString.contains("ClassName")) {
					response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
					return response;
				}
				LocationByLocationIdSORData obj = mapper.readValue(sorOutputString, LocationByLocationIdSORData.class);
				Object orderId = getOrderId(obj);
				sorOutputString = AdapterExceptionHandler.getSORData(
						URLUtility.getRequiredUrl(ServiceConstants.LOCATION_BY_ORDER_ID) + orderId + URLUtility.getRequiredUrl(ServiceConstants.LOCATION) + locationIds[i], token);
				if (sorOutputString.contains(ServiceConstants.EXCEPTION)) {
					response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
					return response;
				} else if (sorOutputString.contains("ClassName")) {
					response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
					return response;
				}
				LocationItemsByOrderIdAndLocationIdSORData[] locationItemsobj = mapper.readValue(sorOutputString, LocationItemsByOrderIdAndLocationIdSORData[].class);
				itemResp = getItemResponse(itemResp, locationItemsobj);
			}
			offset = setOffset(offset, itemResp);
			limit = setLimit(limit, itemResp);
			try {
				itemResp = itemResp.subList(offset, offset + limit);
			} catch (Exception e) {
				printException(e, ServiceConstants.GET_LOCATION_ITEMS);
				limit = itemResp.size();
				itemResp = itemResp.subList(offset, limit);
			}
			JSONObject pagination = new JSONObject();
			pagination.put(ServiceConstants.SIZE, limit);
			pagination.put(ServiceConstants.OFFSET, offset);
			result.put(ServiceConstants.PAGINATION, pagination);
			result.put(ServiceConstants.DATA, itemResp);
			result.put(ServiceConstants.ERRORS, new JSONArray());
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK, ServiceConstants.SUCCESS_MESSAGE, result);
		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.GET_LOCATION_ITEMS);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_LOCATION_ITEMS);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	private Integer setLimit(Integer limit, List<JSONObject> itemResp) {
		try {
			if (limit == -1 || limit == 0)
				limit = itemResp.size();
		} catch (Exception e) {
			printException(e, ServiceConstants.SET_LIMIT);
		}
		return limit;
	}

	private Integer setOffset(Integer offset, List<JSONObject> itemResp) {
		try {
			if (offset == -1 || offset >= itemResp.size())
				offset = 0;
		} catch (Exception e) {
			printException(e, ServiceConstants.SET_OFFSET);
		}
		return offset;
	}

	private List<JSONObject> getItemResponse(List<JSONObject> itemResp, LocationItemsByOrderIdAndLocationIdSORData[] locationItemsobj) {
		try {
			if (null == locationItemsobj) {
				logger.info("Location items object is null or empty for the given location id and order id.");
			} else {
				for (int j = 0; j < locationItemsobj.length; j++) {
					JSONObject locationItemObj = new JSONObject();
					Object intItemId = locationItemsobj[j].getId_item();
					locationItemObj.put(ServiceConstants.ITEMID, intItemId.toString());
					locationItemObj.put(ServiceConstants.DISPLAY_NAME, null != locationItemsobj[j].getIosTag() ? locationItemsobj[j].getIosTag() : null);
					locationItemObj.put(ServiceConstants.ITEMDESCRITION, null != locationItemsobj[j].getTag_desc() ? locationItemsobj[j].getTag_desc() : null);
					locationItemObj.put(ServiceConstants.TYPE, ServiceConstants.ITEM);// ###
																						// pending
					locationItemObj.put(ServiceConstants.PARENTITEMID, null);
					itemResp.add(locationItemObj);
				}
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.SET_ITEM_RESPONSE);
		}
		return itemResp;
	}

	@Override
	public Response updateNcr(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String ncrRequest, String token) {

		Response response = null;
		JSONObject resObject = new JSONObject();
		String updateResult;
		List<JSONObject> error = new ArrayList<>();
		JSONObject errorResponse = new JSONObject();
		try {
			if (ncrRequest.isEmpty()) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY, ServiceConstants.LEVEL_ERROR);
				return response;
			}
			updateResult = UpdateNCR.processUpdateNCR(ncrRequest, token);

			if (updateResult.isEmpty()) {
				errorResponse.put("message", "No data from SOR response after update NCR call.");
				errorResponse.put("id", "400");
				errorResponse.put("level", "ERROR");
				error.add(errorResponse);
				resObject.put("errors", error);
				resObject.put("data", new JSONArray());
				response = APIResponseBuilder.sendSuccessResponse(apiVersion, 400, ServiceConstants.SUCCESS_MESSAGE, resObject);
				return response;
			}
			resObject = getResponseObject(updateResult, resObject, "NCR");
			JSONObject resObjectValue = (JSONObject) JSONValue.parse(updateResult);
			String code = (String) resObjectValue.get("id");
			int responseCode = Integer.parseInt(code);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode, ServiceConstants.SUCCESS_MESSAGE, resObject);
		} catch (Exception e) {
			printException(e, "updateNcr");
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	private String getFormattedDate(String closingDate) {
		String date = null;
		try {
			if (null != closingDate && !closingDate.isEmpty()) {
				date = closingDate.substring(0, 10);
			}
		} catch (Exception e) {
			printException(e, "getFormattedDate");
		}
		return date;
	}

	@Override
	public Response updatePunchListItem(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String punchListItem, String token) {
		Response response = null;
		JSONObject errorResponse = new JSONObject();
		List<JSONObject> error = new ArrayList<>();
		JSONObject resObject = new JSONObject();
		String result = "";
		try {
			if (punchListItem.isEmpty()) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY, ServiceConstants.LEVEL_ERROR);

				return response;
			}
			result = UpdatePunchListItems.processUpdatePunchListItems(punchListItem, token);
			if (result.isEmpty()) {
				errorResponse.put("message", "No data from SOR response after update PunchList call.");
				errorResponse.put("id", "400");
				errorResponse.put("level", "ERROR");
				error.add(errorResponse);
				resObject.put("errors", error);
				resObject.put("data", new JSONArray());
				response = APIResponseBuilder.sendSuccessResponse(apiVersion, 400, ServiceConstants.SUCCESS_MESSAGE, resObject);
				return response;
			}
			resObject = getResponseObject(result, resObject, "PunchListItems");
			JSONObject resObjectValue = (JSONObject) JSONValue.parse(result);
			String code = (String) resObjectValue.get("id");
			int responseCode = Integer.parseInt(code);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode, ServiceConstants.SUCCESS_MESSAGE, resObject);
		} catch (Exception e) {
			printException(e, "updatePunchListItem");
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
				successObject.put("data", successObjectList);
				successObject.put("errors", new JSONArray());
				return successObject;
			} else if ("403".equalsIgnoreCase(code)) {
				errorResponse.put("message", "Authorization is not valid.Please login again.");
				errorResponse.put("id", "403");
				errorResponse.put("level", "ERROR");
				error.add(errorResponse);
				errorObject.put("errors", error);
				errorObject.put("data", new JSONArray());
			} else {
				errorResponse.put("message", "Update " + item + " failed.");
				errorResponse.put("id", "400");
				errorResponse.put("level", "ERROR");
				error.add(errorResponse);
				errorObject.put("errors", error);
				errorObject.put("data", new JSONArray());
			}
		} catch (Exception e) {
			printException(e, "getResponseObject");
		}
		return errorObject;
	}

	@Override
	public Response updateIncident(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String incidentRequest, String token) {
		Response response = null;
		JSONObject errorResponse = new JSONObject();
		List<JSONObject> error = new ArrayList<>();
		String result = "";
		JSONObject resObject = new JSONObject();

		try {
			if (incidentRequest.isEmpty()) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY, ServiceConstants.LEVEL_ERROR);

				return response;
			}
			logger.severe("Before UpdateIncidents.processUpdateIncidents.");
			result = UpdateIncidents.processUpdateIncidents(incidentRequest, token);
			logger.severe("After UpdateIncidents.processUpdateIncidents.");
			if (result.isEmpty()) {
				errorResponse.put("message", "No data from SOR response after update Incidents call.");
				errorResponse.put("id", "400");
				errorResponse.put("level", "ERROR");
				error.add(errorResponse);
				resObject.put("errors", error);
				resObject.put("data", new JSONArray());
				response = APIResponseBuilder.sendSuccessResponse(apiVersion, 400, ServiceConstants.SUCCESS_MESSAGE, resObject);
				return response;
			}
			resObject = getResponseObject(result, resObject, "Incidents");
			JSONObject resObjectValue = (JSONObject) JSONValue.parse(result);
			String code = (String) resObjectValue.get("id");
			int responseCode = Integer.parseInt(code);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode, ServiceConstants.SUCCESS_MESSAGE, resObject);
		} catch (Exception e) {
			printException(e, "updateIncident");
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	@Override
	public Response getMediaDescriptionByLocationId(String contentType, String acceptLanguage, String language, String apiVersion, String loggedInUser, String userAgent,
			String locationId, String userId, Integer offset, Integer limit, String token) {
		Response response = null;
		List<JSONObject> mediaItemResp = new ArrayList<>();
		JSONObject result = new JSONObject();
		JSONObject attachmentObj;
		String docExtension = null;
		boolean version = false;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			String[] locations = locationId.split(",");
			if (locations == null || locations.length == 0) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY, ServiceConstants.LEVEL_ERROR);
				return response;
			}
			String sorOutputStringForRequest = AdapterExceptionHandler.getSORData(
					URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL) + userId + URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_N), token);
			if (sorOutputStringForRequest.contains("ExceptionType")) {
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputStringForRequest, apiVersion);
				return response;
			} else if (sorOutputStringForRequest.contains("ClassName")) {
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringForRequest, apiVersion);
				return response;
			}
			for (int i = 0; i < locations.length; i++) {
				String locationValue = locations[i];
				String sorOutputStringForLocation = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.LOCATION_BY_ID) + locations[i], token);
				if (sorOutputStringForLocation.contains(ServiceConstants.EXCEPTION)) {
					response = adapterExceptionHandler.getSORDataExcpetion(sorOutputStringForLocation, apiVersion);
					return response;
				} else if (sorOutputStringForLocation.contains("ClassName")) {
					response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringForLocation, apiVersion);
					return response;
				}
				LocationByLocationIdSORData obj = mapper.readValue(sorOutputStringForLocation, LocationByLocationIdSORData.class);
				String sorOutputStringForVendor = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.DOCUMENT_VENDOR) + locations[i], token);
				if (sorOutputStringForVendor.contains("ClassName")) {
					response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringForVendor, apiVersion);
					return response;
				}

				Object orderId = getOrderId(obj);
				DocumentVendorByLocationIdSORData[] docVendorobj = mapper.readValue(sorOutputStringForVendor, DocumentVendorByLocationIdSORData[].class);
				setMediaItemByVendor(docVendorobj, mediaItemResp);

				String sorOutputStringForOrder = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.DOCUMENT_ORDER) + orderId.toString(), token);
				if (sorOutputStringForOrder.contains("ClassName")) {
					response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringForOrder, apiVersion);
					return response;
				}
				DocumentOrderByOrderIdSORData[] docOrderobj = mapper.readValue(sorOutputStringForOrder, DocumentOrderByOrderIdSORData[].class);
				setMediaItemByOrder(docOrderobj, mediaItemResp);
				UserRequestByIdSORData[] userReqByIdobj = mapper.readValue(sorOutputStringForRequest, UserRequestByIdSORData[].class);
				if (null != userReqByIdobj && userReqByIdobj.length != 0) {
					for (int j = 0; j < userReqByIdobj.length; j++) {
						Object idLocation = userReqByIdobj[j].getID_LOCATION();
						if (idLocation.toString().equalsIgnoreCase(locationValue)) {
							Object formId = userReqByIdobj[j].getID_FORM();
							String sorOutputStringForAttachments = AdapterExceptionHandler
									.getSORData(URLUtility.getRequiredUrl(ServiceConstants.SERIALIZED_OBJECT) + formId, token);
							if (sorOutputStringForAttachments.contains("ClassName")) {
								response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringForAttachments, apiVersion);
								return response;
							}
							SerializedAttachmentFormSORData[] serAttFormobj = mapper.readValue(sorOutputStringForAttachments, SerializedAttachmentFormSORData[].class);
							if (null != serAttFormobj) {
								for (int l = 0; l < serAttFormobj.length; l++) {
									attachmentObj = new JSONObject();
									String path = serAttFormobj[l].getFILEPATH();
									if (null != path && !path.isEmpty()) {
										int pathValue = path.length();
										int dotValue = path.lastIndexOf(ServiceConstants.DOT);
										docExtension = path.substring(dotValue + 1, pathValue);
									}
									attachmentObj.put(ServiceConstants.MEDIAID, serAttFormobj[l].getID_ATTACHMENT().toString());
									attachmentObj.put(ServiceConstants.FILE_NAME, serAttFormobj[l].getNAME());
									attachmentObj.put(ServiceConstants.LAST_DOWNLOAD_DATE, null);
									attachmentObj.put(ServiceConstants.MEDIA_DESCRIPTION, null != serAttFormobj[l].getDESC() ? serAttFormobj[l].getDESC() : null);
									attachmentObj.put(ServiceConstants.CODE, null != serAttFormobj[l].getCode()? serAttFormobj[l].getCode().toString() : null);
									attachmentObj.put(ServiceConstants.DOCUMENT_STATUS, null);
									attachmentObj.put(ServiceConstants.DISPLAYNAME, serAttFormobj[l].getNAME());
									attachmentObj.put(ServiceConstants.SUPPORT_DOC_TYPE, ServiceConstants.ATTACHMENTS);
									attachmentObj.put(ServiceConstants.STATUS, null);
									attachmentObj.put(ServiceConstants.SYNC_STATUS, null);
									attachmentObj.put(ServiceConstants.THUMB_NAIL, null);
									String type = getTypeForAttachements(docExtension);
									attachmentObj.put(ServiceConstants.TYPE, type);
									attachmentObj.put(ServiceConstants.FILE_EXTENSION, docExtension);
									attachmentObj.put(ServiceConstants.VERSION, null);
									attachmentObj.put(ServiceConstants.TITLE, serAttFormobj[l].getNAME());
									version = getNewVersionAvailable(serAttFormobj[l].getLastVersion());
									attachmentObj.put(ServiceConstants.NEW_VERSION_AVAILABLE, version);
									attachmentObj.put(ServiceConstants.PATH, null);
									attachmentObj.put(ServiceConstants.REF_VENDOR, null);
									attachmentObj.put(ServiceConstants.DOCUMENT_TAG, null);
									mediaItemResp.add(attachmentObj);
								}
							}
						}
					}
				}
			}

			offset = setOffset(offset, mediaItemResp);
			limit = setLimit(limit, mediaItemResp);
			try {
				mediaItemResp = mediaItemResp.subList(offset, offset + limit);
			} catch (Exception e) {
				printException(e, ServiceConstants.MEDIA_DESCRIPTION_BY_ID);
				limit = mediaItemResp.size();
				mediaItemResp = mediaItemResp.subList(offset, limit);
			}
			JSONObject pagination = new JSONObject();
			pagination.put(ServiceConstants.SIZE, limit);
			pagination.put(ServiceConstants.OFFSET, offset);

			result.put(ServiceConstants.PAGINATION, pagination);
			result.put(ServiceConstants.DATA, mediaItemResp);
			result.put(ServiceConstants.ERRORS, new JSONArray());

			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK, ServiceConstants.SUCCESS_MESSAGE, result);

		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.MEDIA_DESCRIPTION_BY_ID);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.MEDIA_DESCRIPTION_BY_ID);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}

		return response;
	}

	private boolean getNewVersionAvailable(Object lastVersion) {
		boolean version = false;
		try {
			if (null != lastVersion && !lastVersion.toString().isEmpty()) {
				if ("true".equalsIgnoreCase(lastVersion.toString())) {
					version = true;
				}
			}
		} catch (Exception e) {
			printException(e, "getNewVersionAvailable");
		}
		return version;
	}

	private String getTypeForAttachements(String docExtension) {
		String type = null;
		try {
			if (null != docExtension && !docExtension.isEmpty()) {
				if ("bmp".equalsIgnoreCase(docExtension) || "gif".equalsIgnoreCase(docExtension) || "jpg".equalsIgnoreCase(docExtension) || "png".equalsIgnoreCase(docExtension)
						|| "jpeg".equalsIgnoreCase(docExtension)) {
					type = "photo";
				} else {
					type = "document";
				}
			}
		} catch (Exception e) {
			printException(e, "getTypeForAttachements");
		}
		return type;
	}

	private void setMediaItemByOrder(DocumentOrderByOrderIdSORData[] docOrderobj, List<JSONObject> mediaItemResp) {
		JSONObject docOrderJSON;
		String docExtension = null;
		boolean version = false;
		try {
			if (null != docOrderobj) {
				for (int j = 0; j < docOrderobj.length; j++) {
					docOrderJSON = new JSONObject();
					String path = docOrderobj[j].getFilepath();
					if (null != path && !path.isEmpty()) {
						int pathValue = path.length();
						int dotValue = path.lastIndexOf(ServiceConstants.DOT);
						docExtension = path.substring(dotValue + 1, pathValue);
					}
					docOrderJSON.put(ServiceConstants.DISPLAYNAME, docOrderobj[j].getName());
					docOrderJSON.put(ServiceConstants.FILE_NAME, docOrderobj[j].getName());
					docOrderJSON.put(ServiceConstants.TITLE, null != docOrderobj[j].getTitle() ? docOrderobj[j].getTitle() : null);
					docOrderJSON.put(ServiceConstants.CODE, null != docOrderobj[j].getCode() ? docOrderobj[j].getCode().toString() : null);
					docOrderJSON.put(ServiceConstants.STATUS, docOrderobj[j].getTr_status());
					docOrderJSON.put(ServiceConstants.LAST_DOWNLOAD_DATE, docOrderobj[j].getDate_mod());
					docOrderJSON.put(ServiceConstants.MEDIA_DESCRIPTION, docOrderobj[j].getName());
					docOrderJSON.put(ServiceConstants.MEDIAID, docOrderobj[j].getId_document().toString());
					version = getNewVersionAvailable(docOrderobj[j].getLastVersion());
					docOrderJSON.put(ServiceConstants.NEW_VERSION_AVAILABLE, version);
					docOrderJSON.put(ServiceConstants.PATH, null);
					docOrderJSON.put(ServiceConstants.SYNC_STATUS, null);
					docOrderJSON.put(ServiceConstants.THUMB_NAIL, null);
					docOrderJSON.put(ServiceConstants.TYPE, ServiceConstants.DOCUMENT);
					docOrderJSON.put(ServiceConstants.DOCUMENT_STATUS, null != docOrderobj[j].getTr_status() ? docOrderobj[j].getTr_status() : null);
					docOrderJSON.put(ServiceConstants.SUPPORT_DOC_TYPE, ServiceConstants.PURCHASE_ORDER);
					docOrderJSON.put(ServiceConstants.FILE_EXTENSION, docExtension);
					docOrderJSON.put(ServiceConstants.REF_VENDOR, docOrderobj[j].getREF_VENDOR());
					docOrderJSON.put(ServiceConstants.DOCUMENT_TAG, docOrderobj[j].getTAG());
					docOrderJSON.put(ServiceConstants.VERSION, docOrderobj[j].getRev_tr());
					mediaItemResp.add(docOrderJSON);
				}
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.MEDIA_ITEM_ORDER);
		}
	}

	private void setMediaItemByVendor(DocumentVendorByLocationIdSORData[] docVendorobj, List<JSONObject> mediaItemResp) {
		JSONObject docVendorJSON;
		String docExtension = null;
		boolean version = false;
		try {
			if (null != docVendorobj) {
				for (int k = 0; k < docVendorobj.length; k++) {
					String path = docVendorobj[k].getFilepath();
					if (null != path && !path.isEmpty()) {
						int pathValue = path.length();
						int dotValue = path.lastIndexOf(ServiceConstants.DOT);
						docExtension = path.substring(dotValue + 1, pathValue);
					}
					docVendorJSON = new JSONObject();
					docVendorJSON.put(ServiceConstants.DISPLAYNAME, docVendorobj[k].getName());
					docVendorJSON.put(ServiceConstants.CODE, null != docVendorobj[k].getCode() ? docVendorobj[k].getCode().toString() : null);
					docVendorJSON.put(ServiceConstants.FILE_NAME, docVendorobj[k].getName());
					docVendorJSON.put(ServiceConstants.TITLE, null != docVendorobj[k].getTitle() ? docVendorobj[k].getTitle() : null);
					docVendorJSON.put(ServiceConstants.DOCUMENT_STATUS, null != docVendorobj[k].getTr_status() ? docVendorobj[k].getTr_status() : null);
					docVendorJSON.put(ServiceConstants.STATUS, docVendorobj[k].getTr_status());
					docVendorJSON.put(ServiceConstants.LAST_DOWNLOAD_DATE, docVendorobj[k].getDate_mod());
					docVendorJSON.put(ServiceConstants.MEDIA_DESCRIPTION, docVendorobj[k].getName());
					docVendorJSON.put(ServiceConstants.MEDIAID, docVendorobj[k].getId_document().toString());
					version = getNewVersionAvailable(docVendorobj[k].getLastVersion());
					docVendorJSON.put(ServiceConstants.NEW_VERSION_AVAILABLE, version);
					docVendorJSON.put(ServiceConstants.PATH, null);
					docVendorJSON.put(ServiceConstants.SYNC_STATUS, null);
					docVendorJSON.put(ServiceConstants.TYPE, ServiceConstants.DOCUMENT);
					docVendorJSON.put(ServiceConstants.THUMB_NAIL, null);
					docVendorJSON.put(ServiceConstants.SUPPORT_DOC_TYPE, ServiceConstants.VENDOR);
					docVendorJSON.put(ServiceConstants.FILE_EXTENSION, docExtension);
					docVendorJSON.put(ServiceConstants.VERSION, docVendorobj[k].getRev_tr());
					docVendorJSON.put(ServiceConstants.REF_VENDOR, docVendorobj[k].getREF_VENDOR());
					docVendorJSON.put(ServiceConstants.DOCUMENT_TAG, docVendorobj[k].getTAG());
					mediaItemResp.add(docVendorJSON);
				}
			}
		} catch (Exception e) {
			printException(e, ServiceConstants.MEDIA_ITEM_VENDOR);
		}
	}

	@Override
	public Response getReportsDescriptionByLocationId(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String locationId,
			String userId, Integer offset, Integer limit, String token) {
		Response response = null;
		List<JSONObject> reportsResp = new ArrayList<>();
		JSONObject result = new JSONObject();
		try {
			String[] locations = locationId.split(ServiceConstants.COMMA);
			if (ArrayUtils.isEmpty(locations)) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY, ServiceConstants.LEVEL_ERROR);
				return response;
			}
			for (int i = 0; i < locations.length; i++) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
				String sorOutputString = AdapterExceptionHandler.getSORData(
						URLUtility.getRequiredUrl(ServiceConstants.HISTORY_INSPECTOR) + userId + URLUtility.getRequiredUrl(ServiceConstants.LOCATION) + locations[i], token);
				if (sorOutputString.contains("ClassName")) {
					response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
					return response;
				}
				ReportDescriptionByLocationIdSORData[] reportDescriptionByLocationIdSORData = mapper.readValue(sorOutputString, ReportDescriptionByLocationIdSORData[].class);
				reportsResp = getReportsDataFromSOR(reportDescriptionByLocationIdSORData);
			}

			offset = setOffset(offset, reportsResp);
			limit = setLimit(limit, reportsResp);

			try {
				if (!reportsResp.isEmpty()) {
					reportsResp = reportsResp.subList(offset, offset + limit);
				}
			} catch (Exception e) {
				printException(e, ServiceConstants.MEDIA_DESC);
				limit = reportsResp.size();
				reportsResp = reportsResp.subList(offset, limit);
			}
			JSONObject pagination = new JSONObject();
			pagination.put(ServiceConstants.SIZE, limit);
			pagination.put(ServiceConstants.OFFSET, offset);

			result.put(ServiceConstants.PAGINATION, pagination);
			result.put(ServiceConstants.DATA, reportsResp);
			result.put(ServiceConstants.ERRORS, new JSONArray());

			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK, ServiceConstants.SUCCESS_MESSAGE, result);
		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.MEDIA_DESC);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.MEDIA_DESC);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	private List<JSONObject> getReportsDataFromSOR(ReportDescriptionByLocationIdSORData[] reportDescriptionByLocationIdSORData) {
		JSONObject locationReportsObj;
		List<JSONObject> reportsResp = new ArrayList<>();
		if (null != reportDescriptionByLocationIdSORData) {
			String subStringDate = null;
			for (int j = 0; j < reportDescriptionByLocationIdSORData.length; j++) {
				locationReportsObj = new JSONObject();
				String incidents = reportDescriptionByLocationIdSORData[j].getNUMBER_INC();
				String ncrs = reportDescriptionByLocationIdSORData[j].getNUMBER_NCR();
				int intIncidents = Integer.parseInt(incidents);
				int intncrs = Integer.parseInt(ncrs);
				/**
				 * String date = reportDescriptionByLocationIdSORData[j].
				 * getMODIFICATION_DATE();
				 * if(StringUtils.isNotBlank(date)){
				 * subStringDate = date.substring(0,10);
				 * }
				 **/
				locationReportsObj.put(ServiceConstants.VISIT_REPORT_ID, reportDescriptionByLocationIdSORData[j].getID_REPORT());
				locationReportsObj.put(ServiceConstants.DATE, reportDescriptionByLocationIdSORData[j].getMODIFICATION_DATE());
				locationReportsObj.put(ServiceConstants.DISPLAYNAME, reportDescriptionByLocationIdSORData[j].getFORM_NAME());
				locationReportsObj.put(ServiceConstants.FILE_EXTENSION, "pdf");
				locationReportsObj.put(ServiceConstants.FILE_NAME, null);
				locationReportsObj.put(ServiceConstants.LAST_DOWNLOAD_DATE, null);
				locationReportsObj.put(ServiceConstants.LOC_ID, reportDescriptionByLocationIdSORData[j].getID_LOCATION());
				locationReportsObj.put(ServiceConstants.NO_OF_INCIDENTS, intIncidents);
				locationReportsObj.put(ServiceConstants.NO_OF_NCRS, intncrs);
				locationReportsObj.put(ServiceConstants.PATH, null);
				locationReportsObj.put(ServiceConstants.TYPE, "Reports");
				reportsResp.add(locationReportsObj);
			}
		}
		return reportsResp;
	}

	void printException(Exception e, String methodName) {

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

	@Override
	public Response getSingleNcrsByLocationId(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String userId, String visitId,
			String locationId, Integer offset, Integer limit, String token) {
		Response response = null;
		List<JSONObject> locationResp = new ArrayList<>();
		JSONObject result = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		logger.severe("Start time of single NCR " + URLUtility.getTime());
		try {
			if (StringUtils.isBlank(userId)) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY, ServiceConstants.LEVEL_ERROR);
				return response;
			}
			String sorOutputStringForVisits = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.NCR_BY_LOCATION_ID) + visitId, token);
			if (sorOutputStringForVisits.contains(ServiceConstants.EXCEPTION_TYPE)) {
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputStringForVisits, apiVersion);
				return response;
			} else if (sorOutputStringForVisits.contains("ClassName")) {
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringForVisits, apiVersion);
				return response;
			}
			locationResp = new SingleNCRData().getSingleNCR(locationId, token, mapper, locationResp, userId, visitId, sorOutputStringForVisits);

			result.put(ServiceConstants.DATA, null != locationResp ? locationResp : new JSONArray());
			result.put(ServiceConstants.ERRORS, new JSONArray());

			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK, ServiceConstants.SUCCESS_MESSAGE, result);
			logger.severe("End time of single NCR " + URLUtility.getTime());
		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.GET_NCR);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_NCR);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	@Override
	public Response getSinglePunchListItemByLocationId(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String userId,
			String visitId, Integer offset, Integer limit, String token) {
		Response response = null;
		List<JSONObject> punchResp = new ArrayList<>();
		JSONObject result = new JSONObject();
		logger.severe("Start time of single punchList " + URLUtility.getTime());
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			String sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.PUNCH_LIST_BY_LOCATION_ID) + visitId, token);
			if (sorOutputString.contains(ServiceConstants.EXCEPTION_TYPE)) {
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
				return response;
			} else if (sorOutputString.contains("ClassName")) {
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
				return response;
			}
			punchResp = new SinglePunchListData().getSinglePunchList(mapper, sorOutputString, visitId, userId, token, punchResp);

			result.put(ServiceConstants.DATA, null != punchResp ? punchResp : new JSONArray());
			result.put(ServiceConstants.ERRORS, new JSONArray());

			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK, ServiceConstants.SUCCESS_MESSAGE, result);
			logger.severe("End time of single punchLists " + URLUtility.getTime());

		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.PUNCH_RESPONSE);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.PUNCH_RESPONSE);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	@Override
	public Response getSingleIncidentByLocationId(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String userId,
			String visitId, Integer offset, Integer limit, String token) {
		Response response = null;
		List<JSONObject> incidentResp = new ArrayList<>();
		JSONObject result = new JSONObject();
		logger.severe("Start time of single incidents " + URLUtility.getTime());
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			String sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.INCIDENT_BY_LOCATION_ID) + visitId, token);
			if (sorOutputString.contains("ExceptionType")) {
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputString, apiVersion);
				return response;
			} else if (sorOutputString.contains("ClassName")) {
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
				return response;
			}
			incidentResp = new SingleIncidentsData().getSingleIncidents(sorOutputString, token, mapper, visitId, incidentResp, userId);

			result.put("data", incidentResp != null ? incidentResp : new JSONArray());
			result.put("errors", new JSONArray());

			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK, ServiceConstants.SUCCESS_MESSAGE, result);

			logger.severe("End time of single incidents " + URLUtility.getTime());
		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.INCIDENT_RESPONSE);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.INCIDENT_RESPONSE);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	@Override
	public Response getLocationByIdFromSingleSource(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String locationIds,
			String token) {
		Response response = null;
		JSONObject result = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		JSONObject pagination = new JSONObject();
		String[] locations = locationIds.split(ServiceConstants.COMMA);
		try {
			if (locations == null || locations.length == 0) {
				response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY, ServiceConstants.LEVEL_ERROR);
				return response;
			}
			String url = getUrlWithMultipleLocationIds(locations);
			// Invoking the SOR service
			String responseFromSingleSORForLocation = AdapterExceptionHandler.getSORData(url, token);
			if (responseFromSingleSORForLocation.contains(ServiceConstants.EXCEPTION_TYPE)) {
				response = adapterExceptionHandler.getSORDataExcpetion(responseFromSingleSORForLocation, apiVersion);
				return response;
			} else if (responseFromSingleSORForLocation.contains("ClassName")) {
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(responseFromSingleSORForLocation, apiVersion);
				return response;
			}
			IOSLocationByLocationIdSORData[] dataFromSOR = mapper.readValue(responseFromSingleSORForLocation, IOSLocationByLocationIdSORData[].class);
			response = mapAndGetResponseFromSORData(apiVersion, result, pagination, dataFromSOR);
		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.GET_IOS_LOCATION);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.GET_IOS_LOCATION);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	/**
	 * @param apiVersion
	 * @param result
	 * @param pagination
	 * @param dataFromSOR
	 * @return Response
	 * @throws IOException
	 */
	private Response mapAndGetResponseFromSORData(String apiVersion, JSONObject result, JSONObject pagination, IOSLocationByLocationIdSORData[] dataFromSOR) throws IOException {
		Response response;
		JSONObject wholeObj;
		List<JSONObject> locationResp = new ArrayList<>();
		for (IOSLocationByLocationIdSORData data : dataFromSOR) {
			wholeObj = new JSONObject();
			wholeObj.put(ServiceConstants.LOCATION_ID, null != data.getId_location() ? data.getId_location().toString() : null);
			wholeObj.put(ServiceConstants.DISPLAY_NAME, data.getName());
			wholeObj.put(ServiceConstants.STREET_ADDRESS, data.getAddress());
			wholeObj.put(ServiceConstants.ASSOCIATED_ITEMS_ID, data.getId_Associated_Items());
			wholeObj.put(ServiceConstants.ASSOCIATED_MEDIA_FILES_ID, data.getId_Associated_Media_Fields());
			locationResp.add(wholeObj);
		}
		if (locationResp.isEmpty()) {
			response = APIResponseBuilder.sendFailResponse(apiVersion, 400, ServiceConstants.ERROR_MESSAGE_ERROR, "400", "SOR data issue in getLocationByIdFromSingleSource",
					ServiceConstants.LEVEL_ERROR);
			return response;
		}
		pagination.put(ServiceConstants.SIZE, 0);
		pagination.put(ServiceConstants.OFFSET, 0);
		result.put(ServiceConstants.PAGINATION, pagination);
		result.put(ServiceConstants.DATA, locationResp);
		result.put(ServiceConstants.ERRORS, new JSONArray());
		response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK, ServiceConstants.SUCCESS_MESSAGE, result);
		return response;
	}

	private String getUrlWithMultipleLocationIds(String[] locations) {
		StringBuilder url = new StringBuilder(URLUtility.getRequiredUrl(ServiceConstants.IOS_LOCATION_BY_ID));
		boolean isMultiLocations = false;
		try {
			for (String location : locations) {
				if (!isMultiLocations) {
					url.append(location);
					isMultiLocations = true;
				} else {
					url.append(URLUtility.getRequiredUrl(ServiceConstants.ADD_LOCATION));
					url.append(location);
				}
			}
		} catch (Exception e) {
			printException(e, "getUrlWithMultipleLocationIds");
		}
		return url.toString();
	}

	@Override
	public Response updateNewNcr(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String ncrRequest, String token) {
		Response response = null;
		String updateResult = "";
		JSONObject resObject = new JSONObject();
		try {
			if (ncrRequest.isEmpty()) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY));
			}
			updateResult = UpdateNCR.processUpdateNewNCR(ncrRequest, token);
			if (updateResult.isEmpty()) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse("400",
						ServiceConstants.EMPTY_NCR_SOR_RESPONSE));
			}
			resObject = getResponseObject(updateResult, resObject, "NCR");
			JSONObject resObjectValue = (JSONObject) JSONValue.parse(updateResult);
			String code = (String) resObjectValue.get("id");
			int responseCode = Integer.parseInt(code);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode, ServiceConstants.SUCCESS_MESSAGE, resObject);
		} catch (TecnicasException te) {
			throw te;
		} catch (Exception e) {
			printException(e, "updateNewNcr");
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	@Override
	public Response updateNewIncident(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String incidentRequest, String token) {
		Response response = null;
		String result = "";
		JSONObject resObject = new JSONObject();
		try {
			if (incidentRequest.isEmpty()) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY));
			}
			result = UpdateIncidents.processUpdateNewIncidents(incidentRequest, token);
			if (result.isEmpty()) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse("400",
						ServiceConstants.EMPTY_INCIDENT_SOR_RESPONSE));
			}
			resObject = getResponseObject(result, resObject, "Incidents");
			JSONObject resObjectValue = (JSONObject) JSONValue.parse(result);
			String code = (String) resObjectValue.get("id");
			int responseCode = Integer.parseInt(code);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode, ServiceConstants.SUCCESS_MESSAGE, resObject);
		} catch (TecnicasException te) {
			throw te;
		} catch (Exception e) {
			printException(e, "updateIncident");
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	@Override
	public Response updateNewPunchListItems(String contentType, String acceptLanguage, String apiVersion, String loggedInUser, String userAgent, String punchListItem, String token) {
		Response response = null;
		JSONObject resObject = new JSONObject();
		String result = "";
		try {
			if (punchListItem.isEmpty()) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY));
			}
			result = UpdatePunchListItems.processUpdateNewPunchListItems(punchListItem, token);
			if (result.isEmpty()) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse("400",
						ServiceConstants.EMPTY_PUNCHLIST_SOR_RESPONSE));
			}
			resObject = getResponseObject(result, resObject, "PunchListItems");
			JSONObject resObjectValue = (JSONObject) JSONValue.parse(result);
			String code = (String) resObjectValue.get("id");
			int responseCode = Integer.parseInt(code);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode, ServiceConstants.SUCCESS_MESSAGE, resObject);
		} catch (TecnicasException te) {
			throw te;
		} catch (Exception e) {
			printException(e, "updatePunchListItem");
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
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

	@Override
	public Response getNewMediaDescriptionByLocationId(String contentType,
			String acceptLanguage, String language, String apiVersion,
			String loggedInUser, String userAgent, String locationId,
			Integer offset, Integer limit, String token) {
		Response response = null;
		JSONObject result = new JSONObject();
		List<JSONObject> mediaItemResp = new ArrayList<>();
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			if (StringUtils.isBlank(locationId)) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.INVALID_LOCATION_ID));
			}
			String mediaUrl = generateMediaURL(locationId);
			String sorOutputStringForRequest = AdapterExceptionHandler.getSORData(
					URLUtility.getRequiredUrl(ServiceConstants.NEW_MEDIA_URL) + mediaUrl,token);
			if (StringUtils.isBlank(sorOutputStringForRequest)) {
				throw new TecnicasException(ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_CODE_OK, getErrorResponse(
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.MEDIA_DESCRIPTION_NO_RESPONSE));
			}
			if (sorOutputStringForRequest.contains("ExceptionType")) {
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputStringForRequest, apiVersion);
				return response;
			} else if (sorOutputStringForRequest.contains("ClassName")) {
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringForRequest, apiVersion);
				return response;
			}
			MediaDescriptionList[] mediaList = mapper.readValue(sorOutputStringForRequest, MediaDescriptionList[].class);
			setMediaItems(mediaList,mediaItemResp);
		offset = setOffset(offset, mediaItemResp);
		limit = setLimit(limit, mediaItemResp);
		try {
			mediaItemResp = mediaItemResp.subList(offset, offset + limit);
		} catch (Exception e) {
			printException(e, ServiceConstants.NEW_MEDIA_DESCRIPTION_BY_ID);
			limit = mediaItemResp.size();
			mediaItemResp = mediaItemResp.subList(offset, limit);
		}
		JSONObject pagination = new JSONObject();
		pagination.put(ServiceConstants.SIZE, limit);
		pagination.put(ServiceConstants.OFFSET, offset);

		result.put(ServiceConstants.PAGINATION, pagination);
		result.put(ServiceConstants.DATA, mediaItemResp);
		result.put(ServiceConstants.ERRORS, new JSONArray());

		response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK, ServiceConstants.SUCCESS_MESSAGE, result);
		} catch (UnknownHostException uhe) {
			printException(uhe, ServiceConstants.NEW_MEDIA_DESCRIPTION_BY_ID);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR, ServiceConstants.VPN,
					ServiceConstants.VPN_NOT_CONNECTED, ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e, ServiceConstants.NEW_MEDIA_DESCRIPTION_BY_ID);
			response = APIResponseBuilder.sendFailResponse(apiVersion, ServiceConstants.ERROR_CODE_ERROR, ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(), ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

	private void setMediaItems(MediaDescriptionList[] mediaList,
			List<JSONObject> mediaItemResp) {
		
	JSONObject mediaObject;
		try{
			if (null != mediaList) {
				for (int k = 0; k < mediaList.length; k++) {
					mediaObject = new JSONObject();
					String locationIds = mediaList[k].getLocationId();
					ArrayList<String> locationIdList = getLocationIdList(locationIds);
					if(null!=locationIdList && locationIdList.size()>1){
						for(int i=0;i<locationIdList.size();i++){
							mediaObject = new JSONObject();
							mediaObject.put(ServiceConstants.DOCUMENT_TAG, mediaList[k].getDocumentTag());
							mediaObject.put(ServiceConstants.FILE_NAME, mediaList[k].getFileName());
							boolean version = getNewVersionAvailable(mediaList[k].getNewVersionAvailable());
							mediaObject.put(ServiceConstants.NEW_VERSION_AVAILABLE, version);
							mediaObject.put(ServiceConstants.VERSION, mediaList[k].getVersion());
							mediaObject.put(ServiceConstants.FILE_EXTENSION, mediaList[k].getFileExtension());
							mediaObject.put(ServiceConstants.MEDIA_DESCRIPTION, mediaList[k].getMediaDescription());
							mediaObject.put(ServiceConstants.TYPE, mediaList[k].getType());
							mediaObject.put(ServiceConstants.TITLE, mediaList[k].getTitle());
							mediaObject.put(ServiceConstants.CODE, mediaList[k].getCode());
							mediaObject.put(ServiceConstants.MEDIAID, mediaList[k].getMediaId());
							mediaObject.put(ServiceConstants.REF_VENDOR, ""!=mediaList[k].getRefVendor()?mediaList[k].getRefVendor():null);
							mediaObject.put(ServiceConstants.SUPPORT_DOC_TYPE, mediaList[k].getSupportingDocumentType());
							mediaObject.put(ServiceConstants.DOCUMENT_STATUS, mediaList[k].getDocumentStatus());
							mediaObject.put(ServiceConstants.DISPLAYNAME, mediaList[k].getDisplayName());
							mediaObject.put(ServiceConstants.LOCATION_ID, locationIdList.get(i));
							mediaItemResp.add(mediaObject);
						}
					}else{
					mediaObject.put(ServiceConstants.DOCUMENT_TAG, mediaList[k].getDocumentTag());
					mediaObject.put(ServiceConstants.FILE_NAME, mediaList[k].getFileName());
					boolean version = getNewVersionAvailable(mediaList[k].getNewVersionAvailable());
					mediaObject.put(ServiceConstants.NEW_VERSION_AVAILABLE, version);
					mediaObject.put(ServiceConstants.VERSION, mediaList[k].getVersion());
					mediaObject.put(ServiceConstants.FILE_EXTENSION, mediaList[k].getFileExtension());
					mediaObject.put(ServiceConstants.MEDIA_DESCRIPTION, mediaList[k].getMediaDescription());
					mediaObject.put(ServiceConstants.TYPE, mediaList[k].getType());
					mediaObject.put(ServiceConstants.TITLE, mediaList[k].getTitle());
					mediaObject.put(ServiceConstants.CODE, mediaList[k].getCode());
					mediaObject.put(ServiceConstants.MEDIAID, mediaList[k].getMediaId());
					mediaObject.put(ServiceConstants.REF_VENDOR, ""!=mediaList[k].getRefVendor()?mediaList[k].getRefVendor():null);
					mediaObject.put(ServiceConstants.SUPPORT_DOC_TYPE, mediaList[k].getSupportingDocumentType());
					mediaObject.put(ServiceConstants.DOCUMENT_STATUS, mediaList[k].getDocumentStatus());
					mediaObject.put(ServiceConstants.DISPLAYNAME, mediaList[k].getDisplayName());
					mediaObject.put(ServiceConstants.LOCATION_ID, locationIds);
					mediaItemResp.add(mediaObject);
					}
				}
		}
		}catch(Exception e){
			printException(e, ServiceConstants.SET_MEDIA_ITEMS);
		}
	}

	private ArrayList<String> getLocationIdList(String locationIds) {
		ArrayList<String> locationIdList = new ArrayList<>();
		if (locationIds.contains("-")) {
		try{
			List<String> locationList = Arrays.asList(locationIds.trim().split("-"));
			for (int i = 0; i < locationList.size(); i++) {
				locationIdList.add(locationList.get(i));
			}
		}catch(Exception e){
			printException(e, ServiceConstants.GET_LOCATION_ID_LIST);
		}
		
	}else{
		locationIdList.add(locationIds);
	}
		return locationIdList;
	}

	private String generateMediaURL(String locationId) {
			StringBuilder builder = new StringBuilder();
			if (locationId.contains(",")) {
				List<String> idMediaList = Arrays.asList(locationId.trim().split(","));
				for (int i = 0; i < idMediaList.size(); i++) {
					builder.append("idLocation=").append(idMediaList.get(i));
					if (i != idMediaList.size() - 1) {
						builder.append("&");
					}
				}
				return builder.toString();
			} else
				return builder.append("idLocation=").append(locationId).toString();
	}
}
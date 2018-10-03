package com.ibm.mobilefirst.industrial.materialsinspect.adapter.media.demo.data;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.Utils.URLUtility;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.media.MediaAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.media.MediaResource;
import com.ibm.mobilefirst.industrial.materialsinspect.api.media.UserRequestByIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.media.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.media.response.APIResponseBuilder;
import com.ibm.mobilefirst.industrial.materialsinspect.api.media.exception.AdapterExceptionHandler;


public class MediaDataAdapter implements MediaAdapter {

	static Logger logger = Logger.getLogger(MediaResource.class.getName());

	AdapterExceptionHandler  adapterExceptionHandler= new AdapterExceptionHandler();
	@Override
	public Response getMediaFileByMediaPath(String contentType, String acceptLanguage, String apiVersion,
			String userAgent, String mediaId,String mediaType, Integer offset, Integer limit,String token) {
		Response response = null;
		List<JSONObject> mediaItemResp = new ArrayList<>();
		JSONObject result = new JSONObject();
		byte[] byteResponse=null;
		HttpURLConnection con=null;
		try{
			if(ServiceConstants.VENDOR.equalsIgnoreCase(mediaType) || ServiceConstants.ORDER.equalsIgnoreCase(mediaType)){
				URL url = new URL(URLUtility.getRequiredUrl(ServiceConstants.DOCUMENT_URL)+mediaId);
				con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod(ServiceConstants.GET);
				con.setRequestProperty(ServiceConstants.CONTENT_TYPE, ServiceConstants.APPLICATIONJSON);
				con.setRequestProperty(ServiceConstants.AUTHORIZATION,token);
				con.setRequestProperty(ServiceConstants.LANGUAGE,ServiceConstants.US);
				con.setRequestProperty("Cache-Control","no-cache,no-store");
				con.setRequestProperty("Pragma","no-cache");
				con.setDoOutput(true);
				con.connect();
				int responseCode = con.getResponseCode();
				if(403==responseCode){
					response = APIResponseBuilder.sendFailResponse(apiVersion,
							403,
							ServiceConstants.ERROR_MESSAGE_ERROR,
							"403", "Authorization is not valid.Please login again.",
							ServiceConstants.LEVEL_ERROR);
					return response;
				}else if(200!=responseCode){
					response = APIResponseBuilder.sendFailResponse(apiVersion,
							ServiceConstants.ERROR_CODE_ERROR,
							ServiceConstants.ERROR_MESSAGE_ERROR,
							"400", "No data from SOR for the id "+mediaId,
							ServiceConstants.LEVEL_ERROR);
					return response;
				}
				InputStream in = new BufferedInputStream(con.getInputStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1!=(n=in.read(buf)))
				 {
				   out.write(buf, 0, n);
				 }
				out.close();
				in.close();
				byteResponse = out.toByteArray();
			}else if(ServiceConstants.ATTACHMENTS.equalsIgnoreCase(mediaType)){
				URL url = new URL(URLUtility.getRequiredUrl(ServiceConstants.ATTACHMENT_URL)+mediaId);
				con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod(ServiceConstants.GET);
				con.setRequestProperty(ServiceConstants.CONTENT_TYPE, ServiceConstants.APPLICATIONJSON);
				con.setRequestProperty(ServiceConstants.AUTHORIZATION,token);
				con.setRequestProperty(ServiceConstants.LANGUAGE,ServiceConstants.US);
				con.setRequestProperty("Cache-Control","no-cache,no-store");
				con.setRequestProperty("Pragma","no-cache");
				con.setDoOutput(true);
				con.connect();
				int responseCode = con.getResponseCode();
				if(403==responseCode){
					response = APIResponseBuilder.sendFailResponse(apiVersion,
							403,
							ServiceConstants.ERROR_MESSAGE_ERROR,
							"403", "Authorization is not valid.Please login again.",
							ServiceConstants.LEVEL_ERROR);
					return response;
				}else if(200!=responseCode){
					response = APIResponseBuilder.sendFailResponse(apiVersion,
							ServiceConstants.ERROR_CODE_ERROR,
							ServiceConstants.ERROR_MESSAGE_ERROR,
							"400", "No data from SOR for the id "+mediaId,
							ServiceConstants.LEVEL_ERROR);
					return response;
				}
				InputStream in = new BufferedInputStream(con.getInputStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1!=(n=in.read(buf)))
				  {
				    out.write(buf, 0, n);
				  }
				out.close();
				in.close();
				byteResponse = out.toByteArray();
			}else if(ServiceConstants.REPORTS.equalsIgnoreCase(mediaType)){
				URL url = new URL(URLUtility.getRequiredUrl(ServiceConstants.REPORT_URL)+mediaId);
				con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod(ServiceConstants.GET);
				con.setRequestProperty(ServiceConstants.CONTENT_TYPE, ServiceConstants.APPLICATIONJSON);
				con.setRequestProperty(ServiceConstants.AUTHORIZATION,token);
				con.setRequestProperty(ServiceConstants.LANGUAGE,ServiceConstants.US);
				con.setRequestProperty("Cache-Control","no-cache,no-store");
				con.setRequestProperty("Pragma","no-cache");
				con.setDoOutput(true);
				con.connect();
				int responseCode = con.getResponseCode();
				if(403==responseCode){
					response = APIResponseBuilder.sendFailResponse(apiVersion,
							403,
							ServiceConstants.ERROR_MESSAGE_ERROR,
							"403", "Authorization is not valid.Please login again.",
							ServiceConstants.LEVEL_ERROR);
					return response;
				}else if(200!=responseCode){
					response = APIResponseBuilder.sendFailResponse(apiVersion,
							ServiceConstants.ERROR_CODE_ERROR,
							ServiceConstants.ERROR_MESSAGE_ERROR,
							"400", "No data from SOR for the id "+mediaId,
							ServiceConstants.LEVEL_ERROR);
					return response;
				}
				InputStream in = new BufferedInputStream(con.getInputStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1!=(n=in.read(buf)))
				  {
				    out.write(buf, 0, n);
				  }
				out.close();
				in.close();
				byteResponse = out.toByteArray();
			}
			
		    JSONObject mediaObj = new JSONObject();
			mediaObj.put(ServiceConstants.BYTE_DATA, byteResponse);
			mediaItemResp.add(mediaObj);
			if(mediaItemResp.isEmpty()){
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						400,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						"400", "SOR data issue in getMediaFiles",
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			offset = setOffset(offset,mediaItemResp);
			limit = setLimit(limit,mediaItemResp);

			try {
				mediaItemResp = mediaItemResp.subList(offset, offset+ limit);
			} catch (Exception e) {
				printException(e,ServiceConstants.MEDIA_BY_MEDIA_FILE_PATH);
				limit = mediaItemResp.size();
				mediaItemResp = mediaItemResp.subList(offset, limit);
			}
			JSONObject pagination = new JSONObject();
			pagination.put(ServiceConstants.SIZE,limit);
			pagination.put(ServiceConstants.OFFSET,offset);
			
			result.put(ServiceConstants.PAGINATION, pagination);
			result.put(ServiceConstants.DATA, mediaItemResp);
			result.put(ServiceConstants.ERRORS, new JSONArray());
			
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK,
					ServiceConstants.SUCCESS_MESSAGE,result);
		}catch(UnknownHostException uhe){
			printException(uhe,ServiceConstants.MEDIA_BY_MEDIA_FILE_PATH);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
					ServiceConstants.LEVEL_ERROR);
		} catch (Exception e) {
			printException(e,ServiceConstants.MEDIA_BY_MEDIA_FILE_PATH);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(),
					ServiceConstants.LEVEL_ERROR);
		}
		return response;
		
	}
	
	@Override
	public Response updateMedia(String contentType, String acceptLanguage,
			String userAgent, String apiVersion, String loggedInUser,String userId,String visitId,
			String mediaData, String token) {
		Response response = null;
		String formId = null;
		JSONObject result = new JSONObject();
		List<JSONObject> error = new ArrayList<>();
		JSONObject sorRequest = new JSONObject();
		JSONObject listAttachment = new JSONObject();
		JSONObject attachmentObject = new JSONObject();
		JSONArray dataArray = new JSONArray();
		JSONObject dataObject = new JSONObject();
		JSONObject jsonRequest = new JSONObject();
		List<JSONObject> attachmentArray = new ArrayList<>();
		JSONObject iOSResponse = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		
		try{
			if(mediaData.isEmpty()){
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						ServiceConstants.ERROR_CODE_ERROR,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY,
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			String sorOutputStringUserRequest = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL)+userId+URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_N),token);
			if(sorOutputStringUserRequest.contains("ExceptionType")){
				response = adapterExceptionHandler.getSORDataExcpetion(sorOutputStringUserRequest, apiVersion);
				return response;
			}else if(sorOutputStringUserRequest.contains("ClassName")){
				response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringUserRequest, apiVersion);
				return response;
			}
			dataObject = (JSONObject) JSONValue.parse(mediaData);
			dataArray = (JSONArray) dataObject.get(ServiceConstants.DATA);
			jsonRequest = (JSONObject) dataArray.get(0);
			logger.severe("IOS request for updateMedia :" + jsonRequest);
			formId = (String) jsonRequest.get("formId");
			List<JSONObject> associatedQuestion = (List<JSONObject>) jsonRequest.get(ServiceConstants.ASSOCIATED_QUESTION);
			String questionId = (String) associatedQuestion.get(0).get(ServiceConstants.QUESTION_ID);
			attachmentObject.put(ServiceConstants.ID_ATTACHMENT, null);
			attachmentObject.put(ServiceConstants.NAME, jsonRequest.get(ServiceConstants.TITLE)+ServiceConstants.DOT+jsonRequest.get(ServiceConstants.FILE_EXTENSION).toString());
			attachmentObject.put(ServiceConstants.DESC, null);
			attachmentObject.put(ServiceConstants.DATE_CREATED, null);
			String type = getType(jsonRequest);
			String id = getTypeId(type);
			attachmentObject.put(ServiceConstants.TYPE, type);
			attachmentObject.put(ServiceConstants.ID_TYPE, id);
			attachmentObject.put(ServiceConstants.THUMBNAIL,null);
			attachmentObject.put(ServiceConstants.FILE_PATH, null);
			listAttachment.put(ServiceConstants.ATTACHMENT, attachmentObject);
			listAttachment.put(ServiceConstants.SERIALIZED_FILE, jsonRequest.get("originalImageData").toString());
			attachmentArray.add(listAttachment);
			sorRequest.put(ServiceConstants.LIST_ATTACHMENT,attachmentArray);
			sorRequest.put(ServiceConstants.ID_DESTINATION,formId);
			sorRequest.put(ServiceConstants.TYPE,ServiceConstants.FIELD);
			logger.severe("Before posting mediaItems :" + sorRequest);
			JSONObject output = URLUtility.getSORDataWithHeaderParamsForUploadMedia(URLUtility.getRequiredUrl(ServiceConstants.MEDIA_UPLOAD),sorRequest.toString(),token);
			String code = (String) output.get("id");
			if("200".equalsIgnoreCase(code)){
				String value = (String) output.get("value");
				if(StringUtils.isBlank(value)){
		            	value = ServiceConstants.UPLOAD_MEDIA_OUTPUT;
		            	iOSResponse.put("message",value);
		            	error.add(iOSResponse);
		            	result.put("data", new JSONArray());
						result.put("errors", error);	
						response = APIResponseBuilder.sendSuccessResponse(apiVersion, 500,
								ServiceConstants.SUCCESS_MESSAGE,result);
						return response;
	            }
				JSONArray arrayOutput = (JSONArray) JSONValue.parse(value);
				logger.severe("arrayOutput value :" + arrayOutput);
				
				 JSONObject idObject = (JSONObject) arrayOutput.get(0);
				 logger.severe("idObject value :" + idObject);
				 Object attId = idObject.get(ServiceConstants.ID_ATTACHMENT);
				 logger.severe("attId value :" + attId);
				 iOSResponse.put(ServiceConstants.DISPLAY_NAME, null);
				 iOSResponse.put(ServiceConstants.FILE_NAME, jsonRequest.get(ServiceConstants.FILE_NAME).toString());
				 iOSResponse.put(ServiceConstants.LAST_DOWNLOAD_DATE, null);
				 iOSResponse.put(ServiceConstants.MEDIA_DESCRIPTION, null);
				 iOSResponse.put(ServiceConstants.MEDIA_ID, attId.toString());
				 iOSResponse.put(ServiceConstants.NEW_VERSION_AVAILABLE, null);
				 iOSResponse.put(ServiceConstants.PATH, null);
				 iOSResponse.put(ServiceConstants.SYNC_STATUS, null);
				 iOSResponse.put(ServiceConstants.THUMB_NAIL, null);
				 iOSResponse.put(ServiceConstants.TYPE, jsonRequest.get("type").toString());
				 iOSResponse.put(ServiceConstants.FILE_EXTENSION, jsonRequest.get(ServiceConstants.FILE_EXTENSION).toString());
				 iOSResponse.put(ServiceConstants.VERSION, null);
				 iOSResponse.put(ServiceConstants.TITLE, jsonRequest.get(ServiceConstants.TITLE));
				 iOSResponse.put(ServiceConstants.DOCUMENT_STATUS, null);
				 iOSResponse.put(ServiceConstants.QUESTION_ID,questionId);
				 iOSResponse.put(ServiceConstants.SUPPORTING_DOCUMENT_TYPE, jsonRequest.get(ServiceConstants.SUPPORTING_DOCUMENT_TYPE).toString());
				 result.put(ServiceConstants.DATA, iOSResponse);
				 result.put(ServiceConstants.ERRORS, new JSONArray());	
			}else if("403".equalsIgnoreCase(code)){
				iOSResponse.put("message", "Authorization is not valid.Please login again");
				iOSResponse.put("id", code);
				iOSResponse.put("level", "ERROR");
				error.add(iOSResponse);
				result.put(ServiceConstants.DATA, new JSONArray());
				result.put(ServiceConstants.ERRORS, error);	
			}else{
				iOSResponse.put("message", "SOR update failed for update media");
				iOSResponse.put("id", code);
				iOSResponse.put("level", "ERROR");
				error.add(iOSResponse);
				result.put(ServiceConstants.DATA, new JSONArray());
				result.put(ServiceConstants.ERRORS, error);	
			}
			
			int responseCode = Integer.parseInt(code);
			response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode,
					ServiceConstants.SUCCESS_MESSAGE,result);
					
		}catch(UnknownHostException uhe){
			printException(uhe,"updateMedia");
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
					ServiceConstants.LEVEL_ERROR);
		}catch(Exception e){
			printException(e,ServiceConstants.UPDATE_MEDIA);
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(),
					ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}
	
	private String getType(JSONObject jsonRequest) {
		String type=null;
		Map<String,String> typesKeyMap = getTypeMap();
		try{
			String extension = (String)jsonRequest.get(ServiceConstants.FILE_EXTENSION);
			type = typesKeyMap.get(extension);
		}catch(Exception e){
			printException(e,ServiceConstants.GET_TYPE);
		}
		return type;
	}
	
	private String getTypeId(String type) {
		String typeId=null;
		Map<String,String> typeIdKeyMap = getTypeIdMap();
		try{
			typeId = typeIdKeyMap.get(type);
		}catch(Exception e){
			printException(e,ServiceConstants.GET_TYPE_ID);
		}
		return typeId;
	}
	private String getFormId(String userId,String visitId,String authToken,ObjectMapper mapper) {
		Object formId="";
		try{
			String sorOutputStringUserRequest = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL)+userId+URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_N),authToken);
			UserRequestByIdSORData[] userReqByIdobj = mapper.readValue(sorOutputStringUserRequest, UserRequestByIdSORData[].class);
			for(int i=0;i<userReqByIdobj.length;i++){
				Object eventId = userReqByIdobj[i].getID_EVENT();
				if(eventId.toString().equalsIgnoreCase(visitId)){
					formId = userReqByIdobj[i].getID_FORM();
				}
			}
		}catch(Exception e){
			printException(e,ServiceConstants.GET_FORM_ID);
		}
		logger.severe("formId value: " + formId);
		return formId.toString();
	}
	private Integer setLimit(Integer limit,List<JSONObject> itemResp) {
		try{
			if(limit == -1 || limit == 0)
				limit = itemResp.size();
		}catch(Exception e){
			printException(e,ServiceConstants.SET_LIMIT);
		}
		return limit;
	}
	private Integer setOffset(Integer offset,List<JSONObject> itemResp) {
        try{
        	if(offset == -1 || offset>=itemResp.size())
				offset = 0;
		}catch(Exception e){
			printException(e,ServiceConstants.SET_OFFSET);
		}
		return offset;
	}
	
	public static Map<String, String>  getTypeMap(){
		Map<String, String> typeKeyMap=new HashMap<>();
		typeKeyMap.put(ServiceConstants.PDF, ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.XLS,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.XLSX,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.XLTX,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.XLSM,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.XLSB,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.DOC,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.DOCX,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.DOCM,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.DOTM,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.DOTX,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.PPT,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.PPTX,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.PPTM,ServiceConstants.DOCUMENT);
		typeKeyMap.put(ServiceConstants.PPSX,ServiceConstants.DOCUMENT);
		
		typeKeyMap.put(ServiceConstants.AVI,ServiceConstants.VIDEO);
		typeKeyMap.put(ServiceConstants.MP4,ServiceConstants.VIDEO);
		typeKeyMap.put(ServiceConstants.MOV,ServiceConstants.VIDEO);
		
		typeKeyMap.put(ServiceConstants.BMP,ServiceConstants.IMAGE);
		typeKeyMap.put(ServiceConstants.GIF,ServiceConstants.IMAGE);
		typeKeyMap.put(ServiceConstants.JPG,ServiceConstants.IMAGE);
		typeKeyMap.put(ServiceConstants.PNG,ServiceConstants.IMAGE);
		typeKeyMap.put(ServiceConstants.JPEG,ServiceConstants.IMAGE);
		
		typeKeyMap.put(ServiceConstants.MP3,ServiceConstants.AUDIO);
		typeKeyMap.put(ServiceConstants.WMA,ServiceConstants.AUDIO);
		typeKeyMap.put(ServiceConstants.AAC,ServiceConstants.AUDIO);
		typeKeyMap.put(ServiceConstants.AC3,ServiceConstants.AUDIO);
		return typeKeyMap;
	}
	
	public static Map<String, String>  getTypeIdMap(){
		Map<String, String> getTypeIdMap=new HashMap<>();
		getTypeIdMap.put(ServiceConstants.DOCUMENT, ServiceConstants.DOCUMENT_ID);
		getTypeIdMap.put(ServiceConstants.IMAGE,ServiceConstants.IMAGE_ID);
		getTypeIdMap.put(ServiceConstants.AUDIO,ServiceConstants.AUDIO_ID);
		getTypeIdMap.put(ServiceConstants.VIDEO,ServiceConstants.VIDEO_ID);
		return getTypeIdMap;
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

@Override
public Response ncrMedia(String contentType, String acceptLanguage,
		String userAgent, String apiVersion, String loggedInUser,
		String userId, String visitId, String mediaData, String authToken) {
				Response response = null;
				List<JSONObject> error = new ArrayList<>();
				JSONObject result = new JSONObject();
				JSONObject sorRequest = new JSONObject();
				JSONObject listAttachment = new JSONObject();
				JSONObject attachmentObject = new JSONObject();
				List<JSONObject> attachmentArray = new ArrayList<>();
				JSONObject iOSResponse = new JSONObject();
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
					try{
						if(mediaData.isEmpty()){
							response = APIResponseBuilder.sendFailResponse(apiVersion,
									ServiceConstants.ERROR_CODE_ERROR,
									ServiceConstants.ERROR_MESSAGE_ERROR,
									ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY,
									ServiceConstants.LEVEL_ERROR);
							return response;
						}
						String sorOutputStringUserRequest = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL)+userId+URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_N),authToken);
						if(sorOutputStringUserRequest.contains("ExceptionType")){
							response = adapterExceptionHandler.getSORDataExcpetion(sorOutputStringUserRequest, apiVersion);
							return response;
						}else if(sorOutputStringUserRequest.contains("ClassName")){
							response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputStringUserRequest, apiVersion);
							return response;
						}
						JSONObject dataObject = (JSONObject) JSONValue.parse(mediaData);
						JSONArray dataArray = (JSONArray) dataObject.get("data");
						JSONObject jsonRequest = (JSONObject) dataArray.get(0);
						logger.severe("Request for updateNCR media :" + jsonRequest);
						JSONObject idTypeObject = updateMediaByType(jsonRequest);
						Map<String,String> typeIdTypeMap = getIdTypeMap();
						String typeValue = typeIdTypeMap.get(idTypeObject.get("type"));
						attachmentObject.put("ID_ATTACHMENT", null);
						attachmentObject.put("NAME", jsonRequest.get("title").toString()+"."+jsonRequest.get("fileExtension").toString());
						attachmentObject.put("DESC", null);
						attachmentObject.put("DATE_CREATED", null);
						String type = getType(jsonRequest);
						String id = getTypeId(type);
						attachmentObject.put("TYPE", type);
						attachmentObject.put("ID_TYPE", id);
						attachmentObject.put("THUMBNAIL",null);
						attachmentObject.put("FILEPATH", null);
						listAttachment.put("ATTACHMENT", attachmentObject);
						listAttachment.put("SERIALIZED_FILE", jsonRequest.get("originalImageData").toString());
						attachmentArray.add(listAttachment);
						sorRequest.put("listAttachment",attachmentArray);
						sorRequest.put("ID_DESTINATION",idTypeObject.get("formId"));
						sorRequest.put("TYPE","Field");
						logger.severe("Before posting mediaItems for ncr:" + sorRequest);
						JSONObject output = URLUtility.getSORDataWithHeaderParamsForUploadMedia(URLUtility.getRequiredUrl(ServiceConstants.MEDIA_UPLOAD),sorRequest.toString(),authToken);
						String code = (String) output.get("id");
						if("200".equalsIgnoreCase(code)){
							String value = (String) output.get("value");
							if(null==value || value.isEmpty()){
				            	value = "No value from SOR response after upload media for ncr.";
				            	iOSResponse.put("message",value);
				            	error.add(iOSResponse);
				            	result.put("data", new JSONArray());
								result.put("errors", error);	
								response = APIResponseBuilder.sendSuccessResponse(apiVersion,500,
										ServiceConstants.SUCCESS_MESSAGE,result);
								return response;
				            }
							JSONArray arrayOutput = (JSONArray) JSONValue.parse(value);
							logger.severe("arrayOutput value for ncr:" + arrayOutput);
							 
							 JSONObject idObject = (JSONObject) arrayOutput.get(0);
							 logger.severe("idObject value :" + idObject);
							 Object attId = idObject.get("ID_ATTACHMENT");
							 logger.severe("attId value :" + attId);
							 iOSResponse.put("displayName", null);
							 iOSResponse.put("fileName", jsonRequest.get("fileName").toString());
							 iOSResponse.put("lastDownloadDate", null);
							 iOSResponse.put("mediaDescription", null);
							 iOSResponse.put("mediaId", attId.toString());
							 iOSResponse.put("newVersionAvailable", null);
							 iOSResponse.put("path", null);
							 iOSResponse.put("syncStatus", null);
							 iOSResponse.put("thumbnail", null);
							 iOSResponse.put("type", jsonRequest.get("type").toString());
							 iOSResponse.put("fileExtension", jsonRequest.get("fileExtension").toString());
							 iOSResponse.put("version", null);
							 iOSResponse.put("title", jsonRequest.get("title"));
							 iOSResponse.put("documentStatus", null);
							 iOSResponse.put(typeValue,idTypeObject.get(typeValue));
							 iOSResponse.put("supportingDocumentType", jsonRequest.get("supportingDocumentType").toString());
							 result.put("data", iOSResponse);
							 result.put("errors", new JSONArray());
						}else if("403".equalsIgnoreCase(code)){
							iOSResponse.put("message", "Authorization is not valid.Please login again");
							iOSResponse.put("id", code);
							iOSResponse.put("level", "ERROR");
							error.add(iOSResponse);
							result.put(ServiceConstants.DATA, new JSONArray());
							result.put(ServiceConstants.ERRORS, error);
						}else{
							iOSResponse.put("message", "SOR update failed for update ncr media");
							iOSResponse.put("id", code);
							iOSResponse.put("level", "ERROR");
							error.add(iOSResponse);
							result.put("data", new JSONArray());
							result.put("errors", error);
						}
							int responseCode = Integer.parseInt(code);
							response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode,
									ServiceConstants.SUCCESS_MESSAGE,result);	
					}catch(UnknownHostException uhe){
						printException(uhe,"ncrMedia");
						response = APIResponseBuilder.sendFailResponse(apiVersion,
								ServiceConstants.ERROR_CODE_ERROR,
								ServiceConstants.ERROR_MESSAGE_ERROR,
								ServiceConstants.VPN, ServiceConstants.VPN_NOT_CONNECTED,
								ServiceConstants.LEVEL_ERROR);
					}catch(Exception e){
						printException(e,"ncrupdate");
						response = APIResponseBuilder.sendFailResponse(apiVersion,
								ServiceConstants.ERROR_CODE_ERROR,
								ServiceConstants.ERROR_MESSAGE_ERROR,
								ServiceConstants.ERROR_CODE_UNDEFINEDISSUE, e.getMessage(),
								ServiceConstants.LEVEL_ERROR);
		}
		return response;
	}

private JSONObject updateMediaByType(JSONObject jsonRequest) {
	JSONObject idObject = new JSONObject();
	try{
		if(jsonRequest.containsKey("associatedNCR")){
			List<JSONObject> associatedNCR = (List<JSONObject>) jsonRequest.get("associatedNCR");
			String ncrId = (String) associatedNCR.get(0).get("ncrId");
			String formId = (String) associatedNCR.get(0).get("formId");
			idObject.put("ncrId", ncrId);
			idObject.put("type", "N");
			idObject.put("formId", formId);
		}else if(jsonRequest.containsKey("associatedPunchList")){
			List<JSONObject> associatedNCR = (List<JSONObject>) jsonRequest.get("associatedPunchList");
			String punchListItemId = (String) associatedNCR.get(0).get("punchListItemId");
			String formId = (String) associatedNCR.get(0).get("formId");
			idObject.put("punchListItemId", punchListItemId);
			idObject.put("type", "P");
			idObject.put("formId", formId);
		}else if(jsonRequest.containsKey("associatedIncident")){
			List<JSONObject> associatedNCR = (List<JSONObject>) jsonRequest.get("associatedIncident");
			String incidentId = (String) associatedNCR.get(0).get("incidentId");
			String formId = (String) associatedNCR.get(0).get("formId");
			idObject.put("incidentId", incidentId);
			idObject.put("type", "I");
			idObject.put("formId", formId);
		}
	}catch(Exception e){
		printException(e,"updateMediaByType");
	}
	return idObject;
}

public static Map<String, String>  getIdTypeMap(){
	Map<String, String> getIdTypeMap=new HashMap<>();
	getIdTypeMap.put("N", "ncrId");
	getIdTypeMap.put("I","incidentId");
	getIdTypeMap.put("P","punchListItemId");
	return getIdTypeMap;
}
}

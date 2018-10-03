package com.ibm.mobilefirst.industrial.materialsinspect.api.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.Utils.URLUtility;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.Location.DocumentOrderByOrderIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.Location.DocumentVendorByLocationIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.Location.LocationByLocationIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.Location.SerializedAttachmentFormSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.Location.UserRequestByIdSORData;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.exception.AdapterExceptionHandler;

public class RetrieveItemsIdsFromLocation {
	
	static Logger logger = Logger.getLogger(RetrieveItemsIdsFromLocation.class.getName());
	AdapterExceptionHandler  adapterExceptionHandler= new AdapterExceptionHandler();
	public JSONObject retrieveItemsIds(String locationId,String userId,String token,String formId) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		JSONObject wholeObj = new JSONObject();
		List<String> mediaFilesId = new ArrayList<>();
		try {
				String sorOutputString = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.GET_LOCATION_BY_ID)+locationId,token);
				LocationByLocationIdSORData obj = mapper.readValue(sorOutputString, LocationByLocationIdSORData.class);
				Object orderId = getOrderId(obj);
				if(orderId.toString().isEmpty())
					logger.info("ID_ORDER is empty for the given location id.");
				String sorOutputStringOrder = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.DOCUMENT_ORDER)+orderId.toString(),token);
				DocumentOrderByOrderIdSORData[] docOrderobj = mapper.readValue(sorOutputStringOrder, DocumentOrderByOrderIdSORData[].class);
				setMediaFilesIdFromOrderObject(docOrderobj,mediaFilesId);
				String sorOutputStringLocation = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.DOCUMENT_VENDOR)+locationId,token);
				DocumentVendorByLocationIdSORData[] docVendorobj = mapper.readValue(sorOutputStringLocation, DocumentVendorByLocationIdSORData[].class);
				setMediaFilesIdFromVendorObject(docVendorobj,mediaFilesId);
				String sorOutput = URLUtility.getSORDataWithHeaderParams(URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_1)+userId+URLUtility.getRequiredUrl(ServiceConstants.IS_REQUEST_N),token);
				UserRequestByIdSORData[] userReqByIdobj = mapper.readValue(sorOutput, UserRequestByIdSORData[].class);
				setMediaItemsIdFromSerializedObject(mediaFilesId,userReqByIdobj,mapper,formId,token);
				wholeObj.put("associatedMediaItemsId",mediaFilesId);
				
		} catch (Exception e) {
			printException(e,"retrieveItemsIds");
		} 
		return wholeObj;
	}
	private void setMediaItemsIdFromSerializedObject(List<String> mediaFilesId,
			UserRequestByIdSORData[] userReqByIdobj,ObjectMapper mapper,String formId,String token) {
		try{
			if(null!=userReqByIdobj){
					String sorOutputStringSerialized = AdapterExceptionHandler.getSORData(URLUtility.getRequiredUrl(ServiceConstants.SERIALIZED_OBJECT)+formId,token);
					SerializedAttachmentFormSORData[] serAttFormobj = mapper.readValue(sorOutputStringSerialized, SerializedAttachmentFormSORData[].class);
					setMediaItems(serAttFormobj,mediaFilesId);
			}
		}catch(Exception e){
			printException(e,"setMediaItemsIdFromSerializedObject");
		}
	}
	
	private void setMediaItems(SerializedAttachmentFormSORData[] serAttFormobj,
			List<String> mediaFilesId) {
		try{
			if(null!=serAttFormobj){
				for(int l=0;l<serAttFormobj.length;l++){
					mediaFilesId.add(null!=serAttFormobj[l].getID_ATTACHMENT()?serAttFormobj[l].getID_ATTACHMENT():"");
			}
		 }
		}catch(Exception e){
			printException(e,"setMediaItems");
		}
		
	}
	private void setMediaFilesIdFromVendorObject(
			DocumentVendorByLocationIdSORData[] docVendorobj,
			List<String> mediaFilesId) {
		try{
			if(null==docVendorobj){
				logger.info("Document Vendor object is null or empty for the given location id.");
				}else{
			for(int k=0;k<docVendorobj.length;k++){
				mediaFilesId.add(null!=docVendorobj[k].getId_document()?docVendorobj[k].getId_document().toString():"");
			}
				}
		}catch(Exception e){
			printException(e,"setMediaFilesIdFromVendorObject");
		}
		
	}
	
	private void setMediaFilesIdFromOrderObject(
			DocumentOrderByOrderIdSORData[] docOrderobj, List<String> mediaFilesId) {
		try{
			if(null==docOrderobj){
				logger.info("Document Order object is null or empty for the given order id.");
				}else{
			for(int j=0;j<docOrderobj.length;j++){
				mediaFilesId.add(null!=docOrderobj[j].getId_document()?docOrderobj[j].getId_document().toString():"");
			}
				}
		}catch(Exception e){
			printException(e,"setMediaFilesIdFromOrderObject");
		}
		
	}
	
	private Object getOrderId(LocationByLocationIdSORData obj) {
		Object orderId="";
		try{
			if(null==obj){
				logger.info("Object is null for the given location id.");
			}else{
				orderId = obj.getId_order();
			}
		}catch(Exception e){
			printException(e,"getOrderId");
		}
		return orderId;
	}
	
     private static List<String> getMediaFileIdByDocumentOrderObject(
			DocumentOrderByOrderIdSORData[] docOrderobj,
			List<String> mediaFilesId) {
    	 try{
    		 for(int j=0;j<docOrderobj.length;j++){
 				mediaFilesId.add(null!=docOrderobj[j].getId_document()?docOrderobj[j].getId_document().toString():"");
 			}
    	 }catch(Exception e){
    		 printException(e,"getMediaFileIdByDocumentOrderObject");
    	 }
		 return mediaFilesId;
	}

	private static List<String> getMediaFileIdByDocumentVendorObject(DocumentVendorByLocationIdSORData[] docVendorobj,
			List<String> mediaFilesId) {
				try{
					for(int k=0;k<docVendorobj.length;k++){
						mediaFilesId.add(null!=docVendorobj[k].getId_document()?docVendorobj[k].getId_document().toString():"");
					}
		   	 }catch(Exception e){
		   		printException(e,"getMediaFileIdByDocumentVendorObject");
		   	 }
		 return mediaFilesId;
	}

	static void printException(Exception e,String methodName) {
		
		logger.severe("Exception in "+methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}
}

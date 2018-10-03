/**
 * 
 */
package com.ibm.mobilefirst.industrial.materialsinspect.api.user;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.ArrayUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.Utils.URLUtility;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.constants.ServiceConstants;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.exception.AdapterExceptionHandler;
import com.ibm.mobilefirst.industrial.materialsinspect.api.user.response.APIResponseBuilder;

/**
 * @author IBM
 *
 */
public class PostVisitforUser{
	static Logger logger = Logger.getLogger(PostVisitforUser.class.getName());
	
	
	
	@SuppressWarnings({ "unused", "static-access" })
	public Object getSORJSON(String userId, String locationId,String visitId,String apiVersion, String token){
		logger.severe("getSORJSON");
		AdapterExceptionHandler  adapterExceptionHandler= new AdapterExceptionHandler();
		Response response = null;
		VisitsByIDBean visitsAPISORData =  null;
		String sorOutputString = new String();
		try{	
			String idForm;
			String idEvent;
			String idLocation;
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			String url = URLUtility.getRequiredUrl(ServiceConstants.USER_REQUEST_URL_1)+userId+URLUtility.getRequiredUrl(ServiceConstants.IS_REQUEST_N);
			
			sorOutputString = AdapterExceptionHandler.getSORData(url,token); 
			if(sorOutputString!=null && (sorOutputString.contains("ExceptionType") || sorOutputString.contains("ClassName"))){
				return sorOutputString;
			}
			
			logger.severe("sorOutput value :" +sorOutputString);
			UserVisitByIdSORData[] userVisitByIdobj = null;
			if(sorOutputString!=null){
				 userVisitByIdobj = mapper.readValue(sorOutputString, UserVisitByIdSORData[].class);
			}
			
			if(null==userVisitByIdobj){
				logger.severe("User notification request is empty for the given userId.");
			}else{
				int loopInt =0;
				for(int userReqByIdLoopVar=0;userReqByIdLoopVar<userVisitByIdobj.length;userReqByIdLoopVar++){
					
					idLocation = (null!=userVisitByIdobj[userReqByIdLoopVar].getID_FORM()?userVisitByIdobj[userReqByIdLoopVar].getID_LOCATION():"");
					idEvent = (null!=userVisitByIdobj[userReqByIdLoopVar].getID_EVENT()?userVisitByIdobj[userReqByIdLoopVar].getID_EVENT():"");
					logger.severe("idLocation " +idLocation);
					if(idLocation.equalsIgnoreCase(locationId) && idEvent.equalsIgnoreCase(visitId)){
						loopInt = loopInt+1;
						idForm = (null!=userVisitByIdobj[userReqByIdLoopVar].getID_FORM()?userVisitByIdobj[userReqByIdLoopVar].getID_FORM():"");
						String formURL = URLUtility.getRequiredUrl(ServiceConstants.GET_VISIT_URL)+idForm+URLUtility.getRequiredUrl(ServiceConstants.GET_LOCATION_VISIT_URL)+idLocation+URLUtility.getRequiredUrl(ServiceConstants.GET_EVENT_VISIT_URL)+idEvent+URLUtility.getRequiredUrl(ServiceConstants.GET_USER_VISIT_URL)+userId;
			
						sorOutputString = adapterExceptionHandler.getSORData(formURL,token); 
						logger.severe("SOR visit JSON "  +sorOutputString);
						if(sorOutputString!=null && sorOutputString.contains("ExceptionType")){
							return sorOutputString;
						}else if(sorOutputString!=null && sorOutputString.contains("ClassName")){
							response = adapterExceptionHandler.getSORDataExcpetionForAuthorization(sorOutputString, apiVersion);
							return response;
						}
						
						if(sorOutputString!=null){
							visitsAPISORData = mapper.readValue(sorOutputString,VisitsByIDBean.class);
						}
						
						if(null==visitsAPISORData){
							logger.info("VisitAPI data is empty for the obtained idForm,idLocation,idEvent and idUser.");
						}
					}else{
						logger.severe("Locaiton ID && Event ID does not match in SOR Data");
					}
				}
				
			}
	} catch (Exception e) {
		response = APIResponseBuilder.sendFailResponse(apiVersion,
				ServiceConstants.ERROR_CODE_ERROR,
				ServiceConstants.ERROR_MESSAGE_ERROR,
				ServiceConstants.USER_ID_CHECK_MESSAGE, e.getMessage(),
				ServiceConstants.LEVEL_ERROR);
		printException(e,"PostVisitforUser -getSORJSON");
	}
		return visitsAPISORData;
	}
	

	public ListCards getParentActivityforCatagories(ListCards[] listOfSORCardsforParent, String iosParentID){
		ListCards parentListCard = new ListCards();
		if(listOfSORCardsforParent!=null){
			for (int i = 0; i < listOfSORCardsforParent.length; i++) {
				String sorParentID = (String) listOfSORCardsforParent[i].getExternalId();
				if(sorParentID.equalsIgnoreCase(iosParentID) ){
					parentListCard = listOfSORCardsforParent[i];
				}
			}
		}
		return parentListCard;
	}
	
	
	@SuppressWarnings("unchecked")
	public ListCards[] setActivitiesforCatagories(List<JSONObject> listofiOSActivitiesforCatagories,ListCards[] listOfSORCards, String iosCatagoryId) {
		//Activity Loop Starts
		logger.severe("setActivitiesforCatagories");
		int listSORCardsLength =listOfSORCards.length;
		ListCards[] listOfSORCardsforParent = listOfSORCards;
		int nullLoop =0;
		String strValue="";
		String newCardID = new String();	
		ListCards parentListCard = new ListCards();
		String iosParentID = new String();
		try{
			if(listofiOSActivitiesforCatagories!=null){
				for (int i = 0; i < listofiOSActivitiesforCatagories.size(); i++) {
					nullLoop =0;
					
					for (int j = 0; j < listSORCardsLength; j++) {
						String iosActivityID = (String) listofiOSActivitiesforCatagories.get(i).get("activityId");
						logger.severe("iosactivity " + iosActivityID);
//						String sorActivityID = listOfSORCards[j].getCardId();
						String sorActivityExtID = listOfSORCards[j].getExternalId();
						
						Long isModified = (Long) listofiOSActivitiesforCatagories.get(i).get("isModified");
						if(isModified!=null && isModified == 1){
						
							if((iosActivityID.startsWith("AppGenerated-Activity")) && (nullLoop == 0)){	
								iosParentID = (String) listofiOSActivitiesforCatagories.get(i).get("parentCardId");
								logger.severe("iosParentID : " + iosParentID);
								parentListCard = getParentActivityforCatagories(listOfSORCardsforParent, iosParentID);
								nullLoop = nullLoop +1;
								ListCards listOfNewRCards = new ListCards();	
								listOfNewRCards.setParentActivityId(iosParentID); // added for the new field - parentActivityId
								listOfNewRCards.setInternalId(null);
								listOfNewRCards.setInternalIdCFG((null!=parentListCard.getInternalIdCFG()?parentListCard.getInternalIdCFG().toString():"null")); 
								newCardID = iosCatagoryId +"-"+"Card"+(listSORCardsLength+1);
								listOfNewRCards.setCardId(newCardID); 
								listOfNewRCards.setActivityId(parentListCard.getActivityId()); 
								listOfNewRCards.setAdditionalInformation(parentListCard.getAdditionalInformation());
								listOfNewRCards.setDisplayName(null!=listofiOSActivitiesforCatagories.get(i).get("displayName")?listofiOSActivitiesforCatagories.get(i).get("displayName").toString():"");
								listOfNewRCards.setDisplayOrder((listSORCardsLength+1)+"");
								
								strValue = getRequiredBoolean((Long) listofiOSActivitiesforCatagories.get(i).get("required"));
								listOfNewRCards.setIsRequired(strValue);
	
								strValue = getRequiredBoolean((Long) listofiOSActivitiesforCatagories.get(i).get("duplicable"));
								listOfNewRCards.setIsDuplicable(strValue);
								
								strValue = getRequiredBoolean((Long) listofiOSActivitiesforCatagories.get(i).get("table"));
								listOfNewRCards.setIsTable(parentListCard.getIsTable());
								
								strValue = getRequiredBoolean((Long) listofiOSActivitiesforCatagories.get(i).get("skippable"));
								listOfNewRCards.setIsSkippable(parentListCard.getIsSkippable());
								
								listOfNewRCards.setStateId(parentListCard.getStateId());
								listOfNewRCards.setState(null!=listofiOSActivitiesforCatagories.get(i).get("status")?listofiOSActivitiesforCatagories.get(i).get("status").toString():"");
								listOfNewRCards.setTypeId(parentListCard.getTypeId());
								listOfNewRCards.setType(parentListCard.getType());
								listOfNewRCards.setDependentCard(parentListCard.getDependentCard());
								listOfNewRCards.setDependOnCard(parentListCard.getDependOnCard());
								listOfNewRCards.setDisplayType(parentListCard.getDisplayType());
								strValue = getRequiredBoolean((Long) listofiOSActivitiesforCatagories.get(i).get("userCreated"));
								listOfNewRCards.setUserCreated(strValue);
								//listOfNewRCards.setReason(parentListCard.getReason()); commented for new reason mapping from iOS
								listOfNewRCards.setReason(null!=listofiOSActivitiesforCatagories.get(i).get("reason")?listofiOSActivitiesforCatagories.get(i).get("reason").toString():"");
								listOfNewRCards.setInfoCard(null!=listofiOSActivitiesforCatagories.get(i).get("infoCard")?listofiOSActivitiesforCatagories.get(i).get("infoCard").toString():"");
								listOfNewRCards.setTypePossibleSections(parentListCard.getTypePossibleSections());
								listOfNewRCards.setIsModified("1");		
								
								//associatedItemsId pending
								
								//Associated Sections - Starts
								List<JSONObject> listofAssociatedSectionforActivities = (List<JSONObject>)listofiOSActivitiesforCatagories.get(i).get("associatedSections");
								ListSections[] listNewSections = null;
								listNewSections = setNewAssociatedSelections(listofAssociatedSectionforActivities, listNewSections, newCardID, parentListCard);
								//Associated Sections - End
								listOfNewRCards.setListSections(listNewSections);
								
								for (int listSecInt = 0; listSecInt < listNewSections.length; listSecInt++) {
									ListQuestions[] listQuestionType = listNewSections[listSecInt].getListQuestions();
									for (int listQuestInt = 0; listQuestInt < listQuestionType.length; listQuestInt++) {
										if(listQuestionType[listQuestInt].getScopeType()!= null && listQuestionType[listQuestInt].getScopeType().equalsIgnoreCase("1") && listQuestionType[listQuestInt].getScopeTypeValue() != null){
											listOfNewRCards.setScope(listQuestionType[listQuestInt].getScopeTypeValue());
										}else if(listQuestionType[listQuestInt].getResultType()!=null && listQuestionType[listQuestInt].getResultType().equalsIgnoreCase("1") && listQuestionType[listQuestInt].getResultTypeValue() != null){
											listOfNewRCards.setState(listQuestionType[listQuestInt].getResultTypeValue());
											listOfNewRCards.setStateId(null);
										}
									}
								}	
								
								
								//Section Template - Starts
								JSONObject sectionTemplagteforActivities = (JSONObject)listofiOSActivitiesforCatagories.get(i).get("sectionTemplate");
								List<JSONObject> listofAssociatedQuestionsforSectionTemplate = (List<JSONObject>) sectionTemplagteforActivities.get("associatedQuestions");
								SectionTemplate[] listQuestionsInSORSectionTemplate = null;
								if(listofAssociatedQuestionsforSectionTemplate != null){
									listQuestionsInSORSectionTemplate = getNewSelectedTempalateAssociatedQuestions(listofAssociatedQuestionsforSectionTemplate,listQuestionsInSORSectionTemplate,newCardID);
									
								}else{
									listQuestionsInSORSectionTemplate = new SectionTemplate[]{};
								}
								//Section Template  - Ends
								listOfNewRCards.setSectionTemplate(listQuestionsInSORSectionTemplate);
								
								
								List<JSONObject> listofPossibleSections = (List<JSONObject>)listofiOSActivitiesforCatagories.get(i).get("possibleSections");
								PossibleSections possibleSections =new PossibleSections();
								PossibleSections[] possibleSectionsArray =null;
								
								if(listofPossibleSections!=null){
									for (int k = 0; k < listofPossibleSections.size(); k++) {
										possibleSections =new PossibleSections();
										possibleSections.setInternalId(null);
										possibleSections.setDisplayLabel((null!=listofPossibleSections.get(k).get("displayLabel")?listofPossibleSections.get(k).get("displayLabel").toString():""));
										strValue = getRequiredBoolean((Long) listofiOSActivitiesforCatagories.get(i).get("required"));
										possibleSections.setIsSelected((null!=listofPossibleSections.get(k).get("required")?listofPossibleSections.get(k).get("required").toString():""));
										possibleSections.setIsRequired(strValue);
										possibleSections.setDisplayOrder(k+"");
										possibleSectionsArray = (PossibleSections[]) ArrayUtils.add(possibleSectionsArray, possibleSections);
									}
								}else{
										possibleSectionsArray = new PossibleSections[]{};
								}
									
								listOfNewRCards.setPossibleSections(possibleSectionsArray);
								
								listOfSORCards = (ListCards[]) ArrayUtils.add(listOfSORCards, listOfNewRCards); 
								
							}else if(iosActivityID.equalsIgnoreCase(sorActivityExtID) && sorActivityExtID !=null){
								
								
								listOfSORCards[j].setAdditionalInformation((null!=listofiOSActivitiesforCatagories.get(i).get("additionalInformation")?listofiOSActivitiesforCatagories.get(i).get("additionalInformation").toString():""));
								listOfSORCards[j].setDisplayName(null!=listofiOSActivitiesforCatagories.get(i).get("displayName")?listofiOSActivitiesforCatagories.get(i).get("displayName").toString():"");
								strValue = getRequiredBoolean((Long) listofiOSActivitiesforCatagories.get(i).get("required"));
								listOfSORCards[j].setIsRequired(strValue);
								strValue = getRequiredBoolean((Long) listofiOSActivitiesforCatagories.get(i).get("duplicable"));
								listOfSORCards[j].setIsDuplicable(strValue);
								listOfSORCards[j].setDisplayType(null!=listofiOSActivitiesforCatagories.get(i).get("displayType")?listofiOSActivitiesforCatagories.get(i).get("displayType").toString():"");
								listOfSORCards[j].setReason(null!=listofiOSActivitiesforCatagories.get(i).get("reason")?listofiOSActivitiesforCatagories.get(i).get("reason").toString():"");
								//added for new reason mapping from iOS
								listOfSORCards[j].setState(null!=listofiOSActivitiesforCatagories.get(i).get("status")?listofiOSActivitiesforCatagories.get(i).get("status").toString():"");
								strValue = getRequiredBoolean((Long) listofiOSActivitiesforCatagories.get(i).get("userCreated"));
								listOfSORCards[j].setUserCreated(strValue);
								listOfSORCards[j].setIsModified("1");
								logger.severe("iosParentID is null to SoR");
								listOfSORCards[j].setParentActivityId(null); // added for the new field - parentActivityId
								
								//Associated items id pending
								SectionTemplate[] listQuestionsInSORSectionTemplate = listOfSORCards[j].getSectionTemplate();
								
								//Associated Sections - Starts
								List<JSONObject> listofAssociatedSectionforActivities = (List<JSONObject>)listofiOSActivitiesforCatagories.get(i).get("associatedSections");
								ListSections[] listSections = listOfSORCards[j].getListSections();
								listSections = setAssociatedSelections(listofAssociatedSectionforActivities, listSections, listQuestionsInSORSectionTemplate,listOfSORCards[j]);
								//Associated Sections - End
																	
								listOfSORCards[j].setListSections(listSections);
								
								for (int lstNSInt = 0; lstNSInt < listSections.length; lstNSInt++) {
									if(listSections[lstNSInt].getNotesNewSectionIdentity()==1){
										listOfSORCards[j].setNotesIdentityInCard(1);
									}
								}
								
								
								for (int listSecInt = 0; listSecInt < listSections.length; listSecInt++) {
									ListQuestions[] listQuestionType = listSections[listSecInt].getListQuestions();
									for (int listQuestInt = 0; listQuestInt < listQuestionType.length; listQuestInt++) {
										if(listQuestionType[listQuestInt].getScopeType()!= null && listQuestionType[listQuestInt].getScopeType().equalsIgnoreCase("1") && listQuestionType[listQuestInt].getScopeTypeValue() != null){
											listOfSORCards[j].setScope(listQuestionType[listQuestInt].getScopeTypeValue());
										}else if(listQuestionType[listQuestInt].getResultType()!=null && listQuestionType[listQuestInt].getResultType().equalsIgnoreCase("1") && listQuestionType[listQuestInt].getResultTypeValue() != null){
											listOfSORCards[j].setState(listQuestionType[listQuestInt].getResultTypeValue());
											listOfSORCards[j].setStateId(null);
										}
									}
								}	
													
								//associated templates
							}else {
								//throw new Exception();
							}
						}// If loop for isModified
					}
				} // Activities loop end
			}
		}catch(Exception e){
			printException(e,"PostVisitforUser - setActivitiesforCatagories");
		}

		//Remove the isMOdified == 0 in card level
//		int deleteListOfSORCardsInt = listOfSORCards.length;
//		ListCards[] listCardsFinal = new ListCards[]{};
//
//		for (int listCardsDelete = 0; listCardsDelete < deleteListOfSORCardsInt ; listCardsDelete++) {
//			if(listOfSORCards[listCardsDelete].getIsModified().equals("1"))
//				listCardsFinal = (ListCards[]) ArrayUtils.add(listCardsFinal, listOfSORCards[listCardsDelete]);
//		}

//		return listCardsFinal;
				return listOfSORCards;

	}
	
	
	@SuppressWarnings("unchecked")
	public ListSections[] setNewAssociatedSelections(List<JSONObject> listofAssociatedSectionforActivities, ListSections[] listNewSections, String newCardID, ListCards parentListCard) {
		logger.severe("setNewAssociatedSelections");
		String newSectionID = new String();
		
		ListSections parentListSections = new ListSections();
		try{
			for (int k = 0; k < listofAssociatedSectionforActivities.size(); k++) {
				ListSections listOfNewSection = new ListSections();
				parentListSections = parentListCard.getListSections()[k];
				listOfNewSection.setInternalId(null);
				listOfNewSection.setDisplayName(null!=listofAssociatedSectionforActivities.get(k).get("displayName")?listofAssociatedSectionforActivities.get(k).get("displayName").toString():"");
				listOfNewSection.setDisplayOrder(null!=listofAssociatedSectionforActivities.get(k).get("displayOrder")?listofAssociatedSectionforActivities.get(k).get("displayOrder").toString():"");
				newSectionID = newCardID +"-Section"+(listOfNewSection.getDisplayOrder());
				listOfNewSection.setSectionId(newSectionID); 
				
				//Associated Questions - starts
				List<JSONObject> listofAssociatedQuestionsforAssociatedSections = (List<JSONObject>)listofAssociatedSectionforActivities.get(k).get("associatedQuestions");
				ListQuestions[] listNewQuestions = null;
				listNewQuestions = setNewAssociatedQuestions(listofAssociatedQuestionsforAssociatedSections, listNewQuestions, newSectionID,parentListSections);
				listOfNewSection.setListQuestions(listNewQuestions);
				
				listNewSections = (ListSections[]) ArrayUtils.add(listNewSections, listOfNewSection); 
			}
		}catch(Exception e){
			printException(e,"PostVisitforUser -setNewAssociatedSelections");
		}
		return listNewSections;
	}
	
	
	@SuppressWarnings("unchecked")
	public ListSections setNewAssociatedSelectionsNewSection(List<JSONObject> listofAssociatedSectionforActivities, ListSections[] listNewSections, String newCardID, SectionTemplate[] listQuestionsInSORSectionTemplate, int secCount, ListCards listOfSORCards){
		logger.severe("setNewAssociatedSelectionsNewSection");
		String newSectionID = new String();
		ListSections listOfNewSection = new ListSections();
		ListSections parentListSections = new ListSections();
		try{
				
				listOfNewSection.setInternalId(null);
				listOfNewSection.setDisplayName(null!=listofAssociatedSectionforActivities.get(secCount).get("displayName")?listofAssociatedSectionforActivities.get(secCount).get("displayName").toString():"");
				listOfNewSection.setDisplayOrder(null!=listofAssociatedSectionforActivities.get(secCount).get("displayOrder")?listofAssociatedSectionforActivities.get(secCount).get("displayOrder").toString():"");
				newSectionID = newCardID +"-Section"+(listOfNewSection.getDisplayOrder());
				listOfNewSection.setSectionId(newSectionID); 
				
				//Associated Questions - starts
				List<JSONObject> listofAssociatedQuestionsforAssociatedSections = (List<JSONObject>)listofAssociatedSectionforActivities.get(secCount).get("associatedQuestions");
				ListQuestions[] listNewQuestions = null;
				listNewQuestions = setNewAssociatedQuestionsNewSections(listofAssociatedQuestionsforAssociatedSections, listNewQuestions, newSectionID,listQuestionsInSORSectionTemplate,listOfSORCards);
				listOfNewSection.setListQuestions(listNewQuestions);
				
		}catch(Exception e){
			printException(e,"PostVisitforUser -setNewAssociatedSelectionsNewSection");
		}
		return listOfNewSection;
	}
	
	
	
	
	

	@SuppressWarnings("unchecked")
	public ListSections[] setAssociatedSelections(List<JSONObject> listofAssociatedSectionforActivities, ListSections[] listSections, SectionTemplate[] listQuestionsInSORSectionTemplate, ListCards listOfSORCards){
		logger.severe("setAssociatedSelections");
		int nullLoop=0;
		ListSections listnNewSection = new ListSections();
		int listSectionsLength =listSections.length;
		String cardID = listOfSORCards.getCardId(); 
		if(listofAssociatedSectionforActivities!=null){
			for (int k = 0; k < listofAssociatedSectionforActivities.size(); k++) {
				nullLoop = 0;
				String iosSectionID = (String) listofAssociatedSectionforActivities.get(k).get("sectionId");
				Long displayOrder = (Long) listofAssociatedSectionforActivities.get(k).get("displayOrder");
				
				
				if(listSectionsLength == 0 && iosSectionID.startsWith("AppGenerated-Section")){
					ListSections[] listNewSections = null;
					listnNewSection = new ListSections();
					try {
						listnNewSection = setNewAssociatedSelectionsNewSection(listofAssociatedSectionforActivities, listNewSections, cardID, listQuestionsInSORSectionTemplate, displayOrder.intValue(),listOfSORCards);
						listnNewSection.setRemoveSection(1);
						listnNewSection.setNotesNewSectionIdentity(1);
						listSections = (ListSections[]) ArrayUtils.add(listSections, listnNewSection);
					} catch (Exception e) {
						printException(e,"PostVisitforUser -setAssociatedSelections");
					}
				}else{
					for (int k2 = 0; k2 < listSections.length; k2++) {
						String sorSectionExtID = listSections[k2].getExternalId();
						
						if(iosSectionID.equalsIgnoreCase(sorSectionExtID)){
							listSections[k2].setDisplayName(null!=listofAssociatedSectionforActivities.get(k).get("displayName")?listofAssociatedSectionforActivities.get(k).get("displayName").toString():"");
							listSections[k2].setDisplayOrder(null!=listofAssociatedSectionforActivities.get(k).get("displayOrder")?listofAssociatedSectionforActivities.get(k).get("displayOrder").toString():"");
							
							//Associated Questions - starts
							List<JSONObject> listofAssociatedQuestionsforAssociatedSections = (List<JSONObject>)listofAssociatedSectionforActivities.get(k).get("associatedQuestions");
							ListQuestions[] listQuestions = listSections[k2].getListQuestions();
							listQuestions = getAssociatedQuestions(listofAssociatedQuestionsforAssociatedSections, listQuestions,listQuestionsInSORSectionTemplate,listSections[k2].getSectionId());
							listSections[k2].setListQuestions(listQuestions);
							listSections[k2].setRemoveSection(1);
							
							//section template
						}else if(iosSectionID.startsWith("AppGenerated-Section") && nullLoop == 0){
							nullLoop = nullLoop + 1;
							ListSections[] listNewSections = null;
							listnNewSection = new ListSections();
							try {
								listnNewSection = setNewAssociatedSelectionsNewSection(listofAssociatedSectionforActivities, listNewSections, cardID, listQuestionsInSORSectionTemplate, k,listOfSORCards);
								listnNewSection.setRemoveSection(1);
								listnNewSection.setNotesNewSectionIdentity(1);
							} catch (Exception e) {
								printException(e,"PostVisitforUser -setAssociatedSelections - Else Part");
							}
							listSections = (ListSections[]) ArrayUtils.add(listSections, listnNewSection);
						}
					}
				}
			}
		}

		//Associated Sections - End
		int deleteSectionLenght = listSections.length;
		ListSections[] listSectionsFinal = new ListSections[]{};
		
		for (int listSelectionsDelete = 0; listSelectionsDelete < deleteSectionLenght ; listSelectionsDelete++) {
			if(listSections[listSelectionsDelete].getRemoveSection() == 1)
				listSectionsFinal = (ListSections[]) ArrayUtils.add(listSectionsFinal, listSections[listSelectionsDelete]);
		}
		return listSectionsFinal;
	}
	
	@SuppressWarnings("null")
	public ListQuestions[] getAssociatedQuestions(List<JSONObject> listofAssociatedQuestionsforAssociatedSections,ListQuestions[] listQuestions,SectionTemplate[] listQuestionsInSORSectionTemplate, String sectionID) {
		logger.severe("getAssociatedQuestions");
		//Associated Questions - starts
		
		int listQuestionsLength = listQuestions.length;
		if(listofAssociatedQuestionsforAssociatedSections!=null){
			for (int l = 0; l < listofAssociatedQuestionsforAssociatedSections.size(); l++) {
				for (int l2 = 0; l2 < listQuestions.length; l2++) {
					String iosQuestionID = (String)listofAssociatedQuestionsforAssociatedSections.get(l).get("questionId");
					String sorQuestionExtID = listQuestions[l2].getExternalId();
					
					
					if(iosQuestionID.equalsIgnoreCase(sorQuestionExtID)){
						
						String strValue = "";
						strValue = getRequiredBoolean((Long) listofAssociatedQuestionsforAssociatedSections.get(l).get("required"));
						listQuestions[l2].setIsRequired(strValue);
						listQuestions[l2].setType(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("type")?listofAssociatedQuestionsforAssociatedSections.get(l).get("type").toString():"");
						listQuestions[l2].setUnits(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("units")?listofAssociatedQuestionsforAssociatedSections.get(l).get("units").toString():"");
						listQuestions[l2].setDependent(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("dependent")?listofAssociatedQuestionsforAssociatedSections.get(l).get("dependent").toString():"");
						listQuestions[l2].setDependOn(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("dependOn")?listofAssociatedQuestionsforAssociatedSections.get(l).get("dependOn").toString():"");
						listQuestions[l2].setCondition(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("condition")?listofAssociatedQuestionsforAssociatedSections.get(l).get("condition").toString():"");
						listQuestions[l2].setAdditionalInformation(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("additionalInformation")?listofAssociatedQuestionsforAssociatedSections.get(l).get("additionalInformation").toString():"");
						listQuestions[l2].setGhostValue(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue")?listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue").toString():"");
						listQuestions[l2].setDisplayLabel(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("displayLabel")?listofAssociatedQuestionsforAssociatedSections.get(l).get("displayLabel").toString():"");
						String iosAnswerStringArray = (String) listofAssociatedQuestionsforAssociatedSections.get(l).get("answer");
						Answer answerSORObject = new Answer();
						if(iosAnswerStringArray!=null && !iosAnswerStringArray.isEmpty()){
							String[] strArray = iosAnswerStringArray.split(",");
							answerSORObject = listQuestions[l2].getAnswer();
							if(answerSORObject!=null){
								answerSORObject.setValue(strArray);
							}
							listQuestions[l2].setAnswer(answerSORObject);
						}else{
							answerSORObject = listQuestions[l2].getAnswer();
							if(answerSORObject!=null){
								answerSORObject.setValue(new String[0]);
							}
							listQuestions[l2].setAnswer(answerSORObject);
						}
						
						if(listofAssociatedQuestionsforAssociatedSections.get(l).get("type") != null){
							List<JSONObject> listPossibleAnswers =  (List<JSONObject>) listofAssociatedQuestionsforAssociatedSections.get(l).get("possibleAnswers");
							if(listofAssociatedQuestionsforAssociatedSections.get(l).get("type").equals("scope") && listPossibleAnswers!=null){
								for (int listPAScope = 0; listPAScope < listPossibleAnswers.size(); listPAScope++) {
									if(listPossibleAnswers.get(listPAScope).get("value") != null && ((listPossibleAnswers.get(listPAScope).get("value").equals(listofAssociatedQuestionsforAssociatedSections.get(l).get("answer"))))){
										listQuestions[l2].setScopeType("1");
										listQuestions[l2].setScopeTypeValue((String) listPossibleAnswers.get(listPAScope).get("displayLabel"));
									}
								}
							}else if(listofAssociatedQuestionsforAssociatedSections.get(l).get("type").equals("results") && listPossibleAnswers!=null){
								for (int listPARes = 0; listPARes < listPossibleAnswers.size(); listPARes++) {
									if(listPossibleAnswers.get(listPARes).get("value") != null && ((listPossibleAnswers.get(listPARes).get("value").equals(listofAssociatedQuestionsforAssociatedSections.get(l).get("answer"))))){
										listQuestions[l2].setResultType("1");
										listQuestions[l2].setResultTypeValue((String) listPossibleAnswers.get(listPARes).get("displayLabel"));
									}
								}
							}
						}
						
					}else if(iosQuestionID.equalsIgnoreCase("AppGenerated-Question")){
						if(listQuestionsInSORSectionTemplate!=null){
							for (int listSTS = 0; listSTS < listQuestionsInSORSectionTemplate.length; listSTS++) {
								
								if(listQuestions[l2].getDisplayLabel().equalsIgnoreCase(listQuestionsInSORSectionTemplate[listSTS].getDisplayLabel())){
									listQuestions[l2].setInternalId(null!=listQuestionsInSORSectionTemplate[listSTS].getInternalId()?listQuestionsInSORSectionTemplate[listSTS].getInternalId().toString():"");
									listQuestions[l2].setInternalIdCFG(null!=listQuestionsInSORSectionTemplate[listSTS].getInternalIdCFG()?listQuestionsInSORSectionTemplate[listSTS].getInternalIdCFG().toString():"");
									listQuestions[l2].setQuestionId(sectionID+"-"+listQuestionsLength);
									listQuestions[l2].setDefaultValue("");
									listQuestions[l2].setAdditionalInformation(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("additionalInformation")?listofAssociatedQuestionsforAssociatedSections.get(l).get("additionalInformation").toString():"");
									listQuestions[l2].setDisplayLabel(null!=listQuestionsInSORSectionTemplate[listSTS].getDisplayLabel()?listQuestionsInSORSectionTemplate[listSTS].getDisplayLabel().toString():"");
									listQuestions[l2].setDisplayOrder(null!=listQuestionsInSORSectionTemplate[listSTS].getDisplayOrder()?listQuestionsInSORSectionTemplate[listSTS].getDisplayOrder().toString():"");
									listQuestions[l2].setGhostValue(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue")?listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue").toString():"");
									listQuestions[l2].setIsRequired(null!=listQuestionsInSORSectionTemplate[listSTS].getIsRequired()?listQuestionsInSORSectionTemplate[listSTS].getIsRequired().toString():"");
									listQuestions[l2].setIsEditable(null!=listQuestionsInSORSectionTemplate[listSTS].getIsEditable()?listQuestionsInSORSectionTemplate[listSTS].getIsEditable().toString():"");
									listQuestions[l2].setIsVisible(null!=listQuestionsInSORSectionTemplate[listSTS].getIsVisible()?listQuestionsInSORSectionTemplate[listSTS].getIsVisible().toString():"");
									listQuestions[l2].setTypeId(null!=listQuestionsInSORSectionTemplate[listSTS].getTypeId()?listQuestionsInSORSectionTemplate[listSTS].getTypeId().toString():"");
									listQuestions[l2].setType(null!=listQuestionsInSORSectionTemplate[listSTS].getType()?listQuestionsInSORSectionTemplate[listSTS].getType().toString():"");
									listQuestions[l2].setUnits(null!=listQuestionsInSORSectionTemplate[listSTS].getUnits()?listQuestionsInSORSectionTemplate[listSTS].getUnits().toString():"");
									
									String iosAnswerStringArray = (String) listofAssociatedQuestionsforAssociatedSections.get(l).get("answer");
									
									if(iosAnswerStringArray!=null){
										String[] strArray = iosAnswerStringArray.split(",");
										Answer answerSORObject = listQuestions[l2].getAnswer();
										if(answerSORObject!=null){
											answerSORObject.setType("");
											answerSORObject.setTypeId("");
											answerSORObject.setValue(strArray);
										}else{
											answerSORObject.setValue(null);
										}
										listQuestions[l2].setAnswer(answerSORObject);
									}
								}
							}
						}
					}
				}
			}
		}
		return listQuestions;
		//Associated Questions - End
	}
	
	
	@SuppressWarnings("unchecked")
	public ListQuestions[] setNewAssociatedQuestions(List<JSONObject> listofAssociatedQuestionsforAssociatedSections,ListQuestions[] listQuestions, String newSectionID,ListSections parentListSections) {
		logger.severe("setNewAssociatedQuestions");
		//Associated Questions - starts
		ListQuestions listNewQuestions = new ListQuestions();
		String newQuestionID = new String();
		ListQuestions parentListQuestions = new ListQuestions();
		ListQuestions[] listQuestionsforDisplayOrder = parentListSections.getListQuestions();
		String strValue = new String();
		try{
			for (int l = 0; l < listofAssociatedQuestionsforAssociatedSections.size(); l++) {
				listNewQuestions = new ListQuestions();
				for (int  displayOrderInt= 0; displayOrderInt < listQuestionsforDisplayOrder.length; displayOrderInt++) {
					if(listofAssociatedQuestionsforAssociatedSections.get(l).get("displayOrder").toString().equalsIgnoreCase(parentListSections.getListQuestions()[displayOrderInt].getDisplayOrder())){
						parentListQuestions = parentListSections.getListQuestions()[displayOrderInt];
					}
				}
				newQuestionID = newSectionID +"-Question"+l;
				listNewQuestions.setQuestionId(null!=newQuestionID?newQuestionID:"");
				listNewQuestions.setGhostValue(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue")?listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue").toString():"");
				listNewQuestions.setType(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("type")?listofAssociatedQuestionsforAssociatedSections.get(l).get("type").toString():"");
				listNewQuestions.setUnits(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("units")?listofAssociatedQuestionsforAssociatedSections.get(l).get("units").toString():"");
				listNewQuestions.setDependent(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("dependent")?listofAssociatedQuestionsforAssociatedSections.get(l).get("dependent").toString():"");
				listNewQuestions.setDependOn(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("dependOn")?listofAssociatedQuestionsforAssociatedSections.get(l).get("dependOn").toString():"");
				listNewQuestions.setCondition(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("condition")?listofAssociatedQuestionsforAssociatedSections.get(l).get("condition").toString():"");
				listNewQuestions.setAdditionalInformation(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("additionalInformation")?listofAssociatedQuestionsforAssociatedSections.get(l).get("additionalInformation").toString():"");
				listNewQuestions.setDisplayOrder(parentListQuestions.getDisplayOrder());
				listNewQuestions.setGhostValue(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue")?listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue").toString():"");
				listNewQuestions.setDisplayLabel(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("displayLabel")?listofAssociatedQuestionsforAssociatedSections.get(l).get("displayLabel").toString():"");
				if(parentListQuestions!=null){
					listNewQuestions.setInternalIdCFG(parentListQuestions.getInternalIdCFG());
					listNewQuestions.setInternalId(null);
				}else{
					listNewQuestions.setInternalIdCFG(null);
					listNewQuestions.setInternalId(null);
				}
				
				strValue = getRequiredBoolean((Long) listofAssociatedQuestionsforAssociatedSections.get(l).get("required"));
				
				listNewQuestions.setIsRequired(strValue);
				listNewQuestions.setIsEditable(parentListQuestions.getIsEditable());
				listNewQuestions.setIsVisible(parentListQuestions.getIsVisible());
				String iosAnswerStringArray = (String) listofAssociatedQuestionsforAssociatedSections.get(l).get("answer");
				Answer answerSORObject =  new Answer();
				if(iosAnswerStringArray!=null && !iosAnswerStringArray.isEmpty()){
					String[] strArray = iosAnswerStringArray.split(",");
						answerSORObject.setValue(strArray);
						answerSORObject.setType(parentListQuestions.getAnswer().getType());
						answerSORObject.setTypeId(parentListQuestions.getAnswer().getTypeId());
				}else{
					answerSORObject.setValue(new String[0]);
					answerSORObject.setType(parentListQuestions.getAnswer().getType());
					answerSORObject.setTypeId(parentListQuestions.getAnswer().getTypeId());
				}
				listNewQuestions.setAnswer(answerSORObject);
				
				if(listofAssociatedQuestionsforAssociatedSections.get(l).get("type") != null){
					List<JSONObject> listPossibleAnswers =  (List<JSONObject>) listofAssociatedQuestionsforAssociatedSections.get(l).get("possibleAnswers");
					if(listofAssociatedQuestionsforAssociatedSections.get(l).get("type").equals("scope") && listPossibleAnswers!=null){
						for (int listPAScope = 0; listPAScope < listPossibleAnswers.size(); listPAScope++) {
							if(listPossibleAnswers.get(listPAScope).get("value") != null && ((listPossibleAnswers.get(listPAScope).get("value").equals(listofAssociatedQuestionsforAssociatedSections.get(l).get("answer"))))){
								listNewQuestions.setScopeType("1");
								listNewQuestions.setScopeTypeValue((String) listPossibleAnswers.get(listPAScope).get("displayLabel"));
							}
						}
					}else if(listofAssociatedQuestionsforAssociatedSections.get(l).get("type").equals("results") && listPossibleAnswers!=null){
						for (int listPARes = 0; listPARes < listPossibleAnswers.size(); listPARes++) {
							if(listPossibleAnswers.get(listPARes).get("value") != null && ((listPossibleAnswers.get(listPARes).get("value").equals(listofAssociatedQuestionsforAssociatedSections.get(l).get("answer"))))){
								listNewQuestions.setResultType("1");
								listNewQuestions.setResultTypeValue((String) listPossibleAnswers.get(listPARes).get("displayLabel"));
							}
						}
					}
				}
				
				//possible Answers
				List<JSONObject> possibleAnswersJSON = (List<JSONObject>)listofAssociatedQuestionsforAssociatedSections.get(l).get("possibleAnswers");
				ListPossibleAnswers listPossibleAnswer = new ListPossibleAnswers();
				ListPossibleAnswers[] listPossibleAnswers=new ListPossibleAnswers[]{};
				
				JSONObject possibleAnswer = new JSONObject();
				if(possibleAnswersJSON!=null){
					for (int i = 0; i < possibleAnswersJSON.size(); i++) {
						possibleAnswer = possibleAnswersJSON.get(i);
						listPossibleAnswer = new ListPossibleAnswers();
						listPossibleAnswer.setDisplayLabel(null!=possibleAnswer.get("displayLabel")?possibleAnswer.get("displayLabel").toString():"");
						listPossibleAnswer.setDisplayOrder(null!=possibleAnswer.get("displayOrder")?possibleAnswer.get("displayOrder").toString():"");
						listPossibleAnswer.setValue(null!=possibleAnswer.get("value")?possibleAnswer.get("value").toString():"");
						strValue = getRequiredBoolean((Long) possibleAnswer.get("isRequired"));
						listPossibleAnswer.setIsRequired(strValue);
						listPossibleAnswers = (ListPossibleAnswers[]) ArrayUtils.add(listPossibleAnswers, listPossibleAnswer);
					}
				}else{
					listPossibleAnswers= new ListPossibleAnswers[]{};
				}
				listNewQuestions.setListPossibleAnswers(listPossibleAnswers);
				listQuestions = (ListQuestions[]) ArrayUtils.add(listQuestions, listNewQuestions); 
		}	
		}catch(Exception e){
			printException(e,"PostVisitforUser -setNewAssociatedQuestions");
		}
		return listQuestions;
	}
	

	@SuppressWarnings({ "unused", "unchecked" })
	public ListQuestions[] setNewAssociatedQuestionsNewSections(List<JSONObject> listofAssociatedQuestionsforAssociatedSections,ListQuestions[] listQuestions, String newSectionID,SectionTemplate[] listQuestionsInSORSectionTemplate, ListCards listOfSORCards){
		logger.severe("setNewAssociatedQuestionsNewSections");
		//Associated Questions - starts
		ListQuestions listNewQuestions = new ListQuestions();
		String newQuestionID = new String();
		SectionTemplate parentListQuestions = new SectionTemplate();
		int listQuestionsforDisplayOrder = listQuestionsInSORSectionTemplate.length;
		
		String strValue = new String();
		String oriValue = new String();
		String strDisplayArray = new String();
		try{
			for (int l = 0; l < listofAssociatedQuestionsforAssociatedSections.size(); l++) {
						
				listNewQuestions = new ListQuestions();
				for (int  displayOrderInt= 0; displayOrderInt < listQuestionsforDisplayOrder; displayOrderInt++) {
					if(listofAssociatedQuestionsforAssociatedSections.get(l).get("displayOrder").toString().equalsIgnoreCase(listQuestionsInSORSectionTemplate[displayOrderInt].getDisplayOrder())){
						parentListQuestions = listQuestionsInSORSectionTemplate[displayOrderInt];
					}
				}
				
				newQuestionID = newSectionID +"-Question"+l;
				listNewQuestions.setQuestionId(null!=newQuestionID?newQuestionID:"");
				listNewQuestions.setGhostValue(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue")?listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue").toString():"");
				listNewQuestions.setType(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("type")?listofAssociatedQuestionsforAssociatedSections.get(l).get("type").toString():"");
				listNewQuestions.setUnits(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("units")?listofAssociatedQuestionsforAssociatedSections.get(l).get("units").toString():"");
				listNewQuestions.setDependent(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("dependent")?listofAssociatedQuestionsforAssociatedSections.get(l).get("dependent").toString():"");
				listNewQuestions.setDependOn(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("dependOn")?listofAssociatedQuestionsforAssociatedSections.get(l).get("dependOn").toString():"");
				listNewQuestions.setCondition(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("condition")?listofAssociatedQuestionsforAssociatedSections.get(l).get("condition").toString():"");
				listNewQuestions.setAdditionalInformation(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("additionalInformation")?listofAssociatedQuestionsforAssociatedSections.get(l).get("additionalInformation").toString():"");
				listNewQuestions.setDisplayOrder(parentListQuestions.getDisplayOrder());
				listNewQuestions.setGhostValue(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue")?listofAssociatedQuestionsforAssociatedSections.get(l).get("ghostValue").toString():"");
				listNewQuestions.setDisplayLabel(null!=listofAssociatedQuestionsforAssociatedSections.get(l).get("displayLabel")?listofAssociatedQuestionsforAssociatedSections.get(l).get("displayLabel").toString():"");
				if(parentListQuestions!=null){
					listNewQuestions.setInternalIdCFG(parentListQuestions.getInternalIdCFG());
					listNewQuestions.setInternalId(null);
				}else{
					listNewQuestions.setInternalIdCFG(null);
					listNewQuestions.setInternalId(null);
				}
				
				strValue = getRequiredBoolean((Long) listofAssociatedQuestionsforAssociatedSections.get(l).get("required"));
				
				listNewQuestions.setIsRequired(strValue);
				listNewQuestions.setIsEditable(parentListQuestions.getIsEditable());
				listNewQuestions.setIsVisible(parentListQuestions.getIsVisible());
				
			
				
				String iosAnswerStringArray = (String) listofAssociatedQuestionsforAssociatedSections.get(l).get("answer");
				Answer answerSORObject =  new Answer();
				if(iosAnswerStringArray!=null){
					String[] strArray = iosAnswerStringArray.split(",");

					if(listOfSORCards.getIsTable().equalsIgnoreCase("true") && listNewQuestions.getType().equalsIgnoreCase("informational") && listOfSORCards.getPossibleSections() != null){
						PossibleSections[] possibleSections = (PossibleSections[])listOfSORCards.getPossibleSections();
						PossibleSections possibleSection = new PossibleSections();
						for (int possibleSectionsInt = 0; possibleSectionsInt < possibleSections.length; possibleSectionsInt++) {
							possibleSection = possibleSections[possibleSectionsInt];
							oriValue = getOrginValue(possibleSection.getDisplayLabel()).trim();
							for (int strArrayInt = 0; strArrayInt < strArray.length; strArrayInt++) {
								strDisplayArray = strArray[strArrayInt].toString();
								if(oriValue.equalsIgnoreCase(strDisplayArray)){
									strArray[strArrayInt] = possibleSection.getInternalId();
								}
							}
						}
					}
					
					answerSORObject.setValue(strArray);
					answerSORObject.setType(parentListQuestions.getAnswer().getType());
					answerSORObject.setTypeId(parentListQuestions.getAnswer().getTypeId());
				}else{
					answerSORObject.setValue(new String[0]);
					answerSORObject.setType(parentListQuestions.getAnswer().getType());
					answerSORObject.setTypeId(parentListQuestions.getAnswer().getTypeId());
				}
				listNewQuestions.setAnswer(answerSORObject);
				
				if(listofAssociatedQuestionsforAssociatedSections.get(l).get("type") != null){
					List<JSONObject> listPossibleAnswers =  (List<JSONObject>) listofAssociatedQuestionsforAssociatedSections.get(l).get("possibleAnswers");
					if(listofAssociatedQuestionsforAssociatedSections.get(l).get("type").equals("scope") && listPossibleAnswers!=null){
						for (int listPAScope = 0; listPAScope < listPossibleAnswers.size(); listPAScope++) {
							if(listPossibleAnswers.get(listPAScope).get("value") != null && ((listPossibleAnswers.get(listPAScope).get("value").equals(listofAssociatedQuestionsforAssociatedSections.get(l).get("answer"))))){
								listNewQuestions.setScopeType("1");
								listNewQuestions.setScopeTypeValue((String) listPossibleAnswers.get(listPAScope).get("displayLabel"));
							}
						}
					}else if(listofAssociatedQuestionsforAssociatedSections.get(l).get("type").equals("results") && listPossibleAnswers!=null){
						for (int listPARes = 0; listPARes < listPossibleAnswers.size(); listPARes++) {
							if(listPossibleAnswers.get(listPARes).get("value") != null && ((listPossibleAnswers.get(listPARes).get("value").equals(listofAssociatedQuestionsforAssociatedSections.get(l).get("answer"))))){
								listNewQuestions.setResultType("1");
								listNewQuestions.setResultTypeValue((String) listPossibleAnswers.get(listPARes).get("displayLabel"));
							}
						}
					}
				}
				
				//possible Answers
				List<JSONObject> possibleAnswersJSON = (List<JSONObject>)listofAssociatedQuestionsforAssociatedSections.get(l).get("possibleAnswers");
				ListPossibleAnswers listPossibleAnswer = new ListPossibleAnswers();
				ListPossibleAnswers[] listPossibleAnswers=new ListPossibleAnswers[]{};
				
				JSONObject possibleAnswer = new JSONObject();
				if(possibleAnswersJSON!=null){
					for (int i = 0; i < possibleAnswersJSON.size(); i++) {
						possibleAnswer = possibleAnswersJSON.get(i);
						listPossibleAnswer = new ListPossibleAnswers();
						listPossibleAnswer.setDisplayLabel(null!=possibleAnswer.get("displayLabel")?possibleAnswer.get("displayLabel").toString():"");
						listPossibleAnswer.setDisplayOrder(null!=possibleAnswer.get("displayOrder")?possibleAnswer.get("displayOrder").toString():"");
						listPossibleAnswer.setValue(null!=possibleAnswer.get("value")?possibleAnswer.get("value").toString():"");
						strValue = getRequiredBoolean((Long) possibleAnswer.get("isRequired"));
						listPossibleAnswer.setIsRequired(strValue);
						listPossibleAnswers = (ListPossibleAnswers[]) ArrayUtils.add(listPossibleAnswers, listPossibleAnswer);
					}
				}else{
					listPossibleAnswers= new ListPossibleAnswers[]{};
				}
				listNewQuestions.setListPossibleAnswers(listPossibleAnswers);
				listQuestions = (ListQuestions[]) ArrayUtils.add(listQuestions, listNewQuestions); 
		}	
		}catch(Exception e){
			printException(e,"PostVisitforUser -setNewAssociatedQuestionsNewSections");
		}
		return listQuestions;
	}

	
	
	public SectionTemplate[] getNewSelectedTempalateAssociatedQuestions(List<JSONObject> listofAssociatedQuestionsforSectionTemplate, SectionTemplate[] listQuestionsInSORSectionTemplate, String newCardID){
		logger.severe("getNewSelectedTempalateAssociatedQuestions");
		SectionTemplate listQuestionsNewInSORSectionTemplate = new SectionTemplate();
		String questionIDST = new String();
		
		try{
			for (int m = 0; m < listofAssociatedQuestionsforSectionTemplate.size(); m++) {
				listQuestionsNewInSORSectionTemplate = new SectionTemplate();
				questionIDST = newCardID +"-Section0-Question"+m;
				listQuestionsNewInSORSectionTemplate.setQuestionId(questionIDST); 
				listQuestionsNewInSORSectionTemplate.setIsRequired(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("isRequired")?listofAssociatedQuestionsforSectionTemplate.get(m).get("isRequired").toString():"");
				listQuestionsNewInSORSectionTemplate.setType(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("type")?listofAssociatedQuestionsforSectionTemplate.get(m).get("type").toString():"");
				listQuestionsNewInSORSectionTemplate.setUnits(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("units")?listofAssociatedQuestionsforSectionTemplate.get(m).get("units").toString():"");
				listQuestionsNewInSORSectionTemplate.setDependent(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("dependent")?listofAssociatedQuestionsforSectionTemplate.get(m).get("dependent").toString():"");
				listQuestionsNewInSORSectionTemplate.setDependOn(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("dependOn")?listofAssociatedQuestionsforSectionTemplate.get(m).get("dependOn").toString():"");
				listQuestionsNewInSORSectionTemplate.setCondition(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("condition")?listofAssociatedQuestionsforSectionTemplate.get(m).get("condition").toString():"");
				listQuestionsNewInSORSectionTemplate.setAdditionalInformation(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("additionalInformation")?listofAssociatedQuestionsforSectionTemplate.get(m).get("additionalInformation").toString():"");
				listQuestionsNewInSORSectionTemplate.setDisplayLabel(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("displayLabel")?listofAssociatedQuestionsforSectionTemplate.get(m).get("displayLabel").toString():"");
				listQuestionsNewInSORSectionTemplate.setInfoQuestion(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("infoQuestion")?listofAssociatedQuestionsforSectionTemplate.get(m).get("infoQuestion").toString():"");
				listQuestionsNewInSORSectionTemplate.setIsVisible(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("isVisible")?listofAssociatedQuestionsforSectionTemplate.get(m).get("isVisible").toString():"");
				listQuestionsNewInSORSectionTemplate.setDisplayOrder(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("displayOrder")?listofAssociatedQuestionsforSectionTemplate.get(m).get("displayOrder").toString():"");
				listQuestionsNewInSORSectionTemplate.setGhostValue(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("ghostValue")?listofAssociatedQuestionsforSectionTemplate.get(m).get("ghostValue").toString():"");
				listQuestionsNewInSORSectionTemplate.setIsEditable(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("isEditable")?listofAssociatedQuestionsforSectionTemplate.get(m).get("isEditable").toString():"");
				listQuestionsNewInSORSectionTemplate.setDisplayLabel(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("displayLabel")?listofAssociatedQuestionsforSectionTemplate.get(m).get("displayLabel").toString():"");
				
				String iosAnswerStringArray = (String) listofAssociatedQuestionsforSectionTemplate.get(m).get("answer");
				if(iosAnswerStringArray!=null){
					String[] strArray = iosAnswerStringArray.split(",");
					Answer answerSORObject = new Answer();
					answerSORObject.setValue(strArray);
					listQuestionsNewInSORSectionTemplate.setAnswer(answerSORObject);
				}

				listQuestionsInSORSectionTemplate = (SectionTemplate[]) ArrayUtils.add(listQuestionsInSORSectionTemplate, listQuestionsNewInSORSectionTemplate); 
				
			}
		}catch(Exception e){
			printException(e,"PostVisitforUser - getNewSelectedTempalateAssociatedQuestions");
		}
		return listQuestionsInSORSectionTemplate;
	}
		
		
	
	
	public SectionTemplate[] getSelectedTempalateAssociatedQuestions(List<JSONObject> listofAssociatedQuestionsforSectionTemplate,SectionTemplate[] listQuestionsInSORSectionTemplate){
		logger.severe("getSelectedTempalateAssociatedQuestions");
		//Section Templated -- Associated Questions - starts

		for (int m = 0; m < listofAssociatedQuestionsforSectionTemplate.size(); m++) {
			for (int m2 = 0; m2 < listQuestionsInSORSectionTemplate.length; m2++) {
				String iosQuestionID = (String)listofAssociatedQuestionsforSectionTemplate.get(m).get("questionId");
				String sorQuestionID = listQuestionsInSORSectionTemplate[m2].getQuestionId();
				
				if(iosQuestionID.equalsIgnoreCase(sorQuestionID)){
					listQuestionsInSORSectionTemplate[m2].setIsRequired(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("isRequired")?listofAssociatedQuestionsforSectionTemplate.get(m).get("isRequired").toString():"");
					listQuestionsInSORSectionTemplate[m2].setType(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("type")?listofAssociatedQuestionsforSectionTemplate.get(m).get("type").toString():"");
					listQuestionsInSORSectionTemplate[m2].setUnits(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("units")?listofAssociatedQuestionsforSectionTemplate.get(m).get("units").toString():"");
					listQuestionsInSORSectionTemplate[m2].setDependent(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("dependent")?listofAssociatedQuestionsforSectionTemplate.get(m).get("dependent").toString():"");
					listQuestionsInSORSectionTemplate[m2].setDependOn(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("dependOn")?listofAssociatedQuestionsforSectionTemplate.get(m).get("dependOn").toString():"");
					listQuestionsInSORSectionTemplate[m2].setCondition(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("condition")?listofAssociatedQuestionsforSectionTemplate.get(m).get("condition").toString():"");
					listQuestionsInSORSectionTemplate[m2].setAdditionalInformation(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("additionalInformation")?listofAssociatedQuestionsforSectionTemplate.get(m).get("additionalInformation").toString():"");
					listQuestionsInSORSectionTemplate[m2].setDisplayLabel(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("displayLabel")?listofAssociatedQuestionsforSectionTemplate.get(m).get("displayLabel").toString():"");
					listQuestionsInSORSectionTemplate[m2].setInfoQuestion(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("infoQuestion")?listofAssociatedQuestionsforSectionTemplate.get(m).get("infoQuestion").toString():"");
					listQuestionsInSORSectionTemplate[m2].setIsVisible(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("isVisible")?listofAssociatedQuestionsforSectionTemplate.get(m).get("isVisible").toString():"");
					listQuestionsInSORSectionTemplate[m2].setDisplayOrder(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("displayOrder")?listofAssociatedQuestionsforSectionTemplate.get(m).get("displayOrder").toString():"");
					listQuestionsInSORSectionTemplate[m2].setGhostValue(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("ghostValue")?listofAssociatedQuestionsforSectionTemplate.get(m).get("ghostValue").toString():"");
					listQuestionsInSORSectionTemplate[m2].setIsEditable(null!=listofAssociatedQuestionsforSectionTemplate.get(m).get("isEditable")?listofAssociatedQuestionsforSectionTemplate.get(m).get("isEditable").toString():"");
					JSONObject iosAnswerJSONObject = (JSONObject) listofAssociatedQuestionsforSectionTemplate.get(m).get("answer");
					if(iosAnswerJSONObject!=null){
						JSONArray iosAnswerValue = (JSONArray) iosAnswerJSONObject.get("value");
						Answer answerSORObject = listQuestionsInSORSectionTemplate[m2].getAnswer();
						if (answerSORObject!=null){
							answerSORObject.setValue(jsonArrayToStringArray(iosAnswerValue));
						}else{
							answerSORObject = null;
						}
						
						listQuestionsInSORSectionTemplate[m2].setAnswer(answerSORObject);
					}
					
				}
			}
		}	
		return listQuestionsInSORSectionTemplate;
		//Associated Questions - End
	}
	
	
public String[] jsonArrayToStringArray(JSONArray jsonArray) {
    int arraySize = jsonArray.size();
    String[] stringArray = new String[arraySize];

    for(int i=0; i<arraySize; i++) {
        stringArray[i] = (String) jsonArray.get(i);
    }

    return stringArray;
}



	public String[] stringArrayToJSONArray(String[] stringArray) {
	    return stringArray;
	}

	
	
	@SuppressWarnings("unchecked")
	public VisitsByIDBean setListOfCatagories(JSONObject iosJSON,VisitsByIDBean visitsByIDSORData) {
		logger.severe("setListOfCatagories");
		List<JSONObject> listofiOSCatagories = (List<JSONObject>) iosJSON.get("categories");
		ListCategories[] listOfSORCatagories = visitsByIDSORData.getListCategories();
		if (listofiOSCatagories !=null && listOfSORCatagories!= null){
			for (int iosListCatagories = 0; iosListCatagories < listofiOSCatagories.size(); iosListCatagories++) {
				for (int sorListCatagories = 0; sorListCatagories < listOfSORCatagories.length; sorListCatagories++) {
					String iosCatagoryId = (String) listofiOSCatagories.get(iosListCatagories).get("categoryId");
					String sorCatagoryExtID = 	listOfSORCatagories[sorListCatagories].getExternalId();
					
					if(iosCatagoryId.equalsIgnoreCase(sorCatagoryExtID)){
						logger.severe("sorCatagoryID   " +sorCatagoryExtID);
						listOfSORCatagories[sorListCatagories].setDisplayName(null!=listofiOSCatagories.get(iosListCatagories).get("displayName")?listofiOSCatagories.get(iosListCatagories).get("displayName").toString():"");
						listOfSORCatagories[sorListCatagories].setDisplayOrder(null!=listofiOSCatagories.get(iosListCatagories).get("displayOrder")?listofiOSCatagories.get(iosListCatagories).get("displayOrder").toString():"");
						//Get the activities from catagories
						List<JSONObject> listofiOSActivitiesforCatagories = (List<JSONObject>)listofiOSCatagories.get(iosListCatagories).get("activities");
						ListCards[] listOfSORCards =  listOfSORCatagories[sorListCatagories].getListCards();
						listOfSORCards = setActivitiesforCatagories(listofiOSActivitiesforCatagories, listOfSORCards, iosCatagoryId);
						
						if(listOfSORCatagories[sorListCatagories].getType().equalsIgnoreCase("Notes")){
							for (int lstSORCardInt = 0; lstSORCardInt < listOfSORCards.length; lstSORCardInt++) {
								
								if(listOfSORCards[lstSORCardInt].getNotesIdentityInCard()==1){
									if(listOfSORCatagories[sorListCatagories].getInternalId()!= null && listOfSORCatagories[sorListCatagories].getInternalId().equals("-1")){
										listOfSORCatagories[sorListCatagories].setInternalId(null);
									}
									if(listOfSORCards[lstSORCardInt].getInternalId()!=null && listOfSORCards[lstSORCardInt].getInternalId().equals("-1")){
										listOfSORCards[lstSORCardInt].setInternalId(null);
									}
								}
							}
						}
						
						listOfSORCatagories[sorListCatagories].setListCards(listOfSORCards);
					}else{
						// error - Catagory ID if fails
					}
				}
				
			} // Catagories loop end
			
			visitsByIDSORData.setVendorName(null!=iosJSON.get("vendorName")?iosJSON.get("vendorName").toString():"");
			visitsByIDSORData.setState(null!=iosJSON.get("visitStatus")?iosJSON.get("visitStatus").toString():"");  //************
			visitsByIDSORData.setDescriptor2Value(null!=iosJSON.get("descriptor2Value")?iosJSON.get("descriptor2Value").toString():"");
			visitsByIDSORData.setLocationId(null!=iosJSON.get("locationId")?iosJSON.get("locationId").toString():"");
			visitsByIDSORData.setSyncStatus(null!=iosJSON.get("syncStatus")?iosJSON.get("syncStatus").toString():"");
			visitsByIDSORData.setVisitId(null!=iosJSON.get("visitId")?iosJSON.get("visitId").toString():"");
			visitsByIDSORData.setDate(null!=iosJSON.get("date")?iosJSON.get("date").toString():"");
			visitsByIDSORData.setType(null!=iosJSON.get("type")?iosJSON.get("type").toString():"");
			visitsByIDSORData.setDescriptor1Label(null!=iosJSON.get("descriptor1Label")?iosJSON.get("descriptor1Label").toString():"");
			visitsByIDSORData.setTotalDuration(null!=iosJSON.get("accumulatedTime")?iosJSON.get("accumulatedTime").toString():"");
			visitsByIDSORData.setDescriptor1Value(null!=iosJSON.get("descriptor1Value")?iosJSON.get("descriptor1Value").toString():"");
			visitsByIDSORData.setManufacturerName(null!=iosJSON.get("manufacturerName")?iosJSON.get("manufacturerName").toString():"");
			visitsByIDSORData.setDescriptor2Label(null!=iosJSON.get("descriptor2Label")?iosJSON.get("descriptor2Label").toString():"");
			visitsByIDSORData.setDisplayName(null!=iosJSON.get("displayName")?iosJSON.get("displayName").toString():"");
			visitsByIDSORData.setStateId(null);
		}
		return visitsByIDSORData;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean isCatagoryIdsforSORwithiOS(VisitsByIDBean visitsByIDSORData, JSONObject iosJSON){
		int catagoryTrueValue = 0;
		int catagoryFalseVlaue =0;
		List<JSONObject> listofiOSCatagories = (List<JSONObject>) iosJSON.get("categories");
		ListCategories[] listOfSORCatagories = visitsByIDSORData.getListCategories();
		for (int iosListCatagories = 0; iosListCatagories < listofiOSCatagories.size(); iosListCatagories++) {
			for (int sorListCatagories = 0; sorListCatagories < listOfSORCatagories.length; sorListCatagories++) {
				String iosCatagoryId = (String) listofiOSCatagories.get(iosListCatagories).get("categoryId");
				String sorCatagoryID = 	listOfSORCatagories[sorListCatagories].getCategoryId();
				if(iosCatagoryId.equalsIgnoreCase(sorCatagoryID)){
					catagoryTrueValue = catagoryTrueValue +1;
				}else{
					catagoryFalseVlaue = catagoryFalseVlaue +1;
				}
			}
		}
		if(listofiOSCatagories.size() == catagoryTrueValue){
			return true;
		}else{
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Response postVisit(String contentType, String acceptLanguage, String userAgent, String apiVersion, String request, String userId, String token) {
		logger.severe("postVisit - postvist");
		Response response = null;
		String clientSORRes="";
		JSONObject resObject = new JSONObject();
		JSONObject errorResponse = new JSONObject();
		List<JSONObject> error = new ArrayList<>();
		JSONObject errorObject = new JSONObject();
		try{
			
			if(null==request || request.isEmpty()){
				response = APIResponseBuilder.sendFailResponse(apiVersion,
						ServiceConstants.ERROR_CODE_ERROR,
						ServiceConstants.ERROR_MESSAGE_ERROR,
						ServiceConstants.USER_ID_CHECK_MESSAGE, ServiceConstants.ERROR_MESSAGE_INVALIDREQUESTBODY,
						ServiceConstants.LEVEL_ERROR);
				return response;
			}
			JSONObject iosRequest = (JSONObject) JSONValue.parse(request);
			JSONArray dataArray = (JSONArray) iosRequest.get("data");
			JSONObject iosJSON = (JSONObject) dataArray.get(0);
			logger.severe("JSONObject retrieved after successful parsing." + iosJSON); 
			String locationId = (String) iosJSON.get("locationId");
			String visitId= (String)iosJSON.get("visitId");		
			
			Object objectSOR  = getSORJSON(userId,locationId,visitId,apiVersion,token);
			
			if(objectSOR!=null && (objectSOR instanceof VisitsByIDBean)){
				VisitsByIDBean visitsByIDSORData = (VisitsByIDBean) objectSOR;
				VisitsByIDBean finalJSONObject = null;
				finalJSONObject = setListOfCatagories(iosJSON,visitsByIDSORData);
				
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
				
				String jsonInString = mapper.writeValueAsString(finalJSONObject);
				logger.severe("jsonInString  " +jsonInString);
				clientSORRes = URLUtility.getSORDataWithHeaderParamsForUpdateNCR(URLUtility.getRequiredUrl(ServiceConstants.IOS_VISIT),jsonInString,token);
				JSONObject sorResponse = (JSONObject) JSONValue.parse(clientSORRes);
				String sorResCode = new String();
				String sorResMessage = new String();
				if(sorResponse!=null){
					sorResCode = (String)sorResponse.get("id");
					sorResMessage = (String) sorResponse.get("message");
				}else{
					sorResCode = "400";
					sorResMessage = "Invalid SOR Response";
				}
				resObject = getResponseObject(clientSORRes,resObject,"visits");
				 JSONObject resObjectValue = (JSONObject) JSONValue.parse(clientSORRes);
				 String code = (String) resObjectValue.get("id");
				 int responseCode = Integer.parseInt(code);
				response = APIResponseBuilder.sendSuccessResponse(apiVersion, responseCode,
						ServiceConstants.SUCCESS_MESSAGE,resObject);
				
			}else if(objectSOR!=null && (objectSOR instanceof String)){
				if(objectSOR.toString().contains("Authorization is not valid")){
					errorResponse.put("message", "Authorization is not valid.");
	            	errorResponse.put("id", "403");
	            	errorResponse.put("level", "ERROR");
	            	error.add(errorResponse);
	            	errorObject.put("errors", error);
	            	errorObject.put("data", new JSONArray());
	            	response = APIResponseBuilder.sendSuccessResponse(apiVersion, 403,
							ServiceConstants.SUCCESS_MESSAGE,errorObject);
	            	logger.severe(" " +errorObject.get("errors"));
				}else{
					errorResponse.put("message", "No data from SOR response after update call.");
	            	errorResponse.put("id", "400");
	            	errorResponse.put("level", "ERROR");
	            	error.add(errorResponse);
	            	errorObject.put("errors", error);
	            	errorObject.put("data", new JSONArray());
	            	response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK,
							ServiceConstants.SUCCESS_MESSAGE,errorObject);
	            	logger.severe(" " +errorObject.get("errors"));
				}
			}else if(objectSOR!=null && (objectSOR instanceof Response)){
				errorResponse.put("message", "VPN Service is not Available");
            	errorResponse.put("id", "500");
            	errorResponse.put("level", "ERROR");
            	error.add(errorResponse);
            	errorObject.put("errors", error);
            	errorObject.put("data", new JSONArray());
            	response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK,
						ServiceConstants.SUCCESS_MESSAGE,errorObject);
            	logger.severe(" " +errorObject.get("errors"));
			}else{
				errorResponse.put("message", "No data from SOR response after update call.");
            	errorResponse.put("id", "500");
            	errorResponse.put("level", "ERROR");
            	error.add(errorResponse);
            	errorObject.put("errors", error);
            	errorObject.put("data", new JSONArray());
            	response = APIResponseBuilder.sendSuccessResponse(apiVersion, ServiceConstants.ERROR_CODE_OK,
						ServiceConstants.SUCCESS_MESSAGE,errorObject);
            	logger.severe(" " +errorObject.get("errors"));
			}
		} catch (Exception e) {
			response = APIResponseBuilder.sendFailResponse(apiVersion,
					ServiceConstants.ERROR_CODE_ERROR,
					ServiceConstants.ERROR_MESSAGE_ERROR,
					ServiceConstants.USER_ID_CHECK_MESSAGE, e.getMessage(),
					ServiceConstants.LEVEL_ERROR);
			printException(e,"PostVisitforUser - postVisit");
		}
		return response;
	}

	
	/**
	 * 
	 * @param isValue
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getRequiredString(boolean isValue) {
		String stringValue="";
		try{
			if(isValue){
				stringValue = "true";
			}else{
				stringValue = "false";
			}
		}catch(Exception e){
			logger.severe("Exception in getRequiredString");
			printException(e,"PostVisitforUser - getRequiredString");
		}
		return stringValue;
	}
	
	/**
	 * 
	 * @param longValue
	 * @return
	 */
	private String getRequiredBoolean(Long longValue) {
		// 0- false, 1- true
		String retString = null;
		boolean boolValue=false;
			if(longValue != null){
			try{
				if(longValue == 0){
					retString= "false";
				}else if (longValue == 1){
					retString = "true";
				}
			}catch(Exception e){
				logger.severe("Exception in getRequiredBoolean");
				printException(e,"PostVisitforUser - getRequiredBoolean");
			}
		}
		return retString;
	}
	
	/**
	 * 
	 * @param displayLabel
	 * @return
	 */
	public String getOrginValue(String displayLabel){
		byte ptext[] = displayLabel.getBytes();
		byte ptext2[] = new byte[ptext.length];
		int i3 =0;
		String oriValue = new String();
		try {
			for (int i1 = 0; i1 < ptext.length; i1++) {
				
				if(ptext[i1]>0){
					ptext2[i3++] = ptext[i1];
				}
			}
			oriValue = new String(ptext2, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			printException(e,"PostVisitforUser - getOrginValue");
		}
		return oriValue;
	}
	
	
	void printException(Exception e,String methodName) {
		logger.severe("Exception in "+methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject getResponseObject(String result,JSONObject resObject,String item) {
		JSONObject errorResponse = new JSONObject();
		List<JSONObject> error = new ArrayList<>();
		JSONObject errorObject = new JSONObject();
		JSONObject successObject = new JSONObject();
		List<JSONObject> successObjectList = new ArrayList<>();
		try{
			 resObject = (JSONObject) JSONValue.parse(result);
			 String code = (String) resObject.get("id");
			 if("200".equalsIgnoreCase(code)){
				 successObjectList.add(resObject);
				 successObject.put("data", successObjectList);
				 successObject.put("errors", new JSONArray());
				 return successObject; 
			 }else if("403".equalsIgnoreCase(code)){
				    errorResponse.put("message", "Authorization is not valid.Please login again.");
	            	errorResponse.put("id", "403");
	            	errorResponse.put("level", "ERROR");
	            	error.add(errorResponse);
	            	errorObject.put("errors", error);
	            	errorObject.put("data", new JSONArray());
			 }else{
				    errorResponse.put("message", "Update "+item+" failed.");
	            	errorResponse.put("id", code);
	            	errorResponse.put("level", "ERROR");
	            	error.add(errorResponse);
	            	errorObject.put("errors", error);
	            	errorObject.put("data", new JSONArray());
			 }
		}catch(Exception e){
			printException(e,"getResponseObject");
		}
		return errorObject;
	}

}

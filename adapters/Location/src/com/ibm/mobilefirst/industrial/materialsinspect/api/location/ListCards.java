/**
 * 
 */
package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

/**
 * @author IBM
 *
 */
public class ListCards {
	
	private String internalId;

	private String externalId;
	
    private String cardId;

    private String isDuplicable;

    private String openDate;
    
    private String closedDate;

    private PossibleSections[] possibleSections;

    private String dependentCard;

    private String scope;

    private String isTable;

    private String isSkippable;

    private String reason;

    private String state;

    private ListSections[] listSections;

    private String type;

    private String activityId;

    private String userCreated;

    private String isRequired;

    private String displayType;

    private String stateId;

    private String internalIdCFG;

    private SectionTemplate[] sectionTemplate;

    private String displayOrder;

    private String additionalInformation;

    private String typePossibleSections;

    private String displayName;

    private String infoCard;

    private String typeId;

    private String dependOnCard;

    public String getInternalId ()
    {
        return internalId;
    }

    public void setInternalId (String internalId)
    {
        this.internalId = internalId;
    }
    
    public String getOpenDate ()
    {
        return openDate;
    }

    public void setOpenDate (String openDate)
    {
        this.openDate = openDate;
    }
    
    public String getClosedDate ()
    {
        return closedDate;
    }

    public void setClosedDate (String closedDate)
    {
        this.closedDate = closedDate;
    }
    
    public String getExternalId ()
    {
        return externalId;
    }

    public void setEnternalId (String externalId)
    {
        this.externalId = externalId;
    }

    public String getCardId ()
    {
        return cardId;
    }

    public void setCardId (String cardId)
    {
        this.cardId = cardId;
    }

    public String getIsDuplicable ()
    {
        return isDuplicable;
    }

    public void setIsDuplicable (String isDuplicable)
    {
        this.isDuplicable = isDuplicable;
    }

    public PossibleSections[] getPossibleSections ()
    {
        return possibleSections;
    }

    public void setPossibleSections (PossibleSections[] possibleSections)
    {
        this.possibleSections = possibleSections;
    }

    public String getDependentCard ()
    {
        return dependentCard;
    }

    public void setDependentCard (String dependentCard)
    {
        this.dependentCard = dependentCard;
    }

    public String getScope ()
    {
        return scope;
    }

    public void setScope (String scope)
    {
        this.scope = scope;
    }

    public String getIsTable ()
    {
        return isTable;
    }

    public void setIsTable (String isTable)
    {
        this.isTable = isTable;
    }

    public String getIsSkippable ()
    {
        return isSkippable;
    }

    public void setIsSkippable (String isSkippable)
    {
        this.isSkippable = isSkippable;
    }

    public String getReason ()
    {
        return reason;
    }

    public void setReason (String reason)
    {
        this.reason = reason;
    }

    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public ListSections[] getListSections ()
    {
        return listSections;
    }

    public void setListSections (ListSections[] listSections)
    {
        this.listSections = listSections;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getActivityId ()
    {
        return activityId;
    }

    public void setActivityId (String activityId)
    {
        this.activityId = activityId;
    }

    public String getUserCreated ()
    {
        return userCreated;
    }

    public void setUserCreated (String userCreated)
    {
        this.userCreated = userCreated;
    }

    public String getIsRequired ()
    {
        return isRequired;
    }

    public void setIsRequired (String isRequired)
    {
        this.isRequired = isRequired;
    }

    public String getDisplayType ()
    {
        return displayType;
    }

    public void setDisplayType (String displayType)
    {
        this.displayType = displayType;
    }

    public String getStateId ()
    {
        return stateId;
    }

    public void setStateId (String stateId)
    {
        this.stateId = stateId;
    }

    public String getInternalIdCFG ()
    {
        return internalIdCFG;
    }

    public void setInternalIdCFG (String internalIdCFG)
    {
        this.internalIdCFG = internalIdCFG;
    }

    public SectionTemplate[] getSectionTemplate ()
    {
        return sectionTemplate;
    }

    public void setSectionTemplate (SectionTemplate[] sectionTemplate)
    {
        this.sectionTemplate = sectionTemplate;
    }

    public String getDisplayOrder ()
    {
        return displayOrder;
    }

    public void setDisplayOrder (String displayOrder)
    {
        this.displayOrder = displayOrder;
    }

    public String getAdditionalInformation ()
    {
        return additionalInformation;
    }

    public void setAdditionalInformation (String additionalInformation)
    {
        this.additionalInformation = additionalInformation;
    }

    public String getTypePossibleSections ()
    {
        return typePossibleSections;
    }

    public void setTypePossibleSections (String typePossibleSections)
    {
        this.typePossibleSections = typePossibleSections;
    }

    public String getDisplayName ()
    {
        return displayName;
    }

    public void setDisplayName (String displayName)
    {
        this.displayName = displayName;
    }

    public String getInfoCard ()
    {
        return infoCard;
    }

    public void setInfoCard (String infoCard)
    {
        this.infoCard = infoCard;
    }

    public String getTypeId ()
    {
        return typeId;
    }

    public void setTypeId (String typeId)
    {
        this.typeId = typeId;
    }

    public String getDependOnCard ()
    {
        return dependOnCard;
    }

    public void setDependOnCard (String dependOnCard)
    {
        this.dependOnCard = dependOnCard;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [internalId = "+internalId+", cardId = "+cardId+", isDuplicable = "+isDuplicable+", possibleSections = "+possibleSections+", dependentCard = "+dependentCard+", scope = "+scope+", isTable = "+isTable+", isSkippable = "+isSkippable+", reason = "+reason+", state = "+state+", listSections = "+listSections+", type = "+type+", activityId = "+activityId+", userCreated = "+userCreated+", isRequired = "+isRequired+", displayType = "+displayType+", stateId = "+stateId+", internalIdCFG = "+internalIdCFG+", sectionTemplate = "+sectionTemplate+", displayOrder = "+displayOrder+", additionalInformation = "+additionalInformation+", typePossibleSections = "+typePossibleSections+", displayName = "+displayName+", infoCard = "+infoCard+", typeId = "+typeId+", dependOnCard = "+dependOnCard+"]";
    }

}

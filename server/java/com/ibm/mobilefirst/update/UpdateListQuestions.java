package com.ibm.mobilefirst.update;

import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONObject;

public class UpdateListQuestions {

	private String internalId;

    private String dependOn;

    private String ghostValue;

    private List<JSONObject> ListPossibleAnswers;

    private String displayLabel;

    private String infoQuestion;

    private String questionId;

    private String condition;

    private String dependent;

    private UpdateAnswer answer;

    private String type;

    private String units;

    private String isVisible;

    private String isRequired;

    private String internalIdCFG;

    private String displayOrder;

    private String additionalInformation;

    private String defaultValue;

    private String typeId;

    private String isEditable;

    public String getInternalId ()
    {
        return internalId;
    }

    public void setInternalId (String internalId)
    {
        this.internalId = internalId;
    }

    public String getDependOn ()
    {
        return dependOn;
    }

    public void setDependOn (String dependOn)
    {
        this.dependOn = dependOn;
    }

    public String getGhostValue ()
    {
        return ghostValue;
    }

    public void setGhostValue (String ghostValue)
    {
        this.ghostValue = ghostValue;
    }

    public List<JSONObject> getListPossibleAnswers ()
    {
        return ListPossibleAnswers;
    }

    public void setListPossibleAnswers (List<JSONObject> ListPossibleAnswers)
    {
        this.ListPossibleAnswers = ListPossibleAnswers;
    }

    public String getDisplayLabel ()
    {
        return displayLabel;
    }

    public void setDisplayLabel (String displayLabel)
    {
        this.displayLabel = displayLabel;
    }

    public String getInfoQuestion ()
    {
        return infoQuestion;
    }

    public void setInfoQuestion (String infoQuestion)
    {
        this.infoQuestion = infoQuestion;
    }

    public String getQuestionId ()
    {
        return questionId;
    }

    public void setQuestionId (String questionId)
    {
        this.questionId = questionId;
    }

    public String getCondition ()
    {
        return condition;
    }

    public void setCondition (String condition)
    {
        this.condition = condition;
    }

    public String getDependent ()
    {
        return dependent;
    }

    public void setDependent (String dependent)
    {
        this.dependent = dependent;
    }

    public UpdateAnswer getAnswer ()
    {
        return answer;
    }

    public void setAnswer (UpdateAnswer answer)
    {
        this.answer = answer;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getUnits ()
    {
        return units;
    }

    public void setUnits (String units)
    {
        this.units = units;
    }

    public String getIsVisible ()
    {
        return isVisible;
    }

    public void setIsVisible (String isVisible)
    {
        this.isVisible = isVisible;
    }

    public String getIsRequired ()
    {
        return isRequired;
    }

    public void setIsRequired (String isRequired)
    {
        this.isRequired = isRequired;
    }

    public String getInternalIdCFG ()
    {
        return internalIdCFG;
    }

    public void setInternalIdCFG (String internalIdCFG)
    {
        this.internalIdCFG = internalIdCFG;
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

    public String getDefaultValue ()
    {
        return defaultValue;
    }

    public void setDefaultValue (String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public String getTypeId ()
    {
        return typeId;
    }

    public void setTypeId (String typeId)
    {
        this.typeId = typeId;
    }

    public String getIsEditable ()
    {
        return isEditable;
    }

    public void setIsEditable (String isEditable)
    {
        this.isEditable = isEditable;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [internalId = "+internalId+", dependOn = "+dependOn+", ghostValue = "+ghostValue+", ListPossibleAnswers = "+ListPossibleAnswers+", displayLabel = "+displayLabel+", infoQuestion = "+infoQuestion+", questionId = "+questionId+", condition = "+condition+", dependent = "+dependent+", answer = "+answer+", type = "+type+", units = "+units+", isVisible = "+isVisible+", isRequired = "+isRequired+", internalIdCFG = "+internalIdCFG+", displayOrder = "+displayOrder+", additionalInformation = "+additionalInformation+", defaultValue = "+defaultValue+", typeId = "+typeId+", isEditable = "+isEditable+"]";
    }
}

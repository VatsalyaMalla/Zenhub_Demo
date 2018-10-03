package com.ibm.mobilefirst.update;

import java.util.List;
import org.json.simple.JSONObject;

public class UpdateListSections {
	
	private String internalId;

    private List<JSONObject> listQuestions;

    private String sectionId;

    private String displayOrder;

    private String displayName;

    public String getInternalId ()
    {
        return internalId;
    }

    public void setInternalId (String internalId)
    {
        this.internalId = internalId;
    }

    public List<JSONObject> getListQuestions ()
    {
        return listQuestions;
    }

    public void setListQuestions (List<JSONObject> listQuestions)
    {
        this.listQuestions = listQuestions;
    }

    public String getSectionId ()
    {
        return sectionId;
    }

    public void setSectionId (String sectionId)
    {
        this.sectionId = sectionId;
    }

    public String getDisplayOrder ()
    {
        return displayOrder;
    }

    public void setDisplayOrder (String displayOrder)
    {
        this.displayOrder = displayOrder;
    }

    public String getDisplayName ()
    {
        return displayName;
    }

    public void setDisplayName (String displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [internalId = "+internalId+", listQuestions = "+listQuestions+", sectionId = "+sectionId+", displayOrder = "+displayOrder+", displayName = "+displayName+"]";
    }

}

package com.ibm.mobilefirst.update;

import java.util.List;
import org.json.simple.JSONObject;

public class UpdateListCategories {


	
	 private String internalId;

	    private List<JSONObject> listCards;

	    private String categoryId;

	    private String internalIdCFG;

	    private String displayOrder;

	    private String type;

	    private String displayName;

	    private String typeId;

	    public String getInternalId ()
	    {
	        return internalId;
	    }

	    public void setInternalId (String internalId)
	    {
	        this.internalId = internalId;
	    }

	    public List<JSONObject> getListCards ()
	    {
	        return listCards;
	    }

	    public void setListCards (List<JSONObject> listCards)
	    {
	        this.listCards = listCards;
	    }

	    public String getCategoryId ()
	    {
	        return categoryId;
	    }

	    public void setCategoryId (String categoryId)
	    {
	        this.categoryId = categoryId;
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

	    public String getType ()
	    {
	        return type;
	    }

	    public void setType (String type)
	    {
	        this.type = type;
	    }

	    public String getDisplayName ()
	    {
	        return displayName;
	    }

	    public void setDisplayName (String displayName)
	    {
	        this.displayName = displayName;
	    }

	    public String getTypeId ()
	    {
	        return typeId;
	    }

	    public void setTypeId (String typeId)
	    {
	        this.typeId = typeId;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [internalId = "+internalId+", listCards = "+listCards+", categoryId = "+categoryId+", internalIdCFG = "+internalIdCFG+", displayOrder = "+displayOrder+", type = "+type+", displayName = "+displayName+", typeId = "+typeId+"]";
	    }


}

package com.ibm.mobilefirst.update;

public class UpdatePossibleSections {

	 private String internalId;

	    private String displayLabel;

	    private String isSelected;

	    private String displayOrder;

	    public String getInternalId ()
	    {
	        return internalId;
	    }

	    public void setInternalId (String internalId)
	    {
	        this.internalId = internalId;
	    }

	    public String getDisplayLabel ()
	    {
	        return displayLabel;
	    }

	    public void setDisplayLabel (String displayLabel)
	    {
	        this.displayLabel = displayLabel;
	    }

	    public String getIsSelected ()
	    {
	        return isSelected;
	    }

	    public void setIsSelected (String isSelected)
	    {
	        this.isSelected = isSelected;
	    }

	    public String getDisplayOrder ()
	    {
	        return displayOrder;
	    }

	    public void setDisplayOrder (String displayOrder)
	    {
	        this.displayOrder = displayOrder;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [internalId = "+internalId+", displayLabel = "+displayLabel+", isSelected = "+isSelected+", displayOrder = "+displayOrder+"]";
	    }



}

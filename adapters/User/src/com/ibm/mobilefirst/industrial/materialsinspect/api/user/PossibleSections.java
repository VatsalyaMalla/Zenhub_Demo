/**
 * 
 */
package com.ibm.mobilefirst.industrial.materialsinspect.api.user;

/**
 * @author IBM
 *
 */
public class PossibleSections {
	 private String internalId;

	    private String displayLabel;

	    private String isSelected;

	    private String displayOrder;
	    
	    private String isRequired; 

	    public String getIsRequired() 
	    {
	    	return isRequired;
	    }

	    public void setIsRequired(String isRequired) 
	    {
	    	this.isRequired = isRequired;
	    }

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

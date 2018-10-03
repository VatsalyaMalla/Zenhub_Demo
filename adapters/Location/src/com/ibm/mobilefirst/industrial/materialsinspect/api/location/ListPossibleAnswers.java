/**
 * 
 */
package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

/**
 * @author IBM
 *
 */
public class ListPossibleAnswers {
	 private String displayLabel;

	    private String isRequired;

	    private String value;

	    private String displayOrder;

	    public String getDisplayLabel ()
	    {
	        return displayLabel;
	    }

	    public void setDisplayLabel (String displayLabel)
	    {
	        this.displayLabel = displayLabel;
	    }

	    public String getIsRequired ()
	    {
	        return isRequired;
	    }

	    public void setIsRequired (String isRequired)
	    {
	        this.isRequired = isRequired;
	    }

	    public String getValue ()
	    {
	        return value;
	    }

	    public void setValue (String value)
	    {
	        this.value = value;
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
	        return "ClassPojo [displayLabel = "+displayLabel+", isRequired = "+isRequired+", value = "+value+", displayOrder = "+displayOrder+"]";
	    }

}

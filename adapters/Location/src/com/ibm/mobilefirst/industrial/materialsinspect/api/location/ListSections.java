/**
 * 
 */
package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

/**
 * @author IBM
 *
 */
public class ListSections {
	 	private String internalId;
	 	
	 	private String externalId;
	 	
	 	private String activityName;

	    private ListQuestions[] listQuestions;

	    private String sectionId;

	    private String displayOrder;

	    private String displayName;
	    
	    private String openDate;
	    
	    private String closedDate;
	    
	    public String getExternalId ()
	    {
	        return externalId;
	    }

	    public void setEnternalId (String externalId)
	    {
	        this.externalId = externalId;
	    }
	    
	    public String getActivityName ()
	    {
	        return activityName;
	    }

	    public void setActivityName (String activityName)
	    {
	        this.activityName = activityName;
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

	    public String getInternalId ()
	    {
	        return internalId;
	    }

	    public void setInternalId (String internalId)
	    {
	        this.internalId = internalId;
	    }

	    public ListQuestions[] getListQuestions ()
	    {
	        return listQuestions;
	    }

	    public void setListQuestions (ListQuestions[] listQuestions)
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

/**
 * 
 */
package com.ibm.mobilefirst.industrial.materialsinspect.api.user;

/**
 * @author IBM
 *
 */
public class ListSections {
	 private String internalId;

	    private ListQuestions[] listQuestions;

	    private String sectionId;
	    
	    private String externalId;
	    
	    private int notesNewSectionIdentity;

	    public int getNotesNewSectionIdentity() {
			return notesNewSectionIdentity;
		}

		public void setNotesNewSectionIdentity(int notesNewSectionIdentity) {
			this.notesNewSectionIdentity = notesNewSectionIdentity;
		}

		private String displayOrder;

	    private String displayName;
	    
	    private int removeSection;
	    
	    public String getExternalId ()
	    {
	        return externalId;
	    }

	    public void setExternalId (String externalId)
	    {
	        this.externalId = externalId;
	    }
	    
	    public int getRemoveSection() {
			return removeSection;
		}

		public void setRemoveSection(int removeSection) {
			this.removeSection = removeSection;
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

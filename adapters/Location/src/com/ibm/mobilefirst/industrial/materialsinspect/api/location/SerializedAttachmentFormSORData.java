package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

public class SerializedAttachmentFormSORData {
	
	private Object ID_ATTACHMENT;

    private String NAME;
    
    private java.lang.Object CODE;

    private String DESC;
    
    private String REF_VENDOR;
    
    private String TAG;

    private String FILEPATH;

    private Object ID_TYPE;

    private String DATE_CREATED;

    private String TYPE;
    
    private java.lang.Object LAST_VERSION;
 	
 	public void setLastVersion(java.lang.Object LAST_VERSION) {
		this.LAST_VERSION = LAST_VERSION;
	}
 	
 	public java.lang.Object getLastVersion() {
		return LAST_VERSION;
	}

    public Object getID_ATTACHMENT ()
    {
        return ID_ATTACHMENT;
    }

    public void setID_ATTACHMENT (Object ID_ATTACHMENT)
    {
        this.ID_ATTACHMENT = ID_ATTACHMENT;
    }
    
    public void setCode(java.lang.Object CODE) {
		this.CODE = CODE;
	}
 	
 	public java.lang.Object getCode() {
		return CODE;
	}

    public String getNAME ()
    {
        return NAME;
    }

    public void setNAME (String NAME)
    {
        this.NAME = NAME;
    }

    public String getREF_VENDOR ()
    {
        return REF_VENDOR;
    }

    public void setREF_VENDOR (String REF_VENDOR)
    {
        this.REF_VENDOR = REF_VENDOR;
    }
    
    public String getTAG ()
    {
        return TAG;
    }

    public void setTAG (String TAG)
    {
        this.TAG = TAG;
    }
    
    public String getDESC ()
    {
        return DESC;
    }

    public void setDESC (String DESC)
    {
        this.DESC = DESC;
    }

    public String getFILEPATH ()
    {
        return FILEPATH;
    }

    public void setPATH (String FILEPATH)
    {
        this.FILEPATH = FILEPATH;
    }

    public Object getID_TYPE ()
    {
        return ID_TYPE;
    }

    public void setID_TYPE (Object ID_TYPE)
    {
        this.ID_TYPE = ID_TYPE;
    }

    public String getDATE_CREATED ()
    {
        return DATE_CREATED;
    }

    public void setDATE_CREATED (String DATE_CREATED)
    {
        this.DATE_CREATED = DATE_CREATED;
    }

    public String getTYPE ()
    {
        return TYPE;
    }

    public void setTYPE (String TYPE)
    {
        this.TYPE = TYPE;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ID_ATTACHMENT = "+ID_ATTACHMENT+", NAME = "+NAME+", DESC = "+DESC+", FILEPATH = "+FILEPATH+", ID_TYPE = "+ID_TYPE+", DATE_CREATED = "+DATE_CREATED+", TYPE = "+TYPE+"]";
    }

}

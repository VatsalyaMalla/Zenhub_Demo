package com.ibm.mobilefirst.industrial.materialsinspect.api.user.Location;

public class SerializedAttachmentFormSORData {
	
	private String ID_ATTACHMENT;

    private String NAME;

    private String DESC;

    private String FILEPATH;

    private String ID_TYPE;

    private String DATE_CREATED;

    private String TYPE;

    public String getID_ATTACHMENT ()
    {
        return ID_ATTACHMENT;
    }

    public void setID_ATTACHMENT (String ID_ATTACHMENT)
    {
        this.ID_ATTACHMENT = ID_ATTACHMENT;
    }

    public String getNAME ()
    {
        return NAME;
    }

    public void setNAME (String NAME)
    {
        this.NAME = NAME;
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

    public String getID_TYPE ()
    {
        return ID_TYPE;
    }

    public void setID_TYPE (String ID_TYPE)
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

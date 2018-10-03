package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

public class HistoricsObject {
	private String STATE;

    private String DATE;

    private String ID_TYPE;

    private String ID_STATE;

    private String ID_CATEGORY;

    private String ID_HISTORICAL_CATEGORY;

    private String TYPE;

    public String getSTATE ()
    {
        return STATE;
    }

    public void setSTATE (String STATE)
    {
        this.STATE = STATE;
    }

    public String getDATE ()
    {
        return DATE;
    }

    public void setDATE (String DATE)
    {
        this.DATE = DATE;
    }

    public String getID_TYPE ()
    {
        return ID_TYPE;
    }

    public void setID_TYPE (String ID_TYPE)
    {
        this.ID_TYPE = ID_TYPE;
    }

    public String getID_STATE ()
    {
        return ID_STATE;
    }

    public void setID_STATE (String ID_STATE)
    {
        this.ID_STATE = ID_STATE;
    }

    public String getID_CATEGORY ()
    {
        return ID_CATEGORY;
    }

    public void setID_CATEGORY (String ID_CATEGORY)
    {
        this.ID_CATEGORY = ID_CATEGORY;
    }

    public String getID_HISTORICAL_CATEGORY ()
    {
        return ID_HISTORICAL_CATEGORY;
    }

    public void setID_HISTORICAL_CATEGORY (String ID_HISTORICAL_CATEGORY)
    {
        this.ID_HISTORICAL_CATEGORY = ID_HISTORICAL_CATEGORY;
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
        return "ClassPojo [STATE = "+STATE+", DATE = "+DATE+", ID_TYPE = "+ID_TYPE+", ID_STATE = "+ID_STATE+", ID_CATEGORY = "+ID_CATEGORY+", ID_HISTORICAL_CATEGORY = "+ID_HISTORICAL_CATEGORY+", TYPE = "+TYPE+"]";
    }
}

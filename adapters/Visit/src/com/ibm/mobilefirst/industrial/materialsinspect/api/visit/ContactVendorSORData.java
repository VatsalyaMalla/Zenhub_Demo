package com.ibm.mobilefirst.industrial.materialsinspect.api.visit;

public class ContactVendorSORData {
	
	private String PHONE;

    private String NAME;

    private String EMAIL;

    private String FAX;

    public String getPHONE ()
    {
        return PHONE;
    }

    public void setPHONE (String PHONE)
    {
        this.PHONE = PHONE;
    }

    public String getNAME ()
    {
        return NAME;
    }

    public void setNAME (String NAME)
    {
        this.NAME = NAME;
    }

    public String getEMAIL ()
    {
        return EMAIL;
    }

    public void setEMAIL (String EMAIL)
    {
        this.EMAIL = EMAIL;
    }

    public String getFAX ()
    {
        return FAX;
    }

    public void setFAX (String FAX)
    {
        this.FAX = FAX;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [PHONE = "+PHONE+", NAME = "+NAME+", EMAIL = "+EMAIL+", FAX = "+FAX+"]";
    }

}

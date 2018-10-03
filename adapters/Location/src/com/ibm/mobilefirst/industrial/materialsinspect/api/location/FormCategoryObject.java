package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

public class FormCategoryObject {

	 private String NAME;

	    private HistoricsObject[] HISTORICS;

	    private String ID_CARD;

	    private String FORM_NAME;

	    private String ID_EXTERNAL;
	    
	    private String ID_FORM;

	    private String ID_TYPE;

	    private String ID_CATEGORY_CFG;

	    private String ID_CATEGORY;

	    private String TYPE;

	    public String getNAME()
	    {
	        return NAME;
	    }

	    public void setNAME(String NAME)
	    {
	        this.NAME = NAME;
	    }
	    
	    public String getID_EXTERNAL()
	    {
	        return ID_EXTERNAL;
	    }

	    public void setID_EXTERNAL (String ID_EXTERNAL)
	    {
	        this.ID_EXTERNAL = ID_EXTERNAL;
	    }

	    public HistoricsObject[] getHISTORICS ()
	    {
	        return HISTORICS;
	    }

	    public void setHISTORICS (HistoricsObject[] HISTORICS)
	    {
	        this.HISTORICS = HISTORICS;
	    }

	    public String getID_CARD ()
	    {
	        return ID_CARD;
	    }

	    public void setID_CARD (String ID_CARD)
	    {
	        this.ID_CARD = ID_CARD;
	    }

	    public String getFORM_NAME ()
	    {
	        return FORM_NAME;
	    }

	    public void setFORM_NAME (String FORM_NAME)
	    {
	        this.FORM_NAME = FORM_NAME;
	    }

	    public String getID_FORM ()
	    {
	        return ID_FORM;
	    }

	    public void setID_FORM (String ID_FORM)
	    {
	        this.ID_FORM = ID_FORM;
	    }

	    public String getID_TYPE ()
	    {
	        return ID_TYPE;
	    }

	    public void setID_TYPE (String ID_TYPE)
	    {
	        this.ID_TYPE = ID_TYPE;
	    }

	    public String getID_CATEGORY_CFG ()
	    {
	        return ID_CATEGORY_CFG;
	    }

	    public void setID_CATEGORY_CFG (String ID_CATEGORY_CFG)
	    {
	        this.ID_CATEGORY_CFG = ID_CATEGORY_CFG;
	    }

	    public String getID_CATEGORY ()
	    {
	        return ID_CATEGORY;
	    }

	    public void setID_CATEGORY (String ID_CATEGORY)
	    {
	        this.ID_CATEGORY = ID_CATEGORY;
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
	        return "ClassPojo [NAME = "+NAME+", HISTORICS = "+HISTORICS+", ID_CARD = "+ID_CARD+", FORM_NAME = "+FORM_NAME+", ID_FORM = "+ID_FORM+", ID_TYPE = "+ID_TYPE+", ID_CATEGORY_CFG = "+ID_CATEGORY_CFG+", ID_CATEGORY = "+ID_CATEGORY+", TYPE = "+TYPE+"]";
	    }
}

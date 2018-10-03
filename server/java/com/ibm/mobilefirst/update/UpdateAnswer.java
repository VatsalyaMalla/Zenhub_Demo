package com.ibm.mobilefirst.update;

import java.util.List;

public class UpdateAnswer {
	private List<String> value;

    private String type;

    private String typeId;

    public List<String> getValue ()
    {
        return value;
    }

    public void setValue (List<String> value)
    {
        this.value = value;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getTypeId ()
    {
        return typeId;
    }

    public void setTypeId (String typeId)
    {
        this.typeId = typeId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [value = "+value+", type = "+type+", typeId = "+typeId+"]";
    }
}

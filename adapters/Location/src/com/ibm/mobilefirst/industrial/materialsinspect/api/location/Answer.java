/**
 * 
 */
package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

/**
 * @author IBM
 *
 */
public class Answer {
	private String[] value;

    private String type;

    private String typeId;

    public String[] getValue ()
    {
        return value;
    }

    public void setValue (String[] value)
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


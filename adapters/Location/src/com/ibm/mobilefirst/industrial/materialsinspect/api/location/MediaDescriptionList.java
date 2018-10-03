package com.ibm.mobilefirst.industrial.materialsinspect.api.location;

public class MediaDescriptionList
{
    private String fileExtension;

    private String mediaId;

    private String refVendor;

    private Object newVersionAvailable;

    private String locationId;

    private String code;

    private String type;

    private String version;

    private String supportingDocumentType;

    private String title;

    private String fileName;

    private String mediaDescription;

    private String documentTag;

    private String displayName;

    private String documentStatus;

    public String getFileExtension ()
    {
        return fileExtension;
    }

    public void setFileExtension (String fileExtension)
    {
        this.fileExtension = fileExtension;
    }

    public String getMediaId ()
    {
        return mediaId;
    }

    public void setMediaId (String mediaId)
    {
        this.mediaId = mediaId;
    }

    public String getRefVendor ()
    {
        return refVendor;
    }

    public void setRefVendor (String refVendor)
    {
        this.refVendor = refVendor;
    }

    public Object getNewVersionAvailable ()
    {
        return newVersionAvailable;
    }

    public void setNewVersionAvailable (Object newVersionAvailable)
    {
        this.newVersionAvailable = newVersionAvailable;
    }

    public String getLocationId ()
    {
        return locationId;
    }

    public void setLocationId (String locationId)
    {
        this.locationId = locationId;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getVersion ()
    {
        return version;
    }

    public void setVersion (String version)
    {
        this.version = version;
    }

    public String getSupportingDocumentType ()
    {
        return supportingDocumentType;
    }

    public void setSupportingDocumentType (String supportingDocumentType)
    {
        this.supportingDocumentType = supportingDocumentType;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getFileName ()
    {
        return fileName;
    }

    public void setFileName (String fileName)
    {
        this.fileName = fileName;
    }

    public String getMediaDescription ()
    {
        return mediaDescription;
    }

    public void setMediaDescription (String mediaDescription)
    {
        this.mediaDescription = mediaDescription;
    }

    public String getDocumentTag ()
    {
        return documentTag;
    }

    public void setDocumentTag (String documentTag)
    {
        this.documentTag = documentTag;
    }

    public String getDisplayName ()
    {
        return displayName;
    }

    public void setDisplayName (String displayName)
    {
        this.displayName = displayName;
    }

    public String getDocumentStatus ()
    {
        return documentStatus;
    }

    public void setDocumentStatus (String documentStatus)
    {
        this.documentStatus = documentStatus;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [fileExtension = "+fileExtension+", mediaId = "+mediaId+", refVendor = "+refVendor+", newVersionAvailable = "+newVersionAvailable+", locationId = "+locationId+", code = "+code+", type = "+type+", version = "+version+", supportingDocumentType = "+supportingDocumentType+", title = "+title+", fileName = "+fileName+", mediaDescription = "+mediaDescription+", documentTag = "+documentTag+", displayName = "+displayName+", documentStatus = "+documentStatus+"]";
    }
}

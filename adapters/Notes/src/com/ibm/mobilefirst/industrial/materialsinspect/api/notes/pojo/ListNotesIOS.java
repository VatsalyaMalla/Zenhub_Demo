package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "idNote", "idUser", "nameUser", "creationDate", "plannedDate", "idLocation", "nameLocation", "marked", "priority", "title", "text", "listUsers",
		"listComments" })
public class ListNotesIOS {

	@JsonProperty("idNote")
	private Integer idNote;
	@JsonProperty("idUser")
	private Integer idUser;
	@JsonProperty("nameUser")
	private String nameUser;
	@JsonProperty("creationDate")
	private String creationDate;
	@JsonProperty("plannedDate")
	private String plannedDate;
	@JsonProperty("idLocation")
	private Integer idLocation;
	@JsonProperty("nameLocation")
	private String nameLocation;
	@JsonProperty("marked")
	private Boolean marked;
	@JsonProperty("priority")
	private Integer priority;
	@JsonProperty("title")
	private String title;
	@JsonProperty("text")
	private String text;
	@JsonProperty("listUsers")
	private List<ListUserIOS> listUsers = null;
	@JsonProperty("listComments")
	private List<ListCommentsIOS> listComments = null;

	@JsonProperty("idNote")
	public Integer getIDNOTE() {
		return idNote;
	}

	@JsonProperty("idNote")
	public void setIDNOTE(Integer idNote) {
		this.idNote = idNote;
	}

	@JsonProperty("idUser")
	public Integer getIDUSER() {
		return idUser;
	}

	@JsonProperty("idUser")
	public void setIDUSER(Integer idUser) {
		this.idUser = idUser;
	}

	@JsonProperty("nameUser")
	public String getNAMEUSER() {
		return nameUser;
	}

	@JsonProperty("nameUser")
	public void setNAMEUSER(String nameUser) {
		this.nameUser = nameUser;
	}

	@JsonProperty("creationDate")
	public String getCREATIONDATE() {
		return creationDate;
	}

	@JsonProperty("creationDate")
	public void setCREATIONDATE(String creationDate) {
		this.creationDate = creationDate;
	}

	@JsonProperty("plannedDate")
	public String getPLANNEDDATE() {
		return plannedDate;
	}

	@JsonProperty("plannedDate")
	public void setPLANNEDDATE(String plannedDate) {
		this.plannedDate = plannedDate;
	}

	@JsonProperty("idLocation")
	public Integer getIDLOCATION() {
		return idLocation;
	}

	@JsonProperty("idLocation")
	public void setIDLOCATION(Integer idLocation) {
		this.idLocation = idLocation;
	}

	@JsonProperty("nameLocation")
	public String getNAMELOCATION() {
		return nameLocation;
	}

	@JsonProperty("nameLocation")
	public void setNAMELOCATION(String nameLocation) {
		this.nameLocation = nameLocation;
	}

	@JsonProperty("marked")
	public Boolean getMARKED() {
		return marked;
	}

	@JsonProperty("marked")
	public void setMARKED(Boolean marked) {
		this.marked = marked;
	}

	@JsonProperty("priority")
	public Integer getPRIORITY() {
		return priority;
	}

	@JsonProperty("priority")
	public void setPRIORITY(Integer priority) {
		this.priority = priority;
	}

	@JsonProperty("title")
	public String getTITLE() {
		return title;
	}

	@JsonProperty("title")
	public void setTITLE(String title) {
		this.title = title;
	}

	@JsonProperty("text")
	public String getTEXT() {
		return text;
	}

	@JsonProperty("text")
	public void setTEXT(String text) {
		this.text = text;
	}

	@JsonProperty("listUsers")
	public List<ListUserIOS> getLISTUSERS() {
		return listUsers;
	}

	@JsonProperty("listUsers")
	public void setLISTUSERS(List<ListUserIOS> listUsers) {
		this.listUsers = listUsers;
	}

	@JsonProperty("listComments")
	public List<ListCommentsIOS> getLISTCOMMENTS() {
		return listComments;
	}

	@JsonProperty("listComments")
	public void setLISTCOMMENTS(List<ListCommentsIOS> listComments) {
		this.listComments = listComments;
	}

}

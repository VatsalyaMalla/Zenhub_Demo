package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "ID_NOTE", "ID_USER", "NAME_USER", "CREATION_DATE", "PLANNED_DATE", "ID_LOCATION", "NAME_LOCATION", "MARKED", "PRIORITY", "TITLE", "TEXT", "LIST_USERS",
		"LIST_COMMENTS" })
public class LISTFROM {

	@JsonProperty("ID_NOTE")
	private Integer idNote;
	@JsonProperty("ID_USER")
	private Integer idUser;
	@JsonProperty("NAME_USER")
	private String nameUser;
	@JsonProperty("CREATION_DATE")
	private String creationDate;
	@JsonProperty("PLANNED_DATE")
	private String plannedDate;
	@JsonProperty("ID_LOCATION")
	private Integer idLocation;
	@JsonProperty("NAME_LOCATION")
	private String nameLocation;
	@JsonProperty("MARKED")
	private Boolean marked;
	@JsonProperty("PRIORITY")
	private Integer priority;
	@JsonProperty("TITLE")
	private String title;
	@JsonProperty("TEXT")
	private String text;
	@JsonProperty("LIST_USERS")
	private List<LISTUSER> listUsers = null;
	@JsonProperty("LIST_COMMENTS")
	private List<LISTCOMMENT> listComments = null;

	@JsonProperty("ID_NOTE")
	public Integer getIDNOTE() {
		return idNote;
	}

	@JsonProperty("ID_NOTE")
	public void setIDNOTE(Integer idNote) {
		this.idNote = idNote;
	}

	@JsonProperty("ID_USER")
	public Integer getIDUSER() {
		return idUser;
	}

	@JsonProperty("ID_USER")
	public void setIDUSER(Integer idUser) {
		this.idUser = idUser;
	}

	@JsonProperty("NAME_USER")
	public String getNAMEUSER() {
		return nameUser;
	}

	@JsonProperty("NAME_USER")
	public void setNAMEUSER(String nameUser) {
		this.nameUser = nameUser;
	}

	@JsonProperty("CREATION_DATE")
	public String getCREATIONDATE() {
		return creationDate;
	}

	@JsonProperty("CREATION_DATE")
	public void setCREATIONDATE(String creationDate) {
		this.creationDate = creationDate;
	}

	@JsonProperty("PLANNED_DATE")
	public String getPLANNEDDATE() {
		return plannedDate;
	}

	@JsonProperty("PLANNED_DATE")
	public void setPLANNEDDATE(String plannedDate) {
		this.plannedDate = plannedDate;
	}

	@JsonProperty("ID_LOCATION")
	public Integer getIDLOCATION() {
		return idLocation;
	}

	@JsonProperty("ID_LOCATION")
	public void setIDLOCATION(Integer idLocation) {
		this.idLocation = idLocation;
	}

	@JsonProperty("NAME_LOCATION")
	public String getNAMELOCATION() {
		return nameLocation;
	}

	@JsonProperty("NAME_LOCATION")
	public void setNAMELOCATION(String nameLocation) {
		this.nameLocation = nameLocation;
	}

	@JsonProperty("MARKED")
	public Boolean getMARKED() {
		return marked;
	}

	@JsonProperty("MARKED")
	public void setMARKED(Boolean marked) {
		this.marked = marked;
	}

	@JsonProperty("PRIORITY")
	public Integer getPRIORITY() {
		return priority;
	}

	@JsonProperty("PRIORITY")
	public void setPRIORITY(Integer priority) {
		this.priority = priority;
	}

	@JsonProperty("TITLE")
	public String getTITLE() {
		return title;
	}

	@JsonProperty("TITLE")
	public void setTITLE(String title) {
		this.title = title;
	}

	@JsonProperty("TEXT")
	public String getTEXT() {
		return text;
	}

	@JsonProperty("TEXT")
	public void setTEXT(String text) {
		this.text = text;
	}

	@JsonProperty("LIST_USERS")
	public List<LISTUSER> getLISTUSERS() {
		return listUsers;
	}

	@JsonProperty("LIST_USERS")
	public void setLISTUSERS(List<LISTUSER> listUsers) {
		this.listUsers = listUsers;
	}

	@JsonProperty("LIST_COMMENTS")
	public List<LISTCOMMENT> getLISTCOMMENTS() {
		return listComments;
	}

	@JsonProperty("LIST_COMMENTS")
	public void setLISTCOMMENTS(List<LISTCOMMENT> listComments) {
		this.listComments = listComments;
	}

}

package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "ID_COMMENT", "ID_USER", "NAME_USER", "TEXT", "CREATION_DATE" })
public class LISTCOMMENT {

	@JsonProperty("ID_COMMENT")
	private Integer idComment;
	@JsonProperty("ID_USER")
	private Integer idUser;
	@JsonProperty("NAME_USER")
	private String nameUser;
	@JsonProperty("TEXT")
	private String text;
	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("ID_COMMENT")
	public Integer getIDCOMMENT() {
		return idComment;
	}

	@JsonProperty("ID_COMMENT")
	public void setIDCOMMENT(Integer idComment) {
		this.idComment = idComment;
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

	@JsonProperty("TEXT")
	public String getTEXT() {
		return text;
	}

	@JsonProperty("TEXT")
	public void setTEXT(String text) {
		this.text = text;
	}

	@JsonProperty("CREATION_DATE")
	public String getCREATIONDATE() {
		return creationDate;
	}

	@JsonProperty("CREATION_DATE")
	public void setCREATIONDATE(String creationDate) {
		this.creationDate = creationDate;
	}

}

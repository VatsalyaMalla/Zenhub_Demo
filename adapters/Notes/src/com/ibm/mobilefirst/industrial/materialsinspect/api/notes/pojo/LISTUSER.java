package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "ID", "ID_USER", "NAME_USER" })
public class LISTUSER {

	@JsonProperty("ID")
	private Integer id;
	@JsonProperty("ID_USER")
	private Integer idUser;
	@JsonProperty("NAME_USER")
	private String nameUser;

	@JsonProperty("ID")
	public Integer getID() {
		return id;
	}

	@JsonProperty("ID")
	public void setID(Integer id) {
		this.id = id;
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

}

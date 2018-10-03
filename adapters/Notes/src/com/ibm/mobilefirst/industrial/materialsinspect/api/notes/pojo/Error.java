package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "message", "level" })
public class Error {

	@JsonProperty("id")
	private String id;
	@JsonProperty("message")
	private String message;
	@JsonProperty("level")
	private String level;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("level")
	public String getLevel() {
		return level;
	}

	@JsonProperty("level")
	public void setLevel(String level) {
		this.level = level;
	}

}

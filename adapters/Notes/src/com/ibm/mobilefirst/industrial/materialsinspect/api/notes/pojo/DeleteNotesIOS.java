package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "errors", "data" })
public class DeleteNotesIOS {

	@JsonProperty("errors")
	private List<Error> errors = null;
	@JsonProperty("data")
	private List<DeleteNotesData> data = null;

	@JsonProperty("errors")
	public List<Error> getErrors() {
		return errors;
	}

	@JsonProperty("errors")
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	@JsonProperty("data")
	public List<DeleteNotesData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<DeleteNotesData> data) {
		this.data = data;
	}

}

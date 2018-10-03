package com.ibm.mobilefirst.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "errors", "data" })
public class BaseResponse {

	@Override
	public String toString() {
		return "BaseResponse [errors=" + errors + ", data=" + data + "]";
	}

	@JsonProperty("errors")
	private List<ErrorElement> errors = null;
	@JsonProperty("data")
	private List<DataElement> data = null;

	@JsonProperty("errors")
	public List<ErrorElement> getErrors() {
		return errors;
	}

	@JsonProperty("errors")
	public void setErrors(List<ErrorElement> errors) {
		this.errors = errors;
	}

	@JsonProperty("data")
	public List<DataElement> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<DataElement> data) {
		this.data = data;
	}

}

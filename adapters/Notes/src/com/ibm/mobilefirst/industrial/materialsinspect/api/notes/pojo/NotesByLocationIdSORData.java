package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "LIST_FROM", "LIST_TO" })
public class NotesByLocationIdSORData {

	@JsonProperty("LIST_FROM")
	private List<LISTFROM> listFrom = null;
	@JsonProperty("LIST_TO")
	private List<LISTFROM> listTo = null;

	@JsonProperty("LIST_FROM")
	public List<LISTFROM> getLISTFROM() {
		return listFrom;
	}

	@JsonProperty("LIST_FROM")
	public void setLISTFROM(List<LISTFROM> listFrom) {
		this.listFrom = listFrom;
	}

	@JsonProperty("LIST_TO")
	public List<LISTFROM> getLISTTO() {
		return listTo;
	}

	@JsonProperty("LIST_TO")
	public void setLISTTO(List<LISTFROM> listTo) {
		this.listTo = listTo;
	}

}

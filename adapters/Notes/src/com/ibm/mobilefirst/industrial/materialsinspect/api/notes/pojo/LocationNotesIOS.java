package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "marked", "priority", "title", "text", "userId", "userName", "creationDate", "noteId", "plannedDate", "locationId", "locationName", "userList", "listcomments" })
public class LocationNotesIOS {

	@JsonProperty("priority")
	private Integer priority;
	@JsonProperty("ownerId")
	private String ownerId;
	@JsonProperty("ownerName")
	private String ownerName;
	@JsonProperty("creationDate")
	private Object creationDate;
	@JsonProperty("noteId")
	private String noteId;
	@JsonProperty("plannedDate")
	private String plannedDate;
	@JsonProperty("locationId")
	private String locationId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotesTitle() {
		return notesTitle;
	}

	public void setNotesTitle(String notesTitle) {
		this.notesTitle = notesTitle;
	}

	public String getNotesDescription() {
		return notesDescription;
	}

	public void setNotesDescription(String notesDescription) {
		this.notesDescription = notesDescription;
	}

	@JsonProperty("locationName")
	private String locationName;
	@JsonProperty("userList")
	private List<ListUserIOS> userList = null;
	@JsonProperty("listcomments")
	private List<ListCommentsIOS> listcomments = null;
	@JsonProperty("status")
	private String status;
	@JsonProperty("notesTitle")
	private String notesTitle;
	@JsonProperty("notesDescription")
	private String notesDescription;

	@JsonProperty("priority")
	public Integer getPriority() {
		return priority;
	}

	@JsonProperty("priority")
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@JsonProperty("creationDate")
	public Object getCreationDate() {
		return creationDate;
	}

	@JsonProperty("creationDate")
	public void setCreationDate(Object creationDate) {
		this.creationDate = creationDate;
	}

	@JsonProperty("noteId")
	public String getNoteId() {
		return noteId;
	}

	@JsonProperty("noteId")
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	@JsonProperty("plannedDate")
	public String getPlannedDate() {
		return plannedDate;
	}

	@JsonProperty("plannedDate")
	public void setPlannedDate(String plannedDate) {
		this.plannedDate = plannedDate;
	}

	@JsonProperty("locationId")
	public String getLocationId() {
		return locationId;
	}

	@JsonProperty("locationId")
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	@JsonProperty("locationName")
	public String getLocationName() {
		return locationName;
	}

	@JsonProperty("locationName")
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@JsonProperty("userList")
	public List<ListUserIOS> getUserList() {
		return userList;
	}

	@JsonProperty("userList")
	public void setUserList(List<ListUserIOS> userList) {
		this.userList = userList;
	}

	@JsonProperty("listcomments")
	public List<ListCommentsIOS> getListcomments() {
		return listcomments;
	}

	@JsonProperty("listcomments")
	public void setListcomments(List<ListCommentsIOS> listcomments) {
		this.listcomments = listcomments;
	}

}

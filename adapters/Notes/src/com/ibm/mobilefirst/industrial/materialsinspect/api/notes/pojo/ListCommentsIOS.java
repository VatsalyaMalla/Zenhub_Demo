package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "commentId", "userId", "userName", "text", "creationDate" })
public class ListCommentsIOS {

	@JsonProperty("commentId")
	private String commentId;
	@JsonProperty("userId")
	private String userId;

	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	@JsonProperty("userName")
	private String userName;
	@JsonProperty("text")
	private String text;
	@JsonProperty("creationDate")
	private String creationDate;
	@JsonProperty("noteId")
	private String noteId;

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

}

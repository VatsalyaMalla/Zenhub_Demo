package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.pojo;

public class NotesUserByLocationId {

	private String NAME_USER;

	private java.lang.Object ID;

	private java.lang.Object ID_USER;

	public String getUserName() {
		return NAME_USER;
	}

	public void setUserName(String NAME_USER) {
		this.NAME_USER = NAME_USER;
	}

	public java.lang.Object getId() {
		return ID;
	}

	public void setId(java.lang.Object ID) {
		this.ID = ID;
	}

	public java.lang.Object getUserId() {
		return ID_USER;
	}

	public void setUserId(java.lang.Object ID_USER) {
		this.ID_USER = ID_USER;
	}

	@Override
	public String toString() {
		return "ClassPojo [NAME_USER = " + NAME_USER + ", ID = " + ID + ", ID_USER = " + ID_USER + "]";
	}
}

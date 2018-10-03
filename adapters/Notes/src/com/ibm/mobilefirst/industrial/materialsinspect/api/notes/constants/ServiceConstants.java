package com.ibm.mobilefirst.industrial.materialsinspect.api.notes.constants;

public class ServiceConstants {

	public static final String ADAPTER_PROPERTY_FILE = "materialinspect.adapter.properties";
	public static final String DATA_ADAPTER_NAME = "notesDataAdapter";

	public static final int LOCATION_ID_CHECK_CODE = 1000;
	public static final int ERROR_CODE_OK = 200;
	public static final int ERROR_CODE_MISSING = 404;
	public static final int ERROR_CODE_ERROR = 500;

	public static final String SUCCESS_MESSAGE = "SUCCESS";

	public static final String ERROR_CODE_NODATAFOUND = null;

	public static final String USER_ID_CHECK_MESSAGE = "1000";
	public static final String ERROR_CODE_UNDEFINEDISSUE = "1000";

	public static final String ERROR_LOCATION_NOTFOUND = "Invalid Location Id";
	public static final String ERROR_LOCATION_ID_NULL = "Location Id is null or empty";
	public static final String LOCATION_ID_PATTERN = "[0-9]+";
	public static final String ERROR_MESSAGE_ERROR = "Internal Server Error";
	public static final String SOR_LOCATIONID_MESSAGE = "idLocation must be numeric and positive";
	public static final String GET_NOTES = "getNotes";
	public static final String NOTES_INFO_EMPTY = "SOR response is empty for the given locationId.";
	public static final String NOTES_ID_EMPTY = "Notes id cannot be empty or null in SOR.";
	public static final String ERROR_MESSAGE_INVALIDREQUESTBODY = "Invalid Request Body/Request Body is mandatory";
	public static final String DATA = "data";
	public static final String ERRORS = "errors";
	public static final String VPN = "VPN";
	public static final String VPN_NOT_CONNECTED = "VPN not connected";
	public static final String NOTES = "notes";

	public static final String LEVEL_INFO = "INFO";
	public static final String LEVEL_WARNING = "WARNING";
	public static final String LEVEL_ERROR = "ERROR";

	public static final String PUT = "PUT";
	public static final String POST = "POST";

	public static final String NOTES_ID_URL = "notes_id_url";
	public static final String LOCATION_ID = "locationId";
	public static final String LOCATION_NAME = "locationName";
	public static final String POST_NOTES = "post_notes";
	public static final String UPDATE_NOTES_URL = "update_notes_url";
	public static final String POSTING_NOTES = "postNotes";
	public static final String UPDATE_NOTES = "updateNotes";
	public static final String GET_STATUS = "getStatus";
	public static final String EXCEPTION_TYPE = "ExceptionType";
	public static final String CLASS_NAME = "ClassName";
	public static final String OPEN = "Open";
	public static final String CLOSED = "Closed";
	public static final String ID = "id";
	public static final String NOTES_ID = "noteId";
	public static final String OWNER_ID = "ownerId";
	public static final String OWNER_NAME = "ownerName";
	public static final String ID_APP_GENERATED = "ID_APP_GENERATED";
	public static final String APP_GENERATED_ID = "appGeneratedId";
	public static final String STATUS = "status";
	public static final String NOTES_TITLE = "notesTitle";
	public static final String NOTES_DESCRIPTION = "notesDescription";
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String USER_LIST = "userList";
	public static final String GET_NOTES_LIST = "getNotesList";
	public static final String SEND_IOS_RESPONSE_FOR_UPDATE_NOTES = "sendIOSResponseForUpdateNotes";
	public static final String GET_RESPONSE_OBJECT_IN_UPDATE_NOTES = "getResponseObject in update notes.";
	public static final String POST_NOTES_OUTPUT = "No data from SOR response after post notes.";

	public static final String ID_NOTE = "ID_NOTE";
	public static final String ID_USER = "ID_USER";
	public static final String NAME_USER = "NAME_USER";
	public static final String CREATION_DATE = "CREATION_DATE";
	public static final String PLANNED_DATE = "PLANNED_DATE";
	public static final String ID_LOCATION = "ID_LOCATION";
	public static final String NAME_LOCATION = "NAME_LOCATION";
	public static final String MARKED = "MARKED";
	public static final String PRIORITY = "PRIORITY";
	public static final String TITLE = "TITLE";
	public static final String TEXT = "TEXT";
	public static final String LIST_USERS = "LIST_USERS";
	public static final String NEW_ID = "ID";
	public static final String LIST_COMMENTS = "LIST_COMMENTS";
	public static final String ID_COMMENT = "ID_COMMENT";

	public static final String ID_NOTE_IOS = "noteId";
	public static final String ID_USER_IOS = "userId";
	public static final String NAME_USER_IOS = "userName";
	public static final String CREATION_DATE_IOS = "creationDate";
	public static final String PLANNED_DATE_IOS = "plannedDate";
	public static final String ID_LOCATION_IOS = "locationId";
	public static final String NAME_LOCATION_IOS = "locationName";
	public static final String MARKED_IOS = "status";
	public static final String PRIORITY_IOS = "priority";
	public static final String TITLE_IOS = "notesTitle";
	public static final String TEXT_IOS = "text";
	public static final String NOTES_DESCRIPTION_TEXT = "notesDescription";
	public static final String LIST_USERS_IOS = "userList";
	public static final String NEW_ID_IOS = "id";
	public static final String LIST_COMMENTS_IOS = "listcomments";
	public static final String ID_COMMENT_IOS = "commentId";

	// getNotesForLocation constants
	public static final String NOTES_LOCATION_ID_URL = "notes_locationid_url";
	public static final String GET_IOS_USER_LIST = "getIOSUserList";
	public static final String GET_IOS_COMMENT_LIST = "getIOSCommentsList";
	public static final String GET_SOR_REQUEST_LIST = "getSORRequestList";
	public static final String GET_IOS_RESPONSE = "getIOSResponse";
	public static final String GET_COMMENT_LIST_RESPONSE = "getCommentListResponse";
	public static final String GET_USER_LIST_RESPONSE = "getUserListResponse";
	public static final String SEND_IOS_RESPONSE = "sendIOSResponse";
	public static final String NOTES_NOTFOUND = "No notes found for the given location id";
	public static final String EMPTY_SOR_RESPONSE = "Empty response from SOR while fetching notes";
	public static final String GET_NOTES_ERROR = "Error occured while fetching notes";
	public static final String LOCATION_NOTES = "LOCATION_NOTES";
	public static final String NOTES_STATUS_CLOSED = "Closed";
	public static final String NOTES_STATUS_OPEN = "Open";

	// delete notes constants
	public static final String DELETE_NOTES_URL = "delete_notes_url";
	public static final String DELETE_NOTES = "deleteNotes";
	public static final String DELETE_NOTES_NO_RESPONSE = "No SOR response for delete notes";
	public static final String DELETE_NOTES_ERROR = "Error occured while deleting notes";
	public static final String ERROR_CODE_NO_RESPONSE = "2000";
	public static final String DELETE_NOTES_SOR_FAIL = "SOR delete failed for delete notes";
	public static final String SUCCESS_ID = "200";
	public static final String DELETE_NOTES_SUCCESS = "Successfully deleted notes";
	public static final String DELETE_NOTES_SOR_TRUE = "true";
	public static final String DELETE_NOTES_SOR_FALSE = "false";
	public static final String INVALID_NOTE_ID = "Invalid noteId for delete notes";

}

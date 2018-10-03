package com.ibm.mobilefirst.industrial.materialsinspect.api.user.constants;



public class ServiceConstants {

	public static final String API_VERSION = "v1.0";
	public static final String SUCCESS_CODE = "0";
	public static final String SUCCESS_MESSAGE = "SUCCESS";
	public static final String ERROR_CODE_1 = "1";
	public static final String ERROR_CODE_2 = "2";
	
	public static final int ERROR_CODE_OK = 200;
	public static final int ERROR_CODE_MISSING = 404;
	public static final int ERROR_CODE_ERROR = 500;
	
	public static final String ERROR_MESSAGE_ERROR = "Internal Server Error";
	public static final String ERROR_MESSAGE_NOTFOUND = "Not Found";
	
	public static final String ERROR_CODE_LOGIN = "1001";
	public static final String ERROR_CODE_LOGOUT = "1002";
	public static final String ERROR_CODE_LANGUAGE = "1003";
	public static final String ERROR_CODE_INVALIDREQUEST_METADATA = "2001";
	public static final String ERROR_CODE_UNAUTHORIZEDREQUEST_METADATA = "2002";
	public static final String ERROR_CODE_UNACCEPTEDPOST_METADATA = "2003";
	public static final String ERROR_CODE_UNDEFINEDISSUE_METADATA = "2000";
	public static final String ERROR_CODE_INVALIDREQUEST = "3001";
	public static final String ERROR_CODE_DEMANDINGREQUEST = "3002";
	public static final String ERROR_CODE_INVALIDSYNTAXPOST = "3003";
	public static final String ERROR_CODE_CONFLICTPOST = "3004";
	public static final String ERROR_CODE_UNAUTHORIZEDREQUEST = "3005";
	public static final String ERROR_CODE_INVALIDUSER = "3007";
	
	
	public static final String ERROR_MESSAGE_LOGIN = "Login Failure";
	
	public static final String ERROR_MESSAGE_LOGOUT = "Logout Failure";
	public static final String ERROR_MESSAGE_LANGUAGE = "Unsupported language";
	public static final String ERROR_MESSAGE_UNDEFINEDISSUE = "Undefined Issue";
	public static final String ERROR_MESSAGE_INVALIDREQUEST_METADATA = "Invalid request for Metadata";
	public static final String ERROR_MESSAGE_UNAUTHORIZEDREQUEST_METADATA = "Unauthorized request for Metadata";
	public static final String ERROR_MESSAGE_UNACCEPTEDPOST_METADATA = "Unable to accept posted Metadata";
	public static final String ERROR_MESSAGE_UNDEFINEDISSUE_METADATA = "Undefined Metadata related Issue";
	public static final String ERROR_MESSAGE_INVALIDREQUEST = "Invalid request for User";
	public static final String ERROR_MESSAGE_INVALIDAPPNAME = "Invalid App-Name";
	public static final String ERROR_MESSAGE_INVALIDFIELDS = "Invalid request offset and limit are mandatory";
	public static final String ERROR_MESSAGE_INVALIDLIMIT = "Invalid limit";
	public static final String ERROR_MESSAGE_INVALIDOFFSET = "Invalid offset";
	public static final String ERROR_MESSAGE_DEMANDINGREQUEST = "Demanding request for User";
	public static final String ERROR_MESSAGE_INVALIDSYNTAXPOST = "Invalid syntax in posted User";
	public static final String ERROR_MESSAGE_INVALIDUSER = "Invalid User Id";
	public static final String ERROR_MESSAGE_INVALIDREQUESTBODY = "Invalid Request Body/Request Body is mandatory";
	public static final String ERROR_MESSAGE_CONFLICTPOST = "Conflict in posted User";
	public static final String ERROR_MESSAGE_UNAUTHORIZEDREQUEST = "Unauthorized request for User";
	public static final String ERROR_MESSAGE_NODATAFOUND = "User not found";
	public static final String GET_USER = "getUserById";
	
	public static final String LEVEL_INFO = "INFO";
	public static final String LEVEL_WARNING = "WARNING";
	public static final String LEVEL_ERROR = "ERROR";
	
	// URL
	public static final String USER_INFO_URL = "user_info_url";
	public static final String USER_REQUEST_URL_1 = "user_request_url_1";
	public static final String USER_REQUEST_URL_2 = "user_request_url_2";
	public static final String GET_LOCATION_BY_ID = "get_location_by_id";
	public static final String IS_REQUEST_N = "is_request_n";
	public static final String ACCEPT_REQUEST_URL = "accept_request_url";
	public static final String RESHCEDULE_REQUEST_URL = "reshcedule_request_url";
	public static final String RESHCEDULE_VISIT_REQUEST_URL = "reshcedule_visit_request_url";
	public static final String GET_VISIT_URL = "get_visit_url";
	public static final String GET_LOCATION_VISIT_URL = "get_location_visit_url";
	public static final String GET_EVENT_VISIT_URL = "get_event_visit_url";
	public static final String GET_USER_VISIT_URL = "get_user_visit_url";
	public static final String VISIT_SKELETON = "visit_skeleton";
	public static final String SINGLE_VISIT = "single_visit";
	public static final String IOS_VISIT = "ios_visit";
	public static final String POST_LOGOUT = "post_logout";
	public static final String DOCUMENT_ORDER = "document_order";
	public static final String DOCUMENT_VENDOR = "document_vendor";
	public static final String SERIALIZED_OBJECT = "serialized_object";
	
	//JSON fields
	public static final String SIZE = "size";
	public static final String OFFSET = "offset";
	public static final String PAGINATION = "pagination";
	public static final String DATA = "data";
	public static final String DATE = "date";
	public static final String END_DATE = "endDate";
	public static final String ERRORS = "errors";
	public static final String STREET_ADDRESS = "streetAddress";
	public static final String DESCRIPTOR_2_VALUE = "descriptor2Value";
	public static final String DESCRIPTOR_2_LABEL = "descriptor2Label";
	public static final String DESCRIPTOR_1_LABEL = "descriptor1Label";
	public static final String DESCRIPTOR_1_VALUE = "descriptor1Value";
	public static final String EXPEDITING_LEVEL = "Expediting Level";
	public static final String INSPECTION_LEVEL = "Inspection Level";
	public static final String VENDOR_NAME = "vendorName";
	public static final String STATUS = "status";
	public static final String PROPOSED_DATE = "proposedDate";
	public static final String VISIT_ID = "visitId";
	public static final String DISPLAY_NAME = "displayName";
	public static final String MANUFACTURER_NAME = "manufacturerName";
	public static final String DURATION_UNITS = "durationUnits";
	public static final String EXPECTED_DURATION = "expectedDuration";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PHONE = "phone";
	public static final String PHOTO = "photo";
	public static final String USER_ID = "userId";
	public static final String EMAIL = "email";
	public static final String COMPANY = "company";
	public static final String IBM = "IBM";
	public static final String ROLE = "role";
	public static final String COMMENTS = "comments";
	public static final String INSPECTOR = "Inspector";
	public static final String ADDITIONAL_INFORMATION = "additionalInformation";
	public static final String REQUIRED ="required";
	public static final String EXCEPTION_TYPE = "ExceptionType";
	public static final String VPN = "VPN";
	public static final String VPN_NOT_CONNECTED = "VPN not connected";
	
	// Error Messages
	public static final String USER_INFO_EMPTY ="SOR response is empty for the given userId.";
	public static final String EMPTY_REQUEST_MESSAGE ="The app request is empty.";
	public static final String SOR_USERID_MESSAGE = "idUser must be numeric and positive";
	public static final String RESPONSE_MESSAGE_EMPTY ="No message from SOR service after posting the request for accept/reschedule.";
	
	// Mandatory parameters missing code
	public static final int USER_ID_CHECK_CODE =500;
	public static final int USER_ID_SOR =1001;
	public static final int EMPTY_REQUEST_CODE =1002;
	
	
	public static final String ERROR_CODE_UNDEFINEDISSUE = "1002";
	public static final String SOR_USERID_CODE = "E10";
	
	// Mandatory parameters missing message
	public static final String USER_ID_CHECK_MESSAGE ="1000";
	
	public static final String ADAPTER_NAME="userAdapter";
	public static final String GET_USER_INFO_LIST="getUserInfoList";
	public static final String USER_OBJECT_FOR_REQUEST = "getUserObjectForRequestById";
	public static final String REQUEST_BY_ID = "getRequestById";
	
	public static final String ADAPTER_PROPERTY_FILE="materialinspect.adapter.properties";
	
	
	//added by development team
	public static final String DATA_ADAPTER_NAME="userDataAdapter";
	public static final String ERROR_CODE_NODATAFOUND = null;
	public static final String ERROR_MESSAGE_NOTFOUND_PRESENTATION = "No data Found";
	public static final String ERROR_USER_NOTFOUND = "Invalid User Id";
	public static final String ERROR_USERDATA_NOTFOUND = "No results found for this user and search query";
	
}

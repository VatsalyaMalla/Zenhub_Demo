package com.ibm.mobilefirst.industrial.materialsinspect.api.visit.constants;

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
	public static final String ERROR_CODE_UNDEFINEDISSUE = "1000";
	public static final String ERROR_CODE_INVALIDREQUEST_METADATA = "2001";
	public static final String ERROR_CODE_UNAUTHORIZEDREQUEST_METADATA = "2002";
	public static final String ERROR_CODE_UNACCEPTEDPOST_METADATA = "2003";
	public static final String ERROR_CODE_UNDEFINEDISSUE_METADATA = "2000";
	public static final String ERROR_CODE_INVALIDREQUEST = "3001";
	public static final String ERROR_CODE_DEMANDINGREQUEST = "3002";
	public static final String ERROR_CODE_INVALIDSYNTAXPOST = "3003";
	public static final String ERROR_CODE_CONFLICTPOST = "3004";
	public static final String ERROR_CODE_UNAUTHORIZEDREQUEST = "3005";
	public static final String ERROR_CODE_INVALIDREQUESTBODY = "3006";
	public static final String ERROR_CODE_INVALIDWORKORDER = "3007";
	public static final String ERROR_NODATAFOUND = "3008";
	public static final String ERROR_CODE_NODATAFOUND = "";
	
	public static final String ERROR_MESSAGE_LOGIN = "Login Failure";
	
	public static final String ERROR_MESSAGE_LOGOUT = "Logout Failure";
	public static final String ERROR_MESSAGE_LANGUAGE = "Unsupported language";
	public static final String ERROR_MESSAGE_UNDEFINEDISSUE = "Undefined Issue";
	public static final String ERROR_MESSAGE_INVALIDREQUEST_METADATA = "Invalid request for Metadata";
	public static final String ERROR_MESSAGE_UNAUTHORIZEDREQUEST_METADATA = "Unauthorized request for Metadata";
	public static final String ERROR_MESSAGE_UNACCEPTEDPOST_METADATA = "Unable to accept posted Metadata";
	public static final String ERROR_MESSAGE_UNDEFINEDISSUE_METADATA = "Undefined Metadata related Issue";
	public static final String ERROR_MESSAGE_INVALIDREQUEST = "Invalid request for Work Order";
	public static final String ERROR_MESSAGE_INVALIDAPPNAME = "Invalid App-Name";
	public static final String ERROR_MESSAGE_INVALIDFIELDS = "Invalid request offset and limit are mandatory";
	public static final String ERROR_MESSAGE_INVALIDLIMIT = "Invalid limit";
	public static final String ERROR_MESSAGE_INVALIDOFFSET = "Invalid offset";
	public static final String ERROR_MESSAGE_DEMANDINGREQUEST = "Demanding request for Work Order";
	public static final String ERROR_MESSAGE_INVALIDSYNTAXPOST = "Invalid syntax in posted Work Order";
	public static final String ERROR_MESSAGE_INVALIDWORKORDER = "Invalid Work Order Id";
	public static final String ERROR_MESSAGE_INVALIDREQUESTBODY = "Invalid Request Body/Request Body is mandatory";
	public static final String ERROR_MESSAGE_CONFLICTPOST = "Conflict in posted Work Order";
	public static final String ERROR_MESSAGE_UNAUTHORIZEDREQUEST = "Unauthorized request for Work Order";
	public static final String ERROR_MESSAGE_NODATAFOUND = "Not data found";
	
	public static final String LEVEL_INFO = "INFO";
	public static final String LEVEL_WARNING = "WARNING";
	public static final String LEVEL_ERROR = "ERROR";
	
	public static final String ADAPTER_NAME="visitAdapter";
	
	public static final String ADAPTER_PROPERTY_FILE="materialinspect.adapter.properties";
	
	
	//added by development team
	public static final String DATA_ADAPTER_NAME="visitDataAdapter";
	
	// Exception
	public static final String METHOD_NAME="getSupportTeamMembersByVisitId";
	public static final String TEAM_ARRAY_FROM_STAFF="getTeamArrayFormStaff";
	public static final String TEAM_ARRAY_FROM_VENDOR = "getTeamArrayFromVendor";
	public static final String TEAM_ARRAY_FROM_EXPEDITION="getTeamArrayFromExpedition";
	public static final String TEAM_ARRAY_FROM_INSPECTION="getTeamArrayFromInspection";
	public static final String TEAM_ARRAY_FROM_CONTACT="getTeamArrayFromContact";
	public static final String RESPONSE_LIST_OF_VISIT="getResponseListOfVisit";
	public static final String SET_NULL = "setNullStringValueTo";
	public static final String SET_EMPTY = "setEmptyStringValueTo";
	public static final String VPN="VPN";
	public static final String VPN_NOT_CONNECTED="VPN not connected";
	public static final String EXCEPTION="Exception in ";
	
	// JSON fields
	public static final String SIZE = "size";
	public static final String OFFSET = "offset";
	public static final String PAGINATION = "pagination";
	public static final String DATA = "data";
	public static final String ERRORS = "errors";
	public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PHONE = "phone";
    public static final String ROLE = "role";
    public static final String USER_ID = "userId";
    public static final String COMPANY = "company";
    public static final String PHOTO = "photo";
    public static final String VENDOR ="Vendor";
    public static final String COORDINATOR_EXPEDITION="CoordinatorExpedition";
    public static final String OFFICE_EXPEDITOR = "Office Expeditor";
    public static final String COORDINATOR_INSPECTION="CoordinatorInspection";
    public static final String DISPLAY_ORDER="displayOrder";
    public static final int ONE=1;
    public static final int TWO=2;
    public static final int THREE=3;
    public static final int FOUR=4;
    public static final String CONTACT_VENDOR="ContactVendor";
    public static final String TEAM_MEMBERS = "teamMembers";
    public static final String ASSOCIATED_LOCATION_ID = "associatedLocationId";
    public static final String EXCEPTION_TYPE = "ExceptionType";
	//Hardcoded values
	public static final String COMMA = ",";
	
	//URL
	public static final String SUPPORTING_TEAMS ="supporting_teams";
	
	
	
}

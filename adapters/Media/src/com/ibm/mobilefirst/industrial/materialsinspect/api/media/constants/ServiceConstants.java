package com.ibm.mobilefirst.industrial.materialsinspect.api.media.constants;

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
	public static final String ERROR_CODE_NODATAFOUND = "";
	
	public static final String ERROR_MESSAGE_LOGIN = "Login Failure";
	
	public static final String ERROR_MESSAGE_LOGOUT = "Logout Failure";
	public static final String ERROR_MESSAGE_LANGUAGE = "Unsupported language";
	public static final String ERROR_MESSAGE_UNDEFINEDISSUE = "Undefined Issue";
	public static final String MEDIA_BY_MEDIA_FILE_PATH = "getMediaFileByMediaPath";
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
	
	// URL
	public static final String USER_REQUEST_URL = "user_request_url";
	public static final String USER_REQUEST_URL_Y = "user_request_url_y";
	public static final String USER_REQUEST_URL_N = "user_request_url_n";
	public static final String MEDIA_UPLOAD = "media_upload";
	public static final String DOCUMENT_URL = "document_url";
	public static final String ATTACHMENT_URL = "attachment_url";
	public static final String REPORT_URL = "report_url";
	
	public static final String DOCUMENT = "Document";
	public static final String AUDIO = "Audio";
	public static final String VIDEO = "Video";
	public static final String IMAGE = "Image";
	public static final String VENDOR = "Vendor";
	public static final String ATTACHMENTS = "Attachments";
	public static final String REPORTS = "Reports";
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATIONJSON = "application/json";
	public static final String AUTHORIZATION = "Authorization";
	public static final String LANGUAGE = "language";
	public static final String US = "US";
	public static final String ORDER = "Order";
	public static final String SIZE = "size";
	public static final String OFFSET = "offset";
	public static final String PAGINATION = "pagination";
	public static final String DATA = "data";
	public static final String ERRORS = "errors";
	public static final String BYTE_DATA = "byteData";
	public static final String VPN = "VPN";
	public static final String VPN_NOT_CONNECTED = "VPN not connected";
	public static final String ASSOCIATED_QUESTION = "associatedQuestion";
	public static final String QUESTION_ID = "questionId";
	public static final String ID_ATTACHMENT = "ID_ATTACHMENT";
	public static final String NAME = "NAME";
	public static final String FILE_NAME = "fileName";
	public static final String FILE_EXTENSION = "fileExtension";
	public static final String DESC = "DESC";
	public static final String DATE_CREATED = "DATE_CREATED";
	public static final String TYPE = "TYPE";
	public static final String ID_TYPE = "ID_TYPE";
	public static final String THUMBNAIL = "THUMBNAIL";
	public static final String FILE_PATH = "FILEPATH";
	public static final String ATTACHMENT = "ATTACHMENT";
	public static final String SERIALIZED_FILE = "SERIALIZED_FILE";
	public static final String THUMB_NAIL = "thumbnail";
	public static final String LIST_ATTACHMENT = "listAttachment";
	public static final String ID_DESTINATION = "ID_DESTINATION";
	public static final String FIELD = "Field";
	public static final String DOT = ".";
	public static final String UPLOAD_MEDIA_OUTPUT = "No data from SOR response after upload media.";
	 public static final String DISPLAY_NAME = "displayName";
	 public static final String LAST_DOWNLOAD_DATE = "lastDownloadDate";
	 public static final String MEDIA_DESCRIPTION = "mediaDescription";
	 public static final String MEDIA_ID = "mediaId";
	 public static final String NEW_VERSION_AVAILABLE = "newVersionAvailable";
	 public static final String PATH = "path";
	 public static final String SYNC_STATUS = "syncStatus";
	 public static final String VERSION = "version";
	 public static final String TITLE = "title";
	 public static final String DOCUMENT_STATUS = "documentStatus";
	 public static final String SUPPORTING_DOCUMENT_TYPE = "supportingDocumentType";
	 public static final String UPDATE_MEDIA = "updateMedia";
	 public static final String GET_TYPE = "getType";
	 public static final String GET_TYPE_ID = "getTypeId";
	 public static final String GET_FORM_ID = "getFormId";
	 public static final String SET_LIMIT = "setLimit";
	 public static final String SET_OFFSET = "setOffset";
	public static final String PDF = "pdf";
	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";
	public static final String XLTX = "xltx";
	public static final String XLSM = "xlsm";
	public static final String XLSB = "xlsb";
	public static final String DOC = "doc";
	public static final String DOCX = "docx";
	public static final String DOCM = "docm";
	public static final String DOTM = "dotm";
	public static final String DOTX = "dotx";
	public static final String PPT = "ppt";
	public static final String PPTX = "pptx";
	public static final String PPTM = "pptm";
	public static final String PPSX = "ppsx";
	public static final String AVI = "avi";
	public static final String MP4 = "mp4";
	public static final String MOV = "mov";
	public static final String BMP = "bmp";
	public static final String GIF = "gif";
	public static final String JPG = "jpg";
	public static final String PNG = "png";
	public static final String JPEG = "jpeg";
	public static final String MP3 = "mp3";
	public static final String WMA = "wma";
	public static final String AAC = "aac";
	public static final String AC3 = "ac3";
	public static final String DOCUMENT_ID = "2016091336";
	public static final String IMAGE_ID = "2016091334";
	public static final String AUDIO_ID = "2016113022";
	public static final String VIDEO_ID = "2016091335";
	public static final String EXCEPTION_IN = "Exception in :";
	
	public static final String LEVEL_INFO = "INFO";
	public static final String LEVEL_WARNING = "WARNING";
	public static final String LEVEL_ERROR = "ERROR";
	
	public static final String ADAPTER_NAME="mediaAdapter";
	
	public static final String ADAPTER_PROPERTY_FILE="materialinspect.adapter.properties";
	
	
	//added by development team
	public static final String DATA_ADAPTER_NAME="mediaDataAdapter";
	
	
	
}

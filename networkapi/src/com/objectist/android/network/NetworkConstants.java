package com.objectist.android.network;

public class NetworkConstants {
	
	public static final int CONNECT_TIMEOUT = 5000;
	public static final int READ_TIMEOUT = 30000;
	
	public static final int ACTION_FIRST_LOGIN = 1; 
	
	public static final int ACTION_AUTH = 3 ;
	public static final int ACTION_RAW_TAB_PAGE = 100 ;
	
	public static final int ACTION_RE_LOGIN = 2; //


	
	public static final int STATUS_CODE_SUCCESS = 0;
	public static final int STATUS_CODE_NO_DATA = 2;
	public static final int STATUS_CODE_NEW_VERSION = 500;
	public static final int STATUS_CODE_SESSION_EXPIRED = 503;
	public static final int STATUS_CODE_DEVICE_UNSUPPORTED = 11;
	

	
	public static final int NOTIFICATION_TYPE_1 = 1;
	
	public static final String KEY_ACTION = "action";
	public static final String KEY_STATUS = "errCode";
	public static final String KEY_PAYLOAD= "json" ;
	public static final String KEY_RENDER = "render";
	public static final String KEY_DATA = "result" ;
	public static final int ACTION_LOGIN = 5;

	public static final int ACTION_VERIFY_PASSWORD = 6;
	public static final String RESULT_DATA = "result";
	

}

package com.objectist.android.network;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONObject;

public class NetworkRequest<T> {
	
	//private final int action;
	private final String serverUrl;
	private final HashMap<String, String> urlParams;
	private final Class<T> clazz ;
	private  JSONObject json ;

	public NetworkRequest(String serverUrl, Class<T> lclazz) {
		super();
		this.serverUrl = serverUrl;
		//this.action = action;
		this.urlParams = new HashMap<String, String>();
        
		this.clazz = lclazz ;
	
	}

	public  Class<T>  getClazz() {
		return this.clazz ;
		
	}
	public void addPayload(JSONObject json) {
		this.json = json ;
		
	}
	public String getJsonString() {
//		 JSONObject jrequest = null ;
//		try {
//		 jrequest = new JSONObject();
//
//	  		
//		Set<String> urlParamNames = urlParams.keySet();
//		for (String urlParamName : urlParamNames) {
//			String urlParamValue = urlParams.get(urlParamName);
//			jrequest.put(urlParamName, urlParamValue) ;
//			
//		}
//		}catch(Exception e) {
//			Log.v("ronj", "Fix me " + e.getMessage());
//		}
//		return jrequest.toString() ;
	    return json.toString() ;
	  		
	}
	@Override
	public String toString() {
		return "[MyRequest ("  + ")]";
	}
	
	public void addParam(String paramName, String paramValue) {
		if (paramValue == null) {
			return;
		}
	   
	   urlParams.put(paramName, paramValue);
	   
	}

	
	
	public void removeParam(String paramName) {
		urlParams
		.remove(paramName);
	}

//	public int getAction() {
//		return action;
//	}

	public String getUrl() {
		StringBuilder url = new StringBuilder(this.serverUrl);
		if (this.serverUrl.contains("?") == true) {
			url.append("&");
		} else {
			url.append('?');
		}
		appendQueryParams(url);
		return url.toString();
	}
	
	

	private void appendQueryParams(StringBuilder string) {
		Set<String> urlParamNames = urlParams.keySet();
		for (String urlParamName : urlParamNames) {
			String urlParamValue = urlParams.get(urlParamName);
			if (urlParamValue != null && urlParamValue.length() > 0) {
				string.append(((string.toString().endsWith("?") || string.toString().endsWith("&")) == false ? "&" : "" ) + urlParamName + "=" + urlParamValue);
			}
		}
	}

	public String getParamValue(String paramName) {
		return urlParams.get(paramName);
	}

	public void addParams(HashMap<String, String> customUrlParams) {
		if (customUrlParams != null) {
			Iterator<String> keys = customUrlParams.keySet().iterator();
			String key, value;
			while (keys.hasNext() == true) {
				key = keys.next();
				value = customUrlParams.get(key);
				if (value != null) {
					urlParams.put(key, URLEncoder.encode(value));
				}
			}
		}
	}

}

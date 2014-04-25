package com.objectist.android.network.requests;

import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.objectist.android.network.AbstractNetworkTask;
import com.objectist.android.network.NetworkAPIError;
import com.objectist.android.network.NetworkAPIRequest;
import com.objectist.android.network.NetworkRequest;
import com.objectist.android.network.NetworkResponse;
import com.objectist.android.network.RequestUrlBuilder;
import com.objectist.android.network.listeners.NetworkListener;

public class GenericRequest <T> extends NetworkAPIRequest {

	@SuppressWarnings("unused")
	private static final String CLASS_NAME = GenericRequest.class.getSimpleName();
	
	
	private Object object ;
	
	private final Class<T> clazz ;
	private String webServiceRestPath ;
	
	final NetworkListener listener;

	public  GenericRequest(String webServiceRestPath, Object object, Class<T> lclazz, NetworkListener listener, Object requestId) {
		super(requestId);
		this.webServiceRestPath = webServiceRestPath ;
		this.listener = listener;

	
		this.object = object ;
		this.clazz = lclazz ;

	}

	@Override
	public void run() {
		try {

			String json = new Gson().toJson( object);
			
			JSONObject object = new JSONObject(json) ;
			//Iterator itr = object.keys(); 
			NetworkRequest<?> requestUrl = RequestUrlBuilder.getRequestUrl(webServiceRestPath, clazz);
//			while(itr.hasNext()) {
//
//			    String element = (String) itr.next(); 
//			    String value = object.getString((String) element) ;
//			    
//			    System.out.print(element + " " + value);
//			    requestUrl.addParam(element,value) ;
//
//			} 
			requestUrl.addPayload(object) ;
			
			setRequestUrl(requestUrl);
			executeNetworkTask();

		} catch (Exception e) {

			sendErrorToListener(new NetworkAPIError());
		}
	}

	public void onException(int action, Exception exception) {

		sendErrorToListener(new NetworkAPIError());
	}

	
	public void onResponseErrorCode( NetworkResponse response) {
		sendErrorToListener(new NetworkAPIError(response.getCode(), response.getMessage()));
	}

	
	public void onNetworkTaskSucceeded(Object userObject, JSONObject object) {
		try {
		int errCode = -99 ;
		
		String errCodeStr = object.getString("errCode");
		String message = "";
		if( errCodeStr != null ) {
			errCode = Integer.parseInt(errCodeStr) ;
			message =  object.getString("error") ;
			if( message == null ) {
				message =  object.getString("errMessage") ;
				if( message == null ) {
				  message = "Unknown Error for error=" + errCode ;
				}
			}
			
			
		}else {
			// handle error from wipit decline
			errCodeStr = object.getString("errorCode");
			if( errCodeStr != null ) {
				errCode = Integer.parseInt(errCodeStr) ;
				message =  object.getString("errorMessage") ;
				if( message == null ) {
					message = "Unknown Error for error=" + errCode ;
				}
			}else {
				errCode =  object.getInt("errorCode") ;
				message =  object.getString("errorMessage") ;
			}
			
			
		}
		
		
		
		if( errCode != 0  ) {
			sendErrorToListener(new NetworkAPIError(errCode, message));
		}else {
			sendToUserListener(userObject);	
		}
		}catch(Exception e) {
			//TODO fixme
			Log.v("ronj", "FIX ME");
		}
//		if (isStatusCodeSuccess(responseJson) == true) {
//			processGetTestRequestJson(responseJson);
//		} else {
//			sendErrorToListener(new NetworkAPIError());
//		}
	}

	private void processGetTestRequestJson(JsonElement responseJson) {
		try {
			JsonObject object = responseJson.getAsJsonObject();
			//JsonElement el = object.get(NetworkConstants.RESULT_DATA);
			
			//UserTokenResponse testResponse = new Gson().fromJson(el, UserTokenResponse.class);

			sendToUserListener(object);
		} catch (Exception e) {

			sendErrorToListener(new NetworkAPIError());
		}
	}

	private void sendToUserListener(final Object testRequest) {
		super.postToHandler(new Runnable() {
			@Override
			public void run() {
				listener.onSucceeded(testRequest, getRequestId());
			}
		});
	}

	protected void sendErrorToListener(final NetworkAPIError error) {
		super.postToHandler(new Runnable() {
			@Override
			public void run() {
				listener.onFailed(error, getRequestId());
			}
		});
	}

	@Override
	public Object getRequestId() {

		return null;
	}

	@Override
	public void progressUpdate(final int count0To100) {

		super.postToHandler(new Runnable() {
			@Override
			public void run() {
				Log.v("ronj", "count0To100" + count0To100);
				listener.progressUpdate(count0To100);
			}
		});

	}

	@Override
    public void onRawNetworkTaskSucceeded(String responseString) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void onException(final Exception exception) {
		super.postToHandler(new Runnable() {
			@Override
			public void run() {
				
				listener.onFailed(new NetworkAPIError(AbstractNetworkTask.UNKNOWN_ERROR,exception.getMessage()),  getRequestId()) ;
			}
		});
	    
    }

	@Override
    public void onImageNetworkTaskSucceeded(byte[] response) {
	    // TODO Auto-generated method stub
	    
    }

//	@Override
//    public void onResponseErrorCode(NetworkResponse response) {
//	    // TODO Auto-generated method stub
//	    
//    }
//
//	@Override
//    public void onNetworkTaskSucceeded(Object usersReponseClass, JSONObject json) {
//	    // TODO Auto-generated method stub
//	    
//    }

//	@Override
//    public <T> void onNetworkTaskSucceededNew(int action, Object o) {
//	    // TODO Auto-generated method stub
//	    
//    }

	

}

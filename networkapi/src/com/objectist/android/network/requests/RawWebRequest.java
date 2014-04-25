package com.objectist.android.network.requests;
import org.json.JSONObject;

import android.util.Log;

import com.objectist.android.network.NetworkAPIError;
import com.objectist.android.network.NetworkAPIRequest;
import com.objectist.android.network.NetworkResponse;
import com.objectist.android.network.RequestUrlBuilder;
import com.objectist.android.network.listeners.RawWebRequestListener;
import com.objectist.android.network.response.RawWebResponse;

public class RawWebRequest extends NetworkAPIRequest {

	
	@SuppressWarnings("unused")
	private static final String CLASS_NAME = RawWebRequest.class.getSimpleName();
	
   
    private String url ;
    private NetworkAPIRequest.Protocol protocol ;
    
	final RawWebRequestListener listener;

	public RawWebRequest(String url,NetworkAPIRequest.Protocol protocol, RawWebRequestListener listener, Object requestId) {
		super(requestId);
		this.listener = listener;
		this.protocol = protocol ;
		this.url = url ;

	}

	@Override
	public void run() {
		try {

			setRequestUrl(RequestUrlBuilder.getRawWebRequest(url));
			executeRawNetworkTask(protocol);

		} catch (Exception e) {

			sendErrorToListener(new NetworkAPIError());
		}
	}

	@Override
	public void onException(Exception exception) {

		sendErrorToListener(new NetworkAPIError());
	}

	@Override
	public void onResponseErrorCode( NetworkResponse response) {
		sendErrorToListener(new NetworkAPIError(response.getCode(), response.getMessage()));
	}

//	@Override
//	public void onRawNetworkTaskSucceeded(int action, String response) {
//		
//			processGetRawWebRequest(responseJson);
//		
//		
//	}

	private void processGetRawWebRequest(String responseString) {
		try {
//			JsonObject object = responseJson.getAsJsonObject();
//			JsonElement el = object.get(NetworkConstants.RESULT_DATA);
//			
//			UserTokenResponse testResponse = new Gson().fromJson(el, UserTokenResponse.class);
			
			RawWebResponse response =  new RawWebResponse(responseString);

			sendToListener(response);
		} catch (Exception e) {

			sendErrorToListener(new NetworkAPIError());
		}
	}

	private void sendToListener(final RawWebResponse response) {
		super.postToHandler(new Runnable() {
			@Override
			public void run() {
				listener.onRawWebRequestSucceeded(response, getRequestId());
			}
		});
	}

	protected void sendErrorToListener(final NetworkAPIError error) {
		super.postToHandler(new Runnable() {
			@Override
			public void run() {
				listener.onRawWebRequestFailed(error, getRequestId());
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
    public void onRawNetworkTaskSucceeded( String responseString) {
		
			processGetRawWebRequest(responseString);
		
    }

	@Override
    public void onNetworkTaskSucceeded(Object responseJson, JSONObject object) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void onImageNetworkTaskSucceeded(byte[] response) {
	    // TODO Auto-generated method stub
	    
    }

	

}

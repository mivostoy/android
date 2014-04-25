package com.objectist.android.network.requests;


import org.json.JSONObject;

import android.util.Log;

import com.objectist.android.network.NetworkAPIError;
import com.objectist.android.network.NetworkAPIRequest;
import com.objectist.android.network.NetworkResponse;
import com.objectist.android.network.RequestUrlBuilder;
import com.objectist.android.network.listeners.ImageWebRequestListener;
import com.objectist.android.network.response.ImageResponse;

public class ImageRequest extends NetworkAPIRequest {

	
	@SuppressWarnings("unused")
	private static final String CLASS_NAME = ImageRequest.class.getSimpleName();

    private String url ;
    private NetworkAPIRequest.Protocol protocol ;
    //private String mimeType ;
    
	final ImageWebRequestListener listener;

	public ImageRequest(String url,NetworkAPIRequest.Protocol protocol,String mimeType,ImageWebRequestListener listener, Object requestId) {
		super(requestId);
		this.listener = listener;
		this.protocol = protocol ;
		this.url = url ;
		//this.mimeType = mimeType ;
	}

	@Override
	public void run() {
		try {

			setRequestUrl(RequestUrlBuilder.getImageRequest(url));
			executeImageNetworkTask(protocol);

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

	private void processGetImageWebRequest(byte[] response) {
		try {
//			JsonObject object = responseJson.getAsJsonObject();
//			JsonElement el = object.get(NetworkConstants.RESULT_DATA);
//			
//			UserTokenResponse testResponse = new Gson().fromJson(el, UserTokenResponse.class);
			
			ImageResponse res =  new ImageResponse(response);

			sendToListener(res);
		} catch (Exception e) {

			sendErrorToListener(new NetworkAPIError());
		}
	}

	private void sendToListener(final ImageResponse response) {
		super.postToHandler(new Runnable() {
			@Override
			public void run() {
				listener.onImageWebRequestSucceeded(response, getRequestId());
			}
		});
	}

	protected void sendErrorToListener(final NetworkAPIError error) {
		super.postToHandler(new Runnable() {
			@Override
			public void run() {
				listener.onImageRequestFailed(error, getRequestId());
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
		
			//processGetImageWebRequest(responseString);
		
    }

	@Override
    public void onNetworkTaskSucceeded(Object responseJson, JSONObject object) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void onImageNetworkTaskSucceeded(byte[] response) {
	    processGetImageWebRequest(response);
    }

	

}

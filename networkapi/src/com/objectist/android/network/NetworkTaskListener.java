package com.objectist.android.network;
import org.json.JSONObject;

public interface NetworkTaskListener {

	public void onException( Exception exception);

	public void onResponseErrorCode( NetworkResponse response);

	public void onNetworkTaskSucceeded( Object usersReponseClass, JSONObject json);

	public void progressUpdate(int count0To100);

	public void onRawNetworkTaskSucceeded( String responseString);
	public void onImageNetworkTaskSucceeded( byte[] response);
	
	//public <T> void onNetworkTaskSucceededNew(int action, Object o );

}

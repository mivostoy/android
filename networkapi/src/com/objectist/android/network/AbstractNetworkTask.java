package com.objectist.android.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

@SuppressWarnings("rawtypes")
public abstract class AbstractNetworkTask implements Cancellable {

	@SuppressWarnings("unused")
	private static final String CLASS_NAME = NetworkTask.class.getSimpleName();

	protected final NetworkTaskListener listener;
	protected final NetworkRequest request;
	protected final Object requestId;
    public final static int UNKNOWN_ERROR = -1 ;
    public final static String jsonMimeType = "application/json" ;
    public final static String textHtmlMimeType = "text/html" ;
    public final static String byteArrayMimeType = "application/x-www-form-urlencoded";
      
	protected volatile boolean cancelled = false;
	protected volatile boolean notifiedListener = false;
	protected volatile boolean retry = true;

	public AbstractNetworkTask(NetworkRequest request, NetworkTaskListener listener, Object requestId) {
		super();
		this.request = request;
		this.listener = listener;
		this.requestId = requestId;
	}

	public void doNotRetry() {
		retry = false;
	}

	
	public void cancel() {
		cancelled = true;
	}

	
	public boolean isCancelled() {
		return cancelled;
	}

	public abstract void run();

	public abstract NetworkResponse getNetworkResponse() throws MalformedURLException, IOException;

	
	
	public static String read(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		try {
			String data = reader.readLine();
			while (data != null) {

				data = reader.readLine();
				if (data != null) {
					buffer.append(data);
				}
			}
			reader.close();
		} catch (Exception e) {
		}
		return buffer.toString();
	}

	

	protected  ByteArrayBuffer getResponseBytes(URLConnection connection) throws Exception {
		InputStream inputStream = connection.getInputStream();

		int contentLength = connection.getContentLength();

		if (contentLength <= 0) {
			throw new Exception("Content length is zero");
		}

		ByteArrayBuffer responseBytes = new ByteArrayBuffer(inputStream.available());
		byte[] buf = new byte[8192];
		int bytes = 0;
		int bytesRead = 0 ;
		int count = 0 ;
		while ((bytesRead = inputStream.read(buf)) > 0) {
			if (cancelled == true) {
				break;
			}
			bytes= bytes + bytesRead;
			responseBytes.append(buf, 0, bytesRead);
		
			count =  (int) (((float) bytes / (float) contentLength)*100f);
			
			listener.progressUpdate(count);
		}
		inputStream.close();
		listener.progressUpdate(count);
		
		return responseBytes;
	}
	

	
	public abstract <T> void processResponse(ByteArrayBuffer responseBytes) ;
	
//	private void processResponse(ByteArrayBuffer responseBytes) throws Exception {
//		if (cancelled == true) {
//			return;
//		}
//
//		String responseString = new String(responseBytes.toByteArray(), "UTF-8");
//		Log.v("ronj", responseString);
//		responseBytes.clear();
//
//		try {
//			JSONObject object = new JSONObject(responseString);
//			JsonElement responseJson = new JsonParser().parse(responseString);
//			String errCodeStr = object.getString("errCode");
//			String errorStr = object.getString("error");
//
//			notifiedListener = true;
//
//			listener.onNetworkTaskSucceeded(request.getAction(), responseJson);
//
//		} catch (JSONException e) {
//			Log.v("ronj", "e=" + e.getMessage());
//		}
//	}

	private void sendResponseCodeErrorToListener(NetworkResponse response) {
		if (cancelled == true) {
			return;
		}
		try {
			notifiedListener = true;
			listener.onResponseErrorCode( response);
		} catch (Exception e) {
		}
	}

	@Override
	public String toString() {
		return "[" + NetworkTask.class.getSimpleName() + "]";
	}

	
	public Object getRequestId() {
		return requestId;
	}

}

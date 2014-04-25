package com.objectist.android.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@SuppressWarnings("rawtypes")
public class NetworkTask extends AbstractNetworkTask {

	@SuppressWarnings("unused")
	private static final String CLASS_NAME = NetworkTask.class.getSimpleName();

	private HttpsURLConnection connection;

	public NetworkTask(NetworkRequest request, NetworkTaskListener listener, Object requestId) {
		super(request, listener, requestId);

	}

	public NetworkResponse getNetworkResponse() throws MalformedURLException, IOException {
		//int code = 0;
		//String url = request.getUrl();

		connection = (HttpsURLConnection) new URL(request.getUrl()).openConnection();

		connection.setDoOutput(true); // Triggers POST.

		connection.setRequestMethod("POST");
		connection.setChunkedStreamingMode(HttpsURLConnection.HTTP_PARTIAL);

		connection.setRequestProperty("Content-Type", jsonMimeType);

		try {

			OutputStream output = null;
			try {
				String input = request.getJsonString();
				connection.setRequestProperty("Content-Type", jsonMimeType);

				connection.setRequestProperty("Accept", jsonMimeType);

				connection.setRequestProperty("Content-Length", Integer.toString(input.length()));

				output = connection.getOutputStream();

				output.write(input.getBytes("UTF-8"));

				output.flush();

			} finally {
				if (output != null)
					try {
						output.close();
					} catch (IOException logOrIgnore) {
					}
			}

		} catch (Exception e) {
			Log.v("ronj", e.getMessage());
		}

		if (connection.getResponseCode() != 200) {

			return new NetworkResponse(connection.getResponseCode(), connection.getResponseMessage());

		}

		return new NetworkResponse(connection.getResponseCode(), null);
	}

	@Override
	public void run() {
		if (cancelled == true) {
			return;
		}
		NetworkResponse networkResponse = null;
		Exception exception = null;

		for (int i = 0; i < 4; i++) {
			if (i > 0) {
				try {
					Thread.sleep(400);
				} catch (InterruptedException ie) {
				}
			}
			try {
				networkResponse = getNetworkResponse();
				if (networkResponse.getCode() == HttpURLConnection.HTTP_OK) {
					ByteArrayBuffer responseBytes = getResponseBytes(connection);
					processResponse(responseBytes);
					break;
				} else {

					sendResponseCodeErrorToListener(networkResponse);
					break;
				}
			} catch (Exception e) {
				exception = e;

			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
			if (retry == false) {
				break;
			}
		}
		if (notifiedListener == false) {
			if (exception != null) {
				listener.onException( exception);
			} else {
				listener.onResponseErrorCode( networkResponse);
			}
		}
	}



//	private ByteArrayBuffer getResponseBytes(HttpURLConnection connection) throws Exception {
//		InputStream inputStream = connection.getInputStream();
//
//		int contentLength = connection.getContentLength();
//
//		if (contentLength <= 0) {
//			throw new Exception("Content length is zero");
//		}
//
//		ByteArrayBuffer responseBytes = new ByteArrayBuffer(inputStream.available());
//		byte[] buf = new byte[8192];
//		int bytes = 0;
//		int bytesRead = 0 ;
//		int count = 0 ;
//		while ((bytesRead = inputStream.read(buf)) > 0) {
//			if (cancelled == true) {
//				break;
//			}
//			bytes= bytes + bytesRead;
//			responseBytes.append(buf, 0, bytesRead);
//		
//			count =  (int) (((float) bytes / (float) contentLength)*100f);
//			
//			listener.progressUpdate(count);
//		}
//		inputStream.close();
//		listener.progressUpdate(count);
//		
//		return responseBytes;
//	}
//	
	
	private int updateCountTo100(int contentLength, int count) {
	    return (int) ((count / contentLength)*100f);
    }
	// @SuppressWarnings("unchecked")
	@Override
	public <T> void processResponse(ByteArrayBuffer responseBytes) {
		if (cancelled == true) {
			return;
		}
		try {

			String responseString = new String(responseBytes.toByteArray(), "UTF-8");
			Log.v("ronj", responseString);
			responseBytes.clear();

			// try {

			JSONObject object = new JSONObject(responseString);
			JsonElement responseJson = new JsonParser().parse(responseString);

			// String errCodeStr = responseJson.getString("errCode");
			// String errorStr = object.getString("error");

			notifiedListener = true;

			//Class<T> a = request.getClazz();
			Object b = new Gson().fromJson(responseJson, request.getClazz());

			// new Gson().fromJson(responseString, request.getClazz())

			listener.onNetworkTaskSucceeded( b, object);

		} catch (Exception e) {
			Log.v("ronj", "e=" + e.getMessage());
			listener.onResponseErrorCode( new NetworkResponse(UNKNOWN_ERROR, e.getMessage()));

		}
	}

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

	@Override
	public Object getRequestId() {
		return requestId;
	}

}

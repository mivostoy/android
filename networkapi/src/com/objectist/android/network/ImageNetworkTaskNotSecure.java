package com.objectist.android.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

@SuppressWarnings("rawtypes")
public class ImageNetworkTaskNotSecure extends AbstractNetworkTask {

	@SuppressWarnings("unused")
	private static final String CLASS_NAME = ImageNetworkTaskNotSecure.class.getSimpleName();
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	private HttpURLConnection connection;

	public ImageNetworkTaskNotSecure(NetworkRequest request, NetworkTaskListener listener, Object requestId) {
		super(request, listener, requestId);

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

			}
			if (retry == false) {
				break;
			}
		}
		if (notifiedListener == false) {
			if (exception != null) {
				listener.onException(exception);
			} else {
				listener.onResponseErrorCode(networkResponse);
			}
		}
	}

	public NetworkResponse getNetworkResponse() throws MalformedURLException, IOException {
		//int code = 0;
		//String url = request.getUrl();
		// trustAllHosts();
		connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
		// connection.setHostnameVerifier(DO_NOT_VERIFY);

		connection.setDoOutput(true); // Triggers POST.

		connection.setRequestMethod("GET");
		connection.setChunkedStreamingMode(HttpURLConnection.HTTP_PARTIAL);

		connection.setRequestProperty("Content-Type", byteArrayMimeType);
		try {

			OutputStream output = null;
			try {
				
				connection.setRequestProperty("Content-Type", byteArrayMimeType);

				connection.setRequestProperty("Accept", byteArrayMimeType);

				
				output = connection.getOutputStream();

				
				output.flush();

			} finally {
				if (output != null)
					try {
						output.close();
					} catch (IOException logOrIgnore) {
					}
			}

		} catch (Exception e) {
Log.v("ronj", "exception e" + e.getMessage());
		}

		if (connection.getResponseCode() != 200) {

			return new NetworkResponse(connection.getResponseCode(), connection.getResponseMessage());

		}

		return new NetworkResponse(connection.getResponseCode(), null);

	}



	@Override
	public void processResponse(ByteArrayBuffer responseBytes) {
		if (cancelled == true) {
			return;
		}

		try {
			
			
			
			notifiedListener = true;

			listener.onImageNetworkTaskSucceeded(responseBytes.toByteArray());

		} catch (Exception e) {
			listener.onResponseErrorCode(new NetworkResponse(UNKNOWN_ERROR, e.getMessage()));
		}
	}

	private void sendResponseCodeErrorToListener(NetworkResponse response) {
		if (cancelled == true) {
			return;
		}
		try {
			notifiedListener = true;
			listener.onResponseErrorCode(response);
		} catch (Exception e) {
		}
	}

	@Override
	public String toString() {
		return "[" + ImageNetworkTaskNotSecure.class.getSimpleName() + "]";
	}

	@Override
	public Object getRequestId() {
		return requestId;
	}

}

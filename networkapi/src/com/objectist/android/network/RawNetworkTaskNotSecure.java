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
public class RawNetworkTaskNotSecure extends AbstractNetworkTask {

	@SuppressWarnings("unused")
	private static final String CLASS_NAME = RawNetworkTaskNotSecure.class.getSimpleName();
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	private HttpURLConnection connection;

	public RawNetworkTaskNotSecure(NetworkRequest request, NetworkTaskListener listener, Object requestId) {
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

		connection.setRequestMethod("POST");
		connection.setChunkedStreamingMode(HttpURLConnection.HTTP_PARTIAL);

		connection.setRequestProperty("Content-Type", textHtmlMimeType);
		try {

			OutputStream output = null;
			try {
				String input = request.getJsonString();
				connection.setRequestProperty("Content-Type", textHtmlMimeType);

				connection.setRequestProperty("Accept", textHtmlMimeType);

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

		}

		if (connection.getResponseCode() != 200) {

			return new NetworkResponse(connection.getResponseCode(), connection.getResponseMessage());

		}

		return new NetworkResponse(connection.getResponseCode(), null);

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
//		int bytesRead = 0;
//
//		int count = 0;
//		while ((bytesRead = inputStream.read(buf)) > 0) {
//			if (cancelled == true) {
//				break;
//			}
//			bytes = bytes + bytesRead;
//			responseBytes.append(buf, 0, bytesRead);
//
//			count = (int) (((float) bytes / (float) contentLength) * 100f);
//
//			listener.progressUpdate(count);
//
//		}
//		inputStream.close();
//		listener.progressUpdate(count);
//		return responseBytes;
//	}

	@Override
	public void processResponse(ByteArrayBuffer responseBytes) {
		if (cancelled == true) {
			return;
		}

		try {
			String responseString = new String(responseBytes.toByteArray(), "UTF-8");
			Log.v("ronj", responseString);
			responseBytes.clear();
			notifiedListener = true;

			listener.onRawNetworkTaskSucceeded(responseString);

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
		return "[" + RawNetworkTaskNotSecure.class.getSimpleName() + "]";
	}

	@Override
	public Object getRequestId() {
		return requestId;
	}

}

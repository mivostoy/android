package com.objectist.android.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.util.ByteArrayBuffer;

@SuppressWarnings("rawtypes")
public class RawNetworkTask extends AbstractNetworkTask {

	@SuppressWarnings("unused")
	private static final String CLASS_NAME = RawNetworkTask.class.getSimpleName();
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	private HttpsURLConnection connection;

	public RawNetworkTask(NetworkRequest request, NetworkTaskListener listener, Object requestId) {
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
				if (networkResponse.getCode() == HttpsURLConnection.HTTP_OK) {
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
				listener.onException( exception);
			} else {
				listener.onResponseErrorCode( networkResponse);
			}
		}
	}

	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NetworkResponse getNetworkResponse() throws MalformedURLException, IOException {
		//int code = 0;
		//String url = request.getUrl();
		trustAllHosts();
		connection = (HttpsURLConnection) new URL(request.getUrl()).openConnection();
		connection.setHostnameVerifier(DO_NOT_VERIFY);

		connection.setDoOutput(true); // Triggers POST.

		connection.setRequestMethod("POST");
		connection.setChunkedStreamingMode(HttpsURLConnection.HTTP_PARTIAL);

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



//	private ByteArrayBuffer getResponseBytes(HttpsURLConnection connection) throws Exception {
//		InputStream inputStream = connection.getInputStream();
//
//		int contentLength = connection.getContentLength();
//
//		if (contentLength <= 0) {
//			throw new Exception("Content length is zero");
//		}
//		Log.v("ronj", "contentLength=" + contentLength);
//		ByteArrayBuffer responseBytes = new ByteArrayBuffer(inputStream.available());
//		byte[] buf = new byte[8192];
//		int bytes = 0;
//		int bytesRead = 0 ;
//		int count = 0 ;
//
//		while ((bytesRead = inputStream.read(buf)) > 0) {
//			if (cancelled == true) {
//				break;
//			}
//			count = count + bytesRead;
//			bytes= bytes + bytesRead;
//			responseBytes.append(buf, 0, bytesRead);
//		
//			count =  (int) (((float) bytes / (float) contentLength)*100f);
//			
//			listener.progressUpdate(count);
//		}
//		inputStream.close();
//		listener.progressUpdate(count);
//		return responseBytes;
//	}
	private int updateCountTo100(int contentLength, int count) {
	    return (int) ((count / contentLength)*100f);
    }
	
	public void processResponse(ByteArrayBuffer responseBytes)  {
		if (cancelled == true) {
			return;
		}
       
		

		try {
			String responseString = new String(responseBytes.toByteArray(), "UTF-8");
			
			responseBytes.clear();

			notifiedListener = true;

			listener.onRawNetworkTaskSucceeded( responseString);

		} catch (Exception e) {
			listener.onResponseErrorCode(new NetworkResponse(UNKNOWN_ERROR,e.getMessage()));
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
			
			listener.onResponseErrorCode(new NetworkResponse(UNKNOWN_ERROR,e.getMessage()));
		}
	}

	@Override
	public String toString() {
		return "[" + RawNetworkTask.class.getSimpleName() + "]";
	}

	@Override
	public Object getRequestId() {
		return requestId;
	}

}

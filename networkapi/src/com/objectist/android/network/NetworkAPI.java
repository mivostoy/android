package com.objectist.android.network;


import com.objectist.android.network.listeners.ImageWebRequestListener;
import com.objectist.android.network.listeners.NetworkListener;
import com.objectist.android.network.listeners.RawWebRequestListener;
import com.objectist.android.network.requests.GenericRequest;
import com.objectist.android.network.requests.ImageRequest;
import com.objectist.android.network.requests.RawWebRequest;


public class NetworkAPI   {

	

	public static void initialize( )  {
		NetworkExecutor.getInstance();
	}
	
	public static void setCommonUrlParameters(Object commonParameters) {
		NetworkExecutor.getInstance().setCommonParameters(commonParameters);
	}
	
	public static void cancelRequestFor(Object requestId) {
		NetworkExecutor.getInstance().cancelTasksWithRequestId(requestId);
	}
	
	public static <T> Cancellable executeRequest(String restUrl, Object object, Class<T> lclazz , NetworkListener listener, Object requestId) {
		NetworkAPIRequest request = new GenericRequest<T>(restUrl, object,   lclazz,listener, requestId);
		
		request.run();
		
		return request;
	    
    }

	public static Cancellable  getRawWebRequest(String url,NetworkAPIRequest.Protocol protocol, RawWebRequestListener listener, Object requestId) {
		NetworkAPIRequest request = new RawWebRequest(url,protocol, listener, requestId);
		request.run();
		return request;
	    
    }
	public static Cancellable  getImageFromURL(String url,NetworkAPIRequest.Protocol protocol, ImageWebRequestListener listener, Object requestId) {
		NetworkAPIRequest request = new ImageRequest(url,protocol, "application/x-www-form-urlencoded",listener, requestId);
		request.run();
		return request;
	    
    }
	
}

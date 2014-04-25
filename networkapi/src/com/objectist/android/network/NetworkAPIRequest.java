package com.objectist.android.network;

import android.os.Handler;

import com.google.gson.JsonElement;

@SuppressWarnings("rawtypes")
public abstract class NetworkAPIRequest implements Cancellable, NetworkTaskListener {

	@SuppressWarnings("unused")
	private static final String CLASS_NAME = NetworkAPIRequest.class.getSimpleName();
	public static enum Protocol {
	    HTTP,HTTPS 
	}
	//private final Object uiRequestId;
	protected final Handler handler;

	private AbstractNetworkTask networkTask = null;
	private NetworkRequest requestUrl = null;
	
	protected volatile boolean cancelled = false;

	protected NetworkAPIRequest(Object requestId) {
		super();
		//this.uiRequestId = requestId;
		this.handler = new Handler();
	}

	public void setRequestUrl(NetworkRequest requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	protected void executeImageNetworkTask(Protocol protocol) {
		if (cancelled == true) {
			return;
		}
		if (protocol == Protocol.HTTP) {
			networkTask = new ImageNetworkTaskNotSecure(requestUrl, this, getRequestId());
		} else {
			networkTask = new ImageNetworkTask(requestUrl, this, getRequestId());

		}
		NetworkExecutor.getInstance().execute(networkTask);
	}
	
	
	protected void executeRawNetworkTask(Protocol protocol) {
		if (cancelled == true) {
			return;
		}
		if (protocol == Protocol.HTTP) {
			networkTask = new RawNetworkTaskNotSecure(requestUrl, this, getRequestId());
		} else {
			networkTask = new RawNetworkTask(requestUrl, this, getRequestId());

		}
		NetworkExecutor.getInstance().execute(networkTask);
	}
	protected void executeNetworkTask() {
		if (cancelled == true) {
			return;
		}
		networkTask = new NetworkTask(requestUrl, this, getRequestId());
		NetworkExecutor.getInstance().execute(networkTask);
	}
	
	protected void postToHandler(Runnable runnable) {
		if (cancelled == true) {
			return;
		}
		handler.post(runnable);
	}

	
	public void cancel() {
		cancelled = true;
		if (networkTask != null) {
			networkTask.cancel();
		}
	}

	
	public boolean isCancelled() {
		return cancelled;
	}

	protected boolean isStatusCodeSuccess(final JsonElement responseJson) {
		if (cancelled == true) {
			return false;
		}
		return NetworkUtils.isStatusCodeSuccess(responseJson);
	}

	protected boolean isStatusCodeNoData(final JsonElement responseJson) {
		return NetworkUtils.isStatusCodeNoData(responseJson);
	}

	protected abstract void sendErrorToListener(final NetworkAPIError error);

}
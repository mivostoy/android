package com.objectist.android.network;

public interface Cancellable extends Runnable {

	public void cancel();

	public boolean isCancelled();

	public Object getRequestId();

}


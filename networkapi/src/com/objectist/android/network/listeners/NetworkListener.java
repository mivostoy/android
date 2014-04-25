package com.objectist.android.network.listeners;


import com.objectist.android.network.NetworkAPIError;

public interface NetworkListener {
	public void onSucceeded(Object userTokenResponse, Object requestId);
    public void progressUpdate(int countZeroTo100) ;
	public void onFailed(NetworkAPIError error, Object requestId);
}


package com.objectist.android.network.listeners;

import com.objectist.android.network.NetworkAPIError;
import com.objectist.android.network.response.RawWebResponse;

public interface RawWebRequestListener {
	public void onRawWebRequestSucceeded(RawWebResponse response, Object requestId);
    public void progressUpdate(int countZeroTo100) ;
	public void onRawWebRequestFailed(NetworkAPIError error, Object requestId);
}

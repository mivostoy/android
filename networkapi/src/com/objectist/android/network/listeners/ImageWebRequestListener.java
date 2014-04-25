package com.objectist.android.network.listeners;

import com.objectist.android.network.NetworkAPIError;
import com.objectist.android.network.response.ImageResponse;

public interface ImageWebRequestListener {
	public void onImageWebRequestSucceeded(ImageResponse response, Object requestId);
    public void progressUpdate(int countZeroTo100) ;
	public void onImageRequestFailed(NetworkAPIError error, Object requestId);
}

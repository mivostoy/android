package com.objectist.android.network.response;

public class ImageResponse {

	private byte [] data;
	
	
	private byte[] Type = null;

	public ImageResponse(byte[] data) {
        this.data = data ;
	}

	public byte[] getType() {
		return this.Type;
	}

	

	public byte[] getData() {
		return this.data;
	}

	
}

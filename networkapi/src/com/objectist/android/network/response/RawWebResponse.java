package com.objectist.android.network.response;

public class RawWebResponse {

	private String data;
	
	
	private String Type = null;

	public RawWebResponse(String data) {
this.data = data ;
	}

	public String getType() {
		return this.Type;
	}

	

	public String getData() {
		return this.data;
	}

	
}

package com.objectist.android.network;

import org.apache.http.HttpResponse;

public class WebResponse {
	private HttpResponse response ;
	private String result ;
	public WebResponse(HttpResponse response, String result) {
		this.response = response ;
		this.result = result ;
	}
	public HttpResponse getResponse() {
		return response ;
	}
	public String getResult() {
		return result ;
	}
}
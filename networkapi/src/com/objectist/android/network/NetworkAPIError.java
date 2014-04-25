package com.objectist.android.network;

public class NetworkAPIError {
	
	private int errorCode;		
	private String errorMessage;

	public NetworkAPIError() {
		this.errorCode = AbstractNetworkTask.UNKNOWN_ERROR;
		this.errorMessage = "Unknown Error";
	}

	public NetworkAPIError(int errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = "Unknown Error";
	}

	public NetworkAPIError(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

}

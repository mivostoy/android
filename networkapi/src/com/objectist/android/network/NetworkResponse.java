package com.objectist.android.network;
public class NetworkResponse {
  private int code ;
  private String message  ;
  
  public NetworkResponse(int code, String message) {
	  this.code = code ;
	  this.message = message ;
  }
  public void setCode(int code) {
	  this.code = code ;
  }
  public void setMessage(String message) {
	  this.message = message ;
  }
  public String getMessage() {
	  return this.message  ;
  }
  public int getCode() {
	  return this.code ;
  }
}

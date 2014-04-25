package com.objectist.android.network;

import com.google.gson.JsonElement;

public class NetworkUtils {
	
	@SuppressWarnings("unused")
	private static final String CLASS_NAME = NetworkUtils.class.getSimpleName();
	
	public static boolean isStatusCodeSuccess(final JsonElement responseJson) {
		try {
			//JsonObject object = responseJson.getAsJsonObject() ;
			int status = responseJson.getAsJsonObject().get(NetworkConstants.KEY_STATUS).getAsInt();
			return status == NetworkConstants.STATUS_CODE_SUCCESS;
		} catch (Exception e) {
	
		}
		return false;
	}
	
	public static boolean isStatusCodeNoData(final JsonElement responseJson) {
		try {
			int status = responseJson.getAsJsonObject().get(NetworkConstants.KEY_STATUS).getAsInt();
			return status == NetworkConstants.STATUS_CODE_NO_DATA;
		} catch (Exception e) {
		
		}
		return false;
	}

	public static boolean isStatusCodeNewVersion(final JsonElement responseJson) {
		try {
			int status = responseJson.getAsJsonObject().get(NetworkConstants.KEY_STATUS).getAsInt();
			return status == NetworkConstants.STATUS_CODE_NEW_VERSION;
		} catch (Exception e) {
		
		}
		return false;
	}

	public static boolean isStatusCodeSessionExpired(final JsonElement responseJson) {
		try {
			int status = responseJson.getAsJsonObject().get(NetworkConstants.KEY_STATUS).getAsInt();
			return status == NetworkConstants.STATUS_CODE_SESSION_EXPIRED;
		} catch (Exception e) {
	
		}
		return false;
	}
	
	
}

package com.objectist.android.network;

import java.util.Iterator;

import org.json.JSONObject;

import com.google.gson.Gson;

@SuppressWarnings("rawtypes")
public class RequestUrlBuilder {

	@SuppressWarnings("unused")
	private static final String CLASS_NAME = RequestUrlBuilder.class.getSimpleName();

	public static NetworkRequest getRawRequestUrl(String url) {

		NetworkRequest requestUrl = new NetworkRequest(url, null);
		addCommonParamsToRequestUrl(requestUrl);
		return requestUrl;
	}
	public static NetworkRequest getImageRequestUrl(String url) {

		NetworkRequest requestUrl = new NetworkRequest(url, null);
		
		return requestUrl;
	}

	public static <T> NetworkRequest getRequestUrl(String url, Class<T> clazz) {

		NetworkRequest requestUrl = new NetworkRequest(url, clazz);
		addCommonParamsToRequestUrl(requestUrl);
		return requestUrl;
	}

	public static NetworkRequest getRawWebRequest(String url) {

		NetworkRequest requestUrl = getRawRequestUrl(url);
		requestUrl.addParam("Content-Type", "text/html");

		return requestUrl;
	}

	private static void addCommonParamsToRequestUrl(NetworkRequest requestUrl) {
		try {
			Object userCommonParams = NetworkExecutor.getInstance().getCommonParameters();
			if (userCommonParams == null) {
				return;
			}
			String json = new Gson().toJson(userCommonParams);
			JSONObject object = new JSONObject(json);
			Iterator itr = object.keys();

			while (itr.hasNext()) {

				String element = (String) itr.next();
				String value = object.getString((String) element);

				System.out.print(element + " " + value);
				requestUrl.addParam(element, value);

			}
		} catch (Exception e) {

		}

	}

	public static boolean isCommonParam(String paramKey) {
		return paramKey.equals(NetworkConstants.KEY_RENDER);
	}

	public static NetworkRequest getImageRequest(String url) {
		NetworkRequest requestUrl = getImageRequestUrl(url);
		
	    return requestUrl;
    }

}

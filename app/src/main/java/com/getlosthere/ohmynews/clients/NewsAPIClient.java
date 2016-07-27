package com.getlosthere.ohmynews.clients;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by violetaria on 7/26/16.
 */
public class NewsAPIClient {
    private static final String BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    private static final String API_KEY = "597d33abf757488a806eed649bf00ca2";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        params.put("api-key", API_KEY);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}

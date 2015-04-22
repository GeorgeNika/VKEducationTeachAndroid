package com.teachandroid.app.api;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

class VkRequestBuilder implements RequestBuilder {

    private static final String API_URL = "https://api.vk.com/method/";

    private final String method;

    private final Map<String, String> params = new LinkedHashMap<String, String>();

    public VkRequestBuilder(String method, String accessToken) {
        this.method = method;
        this.params.put("access_token", accessToken);
        this.params.put("v", "5.28");
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    @Override
    public String query() {
        Iterator<String> iterator = params.keySet().iterator();
        StringBuilder request = new StringBuilder(API_URL);
        request.append(method);
        request.append("?");

        int index = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!TextUtils.isEmpty(params.get(key))) {
                request.append(key);
                request.append("=");
                try {
                    request.append(URLEncoder.encode(params.get(key), "UTF-8"));
                    if(index++ < params.size() -1) {
                        request.append("&");
                    }
                } catch (UnsupportedEncodingException e) {
                    throw  new RuntimeException(e);
                }
            }

        }
        return request.toString();
    }

}

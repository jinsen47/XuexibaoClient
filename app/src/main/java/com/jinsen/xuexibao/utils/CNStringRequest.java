package com.jinsen.xuexibao.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Administrator on 2015/2/26.
 */
public class CNStringRequest extends StringRequest {
    private Map headers;

    public CNStringRequest(int method, String url, Map headers, Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.headers = headers;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse networkResponse) {
        String s = null;
        try {
            s = new String(networkResponse.data,"utf-8");
        } catch (UnsupportedEncodingException e) {
            s = new String(networkResponse.data);
        }
        return Response.success(s, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }
}

package com.jinsen.xuexibao.utils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jinsen.xuexibao.AppData;


/**
 * Created by Jinsen on 2015/2/12.
 */
public class RequestManager {
    public static RequestQueue mRequestQueue = newRequestQueue();
    private RequestManager(){

    }

    private static RequestQueue newRequestQueue() {
        RequestQueue requestQueue = Volley.newRequestQueue(AppData.getsCxt());
        requestQueue.start();
        return requestQueue;
    }

    public static void addRequest(Request request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

}
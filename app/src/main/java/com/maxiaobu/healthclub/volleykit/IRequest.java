package com.maxiaobu.healthclub.volleykit;


import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 2015/12/17 封装请求
 * url在前不带加载
 * context在前带加载
 */
public class IRequest {
    public RequestManager sRequestManager;

    public IRequest(Application sApplication) {
        RequestQueue sRequestQueue = Volley.newRequestQueue(sApplication);
        sRequestManager = new RequestManager(sRequestQueue, sApplication);
    }

    public RequestManager getRequestManager() {
        return sRequestManager;
    }


    public void get(Context context, String url, RequestListener l) {
        sRequestManager.get(url, context, "", true, l);
    }


    public void get(String url, Context context, RequestListener l) {
        sRequestManager.get(url, context, "", false, l);
    }


    public <T> void get(Context context, String url, Class<T> classOfT,
                        RequestJsonListener<T> l) {
        sRequestManager.get(url, context, classOfT, "", true, l);
    }


    public <T> void get(String url, Context context, Class<T> classOfT,
                        RequestJsonListener<T> l) {
        sRequestManager.get(url, context, classOfT, "", false, l);
    }


    public void post(Context context, String url, RequestParams params,
                     RequestListener l) {
        sRequestManager.post(url, context, params, "", true, l);
    }


    public void post(String url, Context context, RequestParams params,
                     RequestListener l) {
        sRequestManager.post(url, context, params, "", false, l);
    }


    public <T> void post(Context context, String url, Class<T> classOfT,
                         RequestParams params, RequestJsonListener<T> l) {
        sRequestManager.post(url, context, classOfT, params, null, true, l);
    }

    public <T> void post(String url, Context context, Class<T> classOfT,
                         RequestParams params, RequestJsonListener<T> l) {
        sRequestManager.post(url, context, classOfT, params, null, false, l);
    }
   /* public static void postImg(Context context, String url, RequestParams params,
                               RequestListener l) {
        RequestManager.postImg(url, context, params, l);
    }*/


}

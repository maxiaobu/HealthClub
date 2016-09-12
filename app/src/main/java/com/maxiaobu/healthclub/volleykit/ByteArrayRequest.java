package com.maxiaobu.healthclub.volleykit;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * create by maxiaobu 2015-12-17
 * 自定义Request 继承Request<byte[]>
 */
class ByteArrayRequest extends Request<byte[]> {

    private final Response.Listener<byte[]> mListener;

    private Object mPostBody = null;

    private HttpEntity httpEntity = null;

    /**
     * @param method        请求方式
     * @param url           链接
     * @param postBody      请求参数
     * @param listener      成功回调
     * @param errorListener 失败回调
     */
    public ByteArrayRequest(int method, String url, Object postBody, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mPostBody = postBody;
        this.mListener = listener;

        if (this.mPostBody != null && this.mPostBody instanceof RequestParams) {// contains file
            this.httpEntity = ((RequestParams) this.mPostBody).getEntity();
        }
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        if (this.httpEntity == null && this.mPostBody != null && this.mPostBody instanceof Map<?, ?>) {
            return ((Map<String, String>) this.mPostBody);//common Map<String, String>
        }
        return null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }
        return headers;
    }

    @Override
    public String getBodyContentType() {
        if (httpEntity != null) {
            return httpEntity.getContentType().getValue();
        }
        return null;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (this.mPostBody != null && this.mPostBody instanceof String) {
            String postString = (String) mPostBody;
            if (postString.length() != 0) {
                try {
                    return postString.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        }
        if (this.httpEntity != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return baos.toByteArray();
        }
        return super.getBody();
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        this.mListener.onResponse(response);
    }
}
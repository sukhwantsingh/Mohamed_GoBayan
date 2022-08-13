package com.libyasolutions.libyamarketplace.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by pham on 27/10/2015.
 */
public class HttpGet extends HttpRequest {

    public HttpGet(Context context, String url, Map<String, String> params, boolean isShowDialog, HttpListener httpListener, HttpError httpError) {
        super(context, HttpRequest.METHOD_GET, url, isShowDialog, httpListener, httpError);
        this.params = params;
        this.url = getUrl(url, this.params).replace(" ","%20");
        Log.wtf("Test","Demo"+this.url);
        sendRequest();
    }

    protected void sendRequest() {
        Log.wtf("HttpGET", "url : " + this.url);
        request = getStringRequest();
        super.sendRequest();
    }

    private Request getStringRequest() {
        if (isShowDialog)
            showDialog();
        Response.Listener successResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (isShowDialog)
                    closeDialog();
                httpListener.onHttpRespones(response);

            }
        };
        Response.ErrorListener errorResponse = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isShowDialog)
                    closeDialog();
                httpError.onHttpError(error);

            }
        };
        StringRequest request = new StringRequest(requestMethod, url, successResponse, errorResponse) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        return request;
    }


    protected String getUrl(String url, Map<String, String> params) {
        if (params != null) {
            StringBuilder fullUrl = new StringBuilder(url);
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            int i = 1;
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (i == 1) {
                    fullUrl.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                } else {
                    fullUrl.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
                iterator.remove();
                i++;
            }
            return fullUrl.toString();
        }
        return url;

    }

}

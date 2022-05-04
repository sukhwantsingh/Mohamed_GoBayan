package com.libyasolutions.libyamarketplace.network;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.libyasolutions.libyamarketplace.service.MessageService;
import com.libyasolutions.libyamarketplace.service.ServiceManager;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;

import java.util.Objects;

/**
 * Created by pham on 20/10/2015.
 */
public class ControllerRequest extends Application {
    private RequestQueue requestQueue;
    public static final String TAG = ControllerRequest.class.getSimpleName();
    private static ControllerRequest controller;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        controller = this;
        Fresco.initialize(getApplicationContext());
        if (!ServiceManager.getInstance(getApplicationContext()).isMyServiceRunning(MessageService.class)
                && MySharedPreferences.getInstance(getBaseContext()).getUserInfo() != null) {
            ServiceManager.getInstance(getApplicationContext()).registerService(MessageService.class);
        }
    }

    /**
     *
     * @return
     */

    public static ControllerRequest getInstance() {
        return controller;
    }

    /**
     *
     * @return trả về một đối tượng của RequestQueue sử dụng để gửi request
     */
    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    /**
     *
     * @param request một request bất kì
     * @param tag được sử dụng setTag cho request
     * @param <T> tham số extends từ Object
     */
   public  <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    /**
     *
     * @param request
     * @param <T> tham số extends từ Object
     */
    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);

    }

    /**
     *
     * @param tag
     */
    public void cancelRequest(Objects tag){
        if (requestQueue!=null){
            requestQueue.cancelAll(tag);
        }
    }
}

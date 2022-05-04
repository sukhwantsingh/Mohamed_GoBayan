package com.libyasolutions.libyamarketplace.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by GL62 on 7/4/2017.
 */

public class ServiceManager {

    private static ServiceManager instance;
    private Context context;

    private ServiceManager() {
    }

    public ServiceManager(Context context) {
        this.context = context;
    }

    public static ServiceManager getInstance(Context _context) {
        if (instance == null) {
            instance = new ServiceManager();
            instance.context = _context;
        }
        return instance;
    }

    public void registerService(Class<?> serviceClass) {
        Intent service = new Intent(context, MessageService.class);
        if (!isMyServiceRunning(MessageService.class)) {
            context.startService(service);
        }
    }

    public void unregisterService(Class<?> serviceClass) {
        Intent service = new Intent(context, MessageService.class);
        context.stopService(service);
    }

    //get Service is running on App
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

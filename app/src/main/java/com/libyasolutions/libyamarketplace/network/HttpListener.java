package com.libyasolutions.libyamarketplace.network;

/**
 * Created by phamtuan on 20/10/2015.
 */
public interface HttpListener<T> {
    void onHttpRespones(T respones);

}

package com.libyasolutions.libyamarketplace.interfaces;

import android.view.View;

public interface OnShopTimeListener {
    void onOpenAMClick(View view, int position);

    void onOpenPMClick(View view, int position);

    void onCloseAMClick(View view, int position);

    void onClosePMClick(View view, int position);
}

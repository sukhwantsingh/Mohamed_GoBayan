package com.libyasolutions.libyamarketplace.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MyButton extends android.support.v7.widget.AppCompatButton {
    public MyButton(Context context) {
        super(context);
        initTypeface();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface();
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeface();
    }

    private void initTypeface() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/TanseekModernProArabic-Medium.ttf");
        this.setTypeface(tf);
    }
}

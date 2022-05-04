package com.libyasolutions.libyamarketplace.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class CustomCheckbox extends CheckBox {

    public CustomCheckbox(Context context) {
        super(context);

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/TanseekModernProArabic-Medium.ttf");
        this.setTypeface(face);
    }

    public CustomCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/TanseekModernProArabic-Medium.ttf");
        this.setTypeface(face);
    }

    public CustomCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/TanseekModernProArabic-Medium.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}

package com.libyasolutions.libyamarketplace.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewFontSlogan extends TextView {

    public TextViewFontSlogan(Context context) {
        super(context);

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Far_Kamran.ttf");
        this.setTypeface(face);
    }

    public TextViewFontSlogan(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Far_Kamran.ttf");
        this.setTypeface(face);
    }

    public TextViewFontSlogan(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Far_Kamran.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}

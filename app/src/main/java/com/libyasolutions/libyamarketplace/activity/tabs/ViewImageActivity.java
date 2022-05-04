package com.libyasolutions.libyamarketplace.activity.tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.util.ImageUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ViewImageActivity extends BaseActivity {
    private ImageView imgFull, imgBack, imgMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Intent intent = getIntent();
        final String url = intent.getStringExtra("URL");
        imgFull = findViewById(R.id.imgFullScreen);
        imgMenu = findViewById(R.id.imgMenu);
        imgBack = findViewById(R.id.imgBack);
        imgMenu.setVisibility(View.INVISIBLE);

        Picasso.with(this).load(url).into(imgFull, new Callback() {
            @Override
            public void onSuccess() {
                Picasso.with(ViewImageActivity.this).load(url).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        int w = bitmap.getWidth();
                        int h = bitmap.getHeight();
                        ImageUtil.calViewRatio(ViewImageActivity.this, imgFull, w, h, 0);
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            @Override
            public void onError() {

            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}

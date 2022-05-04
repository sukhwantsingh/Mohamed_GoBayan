package com.libyasolutions.libyamarketplace.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.config.WebServiceConfig;
import com.libyasolutions.libyamarketplace.util.ImageUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by GL62 on 7/4/2017.
 */

public class FullImageActivity extends BaseActivity {
    private ImageView imgFullScreen, imgBack, imgMenu;
    private RelativeLayout llMenu;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_imageview);

        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra("Image");
        url = b.getString("url");

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        llMenu = (RelativeLayout) findViewById(R.id.llMenu);
        imgFullScreen = (ImageView) findViewById(R.id.imgFullScreen);

        Picasso.with(FullImageActivity.this).load(WebServiceConfig.FILES_API + url).into(imgFullScreen, new Callback() {
            @Override
            public void onSuccess() {
                Picasso.with(FullImageActivity.this).load(WebServiceConfig.FILES_API + url).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        int w = bitmap.getWidth();
                        int h = bitmap.getHeight();
                        ImageUtil.calViewRatio(FullImageActivity.this, imgFullScreen, w, h, 0);
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

        imgFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llMenu.getVisibility() == View.VISIBLE) {
                    llMenu.setVisibility(View.GONE);
                    llMenu.animate().translationY(-150).setDuration(150).start();
                } else {
                    llMenu.animate().translationY(0).setDuration(150).start();
                    llMenu.setVisibility(View.VISIBLE);
                }
            }
        });

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(FullImageActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.image_properties, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String urls = WebServiceConfig.FILES_API + url;
                        try {
                            Uri uri = Uri.parse(urls);
                            Intent i = new Intent(Intent.ACTION_VIEW, uri);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            showToastMessage(getString(R.string.install_chrome));
                        }
                        return false;
                    }
                });
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fadein,
                        R.anim.fadeout);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fadein,
                R.anim.fadeout);
    }
}

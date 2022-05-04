package com.libyasolutions.libyamarketplace.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.ViewImageActivity;

@SuppressLint("NewApi")
public final class BannerFragment extends Fragment {

    private View view;
    private ImageView imgBanner;
    public String urlImage;

    public static BannerFragment InStances(String urlImage) {
        BannerFragment fragment = new BannerFragment();
        fragment.urlImage = urlImage;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_layout_banner, container, false);
        imgBanner = (ImageView) view.findViewById(R.id.imgBanner);
        Glide.with(getActivity())
                .load(urlImage)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imgBanner)
        ;

        imgBanner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urlImage.equals("")) {

                } else {
                    Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                    intent.putExtra("URL", urlImage);
                    getActivity().startActivity(intent);
                }

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

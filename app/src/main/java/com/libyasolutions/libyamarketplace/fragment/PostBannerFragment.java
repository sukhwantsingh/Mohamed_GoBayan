package com.libyasolutions.libyamarketplace.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.ViewImageActivity;

@SuppressLint("NewApi")
public final class PostBannerFragment extends Fragment {

    private View view;
    private ImageView imgBanner;
    private TextView postDesc;
    public String urlImage;
    public String urlName;

    public static PostBannerFragment InStances(String urlImage, String urlName) {
        PostBannerFragment fragment = new PostBannerFragment();
        fragment.urlImage = urlImage;
        fragment.urlName = urlName;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_layout_post_banners, container, false);
        imgBanner = (ImageView) view.findViewById(R.id.imgBanner);
        postDesc = (TextView) view.findViewById(R.id.post_details);
        postDesc.setText("" + urlName);

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
                .error(R.drawable.no_image_available_horizontal)
                .placeholder(R.drawable.no_image_available_horizontal)
                .into(imgBanner);

        imgBanner.setOnClickListener(v -> {
            if (urlImage.equals("")) {

            } else {
                Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                intent.putExtra("URL", urlImage);
                getActivity().startActivity(intent);
            }

        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

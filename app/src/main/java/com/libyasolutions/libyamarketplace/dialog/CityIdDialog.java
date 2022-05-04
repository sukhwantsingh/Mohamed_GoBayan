package com.libyasolutions.libyamarketplace.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.libyasolutions.libyamarketplace.BaseDialog;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ShopCityAdapter;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.City;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CityIdDialog extends BaseDialog {

    @BindView(R.id.rv_city)
    RecyclerView rvCity;
    @BindView(R.id.iv_arrow_bottom)
    ImageView ivArrowBottom;

    private OnSearchByCityIdListener listener;
    private List<City> cityList;

    public static CityIdDialog newInstance() {
        Bundle args = new Bundle();
        CityIdDialog fragment = new CityIdDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.dialog_city_search;
    }

    @Override
    protected void configView() {
        // config recycler view
        rvCity.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvCity.setLayoutManager(manager);

        MySharedPreferences pref = new MySharedPreferences(getActivity());
        cityList = ParserUtility.parseListCity(pref.getCacheCities());

        ShopCityAdapter shopCityAdapter = new ShopCityAdapter(getActivity(), cityList);
        rvCity.setAdapter(shopCityAdapter);

        // click to item city
        shopCityAdapter.setOnItemClickListener((view, position) -> {
            if (listener != null) {
                City city = cityList.get(position);
                if (city != null) {
                    listener.onSearchByCityId(city.getId(), city.getName());
                }
            }
            dismiss();
        });

        rvCity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    ivArrowBottom.setVisibility(View.GONE);
                }  else {
                    ivArrowBottom.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected int getStyleDialog() {
        return 0;
    }

    @Override
    protected double initWidthDialog() {
        return 0.85;
    }

    @Override
    protected double initHeightDialog() {
        return 0;
    }

    @Override
    protected boolean isCancelOnTouchOutside() {
        return true;
    }

    public void setOnSearchByCityIdListener(OnSearchByCityIdListener listener) {
        this.listener = listener;
    }

    public interface OnSearchByCityIdListener {
        void onSearchByCityId(String cityId, String cityName);
    }
}

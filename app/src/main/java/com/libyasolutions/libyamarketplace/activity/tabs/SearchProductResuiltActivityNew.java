package com.libyasolutions.libyamarketplace.activity.tabs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ListFoodAdapterNew;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

public class SearchProductResuiltActivityNew extends BaseActivity implements View.OnClickListener {
    private ImageView btnBack;
    private TextView tvFoodQuality;
    private com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout SwipyRefreshLayout;
    private RecyclerView rclViewShop;
    private ArrayList<Menu> arrProducts = new ArrayList<Menu>();
    private ListFoodAdapterNew listFoodAdapterNew;

    private int page = 1;
    private String searchKey = "";
    private String categoryId = "";
    private String cityId = "";
    private String open = "";
    private String distance = "";
    private String sortBy = "";
    private String sortType = "";
    private String lat = "";
    private String lon = "";
    private boolean isMore = true;
    private ImageView iv_show_more;


    private LinearLayout tabProfile, tabCart, tabSearch, tabHome;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        initView();
        getParamSearch();
        initControl();
        getDataSearch(1, true);
        iv_show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataSearch(page++, false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalValue.arrMyMenuShop.size() != 0) {
            tabCart.setBackgroundColor(Color.BLUE);
        } else {
            tabCart.setBackgroundColor(getResources().getColor(R.color.background_new));
        }
    }

    private void initView() {
        iv_show_more = findViewById(R.id.iv_show_more);
        SwipyRefreshLayout = findViewById(R.id.refreshLayout);
        btnBack = findViewById(R.id.btnBack);
        rclViewShop = findViewById(R.id.rclViewShop);
        tvFoodQuality = findViewById(R.id.tvFoodQuality);
        tabProfile = findViewById(R.id.tab_profile);
        tabCart = findViewById(R.id.tab_cart);
        tabSearch = findViewById(R.id.tab_search);
        tabHome = findViewById(R.id.tab_home);
    }

    private void getParamSearch() {
        Bundle b = getIntent().getExtras();
        searchKey = b.getString(GlobalValue.KEY_SEARCH);
        cityId = b.getString(GlobalValue.KEY_CITY_ID);
        categoryId = b.getString(GlobalValue.KEY_CATEGORY_ID);
        open = b.getString(GlobalValue.KEY_OPEN);
        distance = b.getString(GlobalValue.KEY_DISTANCE);
        sortBy = b.getString(GlobalValue.KEY_SORT_BY);
        sortType = b.getString(GlobalValue.KEY_SORT_TYPE);
        lat = b.getString(GlobalValue.KEY_LAT);
        lon = b.getString(GlobalValue.KEY_LONG);
    }

    private void initControl() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SwipyRefreshLayout.setOnRefreshListener(swipyRefreshLayoutDirection -> {
            Log.d("MainActivity", "Refresh triggered at "
                    + (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
            if (isMore) {
                page++;
            }
            getDataSearch(page, true);
            SwipyRefreshLayout.setRefreshing(false);

        });

        tabProfile.setOnClickListener(this);
        tabCart.setOnClickListener(this);
        tabSearch.setOnClickListener(this);
        tabHome.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclViewShop.setLayoutManager(layoutManager);
        listFoodAdapterNew = new ListFoodAdapterNew(rclViewShop, getApplicationContext(), arrProducts);
        rclViewShop.setAdapter(listFoodAdapterNew);


    }

    private void getDataSearch(int pagee, boolean isPull) {
        Log.e("HUY", String.valueOf(page));
        if (page <= 1) {
            arrProducts.clear();
        }

        ModelManager.getListFoodBySearch(self, searchKey, categoryId, cityId,
                page, open, distance, sortBy, sortType, lat, lon, isPull,
                new ModelManagerListener() {

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(Object object) {
                        String json = (String) object;
                        if (ParserUtility.isSuccess(json)) {
                            int count = ParserUtility.ParseCount(json);
                            tvFoodQuality.setText(String.valueOf(count) + " " + getString(R.string.menu_found));
                        }
                        ArrayList<Menu> arr = ParserUtility.parseListFoodInSearch(json);
                        if (arr.size() > 0) {
                            iv_show_more.setVisibility(View.VISIBLE);
                            isMore = true;
                            arrProducts.addAll(arr);
                            listFoodAdapterNew.notifyDataSetChanged();
                        } else {
                            isMore = false;
                            iv_show_more.setVisibility(View.GONE);
                            Toast.makeText(self, getResources().getString(R.string.have_no_more_date),
                                    Toast.LENGTH_SHORT).show();
                        }
                        //check show list
                    }
                });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_profile:
                sendAction(Constant.SHOW_TAB_PROFILE);
                break;
            case R.id.tab_cart:
                sendAction(Constant.SHOW_TAB_CART);
                break;
            case R.id.tab_search:
                sendAction(Constant.SHOW_TAB_SEARCH);
                break;
            case R.id.tab_home:
                sendAction(Constant.SHOW_TAB_HOME);
                break;
        }
    }

    private void sendAction(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
        finish();
    }
}

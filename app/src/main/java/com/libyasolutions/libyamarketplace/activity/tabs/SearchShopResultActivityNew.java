package com.libyasolutions.libyamarketplace.activity.tabs;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ShopAdapterNew;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

public class SearchShopResultActivityNew extends BaseActivity {
    private ImageView btnBack;
    private RecyclerView rclViewShop;
    private TextView tvShopQuality;
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
    private int page = 1;
    private ArrayList<Shop> arrShop = new ArrayList<>();
    private ShopAdapterNew shopAdapterNew;
    private com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout SwipyRefreshLayout;
    private ImageView iv_show_more;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);
        getParamSearch();
        initView();
        initControl();
        getDataSearch(1, true);
        iv_show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataSearch(page++, true);
            }
        });
    }

    private void initControl() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                Log.d("MainActivity", "Refresh triggered at "
                        + (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                if (isMore) {
                    page++;
                }
                getDataSearch(page, true);
                SwipyRefreshLayout.setRefreshing(false);

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclViewShop.setLayoutManager(layoutManager);
        shopAdapterNew = new ShopAdapterNew(rclViewShop, arrShop, getApplicationContext());
        rclViewShop.setAdapter(shopAdapterNew);


    }

    private void initView() {
        iv_show_more = findViewById(R.id.iv_show_more);
        SwipyRefreshLayout = findViewById(R.id.refreshLayout);
        btnBack = findViewById(R.id.btnBack);
        rclViewShop = findViewById(R.id.rclViewShop);
        tvShopQuality = findViewById(R.id.tvShopQuality);
    }

    private void getParamSearch() {
        // get data search ;
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


    private void getDataSearch(int pagee, boolean isPull) {
        Log.e("HUY", String.valueOf(page));
        if (page <= 1) {
            arrShop.clear();
        }
        ModelManager.getListShopBySearch(this, searchKey, categoryId, cityId,
                page, open, distance, sortBy, sortType, lat, lon, isPull,
                new ModelManagerListener() {
                    @Override
                    public void onError(VolleyError error) {
                        Toast.makeText(SearchShopResultActivityNew.this, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(Object object) {
                        String json = (String) object;
                        List<Shop> arr = ParserUtility.getListShop(json);
                        if (ParserUtility.isSuccess(json)) {
                            int count = ParserUtility.ParseCount(json);
                            tvShopQuality.setText((String.valueOf(count) + " " + getString(R.string.restaurant_found)));
                        }
                        if (arr.size() > 0) {
                            isMore = true;
                            iv_show_more.setVisibility(View.VISIBLE);
                            arrShop.addAll(arr);
                            shopAdapterNew.notifyDataSetChanged();
                        } else {
                            isMore = false;
                            iv_show_more.setVisibility(View.GONE);
                            Toast.makeText(SearchShopResultActivityNew.this, getResources().getString(R.string.have_no_more_date),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}

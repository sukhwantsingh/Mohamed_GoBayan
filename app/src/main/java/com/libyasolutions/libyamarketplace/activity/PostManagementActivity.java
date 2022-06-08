package com.libyasolutions.libyamarketplace.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.PostManagementAdapter;
import com.libyasolutions.libyamarketplace.adapter.ShopManagementAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

public class PostManagementActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private SwipyRefreshLayout swipyRefreshLayout;
    private RecyclerView rcvProduct;
    private FloatingActionButton fabNewShop;
    private TextView tvShopQuality;

    private int page = 1;
    private ArrayList<Shop> listShops = new ArrayList<>();
    private PostManagementAdapter postManagementAdapter;
    private boolean isMore = true;

    private BroadcastReceiver broadcastReceiver;

    private ConstraintLayout cnsSearchShort;
    private String shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_management);

        if(null != getIntent()) shopId = getIntent().getStringExtra(Constant.SHOP_ID);

        initViews();
        initData();
        initControl();
        mListener();
    }

    private void mListener() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.REFRESH_DATA)) {
                    page = 1;
                    getListShopByAccount(true, page);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.REFRESH_DATA);
        registerReceiver(broadcastReceiver, filter);
    }

    private void initViews() {
        ivBack = findViewById(R.id.imageBack);
        tvTitle = findViewById(R.id.lblTitle);

        cnsSearchShort = findViewById(R.id.cns_search_sort);
        fabNewShop = findViewById(R.id.fab_new_shop);
        swipyRefreshLayout = findViewById(R.id.refreshLayout);
        tvShopQuality = findViewById(R.id.tv_shop_quality);

        rcvProduct = findViewById(R.id.rv_shop);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new LinearLayoutManager(self));

        // visibility
        cnsSearchShort.setVisibility(View.VISIBLE);

    }

    private void initData() {
        tvTitle.setText(getString(R.string.post_management));

        postManagementAdapter = new PostManagementAdapter(self, listShops);
        rcvProduct.setAdapter(postManagementAdapter);

        getListShopByAccount(true, page);
    }

    private void getListShopByAccount(boolean isPull, int page) {
        if (page <= 1) {
            listShops.clear();
        }
        ModelManager.getListShopByAccount(this, /*"1432968757"*/ GlobalValue.myAccount.getId(), page, isPull, new ModelManagerListener() {

            @Override
            public void onSuccess(Object object) {
                String json = (String) object;
                ArrayList<Shop> arr = ParserUtility.getListShopByAccount(json);
                if (arr.size() > 0) {
                    isMore = true;
                    listShops.addAll(ParserUtility.getListShopByAccount(json));
                    Log.e("kevin", "list shop size: " + listShops.size());
                } else {
                    isMore = false;
                    Toast.makeText(self, getResources().getString(R.string.have_no_more_date),
                            Toast.LENGTH_SHORT).show();
                }
                postManagementAdapter.notifyDataSetChanged();
                String shopCount = "<b>" + ParserUtility.ParseCount(object.toString()) + "</b>";
                tvShopQuality.setText(Html.fromHtml(getString(R.string.shop_found, shopCount)));
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initControl() {
        swipyRefreshLayout.setOnRefreshListener(swipyRefreshLayoutDirection -> {
            Log.d("MainActivity", "Refresh triggered at " + (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
            if (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP) {
                page = 1;
                getListShopByAccount(true, page);
            } else {
                if (isMore) {
                    page++;
                }
                getListShopByAccount(true, page);
            }

            swipyRefreshLayout.setRefreshing(false);

        });

        ivBack.setOnClickListener(this);
        fabNewShop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.fab_new_shop:
                AddNewPostActivity.startActivity(this);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
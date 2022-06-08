package com.libyasolutions.libyamarketplace.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ManageOrderAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.ConstantApp;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.dialog.OrderStatusDialog;
import com.libyasolutions.libyamarketplace.interfaces.AdapterListener;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.ShopOrder;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

public class ManageOrderActivity extends BaseActivity {

    private ImageView btnBack;
    private SwipyRefreshLayout swipyRefreshLayout;
    private RecyclerView rcvOrder;
    private TextView tvOrderQuality;
    private ImageView ivShowMore;
    private FrameLayout containerSort;
    private ImageView ivStatus;
    private ImageView ivSort;
    private EditText edtSearch;
    private FrameLayout containerSearch;

    private int page = 1;
    private boolean isMore = true;
    private String sortType = "desc";
    private String sortBy = "date";
    private String orderStatus = ""; // order new

    private ArrayList<ShopOrder> shopOrders = new ArrayList<>();
    private ManageOrderAdapter adapter;
    private BroadcastReceiver broadcastReceiver;
    private MySharedPreferences mySharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);
        initView();
        initControls();
        initData();
        mListener();
    }

    private void mListener() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.REFRESH_DATA)) {
                    getShopOrders(true);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.REFRESH_DATA);
        registerReceiver(broadcastReceiver, filter);
    }

    private void initData() {
        mySharedPreferences = new MySharedPreferences(this);
        adapter = new ManageOrderAdapter(shopOrders, ManageOrderActivity.this);
        rcvOrder.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterListener.onItemClickListener() {
            @Override
            public void onclick(View view, int position) {
                GlobalValue.currentShopOrder = shopOrders.get(position);
                gotoActivity(ManageOrderDetailActivityV2.class);
            }
        });

        page = 1;
        getShopOrders(true);

    }

    private void getShopOrders(boolean isPull) {
        if (page <= 1) {
            shopOrders.clear();
        }
        ModelManager.showOrderByShop2(this, mySharedPreferences.getUserInfo().getId(), page,
                sortType, sortBy, edtSearch.getText().toString(), orderStatus
                ,isPull, new ModelManagerListener() {

            @Override
            public void onSuccess(Object object) {
                String json = (String) object;
                ArrayList<ShopOrder> arr = ParserUtility.parseListShopOrder(json);
                if (arr.size() > 0) {
                    isMore = true;
                    shopOrders.addAll(ParserUtility.parseListShopOrder(json));
                    ivShowMore.setVisibility(View.VISIBLE);
                } else {
                    isMore = false;
                    Toast.makeText(self, getResources().getString(R.string.have_no_more_date),
                            Toast.LENGTH_SHORT).show();
                    ivShowMore.setVisibility(View.GONE);
                }
                String orderCount = "<b>"+ParserUtility.ParseCount(object.toString())+"</b>";
                tvOrderQuality.setText(Html.fromHtml(getString(R.string.order_found, orderCount)));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initControls() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipyRefreshLayout.setOnRefreshListener(new com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                Log.d("MainActivity", "Refresh triggered at "
                        + (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                if (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP) {
                    page = 1;
                    getShopOrders(true);
                } else {
                    if (isMore) {
                        page++;
                    }
                    getShopOrders(true);
                }

                swipyRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void initView() {

        btnBack = findViewById(R.id.iv_back);
        rcvOrder = findViewById(R.id.rcv_order);
        swipyRefreshLayout = findViewById(R.id.refreshLayout);
        tvOrderQuality = findViewById(R.id.tv_order_quality);
        ivShowMore = findViewById(R.id.iv_show_more);
        containerSort = findViewById(R.id.container_sort);
        ivSort = findViewById(R.id.iv_sort);
        edtSearch = findViewById(R.id.edt_search);
        ivStatus = findViewById(R.id.iv_status);
        containerSearch = findViewById(R.id.container_search);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvOrder.setLayoutManager(layoutManager);

        ivShowMore.setOnClickListener(view -> {
            page++;
            getShopOrders(true);
        });

        containerSort.setOnClickListener(view -> {
            if (sortType.equals(ConstantApp.SORT_TYPE_ASC)) {
                ivSort.setImageResource(R.drawable.ic_arrows_exchange_alt_v);
                sortType = ConstantApp.SORT_TYPE_DESC;
            } else if (sortType.equals(ConstantApp.SORT_TYPE_DESC)) {
                ivSort.setImageResource(R.drawable.ic_arrow_exchange_alt_v_up);
                sortType = ConstantApp.SORT_TYPE_ASC;
            }

            getShopOrders(true);

        });

        ivStatus.setOnClickListener(view -> {
            OrderStatusDialog dialog = OrderStatusDialog.newInstance(orderStatus);
            dialog.show(getSupportFragmentManager(), dialog.getTag());

            dialog.setOnOrderStatusListener(status -> {
                orderStatus = status;
                getShopOrders(true);
            });
        });

        containerSearch.setOnClickListener(view -> {
            page = 1;
            getShopOrders(true);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}

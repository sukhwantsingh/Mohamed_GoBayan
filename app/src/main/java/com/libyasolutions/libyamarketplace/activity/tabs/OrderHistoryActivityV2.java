package com.libyasolutions.libyamarketplace.activity.tabs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivityV2;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.HistoryAdapterNew;
import com.libyasolutions.libyamarketplace.config.ConstantApp;
import com.libyasolutions.libyamarketplace.dialog.OrderStatusDialog;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.OrderGroup;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderHistoryActivityV2 extends BaseActivityV2 {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container_search)
    FrameLayout containerSearch;
    @BindView(R.id.iv_sort)
    ImageView ivSort;
    @BindView(R.id.container_sort)
    FrameLayout containerSort;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.tv_order_quality)
    TextView tvOrderQuality;
    @BindView(R.id.rv_order_history)
    RecyclerView rvOrderHistory;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.iv_show_more)
    ImageView ivShowMore;


    private List<OrderGroup> orderGroupList = new ArrayList<>();
    private HistoryAdapterNew adapter;

    private int page = 1;
    private boolean isMore = true;
    private String sortType = "desc";
    private String sortBy = "date";
    private String orderStatus = ""; // order new
    private MySharedPreferences mySharedPreferences;

    @Override
    protected void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_history_v2;
    }

    @Override
    protected void initData() {
        mySharedPreferences = new MySharedPreferences(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("CHANGE_ORDER_STATUS"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("CHANGE_ORDER_STATUS");
            if (message.equals("CHANGE_ORDER_STATUS")) {
                getOrderHistory();
            }
        }
    };

    @Override
    protected void configView() {
        refreshLayout.setOnRefreshListener(swipyRefreshLayoutDirection -> {
            if (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP) {
                page = 1;
                getOrderHistory();
            } else {
                if (isMore) {
                    page++;
                }
                getOrderHistory();
            }
            refreshLayout.setRefreshing(false);
        });


        adapter = new HistoryAdapterNew(orderGroupList, OrderHistoryActivityV2.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderHistoryActivityV2.this);
        rvOrderHistory.setLayoutManager(layoutManager);
        rvOrderHistory.setAdapter(adapter);

        page = 1;
        getOrderHistory();
    }

    @OnClick(R.id.iv_back)
    void chooseBack() {
        Intent intent = new Intent(this, MainTabActivity.class);
        intent.putExtra("CODE", 99);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @OnClick(R.id.iv_show_more)
    void chooseShowMore() {
        page++;
        getOrderHistory();
    }

    private void getOrderHistory() {
        if (page <= 1) {
            orderGroupList.clear();
        }

        if (NetworkUtil.checkNetworkAvailable(this)) {
            ModelManager.getListOrder2(this, mySharedPreferences.getUserInfo().getId(),
                    page, sortType, sortBy, edtSearch.getText().toString(), orderStatus,
                    true, new ModelManagerListener() {

                        @Override
                        public void onSuccess(Object object) {
                            if (ParserUtility.isSuccess(object.toString())) {
                                ArrayList<OrderGroup> list = ParserUtility
                                        .parseListOrderGroup(object.toString());
                                if (list.size() > 0) {
                                    isMore = true;
                                    orderGroupList.addAll(list);
                                    ivShowMore.setVisibility(View.VISIBLE);
                                } else {
                                    isMore = false;
                                    ivShowMore.setVisibility(View.GONE);
                                    showToast(R.string.no_more_data);
                                }
                            } else {
                                showToast(ParserUtility.getMessage(object.toString()));
                            }
                            String orderCount = "<b>"+ParserUtility.ParseCount(object.toString())+"</b>";
                            tvOrderQuality.setText(Html.fromHtml(getString(R.string.order_found, orderCount)));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            showToast(ErrorNetworkHandler.processError(error));
                        }
                    });
        } else {
            showToast(R.string.no_connection);
        }
    }

    @OnClick(R.id.container_sort)
    void chooseOrderSearchType() {
        if (sortType.equals(ConstantApp.SORT_TYPE_ASC)) {
            ivSort.setImageResource(R.drawable.ic_arrows_exchange_alt_v);
            sortType = ConstantApp.SORT_TYPE_DESC;
        } else if (sortType.equals(ConstantApp.SORT_TYPE_DESC)) {
            ivSort.setImageResource(R.drawable.ic_arrow_exchange_alt_v_up);
            sortType = ConstantApp.SORT_TYPE_ASC;
        }

        getOrderHistory();
    }

    @OnClick(R.id.iv_status)
    void chooseOrderStatus() {
        OrderStatusDialog dialog = OrderStatusDialog.newInstance(orderStatus);
        dialog.show(getSupportFragmentManager(), dialog.getTag());

        dialog.setOnOrderStatusListener(status -> {
            orderStatus = status;
            getOrderHistory();
        });
    }

    @OnClick(R.id.container_search)
    void search() {
        page = 1;
        getOrderHistory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
}

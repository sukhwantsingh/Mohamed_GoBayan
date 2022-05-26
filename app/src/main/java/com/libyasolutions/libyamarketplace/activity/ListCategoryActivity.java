package com.libyasolutions.libyamarketplace.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ListCategoryAdapter;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Category;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

public class ListCategoryActivity extends BaseActivity implements
        OnClickListener {

    private TextView lblShopName;
    private ImageView btnBack;
    private ArrayList<Category> arrCategories = new ArrayList<Category>();
    private ListView lsvCategory;
    private ListCategoryAdapter categoryAdapter;
    private int shopId = -1;
    private String shopName = "";
    public static BaseActivity self;

    private int page = 1;
    private boolean isMore = true;
    private ImageView ivShowMore;
    private SwipyRefreshLayout swipyRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        self = this;
        initUI();
        initControl();
        initData();

    }

    private void initUI() {
        ivShowMore = findViewById(R.id.iv_show_more);
        swipyRefreshLayout = findViewById(R.id.refreshLayout);

        lsvCategory = (ListView) findViewById(R.id.lsvCategory);
        lblShopName = (TextView) findViewById(R.id.tvNameShop);
        btnBack = (ImageView) findViewById(R.id.btnBack);
    }

    private void initControl() {
        categoryAdapter = new ListCategoryAdapter(self, arrCategories);
        lsvCategory.setAdapter(categoryAdapter);
        lsvCategory.setOnItemClickListener((arg0, arg1, index, arg3) -> {

            if (NetworkUtil.checkNetworkAvailable(self)) {
                Category category = arrCategories.get(index);
                Bundle b = new Bundle();
                b.putInt(GlobalValue.KEY_SHOP_ID, shopId);
                b.putString(GlobalValue.KEY_SHOP_NAME, shopName);
                b.putString(GlobalValue.KEY_CATEGORY_ID, category.getId());
                b.putString(GlobalValue.KEY_CATEGORY_NAME,
                        category.getName());

                gotoActivity(self, ListFoodActivity.class, b);
            } else {
                Toast.makeText(self,
                        R.string.message_network_is_unavailable,
                        Toast.LENGTH_LONG).show();
            }

        });
        btnBack.setOnClickListener(this);
        swipyRefreshLayout.setOnRefreshListener(swipyRefreshLayoutDirection -> {
            Log.d("MainActivity", "Refresh triggered at " + (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
            if (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP) {
                page = 1;
               initData();
            } else {
                if (isMore) {
                    page++;
                }
                initData();
            }

            swipyRefreshLayout.setRefreshing(false);

        });
        ivShowMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMore) {
                    page++;
                }
                initData();
            }
        });
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(GlobalValue.KEY_SHOP_ID)) {
                shopId = b.getInt(GlobalValue.KEY_SHOP_ID);
            }

            if (b.containsKey(GlobalValue.KEY_SHOP_NAME)) {
                shopName = b.getString(GlobalValue.KEY_SHOP_NAME);
             //   lblShopName.setText(shopName);
            }
        }

        if (shopId != -1) {
            if (page <= 1) {
                arrCategories.clear();
            }
            if (!NetworkUtil.checkNetworkAvailable(this)) {
                Toast.makeText(this, R.string.message_network_is_unavailable, Toast.LENGTH_LONG).show();
            } else
                ModelManager.getListCategoryByShop(self, shopId, page, true,
                        new ModelManagerListener() {

                            @Override
                            public void onSuccess(Object object) {
                                // TODO Auto-generated method stub
                                String json = (String) object;
                                ArrayList<Category> arr = ParserUtility.parseListCategories(json);
                                if (arr.size() > 0) {
                                    isMore = true;
                                    arrCategories.clear();
                                    arrCategories.addAll(ParserUtility
                                            .parseListCategories(json));
                                } else {
                                    isMore = false;
                                    Toast.makeText(self, getResources().getString(R.string.have_no_more_date),
                                            Toast.LENGTH_SHORT).show();
                                }
                                ivShowMore.setVisibility(isMore ? View.VISIBLE : View.GONE);
//                                if(ParserUtility
//                                        .parseListCategories(json).size() == 0){
//                                    showToastMessage(getResources().getString(R.string.have_no_date));
//                                }else{
//                                    arrCategories.clear();
//                                    arrCategories.addAll(ParserUtility
//                                            .parseListCategories(json));
//                                    categoryAdapter.notifyDataSetChanged();
//                                }
                                categoryAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(VolleyError error) {
                                // TODO Auto-generated method stub
                                arrCategories.clear();
                                categoryAdapter.notifyDataSetChanged();
                                Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                            }
                        });
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnBack) {
            onBackPressed();
        }
    }
}

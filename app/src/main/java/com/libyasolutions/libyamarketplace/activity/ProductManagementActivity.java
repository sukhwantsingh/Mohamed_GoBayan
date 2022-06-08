package com.libyasolutions.libyamarketplace.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ProductManagementAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.ConstantApp;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.interfaces.AdapterListener;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

public class ProductManagementActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;

    private int page = 1;
    private ArrayList<Menu> listProducts = new ArrayList<>();
    private boolean isMore = true;

    private FloatingActionButton fabNewProduct;

    private SwipyRefreshLayout swipyRefreshLayout;
    private RecyclerView rcvProduct;
    private TextView tvProductQuality;
    private FrameLayout containerSearch;
    private LinearLayout containerSort;
    private ImageView ivSort;
    private EditText edtSearch;
    private ImageView ivShowMore;

    private ProductManagementAdapter productManagementAdapter;

    private String shopId;
    private BroadcastReceiver broadcastReceiver;
    private MySharedPreferences mySharedPreferences;

    private String sortBy = "desc";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);
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
                    getMyProduct(true, page);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.REFRESH_DATA);
        registerReceiver(broadcastReceiver, filter);
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setSelected(true);

        fabNewProduct = findViewById(R.id.fab_new_product);

        swipyRefreshLayout = findViewById(R.id.refreshLayout);

        rcvProduct = findViewById(R.id.rcv_product);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new LinearLayoutManager(self));

        tvProductQuality = findViewById(R.id.tv_product_quality);
        containerSearch = findViewById(R.id.container_search);
        containerSort = findViewById(R.id.container_sort);
        ivSort = findViewById(R.id.iv_sort);
        edtSearch = findViewById(R.id.edt_search);
        ivShowMore = findViewById(R.id.iv_show_more);
    }

    private void initData() {
        mySharedPreferences = new MySharedPreferences(this);

        final Intent intent = getIntent();
        shopId = intent.getStringExtra(Constant.SHOP_ID);
        Log.e(TAG, "shopId: " + shopId);

        productManagementAdapter = new ProductManagementAdapter(self, listProducts);
        rcvProduct.setAdapter(productManagementAdapter);

        // update product
        productManagementAdapter.setOnItemClickListener((view, position) -> {
            Menu product = listProducts.get(position);
            Intent intent1 = new Intent(self, AddNewProductActivityV2.class);
            intent1.putExtra(Constant.PRODUCT_OBJ, product);
            intent1.putExtra(Constant.SHOP_ID, shopId);
            GlobalValue.currentProduct = product;
            startActivity(intent1);
        });

        // delete this product
        productManagementAdapter.setOnItemProductManagement((view, position) -> {
            Dialog mDialog = new Dialog(this);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.dialog_convert_to_shop_onwer);

            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);

            Window window = mDialog.getWindow();
            if (window != null) {
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                window.setLayout(width, height);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            Button btnNo = mDialog.findViewById(R.id.btn_no);
            Button btnYes = mDialog.findViewById(R.id.btn_yes);
            TextView tvMessage = mDialog.findViewById(R.id.tv_dialog_message);
            tvMessage.setText(getString(R.string.you_want_delete_this_product));

            // no
            btnNo.setOnClickListener(view1 -> {
                mDialog.dismiss();
            });

            // delete product
            btnYes.setOnClickListener(view2 -> {
                Menu product = listProducts.get(position);
                deleteProductByProductId(String.valueOf(product.getId()));
                mDialog.dismiss();
            });

            mDialog.show();
        });

        page = 1;
        getMyProduct(true, page);
    }

    private void deleteProductByProductId(String productId) {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            // call api here
            ModelManager.deleteProduct(this, mySharedPreferences.getUserInfo().getId(), productId,
                    true, new ModelManagerListener() {
                        @Override
                        public void onError(VolleyError error) {
                            showToastMessage(ErrorNetworkHandler.processError(error));
                        }

                        @Override
                        public void onSuccess(Object object) {
                            if (ParserUtility.isSuccess(object.toString())) {
                                showToastMessage(R.string.delete_product_success);
                                getMyProduct(true, 1);
                            } else {
                                showToastMessage(ParserUtility.getMessage(object.toString()));
                            }
                        }
                    });
        } else {
            showToastMessage(R.string.no_connection);
        }
    }

    private void getMyProduct(boolean isPull, int page) {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            if (page <= 1) {
                listProducts.clear();
            }

            String keyword = edtSearch.getText().toString().trim();
            ModelManager.getMyProducts(this, shopId, sortBy, keyword, page, isPull, new ModelManagerListener() {

                @Override
                public void onSuccess(Object object) {
                    String json = (String) object;
                    ArrayList<Menu> arr = ParserUtility.parseListMyProduct(json);
                    if (arr.size() > 0) {
                        isMore = true;
                        listProducts.addAll(ParserUtility.parseListMyProduct(json));
                        ivShowMore.setVisibility(View.VISIBLE);
                    } else {
                        isMore = false;
                        Toast.makeText(self, getResources().getString(R.string.have_no_more_date),
                                Toast.LENGTH_SHORT).show();
                        ivShowMore.setVisibility(View.GONE);
                    }
                    productManagementAdapter.notifyDataSetChanged();
                    String productCount = "<b>" + ParserUtility.ParseCount(object.toString()) + "</b>";
                    tvProductQuality.setText(Html.fromHtml(getString(R.string.product_found, productCount)));
                }

                @Override
                public void onError(VolleyError error) {
                    Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            showToastMessage(R.string.no_connection);
        }
    }

    private void initControl() {
        swipyRefreshLayout.setOnRefreshListener(new com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                Log.d("MainActivity", "Refresh triggered at "
                        + (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                if (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP) {
                    page = 1;
                    getMyProduct(true, page);
                } else {
                    if (isMore) {
                        page++;
                    }
                    getMyProduct(true, page);
                }

                swipyRefreshLayout.setRefreshing(false);

            }
        });


        ivBack.setOnClickListener(this);
        fabNewProduct.setOnClickListener(this);
        containerSearch.setOnClickListener(this);
        containerSort.setOnClickListener(this);
        ivShowMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.fab_new_product:
                Intent intent = new Intent(self, AddNewProductActivityV2.class);
                intent.putExtra(Constant.SHOP_ID, shopId);
                startActivity(intent);
//                gotoActivity(AddNewProductActivity.class);
                break;
            case R.id.container_sort: {
                if (sortBy.equals(ConstantApp.SORT_TYPE_ASC)) {
                    ivSort.setImageResource(R.drawable.ic_arrows_exchange_alt_v);
                    sortBy = ConstantApp.SORT_TYPE_DESC;
                } else if (sortBy.equals(ConstantApp.SORT_TYPE_DESC)) {
                    ivSort.setImageResource(R.drawable.ic_arrow_exchange_alt_v_up);
                    sortBy = ConstantApp.SORT_TYPE_ASC;
                }
                break;
            }
            case R.id.container_search: {
                page = 1;
                getMyProduct(true, page);
                break;
            }
            case R.id.iv_show_more: {
                page++;
                getMyProduct(true, page);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}

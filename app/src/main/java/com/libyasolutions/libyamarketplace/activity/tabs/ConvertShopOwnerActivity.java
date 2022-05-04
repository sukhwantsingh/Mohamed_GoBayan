package com.libyasolutions.libyamarketplace.activity.tabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivityV2;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.SplashActivity;
import com.libyasolutions.libyamarketplace.adapter.ShopCategoryAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.dialog.CityIdDialog;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.object.Category;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class ConvertShopOwnerActivity extends BaseActivityV2 {

    private static final String TAG = "ConvertShopOwnerActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_waiting_approval)
    ConstraintLayout layoutWaitingApproval;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.edt_shop_name)
    EditText edtShopName;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.edt_category)
    EditText edtCategory;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.edt_city)
    EditText edtCity;
    @BindView(R.id.layout_shop_info)
    ConstraintLayout layoutShopInfo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.tv_shop_phone)
    TextView tvPhone;
    @BindView(R.id.edt_shop_phone)
    EditText edtPhone;
    @BindView(R.id.layout_shop_owner_info)
    ConstraintLayout layoutShopOwnerInfo;
    @BindView(R.id.iv_empty_shop_name)
    ImageView ivEmptyShopName;
    @BindView(R.id.iv_empty_category)
    ImageView ivEmptyCategory;
    @BindView(R.id.iv_empty_city)
    ImageView ivEmptyCity;
    @BindView(R.id.iv_empty_name)
    ImageView ivEmptyName;
    @BindView(R.id.iv_empty_shop_phone)
    ImageView ivEmptyPhone;
    @BindView(R.id.btn_shop_owner_info)
    ConstraintLayout btnShopOwnerInfo;
    @BindView(R.id.btn_send_request)
    Button btnSendRequest;
    @BindView(R.id.btn_shop_info)
    ConstraintLayout btnShopInfo;
    @BindView(R.id.iv_shop_info_show)
    ImageView ivShopInfoShow;
    @BindView(R.id.iv_shop_info_hide)
    ImageView ivShopInfoHide;
    @BindView(R.id.iv_shop_owner_info_show)
    ImageView ivShopOwnerInfoShow;
    @BindView(R.id.iv_shop_owner_info_hide)
    ImageView ivShopOwnerInfoHide;
    @BindView(R.id.tv_waiting_approval)
    TextView tvWaitingApproval;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private ArrayList<Category> listCategories;
    private MySharedPreferences sharedPref;
    private HashMap<Integer, Category> mMapCategory = new HashMap<>();
    private HashMap<Integer, Category> mTempMapCategory = new HashMap<>();

    private String categoryId = "";
    private String cityId = "";

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ConvertShopOwnerActivity.class));
    }

    @Override
    protected void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_convert_shop_owner;
    }

    @Override
    protected void initData() {
        sharedPref = new MySharedPreferences(this);
        if (getIntent() != null) {
            String message = getIntent().getStringExtra("EXTRA_MESSAGE");
            if (message != null) {
                if (GlobalValue.myAccount == null) {
                    Account account = sharedPref.getUserInfo();
                    account.setIsCannotSendRequest("1");
                    sharedPref.saveUserInfo(account);

                    Intent intent = new Intent(this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

        }


        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("REQUEST_SHOP_OWNER_REJECT"));

        if (sharedPref.getUserInfo().getWaitApproveShopOwner() == 0) {
            listCategories = new ArrayList<>();
            listCategories.addAll(ParserUtility
                    .parseListCategories(sharedPref.getCacheCategories()));
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("REJECT_SHOP_OWNER");
            if (message.equals("REJECT_SHOP_OWNER")) {
                tvContent.setText(getString(R.string.reject_convert_to_shop_owner));
                Account account = sharedPref.getUserInfo();
                account.setIsCannotSendRequest("1");
                sharedPref.saveUserInfo(account);
            }
        }
    };

    @Override
    protected void configView() {
        if (sharedPref.getUserInfo().getIsCannotSendRequest().equals("1")) {
            layoutWaitingApproval.setVisibility(View.VISIBLE);
            hideFormConvertToShopOwner();
            tvContent.setText(getString(R.string.reject_convert_to_shop_owner));
        } else {
            if (sharedPref.getUserInfo().getWaitApproveShopOwner() == 1) {
                layoutWaitingApproval.setVisibility(View.VISIBLE);
                hideFormConvertToShopOwner();
            } else {
                tvShopName.setText(Html.fromHtml(getString(R.string.shop_name_2)));
                tvCategory.setText(Html.fromHtml(getString(R.string.category_2)));
                tvCity.setText(Html.fromHtml(getString(R.string.city_2)));
                tvName.setText(Html.fromHtml(getString(R.string.name_2)));
                tvPhone.setText(Html.fromHtml(getString(R.string.phone_2)));
            }
        }

    }

    @OnClick(R.id.btnBack)
    void chooseBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_send_request)
    void convertToShopOwner() {
        checkEmptyEditText();
        if (validateInput()) {
            if (NetworkUtil.checkNetworkAvailable(this)) {
                ModelManager.registerShopOwner2(
                        this, GlobalValue.myAccount.getId(),
                        edtShopName.getText().toString(), edtCategory.getText().toString(),
                        cityId, edtName.getText().toString(),
                        edtPhone.getText().toString(), true, new ModelManagerListener() {
                            @Override
                            public void onError(VolleyError error) {
                                showToast(ErrorNetworkHandler.processError(error));
                            }

                            @Override
                            public void onSuccess(Object object) {
                                if (ParserUtility.isSuccess(object.toString())) {
                                    Account account = sharedPref.getUserInfo();
                                    account.setWaitApproveShopOwner(1);
                                    sharedPref.saveUserInfo(account);
                                    layoutWaitingApproval.setVisibility(View.VISIBLE);
                                    hideFormConvertToShopOwner();
                                } else {
                                    showToast(ParserUtility.getMessage(object.toString()));
                                }
                            }
                        }
                );
            } else {
                showToast(R.string.no_connection);
            }
        }
    }

    @OnClick(R.id.btn_shop_info)
    void chooseShopInfo() {
        if (layoutShopInfo.getVisibility() == View.GONE) {
            layoutShopInfo.setVisibility(View.VISIBLE);
            ivShopInfoShow.setVisibility(View.VISIBLE);
            ivShopInfoHide.setVisibility(View.GONE);
        } else if (layoutShopInfo.getVisibility() == View.VISIBLE) {
            layoutShopInfo.setVisibility(View.GONE);
            ivShopInfoShow.setVisibility(View.GONE);
            ivShopInfoHide.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_shop_owner_info)
    void chooseShopOwnerInfo() {
        if (layoutShopOwnerInfo.getVisibility() == View.GONE) {
            layoutShopOwnerInfo.setVisibility(View.VISIBLE);
            ivShopOwnerInfoShow.setVisibility(View.VISIBLE);
            ivShopOwnerInfoHide.setVisibility(View.GONE);
        } else if (layoutShopOwnerInfo.getVisibility() == View.VISIBLE) {
            layoutShopOwnerInfo.setVisibility(View.GONE);
            ivShopOwnerInfoShow.setVisibility(View.GONE);
            ivShopOwnerInfoHide.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_done)
    void chooseDone() {
        onBackPressed();
    }

    @OnClick(R.id.edt_city)
    void chooseCity() {
        CityIdDialog dialog = CityIdDialog.newInstance();
        dialog.show(getSupportFragmentManager(), dialog.getTag());
        dialog.setOnSearchByCityIdListener((idCity, cityName) -> {
            if (idCity != null && cityName != null) {
                cityId = idCity;
                edtCity.setText(cityName);
            }
        });
    }

    private void showCategoryDialog() {
        if (mTempMapCategory.size() != mMapCategory.size()) {
            mTempMapCategory.clear();
            mTempMapCategory.putAll(mMapCategory);
        }

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_category, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);

        RecyclerView rcvCategory = view.findViewById(R.id.rcv_category);
        rcvCategory.setHasFixedSize(true);
        rcvCategory.setLayoutManager(new GridLayoutManager(this, 2));

        final ShopCategoryAdapter shopCategoryAdapter = new ShopCategoryAdapter(this, listCategories);
        rcvCategory.setAdapter(shopCategoryAdapter);

        shopCategoryAdapter.setOnCheckChangeListener(new ShopCategoryAdapter.OnCheckChangeListener() {
            @Override
            public void onCheckChange(View view, int position, boolean isChecked) {
                if (isChecked) {
                    mTempMapCategory.put(position, listCategories.get(position));
                } else {
                    mTempMapCategory.remove(position);
                }
            }
        });

        Button btnCancel = view.findViewById(R.id.btn_no);
        Button btnOK = view.findViewById(R.id.btn_ok);

        final Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
        dialog.getWindow().setLayout(width, height);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategory();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTempMapCategory.clear();
                dialog.dismiss();
            }
        });
    }

    public void setCategory() {
        edtCategory.setText("");
        StringBuilder category = new StringBuilder();
        categoryId = "";
        if (mMapCategory.size() != mTempMapCategory.size()) {
            mMapCategory.clear();
            mMapCategory.putAll(mTempMapCategory);
            mTempMapCategory.clear();
        }
        if (mMapCategory.size() > 0) {
            for (Integer index : mMapCategory.keySet()) {
                category.append(mMapCategory.get(index).getName()).append(",");
                categoryId += mMapCategory.get(index).getId() + ",";
            }
            if (category.length() > 1 && category.toString().contains(","))
                category = new StringBuilder(category.substring(0, category.length() - 1));
            edtCategory.setText(category.toString());
            if (categoryId.length() > 1 && categoryId.contains(","))
                categoryId = categoryId.substring(0, categoryId.length() - 1);

            for (int i = 0; i < listCategories.size(); i++) {
                if (mMapCategory.containsKey(i)) {
                    listCategories.get(i).setCheck(true);
                } else {
                    listCategories.get(i).setCheck(false);
                }

            }
        } else {
            for (int i = 0; i < listCategories.size(); i++) {
                listCategories.get(i).setCheck(false);
            }
        }
    }

    private boolean validateInput() {
        String shopName = edtShopName.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();
        String city = edtCity.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (shopName.isEmpty()) {
            showToast(R.string.please_enter_shop_name);
            edtShopName.requestFocus();
            return false;
        }

        if (category.isEmpty()) {
            showToast(R.string.please_select_category);
            edtCategory.requestFocus();
            return false;
        }

        if (city.isEmpty()) {
            showToast(R.string.please_enter_city);
            edtCity.requestFocus();
            return false;
        }

        if (name.isEmpty()) {
            showToast(R.string.please_enter_name);
            edtName.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            showToast(R.string.please_enter_phone_number);
            edtPhone.requestFocus();
            return false;
        }
        return true;
    }

    private void hideFormConvertToShopOwner() {
        tvWaitingApproval.setVisibility(View.GONE);
        btnShopInfo.setVisibility(View.GONE);
        layoutShopInfo.setVisibility(View.GONE);
        btnShopOwnerInfo.setVisibility(View.GONE);
        layoutShopOwnerInfo.setVisibility(View.GONE);
        btnSendRequest.setVisibility(View.GONE);
    }

    private void checkEmptyEditText() {
        // shop name
        if (edtShopName.getText().toString().trim().isEmpty()) {
            if (ivEmptyShopName.getVisibility() == View.GONE) {
                ivEmptyShopName.setVisibility(View.VISIBLE);
            }
            edtShopName.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyShopName.getVisibility() == View.VISIBLE) {
                ivEmptyShopName.setVisibility(View.GONE);
            }
            edtShopName.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // category
        if (edtCategory.getText().toString().trim().isEmpty()) {
            if (ivEmptyCategory.getVisibility() == View.GONE) {
                ivEmptyCategory.setVisibility(View.VISIBLE);
            }
            edtCategory.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyCategory.getVisibility() == View.VISIBLE) {
                ivEmptyCategory.setVisibility(View.GONE);
            }
            edtCategory.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // city
        if (edtCity.getText().toString().trim().isEmpty()) {
            if (ivEmptyCity.getVisibility() == View.GONE) {
                ivEmptyCity.setVisibility(View.VISIBLE);
            }
            edtCity.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyCity.getVisibility() == View.VISIBLE) {
                ivEmptyCity.setVisibility(View.GONE);
            }
            edtCity.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // name
        if (edtName.getText().toString().trim().isEmpty()) {
            if (ivEmptyName.getVisibility() == View.GONE) {
                ivEmptyName.setVisibility(View.VISIBLE);
            }
            edtName.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyName.getVisibility() == View.VISIBLE) {
                ivEmptyName.setVisibility(View.GONE);
            }
            edtName.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // phone
        if (edtPhone.getText().toString().trim().isEmpty()) {
            if (ivEmptyPhone.getVisibility() == View.GONE) {
                ivEmptyPhone.setVisibility(View.VISIBLE);
            }
            edtPhone.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyPhone.getVisibility() == View.VISIBLE) {
                ivEmptyPhone.setVisibility(View.GONE);
            }
            edtPhone.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}

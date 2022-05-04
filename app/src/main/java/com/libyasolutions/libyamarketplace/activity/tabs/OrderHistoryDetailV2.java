package com.libyasolutions.libyamarketplace.activity.tabs;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivityV2;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.SplashActivity;
import com.libyasolutions.libyamarketplace.adapter.ProductOrderAdapterV2;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.ShopOrder;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator2;

public class OrderHistoryDetailV2 extends BaseActivityV2 {

    private static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_value_order_number)
    TextView tvValueOrderNumber;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_value_order_date)
    TextView tvValueOrderDate;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_value_order_total)
    TextView tvValueOrderTotal;
    @BindView(R.id.tv_order_total)
    TextView tvOrderTotal;
    @BindView(R.id.tv_value_order_buyer)
    TextView tvValueOrderBuyer;
    @BindView(R.id.tv_order_buyer)
    TextView tvOrderBuyer;
    @BindView(R.id.tv_value_order_status)
    TextView tvValueOrderStatus;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;
    @BindView(R.id.tv_buyer_name)
    TextView tvBuyerName;
    @BindView(R.id.tv_value_buyer_name)
    TextView tvValueBuyerName;
    @BindView(R.id.tv_buyer_phone)
    TextView tvBuyerPhone;
    @BindView(R.id.tv_value_buyer_phone)
    TextView tvValueBuyerPhone;
    @BindView(R.id.iv_buyer_phone)
    ImageView ivBuyerPhone;
    @BindView(R.id.tv_buyer_email)
    TextView tvBuyerEmail;
    @BindView(R.id.tv_value_buyer_email)
    TextView tvValueBuyerEmail;
    @BindView(R.id.tv_shipping_address)
    TextView tvShippingAddress;
    @BindView(R.id.tv_value_shipping_address)
    TextView tvValueShippingAddress;
    @BindView(R.id.tv_shipping_name)
    TextView tvShippingName;
    @BindView(R.id.tv_value_shipping_name)
    TextView tvValueShippingName;
    @BindView(R.id.tv_shipping_phone)
    TextView tvShippingPhone;
    @BindView(R.id.tv_value_shipping_phone)
    TextView tvValueShippingPhone;
    @BindView(R.id.iv_shipping_phone)
    ImageView ivShippingPhone;
    @BindView(R.id.tv_value_order_sub_title)
    TextView tvValueOrderSubTitle;
    @BindView(R.id.tv_order_sub_title)
    TextView tvOrderSubTitle;
    @BindView(R.id.tv_value_order_shipping)
    TextView tvValueOrderShipping;
    @BindView(R.id.tv_order_shipping)
    TextView tvOrderShipping;
    @BindView(R.id.tv_value_order_total_2)
    TextView tvValueOrderTotal2;
    @BindView(R.id.tv_order_total_2)
    TextView tvOrderTotal2;
    @BindView(R.id.tv_value_order_tax)
    TextView tvValueOrderTax;
    @BindView(R.id.tv_order_tax)
    TextView tvOrderTax;
    @BindView(R.id.tv_value_order_grand_total)
    TextView tvValueOrderGrandTotal;
    @BindView(R.id.tv_order_grant_total)
    TextView tvOrderGrantTotal;
    @BindView(R.id.tv_value_order_requirement)
    TextView tvValueOrderRequirement;
    @BindView(R.id.tv_payment_method)
    TextView tvPaymentMethod;
    @BindView(R.id.indicator)
    CircleIndicator2 indicator;
    @BindView(R.id.btn_buyer_info)
    Button btnBuyerInfo;

    private ArrayList<ShopOrder> shopOrdersList = new ArrayList<>();
    private ArrayList<Menu> listProducts = new ArrayList<>();

    private ShopOrder shopOrder;
    private String groupCode;
    private String shipingPhone;
    private String buyerPhone;
    private MySharedPreferences mySharedPreferences;

    public static void startActivity(Context context, String groupCode) {
        context.startActivity(new Intent(context, OrderHistoryDetailV2.class)
                .putExtra(EXTRA_GROUP_ID, groupCode));
    }

    @Override
    protected void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_history_detail_v2;
    }

    @Override
    protected void initData() {
        mySharedPreferences = new MySharedPreferences(this);
        if (getIntent() != null) {
            groupCode = getIntent().getStringExtra(EXTRA_GROUP_ID);
        } else {
            Log.e(TAG, "intent null");
        }
        getOrderHistoryDetail();
    }

    @Override
    protected void configView() {

    }

    @OnClick(R.id.iv_back)
    void chooseBack() {
        onBackPressed();
    }

    private void getOrderHistoryDetail() {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            ModelManager.getListDetailOrder(this, groupCode, true,
                    new ModelManagerListener() {

                        @Override
                        public void onError(VolleyError error) {
                            showToast(ErrorNetworkHandler.processError(error));
                        }

                        @Override
                        public void onSuccess(Object object) {
                            if (ParserUtility.isSuccess(object.toString())) {
                                ArrayList<ShopOrder> list = ParserUtility
                                        .parseListShopOrder(object.toString());
                                if (list.size() > 0) {
                                    shopOrdersList.clear();
                                    shopOrdersList.addAll(list);
                                    showData(shopOrdersList.get(0));
                                } else {
                                    showToast(R.string.no_data);
                                }
                            } else {
                                showToast(ParserUtility.getMessage(object.toString()));
                            }
                        }
                    });
        } else {
            showToast(R.string.no_connection);
        }
    }

    private void showData(ShopOrder shopOrder) {
        if (shopOrder != null) {
            tvOrderBuyer.setText(getString(R.string.shop_name));
            tvValueOrderBuyer.setText(shopOrder.getShopName());

            btnBuyerInfo.setText(getString(R.string.button_text_shop_information));
            tvValueBuyerName.setText(shopOrder.getShopName());
            tvValueBuyerPhone.setText(shopOrder.getShopPhone());

            tvBuyerEmail.setText(getString(R.string.shop_address));
            tvValueBuyerEmail.setText(shopOrder.getShopAddress());

            shipingPhone = shopOrder.getShippingPhone();
            buyerPhone = shopOrder.getBuyerPhone();
            // order
            tvValueOrderNumber.setText("#" + shopOrder.getOrderId());
            tvValueOrderDate.setText(shopOrder.getOrderTime());
            tvValueOrderTotal.setText(shopOrder.getTotalPrice() + " " + getResources().getString(R.string.currency));
            setOrderStatus(shopOrder.getOrderStatus() + "", tvValueOrderStatus);

            // product info
            rvOrder.setHasFixedSize(true);
            rvOrder.setNestedScrollingEnabled(false);
            rvOrder.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false));

            listProducts.clear();
            listProducts.addAll(shopOrder.getArrFoods());
            ProductOrderAdapterV2 productOrderAdapter =
                    new ProductOrderAdapterV2(this, listProducts);
            rvOrder.setAdapter(productOrderAdapter);

            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
            pagerSnapHelper.attachToRecyclerView(rvOrder);
            indicator.attachToRecyclerView(rvOrder, pagerSnapHelper);

            productOrderAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

            // order requirement
            tvValueOrderRequirement.setText(shopOrder.getOrderRequirement());

            // payment method
            if (shopOrder.getPaymentMethod() == 1) {
                tvPaymentMethod.setText(getString(R.string.delivery_method));
            } else if (shopOrder.getPaymentMethod() == 2) {
                tvPaymentMethod.setText(getString(R.string.paypal_method));
            } else if (shopOrder.getPaymentMethod() == 3) {
                tvPaymentMethod.setText(getString(R.string.banking_method));
            } else if (shopOrder.getPaymentMethod() == 6) {
                tvPaymentMethod.setText(getString(R.string.pay_at_collection));
            }

            //shop info
            tvValueShippingAddress.setText(shopOrder.getShippingAddress());
            tvValueShippingName.setText(shopOrder.getShippingName());
            tvValueShippingPhone.setText(shopOrder.getShippingPhone());

            tvValueOrderSubTitle.setText(shopOrder.getTotalPrice() + " " + getResources().getString(R.string.currency));
            tvValueOrderShipping.setText(shopOrder.getShipping() + " " + getResources().getString(R.string.currency));
            tvValueOrderTotal2.setText(shopOrder.getTotalPrice() + " " + getResources().getString(R.string.currency));
            tvValueOrderTax.setText(shopOrder.getVAT() + " " + getResources().getString(R.string.currency));
            tvValueOrderGrandTotal.setText(shopOrder.getGrandTotal() + " " + getResources().getString(R.string.currency));
        }
    }

    private void setOrderStatus(String orderStatus, TextView tvValueOrderStatus) {
        switch (orderStatus) {
            case Constant.ORDER_NEW:
                tvValueOrderStatus.setText(getResources().getString(R.string.order_waiting));
                break;
            case Constant.ORDER_IN_PROCESS:
                tvValueOrderStatus.setText(getResources().getString(R.string.inprocess));
                break;
            case Constant.ORDER_READY:
                tvValueOrderStatus.setText(getResources().getString(R.string.ready));
                break;
            case Constant.ORDER_ON_THE_WAY:
                tvValueOrderStatus.setText(getResources().getString(R.string.on_the_way));
                break;
            case Constant.ORDER_DELIVERED:
                tvValueOrderStatus.setText(getResources().getString(R.string.delivered));
                break;
            case Constant.ORDER_CANCEL:
                tvValueOrderStatus.setText(getResources().getString(R.string.cancel));
                break;
            case Constant.ORDER_FAIL_DELIVERY:
                tvValueOrderStatus.setText(getResources().getString(R.string.fail_delivery));
                break;
            case Constant.ORDER_ACCEPT:
                tvValueOrderStatus.setText(getString(R.string.order_accept));
                break;
            case Constant.ORDER_REJECT_INFO_NOT_ENOUGH :
                tvValueOrderStatus.setText(getString(R.string.reject_information_not_enough));
                break;
            case Constant.ORDER_REJECT_QUANTITY_NOT_AVAILABLE  :
                tvValueOrderStatus.setText(getString(R.string.reject_quantity_not_available));
                break;
        }
    }

    @OnClick(R.id.iv_shipping_phone)
    void callShipping() {
        if (shipingPhone != null && !shipingPhone.isEmpty()) {
            Uri link = Uri.parse("tel: " + shipingPhone);
            startActivity(new Intent(Intent.ACTION_DIAL, link));
        } else {
            showToast(R.string.phone_number_empty);
        }
    }

    @OnClick(R.id.iv_buyer_phone)
    void callBuyer() {
        if (buyerPhone != null && !buyerPhone.isEmpty()) {
            Uri link = Uri.parse("tel: " + buyerPhone);
            startActivity(new Intent(Intent.ACTION_DIAL, link));
        } else {
            showToast(R.string.phone_number_empty);
        }
    }

}

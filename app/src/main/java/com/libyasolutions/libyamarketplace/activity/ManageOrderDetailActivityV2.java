package com.libyasolutions.libyamarketplace.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivityV2;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.OrderStatusAdapter;
import com.libyasolutions.libyamarketplace.adapter.ProductOrderAdapterV2;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.ShopOrder;
import com.libyasolutions.libyamarketplace.object.StatusObj;
import com.libyasolutions.libyamarketplace.util.Logger;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator2;

/**
 * Create by Nguyen Dinh Doan
 */
public class ManageOrderDetailActivityV2 extends BaseActivityV2 {

    private static final String TAG = "ManageOrderDetailActivity";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
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
    @BindView(R.id.btn_update_status_order)
    Button btnUpdateStatusOrder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_value_order_requirement)
    TextView tvValueOrderRequirement;
    @BindView(R.id.tv_payment_method)
    TextView tvPaymentMethod;
    @BindView(R.id.indicator)
    CircleIndicator2 indicator;

    private ShopOrder shopOrder;
    private String orderStatus = "";

    private ArrayList<Menu> listProducts = new ArrayList<>();
    private ArrayList<StatusObj> listStatus = new ArrayList<>();
    private MySharedPreferences mySharedPreferences;

    @Override
    protected void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_order_detail_v2;
    }

    @Override
    protected void initData() {
        mySharedPreferences = new MySharedPreferences(this);
        shopOrder = GlobalValue.currentShopOrder;
        getListStatus();
    }

    @Override
    protected void configView() {
        if (shopOrder != null) {
            // order
            tvValueOrderNumber.setText("#" + shopOrder.getOrderId());
            tvValueOrderDate.setText(shopOrder.getOrderTime());
            tvValueOrderTotal.setText(shopOrder.getTotalPrice() + " " + getResources().getString(R.string.currency));
            tvValueOrderBuyer.setText(shopOrder.getBuyerName());
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

            // buyer
            tvValueBuyerName.setText(shopOrder.getBuyerName());
            tvValueBuyerPhone.setText(shopOrder.getBuyerPhone());
            tvValueBuyerEmail.setText(shopOrder.getBuyerEmail());

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

    private void getListStatus() {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            ModelManager.getOrderStatus(this, mySharedPreferences.getUserInfo().getId() ,
                    String.valueOf(shopOrder.getShopId()), shopOrder.getOrderId(), true, new ModelManagerListener() {
                        @Override
                        public void onError(VolleyError error) {
                            showToast(ErrorNetworkHandler.processError(error));
                        }

                        @Override
                        public void onSuccess(Object object) {
                            String json = (String) object;
                            if (ParserUtility.isSuccess(json)) {
                                listStatus.clear();
                                listStatus.addAll(ParserUtility.getOrderStatus(json));
                            } else {
                                showToast(ParserUtility.getMessage(json));
                            }
                        }
                    });
        } else {
            showToast(R.string.no_connection);
        }

    }

    private void updateOrderStatus(final String status) {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            ModelManager.updateOrderStatusByShop(this, mySharedPreferences.getUserInfo().getId(),
                    shopOrder.getShopId() + "", shopOrder.getOrderId(), status, true, new ModelManagerListener() {
                @Override
                public void onError(VolleyError error) {
                    showToast(ErrorNetworkHandler.processError(error));
                }

                @Override
                public void onSuccess(Object object) {
                    String json = (String) object;
                    if (ParserUtility.isSuccess(json)) {
                        showToast(ParserUtility.getMessage(json));
                        setOrderStatus(status, tvValueOrderStatus);
                        orderStatus = status;

                        Intent intent = new Intent();
                        intent.setAction(Constant.REFRESH_DATA);
                        sendBroadcast(intent);
                    } else {
                        showToast(ParserUtility.getMessage(json));
                    }
                }
            });
        } else {
            showToast(R.string.no_connection);
        }

    }

    private void showChangeOrderStatusDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_change_order_status, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        final OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(this, listStatus);
        ListView lvStatus = view.findViewById(R.id.lv_status);
        lvStatus.setAdapter(orderStatusAdapter);

        if (listStatus.size() > 0) {
            for (int i = 0; i < listStatus.size(); i++) {
                Logger.e(TAG, "order status: " + orderStatus);
                if (orderStatus.equals(listStatus.get(i).getValue() + "")) {
                    orderStatusAdapter.setCurrentPosition(i);
                    orderStatusAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }

        lvStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderStatus = listStatus.get(position).getValue() + "";
                orderStatusAdapter.setCurrentPosition(position);
                orderStatusAdapter.notifyDataSetChanged();
            }
        });

        Button btnOK = view.findViewById(R.id.btn_ok);
        Button btnCanel = view.findViewById(R.id.btn_no);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderStatus == null || orderStatus.isEmpty()) {
                    showToast(R.string.order_status_not_selected);
                } else {
                    updateOrderStatus(orderStatus);
                }
                dialog.dismiss();
            }
        });

        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
                tvValueOrderStatus.setText(getResources().getString(R.string.order_accept));
                break;
            case Constant.ORDER_REJECT_INFO_NOT_ENOUGH:
                tvValueOrderStatus.setText(getResources().getString(R.string.reject_information_not_enough));
                break;
            case Constant.ORDER_REJECT_QUANTITY_NOT_AVAILABLE:
                tvValueOrderStatus.setText(getResources().getString(R.string.reject_quantity_not_available));
                break;
        }
    }

    @OnClick(R.id.iv_back)
    void chooseBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_update_status_order)
    void updateStatusOrder() {
        if (listStatus.size() > 0) {
            showChangeOrderStatusDialog();
        } else {
            showToast(R.string.order_message);
        }
    }

    @OnClick(R.id.iv_shipping_phone)
    void callShipping() {
        String phoneNumber = shopOrder.getShippingPhone();
        if (!phoneNumber.isEmpty()) {
            Uri link = Uri.parse("tel: " + phoneNumber);
            startActivity(new Intent(Intent.ACTION_DIAL, link));
        } else {
            showToast(R.string.phone_number_empty);
        }
    }

    @OnClick(R.id.iv_buyer_phone)
    void callBuyer() {
        String phoneNumber = shopOrder.getBuyerPhone();
        if (!phoneNumber.isEmpty()) {
            Uri link = Uri.parse("tel: " + phoneNumber);
            startActivity(new Intent(Intent.ACTION_DIAL, link));
        } else {
            showToast(R.string.phone_number_empty);
        }
    }

}

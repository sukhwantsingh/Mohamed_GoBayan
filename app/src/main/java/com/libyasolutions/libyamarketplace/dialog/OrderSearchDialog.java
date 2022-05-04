package com.libyasolutions.libyamarketplace.dialog;

import android.os.Bundle;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.BaseDialog;
import com.libyasolutions.libyamarketplace.R;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OrderSearchDialog extends BaseDialog {

    private static final String TAG = "OrderSearchDialog";
    private static final String EXTRA_NUMBER = "number";
    private static final String EXTRA_DATE = "date";
    private static final String EXTRA_BUYER = "buyer";
    private static final String EXTRA_STATUS = "status";
    private static final String EXTRA_ORDER_SEARCH = "EXTRA_ORDER_SEARCH";

    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_order_buyer)
    TextView tvOrderBuyer;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;

    private String orderSearch = "";

    private OnOrderSearchListener listener;

    public static OrderSearchDialog newInstance(String orderSearch) {
        Bundle args = new Bundle();
        OrderSearchDialog fragment = new OrderSearchDialog();
        args.putString(EXTRA_ORDER_SEARCH, orderSearch);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        try {
            orderSearch = getArguments().getString(EXTRA_ORDER_SEARCH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.dialog_order_search;
    }

    @Override
    protected void configView() {
        switch (orderSearch) {
            case EXTRA_NUMBER: {
                setupOrderNumberSelected();
                setupOrderDateUnSelected();
                setupOrderBuyerUnSelected();
                setupOrderStatusUnSelected();
                break;
            }
            case EXTRA_DATE: {
                setupOrderDateSelected();
                setupOrderNumberUnSelected();
                setupOrderBuyerUnSelected();
                setupOrderStatusUnSelected();
                break;
            }
            case EXTRA_BUYER: {
                setupOrderBuyerSelected();
                setupOrderNumberUnSelected();
                setupOrderStatusUnSelected();
                setupOrderDateUnSelected();
                break;
            }
            case EXTRA_STATUS: {
                setupOrderStatusSelected();
                setupOrderNumberUnSelected();
                setupOrderDateUnSelected();
                setupOrderBuyerUnSelected();
                break;
            }
        }
    }

    @Override
    protected int getStyleDialog() {
        return 0;
    }

    @Override
    protected double initWidthDialog() {
        return 0.85;
    }

    @Override
    protected double initHeightDialog() {
        return 0;
    }

    @Override
    protected boolean isCancelOnTouchOutside() {
        return true;
    }

    @OnClick(R.id.tv_order_number)
    void chooseOrderNumber() {
        if (listener != null) {
            listener.onOrderSearch(EXTRA_NUMBER);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_date)
    void chooseOrderDate() {
        if (listener != null) {
            listener.onOrderSearch(EXTRA_DATE);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_buyer)
    void chooseOrderBuyer() {
        if (listener != null) {
            listener.onOrderSearch(EXTRA_BUYER);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_status)
    void chooseOrderStatus() {
        if (listener != null) {
            listener.onOrderSearch(EXTRA_STATUS);
        }
        dismiss();
    }

    private void setupOrderNumberSelected() {
        tvOrderNumber.setBackgroundResource(R.color.color_red);
        tvOrderNumber.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderNumberUnSelected() {
        tvOrderNumber.setBackgroundResource(R.color.white);
        tvOrderNumber.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderDateSelected() {
        tvOrderDate.setBackgroundResource(R.color.color_red);
        tvOrderDate.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderDateUnSelected() {
        tvOrderDate.setBackgroundResource(R.color.white);
        tvOrderDate.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderBuyerSelected() {
        tvOrderBuyer.setBackgroundResource(R.color.color_red);
        tvOrderBuyer.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderBuyerUnSelected() {
        tvOrderBuyer.setBackgroundResource(R.color.white);
        tvOrderBuyer.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderStatusSelected() {
        tvOrderStatus.setBackgroundResource(R.color.color_red);
        tvOrderStatus.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderStatusUnSelected() {
        tvOrderStatus.setBackgroundResource(R.color.white);
        tvOrderStatus.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    public void setOnOrderSearchListener(OnOrderSearchListener listener) {
        this.listener = listener;
    }

    public interface OnOrderSearchListener {
        void onOrderSearch(String orderSearch);
    }
}

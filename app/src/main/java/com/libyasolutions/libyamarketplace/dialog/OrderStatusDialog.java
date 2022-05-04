package com.libyasolutions.libyamarketplace.dialog;

import android.os.Bundle;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.BaseDialog;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.config.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderStatusDialog extends BaseDialog {

    private static final String EXTRA_ORDER_STATUS = "EXTRA_ORDER_STATUS";

    @BindView(R.id.tv_order_reject_info)
    TextView tvOrderRejectInfo;
    @BindView(R.id.tv_order_reject_available)
    TextView tvOrderRejectAvailable;
    @BindView(R.id.tv_order_accept)
    TextView tvOrderAccept;
    @BindView(R.id.tv_order_in_progress)
    TextView tvOrderInProgress;
    @BindView(R.id.tv_order_ready_collect)
    TextView tvOrderReadyCollect;
    @BindView(R.id.tv_order_on_the_way)
    TextView tvOrderOnTheWay;
    @BindView(R.id.tv_order_failed_delivery)
    TextView tvOrderFailedDelivery;
    @BindView(R.id.tv_order_delivered)
    TextView tvOrderDelivered;
    @BindView(R.id.tv_order_new)
    TextView tvOrderNew;
    @BindView(R.id.tv_order_cancel)
    TextView tvOrderCancel;
    @BindView(R.id.tv_order_all)
    TextView tvOrderAll;

    private OnOrderStatusListener listener;
    private String orderStatus = "0"; // new order

    public static OrderStatusDialog newInstance(String orderStatus) {
        Bundle args = new Bundle();
        OrderStatusDialog fragment = new OrderStatusDialog();
        args.putString(EXTRA_ORDER_STATUS, orderStatus);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnOrderStatusListener(OnOrderStatusListener listener) {
        this.listener = listener;
    }

    @Override
    protected void initData() {
        try {
            orderStatus = getArguments().getString(EXTRA_ORDER_STATUS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.dialog_order_status;
    }

    @Override
    protected void configView() {
        switch (orderStatus) {
            case Constant.ORDER_ALL: {
                setupOrderAllSelected();

                setupOrderNewUnSelected();
                setupOrderInProcessUnSelected();
                setupOrderReadyUnSelected();
                setupOrderDeliveriedUnSelected();
                setupOrderOnTheWayUnSelected();
                setupOrderCancelUnSelected();
                setupOrderFailedDeliveryUnSelected();
                setupOrderAcceptUnSelected();
                setupOrderRejectInfoNotEnoughUnSelected();
                setupOrderRejectQuantityNotAvailableUnSelected();
                break;
            }
            case Constant.ORDER_NEW:
                setupOrderNewSelected();

                setupOrderAllUnSelected();
                setupOrderInProcessUnSelected();
                setupOrderReadyUnSelected();
                setupOrderDeliveriedUnSelected();
                setupOrderOnTheWayUnSelected();
                setupOrderCancelUnSelected();
                setupOrderFailedDeliveryUnSelected();
                setupOrderAcceptUnSelected();
                setupOrderRejectInfoNotEnoughUnSelected();
                setupOrderRejectQuantityNotAvailableUnSelected();
                break;
            case Constant.ORDER_IN_PROCESS:
                setupOrderInProcessSelected();

                setupOrderAllUnSelected();
                setupOrderNewUnSelected();
                setupOrderReadyUnSelected();
                setupOrderDeliveriedUnSelected();
                setupOrderOnTheWayUnSelected();
                setupOrderCancelUnSelected();
                setupOrderFailedDeliveryUnSelected();
                setupOrderAcceptUnSelected();
                setupOrderRejectInfoNotEnoughUnSelected();
                setupOrderRejectQuantityNotAvailableUnSelected();
                break;
            case Constant.ORDER_READY:
                setupOrderReadySelected();

                setupOrderAllUnSelected();
                setupOrderNewUnSelected();
                setupOrderInProcessUnSelected();
                setupOrderDeliveriedUnSelected();
                setupOrderOnTheWayUnSelected();
                setupOrderCancelUnSelected();
                setupOrderFailedDeliveryUnSelected();
                setupOrderAcceptUnSelected();
                setupOrderRejectInfoNotEnoughUnSelected();
                setupOrderRejectQuantityNotAvailableUnSelected();
                break;
            case Constant.ORDER_ON_THE_WAY:
                setupOrderOnTheWaySelected();

                setupOrderAllUnSelected();
                setupOrderNewUnSelected();
                setupOrderInProcessUnSelected();
                setupOrderReadyUnSelected();
                setupOrderDeliveriedUnSelected();
                setupOrderCancelUnSelected();
                setupOrderFailedDeliveryUnSelected();
                setupOrderAcceptUnSelected();
                setupOrderRejectInfoNotEnoughUnSelected();
                setupOrderRejectQuantityNotAvailableUnSelected();
                break;
            case Constant.ORDER_DELIVERED:
                setupOrderDeliveriedSelected();

                setupOrderAllUnSelected();
                setupOrderNewUnSelected();
                setupOrderInProcessUnSelected();
                setupOrderReadyUnSelected();
                setupOrderOnTheWayUnSelected();
                setupOrderCancelUnSelected();
                setupOrderFailedDeliveryUnSelected();
                setupOrderAcceptUnSelected();
                setupOrderRejectInfoNotEnoughUnSelected();
                setupOrderRejectQuantityNotAvailableUnSelected();
                break;
            case Constant.ORDER_CANCEL:
                setupOrderCancelSelected();

                setupOrderAllUnSelected();
                setupOrderNewUnSelected();
                setupOrderInProcessUnSelected();
                setupOrderReadyUnSelected();
                setupOrderOnTheWayUnSelected();
                setupOrderDeliveriedUnSelected();
                setupOrderFailedDeliveryUnSelected();
                setupOrderAcceptUnSelected();
                setupOrderRejectInfoNotEnoughUnSelected();
                setupOrderRejectQuantityNotAvailableUnSelected();
                break;
            case Constant.ORDER_FAIL_DELIVERY:
                setupOrderFailedDeliverySelected();

                setupOrderAllUnSelected();
                setupOrderNewUnSelected();
                setupOrderInProcessUnSelected();
                setupOrderReadyUnSelected();
                setupOrderOnTheWayUnSelected();
                setupOrderDeliveriedUnSelected();
                setupOrderCancelUnSelected();
                setupOrderAcceptUnSelected();
                setupOrderRejectInfoNotEnoughUnSelected();
                setupOrderRejectQuantityNotAvailableUnSelected();
                break;
            case Constant.ORDER_ACCEPT:
                setupOrderAcceptSelected();

                setupOrderAllUnSelected();
                setupOrderNewUnSelected();
                setupOrderInProcessUnSelected();
                setupOrderReadyUnSelected();
                setupOrderOnTheWayUnSelected();
                setupOrderDeliveriedUnSelected();
                setupOrderCancelUnSelected();
                setupOrderFailedDeliveryUnSelected();
                setupOrderRejectInfoNotEnoughUnSelected();
                setupOrderRejectQuantityNotAvailableUnSelected();
                break;
            case Constant.ORDER_REJECT_INFO_NOT_ENOUGH:
                setupOrderRejectInfoNotEnoughSelected();

                setupOrderAllUnSelected();
                setupOrderNewUnSelected();
                setupOrderInProcessUnSelected();
                setupOrderReadyUnSelected();
                setupOrderOnTheWayUnSelected();
                setupOrderDeliveriedUnSelected();
                setupOrderCancelUnSelected();
                setupOrderFailedDeliveryUnSelected();
                setupOrderAcceptUnSelected();
                setupOrderRejectQuantityNotAvailableUnSelected();
                break;
            case Constant.ORDER_REJECT_QUANTITY_NOT_AVAILABLE:
                setupOrderRejectQuantityNotAvailableSelected();

                setupOrderAllUnSelected();
                setupOrderNewUnSelected();
                setupOrderInProcessUnSelected();
                setupOrderReadyUnSelected();
                setupOrderOnTheWayUnSelected();
                setupOrderDeliveriedUnSelected();
                setupOrderCancelUnSelected();
                setupOrderFailedDeliveryUnSelected();
                setupOrderAcceptUnSelected();
                setupOrderRejectInfoNotEnoughUnSelected();
                break;
        }
    }

    @OnClick(R.id.tv_order_new)
    void chooseOrderNew() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_NEW);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_in_progress)
    void chooseOrderInProcess() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_IN_PROCESS);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_ready_collect)
    void chooseOrderReadyCollect() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_READY);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_on_the_way)
    void chooseOrderOnTheWay() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_ON_THE_WAY);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_delivered)
    void chooseOrderDelivered() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_DELIVERED);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_cancel)
    void chooseOrderCancel() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_CANCEL);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_failed_delivery)
    void chooseOrderFailedDelivery() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_FAIL_DELIVERY);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_accept)
    void chooseOrderAccept() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_ACCEPT);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_reject_info)
    void chooseOrderRejectInfo() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_REJECT_INFO_NOT_ENOUGH);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_reject_available)
    void chooseOrderRejectAvailable() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_REJECT_QUANTITY_NOT_AVAILABLE);
        }
        dismiss();
    }

    @OnClick(R.id.tv_order_all)
    void chooseOrderAll() {
        if (listener != null) {
            listener.onOrderStatus(Constant.ORDER_ALL);
        }
        dismiss();
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

    private void setupOrderNewSelected() {
        tvOrderNew.setBackgroundResource(R.color.color_red);
        tvOrderNew.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderNewUnSelected() {
        tvOrderNew.setBackgroundResource(R.color.white);
        tvOrderNew.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderInProcessSelected() {
        tvOrderInProgress.setBackgroundResource(R.color.color_red);
        tvOrderInProgress.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderInProcessUnSelected() {
        tvOrderInProgress.setBackgroundResource(R.color.white);
        tvOrderInProgress.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderReadySelected() {
        tvOrderReadyCollect.setBackgroundResource(R.color.color_red);
        tvOrderReadyCollect.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderReadyUnSelected() {
        tvOrderReadyCollect.setBackgroundResource(R.color.white);
        tvOrderReadyCollect.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderOnTheWaySelected() {
        tvOrderOnTheWay.setBackgroundResource(R.color.color_red);
        tvOrderOnTheWay.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderOnTheWayUnSelected() {
        tvOrderOnTheWay.setBackgroundResource(R.color.white);
        tvOrderOnTheWay.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderDeliveriedSelected() {
        tvOrderDelivered.setBackgroundResource(R.color.color_red);
        tvOrderDelivered.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderDeliveriedUnSelected() {
        tvOrderDelivered.setBackgroundResource(R.color.white);
        tvOrderDelivered.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderCancelSelected() {
        tvOrderCancel.setBackgroundResource(R.color.color_red);
        tvOrderCancel.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderCancelUnSelected() {
        tvOrderCancel.setBackgroundResource(R.color.white);
        tvOrderCancel.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderFailedDeliverySelected() {
        tvOrderFailedDelivery.setBackgroundResource(R.color.color_red);
        tvOrderFailedDelivery.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderFailedDeliveryUnSelected() {
        tvOrderFailedDelivery.setBackgroundResource(R.color.white);
        tvOrderFailedDelivery.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderAcceptSelected() {
        tvOrderAccept.setBackgroundResource(R.color.color_red);
        tvOrderAccept.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderAcceptUnSelected() {
        tvOrderAccept.setBackgroundResource(R.color.white);
        tvOrderAccept.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderRejectInfoNotEnoughSelected() {
        tvOrderRejectInfo.setBackgroundResource(R.color.color_red);
        tvOrderRejectInfo.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderRejectInfoNotEnoughUnSelected() {
        tvOrderRejectInfo.setBackgroundResource(R.color.white);
        tvOrderRejectInfo.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderRejectQuantityNotAvailableSelected() {
        tvOrderRejectAvailable.setBackgroundResource(R.color.color_red);
        tvOrderRejectAvailable.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderRejectQuantityNotAvailableUnSelected() {
        tvOrderRejectAvailable.setBackgroundResource(R.color.white);
        tvOrderRejectAvailable.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    private void setupOrderAllSelected() {
        tvOrderAll.setBackgroundResource(R.color.color_red);
        tvOrderAll.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    private void setupOrderAllUnSelected() {
        tvOrderAll.setBackgroundResource(R.color.white);
        tvOrderAll.setTextColor(getActivity().getResources().getColor(R.color.black));
    }

    public interface OnOrderStatusListener {
        void onOrderStatus(String orderStatus);
    }

}

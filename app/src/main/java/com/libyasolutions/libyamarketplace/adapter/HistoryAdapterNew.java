package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.OrderHistoryDetailV2;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.OrderGroup;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import java.util.List;

public class HistoryAdapterNew extends RecyclerView.Adapter<HistoryAdapterNew.ViewHolder> {

    private List<OrderGroup> orderGroupArrayList;
    private Context context;
    private MySharedPreferences mySharedPreferences;

    public HistoryAdapterNew(List<OrderGroup> orderGroupArrayList, Context context) {
        this.orderGroupArrayList = orderGroupArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderGroup orderGroup = orderGroupArrayList.get(position);

        if (orderGroup != null) {
            holder.tvTime.setText(orderGroup.getDatetime());
            holder.tvAmount.setText(orderGroup.getPrice() + " " + context.getResources().getString(R.string.currency));
            setOrderStatus(orderGroup.getStatus(), holder.tvStatus);
            holder.tvSTT.setText("#" + orderGroup.getId());

            setOrderIsRead(String.valueOf(orderGroup.getStatus()), orderGroup.getIsRead(), holder.layoutOrderHistory);

            mySharedPreferences = new MySharedPreferences(context);
            holder.layoutOrderHistory.setOnClickListener(view -> {
                updateIsReadOrder(orderGroup.getGroudCode(), orderGroup.getId());
            });
        }

    }

    private void updateIsReadOrder(String orderGroupCode, String orderId) {
        if (NetworkUtil.checkNetworkAvailable(context)) {
            ModelManager.updateIsReadOrder(context, mySharedPreferences.getUserInfo().getId(),
                    orderId, true, new ModelManagerListener() {
                        @Override
                        public void onError(VolleyError error) {
                            Toast.makeText(context, ErrorNetworkHandler.processError(error), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(Object object) {
                            if (ParserUtility.isSuccess(object.toString())) {
                                OrderHistoryDetailV2.startActivity(context, orderGroupCode);
                            } else {
                                Toast.makeText(context, ParserUtility.getMessage(object.toString()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void setOrderIsRead(String orderStatus, String isRead, ConstraintLayout layoutOrder) {
        if (isRead.equals("1")) {
            if (!orderStatus.equals("New")) {
                layoutOrder.setBackgroundResource(R.color.white);
            } else {
                layoutOrder.setBackgroundResource(R.color.color_new_order);
            }
        } else {
            layoutOrder.setBackgroundResource(R.color.color_new_order);
        }
    }

    private void setOrderStatus(String orderStatus, TextView tvStatus) {
        switch (orderStatus) {
            case Constant.ORDER_NEW:
                tvStatus.setText(context.getResources().getString(R.string.order_waiting));
                break;
            case Constant.ORDER_IN_PROCESS:
                tvStatus.setText(context.getResources().getString(R.string.inprocess));
                break;
            case Constant.ORDER_READY:
                tvStatus.setText(context.getResources().getString(R.string.ready));
                break;
            case Constant.ORDER_ON_THE_WAY:
                tvStatus.setText(context.getResources().getString(R.string.on_the_way));
                break;
            case Constant.ORDER_DELIVERED:
                tvStatus.setText(context.getResources().getString(R.string.delivered));
                break;
            case Constant.ORDER_CANCEL:
                tvStatus.setText(context.getResources().getString(R.string.cancel));
                break;
            case Constant.ORDER_FAIL_DELIVERY:
                tvStatus.setText(context.getResources().getString(R.string.fail_delivery));
                break;
            case Constant.ORDER_ACCEPT:
                tvStatus.setText(context.getString(R.string.order_accept));
                break;
            case Constant.ORDER_REJECT_INFO_NOT_ENOUGH:
                tvStatus.setText(context.getString(R.string.reject_information_not_enough));
                break;
            case Constant.ORDER_REJECT_QUANTITY_NOT_AVAILABLE:
                tvStatus.setText(context.getString(R.string.reject_quantity_not_available));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return orderGroupArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSTT;
        private TextView tvTime;
        private TextView tvAmount;
        private TextView tvStatus;
        private ConstraintLayout layoutOrderHistory;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSTT = itemView.findViewById(R.id.tvSTT);
            tvAmount = itemView.findViewById(R.id.tvPriceTotal);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            layoutOrderHistory = itemView.findViewById(R.id.layoutOrderHistory);
        }
    }

}

package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.interfaces.AdapterListener;
import com.libyasolutions.libyamarketplace.object.ShopOrder;

import java.util.ArrayList;

public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.ViewHolder> {
    private ArrayList<ShopOrder> shopOrders;
    private Context context;

    private AdapterListener.onItemClickListener onItemClickListener;

    public ManageOrderAdapter(ArrayList<ShopOrder> shopOrders, Context context) {
        this.shopOrders = shopOrders;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_manage_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopOrder shopOrder = shopOrders.get(position);
        holder.tvOrderNumber.setText("#" + shopOrder.getOrderId());
        holder.tvBuyer.setText(shopOrder.getBuyerName());
        holder.tvRequestDate.setText(shopOrder.getOrderTime());
        holder.tvTotal.setText(shopOrder.getTotalPrice() + " " + context.getResources().getString(R.string.currency));

        setOrderStatus(String.valueOf(shopOrder.getOrderStatus()), holder.tvStatus, holder.layoutOrder);
    }

    private void setOrderStatus(String orderStatus, TextView tvStatus, ConstraintLayout layoutOrder) {
        switch (orderStatus) {
            case Constant.ORDER_NEW:
                tvStatus.setText(context.getResources().getString(R.string.order_waiting));
                layoutOrder.setBackgroundResource(R.color.color_new_order);
                break;
            case Constant.ORDER_IN_PROCESS:
                tvStatus.setText(context.getResources().getString(R.string.inprocess));
                layoutOrder.setBackgroundResource(R.color.white);
                break;
            case Constant.ORDER_READY:
                tvStatus.setText(context.getResources().getString(R.string.ready));
                layoutOrder.setBackgroundResource(R.color.white);
                break;
            case Constant.ORDER_ON_THE_WAY:
                tvStatus.setText(context.getResources().getString(R.string.on_the_way));
                layoutOrder.setBackgroundResource(R.color.white);
                break;
            case Constant.ORDER_DELIVERED:
                tvStatus.setText(context.getResources().getString(R.string.delivered));
                layoutOrder.setBackgroundResource(R.color.white);
                break;
            case Constant.ORDER_CANCEL:
                tvStatus.setText(context.getResources().getString(R.string.cancel));
                layoutOrder.setBackgroundResource(R.color.white);
                break;
            case Constant.ORDER_FAIL_DELIVERY:
                tvStatus.setText(context.getResources().getString(R.string.fail_delivery));
                layoutOrder.setBackgroundResource(R.color.white);
                break;
            case Constant.ORDER_ACCEPT:
                tvStatus.setText(context.getString(R.string.order_accept));
                layoutOrder.setBackgroundResource(R.color.white);
                break;
            case Constant.ORDER_REJECT_INFO_NOT_ENOUGH :
                tvStatus.setText(context.getString(R.string.reject_information_not_enough));
                layoutOrder.setBackgroundResource(R.color.white);
                break;
            case Constant.ORDER_REJECT_QUANTITY_NOT_AVAILABLE  :
                tvStatus.setText(context.getString(R.string.reject_quantity_not_available));
                layoutOrder.setBackgroundResource(R.color.white);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return shopOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderNumber;
        private TextView tvBuyer;
        private TextView tvTotal;
        private TextView tvStatus;
        private TextView tvRequestDate;
        private ConstraintLayout layoutOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tv_order_number);
            tvBuyer = itemView.findViewById(R.id.tv_buyer);
            tvTotal = itemView.findViewById(R.id.tv_total_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvRequestDate = itemView.findViewById(R.id.tv_request_date);
            layoutOrder = itemView.findViewById(R.id.layout_order);

            itemView.setOnClickListener(v -> onItemClickListener.onclick(v, getAdapterPosition()));
        }
    }

    public void setOnItemClickListener(AdapterListener.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

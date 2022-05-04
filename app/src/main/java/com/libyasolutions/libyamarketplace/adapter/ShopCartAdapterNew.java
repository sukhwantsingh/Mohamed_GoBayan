package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.cart.ShopCartDetailActivity;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.StringUtility;

import java.util.ArrayList;

public class ShopCartAdapterNew extends RecyclerView.Adapter<ShopCartAdapterNew.ViewHolder> {
    private ArrayList<Shop> lsvShop;
    private Context context;
    private ShopCartListener shopCartListener;

    public ShopCartAdapterNew(ArrayList<Shop> lsvShop, Context context, ShopCartListener shopCartListener) {
        this.lsvShop = lsvShop;
        this.context = context;
        this.shopCartListener = shopCartListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_shop_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(lsvShop.get(position).getImage()).into(holder.imgShop);
        holder.lblShopName.setText(lsvShop.get(position).getShopName());
        holder.lblFoodNumber.setText(StringUtility.replaceArabicNumbers(String.valueOf(lsvShop.get(position).getNumberItems())) + " " + context.getString(R.string.items));
        if ((lsvShop.get(position).getTotalPrice() + lsvShop.get(position).getCurrentTotalVAT()) >= lsvShop.get(position).getMinPriceForDelivery()) {
            holder.lblShipping.setText(context.getString(R.string.ship) + context.getResources().getString(R.string.currency) + StringUtility.replaceArabicNumbers(String.valueOf(0)));
        } else {
            holder.lblShipping.setText(context.getString(R.string.ship) + context.getResources().getString(R.string.currency) + StringUtility.replaceArabicNumbers(String.valueOf(lsvShop.get(position).getcurrentShipping())));
        }
        holder.lblVAT.setText(context.getString(R.string.vat) + StringUtility.replaceArabicNumbers(String.valueOf(lsvShop.get(position).getShopVAT())) + "%");
        holder.lblTotalPrice.setText(StringUtility.replaceArabicNumbers(String.valueOf(lsvShop.get(position).getTotalPrice())) + " " + context.getResources().getString(R.string.currency));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopCartListener.deleteItem(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("position", position);
                Intent intent = new Intent(context, ShopCartDetailActivity.class);
                intent.putExtras(b);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lsvShop.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgShop, btnDelete;
        ProgressBar progress;
        TextView lblShopName, lblCategory, lblAddress, lblFoodNumber, lblVAT, lblShipping,
                lblTotalPrice;
        LinearLayout layoutRowShopCart;

        public ViewHolder(View itemView) {
            super(itemView);
            imgShop = itemView.findViewById(R.id.imgShop);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            lblShopName = itemView.findViewById(R.id.lblShopName);
            lblVAT = itemView.findViewById(R.id.lblVAT);
            lblShipping = itemView.findViewById(R.id.lblShipping);
            lblFoodNumber = itemView.findViewById(R.id.lblFoodNumber);
            lblTotalPrice = itemView.findViewById(R.id.lblTotalPrice);
        }
    }

    public interface ShopCartListener {
        void deleteItem(int position);
    }
}

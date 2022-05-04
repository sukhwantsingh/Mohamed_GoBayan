package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.ExtraOptions;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductOrderAdapterV2 extends RecyclerView.Adapter<ProductOrderAdapterV2.ViewHolder> {

    private Activity context;
    private ArrayList<Menu> listProducts;
    private LayoutInflater inflater;

    public ProductOrderAdapterV2(Activity context, ArrayList<Menu> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_list_product_order_v2, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Menu product = listProducts.get(i);
        if (product != null) {
            holder.tvValueProductName.setText(product.getName());
            holder.tvValueProductPrice.setText(product.getPrice() + " " + context.getResources().getString(R.string.currency));
            Glide.with(context).load(product.getImage())
                    .error(R.drawable.no_image_available).into(holder.ivLogoProduct);
            holder.tvValueProductCode.setText(product.getCode());
            holder.tvValueProductNumber.setText(String.valueOf(product.getOrderNumber()));
            holder.tvValueProductTotal.setText(product.getTotalPrice() + " " + context.getResources().getString(R.string.currency));
            holder.tvValueProductExtraOption.setText(product.getOption());
        }
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_value_product_name)
        TextView tvValueProductName;
        @BindView(R.id.tv_value_product_code)
        TextView tvValueProductCode;
        @BindView(R.id.tv_value_product_extra_option)
        TextView tvValueProductExtraOption;
        @BindView(R.id.tv_value_product_price)
        TextView tvValueProductPrice;
        @BindView(R.id.tv_value_product_number)
        TextView tvValueProductNumber;
        @BindView(R.id.tv_value_product_total)
        TextView tvValueProductTotal;
        @BindView(R.id.iv_logo_product)
        RoundedImageView ivLogoProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

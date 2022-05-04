package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Menu;

import java.util.ArrayList;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<Menu> listProducts;
    private LayoutInflater inflater;


    public ProductOrderAdapter(Activity context, ArrayList<Menu> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = inflater.inflate(R.layout.item_list_product_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Menu product = listProducts.get(i);
        holder.tvProductName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice() + " " + context.getResources().getString(R.string.currency));
        holder.tvDescription.setText(product.getDescription());
        Glide.with(context).load(product.getImage()).into(holder.ivProduct);
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvPrice, tvDescription;
        private ImageView ivProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDescription = itemView.findViewById(R.id.tv_description);

            ivProduct = itemView.findViewById(R.id.iv_product);
        }
    }
}

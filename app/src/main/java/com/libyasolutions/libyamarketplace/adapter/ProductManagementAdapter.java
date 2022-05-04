package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.interfaces.AdapterListener;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import java.util.ArrayList;

public class ProductManagementAdapter extends RecyclerView.Adapter<ProductManagementAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<Menu> listProducts;
    private LayoutInflater inflater;
    private AdapterListener.onItemClickListener onItemClickListener;
    private OnItemProductManagement onItemProductManagement;

    public ProductManagementAdapter(Activity context, ArrayList<Menu> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemProductManagement(OnItemProductManagement onItemProductManagement) {
        this.onItemProductManagement = onItemProductManagement;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_list_product, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Menu product = listProducts.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvDescription.setText(product.getDescription());

        if (product.getStatus().equals("1")) {
            holder.tvStatus.setText(context.getResources().getString(R.string.active));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_red_border));

            holder.tvDeleteProduct.setEnabled(true);
        } else {
            holder.tvStatus.setText(context.getResources().getString(R.string.inactive));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.gray_light));
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_gray_border));

            holder.tvDeleteProduct.setEnabled(false);
        }
        holder.tvPrice.setText(product.getPrice() + " " + context.getResources().getString(R.string.currency));
        holder.tvProductCode.setText(product.getCode());

        Glide.with(context).load(product.getImage()).into(holder.ivProduct);

        // update product
        holder.tvUpdateProduct.setOnClickListener(view -> {
            onItemClickListener.onclick(view, position);
        });

        // delete product
        holder.tvDeleteProduct.setOnClickListener(view -> {
            onItemProductManagement.onItemDelete(view, position);
        });
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private TextView tvProductName, tvDescription, tvStatus, tvPrice;
        private TextView tvUpdateProduct;
        private TextView tvDeleteProduct;
        private TextView tvProductCode;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);

            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvPrice = itemView.findViewById(R.id.tv_price);

            tvUpdateProduct = itemView.findViewById(R.id.tv_update_product);
            tvDeleteProduct = itemView.findViewById(R.id.tv_delete_product);
            tvProductCode = itemView.findViewById(R.id.tv_product_code);
        }
    }

    public void setOnItemClickListener(AdapterListener.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemProductManagement {
        void onItemDelete(View view, int position);
    }
}

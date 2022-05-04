package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.object.ExtraOptions;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.OptionsItem;
import com.libyasolutions.libyamarketplace.util.StringUtility;

import java.util.ArrayList;

public class MyMenuAdapterNew extends RecyclerView.Adapter<MyMenuAdapterNew.ViewHolder> {
    private Context context;
    private ArrayList<Menu> menuArrayList;
    private ShopCartListener shopCartListener;

    public MyMenuAdapterNew(Context context, ArrayList<Menu> menuArrayList, ShopCartListener shopCartListener) {
        this.context = context;
        this.menuArrayList = menuArrayList;
        this.shopCartListener = shopCartListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_food_shop_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(menuArrayList.get(position).getImage()).into(holder.imgFood);
        holder.lblNumber.setText(StringUtility.replaceArabicNumbers(String.valueOf(menuArrayList.get(position).getOrderNumber())));
        holder.lblPrice.setText(context.getResources().getString(R.string.currency) + StringUtility.replaceArabicNumbers(String.valueOf(menuArrayList.get(position).getPrice())));
        holder.lblTotal.setText(context.getResources().getString(R.string.currency) + StringUtility.replaceArabicNumbers(String.valueOf(menuArrayList.get(position).getTotalPrice())));
        holder.lblOptions.setText(createOptionString(menuArrayList.get(position).getExtraOptions()));
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopCartListener.addItem(position, menuArrayList.get(position).getOrderNumber() + 1);


            }
        });
        holder.btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuArrayList.get(position).getOrderNumber() > 1) {
                    shopCartListener.removeItem(position, menuArrayList.get(position).getOrderNumber() - 1);
                }

            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopCartListener.deleteItem(position);
            }
        });
        holder.lblFoodName.setText(menuArrayList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return menuArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood, btnAdd, btnSubtract, btnDelete;
        ProgressBar progress;
        TextView lblFoodName, lblTotal, lblPrice, lblNumber, lblOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            btnAdd = itemView.findViewById(R.id.btnAddNumber);
            btnSubtract = itemView.findViewById(R.id.btnSubtractNumber);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            lblFoodName = itemView.findViewById(R.id.lblFoodName);
            lblTotal = itemView.findViewById(R.id.lblTotalPrice);
            lblPrice = itemView.findViewById(R.id.lblPrice);
            lblNumber = itemView.findViewById(R.id.lblNumberFood);
            lblOptions = itemView.findViewById(R.id.lblOptions);
        }
    }

    public interface ShopCartListener {
        void deleteItem(int position);

        void addItem(int position, int quanlity);

        void removeItem(int position, int quanlity);
    }
    private String createOptionString(ArrayList<ExtraOptions> listExtraOption) {
        StringBuilder option = new StringBuilder();
        for (ExtraOptions extraOption : listExtraOption) {
            for (OptionsItem optionItem : extraOption.getOptionsItems()) {
                if (optionItem.isChecked()) {
                    option.append(optionItem.getParentName()).append(":").append(optionItem.getOptionName()).append(",");
                    break;
                }
            }
        }
        if (option.length() > 1) {
            option = new StringBuilder(option.substring(0, option.length() - 1));
        }
        Log.e("kevin", "createOptionString: " + option);
        return option.toString();
    }
}

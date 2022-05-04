package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.ProductDetailsNewActivity;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.object.Menu;

import java.util.ArrayList;

public class ListFoodHomeAdapter extends RecyclerView.Adapter<ListFoodHomeAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Menu> lsvMenu;
    public static final String HOME_SCREEN = "homeActivity";

    public ListFoodHomeAdapter(Context mcontext, ArrayList<Menu> arrOffer) {
        this.context = mcontext;
        this.lsvMenu = arrOffer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(lsvMenu.get(position).getImage()).into(holder.imgOffer);
        holder.lblRateNumber.setText(lsvMenu.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu = lsvMenu.get(position);
                Bundle b = new Bundle();
                b.putString(GlobalValue.KEY_FOOD_ID, menu.getId() + "");
                b.putString(GlobalValue.KEY_NAVIGATE_TYPE, "FAST");
                b.putString(GlobalValue.KEY_FROM_SCREEN, HOME_SCREEN);
                GlobalValue.KEY_LOCAL_NAME = menu.getLocalName();
                Intent intent = new Intent(context, ProductDetailsNewActivity.class);
                intent.putExtras(b);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return lsvMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgOffer;
        private TextView lblRateNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            imgOffer =  itemView.findViewById(R.id.imgFood);

            lblRateNumber =  itemView.findViewById(R.id.lblRateNumber);

        }
    }
}

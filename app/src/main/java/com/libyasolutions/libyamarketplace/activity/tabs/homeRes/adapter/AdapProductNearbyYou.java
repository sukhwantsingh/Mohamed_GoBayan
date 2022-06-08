package com.libyasolutions.libyamarketplace.activity.tabs.homeRes.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.responses.ModelSingleItem;

import java.util.ArrayList;

public class AdapProductNearbyYou extends RecyclerView.Adapter<AdapProductNearbyYou.ViewHolder> {
    private Activity context;
    private ArrayList<ModelSingleItem> listCities;

    public AdapProductNearbyYou(Activity context, ArrayList<ModelSingleItem> listNames) {
        this.context = context;
        this.listCities = listNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_my_favourite_products, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        ModelSingleItem mode = listCities.get(position);
//        holder.tvTitle.setText(mode.getName());
//        if(mode.isSelected()){
//            holder.rlRootView.setBackgroundResource(R.drawable.blue_fill_round_gradient);
//            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.white));
//        } else {
//          holder.rlRootView.setBackgroundResource(R.drawable.blue_outline_gradient);
//          holder.tvTitle.setTextColor(context.getResources().getColor(R.color.blue_search));
//
//        }

    }

    void selectItem(int pos){
        for (ModelSingleItem mod: listCities) {
            mod.setSelected(false);
        }
        listCities.get(pos).setSelected(true);
        notifyItemRangeChanged(0, listCities.size());
    }

   public Boolean isItemSelected() {
        for (ModelSingleItem mod: listCities) {
          if(mod.isSelected()) return true;
       }
       return false;
    }

    @Override
    public int getItemCount() {
        return 5; // listCities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private RelativeLayout rlRootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_name);
            rlRootView = itemView.findViewById(R.id.rl_root);
          //  itemView.setOnClickListener(v -> selectItem(getAbsoluteAdapterPosition()));
        }
    }

}

package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.interfaces.AdapterListener;
import com.libyasolutions.libyamarketplace.object.City;
import com.libyasolutions.libyamarketplace.responses.ModelSingleItem;

import java.util.ArrayList;
import java.util.List;

public class SingleSelectItemAdapter extends RecyclerView.Adapter<SingleSelectItemAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<ModelSingleItem> listCities;
    private AdapterListener.onItemClickListener onItemClickListener;

    public SingleSelectItemAdapter(Activity context, ArrayList<ModelSingleItem> listNames) {
        this.context = context;
        this.listCities = listNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_details_color_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelSingleItem mode = listCities.get(position);
        holder.tvTitle.setText(mode.getName());
        if(mode.isSelected()){
            holder.rlRootView.setBackgroundResource(R.drawable.blue_fill_round_gradient);
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.white));
        } else {
          holder.rlRootView.setBackgroundResource(R.drawable.blue_outline_gradient);
          holder.tvTitle.setTextColor(context.getResources().getColor(R.color.blue_search));

        }

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
        return listCities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private RelativeLayout rlRootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_name);
            rlRootView = itemView.findViewById(R.id.rl_root);
            itemView.setOnClickListener(v -> selectItem(getAbsoluteAdapterPosition()));
        }
    }

    public void setOnItemClickListener(AdapterListener.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

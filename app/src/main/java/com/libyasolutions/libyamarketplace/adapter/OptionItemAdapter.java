package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.OptionsItem;

import java.util.ArrayList;

public class OptionItemAdapter extends RecyclerView.Adapter<OptionItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<OptionsItem> optionsItems;
    private sendData sendData;
    private int oldPosition = -1;
    private int currentPosition = -1;

    public OptionItemAdapter(Context context, ArrayList<OptionsItem> optionsItems) {
        this.context = context;
        this.optionsItems = optionsItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_optionsitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tvOptionIteml.setText(optionsItems.get(i).getOptionName());
        if (currentPosition != oldPosition) {
            if (currentPosition == i) {
                optionsItems.get(i).setChecked(true);
            } else {
                optionsItems.get(i).setChecked(false);
            }
        } else {
            if (currentPosition == i) {
                if (optionsItems.get(i).isChecked()) {
                    optionsItems.get(i).setChecked(false);
                } else {
                    optionsItems.get(i).setChecked(true);
                }
            }
        }
        if (optionsItems.get(i).isChecked()) {
            viewHolder.tvOptionIteml.setBackground(context.getResources().getDrawable(R.drawable.bg_red_button_with_border));
        } else {
            viewHolder.tvOptionIteml.setBackground(context.getResources().getDrawable(R.drawable.bg_red_border));
        }

    }

    @Override
    public int getItemCount() {
        return optionsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOptionIteml;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOptionIteml = itemView.findViewById(R.id.tvOptionsItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendData.sendData(getAdapterPosition(),optionsItems.get(getAdapterPosition()).getParentName() + ":" + optionsItems.get(getAdapterPosition()).getOptionName());
                }
            });
        }
    }

    public void setCurrentPosition(int currentPosition) {
        oldPosition = this.currentPosition;
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public interface sendData {
        void sendData(int position, String data);
    }

    public void setOnItemClick(sendData onItemClick) {
        this.sendData = onItemClick;
    }
}

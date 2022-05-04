package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.ExtraOptions;

import java.util.ArrayList;

public class OptionsAdapterDialog extends RecyclerView.Adapter<OptionsAdapterDialog.ViewHolder> {
    private Context context;
    private ArrayList<ExtraOptions> extraOptions;

    public OptionsAdapterDialog(Context context, ArrayList<ExtraOptions> extraOptions) {
        this.context = context;
        this.extraOptions = extraOptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_extraoption_recycler, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tvOptions.setText(extraOptions.get(position).getExtraName());

        final OptionItemAdapter optionItemAdapterl = new OptionItemAdapter(context, extraOptions.get(position).getOptionsItems());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewHolder.rclOptionsItem.setLayoutManager(linearLayoutManager);
        viewHolder.rclOptionsItem.setAdapter(optionItemAdapterl);
        optionItemAdapterl.setOnItemClick(new OptionItemAdapter.sendData() {
            @Override
            public void sendData(int positionOption, String data) {
                optionItemAdapterl.setCurrentPosition(positionOption);

            }
        });

    }

    @Override
    public int getItemCount() {
        return extraOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOptions;
        private RecyclerView rclOptionsItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOptions = itemView.findViewById(R.id.tvOptions);
            rclOptionsItem = itemView.findViewById(R.id.rclOptionItem);
        }
    }
}

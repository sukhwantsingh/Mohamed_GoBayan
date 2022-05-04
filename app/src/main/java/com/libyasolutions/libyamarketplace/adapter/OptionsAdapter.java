package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.ExtraOptions;
import com.libyasolutions.libyamarketplace.object.OptionsItem;

import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ExtraOptions> extraOptionsArrayList;

    public OptionsAdapter(Context context, ArrayList<ExtraOptions> extraOptionsArrayList) {
        this.context = context;
        this.extraOptionsArrayList = extraOptionsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_extraoption, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        StringBuilder stringBuilder = new StringBuilder();
        for (OptionsItem optionsItem : extraOptionsArrayList.get(position).getOptionsItems()) {
            stringBuilder.append(",").append(optionsItem.getOptionName());
        }
        viewHolder.tvOptions.setText(extraOptionsArrayList.get(position).getExtraName() + ":" +stringBuilder);
    }

    @Override
    public int getItemCount() {
        return extraOptionsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOptions = itemView.findViewById(R.id.tvOptions);
        }
    }
}

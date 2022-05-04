package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.OptionsItem;

import java.util.ArrayList;

public class ChildOptionAdapter extends RecyclerView.Adapter<ChildOptionAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<OptionsItem> listOptions;
    private LayoutInflater inflater;
    private OnCheckChangeListener onCheckChangeListener;

    public ChildOptionAdapter(Activity context, ArrayList<OptionsItem> listOptions) {
        this.context = context;
        this.listOptions = listOptions;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_list_option, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        OptionsItem optionsItem = listOptions.get(i);
        holder.tvOption.setText(optionsItem.getOptionName());
        holder.checkBox.setChecked(optionsItem.isChecked());
    }

    @Override
    public int getItemCount() {
        return listOptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOption;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOption = itemView.findViewById(R.id.tv_option);
            checkBox = itemView.findViewById(R.id.checkbox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onCheckChangeListener.onCheckChange(buttonView, getAdapterPosition(), isChecked);
                }
            });
//            checkBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onCheckChangeListener.onCheckChange(v, getAdapterPosition(), checkBox.isChecked());
//                }
//            });
        }
    }

    public void setOnCheckChangeListener(OnCheckChangeListener onCheckChangeListener) {
        this.onCheckChangeListener = onCheckChangeListener;
    }

    public interface OnCheckChangeListener {
        void onCheckChange(View view, int position, boolean isChecked);
    }
}

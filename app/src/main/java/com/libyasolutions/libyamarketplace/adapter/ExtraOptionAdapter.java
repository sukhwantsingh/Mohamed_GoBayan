package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.interfaces.AdapterListener;
import com.libyasolutions.libyamarketplace.object.ExtraOptions;
import com.libyasolutions.libyamarketplace.object.OptionsItem;

import java.util.ArrayList;

public class ExtraOptionAdapter extends RecyclerView.Adapter<ExtraOptionAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<ExtraOptions> listOptions;
    private LayoutInflater inflater;

    private AdapterListener.onItemClickListener onItemClickListener;

    public ExtraOptionAdapter(Activity context, ArrayList<ExtraOptions> listOptions) {
        this.context = context;
        this.listOptions = listOptions;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.item_list_extra_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        ExtraOptions extraOptions = listOptions.get(i);
        holder.tvOptionName.setText(extraOptions.getExtraName());
        ArrayList<OptionsItem> optionsItems = extraOptions.getOptionsItems();
        StringBuilder option = new StringBuilder();
        for (OptionsItem optionsItem : optionsItems) {
            if (optionsItem.isChecked()) {
                option.append(optionsItem.getOptionName()).append(" ,");
            }
        }
        if (option.length() > 1 && option.toString().contains(","))
            option = new StringBuilder(option.substring(0, option.length() - 1));
        holder.edtOption.setText(option.toString());
    }

    @Override
    public int getItemCount() {
        return listOptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOptionName;
        private EditText edtOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOptionName = itemView.findViewById(R.id.tv_option_name);
            edtOption = itemView.findViewById(R.id.edt_option);

            edtOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onclick(v, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(AdapterListener.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

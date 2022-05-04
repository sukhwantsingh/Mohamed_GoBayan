package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Category;

import java.util.ArrayList;

public class ShopCategoryAdapter extends RecyclerView.Adapter<ShopCategoryAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<Category> listCategories;
    private LayoutInflater inflater;
    private OnCheckChangeListener onCheckChangeListener;

    public ShopCategoryAdapter(Activity context, ArrayList<Category> listCategories) {
        this.context = context;
        this.listCategories = listCategories;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_list_category, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = listCategories.get(position);
        holder.tvTitle.setText(category.getName());
        holder.checkBox.setChecked(category.isCheck());
    }

    @Override
    public int getItemCount() {
        return listCategories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private CheckBox checkBox;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            checkBox = itemView.findViewById(R.id.checkbox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheckChangeListener.onCheckChange(checkBox, getAdapterPosition(), checkBox.isChecked());
                }
            });
        }
    }

    public void setOnCheckChangeListener(OnCheckChangeListener onCheckChangeListener) {
        this.onCheckChangeListener = onCheckChangeListener;
    }

    public interface OnCheckChangeListener {
        void onCheckChange(View view, int position, boolean isChecked);
    }
}

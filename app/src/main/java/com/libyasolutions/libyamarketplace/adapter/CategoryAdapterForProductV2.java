package com.libyasolutions.libyamarketplace.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapterForProductV2 extends RecyclerView.Adapter<CategoryAdapterForProductV2.ViewHolder> {

    private Activity activity;
    private List<Category> categoryList;

    private OnItemProductCategoryListener listener;

    public void setOnItemProductCategoryListener(OnItemProductCategoryListener listener) {
        this.listener = listener;
    }

    public CategoryAdapterForProductV2(Activity activity, List<Category> categoryList) {
        this.activity = activity;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(viewGroup.getContext())
               .inflate(R.layout.item_product_category, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Category category = categoryList.get(position);
        if (category != null) {
            viewHolder.tvCategoryName.setText(category.getName());
            if (category.isCheck()) {
                viewHolder.tvCategoryName.setBackgroundResource(R.drawable.bg_red_button_with_border);
                viewHolder.tvCategoryName.setTextColor(activity.getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemProductCategory(getAdapterPosition(), true);
                }
            });
        }

    }

    public interface OnItemProductCategoryListener {
        void onItemProductCategory(int position, boolean isChecked);
    }
}

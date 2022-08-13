package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.CategoryV3;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryV3Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<CategoryV3> categoryV3List;
    private OnChooseCategoryListener listener;

    public CategoryV3Adapter(Context context, List<CategoryV3> categoryV3List,
                             OnChooseCategoryListener listener) {
        this.context = context;
        this.categoryV3List = categoryV3List;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_category_v3, viewGroup, false);
            return new ViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_category_header, viewGroup, false);
            return new HeaderHolder(view);
        }
        throw new RuntimeException("No match for" + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryV3 categoryV3 = categoryV3List.get(position);
        if (categoryV3 != null) {
            if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
                String categoryName = categoryV3.getMenuName();
                viewHolder.cbCategory.setText(categoryName);
                viewHolder.cbCategory.setChecked(categoryV3.isChecked());
            } else if (holder instanceof HeaderHolder) {
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.tvCategoryHeader.setText(categoryV3.getDepartmentName());
            }

        }
    }

    @Override
    public int getItemCount() {
        return categoryV3List.size();
    }

    @Override
    public int getItemViewType(int position) {
        return categoryV3List.get(position).isHeader() ? TYPE_HEADER : TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cb_category)
        RadioButton cbCategory;
        @BindView(R.id.ll_item_category)
        LinearLayout llItemCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llItemCategory.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == llItemCategory) {
                listener.onChooseCategory(getAbsoluteAdapterPosition(), categoryV3List.get(getAbsoluteAdapterPosition()));
            }
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_category_header)
        TextView tvCategoryHeader;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnChooseCategoryListener {
        void onChooseCategory(int position, CategoryV3 categoryV3);
    }
}

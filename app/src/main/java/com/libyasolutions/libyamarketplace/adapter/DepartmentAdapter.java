package com.libyasolutions.libyamarketplace.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.object.DepartmentCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {

    private static final String TAG = "DepartmentAdapter";

    private Context context;
    private List<DepartmentCategory> departmentCategoryList;
    private OnSearchByDepartmentListener listener;

    public DepartmentAdapter(Context context, List<DepartmentCategory> categoryList, OnSearchByDepartmentListener listener) {
        this.context = context;
        this.departmentCategoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_search,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        DepartmentCategory department = departmentCategoryList.get(position);
        if (department != null) {
            String categoryImageUrl = department.getDepartmentThumbnail();
            String categoryName = department.getDepartmentName();

            if (categoryImageUrl != null) {
                Glide.with(context)
                        .load(categoryImageUrl)
                        .error(R.drawable.no_image_available)
                        .dontAnimate()
                        .dontTransform()
                        .into(viewHolder.ivDepartmentThumbnail);
            }

            if (categoryName != null) {
                viewHolder.cbDepartment.setText(categoryName);
            }
            viewHolder.cbDepartment.setChecked(department.isChecked());

        }

    }

    @Override
    public int getItemCount() {
        return departmentCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_department_thumbnail)
        ImageView ivDepartmentThumbnail;
        @BindView(R.id.cb_department)
        CheckBox cbDepartment;
        @BindView(R.id.cl_department_category)
        ConstraintLayout clDepartmentCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            clDepartmentCategory.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == clDepartmentCategory) {
                listener.onSearchByDepartment(getAdapterPosition());
            }
        }
    }

    public interface OnSearchByDepartmentListener {
        void onSearchByDepartment(int position);
    }

}

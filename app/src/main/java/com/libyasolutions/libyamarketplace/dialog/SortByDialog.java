package com.libyasolutions.libyamarketplace.dialog;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.BaseDialog;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.config.ConstantApp;

import butterknife.BindView;
import butterknife.OnClick;

public class SortByDialog extends BaseDialog {

    private static final String EXTRA_SORT_BY = "EXTRA_SORT_BY";

    @BindView(R.id.tv_sort_by_rating)
    TextView tvSortByRating;
    @BindView(R.id.tv_sort_by_name)
    TextView tvSortByName;
    @BindView(R.id.tv_sort_by_date)
    TextView tvSortByDate;

    private OnSortByListener listener;
    private String sortBy = "date";

    public static SortByDialog newInstance(String sortBy) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SORT_BY, sortBy);
        SortByDialog fragment = new SortByDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            sortBy = getArguments().getString(EXTRA_SORT_BY);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.dialog_sort_by;
    }

    @Override
    protected void configView() {
        switch (sortBy) {
            case ConstantApp.SORT_BY_RATING:
                tvSortByRating.setTextColor(getResources().getColor(R.color.white));
                tvSortByRating.setBackground(ContextCompat.getDrawable(getActivity(),
                        R.drawable.bg_red_button_with_border));
                break;
            case ConstantApp.SORT_BY_NAME:
                tvSortByName.setTextColor(getResources().getColor(R.color.white));
                tvSortByName.setBackground(ContextCompat.getDrawable(getActivity(),
                        R.drawable.bg_red_button_with_border));
                break;
            case ConstantApp.SORT_BY_DATE:
                tvSortByDate.setTextColor(getResources().getColor(R.color.white));
                tvSortByDate.setBackground(ContextCompat.getDrawable(getActivity(),
                        R.drawable.bg_red_button_with_border));
                break;
        }
    }

    @Override
    protected int getStyleDialog() {
        return 0;
    }

    @Override
    protected double initWidthDialog() {
        return 0.85;
    }

    @Override
    protected double initHeightDialog() {
        return 0;
    }

    @Override
    protected boolean isCancelOnTouchOutside() {
        return true;
    }

    @OnClick(R.id.tv_sort_by_date)
    void sortByDate() {
        if (listener != null) {
            listener.onSortByDate();
        }
        dismiss();
    }

    @OnClick(R.id.tv_sort_by_name)
    void sortByName() {
        if (listener != null) {
            listener.onSortByName();
        }
        dismiss();
    }

    @OnClick(R.id.tv_sort_by_rating)
    void setTvSortByRating() {
        if (listener != null) {
            listener.onSortByRating();
        }
        dismiss();
    }

    public void setOnSortByListener(OnSortByListener listener) {
        this.listener = listener;
    }

    public interface OnSortByListener {
        void onSortByDate();

        void onSortByName();

        void onSortByRating();
    }
}

package com.libyasolutions.libyamarketplace;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialog extends DialogFragment {
    private Unbinder unbinder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getStyleDialog());
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(isCancelOnTouchOutside());
            dialog.setCancelable(isCancelOnTouchOutside());
        }
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * initWidthDialog());

        int height;
        if (initHeightDialog() == 0) {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            height = (int) (getResources().getDisplayMetrics().heightPixels * initHeightDialog());
        }

        if (window != null) {
            window.setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(layoutResId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initData();
        configView();
        return rootView;
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    protected void showToast(int messageResId) {
        Toast.makeText(getActivity(), getString(messageResId), Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected abstract void initData();

    protected abstract int layoutResId();

    protected abstract void configView();

    protected abstract int getStyleDialog();

    protected abstract double initWidthDialog();

    protected abstract double initHeightDialog();

    protected abstract boolean isCancelOnTouchOutside();
}

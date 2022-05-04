package com.libyasolutions.libyamarketplace.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.BaseDialog;
import com.libyasolutions.libyamarketplace.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DistanceDialog extends BaseDialog {

    private static final String EXTRA_DISTANCE = "EXTRA_DISTANCE";

    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.sb_distance)
    SeekBar sbDistance;

    private OnSearchByDistanceListener listener;
    private String distance = "45";

    public static DistanceDialog newInstance(String distance) {
        DistanceDialog fragment = new DistanceDialog();
        Bundle args = new Bundle();
        args.putString(EXTRA_DISTANCE, distance);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            distance = getArguments().getString(EXTRA_DISTANCE);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.dialog_distance;
    }

    @Override
    protected void configView() {
        sbDistance.setMax(450);
        sbDistance.setPadding(10, 0, 10, 0);
        sbDistance.setProgress((int) (Double.parseDouble(distance) * 10));
        if (Double.parseDouble(distance) == 0) {
            tvDistance.setText(getString(R.string.value_distance_all, getString(R.string.text_0_km)));
        } else {
            tvDistance.setText(getString(R.string.value_distance, distance));
        }

        // listener
        sbDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                distance = ((double) progress / 10) + "";
                if (progress == 0) {
                    tvDistance.setText(getString(R.string.value_distance_all,
                            getString(R.string.text_0_km)));
                } else {
                    tvDistance.setText(getString(R.string.value_distance, distance));
                }

                if (listener != null) {
                    listener.onSearchByDistance(distance);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    public void setOnSearchByDistanceListener(OnSearchByDistanceListener listener) {
        this.listener = listener;
    }

    public interface OnSearchByDistanceListener {
        void onSearchByDistance(String distance);
    }
}

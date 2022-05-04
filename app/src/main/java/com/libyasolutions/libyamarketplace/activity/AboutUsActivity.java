package com.libyasolutions.libyamarketplace.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;

public class AboutUsActivity extends BaseActivity {

    ImageView btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initUI();
        initControl();
    }

    private void initControl() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initUI() {
        btnBack = findViewById(R.id.btnBack);
    }
}

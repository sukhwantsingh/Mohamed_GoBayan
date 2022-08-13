package com.libyasolutions.libyamarketplace;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.libyasolutions.libyamarketplace.activity.ListCategoryActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivityV2 extends AppCompatActivity {
    protected String TAG = getClass().getName();
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        setupToolbar();
        initData();
        configView();
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    protected abstract void setupToolbar();

    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract void configView();

     protected void showToast(int messageResId) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void gotoActivity(Class<?> cla) {
        Intent intent = new Intent(this, cla);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
    }

    public void backActivity(Class<?> cla, Bundle b) {
        Intent intent = new Intent(this, ListCategoryActivity.class);
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
    }


    public void backActivity(Class<?> cla) {
        Intent intent = new Intent(this, cla);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void gotoActivity(Class<?> cla, int flag) {
        Intent intent = new Intent(this, cla);
        intent.setFlags(flag);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
    }

    public void gotoWeb(Uri uri) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(myIntent);
    }
    public void gotoActivityForResult(Context context, Class<?> cla,
                                      int requestCode) {
        Intent intent = new Intent(context, cla);
        startActivityForResult(intent, requestCode);
    }

}

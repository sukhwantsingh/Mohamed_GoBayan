package com.libyasolutions.libyamarketplace.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.FoodOfShopOrderAdapter;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

@SuppressLint("NewApi")
public class DetailFoodOfShopOrderActivity extends BaseActivity implements
        OnClickListener {

    private ImageView btnBack, imgShop;
    private ListView lsvShops;
    private FoodOfShopOrderAdapter shopAdapter;
    private ArrayList<Menu> arrShopOrders;
    private TextView lblSum, lblVAT, lblShip, lblShopName, lblTitle, lblAddress, lblCategory, lblProductNumber;
    private double totalVAT = 0, totalShip = 0, totalPrice = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_of_shop_cart);
        initUI();
        initUIControls();
        updateOrderValue();

    }

    private void initUI() {
        imgShop = (ImageView) findViewById(R.id.imgShop);
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        lblShopName = (TextView) findViewById(R.id.lblShopName);
        lblCategory = (TextView) findViewById(R.id.lblCategoryName);
        lblAddress = (TextView) findViewById(R.id.lblAddress);
        lblProductNumber = (TextView) findViewById(R.id.lblFoodNumber);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        lsvShops = (ListView) findViewById(R.id.lsvShop);
        lblSum = (TextView) findViewById(R.id.lblTotalPrice);
        lblShip = (TextView) findViewById(R.id.lblShipping);
        lblVAT = (TextView) findViewById(R.id.lblVAT);
    }

    private void initUIControls() {

        arrShopOrders = GlobalValue.currentShopOrder.getArrFoods();
        shopAdapter = new FoodOfShopOrderAdapter(self, arrShopOrders);
        lsvShops.setAdapter(shopAdapter);
        btnBack.setOnClickListener(this);
    }

    private void updateOrderValue() {

        lblShopName.setText(GlobalValue.currentShopOrder.getShopName());
        lblTitle.setText(GlobalValue.currentShopOrder.getShopName());
        lblCategory.setText(GlobalValue.currentShopOrder.getShopCategories());
        lblAddress.setText(GlobalValue.currentShopOrder.getShopAddress());
        lblProductNumber.setText(GlobalValue.currentShopOrder.getNumberItems()+" " + getString(R.string.item));
        totalPrice = GlobalValue.currentShopOrder.getGrandTotal();
        totalShip = GlobalValue.currentShopOrder.getShipping();
        totalVAT = GlobalValue.currentShopOrder.getVAT();

        lblVAT.setText(getResources().getString(R.string.vat) + " " + getResources().getString(R.string.currency)
                + String.format("%.1f", totalVAT));
        lblShip.setText(getResources().getString(R.string.ship) + " " + getResources().getString(R.string.currency)
                + String.format("%.1f", totalShip));
        lblSum.setText(String.format("%.1f", totalPrice));

        Picasso.with(this).load(GlobalValue.currentShopOrder.getShopImage())
                .error(R.drawable.no_image_available_horizontal)
                .resize(300, 300)
                .centerInside()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Drawable d = new BitmapDrawable(self
                                .getResources(), bitmap);
                        imgShop.setBackground(d);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnBack) {
            onBackPressed();
        }
    }
}

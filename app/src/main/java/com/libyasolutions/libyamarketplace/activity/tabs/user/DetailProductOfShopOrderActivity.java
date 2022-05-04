package com.libyasolutions.libyamarketplace.activity.tabs.user;

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
import com.libyasolutions.libyamarketplace.object.Shop;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

@SuppressLint("NewApi")
public class DetailProductOfShopOrderActivity extends BaseActivity {
    private ImageView btnBack, imgShop;
    private TextView lblSum, lblVAT, lblShip, lblShopName, lblTitle, lblAddress, lblCategory, lblProductNumber;
    private ListView lsvFood;
    private FoodOfShopOrderAdapter foodAdapter;
    private double total;
    public Shop shop = null;
    private int shop_position = -1;

    public interface MenuListener {
        public void deleteItem(int position);

        public void addQuantity(int position, int quantity);

        public void deleteQuantity(int position, int quantity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_of_shop_cart);
        initUI();
        initData();

    }

    private void initUI() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        imgShop = (ImageView) findViewById(R.id.imgShop);
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        lblShopName = (TextView) findViewById(R.id.lblShopName);
        lblCategory = (TextView) findViewById(R.id.lblCategoryName);
        lblAddress = (TextView) findViewById(R.id.lblAddress);
        lblProductNumber = (TextView) findViewById(R.id.lblFoodNumber);
        lblSum = (TextView) findViewById(R.id.lblTotalPrice);
        lsvFood = (ListView) findViewById(R.id.lsvShop);
        lblShip = (TextView) findViewById(R.id.lblShipping);
        lblVAT = (TextView) findViewById(R.id.lblVAT);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            shop_position = b.getInt("position");
        }
        shop = GlobalValue.arrMyMenuShop.get(shop_position);
        lblShopName.setText(shop.getShopName());
        lblTitle.setText(shop.getShopName());
        lblCategory.setText(shop.getCategory());
        lblAddress.setText(shop.getAddress());
        Picasso.with(this).load(shop.getImage())
                .error(R.drawable.no_image_available_horizontal)
                .resize(300,300)
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



        lsvFood.setAdapter(foodAdapter);
        refreshContent();
    }

    private void refreshContent() {

        lblProductNumber.setText(shop.getNumberItems() + " " + getString(R.string.item));

        lblVAT.setText(getString(R.string.vat) + " "
                + getString(R.string.currency)
                + String.format("%.1f", shop.getCurrentTotalVAT()));
        lblShip.setText(getString(R.string.ship) + " "
                + getString(R.string.currency)
                + String.format("%.1f", shop.getcurrentShipping()));

        total = shop.getCurrentTotalVAT() + shop.getcurrentShipping()
                + shop.getTotalPrice();
        lblSum.setText(String.format("%.1f", total));

        foodAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

}

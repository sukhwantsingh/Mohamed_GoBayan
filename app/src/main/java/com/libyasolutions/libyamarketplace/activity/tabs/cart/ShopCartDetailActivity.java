package com.libyasolutions.libyamarketplace.activity.tabs.cart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.MyMenuAdapterNew;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.StringUtility;

@SuppressLint("NewApi")
public class ShopCartDetailActivity extends BaseActivity implements MyMenuAdapterNew.ShopCartListener {
    private ImageView btnBack;
    private TextView lblSum, lblVAT, lblShip, lblShopName;
    private RecyclerView lsvFood;
    private MyMenuAdapterNew menuAdapterNew;
    private double total;
    public Shop shop = null;
    private int shop_position = -1;
    private ImageView imgShop;
    private LinearLayout llbottom;
    private TextView tvCate,tvDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfood_of_shopcart);
        initUI();
        initData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(self);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lsvFood.setLayoutManager(layoutManager);
        menuAdapterNew = new MyMenuAdapterNew(self, shop.getArrOrderFoods(), this);
        lsvFood.setAdapter(menuAdapterNew);

    }

    private void initUI() {
        tvCate=findViewById(R.id.tvCate);
        tvDescription=findViewById(R.id.tvDesciption);
        llbottom = findViewById(R.id.llbottom);
        imgShop = findViewById(R.id.imgShop);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        lblShopName = findViewById(R.id.lblShopName);
        lblSum = (TextView) findViewById(R.id.lblTotalPrice);
        lsvFood = findViewById(R.id.lsvShop);
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
        tvDescription.setText(shop.getFacebook());
        tvCate.setText(shop.getEmail());
        Glide.with(this).load(shop.getImage()).into(imgShop);
        lblShopName.setText(shop.getShopName());
        lblVAT.setText(getString(R.string.currency) + StringUtility.replaceArabicNumbers(String.format("%.1f", shop.getCurrentTotalVAT())));
        if ((shop.getTotalPrice() + shop.getCurrentTotalVAT()) >= shop.getMinPriceForDelivery()) {
            lblShip.setText(getString(R.string.currency) + StringUtility.replaceArabicNumbers(String.valueOf(0)));
            total = shop.getCurrentTotalVAT() + shop.getTotalPrice();
        } else {
            lblShip.setText(getString(R.string.currency)
                    + StringUtility.replaceArabicNumbers(String.format("%.1f", shop.getDeliveryPrice())));
            total = shop.getCurrentTotalVAT() + shop.getDeliveryPrice()
                    + shop.getTotalPrice();
        }
        lblSum.setText(getString(R.string.currency) + StringUtility.replaceArabicNumbers(String.format("%.1f", total)));

    }

    private void refreshContent() {

        lblVAT.setText(getString(R.string.currency)
                + StringUtility.replaceArabicNumbers(String.format("%.1f", shop.getCurrentTotalVAT())));
        if ((shop.getTotalPrice() + shop.getCurrentTotalVAT()) >= shop.getMinPriceForDelivery()) {
            lblShip.setText(getString(R.string.currency) + StringUtility.replaceArabicNumbers(String.valueOf(0)));
            total = shop.getCurrentTotalVAT() + shop.getTotalPrice();
        } else {
            lblShip.setText(getString(R.string.currency)
                    + StringUtility.replaceArabicNumbers(String.format("%.1f", shop.getDeliveryPrice())));
            total = shop.getCurrentTotalVAT() + shop.getDeliveryPrice()
                    + shop.getTotalPrice();
        }
        lblSum.setText(getString(R.string.currency) + StringUtility.replaceArabicNumbers(String.format("%.1f", total)));

        menuAdapterNew.notifyDataSetChanged();
        if (GlobalValue.arrMyMenuShop.get(shop_position).getArrOrderFoods().size() > 0){
            llbottom.setVisibility(View.GONE);
        }else {
            llbottom.setVisibility(View.GONE);
            onBackPressed();
        }

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

    @Override
    public void deleteItem(int position) {
        shop.removeFoodOrder(shop.getArrOrderFoods().get(
                position));
        refreshContent();

    }

    @Override
    public void addItem(int position, int quanlity) {
        shop.updateQuantityOfFood(position, quanlity);
        GlobalValue.arrMyMenuShop.get(shop_position)
                .updateQuantityOfFood(position, quanlity);
        refreshContent();

    }

    @Override
    public void removeItem(int position, int quanlity) {
        shop.updateQuantityOfFood(position, quanlity);
        GlobalValue.arrMyMenuShop.get(shop_position).updateQuantityOfFood(position, quanlity);
        refreshContent();

    }
}

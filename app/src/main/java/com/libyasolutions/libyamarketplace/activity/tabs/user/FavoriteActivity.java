package com.libyasolutions.libyamarketplace.activity.tabs.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.Shop;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class FavoriteActivity extends BaseActivity implements OnClickListener {

    public static final int SHOP_TAB_INDEX = 0;
    public static final int PRODUCT_TAB_INDEX = 1;
    private ImageView btnBack;
    private TextView lblShopTab, lblProductTab, title,tv_order_quality;
    private ViewPager viewPager;
    FragmentPagerAdapter pagerAdapter;
    private boolean isSelectedShop = true;
    private ArrayList<Shop> arrFavoriteShops = new ArrayList<>();
    private ArrayList<Menu> arrFavoriteProducts = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_favorite);
        initUI();
        initControl();
        if (getIntent().getStringExtra("type").equals("1")) {
            title.setText(R.string.shop_fav_title);
             isSelectedShop = true;
        } else {
            title.setText(R.string.product_fav_title);
            isSelectedShop = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    private void refreshContent() {
        setData();
    }

    private void initUI() {
        btnBack = findViewById(R.id.btnBack);
        lblProductTab = findViewById(R.id.lblProductTab);
        title = findViewById(R.id.title);
        tv_order_quality = findViewById(R.id.tv_order_quality);
        lblShopTab = findViewById(R.id.lblShopTab);
        viewPager =  findViewById(R.id.viewPagerFavorite);

    }

    private void initControl() {
        btnBack.setOnClickListener(this);
        lblProductTab.setOnClickListener(this);
        lblShopTab.setOnClickListener(this);
    }

    private void setData() {
        ModelManager.getFavourite(this, GlobalValue.myAccount.getId(), false, new ModelManagerListener() {
            @Override
            public void onError(VolleyError error) {

            }

            @Override
            public void onSuccess(Object object) {
                String json = (String) object;
                Log.wtf("res: ","_  " + json);
                //add favorite shop list
                arrFavoriteShops.clear();
                arrFavoriteShops.addAll(ParserUtility.getListFavouriteShop(json));

                //add favorite products
                arrFavoriteProducts.clear();
                arrFavoriteProducts.addAll(ParserUtility.parseListFavoriteFood(json));

                initViewPager();
                setQuantity();
            }
        });
    }

    private void initViewPager() {
        pagerAdapter = new FavoritePageFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == SHOP_TAB_INDEX)  isSelectedShop = true;
                else isSelectedShop = false;

                updateSelectedTab();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateSelectedTab();
    }

    private void setQuantity(){
        if (isSelectedShop) {
            String orderCount = "<b>" + arrFavoriteShops.size() + "</b>";
            tv_order_quality.setText(Html.fromHtml(getString(R.string.shop_found, orderCount)));
        } else {
            String orderCount = "<b>" + arrFavoriteProducts.size() + "</b>";
            tv_order_quality.setText(Html.fromHtml(getString(R.string.order_found, orderCount)));
        }


    }
    private void updateSelectedTab() {
        if (isSelectedShop) {
            // set select shop
            viewPager.setCurrentItem(SHOP_TAB_INDEX);
        } else {
            viewPager.setCurrentItem(PRODUCT_TAB_INDEX);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == lblProductTab) {
            isSelectedShop = false;
            updateSelectedTab();
            viewPager.setCurrentItem(PRODUCT_TAB_INDEX);
            return;
        }
        if (v == lblShopTab) {
            isSelectedShop = true;
            updateSelectedTab();
            viewPager.setCurrentItem(SHOP_TAB_INDEX);
            return;
        }

        if (v == btnBack) {
            onBackPressed();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class FavoritePageFragmentAdapter extends FragmentPagerAdapter {

        public FavoritePageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == SHOP_TAB_INDEX) return FavoriteShopFragment.setInstance(self, arrFavoriteShops);
            else return FavoriteProductFragment.setInstance(self, arrFavoriteProducts);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == PRODUCT_TAB_INDEX)
                return self.getResources().getString(R.string.product_upper_case);
            else
                return self.getResources().getString(R.string.shops_upper_case);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}

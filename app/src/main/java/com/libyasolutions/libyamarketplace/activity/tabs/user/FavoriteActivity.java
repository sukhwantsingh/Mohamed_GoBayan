package com.libyasolutions.libyamarketplace.activity.tabs.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
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
    private TextView lblShopTab, lblProductTab;
    private View viewSelectedShop, viewSelectedProduct;
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

    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
    }

    private void refreshContent() {
        setData();
    }

    private void initUI() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        lblProductTab = (TextView) findViewById(R.id.lblProductTab);
        lblShopTab = (TextView) findViewById(R.id.lblShopTab);
        viewSelectedShop = findViewById(R.id.viewSelectedShops);
        viewSelectedProduct = findViewById(R.id.viewSelectedProduct);
        viewPager = (ViewPager) findViewById(R.id.viewPagerFavorite);

    }

    private void initControl() {
        btnBack.setOnClickListener(this);
        lblProductTab.setOnClickListener(this);
        viewSelectedProduct.setOnClickListener(this);
        lblShopTab.setOnClickListener(this);
        viewSelectedShop.setOnClickListener(this);
    }

    private void setData() {
        ModelManager.getFavourite(this, GlobalValue.myAccount.getId(), false, new ModelManagerListener() {
            @Override
            public void onError(VolleyError error) {

            }

            @Override
            public void onSuccess(Object object) {
                String json = (String) object;
                //add favorite shop list
                arrFavoriteShops.clear();
                arrFavoriteShops.addAll(ParserUtility.getListFavouriteShop(json));
                //add favorite products
                arrFavoriteProducts.clear();
                arrFavoriteProducts.addAll(ParserUtility.parseListFavoriteFood(json));
                initViewPager();

            }
        });
    }

    private void initViewPager() {
        pagerAdapter = new FavoritePageFragmentAdapter(
                getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == SHOP_TAB_INDEX)
                    isSelectedShop = true;
                else
                    isSelectedShop = false;

                updateSelectedTab();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // set current data
        viewPager.setCurrentItem(SHOP_TAB_INDEX);
        isSelectedShop = true;
        updateSelectedTab();
    }

    private void updateSelectedTab() {
        if (isSelectedShop) {
            // set select shop
            viewSelectedShop.setVisibility(View.VISIBLE);
            viewSelectedProduct.setVisibility(View.INVISIBLE);
            lblShopTab.setTextColor(getResources().getColor(R.color.cl_white));
            lblProductTab.setTextColor(getResources().getColor(R.color.gray_light));
        } else {
            viewSelectedShop.setVisibility(View.INVISIBLE);
            viewSelectedProduct.setVisibility(View.VISIBLE);
            lblShopTab.setTextColor(getResources().getColor(R.color.gray_light));
            lblProductTab.setTextColor(getResources().getColor(R.color.cl_white));
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == lblProductTab || v == viewSelectedProduct) {
            isSelectedShop = false;
            updateSelectedTab();
            viewPager.setCurrentItem(PRODUCT_TAB_INDEX);
            return;
        }
        if (v == lblShopTab || v == viewSelectedShop) {
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
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    class FavoritePageFragmentAdapter extends FragmentPagerAdapter {

        public FavoritePageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == SHOP_TAB_INDEX)
                return FavoriteShopFragment.setInstance(self, arrFavoriteShops);
            else
                return FavoriteProductFragment.setInstance(self, arrFavoriteProducts);
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

package com.libyasolutions.libyamarketplace.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ArrayRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.LoginActivity;
import com.libyasolutions.libyamarketplace.activity.tabs.MainTabActivity;
import com.libyasolutions.libyamarketplace.adapter.ShopAdapterNew;
import com.libyasolutions.libyamarketplace.adapter.SimilarShopAdapterNew;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.fragment.BannerFragment;
import com.libyasolutions.libyamarketplace.fragment.PostBannerFragment;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Banner;
import com.libyasolutions.libyamarketplace.object.OpenHour;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.responses.ShopDetailResponse;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Calendar;

public class ShopDetailsNew extends BaseActivity {
    private ImageView btnBack, homeIcon,imgShop,ivArr;
    private LinearLayout btnMap, btnProduct, btnHotline,llArrowDown;
    private TextView tvPhone, tvTime, tvDescription, tvPromotion, tvNameShop,tvPostDetails, tvAddress, totalComent,tvViewAll,tvPosts;
    private RecyclerView recyclerView;
    private int Shopid;
    private Shop shop;
    private RatingBar rtbRating;

    private ViewPager viewPager, viewPager_banners;
    FragmentPagerAdapter pagerAdapter;
    FragmentPagerAdapter postPagerAdapter;
    private ArrayList<Banner> arrBanner;
    private ArrayList<ShopDetailResponse.ShopPostBanner> arrPostBanner;

    private ArrayList<Banner> similarProductBanners = new ArrayList<>();
    private CirclePageIndicator indicatorBannerImages, indicatorGallary;
    private ImageView ivChat, ivTwitter,ivFavorite;
    private RelativeLayout rlChat, ivWebsite, ivInstagram, rlClock, ivFacebook,rlSaveShop;
    private Dialog loginDialog;
    private RelativeLayout rlSubOptions;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details_new);
        initView();
        initControls();
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            if (b.containsKey(GlobalValue.KEY_SHOP_ID)) {
                Shopid = b.getInt(GlobalValue.KEY_SHOP_ID);
            }
        }
        initData(Shopid);
    }

    private void setBanner() {

        if(arrBanner.isEmpty()){
            viewPager.setVisibility(View.GONE);
        } else {
             viewPager.setVisibility(View.VISIBLE);

            pagerAdapter = new BannerPageFragmentAdapter(getSupportFragmentManager());
            viewPager.setAdapter(pagerAdapter);
            indicatorBannerImages.setViewPager(viewPager);
            setIndicatorColors();
            viewPager.setCurrentItem(0);
        }

        // Post banners
        if(null != arrPostBanner && arrPostBanner.isEmpty()){
            tvPosts.setVisibility(View.GONE);
            viewPager_banners.setVisibility(View.GONE);
        } else {
            tvPosts.setVisibility(View.VISIBLE);
            viewPager_banners.setVisibility(View.VISIBLE);

            postPagerAdapter = new PostBannerPageFragmentAdapter(getSupportFragmentManager());
            viewPager_banners.setAdapter(postPagerAdapter);
            indicatorGallary.setViewPager(viewPager_banners);
            setIndicatorColorsForPostBanners();

            viewPager_banners.setCurrentItem(0);
        }

    }

    private void setIndicatorColors(){
        final float density = getResources().getDisplayMetrics().density;
        indicatorBannerImages.setBackgroundColor(0x000000);
        indicatorBannerImages.setRadius(5 * density);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            indicatorBannerImages.setStrokeColor(ContextCompat.getColor(self, R.color.gray_light));
            indicatorBannerImages.setPageColor(ContextCompat.getColor(self, R.color.gray));
            indicatorBannerImages.setFillColor(ContextCompat.getColor(self, R.color.blue_search));

        } else {
            indicatorBannerImages.setStrokeColor(self.getResources().getColor(R.color.gray_light));
            indicatorBannerImages.setPageColor(self.getResources().getColor(R.color.gray));
            indicatorBannerImages.setFillColor(self.getResources().getColor(R.color.blue_search));

        }

     //   indicatorBannerImages.setStrokeWidth(1 * density);
        indicatorBannerImages.setSnap(true);

    }

    private void setIndicatorColorsForPostBanners(){
        final float density = getResources().getDisplayMetrics().density;

        indicatorGallary.setBackgroundColor(0x000000);
        indicatorGallary.setRadius(5 * density);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            indicatorGallary.setStrokeColor(ContextCompat.getColor(self, R.color.gray_light));
            indicatorGallary.setPageColor(ContextCompat.getColor(self, R.color.gray));
            indicatorGallary.setFillColor(ContextCompat.getColor(self, R.color.blue_search));

        } else {
            indicatorGallary.setStrokeColor(self.getResources().getColor(R.color.gray_light));
            indicatorGallary.setPageColor(self.getResources().getColor(R.color.gray));
            indicatorGallary.setFillColor(self.getResources().getColor(R.color.blue_search));


        }

      //  indicatorGallary.setStrokeWidth(1 * density);
        indicatorGallary.setSnap(true);

    }

    private ShopDetailResponse shopDetailResponse;
    private void initData(int Shopid) {
        ModelManager.getShopById(self, Shopid, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        String json = (String) object;
                        shop = ParserUtility.parseShop(json);

                        shopDetailResponse = new Gson().fromJson(json, ShopDetailResponse.class);

                        SimilarShopAdapterNew shopAdapterNew =
                                new SimilarShopAdapterNew(recyclerView, shopDetailResponse.data.similarProducts, ShopDetailsNew.this);
                        recyclerView.setAdapter(shopAdapterNew);

                        if (null != shop) {
                            showShopDetails(shop);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.wtf("error: ","_ " + error.getMessage());
                   }
                });

    }

    private void showShopDetails(Shop shop) {
        arrBanner = shop.getArrBanner();
        arrPostBanner = shopDetailResponse.data.shopPosts;

        setBanner();

        if (GlobalValue.myAccount != null) {
            rlSaveShop.setVisibility(View.VISIBLE);
            if (shop.isFavourite()) ivFavorite.setImageResource(R.drawable.ico_save_shop);
            else ivFavorite.setImageResource(R.drawable.ico_unsave_shop);
        } else {
            rlSaveShop.setVisibility(View.GONE);
        }

        tvNameShop.setText(shop.getShopName());
        tvAddress.setText(shop.getAddress());
      //  tvPostDetails.setText(shop.get());

        Glide.with(ShopDetailsNew.this).load(shop.getImage()).into(imgShop);
        tvDescription.setText(shop.getDescription());
        tvPhone.setText(shop.getPhone());

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                tvTime.setText(shop.getArrOpenHour().get(6).getOpen_AM1() + "-" + shop.getArrOpenHour().get(6).getClose_PM2());
                break;
            case Calendar.MONDAY:
                tvTime.setText(shop.getArrOpenHour().get(0).getOpen_AM1() + "-" + shop.getArrOpenHour().get(0).getClose_PM2());
                break;
            case Calendar.TUESDAY:
                tvTime.setText(shop.getArrOpenHour().get(1).getOpen_AM1() + "-" + shop.getArrOpenHour().get(1).getClose_PM2());
                break;
            case Calendar.WEDNESDAY:
                tvTime.setText(shop.getArrOpenHour().get(2).getOpen_AM1() + "-" + shop.getArrOpenHour().get(2).getClose_PM2());
                break;
            case Calendar.THURSDAY:
                tvTime.setText(shop.getArrOpenHour().get(3).getOpen_AM1() + "-" + shop.getArrOpenHour().get(3).getClose_PM2());
                break;
            case Calendar.FRIDAY:
                tvTime.setText(shop.getArrOpenHour().get(4).getOpen_AM1() + "-" + shop.getArrOpenHour().get(4).getClose_PM2());
                break;
            case Calendar.SATURDAY:
                tvTime.setText(shop.getArrOpenHour().get(5).getOpen_AM1() + "-" + shop.getArrOpenHour().get(5).getClose_PM2());
                break;
        }
        totalComent.setText(shop.getRateNumber() + " " + getString(R.string.review));
        rtbRating.setRating(Float.parseFloat(Math.floor(shop.getRateValue() / 2) + ""));

    }

    private void initView() {
        ivArr = findViewById(R.id.ivArr);
        llArrowDown = findViewById(R.id.drop_down_ly);
        rlSubOptions = findViewById(R.id.rel_drop_options);

        rlSaveShop = findViewById(R.id.save_cart_lay);
        ivFavorite = findViewById(R.id.ivFavorite);
        indicatorBannerImages = findViewById(R.id.indicatorBannerImages);
        indicatorGallary = findViewById(R.id.indicater_img_gallary);
        totalComent = findViewById(R.id.tvTotalComment);
        rtbRating = findViewById(R.id.rtbRating);
        tvNameShop = findViewById(R.id.tv_shopName);
        tvViewAll = findViewById(R.id.similer_shop_view_all);
        tvPostDetails = findViewById(R.id.post_details);
        tvAddress = findViewById(R.id.tvAddress);
        tvPosts = findViewById(R.id.post);

        btnBack = findViewById(R.id.btnBack);
        homeIcon = findViewById(R.id.home_icon);
        imgShop = findViewById(R.id.imgShop);
        btnMap = findViewById(R.id.direction_ly);
        btnProduct = findViewById(R.id.btnProduct);
        btnHotline = findViewById(R.id.call_ly);
        tvPhone = findViewById(R.id.lblPhone);
        tvTime = findViewById(R.id.tvTime);
        tvDescription = findViewById(R.id.tvDescription);
        tvPromotion = findViewById(R.id.tvPromotions);
        recyclerView = findViewById(R.id.rclViewShop);
        viewPager = findViewById(R.id.viewPager);
        viewPager_banners = findViewById(R.id.viewPager_banners);
        ivTwitter = findViewById(R.id.iv_twitter);

        ivChat = findViewById(R.id.iv_chat);
        rlChat = findViewById(R.id.chat_ly);

        ivWebsite = findViewById(R.id.rl_web);
        ivInstagram = findViewById(R.id.rl_insta);
        rlClock = findViewById(R.id.rl_clock);
        ivFacebook = findViewById(R.id.rl_fb);

        // setup initial data
        recyclerView.setHasFixedSize(true);



    }

    private void initControls() {
        btnBack.setOnClickListener(v -> onBackPressed());
        tvViewAll.setOnClickListener(v -> onBackPressed());
        homeIcon.setOnClickListener(v -> { startActivity(new Intent(this, MainTabActivity.class));  });
        btnMap.setOnClickListener(v -> gotoMapDetailActivity(shop));
        btnHotline.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + shop.getPhone()));
            startActivity(callIntent);
        });
        btnProduct.setOnClickListener(v -> gotoMenuActivity(shop));

        ivChat.setOnClickListener(v -> {

            if (GlobalValue.myAccount != null) {
                if(shopDetailResponse.data.chatStatus.equalsIgnoreCase("1")) {

                    Intent intent = new Intent(self, ChatDetailActivity.class);
                    intent.putExtra("idAgent", shop.getShopOwnerId());
                    intent.putExtra("title", shop.getShopName());
                    intent.putExtra("image", shop.getShopOwnerImage());
                    intent.putExtra(Constant.SHOP_ID, shop.getShopId() + "");
                    intent.putExtra(Constant.SHOP_NAME, shop.getShopName());
                    intent.putExtra(Constant.COUNT_SHOP, shop.getCountShop() + "");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);

                } else {
                    showToastMessage(getString(R.string.this_shop_turned_off_chat_option));
                }


            } else {
                showDialogLogin();
            }



        });

        /*doannd website code*/
        ivWebsite.setOnClickListener(view -> {
            if (shop.getWebsite().trim().length() == 0) {
                showToastMessage(R.string.shop_cannot_website_address);
                return;
            }
            try {
                String url = shop.getWebsite();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ivInstagram.setOnClickListener(v -> {
            if (shop.getShopInstagram().trim().length() != 0) {
                try {
                    String url = shop.getShopInstagram();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ivTwitter.setOnClickListener(v -> {
            if (shop.getShopTwitter().trim().length() != 0) {
                try {
                    String url = shop.getShopTwitter();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });
        ivFacebook.setOnClickListener(v -> {
            if (shop.getShopFacebook().trim().length() != 0) {
                try {
                    String url = shop.getShopFacebook();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rlClock.setOnClickListener(v -> {
            dialogOpeningClosingTime(shop);
        });

        rlSaveShop.setOnClickListener(v -> {
            onClickFavourite();
        });
        llArrowDown.setOnClickListener(v-> {
            if(rlSubOptions.isShown()){
                ivArr.setRotation(0);
                rlSubOptions.setVisibility(View.GONE);
            }
            else{
                ivArr.setRotation(180);
                rlSubOptions.setVisibility(View.VISIBLE);
            }
        });


    }

    private void onClickFavourite() {
        if (GlobalValue.myAccount != null) {
            ModelManager.updateFavouriteShop(self, GlobalValue.myAccount.getId(), Shopid + "", !shop.isFavourite(), true, new ModelManagerListener() {
                @Override
                public void onError(VolleyError error) {
                    ErrorNetworkHandler.processError(error);
                }

                @Override
                public void onSuccess(Object object) {
                    shop.setFavourite(!shop.isFavourite());
                    //update favourite button
                    if (shop.isFavourite())
                        ivFavorite.setImageResource(R.drawable.ico_save_shop);
                    else
                        ivFavorite.setImageResource(R.drawable.ico_unsave_shop);
                }
            });
        }
    }


    private void dialogOpeningClosingTime(Shop innerShop) {
      Dialog oepnCLoseDialog = new Dialog(this);
        oepnCLoseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        oepnCLoseDialog.setContentView(R.layout.dialog_opeing_closing_time);
        oepnCLoseDialog.setCancelable(true);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        self.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(oepnCLoseDialog.getWindow().getAttributes());
        lp.width = 6 * (displaymetrics.widthPixels / 7);
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        oepnCLoseDialog.getWindow().setAttributes(lp);
        oepnCLoseDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView subHeading = (TextView) oepnCLoseDialog.findViewById(R.id.tv_subhead_name);
        TextView subHeadingTime = (TextView) oepnCLoseDialog.findViewById(R.id.tv_subhead_time);

        if(null != innerShop){
            StringBuilder mRawName = new StringBuilder();
            StringBuilder mRawTime = new StringBuilder();
            for(int io=0; io < innerShop.getArrOpenHour().size();io++) {
                String inDay = innerShop.getArrOpenHour().get(io).getDateName();
                String inRawOpen = innerShop.getArrOpenHour().get(io).getOpen_AM1();
                String inRawClose = innerShop.getArrOpenHour().get(io).getClose_PM2();

                String combine = "<b>" + inDay + "</b><br><br>";
                String combineTime = inRawOpen + "  -  " + inRawClose + "<br><br>";

                mRawName.append(combine);
                mRawTime.append(combineTime);
            }

         subHeading.setText(Html.fromHtml(mRawName.toString()));
         subHeadingTime.setText(Html.fromHtml(mRawTime.toString()));

        }

        oepnCLoseDialog.show();
    }



    private void gotoMenuActivity(Shop shop) {
        Bundle b = new Bundle();
        b.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
        b.putString(GlobalValue.KEY_SHOP_NAME, shop.getShopName());
        gotoActivity(self, ListCategoryActivity.class, b);
    }

    private void gotoMapDetailActivity(Shop shop) {
        Bundle b = new Bundle();
        b.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
        gotoActivity(self, MapDetailActivity.class, b);
    }

    class BannerPageFragmentAdapter extends FragmentPagerAdapter {

        public BannerPageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Banner banner = arrBanner.get(position);
            return BannerFragment.InStances(banner.getImage());

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return arrBanner.get(position).getName();
        }

        @Override
        public int getCount() {
            return arrBanner.size();
        }
    }


    class PostBannerPageFragmentAdapter extends FragmentPagerAdapter {

        public PostBannerPageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            ShopDetailResponse.ShopPostBanner banner = arrPostBanner.get(position);
            return PostBannerFragment.InStances(banner.image, banner.description);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return arrPostBanner.get(position).description;
        }

        @Override
        public int getCount() {
            return arrPostBanner.size();
        }
    }



}

package com.libyasolutions.libyamarketplace.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.LoginActivity;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.fragment.BannerFragment;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Banner;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Calendar;

public class ShopDetailsNew extends BaseActivity {
    private ImageView btnBack, imgShop;
    private LinearLayout btnMap, btnProduct, btnHotline;
    private TextView tvPhone, tvTime, tvDescription, tvPromotion, tvNameShop, totalComent;
    private RecyclerView recyclerView;
    private int Shopid;
    private Shop shop;
    private RatingBar rtbRating;

    private ViewPager viewPager;
    FragmentPagerAdapter pagerAdapter;
    private ArrayList<Banner> arrBanner;
    private CirclePageIndicator indicatorBannerImages;
    private ImageView ivChat, ivWebsite, ivInstagram, ivTwitter, ivFacebook;
    private Dialog loginDialog;


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
        pagerAdapter = new BannerPageFragmentAdapter(
                getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        indicatorBannerImages.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        indicatorBannerImages.setBackgroundColor(0x000000);
        indicatorBannerImages.setRadius(5 * density);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            indicatorBannerImages.setStrokeColor(ContextCompat.getColor(self, R.color.gray_light));
            indicatorBannerImages.setPageColor(ContextCompat.getColor(self, R.color.gray));
            indicatorBannerImages.setFillColor(ContextCompat.getColor(self, R.color.red));
        } else {
            indicatorBannerImages.setStrokeColor(self.getResources().getColor(R.color.gray_light));
            indicatorBannerImages.setPageColor(self.getResources().getColor(R.color.gray));
            indicatorBannerImages.setFillColor(self.getResources().getColor(R.color.red));
        }
        indicatorBannerImages.setStrokeWidth(1 * density);
        indicatorBannerImages.setSnap(true);
        viewPager.setCurrentItem(0);
    }

    private void initData(int Shopid) {
        ModelManager.getShopById(self, Shopid, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {

                        String json = (String) object;
                        shop = ParserUtility.parseShop(json);
                        if (shop != null) {
                            showShopDetails(shop);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

    }

    private void showShopDetails(Shop shop) {
        arrBanner = shop.getArrBanner();
        setBanner();

        tvNameShop.setText(shop.getShopName());
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
        totalComent.setText(String.valueOf(shop.getRateNumber()) + " " + getString(R.string.review));
        rtbRating.setRating(Float.parseFloat(Math
                .floor(shop.getRateValue() / 2) + ""));
    }

    private void initView() {
        indicatorBannerImages = findViewById(R.id.indicatorBannerImages);
        totalComent = findViewById(R.id.tvTotalComment);
        rtbRating = findViewById(R.id.rtbRating);
        tvNameShop = findViewById(R.id.tvNameShop);
        tvNameShop.setSelected(true);

        btnBack = findViewById(R.id.btnBack);
        imgShop = findViewById(R.id.imgShop);
        btnMap = findViewById(R.id.btnMap);
        btnProduct = findViewById(R.id.btnProduct);
        btnHotline = findViewById(R.id.btnHotLine);
        tvPhone = findViewById(R.id.lblPhone);
        tvTime = findViewById(R.id.tvTime);
        tvDescription = findViewById(R.id.tvDescription);
        tvPromotion = findViewById(R.id.tvPromotions);
        recyclerView = findViewById(R.id.rclViewShop);
        viewPager = findViewById(R.id.viewPager);

        ivChat = findViewById(R.id.iv_chat);
        ivWebsite = findViewById(R.id.iv_website);
        ivInstagram = findViewById(R.id.iv_instagram);
        ivTwitter = findViewById(R.id.iv_twitter);
        ivFacebook = findViewById(R.id.iv_facebook);

    }

    private void initControls() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMapDetailActivity(shop);
            }
        });
        btnHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + shop.getPhone()));
                startActivity(callIntent);
            }
        });
        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMenuActivity(shop);
            }
        });
        ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalValue.myAccount != null) {
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
                    showDialogLogin();
                }
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

        ivInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        ivTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
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

    private void showDialogLogin() {
        loginDialog = new Dialog(this);
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loginDialog.setContentView(R.layout.dialog_confirm);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        self.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(loginDialog.getWindow().getAttributes());
        lp.width = 6 * (displaymetrics.widthPixels / 7);
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        loginDialog.getWindow().setAttributes(lp);
        loginDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvTitle = loginDialog.findViewById(R.id.tvTitle);
        TextView tvContent = loginDialog.findViewById(R.id.tvContent);
        TextView tvCancel = loginDialog.findViewById(R.id.tvCancel);
        TextView tvConfirm = loginDialog.findViewById(R.id.tvConfirm);

        tvContent.setText(R.string.login_user_function);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalValue.myAccount != null)
                    GlobalValue.myAccount = null;
                new MySharedPreferences(getApplicationContext()).clearAccount();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                loginDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });


        loginDialog.show();
    }

}

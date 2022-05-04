package com.libyasolutions.libyamarketplace.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.fragment.BannerFragment;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Banner;
import com.libyasolutions.libyamarketplace.object.OpenHour;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.Utils;
import com.libyasolutions.libyamarketplace.widget.CirclePageIndicator;

import java.util.ArrayList;

public class ShopDetailActivity extends BaseActivity implements OnClickListener {

    private ViewPager viewPager;
    private ArrayList<Banner> arrBanner;
    private ArrayList<OpenHour> arrOpenHour;
    FragmentPagerAdapter pagerAdapter;
    private RelativeLayout btnProduct, btnHotLine, btnMap;
    private Shop shop;
    private TextView lblTitle, lblShopName, lblOpenHourStatus, lblCategoryName, lblReviewNumber, lblProductQuantity;
    private TextView lblWebsite, lblPhoneNumber, lblAddress, lblFacebook, lblTwitter, lblEmail, lblChatLive, lblDescription;
    private LinearLayout layoutWebsite, layoutPhone, layoutAddress, layoutFacebook, layoutTwitter, layoutEmail, layoutChatLive;
    private TextView lblMonOpenTime, lblTueOpenTime, lblWedOpenTime, lblThuOpenTime, lblFriOpenTime, lblSatOpenTime, lblSunOpenTime;
    private ImageView btnBack, imgVerified, imgFeatured;
    private Button btnFavourite, btnShowComments;
    private int shopId = -1;
    public static BaseActivity self;
    private RatingBar rtbRating;
    private CirclePageIndicator indicatorBannerImages;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_shop_detail);
        initUI();
        initControl();
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (shopId != -1) {
            getShopDetailInfo(shopId);
        }
    }

    private void initUI() {
        //header :
        btnBack = (ImageView) findViewById(R.id.btnBack);
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        btnFavourite = (Button) findViewById(R.id.btnFavourite);
        btnShowComments = (Button) findViewById(R.id.btnShowComments);
        //banner
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicatorBannerImages = (CirclePageIndicator) findViewById(R.id.indicatorBannerImages);
        lblOpenHourStatus = (TextView) findViewById(R.id.lblOpenHourStatus);
        btnProduct = (RelativeLayout) findViewById(R.id.btnProduct);
        lblProductQuantity = (TextView) findViewById(R.id.lblProductQuantity);
        btnMap = (RelativeLayout) findViewById(R.id.btnMap);
        btnHotLine = (RelativeLayout) findViewById(R.id.btnHotLine);
        //shop info
        lblShopName = (TextView) findViewById(R.id.lblShopName);
        lblCategoryName = (TextView) findViewById(R.id.lblCategoryName);
        rtbRating = (RatingBar) findViewById(R.id.rtbRating);
        lblReviewNumber = (TextView) findViewById(R.id.lblReviewNumber);
        imgVerified = (ImageView) findViewById(R.id.imgVerified);
        imgFeatured = (ImageView) findViewById(R.id.imgFeatured);
        lblWebsite = (TextView) findViewById(R.id.lblWebsite);
        lblPhoneNumber = (TextView) findViewById(R.id.lblPhone);
        lblAddress = (TextView) findViewById(R.id.lblAddress);
        lblFacebook = (TextView) findViewById(R.id.lblFacebook);
        lblTwitter = (TextView) findViewById(R.id.lblTwitter);
        lblEmail = (TextView) findViewById(R.id.lblEmail);
        lblChatLive = (TextView) findViewById(R.id.lblLiveChat);
        lblDescription = (TextView) findViewById(R.id.lblDescription);
        //layout
        layoutWebsite = (LinearLayout) findViewById(R.id.layoutWebsite);
        layoutPhone = (LinearLayout) findViewById(R.id.layoutPhone);
        layoutAddress = (LinearLayout) findViewById(R.id.layoutAddress);
        layoutFacebook = (LinearLayout) findViewById(R.id.layoutFacebook);
        layoutTwitter = (LinearLayout) findViewById(R.id.layoutTwitter);
        layoutEmail = (LinearLayout) findViewById(R.id.layoutEmail);
        layoutChatLive = (LinearLayout) findViewById(R.id.layoutLiveChat);

        //open time
        lblMonOpenTime = (TextView) findViewById(R.id.lblMonOpenTime);
        lblTueOpenTime = (TextView) findViewById(R.id.lblTueOpenTime);
        lblWedOpenTime = (TextView) findViewById(R.id.lblWedOpenTime);
        lblThuOpenTime = (TextView) findViewById(R.id.lblThuOpenTime);
        lblFriOpenTime = (TextView) findViewById(R.id.lblFriOpenTime);
        lblSatOpenTime = (TextView) findViewById(R.id.lblSatOpenTime);
        lblSunOpenTime = (TextView) findViewById(R.id.lblSunOpenTime);
    }

    private void initControl() {
        btnProduct.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnHotLine.setOnClickListener(this);
        btnFavourite.setOnClickListener(this);
        btnShowComments.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        lblWebsite.setOnClickListener(this);
        lblPhoneNumber.setOnClickListener(this);
        lblAddress.setOnClickListener(this);
        lblFacebook.setOnClickListener(this);
        lblTwitter.setOnClickListener(this);
        lblEmail.setOnClickListener(this);
        lblChatLive.setOnClickListener(this);
    }

    private void getShopDetailInfo(int shopID) {
        ModelManager.getShopById(self, shopID, true,
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
        //show banner
        arrBanner = shop.getArrBanner();
        setBanner();
        //show open-close time
        showOpenHours(shop.getArrOpenHour());
        //show shop info
        lblTitle.setText(shop.getShopName());
        lblShopName.setText(shop.getShopName());
        //button favourite
        if (GlobalValue.myAccount != null) {
            btnFavourite.setVisibility(View.VISIBLE);
            if (shop.isFavourite())
                btnFavourite.setBackgroundResource(R.drawable.ic_favourite);
            else
                btnFavourite.setBackgroundResource(R.drawable.ic_un_favourite);
        } else {
            btnFavourite.setVisibility(View.GONE);
        }

        if (shop.isOpen())
            lblOpenHourStatus.setVisibility(View.GONE);
        else
            lblOpenHourStatus.setVisibility(View.VISIBLE);

        lblProductQuantity.setText(shop.getNumberItems() + "");
        rtbRating.setRating(Float.parseFloat(Math
                .floor(shop.getRateValue() / 2) + ""));
        lblReviewNumber.setText("(" + shop.getRateNumber() + " "+getString(R.string.reviews)+")");
        if (shop.isFeatured())
            imgFeatured.setVisibility(View.VISIBLE);
        else
            imgFeatured.setVisibility(View.GONE);

        if (shop.isVerified())
            imgVerified.setVisibility(View.VISIBLE);
        else
            imgVerified.setVisibility(View.GONE);
        //info
        lblCategoryName.setText(shop.getCategory());
        displaySocialInformation(shop);

    }

    private void displaySocialInformation(Shop shop) {
        if (!shop.getWebsite().isEmpty()) {
            layoutWebsite.setVisibility(View.VISIBLE);
            lblWebsite.setText(Html.fromHtml("<u>" + shop.getWebsite() + "</u>"));
        }
        if (!shop.getPhone().isEmpty()) {
            layoutPhone.setVisibility(View.VISIBLE);
            lblPhoneNumber.setText(Html.fromHtml("<u>" + shop.getPhone() + "</u>"));
        }
        if (!shop.getAddress().isEmpty()) {
            layoutAddress.setVisibility(View.VISIBLE);
            lblAddress.setText(Html.fromHtml("<u>" + shop.getAddress() + "</u>"));

        }
        if (!shop.getFacebook().isEmpty()) {
            layoutFacebook.setVisibility(View.VISIBLE);
            lblFacebook.setText(Html.fromHtml("<u>" + shop.getFacebook() + "</u>"));
        }
        if (!shop.getTwitter().isEmpty()) {
            layoutTwitter.setVisibility(View.VISIBLE);
            lblTwitter.setText(Html.fromHtml("<u>" + shop.getTwitter() + "</u>"));
        }
        if (!shop.getEmail().isEmpty()) {
            layoutEmail.setVisibility(View.VISIBLE);
            lblEmail.setText(Html.fromHtml("<u>" + shop.getEmail() + "</u>"));
        }
        if (!shop.getLive_chat().isEmpty()) {
            layoutChatLive.setVisibility(View.VISIBLE);
            lblChatLive.setText(Html.fromHtml("<u>" + shop.getLive_chat() + "</u>"));
        }
        if (!shop.getDescription().isEmpty()) {
            lblDescription.setVisibility(View.VISIBLE);
            lblDescription.setText(Html.fromHtml(shop.getDescription()));
        }
    }

//    private void getOpenHours(int shopId) {
//        ModelManager.getOpenHourByShop(self, shopId, false,
//                new ModelManagerListener() {
//
//                    @Override
//                    public void onSuccess(Object object) {
//                        // TODO Auto-generated method stub
//                        String json = (String) object;
//                        arrOpenHour = ParserUtility.parseListOpenHour(json);
//                        showOpenHours(arrOpenHour);
//                    }
//
//                    @Override
//                    public void onError(VolleyError error) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//
//    }

    private void showOpenHours(ArrayList<OpenHour> arr) {
        for (OpenHour openHour : arr) {
            String closePM = "";
            switch (openHour.getDateId()) {
                case 1:
                    lblSunOpenTime.setText(Utils.getTimeAMPMFromString(openHour.getOpenAM()) + " - " + Utils.getTimeAMPMFromString(openHour.getClosePM()));
                    break;
                case 2:
                    lblMonOpenTime.setText(Utils.getTimeAMPMFromString(openHour.getOpenAM()) + " - " + Utils.getTimeAMPMFromString(openHour.getClosePM()));
                    break;
                case 3:
                    lblTueOpenTime.setText(Utils.getTimeAMPMFromString(openHour.getOpenAM()) + " - " + Utils.getTimeAMPMFromString(openHour.getClosePM()));
                    break;
                case 4:
                    lblWedOpenTime.setText(Utils.getTimeAMPMFromString(openHour.getOpenAM()) + " - " + Utils.getTimeAMPMFromString(openHour.getClosePM()));
                    break;
                case 5:
                    lblThuOpenTime.setText(Utils.getTimeAMPMFromString(openHour.getOpenAM()) + " - " + Utils.getTimeAMPMFromString(openHour.getClosePM()));
                case 6:
                    lblFriOpenTime.setText(Utils.getTimeAMPMFromString(openHour.getOpenAM()) + " - " + Utils.getTimeAMPMFromString(openHour.getClosePM()));
                    break;
                case 7:
                    lblSatOpenTime.setText(Utils.getTimeAMPMFromString(openHour.getOpenAM()) + " - " + Utils.getTimeAMPMFromString(openHour.getClosePM()));
                    break;

                default:
                    break;
            }
        }
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(GlobalValue.KEY_SHOP_ID)) {
                shopId = b.getInt(GlobalValue.KEY_SHOP_ID);
            }
        }
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
            indicatorBannerImages.setPageColor(ContextCompat.getColor(self, R.color.gray_home_bottom));
            indicatorBannerImages.setFillColor(ContextCompat.getColor(self, R.color.main_button_color));
        } else {
            indicatorBannerImages.setStrokeColor(self.getResources().getColor(R.color.gray_light));
            indicatorBannerImages.setPageColor(self.getResources().getColor(R.color.gray_home_bottom));
            indicatorBannerImages.setFillColor(self.getResources().getColor(R.color.main_button_color));
        }
        indicatorBannerImages.setStrokeWidth(1 * density);
        indicatorBannerImages.setSnap(true);
        viewPager.setCurrentItem(0);
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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnMap || v == lblAddress) {
            gotoMapDetailActivity(shop);
        } else if (v == btnProduct) {
            gotoMenuActivity(shop);
        } else if (v == btnHotLine || v == lblPhoneNumber) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + shop.getPhone()));
            startActivity(callIntent);
        } else if (v == btnBack) {
            onBackPressed();
        } else if (v == btnShowComments) {
            openShopsCommentPage();
        } else if (v == btnFavourite) {
            onClickFavourite();
        } else if (v == lblWebsite) {
            openWeb(shop.getWebsite());
        } else if (v == lblFacebook) {
            openWeb(shop.getFacebook());
        } else if (v == lblTwitter) {
            openWeb(shop.getTwitter());
        } else if (v == lblChatLive) {
            openWeb(shop.getLive_chat());
        } else if (v == lblEmail) {
            sendEmail(shop.getEmail());
        }
    }

    private void onClickFavourite() {
        if (GlobalValue.myAccount != null) {
            ModelManager.updateFavouriteShop(self, GlobalValue.myAccount.getId(), shopId + "", !shop.isFavourite(), true, new ModelManagerListener() {
                @Override
                public void onError(VolleyError error) {
                    ErrorNetworkHandler.processError(error);
                }

                @Override
                public void onSuccess(Object object) {
                    shop.setFavourite(!shop.isFavourite());
                    //update favourite button
                    if (shop.isFavourite())
                        btnFavourite.setBackgroundResource(R.drawable.ic_favourite);
                    else
                        btnFavourite.setBackgroundResource(R.drawable.ic_un_favourite);
                }
            });
        }
    }

    private void openShopsCommentPage() {
        if (shopId != -1) {
            Bundle bundle = new Bundle();
            bundle.putInt(GlobalValue.KEY_SHOP_ID, shopId);
            gotoActivity(self, ShopsCommentActivity.class, bundle);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        self = null;
    }

    private void sendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void openWeb(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}

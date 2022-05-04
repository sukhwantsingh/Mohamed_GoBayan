package com.libyasolutions.libyamarketplace.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.OpenHour;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.CustomToast;
import com.libyasolutions.libyamarketplace.util.ImageUtil;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.Utils;
import com.libyasolutions.libyamarketplace.widget.CirclePageIndicator;

import java.util.ArrayList;

public class ProductDetailActivity extends BaseActivity implements OnClickListener {

    private ViewPager viewPager;
    private ArrayList<Banner> arrGalleries;
    FragmentPagerAdapter pagerAdapter;
    private RelativeLayout btnAddToCart, btnHotLine, btnMap;
    private Menu product;
    private Shop shop;
    private TextView lblTitle, lblProductName, lblPrice, lblCategoryName, lblReviewNumber, lblShopName;
    private TextView lblDescription;
    private TextView lblMonOpenTime, lblTueOpenTime, lblWedOpenTime, lblThuOpenTime, lblFriOpenTime, lblSatOpenTime, lblSunOpenTime;
    private ImageView btnBack, imgShop;
    private Button btnFavourite, btnShowComments;
    public static BaseActivity self;
    private RatingBar rtbRating;
    private CirclePageIndicator indicatorBannerImages;
    private String productId = "0";
    private boolean isFastSearch = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        self = this;
        NetworkUtil.enableStrictMode();
        setContentView(R.layout.activity_product_details);
        initUI();
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!productId.equalsIgnoreCase("0")) {
            getProductDetails(productId);
        }
    }

    private void initUI() {
        //header :
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        btnFavourite = (Button) findViewById(R.id.btnFavourite);
        btnShowComments = (Button) findViewById(R.id.btnShowComments);
        //banner
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicatorBannerImages = (CirclePageIndicator) findViewById(R.id.indicatorBannerImages);
        btnAddToCart = (RelativeLayout) findViewById(R.id.btnAddToCart);
        btnMap = (RelativeLayout) findViewById(R.id.btnMap);
        btnHotLine = (RelativeLayout) findViewById(R.id.btnHotLine);
        //shop info
        lblProductName = (TextView) findViewById(R.id.lblProductName);
        lblCategoryName = (TextView) findViewById(R.id.lblCategoryName);
        lblPrice = (TextView) findViewById(R.id.lblPrice);
        rtbRating = (RatingBar) findViewById(R.id.rtbRating);
        lblReviewNumber = (TextView) findViewById(R.id.lblReviewNumber);
        imgShop = (ImageView) findViewById(R.id.imgShop);
        lblShopName = (TextView) findViewById(R.id.lblShopName);
        lblDescription = (TextView) findViewById(R.id.lblDescription);
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
        btnAddToCart.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnHotLine.setOnClickListener(this);
        btnFavourite.setOnClickListener(this);
        btnShowComments.setOnClickListener(this);
        lblShopName.setOnClickListener(this);
    }

    private void getProductDetails(final String productId) {
        ModelManager.getFoodById(self, productId, false,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {

                        String json = (String) object;
                        product = ParserUtility.parseFood(json);
                        if (product != null) {
                            showProductDetails(product);
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showProductDetails(Menu product) {
        //set action for buttons
        initControl();
        //show banner
        arrGalleries = product.getArrGalleries();
        setBanner();
        //show shop info
        lblTitle.setText(product.getName());
        lblProductName.setText(product.getName());
        lblProductName.setSelected(true);
        lblPrice.setText(product.getPrice() + "");
        //button favourite
        if (GlobalValue.myAccount != null) {
            btnFavourite.setVisibility(View.VISIBLE);
            if (product.isFavourite())
                btnFavourite.setBackgroundResource(R.drawable.ic_favourite);
            else
                btnFavourite.setBackgroundResource(R.drawable.ic_un_favourite);
        } else {
            btnFavourite.setVisibility(View.GONE);
        }

        rtbRating.setRating(Float.parseFloat(Math
                .floor(product.getRateValue() / 2) + ""));
        lblReviewNumber.setText("(" + product.getRateNumber() + " "+getString(R.string.reviews)+")");
        lblCategoryName.setText(product.getCategory());
        lblDescription.setText(Html.fromHtml(product.getDescription()));
        lblShopName.setText(Html.fromHtml("<u>by " + product.getShop().getShopName() + "</u>"));
        imgShop.setImageBitmap(ImageUtil.createBitmapFromUrl(product.getShop().getThumbnail()));
        //show open hours
        showOpenHours(product.getShop().getArrOpenHour());
    }

    private void showOpenHours(ArrayList<OpenHour> arr) {
        for (OpenHour openHour : arr) {
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
            if (b.containsKey(GlobalValue.KEY_FOOD_ID)) {
                productId = b.getString(GlobalValue.KEY_FOOD_ID);
            }

            if (b.containsKey(GlobalValue.KEY_NAVIGATE_TYPE)) {
                isFastSearch = true;
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
//        indicatorBannerImages.setPageColor(ContextCompat.getColor(self, R.color.gray_home_bottom));
        indicatorBannerImages.setPageColor(getResources().getColor(R.color.gray_home_bottom));
//        indicatorBannerImages.setFillColor(ContextCompat.getColor(self, R.color.main_button_color));
        indicatorBannerImages.setFillColor(getResources().getColor(R.color.main_button_color));
        indicatorBannerImages.setStrokeWidth(1 * density);
//        indicatorBannerImages.setStrokeColor(ContextCompat.getColor(self, R.color.gray_light));
        indicatorBannerImages.setStrokeColor(getResources().getColor(R.color.gray_light));
        indicatorBannerImages.setSnap(true);
        viewPager.setCurrentItem(0);
    }

    class BannerPageFragmentAdapter extends FragmentPagerAdapter {

        public BannerPageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Banner banner = arrGalleries.get(position);
            return BannerFragment.InStances(banner.getImage());

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return arrGalleries.get(position).getName();
        }

        @Override
        public int getCount() {
            return arrGalleries.size();
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
        if (v == btnMap) {
            gotoMapDetailActivity(product.getShop());
        } else if (v == btnAddToCart) {
            addToCart(product);
        } else if (v == btnHotLine) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + product.getShop().getPhone()));
            startActivity(callIntent);
        } else if (v == btnBack) {
            onBackPressed();
        } else if (v == btnShowComments) {
            openProductCommentPage();
        } else if (v == btnFavourite) {
            onClickFavourite();
        } else if (v == lblShopName) {
            gotoShopDetail(false);
        }
    }

    private void onClickFavourite() {
        if (GlobalValue.myAccount != null) {
            ModelManager.updateFavouriteProduct(self, GlobalValue.myAccount.getId(), productId, !product.isFavourite(), true, new ModelManagerListener() {
                @Override
                public void onError(VolleyError error) {
                    ErrorNetworkHandler.processError(error);
                }

                @Override
                public void onSuccess(Object object) {
                    product.setIsFavourite(!product.isFavourite());
                    //update favourite button
                    if (product.isFavourite())
                        btnFavourite.setBackgroundResource(R.drawable.ic_favourite);
                    else
                        btnFavourite.setBackgroundResource(R.drawable.ic_un_favourite);
                }
            });
        }
    }

    private void addToCart(Menu food) {
        int shopIndex = -1;
        if (GlobalValue.arrMyMenuShop == null) {
            GlobalValue.arrMyMenuShop = new ArrayList<Shop>();
        }

        if (product.getShop() != null) {
            for (int i = 0; i < GlobalValue.arrMyMenuShop.size(); i++) {
                Shop item = GlobalValue.arrMyMenuShop.get(i);
                if (item.getShopId() == product.getShop().getShopId()) {
                    shopIndex = i;
                    break;
                }
            }

            if (shopIndex != -1) {
                shop = GlobalValue.arrMyMenuShop.get(shopIndex);
                boolean isExistedMenu = false;
                for (Menu menu : shop.getArrOrderFoods()) {
                    if (menu.getId() == food.getId()) {
                        isExistedMenu = true;
                    }
                }

                if (isExistedMenu) {
                    CustomToast.showCustomAlert(this,
                            getString(R.string.item_is_existed_my_cart),
                            Toast.LENGTH_SHORT);
                } else {
                    food.setOrderNumber(1);
                    GlobalValue.arrMyMenuShop.get(shopIndex).addFoodOrder(food);
                    CustomToast.showCustomAlert(this,
                            getString(R.string.item_is_added_my_cart),
                            Toast.LENGTH_SHORT);

                    if (isFastSearch) {
                        onBackPressed();
                    } else {
                        gotoShopDetail(true);
                    }
                }

            } else {
                food.setOrderNumber(1);
                shop = food.getShop();
                shop.addFoodOrder(food);
                GlobalValue.arrMyMenuShop.add(shop);
                CustomToast.showCustomAlert(self,
                        getString(R.string.item_is_added_my_cart), Toast.LENGTH_SHORT);
                if (isFastSearch) {
                    onBackPressed();
                } else {
                    gotoShopDetail(true);
                }
            }
        } else {
            CustomToast.showCustomAlert(self, getString(R.string.have_error),
                    Toast.LENGTH_SHORT);
        }
    }

    private void gotoShopDetail(boolean isBack) {
        if (ListFoodActivity.self != null) {
            ListFoodActivity.self.finish();
        }
        if (ListCategoryActivity.self != null) {
            ListCategoryActivity.self.finish();
        }
        if (ListCategoryActivity.self != null) {
            ListCategoryActivity.self.finish();
        }

        if (ShopDetailActivity.self == null) {
            Bundle b = new Bundle();
            b.putInt(GlobalValue.KEY_SHOP_ID, product.getShop().getShopId());
            if (isBack)
                backActivity(ShopDetailActivity.class, b);
            else
                gotoActivity(self, ShopDetailActivity.class, b);
        } else {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }


    private void openProductCommentPage() {
        if (!productId.equalsIgnoreCase("0")) {
            Bundle bundle = new Bundle();
            bundle.putString(GlobalValue.KEY_FOOD_ID, productId);
            gotoActivity(self, ProductCommentActivity.class, bundle);
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

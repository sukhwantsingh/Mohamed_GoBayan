package com.libyasolutions.libyamarketplace.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.model.LatLng;
import com.libyasolutions.libyamarketplace.BaseActivityV2;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.BannerNewAdapter;
import com.libyasolutions.libyamarketplace.adapter.GalleryToAddAdapter;
import com.libyasolutions.libyamarketplace.adapter.ShopCategoryAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.dialog.CityIdDialog;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.network.ProgressDialog;
import com.libyasolutions.libyamarketplace.object.Banner;
import com.libyasolutions.libyamarketplace.object.Category;
import com.libyasolutions.libyamarketplace.object.OpenHour;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.ImageUtil;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.PermissionUtil;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator2;

public class AddNewShopActivityV2 extends BaseActivityV2 {

    private static final int REQUEST_CODE_GET_LOCATION = 1001;
    public static final int REQUEST_CODE_GET_OPEN_HOUR = 1234;

    private static final int REQUEST_IMAGE_GALLERY_IMAGE_ONE = 0;
    private static final int REQUEST_IMAGE_CAPTURE_IMAGE_ONE = 1;

    private static final int CHOOSE_MORE_IMAGE_CODE = 22;

    @BindView(R.id.iv_logo_shop)
    ImageView ivShopLogo;
    @BindView(R.id.layout_add_logo)
    FrameLayout layoutAddLogo;
    @BindView(R.id.btn_shop_info)
    ConstraintLayout btnShopInfo;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.edt_shop_name)
    EditText edtShopName;
    @BindView(R.id.iv_empty_shop_name)
    ImageView ivEmptyShopName;
    @BindView(R.id.tv_shop_phone)
    TextView tvShopPhone;
    @BindView(R.id.edt_shop_phone)
    EditText edtShopPhone;
    @BindView(R.id.iv_empty_shop_phone)
    ImageView ivEmptyShopPhone;
    @BindView(R.id.tv_shop_description)
    TextView tvShopDescription;
    @BindView(R.id.edt_shop_description)
    EditText edtShopDescription;
    @BindView(R.id.iv_empty_shop_description)
    ImageView ivEmptyShopDescription;
    @BindView(R.id.layout_shop_info)
    ConstraintLayout layoutShopInfo;
    @BindView(R.id.btn_address_info)
    ConstraintLayout btnAddressInfo;
    @BindView(R.id.tv_shop_city)
    TextView tvShopCity;
    @BindView(R.id.layout_choose_shop_city)
    FrameLayout layoutChooseShopCity;
    @BindView(R.id.edt_shop_city)
    EditText edtShopCity;
    @BindView(R.id.iv_empty_city)
    ImageView ivEmptyCity;
    @BindView(R.id.tv_shop_address)
    TextView tvShopAddress;
    @BindView(R.id.edt_shop_address)
    EditText edtShopAddress;
    @BindView(R.id.iv_empty_shop_address)
    ImageView ivEmptyShopAddress;
    @BindView(R.id.btn_location_address)
    Button btnLocationAddress;
    @BindView(R.id.edt_location_address)
    EditText edtShopLocation;
    @BindView(R.id.iv_empty_shop_location)
    ImageView ivEmptyShopLocation;
    @BindView(R.id.layout_address_info)
    ConstraintLayout layoutAddressInfo;
    @BindView(R.id.btn_shop_categories)
    ConstraintLayout btnShopCategories;
    @BindView(R.id.tv_shop_categories)
    TextView tvShopCategories;
    @BindView(R.id.layout_choose_categories)
    FrameLayout layoutChooseCategories;
    @BindView(R.id.edt_shop_categories)
    EditText edtShopCategories;
    @BindView(R.id.iv_empty_categories)
    ImageView ivEmptyCategories;
    @BindView(R.id.tv_shop_hour)
    TextView tvShopHour;
    @BindView(R.id.edt_shop_hour)
    EditText edtShopHour;
    @BindView(R.id.iv_empty_shop_hour)
    ImageView ivEmptyShopHour;
    @BindView(R.id.layout_shop_categories)
    ConstraintLayout layoutShopCategories;
    @BindView(R.id.btn_website_links)
    ConstraintLayout btnWebsiteLinks;
    @BindView(R.id.tv_facebook_link)
    TextView tvFacebookLink;
    @BindView(R.id.edt_facebook_link)
    EditText edtFacebookLink;
    @BindView(R.id.tv_instagram_link)
    TextView tvInstagramLink;
    @BindView(R.id.edt_instagram_link)
    EditText edtInstagramLink;
    @BindView(R.id.tv_twitter_link)
    TextView tvTwitterLink;
    @BindView(R.id.edt_twitter_link)
    EditText edtTwitterLink;
    @BindView(R.id.tv_website_link)
    TextView tvWebsiteLink;
    @BindView(R.id.edt_website_link)
    EditText edtWebsiteLink;
    @BindView(R.id.layout_website_links)
    ConstraintLayout layoutWebsiteLinks;
    @BindView(R.id.btn_create_shop)
    Button btnCreateShop;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_shop_management)
    ConstraintLayout layoutShopManagement;
    @BindView(R.id.iv_shop_info_show)
    ImageView ivShopInfoShow;
    @BindView(R.id.iv_shop_info_hide)
    ImageView ivShopInfoHide;
    @BindView(R.id.iv_address_info_show)
    ImageView ivAddressInfoShow;
    @BindView(R.id.iv_address_info_hide)
    ImageView ivAddressInfoHide;
    @BindView(R.id.iv_shop_categories_show)
    ImageView ivShopCategoriesShow;
    @BindView(R.id.iv_shop_categories_hide)
    ImageView ivShopCategoriesHide;
    @BindView(R.id.iv_website_link_show)
    ImageView ivWebsiteLinkShow;
    @BindView(R.id.iv_website_link_hide)
    ImageView ivWebsiteLinkHide;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_featured)
    TextView tvFeatured;
    @BindView(R.id.tv_verified)
    TextView tvVerified;
    @BindView(R.id.rv_banner_shop)
    RecyclerView rvBannerShop;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_save_shop_info)
    TextView tvSaveShopInfo;
    @BindView(R.id.tv_cancel_shop_info)
    TextView tvCancelShopInfo;
    @BindView(R.id.tv_save_address_info)
    TextView tvSaveAddressInfo;
    @BindView(R.id.tv_cancel_address_info)
    TextView tvCancelAddressInfo;
    @BindView(R.id.tv_save_categories_info)
    TextView tvSaveCategoriesInfo;
    @BindView(R.id.tv_cancel_categories_info)
    TextView tvCancelCategoriesInfo;
    @BindView(R.id.tv_save_website_link)
    TextView tvSaveWebsiteLink;
    @BindView(R.id.tv_cancel_website_link)
    TextView tvCancelWebsiteLink;
    @BindView(R.id.tv_status_title)
    TextView tvStatusTitle;
    @BindView(R.id.tv_featured_title)
    TextView tvFeaturedTitle;
    @BindView(R.id.tv_verified_title)
    TextView tvVerifiedTitle;
    @BindView(R.id.indicator)
    CircleIndicator2 indicator;
    @BindView(R.id.btn_save_shop)
    Button btnSaveShop;

    private LatLng latLng;
    private Bitmap bitmapShop;
    private Bitmap bitmapThumbnail;
    private File shopImage;
    private File thumbnailImage;
    private String cityId = "";
    private String categoryId = "";
    private String gmt;
    private String openHour = "";
    private String deleteIds = "";
    private int oldSizeGallery;
    private String rootFolder;

    private Shop shop;
    private ProgressDialog pDialog;

    private MySharedPreferences sharedPref;
    private ArrayList<Category> listCategories = new ArrayList<>();
    private HashMap<Integer, Category> mMapCategory = new HashMap<>();
    private HashMap<Integer, Category> mTempMapCategory = new HashMap<>();
    ArrayList<String> mResults = new ArrayList<>();
    private ArrayList<File> listFile = new ArrayList<>();
    private ArrayList<OpenHour> listTimes = new ArrayList<>();
    private ArrayList<Banner> arrBanner;

    private BannerNewAdapter bannerNewAdapter;
    private Dialog confirmDeleteGalleryDialog;
    private FragmentPagerAdapter pagerAdapter;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AddNewShopActivityV2.class));
    }

    @Override
    protected void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_new_shop_v2;
    }

    @Override
    protected void initData() {
        rootFolder = Environment.getExternalStorageDirectory() + "/"
                + getString(R.string.app_name) + "/images/";
        sharedPref = new MySharedPreferences(this);

        // list category
        listCategories = new ArrayList<>();
        listCategories.addAll(ParserUtility
                .parseListCategories(sharedPref.getCacheCategories()));

        Intent intent = getIntent();
        shop = intent.getParcelableExtra(Constant.SHOP_OBJ);
        if (shop == null) {
            shop = new Shop();
        } else {
            tvTitle.setText(getResources().getString(R.string.edit_shop));
            btnCreateShop.setText(getResources().getString(R.string.save));
            btnSaveShop.setVisibility(View.VISIBLE);

            // visible
            layoutShopManagement.setVisibility(View.VISIBLE);
            tvSaveShopInfo.setVisibility(View.VISIBLE);
            tvSaveAddressInfo.setVisibility(View.VISIBLE);
            tvSaveCategoriesInfo.setVisibility(View.VISIBLE);
            tvSaveWebsiteLink.setVisibility(View.VISIBLE);

            tvCancelAddressInfo.setVisibility(View.VISIBLE);
            tvCancelShopInfo.setVisibility(View.VISIBLE);
            tvCancelCategoriesInfo.setVisibility(View.VISIBLE);
            tvCancelWebsiteLink.setVisibility(View.VISIBLE);

            tvStatus.setVisibility(View.VISIBLE);
            tvStatusTitle.setVisibility(View.VISIBLE);
            tvFeatured.setVisibility(View.VISIBLE);
            tvFeaturedTitle.setVisibility(View.VISIBLE);
            tvVerified.setVisibility(View.VISIBLE);
            tvVerifiedTitle.setVisibility(View.VISIBLE);

            shop = GlobalValue.currentShop;
            fillData();
        }


        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
        }
    }

    private void fillData() {
        listTimes.clear();
        listTimes.addAll(shop.getArrOpenHour());
        edtShopHour.setText(getResources().getString(R.string.validated_hour));
        edtShopHour.setTextColor(getResources().getColor(R.color.red));

        cityId = shop.getCityId() + "";
        gmt = shop.getGmt();
        oldSizeGallery = shop.getArrBanner().size();

        Glide.with(this).load(shop.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ivShopLogo.setImageBitmap(resource);
                bitmapShop = resource;
            }
        });

        latLng = new LatLng(shop.getLatitude(), shop.getLongitude());
        categoryId = shop.getCategoryId();
        String arrCategoryId[] = shop.getCategoryId().split(",");
        if (arrCategoryId.length != 0) {
            for (int i = 0; i < arrCategoryId.length; i++) {
                for (int j = 0; j < listCategories.size(); j++) {
                    if (listCategories.get(j).getId().equals(arrCategoryId[i])) {
                        listCategories.get(j).setCheck(true);
                        mMapCategory.put(j, listCategories.get(j));
                    }
                }
            }
        }

        edtShopName.setText(shop.getShopName());
        edtShopPhone.setText(shop.getPhone());
        edtShopCity.setText(shop.getCityName());
        edtShopAddress.setText(shop.getAddress());
        String location = getResources().getString(R.string.location_shop,
                String.valueOf(shop.getLatitude()).substring(0, 7),
                String.valueOf(shop.getLongitude()).substring(0, 7));
        edtShopLocation.setText(location);
        edtShopCategories.setText(shop.getCategory());
        edtShopDescription.setText(shop.getDescription());
        edtFacebookLink.setText(shop.getFacebook());
        edtTwitterLink.setText(shop.getTwitter());
        edtInstagramLink.setText(shop.getShopInstagram());
        edtWebsiteLink.setText(shop.getWebsite());

        if (shop.isFeatured()) {
            tvFeatured.setText(getString(R.string.featured_text));
        } else {
            tvFeatured.setText(getString(R.string.not_featured_text));
        }

        if (shop.isVerified()) {
            tvVerified.setText(getString(R.string.verified_text));
        } else {
            tvVerified.setText(getString(R.string.not_verified_text));
        }

        if (shop.getStatus() == 1) {
            tvStatus.setText(getString(R.string.status_text));
        } else {
            tvStatus.setText(getString(R.string.not_status_text));
        }

        setupLayout();

    }

    private void setupLayout() {
        // shop info
        layoutShopInfo.setVisibility(View.GONE);
        ivShopInfoShow.setVisibility(View.GONE);
        ivShopInfoHide.setVisibility(View.VISIBLE);

        // address info
        layoutAddressInfo.setVisibility(View.GONE);
        ivAddressInfoShow.setVisibility(View.GONE);
        ivAddressInfoHide.setVisibility(View.VISIBLE);

        // categories
        layoutShopCategories.setVisibility(View.GONE);
        ivShopCategoriesShow.setVisibility(View.GONE);
        ivShopCategoriesHide.setVisibility(View.VISIBLE);

        // website
        layoutWebsiteLinks.setVisibility(View.GONE);
        ivWebsiteLinkShow.setVisibility(View.GONE);
        ivWebsiteLinkHide.setVisibility(View.VISIBLE);
    }

    private void asynAddShop() {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            if (!pDialog.isShowing()) {
                pDialog.show();
            }
            @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    addShop();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                }
            };
            asyncTask.execute();
        } else {
            showToast(getResources().getString(R.string.no_connection));
        }

    }

    private void addShop() {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            String shopName = edtShopName.getText().toString().trim();
            String phone = edtShopPhone.getText().toString().trim();
            String address = edtShopAddress.getText().toString().trim();
            String description = edtShopDescription.getText().toString().trim();
            final String facebook = edtFacebookLink.getText().toString().trim();
            String twitter = edtTwitterLink.getText().toString().trim();
            String instagram = edtInstagramLink.getText().toString().trim();
            String website = edtWebsiteLink.getText().toString().trim();

            //status = ckbActive.isChecked() ? "1" : "0";

            shop.setShopName(shopName);
            shop.setPhone(phone);
            shop.setAddress(address);
            shop.setCityId(Integer.parseInt(cityId));
            shop.setCategoryId(categoryId);
            shop.setShopTime(openHour);
            shop.setDescription(description);
            shop.setFacebook(facebook);
            shop.setTwitter(twitter);
            shop.setShopInstagram(instagram);
            shop.setWebsite(website);
            shop.setGmt(gmt);


            if (latLng != null) {
                shop.setLatitude(latLng.latitude);
                shop.setLongitude(latLng.longitude);
            } else {
                shop.setLatitude(0.0);
                shop.setLongitude(0.0);
            }

            ModelManager.addNewShop(this, sharedPref.getUserInfo().getId(), shop, "0", deleteIds, shopImage, thumbnailImage, listFile, new ModelManagerListener() {
                @Override
                public void onError(VolleyError error) {
                    Log.e(TAG, "onError: " + error.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast(getResources().getString(R.string.have_error));
                        }
                    });
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
//                    deleteRecursive(new File(rootFolder));
                }

                @Override
                public void onSuccess(Object object) {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    final String json = (String) object;
                    if (ParserUtility.isSuccess(json)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(getResources().getString(R.string.message_success));
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(ParserUtility.getMessage(json));
                            }
                        });
                    }
                    deleteRecursive(new File(rootFolder));

                    Intent intent = new Intent();
                    intent.setAction(Constant.REFRESH_DATA);
                    sendBroadcast(intent);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(getResources().getString(R.string.no_connection));
                }
            });
        }
    }

    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    @Override
    protected void configView() {
        // display * symbol is red color
        tvShopName.setText(Html.fromHtml(getString(R.string.shop_name_2)));
        tvShopPhone.setText(Html.fromHtml(getString(R.string.shop_phone_2)));
        tvShopDescription.setText(Html.fromHtml(getString(R.string.shop_description_2)));
        tvShopAddress.setText(Html.fromHtml(getString(R.string.shop_address_2)));
        tvFacebookLink.setText(Html.fromHtml(getString(R.string.facebook_link_2)));
        tvInstagramLink.setText(Html.fromHtml(getString(R.string.instagram_link_2)));
        tvTwitterLink.setText(Html.fromHtml(getString(R.string.twitter_link_2)));
        tvWebsiteLink.setText(Html.fromHtml(getString(R.string.website_link_2)));

        // config banner
        for (Banner banner : shop.getArrBanner()) {
            Log.e("kevin", "banner image: " + banner.getImage());
        }

        rvBannerShop.setHasFixedSize(true);
        rvBannerShop.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        bannerNewAdapter = new BannerNewAdapter(this, shop.getArrBanner());
        rvBannerShop.setAdapter(bannerNewAdapter);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rvBannerShop);
        indicator.attachToRecyclerView(rvBannerShop, pagerSnapHelper);

        bannerNewAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

        bannerNewAdapter.setOnItemClickListener(new GalleryToAddAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showConfirmDialog(getResources().getString(R.string.remove_from_gallery), position);
            }
        });

    }

    private void showConfirmDialog(String message, int position) {
        View view = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);


        TextView tvMessage = view.findViewById(R.id.tvContent);
        TextView btnYes = view.findViewById(R.id.tvConfirm);
        TextView btnNo = view.findViewById(R.id.tvCancel);

        tvMessage.setText(message);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listFile.size() != 0)
                    listFile.remove(position - oldSizeGallery);
                if (shop != null) {
                    deleteIds += shop.getArrBanner().get(position).getId() + ",";
                    if (deleteIds.length() > 1 && deleteIds.contains(","))
                        deleteIds = deleteIds.substring(0, deleteIds.length() - 1);
                    Log.e("kevin", "deleteIds: " + deleteIds);
                }

                shop.getArrBanner().remove(position);

                // Refresh gallery
                bannerNewAdapter.notifyDataSetChanged();
                confirmDeleteGalleryDialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteGalleryDialog.dismiss();
            }
        });
        confirmDeleteGalleryDialog = builder.create();
        int w = getResources().getDisplayMetrics().widthPixels;
        confirmDeleteGalleryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmDeleteGalleryDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(confirmDeleteGalleryDialog.getWindow().getAttributes());
        lp.width = (int) (w * 0.75);
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        confirmDeleteGalleryDialog.getWindow().setAttributes(lp);
    }

    @OnClick(R.id.iv_back)
    void chooseBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_shop_info)
    void collapseShopInfo() {
        if (layoutShopInfo.getVisibility() == View.GONE) {
            layoutShopInfo.setVisibility(View.VISIBLE);
            ivShopInfoShow.setVisibility(View.VISIBLE);
            ivShopInfoHide.setVisibility(View.GONE);
        } else if (layoutShopInfo.getVisibility() == View.VISIBLE) {
            layoutShopInfo.setVisibility(View.GONE);
            ivShopInfoShow.setVisibility(View.GONE);
            ivShopInfoHide.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_address_info)
    void collapseAddressInfo() {
        if (layoutAddressInfo.getVisibility() == View.GONE) {
            layoutAddressInfo.setVisibility(View.VISIBLE);
            ivAddressInfoShow.setVisibility(View.VISIBLE);
            ivAddressInfoHide.setVisibility(View.GONE);
        } else if (layoutAddressInfo.getVisibility() == View.VISIBLE) {
            layoutAddressInfo.setVisibility(View.GONE);
            ivAddressInfoShow.setVisibility(View.GONE);
            ivAddressInfoHide.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_shop_categories)
    void collapseCategories() {
        if (layoutShopCategories.getVisibility() == View.GONE) {
            layoutShopCategories.setVisibility(View.VISIBLE);
            ivShopCategoriesShow.setVisibility(View.VISIBLE);
            ivShopCategoriesHide.setVisibility(View.GONE);
        } else if (layoutShopCategories.getVisibility() == View.VISIBLE) {
            layoutShopCategories.setVisibility(View.GONE);
            ivShopCategoriesShow.setVisibility(View.GONE);
            ivShopCategoriesHide.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_website_links)
    void collapseWebsiteLinks() {
        if (layoutWebsiteLinks.getVisibility() == View.GONE) {
            layoutWebsiteLinks.setVisibility(View.VISIBLE);
            ivWebsiteLinkShow.setVisibility(View.VISIBLE);
            ivWebsiteLinkHide.setVisibility(View.GONE);
        } else if (layoutWebsiteLinks.getVisibility() == View.VISIBLE) {
            layoutWebsiteLinks.setVisibility(View.GONE);
            ivWebsiteLinkShow.setVisibility(View.GONE);
            ivWebsiteLinkHide.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.btn_create_shop, R.id.btn_save_shop})
    void createShop() {
        validateDataCreateShop();

        if (validate()) {
            asynAddShop();
        }
    }

    private boolean validate() {
        if (bitmapShop == null) {
            showToast(getResources().getString(R.string.please_select_shop_image));
            return false;
        }
        if (edtShopName.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_enter_shop_name));
            edtShopName.requestFocus();
            return false;
        }
        if (edtShopPhone.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_enter_phone_number));
            edtShopPhone.requestFocus();
            return false;
        }
        if (edtShopCity.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_enter_city));
            edtShopCity.requestFocus();
            return false;
        }
        if (edtShopAddress.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_enter_address));
            edtShopAddress.requestFocus();
            return false;
        }
        if (latLng == null) {
            showToast(getResources().getString(R.string.please_select_lat_long));
            edtShopLocation.requestFocus();
            return false;
        }
        if (edtShopCategories.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_select_category));
            edtShopCategories.requestFocus();
            return false;
        }
        if (listTimes.size() == 0) {
            showToast(getResources().getString(R.string.please_select_open_close_hour));
            return false;
        }
        return true;
    }

    @OnClick(R.id.layout_add_logo)
    void addLogoShop() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.select_photo_from)
                .setPositiveButton(R.string.gallery,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // one can be replaced with any action code
                                if (PermissionUtil.checkReadWriteStoragePermission(AddNewShopActivityV2.this)) {
                                    Intent pickPhoto = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto,
                                            REQUEST_IMAGE_GALLERY_IMAGE_ONE);
                                }
                            }
                        })
                .setNegativeButton(R.string.camera,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // zero can be replaced with any action code
                                if (PermissionUtil.checkCameraPermission(AddNewShopActivityV2.this)) {
                                    Intent takePictureIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (takePictureIntent
                                            .resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(takePictureIntent,
                                                REQUEST_IMAGE_CAPTURE_IMAGE_ONE);
                                    }
                                }
                            }
                        }).create();

        dialog.show();

        TextView dialogTitle = dialog.findViewById(androidx.appcompat.R.id.alertTitle);
        Typeface typefaceTitle = Typeface.createFromAsset(getAssets(), "fonts/Nawar_Font_Regular.ttf");
        if (dialogTitle != null) {
            dialogTitle.setTypeface(typefaceTitle);
        }

        TextView dialogMessage = dialog.findViewById(android.R.id.message);
        Button dialogButton1 = dialog.findViewById(android.R.id.button1);
        Button dialogButton2 = dialog.findViewById(android.R.id.button2);
        Typeface typefaceTwo = Typeface.createFromAsset(getAssets(), "fonts/TanseekModernProArabic-Medium.ttf");
        if (dialogMessage != null) {
            dialogMessage.setTypeface(typefaceTwo);
            dialogMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }
        if (dialogButton1 != null) {
            dialogButton1.setTypeface(typefaceTwo);
            dialogButton1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }
        if (dialogButton2 != null) {
            dialogButton2.setTypeface(typefaceTwo);
            dialogButton2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        }


    }

    @OnClick(R.id.btn_location_address)
    void chooseLocationAddress() {
        Intent intent = new Intent(this, ChooseExtraLocationActivity.class);
        if (latLng != null) {
            intent.putExtra(Constant.LOCATION, latLng);
        }
        startActivityForResult(intent, REQUEST_CODE_GET_LOCATION);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
    }

    @OnClick(R.id.layout_choose_shop_city)
    void chooseShopCity() {
        showCityDialog();
    }


    @OnClick(R.id.layout_choose_categories)
    void chooseShopCategory() {
        showCategoryDialog();
    }

    @OnClick(R.id.layout_choose_hour)
    void chooseHour() {
        Intent intent2 = new Intent(this, ChooseTimeActivity.class);
        if (listTimes.size() != 0)
            intent2.putParcelableArrayListExtra(Constant.LIST_TIME, listTimes);
        intent2.putExtra(Constant.GMT, gmt);
        startActivityForResult(intent2, REQUEST_CODE_GET_OPEN_HOUR);
    }

    @OnClick(R.id.iv_add_banner)
    void addShopBanner() {
        chooseMultiImage(CHOOSE_MORE_IMAGE_CODE);
    }

    @OnClick(R.id.tv_cancel_address_info)
    void hideLayoutAddressInfo() {
        if (layoutAddressInfo.getVisibility() == View.VISIBLE) {
            layoutAddressInfo.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_cancel_shop_info)
    void hideLayoutShopInfo() {
        if (layoutShopInfo.getVisibility() == View.VISIBLE) {
            layoutShopInfo.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.tv_cancel_categories_info)
    void hideLayoutCategoriesInfo() {
        if (layoutShopCategories.getVisibility() == View.VISIBLE) {
            layoutShopCategories.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_save_categories_info, R.id.tv_save_website_link,
            R.id.tv_save_shop_info, R.id.tv_save_address_info})
    void saveCategoriesInfo() {
        validateDataCreateShop();

        if (validate()) {
            asynAddShop();
        }
    }

    @OnClick(R.id.tv_cancel_website_link)
    void hideLayoutWebsiteLink() {
        if (layoutWebsiteLinks.getVisibility() == View.VISIBLE) {
            layoutWebsiteLinks.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.layout_shop_management)
    void openShopManagement() {
        Intent intent1 = new Intent(this, ProductManagementActivity.class);
        intent1.putExtra(Constant.SHOP_ID, shop.getShopId() + "");
        startActivity(intent1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_GET_LOCATION:
                if (resultCode == RESULT_OK && data != null) {
                    latLng = data.getParcelableExtra(Constant.LOCATION);
                    String latitude = String.valueOf(latLng.latitude).substring(0, 7);
                    String longitude = String.valueOf(latLng.longitude).substring(0, 7);

                    edtShopLocation.setText(getString(R.string.location_shop, latitude, longitude));
                }
                break;
            case REQUEST_IMAGE_GALLERY_IMAGE_ONE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    Bitmap image = null;
                    Bitmap imageConvert = null;
                    try {
                        bitmapShop = image = ImageUtil.decodeUri(this, selectedImage);
                        int dimension = getSquareCropDimensionForBitmap(image);
                        imageConvert = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                        shopImage = ImageUtil.saveBitmap(this, image);
                        ivShopLogo.setImageBitmap(null);
                        ivShopLogo.setImageBitmap(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE_IMAGE_ONE:
                if (resultCode == RESULT_OK && data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    bitmapShop = imageBitmap;
                    int dimension = getSquareCropDimensionForBitmap(imageBitmap);
                    Bitmap imageConvert = ThumbnailUtils.extractThumbnail(imageBitmap, dimension, dimension);
                    try {
                        ivShopLogo.setImageBitmap(null);
                        shopImage = ImageUtil.saveBitmap(this, imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ivShopLogo.setImageBitmap(imageBitmap);
                }
                break;
            case REQUEST_CODE_GET_OPEN_HOUR:
                if (resultCode == RESULT_OK && data != null) {
                    listTimes = data.getParcelableArrayListExtra(Constant.LIST_TIME);
                    openHour = data.getStringExtra(Constant.SHOP_TIME);
                    gmt = data.getStringExtra(Constant.GMT);

                    edtShopHour.setText(getString(R.string.validated_hour));
                    edtShopHour.setTextColor(getResources().getColor(R.color.red));
                }
                break;
            case CHOOSE_MORE_IMAGE_CODE:
                try {
                    mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                    if (mResults.size() != 0) {
                        for (int i = 0; i < mResults.size(); i++) {
                            File file = new File(mResults.get(i));
                            listFile.add(file);

                            Bitmap bm = ImageUtil.getCorrectlyOrientedImage(this, Uri.fromFile(file));
                            Banner banner = new Banner();
                            banner.setBitmap(ImageUtil.decodeBitmapFromBitmap(bm, 500, 500));
                            shop.getArrBanner().add(banner);
                        }
                    }
                    // Refresh gallery
                    bannerNewAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void chooseMultiImage(int requestCode) {
        // start multiple photos selector
        Intent intent = new Intent(this, ImagesSelectorActivity.class);
        // max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 8);
        // min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
        // show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false);
        // pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
        // start the selector
        startActivityForResult(intent, requestCode);
    }

    private void showCategoryDialog() {
        if (mTempMapCategory.size() != mMapCategory.size()) {
            mTempMapCategory.clear();
            mTempMapCategory.putAll(mMapCategory);
        }

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_category, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);

        RecyclerView rcvCategory = view.findViewById(R.id.rcv_category);
        ImageView ivArrowBottom = view.findViewById(R.id.iv_arrow_bottom);
        rcvCategory.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rcvCategory.setLayoutManager(manager);

        final ShopCategoryAdapter shopCategoryAdapter = new ShopCategoryAdapter(this, listCategories);
        rcvCategory.setAdapter(shopCategoryAdapter);

        shopCategoryAdapter.setOnCheckChangeListener(new ShopCategoryAdapter.OnCheckChangeListener() {
            @Override
            public void onCheckChange(View view, int position, boolean isChecked) {
                if (isChecked) {
                    mTempMapCategory.put(position, listCategories.get(position));
                    Log.e(TAG, "temp size: " + mTempMapCategory.size());
                } else {
                    mTempMapCategory.remove(position);
                }
            }
        });

        Button btnCancel = view.findViewById(R.id.btn_no);
        Button btnOK = view.findViewById(R.id.btn_ok);

        final Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategory();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTempMapCategory.clear();
                dialog.dismiss();
            }
        });

        rcvCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!rcvCategory.canScrollVertically(1)) {
                    ivArrowBottom.setVisibility(View.GONE);
                } else {
                    ivArrowBottom.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setCategory() {
        edtShopCategories.setText("");
        StringBuilder category = new StringBuilder();
        categoryId = "";
        if (mMapCategory.size() != mTempMapCategory.size()) {
            mMapCategory.clear();
            mMapCategory.putAll(mTempMapCategory);
            mTempMapCategory.clear();
        }
        if (mMapCategory.size() > 0) {
            for (Integer index : mMapCategory.keySet()) {
                category.append(mMapCategory.get(index).getName()).append(",");
                categoryId += mMapCategory.get(index).getId() + ",";
            }
            if (category.length() > 1 && category.toString().contains(","))
                category = new StringBuilder(category.substring(0, category.length() - 1));
            edtShopCategories.setText(category.toString());
            if (categoryId.length() > 1 && categoryId.contains(","))
                categoryId = categoryId.substring(0, categoryId.length() - 1);
            Log.e(TAG, "categoryId: " + categoryId);

            for (int i = 0; i < listCategories.size(); i++) {
                if (mMapCategory.containsKey(i)) {
                    listCategories.get(i).setCheck(true);
                } else {
                    listCategories.get(i).setCheck(false);
                }

            }
        } else {
            for (int i = 0; i < listCategories.size(); i++) {
                listCategories.get(i).setCheck(false);
            }
        }
    }

    private void showCityDialog() {
        CityIdDialog dialog = CityIdDialog.newInstance();
        dialog.show(getSupportFragmentManager(), dialog.getTag());
        dialog.setOnSearchByCityIdListener((idCity, cityName) -> {
            if (idCity != null && cityName != null) {
                cityId = idCity;
                edtShopCity.setText(cityName);
            }

        });
    }


    private int getSquareCropDimensionForBitmap(Bitmap bitmap) {
        return Math.min(bitmap.getWidth(), bitmap.getHeight());
    }

    private void validateDataCreateShop() {
        // shop name
        if (edtShopName.getText().toString().trim().isEmpty()) {
            if (ivEmptyShopName.getVisibility() == View.GONE) {
                ivEmptyShopName.setVisibility(View.VISIBLE);
            }
            edtShopName.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyShopName.getVisibility() == View.VISIBLE) {
                ivEmptyShopName.setVisibility(View.GONE);
            }
            edtShopName.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // shop phone
        if (edtShopPhone.getText().toString().trim().isEmpty()) {
            if (ivEmptyShopPhone.getVisibility() == View.GONE) {
                ivEmptyShopPhone.setVisibility(View.VISIBLE);
            }
            edtShopPhone.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyShopPhone.getVisibility() == View.VISIBLE) {
                ivEmptyShopPhone.setVisibility(View.GONE);
            }
            edtShopPhone.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // shop city
        if (edtShopCity.getText().toString().trim().isEmpty()) {
            if (ivEmptyCity.getVisibility() == View.GONE) {
                ivEmptyCity.setVisibility(View.VISIBLE);
            }
            edtShopCity.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyCity.getVisibility() == View.VISIBLE) {
                ivEmptyCity.setVisibility(View.GONE);
            }
            edtShopCity.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // shop address
        if (edtShopAddress.getText().toString().trim().isEmpty()) {
            if (ivEmptyShopAddress.getVisibility() == View.GONE) {
                ivEmptyShopAddress.setVisibility(View.VISIBLE);
            }
            edtShopAddress.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyShopAddress.getVisibility() == View.VISIBLE) {
                ivEmptyShopAddress.setVisibility(View.GONE);
            }
            edtShopAddress.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // shop location
        if (edtShopLocation.getText().toString().trim().isEmpty()) {
            if (ivEmptyShopLocation.getVisibility() == View.GONE) {
                ivEmptyShopLocation.setVisibility(View.VISIBLE);
            }
            edtShopLocation.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyShopLocation.getVisibility() == View.VISIBLE) {
                ivEmptyShopLocation.setVisibility(View.GONE);
            }
            edtShopLocation.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // shop categories
        if (edtShopCategories.getText().toString().trim().isEmpty()) {
            if (ivEmptyCategories.getVisibility() == View.GONE) {
                ivEmptyCategories.setVisibility(View.VISIBLE);
            }
            edtShopCategories.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyCategories.getVisibility() == View.VISIBLE) {
                ivEmptyCategories.setVisibility(View.GONE);
            }
            edtShopCategories.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // shop open-close-hour
        if (edtShopHour.getText().toString().trim().isEmpty()) {
            if (ivEmptyShopHour.getVisibility() == View.GONE) {
                ivEmptyShopHour.setVisibility(View.VISIBLE);
            }
            edtShopHour.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyShopHour.getVisibility() == View.VISIBLE) {
                ivEmptyShopHour.setVisibility(View.GONE);
            }
            edtShopHour.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

//        // facebook link
//        if (edtFacebookLink.getText().toString().trim().isEmpty()) {
//            if (ivEmptyFacebookLink.getVisibility() == View.GONE) {
//                ivEmptyFacebookLink.setVisibility(View.VISIBLE);
//            }
//            edtFacebookLink.setBackground(ContextCompat.getDrawable(
//                    this, R.drawable.bg_edit_text_wrong));
//        } else {
//            if (ivEmptyFacebookLink.getVisibility() == View.VISIBLE) {
//                ivEmptyFacebookLink.setVisibility(View.GONE);
//            }
//            edtFacebookLink.setBackground(ContextCompat.getDrawable(
//                    this, R.drawable.bg_edit_text_normal));
//        }
//
//        // instagram link
//        if (edtInstagramLink.getText().toString().trim().isEmpty()) {
//            if (ivEmptyInstagramLink.getVisibility() == View.GONE) {
//                ivEmptyInstagramLink.setVisibility(View.VISIBLE);
//            }
//            edtInstagramLink.setBackground(ContextCompat.getDrawable(
//                    this, R.drawable.bg_edit_text_wrong));
//        } else {
//            if (ivEmptyInstagramLink.getVisibility() == View.VISIBLE) {
//                ivEmptyInstagramLink.setVisibility(View.GONE);
//            }
//            edtInstagramLink.setBackground(ContextCompat.getDrawable(
//                    this, R.drawable.bg_edit_text_normal));
//        }
//
//        // twitter link
//        if (edtTwitterLink.getText().toString().trim().isEmpty()) {
//            if (ivEmptyTwitterLink.getVisibility() == View.GONE) {
//                ivEmptyTwitterLink.setVisibility(View.VISIBLE);
//            }
//            edtTwitterLink.setBackground(ContextCompat.getDrawable(
//                    this, R.drawable.bg_edit_text_wrong));
//        } else {
//            if (ivEmptyTwitterLink.getVisibility() == View.VISIBLE) {
//                ivEmptyTwitterLink.setVisibility(View.GONE);
//            }
//            edtTwitterLink.setBackground(ContextCompat.getDrawable(
//                    this, R.drawable.bg_edit_text_normal));
//        }
//
//        // website link
//        if (edtWebsiteLink.getText().toString().trim().isEmpty()) {
//            if (ivEmptyWebsiteLink.getVisibility() == View.GONE) {
//                ivEmptyWebsiteLink.setVisibility(View.VISIBLE);
//            }
//            edtWebsiteLink.setBackground(ContextCompat.getDrawable(
//                    this, R.drawable.bg_edit_text_wrong));
//        } else {
//            if (ivEmptyWebsiteLink.getVisibility() == View.VISIBLE) {
//                ivEmptyWebsiteLink.setVisibility(View.GONE);
//            }
//            edtWebsiteLink.setBackground(ContextCompat.getDrawable(
//                    this, R.drawable.bg_edit_text_normal));
//        }
    }
}

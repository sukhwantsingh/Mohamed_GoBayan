package com.libyasolutions.libyamarketplace.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import androidx.core.content.ContextCompat;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.libyasolutions.libyamarketplace.BaseActivityV2;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.BannerNewAdapter;
import com.libyasolutions.libyamarketplace.adapter.CategoryAdapterForProductV2;
import com.libyasolutions.libyamarketplace.adapter.ChildOptionAdapter;
import com.libyasolutions.libyamarketplace.adapter.ExtraOptionAdapter;
import com.libyasolutions.libyamarketplace.adapter.GalleryToAddAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.interfaces.AdapterListener;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.network.ProgressDialog;
import com.libyasolutions.libyamarketplace.object.Banner;
import com.libyasolutions.libyamarketplace.object.Category;
import com.libyasolutions.libyamarketplace.object.ExtraOptions;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.OptionsItem;
import com.libyasolutions.libyamarketplace.util.ImageUtil;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.PermissionUtil;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator2;

public class AddNewProductActivityV2 extends BaseActivityV2 {

    public final int REQUEST_IMAGE_GALLERY_IMAGE_ONE = 0;
    public final int REQUEST_IMAGE_CAPTURE_IMAGE_ONE = 1;
    private final int CHOOSE_MORE_IMAGE_CODE = 22;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.rv_banner_product)
    RecyclerView rvBannerProduct;
    @BindView(R.id.iv_add_banner)
    ImageView ivAddBanner;
    @BindView(R.id.indicator)
    CircleIndicator2 indicator;
    @BindView(R.id.iv_logo_product)
    ImageView ivLogoProduct;
    @BindView(R.id.iv_add_logo)
    ImageView ivAddLogo;
    @BindView(R.id.layout_add_logo)
    FrameLayout layoutAddLogo;
    @BindView(R.id.tv_product_code)
    TextView tvProductCode;
    @BindView(R.id.edt_product_code)
    EditText edtProductCode;
    @BindView(R.id.iv_empty_product_code)
    ImageView ivEmptyProductCode;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.edt_product_name)
    EditText edtProductName;
    @BindView(R.id.iv_empty_product_name)
    ImageView ivEmptyProductName;
    @BindView(R.id.tv_product_price)
    TextView tvProductPrice;
    @BindView(R.id.edt_product_price)
    EditText edtProductPrice;
    @BindView(R.id.iv_empty_product_price)
    ImageView ivEmptyProductPrice;
    @BindView(R.id.tv_product_description)
    TextView tvProductDescription;
    @BindView(R.id.edt_product_description)
    EditText edtProductDescription;
    @BindView(R.id.iv_empty_product_description)
    ImageView ivEmptyProductDescription;
    @BindView(R.id.tv_save_product_info)
    TextView tvSaveProductInfo;
    @BindView(R.id.tv_cancel_product_info)
    TextView tvCancelProductInfo;
    @BindView(R.id.layout_product_info)
    ConstraintLayout layoutProductInfo;
    @BindView(R.id.iv_product_info_show)
    ImageView ivProductInfoShow;
    @BindView(R.id.iv_product_info_hide)
    ImageView ivProductInfoHide;
    @BindView(R.id.btn_product_info)
    ConstraintLayout btnProductInfo;
    @BindView(R.id.container_product_info)
    FrameLayout containerProductInfo;


    @BindView(R.id.edt_product_categories)
    EditText edtProductCategories;
    @BindView(R.id.iv_empty_categories)
    ImageView ivEmptyCategories;
    @BindView(R.id.tv_save_categories_info)
    TextView tvSaveCategoriesInfo;
    @BindView(R.id.tv_cancel_categories_info)
    TextView tvCancelCategoriesInfo;
    @BindView(R.id.layout_product_categories)
    ConstraintLayout layoutProductCategories;
    @BindView(R.id.iv_product_categories_show)
    ImageView ivProductCategoriesShow;
    @BindView(R.id.iv_product_categories_hide)
    ImageView ivProductCategoriesHide;
    @BindView(R.id.btn_product_categories)
    ConstraintLayout btnProductCategories;
    @BindView(R.id.container_product_categories)
    FrameLayout containerProductCategories;
    @BindView(R.id.rv_extra_options)
    RecyclerView rvExtraOptions;
    @BindView(R.id.layout_extra_options)
    ConstraintLayout layoutExtraOptions;
    @BindView(R.id.iv_extra_options_show)
    ImageView ivExtraOptionsShow;
    @BindView(R.id.iv_extra_options_hide)
    ImageView ivExtraOptionsHide;
    @BindView(R.id.btn_extra_options)
    ConstraintLayout btnExtraOptions;
    @BindView(R.id.container_extra_options)
    FrameLayout containerExtraOptions;
    @BindView(R.id.tv_available)
    TextView tvAvailable;
    @BindView(R.id.tv_out_of_stock)
    TextView tvOutOfStock;
    @BindView(R.id.layout_product_status)
    LinearLayout layoutProductStatus;
    @BindView(R.id.tv_active)
    TextView tvActive;
    @BindView(R.id.tv_inactive)
    TextView tvInactive;
    @BindView(R.id.layout_active)
    LinearLayout layoutActive;
    @BindView(R.id.btn_create_product)
    Button btnCreateProduct;
    @BindView(R.id.btn_save_product)
    Button btnSaveProduct;

    ArrayList<String> mResults = new ArrayList<>();
    private ArrayList<File> listFile = new ArrayList<>();
    private ArrayList<Category> listCategories;

    private ArrayList<ExtraOptions> listExtraOption = new ArrayList<>();
    private ArrayList<OptionsItem> listOptionItem = new ArrayList<>();
    private HashMap<Integer, OptionsItem> mMapOptionItem = new HashMap<>();
    private HashMap<Integer, OptionsItem> mTempOptionItem = new HashMap<>();

    private String rootFolder;
    private MySharedPreferences sharedPref;

    private ProgressDialog pDialog;

    private String shopId;
    private String deleteIds = "";
    private String categoryId = "";
    private Menu product;
    private String available = "1";
    private String status = "1";

    private Bitmap bitmapProduct;
    private File productImage, thumbnailImage;
    private int oldSizeGallery;
    private int parentPosition;

    private BannerNewAdapter bannerNewAdapter;
    private Dialog confirmDeleteGalleryDialog;

    private int currentParentPosition, oldParentPosition;
    private ExtraOptionAdapter extraOptionAdapter;


    @Override
    protected void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_new_product_v2;
    }

    @Override
    protected void initData() {
        rootFolder = Environment.getExternalStorageDirectory() + "/"
                + getString(R.string.app_name) + "/images/";
        sharedPref = new MySharedPreferences(this);

        listCategories = new ArrayList<>();
        listCategories.addAll(ParserUtility
                .parseListCategories(sharedPref.getCacheCategories()));

        Intent intent = getIntent();
        shopId = intent.getStringExtra(Constant.SHOP_ID);
        product = intent.getParcelableExtra(Constant.PRODUCT_OBJ);

        rvExtraOptions.setHasFixedSize(true);
        rvExtraOptions.setNestedScrollingEnabled(false);
        rvExtraOptions.setLayoutManager(new LinearLayoutManager(this));


        if (product == null) {
            product = new Menu();
        } else {
            tvTitle.setText(getResources().getString(R.string.edit_product));
            btnCreateProduct.setText(getResources().getString(R.string.save));
            btnSaveProduct.setVisibility(View.VISIBLE);

            product = GlobalValue.currentProduct;
            fillData();
        }
        //setGalleryAdapter();
        getCategoryByShop();

        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
        }
    }

    private void fillData() {
        Glide.with(this).load(product.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                bitmapProduct = resource;
                ivLogoProduct.setImageBitmap(resource);
            }
        });

        oldSizeGallery = product.getArrGalleries().size();

        categoryId = String.valueOf(product.getCategoryId());
        edtProductCategories.setText(product.getCategory());

        edtProductName.setText(product.getName());
        edtProductCode.setText(product.getCode());
        edtProductPrice.setText(product.getPrice() + "");
        edtProductDescription.setText(product.getDescription());




        if (product.getStatus().equals("1")) {
            status = "1";
            tvActive.setBackgroundResource(R.drawable.bg_border_grey);
            tvInactive.setBackgroundResource(R.drawable.bg_black_transparent);
        } else {
            status = "0";
            tvActive.setBackgroundResource(R.drawable.bg_black_transparent);
            tvInactive.setBackgroundResource(R.drawable.bg_border_grey);

        }

        if (product.getAvailable().equals("1")) {
            available = "1";
            tvAvailable.setBackgroundResource(R.drawable.bg_border_grey);
            tvOutOfStock.setBackgroundResource(R.drawable.bg_black_transparent);

        } else {
            available = "0";
            tvAvailable.setBackgroundResource(R.drawable.bg_black_transparent);
            tvOutOfStock.setBackgroundResource(R.drawable.bg_border_grey);

        }

        setupLayout();

        listExtraOption.clear();
        listExtraOption.addAll(product.getExtraOptions());
        extraOptionAdapter = new ExtraOptionAdapter(this, listExtraOption);

        extraOptionAdapter.setOnItemClickListener((view, position) -> {
            listOptionItem.clear();
            listOptionItem.addAll(product.getExtraOptions().get(position).getOptionsItems());
            Log.e("kevin", "option size: " + listOptionItem.size());
            Log.e("kevin", "option size 1: " + product.getExtraOptions().get(position).getOptionsItems().size());
            oldParentPosition = currentParentPosition;
            currentParentPosition = position;
            showExtraOptionDialog(view, parentPosition);
        });

        rvExtraOptions.setAdapter(extraOptionAdapter);
    }

    private void setupLayout() {
        // product info
        layoutProductInfo.setVisibility(View.GONE);
        ivProductInfoShow.setVisibility(View.GONE);
        ivProductInfoHide.setVisibility(View.VISIBLE);

        // extra option
        layoutExtraOptions.setVisibility(View.GONE);
        ivExtraOptionsShow.setVisibility(View.GONE);
        ivExtraOptionsHide.setVisibility(View.VISIBLE);


        // categories
        layoutProductCategories.setVisibility(View.GONE);
        ivProductCategoriesShow.setVisibility(View.GONE);
        ivProductCategoriesHide.setVisibility(View.VISIBLE);
    }

    @Override
    protected void configView() {
        // display * symbol is red color
        tvProductName.setText(Html.fromHtml(getString(R.string.product_name_2)));
        tvProductCode.setText(Html.fromHtml(getString(R.string.product_code_2)));
        tvProductPrice.setText(Html.fromHtml(getString(R.string.product_price_2)));
        tvProductDescription.setText(Html.fromHtml(getString(R.string.product_description_2)));

        // config banner
        for (Banner banner : product.getArrGalleries()) {
            Log.e("kevin", "banner image: " + banner.getImage());
        }

        rvBannerProduct.setHasFixedSize(true);
        rvBannerProduct.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        bannerNewAdapter = new BannerNewAdapter(this, product.getArrGalleries());
        rvBannerProduct.setAdapter(bannerNewAdapter);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rvBannerProduct);
        indicator.attachToRecyclerView(rvBannerProduct, pagerSnapHelper);

        bannerNewAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

        bannerNewAdapter.setOnItemClickListener(new GalleryToAddAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showConfirmDialog(getResources().getString(R.string.remove_from_gallery), position);
            }
        });

    }

    private void getCategoryByShop() {
        if (!NetworkUtil.checkNetworkAvailable(this)) {
            Toast.makeText(this, R.string.message_network_is_unavailable, Toast.LENGTH_LONG).show();
        } else
            ModelManager.getAllCategoryByShop(this, Integer.parseInt(shopId), true,
                    new ModelManagerListener() {

                        @Override
                        public void onSuccess(Object object) {
                            // TODO Auto-generated method stub
                            String json = (String) object;
                            if (ParserUtility
                                    .parseListCategories(json).size() == 0) {
                                showToast(getResources().getString(R.string.have_no_date));
                            } else {
                                listCategories.clear();
                                listCategories.addAll(ParserUtility
                                        .parseListCategories(json));

                            }
                            if (product != null)
                                for (int i = 0; i < listCategories.size(); i++) {
                                    if (listCategories.get(i).getId().equals(categoryId)) {
                                        parentPosition = i;
                                    }
                                }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            // TODO Auto-generated method stub
                            listCategories.clear();
                            Toast.makeText(AddNewProductActivityV2.this, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                        }
                    });
    }

    private void showConfirmDialog(String message, final int position) {
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

                if (product != null) {
                    deleteIds += product.getArrGalleries().get(position).getId() + ",";
                    if (deleteIds.length() > 1 && deleteIds.contains(","))
                        deleteIds = deleteIds.substring(0, deleteIds.length() - 1);
                    Log.e("kevin", "deleteIds: " + deleteIds);
                }

                product.getArrGalleries().remove(position);
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
        confirmDeleteGalleryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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

    @OnClick(R.id.layout_add_logo)
    void addLogoProduct() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.select_photo_from)
                .setPositiveButton(R.string.gallery,  (arg0, arg1) -> {
                            // one can be replaced with any action code
                            if (PermissionUtil.checkReadWriteStoragePermission(AddNewProductActivityV2.this)) {
                                Intent pickPhoto = new Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto,
                                        REQUEST_IMAGE_GALLERY_IMAGE_ONE);
                            }
                        })
                .setNegativeButton(R.string.camera, (arg0, arg1) -> {
                            // zero can be replaced with any action code
                            if (PermissionUtil.checkCameraPermission(AddNewProductActivityV2.this)) {
                                Intent takePictureIntent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent
                                        .resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent,
                                            REQUEST_IMAGE_CAPTURE_IMAGE_ONE);
                                }
                            }
                        }).create();
        dialog.show();

        TextView dialogTitle = dialog.findViewById(R.id.alertTitle);
       // Typeface typefaceTitle = Typeface.createFromAsset(getAssets(), "fonts/Nawar_Font_Regular.ttf");
      //  if (dialogTitle != null) {
      //      dialogTitle.setTypeface(typefaceTitle);
      //  }

        TextView dialogMessage = dialog.findViewById(android.R.id.message);
        Button dialogButton1 = dialog.findViewById(android.R.id.button1);
        Button dialogButton2 = dialog.findViewById(android.R.id.button2);
     //  Typeface typefaceTwo = Typeface.createFromAsset(getAssets(), "fonts/TanseekModernProArabic-Medium.ttf");
//        if (dialogMessage != null) {
//            dialogMessage.setTypeface(typefaceTwo);
//            dialogMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
//        }
//        if (dialogButton1 != null) {
//            dialogButton1.setTypeface(typefaceTwo);
//            dialogButton1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
//        }
//        if (dialogButton2 != null) {
//            dialogButton2.setTypeface(typefaceTwo);
//            dialogButton2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
//        }
    }

    @OnClick(R.id.iv_add_banner)
    void addBannerLogo() {
        chooseMultiImage(CHOOSE_MORE_IMAGE_CODE);
    }

    @OnClick(R.id.layout_product_categories)
    void chooseCategoriesProduct() {
        View view = getLayoutInflater().inflate(R.layout.layout_dialog_category_by_shop, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        RecyclerView rvProductCategory = view.findViewById(R.id.lv_category);
        ImageView ivArrowBottom = view.findViewById(R.id.iv_arrow_bottom);

        final Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
        dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        rvProductCategory.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvProductCategory.setLayoutManager(manager);
        final CategoryAdapterForProductV2 shopCategoryAdapter = new CategoryAdapterForProductV2(this, listCategories);
        rvProductCategory.setAdapter(shopCategoryAdapter);

        shopCategoryAdapter.setOnItemProductCategoryListener((position, isChecked) -> {
            for (Category ca : listCategories) {
                ca.setCheck(false);
            }

            final Category category = listCategories.get(position);
            category.setCheck(isChecked);
            categoryId = category.getId();
            edtProductCategories.setText(category.getName());

            mMapOptionItem.clear();
            mTempOptionItem.clear();

            listExtraOption.clear();
            listExtraOption.addAll(category.getArrExtraOptions());
            extraOptionAdapter = new ExtraOptionAdapter(AddNewProductActivityV2.this, listExtraOption);
            rvExtraOptions.setAdapter(extraOptionAdapter);

            extraOptionAdapter.setOnItemClickListener(new AdapterListener.onItemClickListener() {
                @Override
                public void onclick(View view, int position) {
                    listOptionItem.clear();
                    listOptionItem.addAll(category.getArrExtraOptions().get(position).getOptionsItems());
                    Log.e("kevin", "option size: " + listOptionItem.size());
                    Log.e("kevin", "option size 1: " + category.getArrExtraOptions().get(position).getOptionsItems().size());
                    oldParentPosition = currentParentPosition;
                    currentParentPosition = position;
                    showExtraOptionDialog(view, parentPosition);
                }
            });

            dialog.dismiss();
        });

        rvProductCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    ivArrowBottom.setVisibility(View.GONE);
                } else {
                    ivArrowBottom.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void showExtraOptionDialog(final View edtOption, final int parentPosition) {
        if (oldParentPosition != currentParentPosition) {
            mMapOptionItem.clear();
            for (int i = 0; i < listOptionItem.size(); i++) {
                if (listOptionItem.get(i).isChecked()) {
                    mMapOptionItem.put(i, listOptionItem.get(i));
                }
            }
        }

        if (mTempOptionItem.size() != mMapOptionItem.size()) {
            mTempOptionItem.clear();
            mTempOptionItem.putAll(mMapOptionItem);
        }

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_extra_option, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);

        RecyclerView rcvOption = view.findViewById(R.id.rcv_option);
        rcvOption.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rcvOption.setLayoutManager(manager);

        Button btnOK = view.findViewById(R.id.btn_ok);
        Button btnCancel = view.findViewById(R.id.btn_no);
        ImageView ivArrowBottom = view.findViewById(R.id.iv_arrow_bottom);

        ChildOptionAdapter childOptionAdapter = new ChildOptionAdapter(this, listOptionItem);
        rcvOption.setAdapter(childOptionAdapter);

        childOptionAdapter.setOnCheckChangeListener(new ChildOptionAdapter.OnCheckChangeListener() {
            @Override
            public void onCheckChange(View view, int position, boolean isChecked) {
                OptionsItem optionsItem = listOptionItem.get(position);
                if (isChecked) {
                    mTempOptionItem.put(position, optionsItem);
                } else {
                    mTempOptionItem.remove(position);
                }
                Log.e("kevin", "size: " + mTempOptionItem.size());
            }
        });

        final Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
        dialog.getWindow().setLayout(width, height);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOptionItem(edtOption, parentPosition);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        rvExtraOptions.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    ivArrowBottom.setVisibility(View.GONE);
                } else {
                    ivArrowBottom.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    public void setOptionItem(View edtOption, int parenPosition) {

        StringBuilder option = new StringBuilder();
        if (mMapOptionItem.size() != mTempOptionItem.size()) {
            mMapOptionItem.clear();
            mMapOptionItem.putAll(mTempOptionItem);
            mTempOptionItem.clear();
        }
        if (mMapOptionItem.size() > 0) {

            for (Integer index : mMapOptionItem.keySet()) {
                option.append(mMapOptionItem.get(index).getOptionName()).append(",");
            }
            if (option.length() > 1 && option.toString().contains(","))
                option = new StringBuilder(option.substring(0, option.length() - 1));
            ((EditText) edtOption).setText(option.toString());

            for (int i = 0; i < listOptionItem.size(); i++) {
                if (mMapOptionItem.containsKey(i)) {
                    listOptionItem.get(i).setChecked(true);
                } else {
                    listOptionItem.get(i).setChecked(false);
                }

            }
        } else {
            for (int i = 0; i < listOptionItem.size(); i++) {
                listOptionItem.get(i).setChecked(false);
            }
        }
//        listExtraOption.get(parenPosition).setOptionsItems(listOptionItem);
    }

    private String createExtraOptionJson() {
        JSONArray jsonArray = new JSONArray();
        for (ExtraOptions extraOptions : listExtraOption) {
            JSONObject object = new JSONObject();
            try {
                object.put("parentId", extraOptions.getExtraID());
                StringBuilder optionSelected = new StringBuilder();
                for (OptionsItem optionsItem : extraOptions.getOptionsItems()) {
                    if (optionsItem.isChecked()) {
                        optionSelected.append(optionsItem.getOptionID()).append(",");
                    }
                }
                if (optionSelected.length() > 0 && optionSelected.toString().contains(",")) {
                    optionSelected = new StringBuilder(optionSelected.substring(0, optionSelected.length() - 1));
                }
                object.put("optionSelected", optionSelected.toString());
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        Log.e("kevin", "extra option" + jsonArray.toString());
        return jsonArray.toString();
    }

    private void asynAddProduct() {
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
                    addProduct();
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

    private boolean validate() {
        if (bitmapProduct == null) {
            showToast(getResources().getString(R.string.please_select_product_image));
            return false;
        }
        if (edtProductName.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_enter_product_name));
            edtProductName.requestFocus();
            return false;
        }
        if (edtProductCode.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_enter_product_code));
            edtProductCode.requestFocus();
            return false;
        }
        if (edtProductPrice.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_enter_price));
            edtProductPrice.requestFocus();
            return false;
        }
        if (edtProductCategories.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_select_category));
            edtProductCategories.requestFocus();
            return false;
        }
        return true;
    }

    private void addProduct() {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            String productName = edtProductName.getText().toString().trim();
            String productCode = edtProductCode.getText().toString().trim();
            String price = edtProductPrice.getText().toString().trim();
            String description = edtProductDescription.getText().toString().trim();
            String extraOption = createExtraOptionJson();

            product.setShopId(Integer.parseInt(shopId));
            product.setName(productName);
            product.setCode(productCode);
            product.setPrice(Double.parseDouble(price));
            product.setCategoryId(categoryId);
            product.setDescription(description);
            product.setStatus(status);
            product.setAvailable(available);
            thumbnailImage = productImage;

            ModelManager.addNewProduct(this, sharedPref.getUserInfo().getId(), product, extraOption, deleteIds, productImage, thumbnailImage, listFile, new ModelManagerListener() {
                @Override
                public void onError(VolleyError error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast(getResources().getString(R.string.have_error));
                        }
                    });
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    deleteRecursive(new File(rootFolder));
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
                                finish();
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

    @OnClick({R.id.btn_create_product, R.id.btn_save_product})
    void createProduct() {
        validateDataCreateProduct();
        if (validate()) {
            asynAddProduct();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_GALLERY_IMAGE_ONE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    Bitmap image = null;
                    Bitmap imageConvert = null;
                    try {
                        bitmapProduct = image = ImageUtil.decodeUri(this, selectedImage);
                        int dimension = getSquareCropDimensionForBitmap(image);
                        imageConvert = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                        productImage = ImageUtil.saveBitmap(this, image);
                        ivLogoProduct.setImageBitmap(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE_IMAGE_ONE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    bitmapProduct = imageBitmap;
                    int dimension = getSquareCropDimensionForBitmap(imageBitmap);
                    Bitmap imageConvert = ThumbnailUtils.extractThumbnail(imageBitmap, dimension, dimension);
                    try {
                        productImage = ImageUtil.saveBitmap(this, imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ivLogoProduct.setImageBitmap(imageBitmap);
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
                            product.getArrGalleries().add(banner);
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

    public int getSquareCropDimensionForBitmap(Bitmap bitmap) {
        return Math.min(bitmap.getWidth(), bitmap.getHeight());
    }

    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
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

    @OnClick(R.id.btn_product_info)
    void collapseProductInfo() {
        if (layoutProductInfo.getVisibility() == View.GONE) {
            layoutProductInfo.setVisibility(View.VISIBLE);
            ivProductInfoShow.setVisibility(View.VISIBLE);
            ivProductInfoHide.setVisibility(View.GONE);
        } else if (layoutProductInfo.getVisibility() == View.VISIBLE) {
            layoutProductInfo.setVisibility(View.GONE);
            ivProductInfoShow.setVisibility(View.GONE);
            ivProductInfoHide.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_extra_options)
    void collapseExtraOptions() {
        if (layoutExtraOptions.getVisibility() == View.GONE) {
            layoutExtraOptions.setVisibility(View.VISIBLE);
            ivExtraOptionsShow.setVisibility(View.VISIBLE);
            ivExtraOptionsHide.setVisibility(View.GONE);
        } else if (layoutExtraOptions.getVisibility() == View.VISIBLE) {
            layoutExtraOptions.setVisibility(View.GONE);
            ivExtraOptionsShow.setVisibility(View.GONE);
            ivExtraOptionsHide.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_product_categories)
    void collapseProductCategories() {
        if (layoutProductCategories.getVisibility() == View.GONE) {
            layoutProductCategories.setVisibility(View.VISIBLE);
            ivProductCategoriesShow.setVisibility(View.VISIBLE);
            ivProductCategoriesHide.setVisibility(View.GONE);
        } else if (layoutProductCategories.getVisibility() == View.VISIBLE) {
            layoutProductCategories.setVisibility(View.GONE);
            ivProductCategoriesShow.setVisibility(View.GONE);
            ivProductCategoriesHide.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.tv_available)
    void chooseAvailable() {
        available = "1";
        tvAvailable.setBackgroundResource(R.drawable.bg_border_grey);
        tvOutOfStock.setBackgroundResource(0);

    }

    @OnClick(R.id.tv_out_of_stock)
    void chooseOutOfStock() {
        available = "0";
        tvAvailable.setBackgroundResource(0);
        tvOutOfStock.setBackgroundResource(R.drawable.bg_border_grey);

    }

    @OnClick(R.id.tv_active)
    void chooseActive() {
        status = "1";
        tvActive.setBackgroundResource(R.drawable.bg_border_grey);
        tvInactive.setBackgroundResource(0);

    }

    @OnClick(R.id.tv_inactive)
    void chooseInactive() {
        status = "0";
        tvActive.setBackgroundResource(0);
        tvInactive.setBackgroundResource(R.drawable.bg_border_grey);

    }

    private void validateDataCreateProduct() {
        // shop name
        if (edtProductCode.getText().toString().trim().isEmpty()) {
            if (ivEmptyProductCode.getVisibility() == View.GONE) {
                ivEmptyProductCode.setVisibility(View.VISIBLE);
            }
            edtProductCode.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyProductCode.getVisibility() == View.VISIBLE) {
                ivEmptyProductCode.setVisibility(View.GONE);
            }
            edtProductCode.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // shop phone
        if (edtProductName.getText().toString().trim().isEmpty()) {
            if (ivEmptyProductName.getVisibility() == View.GONE) {
                ivEmptyProductName.setVisibility(View.VISIBLE);
            }
            edtProductName.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyProductName.getVisibility() == View.VISIBLE) {
                ivEmptyProductName.setVisibility(View.GONE);
            }
            edtProductName.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        if (edtProductPrice.getText().toString().trim().isEmpty()) {
            if (ivEmptyProductPrice.getVisibility() == View.GONE) {
                ivEmptyProductPrice.setVisibility(View.VISIBLE);
            }
            edtProductPrice.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyProductPrice.getVisibility() == View.VISIBLE) {
                ivEmptyProductPrice.setVisibility(View.GONE);
            }
            edtProductPrice.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }

        // shop categories
        if (edtProductCategories.getText().toString().trim().isEmpty()) {
            if (ivEmptyCategories.getVisibility() == View.GONE) {
                ivEmptyCategories.setVisibility(View.VISIBLE);
            }
            edtProductCategories.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_wrong));
        } else {
            if (ivEmptyCategories.getVisibility() == View.VISIBLE) {
                ivEmptyCategories.setVisibility(View.GONE);
            }
            edtProductCategories.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.bg_edit_text_normal));
        }
    }
}

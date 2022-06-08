package com.libyasolutions.libyamarketplace.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivityV2;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;

import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;

import com.libyasolutions.libyamarketplace.network.ProgressDialog;
import com.libyasolutions.libyamarketplace.object.Banner;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.util.ImageUtil;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.PermissionUtil;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class AddNewPostActivity extends BaseActivityV2 {

    public final int REQUEST_IMAGE_GALLERY_IMAGE_ONE = 0;
    public final int REQUEST_IMAGE_CAPTURE_IMAGE_ONE = 1;
    private final int CHOOSE_MORE_IMAGE_CODE = 22;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.iv_image)
    ImageView ivImage;

    @BindView(R.id.edt_description)
    EditText edtDescription;



    @BindView(R.id.tv_active)
    TextView tvActive;
    @BindView(R.id.tv_inactive)
    TextView tvInactive;


    @BindView(R.id.btn_create_product)
    Button btnCreateProduct;

    @BindView(R.id.layout_post_status)
    LinearLayout layoutPostStatus;

    ArrayList<String> mResults = new ArrayList<>();

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

    private Dialog confirmDeleteGalleryDialog;

    private int currentParentPosition, oldParentPosition;


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AddNewPostActivity.class));
    }

    @Override
    protected void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_new_post;
    }

    @Override
    protected void initData() {
        rootFolder = Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/images/";
        sharedPref = new MySharedPreferences(this);

        Intent intent = getIntent();
        shopId = intent.getStringExtra(Constant.SHOP_ID);
        product = intent.getParcelableExtra(Constant.PRODUCT_OBJ);


        if (product == null) {
            product = new Menu();
        } else {
            tvTitle.setText(getResources().getString(R.string.edit_post));
            btnCreateProduct.setText(getResources().getString(R.string.update));
            layoutPostStatus.setVisibility(View.VISIBLE);

            product = GlobalValue.currentProduct;
            fillData();
        }
        //setGalleryAdapter();
       //  getCategoryByShop();

        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
        }
    }

    private void fillData() {
//        Glide.with(this).load(product.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                bitmapProduct = resource;
//                ivLogoProduct.setImageBitmap(resource);
//            }
//        });

        oldSizeGallery = product.getArrGalleries().size();

        categoryId = String.valueOf(product.getCategoryId());

    //    edtProductDescription.setText(product.getDescription());




        if (product.getStatus().equals("1")) {
            status = "1";
            tvActive.setBackgroundResource(R.drawable.bg_border_grey);
            tvInactive.setBackgroundResource(R.drawable.bg_black_transparent);
        } else {
            status = "0";
            tvActive.setBackgroundResource(R.drawable.bg_black_transparent);
            tvInactive.setBackgroundResource(R.drawable.bg_border_grey);

        }

        setupLayout();

    }

    private void setupLayout() {
        // product info

    }

    @Override
    protected void configView() {
        // config banner
        for (Banner banner : product.getArrGalleries()) {
            Log.e("kevin", "banner image: " + banner.getImage());
        }

     // Todo: Bind the data here


    }

    private void getCategoryByShop() {
//        if (!NetworkUtil.checkNetworkAvailable(this)) {
//            Toast.makeText(this, R.string.message_network_is_unavailable, Toast.LENGTH_LONG).show();
//        }
        //else
          /*  ModelManager.getAllCategoryByShop(this, Integer.parseInt(shopId), true,
                    new ModelManagerListener() {

                        @Override
                        public void onSuccess(Object object) {
                            String json = (String) object;

//                            if (ParserUtility.parseListCategories(json).size() == 0) {
//                                showToast(getResources().getString(R.string.have_no_date));
//                            }

                        }

                        @Override
                        public void onError(VolleyError error) {
                            Toast.makeText(AddNewPostActivity.this, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                        }
                    });*/
    }


    @OnClick(R.id.iv_back)
    void chooseBack() {
        onBackPressed();
    }

    @OnClick(R.id.iv_add_logo)
    void addLogoProduct() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.select_photo_from)
                .setPositiveButton(R.string.gallery,  (arg0, arg1) -> {
                            // one can be replaced with any action code
                            if (PermissionUtil.checkReadWriteStoragePermission(AddNewPostActivity.this)) {
                            //    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                try {
                                    Intent pickPhoto = new Intent();
                                    pickPhoto.setType("image/*");
                                    pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
                                    //startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY_IMAGE_ONE);
                                    launchGallaryActivity.launch(pickPhoto);
                                } catch (Exception e) {
                                    showToast(e.getMessage());
                                }


                            }
                        })
                .setNegativeButton(R.string.camera, (arg0, arg1) -> {
                            // zero can be replaced with any action code
                            if (PermissionUtil.checkCameraPermission(AddNewPostActivity.this)) {
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_IMAGE_ONE);
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

    ActivityResultLauncher<Intent> launchGallaryActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                             ivImage.setImageBitmap(selectedImageBitmap);

                          } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

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
/*
        if (edtProductName.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_enter_product_name));
            edtProductName.requestFocus();
            return false;
        }
*/

        return true;
    }

    private void addProduct() {
       /* if (NetworkUtil.checkNetworkAvailable(this)) {
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
        }*/
    }

    @OnClick({R.id.btn_create_product})
    void createProduct() {
       // if (validate()) {
       //     asynAddProduct();
      //  }
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
                        if(selectedImage != null) {
                            bitmapProduct = image = ImageUtil.decodeUri(this, selectedImage);
                            int dimension = getSquareCropDimensionForBitmap(image);
                            imageConvert = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                            productImage = ImageUtil.saveBitmap(this, image);

                            ivImage.setImageURI(selectedImage);
                        }
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
                    ivImage.setImageBitmap(imageBitmap);
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

}

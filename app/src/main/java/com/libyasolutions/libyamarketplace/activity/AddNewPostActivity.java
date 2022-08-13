package com.libyasolutions.libyamarketplace.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
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
import androidx.core.content.FileProvider;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.libyasolutions.libyamarketplace.BaseActivityV2;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.addpost.ModelPostAdded;
import com.libyasolutions.libyamarketplace.activity.addpost.ModelPosts;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;

import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;

import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.network.ProgressDialog;
import com.libyasolutions.libyamarketplace.object.Banner;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.util.ImageUtil;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.PermissionUtil;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


public class AddNewPostActivity extends BaseActivityV2 {

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

    private String rootFolder;
    private MySharedPreferences sharedPref;

    private ProgressDialog pDialog;

    private String shopId;
    private String postId;
    private String status = "1";

     private File productImage;

    public static ModelPosts.Data.Post post;


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

        shopId = getIntent().getStringExtra(Constant.SHOP_ID);

        if (null != post) {
            postId = getIntent().getStringExtra(Constant.POST_ID);

            tvTitle.setText(getResources().getString(R.string.edit_post));
            btnCreateProduct.setText(getResources().getString(R.string.update));
            layoutPostStatus.setVisibility(View.VISIBLE);

            setupLayout();
        }

        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(false);
        }
    }


    private void setupLayout() {
        // post info
        Glide.with(this).load(post.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ivImage.setImageBitmap(resource);
            }
        });

        if (null != post.getStatus() && post.getStatus().equalsIgnoreCase("1")) {
            status = "1";
            tvActive.setBackgroundResource(R.drawable.background_border_green);
            tvInactive.setBackgroundResource(R.drawable.bg_border_grey);
        } else {
            status = "0";
            tvActive.setBackgroundResource(R.drawable.bg_border_grey);
            tvInactive.setBackgroundResource(R.drawable.background_border_green);
        }

        edtDescription.setText(post.getDescription() + "");

    }

    @Override
    protected void configView() {


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
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            ivImage.setImageBitmap(selectedImageBitmap);

                           // productImage = new File(getPath(selectedImageUri));
                            Uri tempUri = getImageUri(getApplicationContext(), selectedImageBitmap);
                            productImage = new File(getRealPathFromURI(tempUri));
                          } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

   private boolean validate() {
//        if (post == null && productImage == null) {
//            showToast(getResources().getString(R.string.please_select_product_image));
//            return false;
//        }

        if (edtDescription.getText().toString().trim().length() == 0) {
            showToast(getString(R.string.enter_post_desc));
            edtDescription.requestFocus();
            return false;
        }
        return true;
    }

    private void addPost() {
      if (NetworkUtil.checkNetworkAvailable(this)) {
            String description = edtDescription.getText().toString().trim();

            ModelManager.addNewPost(postId, shopId, GlobalValue.myAccount.getId(),description, status, productImage, new ModelManagerListener() {
                @Override
                public void onError(VolleyError error) {
                    runOnUiThread(() -> showToast(getResources().getString(R.string.have_error)));
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                   deleteRecursive(new File(rootFolder));
                }

                @Override
                public void onSuccess(Object object) {
                    if (pDialog.isShowing()) { pDialog.dismiss(); }
                    final String json = (String) object;
                    ModelPostAdded postAdded = new Gson().fromJson(json,ModelPostAdded.class);
                    if(postAdded != null) {
                        showToast("" + postAdded.getMessage());

                        if(null!= postAdded.getStatus() && postAdded.getStatus().equalsIgnoreCase("success")){
                         finish();

                         deleteRecursive(new File(rootFolder));
                         Intent intent = new Intent();
                         intent.setAction(Constant.REFRESH_DATA);
                         sendBroadcast(intent);
                       }

                    }

               }
            });
        } else {
            runOnUiThread(() -> showToast(getResources().getString(R.string.no_connection)));
        }
    }

    @OnClick({R.id.btn_create_product})
    void createProduct() {
         if (validate()) {
             if (NetworkUtil.checkNetworkAvailable(this)) {
                 if (!pDialog.isShowing()) {
                     pDialog.show();
                 }
                 addPost();
             } else {
                 showToast(getResources().getString(R.string.no_connection));
             }
       }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
           case REQUEST_IMAGE_CAPTURE_IMAGE_ONE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                 //   int dimension = getSquareCropDimensionForBitmap(imageBitmap);
                  //  Bitmap imageConvert = ThumbnailUtils.extractThumbnail(imageBitmap, dimension, dimension);
                    try {
                     //   productImage = ImageUtil.saveBitmap(this, imageBitmap);
                        ivImage.setImageBitmap(imageBitmap);
                        Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
                        productImage = new File(getRealPathFromURI(tempUri));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
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
        tvActive.setBackgroundResource(R.drawable.background_border_green);
        tvInactive.setBackgroundResource(R.drawable.bg_border_grey);

    }

    @OnClick(R.id.tv_inactive)
    void chooseInactive() {
        status = "0";
        tvActive.setBackgroundResource(R.drawable.bg_border_grey);
        tvInactive.setBackgroundResource(R.drawable.background_border_green);

    }

}

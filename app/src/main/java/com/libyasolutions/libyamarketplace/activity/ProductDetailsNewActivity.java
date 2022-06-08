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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.libyasolutions.libyamarketplace.adapter.CommentNewAdapter;
import com.libyasolutions.libyamarketplace.adapter.OptionsAdapter;
import com.libyasolutions.libyamarketplace.adapter.OptionsAdapterDialog;
import com.libyasolutions.libyamarketplace.adapter.SimilarFoodAdapterNew;
import com.libyasolutions.libyamarketplace.adapter.SingleSelectItemAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.config.WebServiceConfig;
import com.libyasolutions.libyamarketplace.fragment.BannerFragment;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Banner;
import com.libyasolutions.libyamarketplace.object.Comment;
import com.libyasolutions.libyamarketplace.object.ExtraOptions;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.OptionsItem;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.responses.ModelSingleItem;
import com.libyasolutions.libyamarketplace.responses.ProductDetailResponse;
import com.libyasolutions.libyamarketplace.util.CustomToast;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.StringUtility;
import com.libyasolutions.libyamarketplace.widget.CirclePageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailsNewActivity extends BaseActivity {

    private Menu product;
    private Shop shop;
    private ImageView btnBack, btnAddtoCard;
    private LinearLayout ivAddToCart;
    private TextView tvNameFood,tvShopName, tvDescription, tvTotalComment, tvPrice,tvPrice2, tvRating;
    private ImageView tvBtnAddcomment;
    private String productId = "0";
    private RecyclerView recyclerView, rclViewSimilar,recycleColors,recycleSize;
    private boolean isFastSearch = false;
    private int page = 1, totalPage = 1;
    private ArrayList<Comment> mArrComment = new ArrayList<>();
    private CommentNewAdapter mCommentAdapter;
    private CirclePageIndicator indicatorBannerImages;
    private ViewPager viewPager;
    private ArrayList<Banner> arrGalleries;
    FragmentPagerAdapter pagerAdapter;
    private CircleImageView imgShop;
    private ImageView ivChat;
    private RecyclerView rclOptions;
    private OptionsAdapter optionsAdapter;
    private ImageView imgOption;
    private Dialog mDialog;
    private RelativeLayout rltOption;
    private Dialog loginDialog;
    private boolean checkAddCard;
    private TextView  tvCode;
    private LinearLayout lLPhone;
    String phoneNumber = "";
    private SingleSelectItemAdapter mSizeAdap, mColorAdapt;
    private RelativeLayout rlAddFav;
    private ImageView imgFav;
    private ImageView three_dots;
    private LinearLayout mapBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        self = this;
        NetworkUtil.enableStrictMode();
        setContentView(R.layout.food_details_new);
        initUI();
        initData();
        initControls();
    }

    private void initControls() {
        imgOption.setOnClickListener(v -> {
            if (GlobalValue.myAccount == null) {
                showDialogLogin();
            } else {
                if (product.getExtraOptions().size() > 0) {
                    checkAddCard = false;
                    showDialogOption(product.getExtraOptions());
                } else {
                    showToastMessage(getResources().getString(R.string.have_no_option_for_this_product));
                }
            }
        });
        btnBack.setOnClickListener(v -> onBackPressed());
        ivChat.setOnClickListener(v -> {
            if (GlobalValue.myAccount != null) {
                Intent intent = new Intent(self, ChatDetailActivity.class);
                intent.putExtra("idAgent", product.getShopOwnerId());
                intent.putExtra("title", product.getShop().getShopName());
                intent.putExtra("image", product.getShopOwnerImage());
                intent.putExtra(Constant.SHOP_ID, product.getShopId() + "");
                intent.putExtra(Constant.SHOP_NAME, product.getShop().getShopName());
                intent.putExtra(Constant.COUNT_SHOP, product.getShop().getCountShop() + "");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
            } else {
                showDialogLogin();
            }
        });
        imgShop.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(GlobalValue.KEY_SHOP_ID, product.getShop().getShopId());
            Intent intent = new Intent(ProductDetailsNewActivity.this, ShopDetailsNew.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        lLPhone.setOnClickListener(view -> {
            if (!phoneNumber.isEmpty()) {
                Uri link = Uri.parse("tel: " + phoneNumber);
                startActivity(new Intent(Intent.ACTION_DIAL, link));
            } else {
                showToastMessage(R.string.phone_number_empty);
            }
        });
        rlAddFav.setOnClickListener(view -> { onClickFavourite(); });

        three_dots.setOnClickListener(view -> {
         gotoActivity(FeedBackActivity.class);
        });

        mapBtn.setOnClickListener(view -> { gotoMapDetailActivity(product.getShop());  });
    }

    private void showAddToBasketDialog() {
        Dialog rateDialog = new Dialog(this);
        rateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rateDialog.setContentView(R.layout.select_the_option);
        rateDialog.getWindow().setGravity(Gravity.BOTTOM);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        self.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(rateDialog.getWindow().getAttributes());
        lp.width = 6 * (displaymetrics.widthPixels / 7);
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        rateDialog.getWindow().setAttributes(lp);
        rateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvCount = (TextView) rateDialog.findViewById(R.id.tvCount);
        TextView tvTitle = (TextView) rateDialog.findViewById(R.id.tvTitle);
        RelativeLayout rlAdd = rateDialog.findViewById(R.id.rl_add);
        RelativeLayout rlMinus = rateDialog.findViewById(R.id.rl_minus);
        RelativeLayout tvConfirm = rateDialog.findViewById(R.id.rl_submit);
        RelativeLayout rlTitle = rateDialog.findViewById(R.id.select_option);


        if(mSizeAdap.isItemSelected()){
            rlTitle.setBackgroundResource(R.drawable.background_border_green);
            tvTitle.setTextColor(getResources().getColor(R.color.green));
            tvTitle.setText(getResources().getString(R.string.option_has_been_selected));

            tvConfirm.setBackgroundResource(R.drawable.blue_fill_round_gradient);
        }

        rlAdd.setOnClickListener( v -> {
            int aC = Integer.parseInt(tvCount.getText().toString().trim());
          tvCount.setText(String.valueOf(++aC));

        });
        rlMinus.setOnClickListener( v -> {
            int aM = Integer.parseInt(tvCount.getText().toString().trim());
            if(aM>1) tvCount.setText(String.valueOf(--aM));
        });

        tvConfirm.setOnClickListener( v -> {
            if(mSizeAdap.isItemSelected()){
                addToCart(product);
            }
            rateDialog.dismiss();
        });

        rateDialog.show();
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
                        imgFav.setImageResource(R.drawable.ic_heart_b);
                    else
                        imgFav.setImageResource(R.drawable.heart_blue_outline_icon);
                }
            });
        } else {
            showDialogLogin();
        }
    }


    private void showDialogOption(final ArrayList<ExtraOptions> extraOptions) {
        for (ExtraOptions extraOptions1 : extraOptions) {
            for (OptionsItem optionsItem : extraOptions1.getOptionsItems()) {
                optionsItem.setChecked(false);
            }
        }
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_options);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        self.getWindowManager().getDefaultDisplay() .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = 6 * (displaymetrics.widthPixels / 7);
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView tvCancel = mDialog.findViewById(R.id.tvCancel);
        TextView tvConfirm = mDialog.findViewById(R.id.tvConfirm);

        RecyclerView rclOptions = mDialog.findViewById(R.id.rclOptions);
        OptionsAdapterDialog optionsAdapterDialog = new OptionsAdapterDialog(self, extraOptions);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(self);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rclOptions.setLayoutManager(linearLayoutManager);
        rclOptions.setAdapter(optionsAdapterDialog);


        tvConfirm.setOnClickListener(v -> {
            if (checkAddCard) {
                addToCart(product);
            }
            mDialog.dismiss();
        });
        tvCancel.setOnClickListener(v -> mDialog.dismiss());

        if (mDialog != null) {
            mDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!productId.equalsIgnoreCase("0")) {
            clearData();
            getProductDetails(productId);
            getComments(productId);
        }
    }

    private void initUI() {
        //header :
        mapBtn = findViewById(R.id.direction_ly);
        three_dots = findViewById(R.id.three_dots);
        imgFav = findViewById(R.id.imgFav);
        rlAddFav = findViewById(R.id.product_save_img_lay);
        rltOption = findViewById(R.id.rltOption);
        imgOption = findViewById(R.id.imgOption);
        rclOptions = findViewById(R.id.rclOptions);
        imgShop = findViewById(R.id.imgShop);
        ivChat = findViewById(R.id.iv_chat);
        indicatorBannerImages = findViewById(R.id.indicatorBannerImages);
        btnBack = findViewById(R.id.btnBack);
        btnAddtoCard = findViewById(R.id.btnAddToCart);
        ivAddToCart = findViewById(R.id.ivAddToCart);
        tvNameFood = findViewById(R.id.tvNameFood);
        tvShopName = findViewById(R.id.tvshopName);

        tvDescription = findViewById(R.id.tvDescription);
        tvTotalComment = findViewById(R.id.tvTotalComment);
        tvBtnAddcomment = findViewById(R.id.tvBtnAddcomment);
        recyclerView = findViewById(R.id.rclView);
        rclViewSimilar = findViewById(R.id.rclViewSimilar);

        recycleColors = findViewById(R.id.color_recycler);
        recycleSize = findViewById(R.id.size_recycler);

        tvPrice = findViewById(R.id.tvPrice);
        tvPrice2 = findViewById(R.id.price_text_digit);
        tvRating = findViewById(R.id.rating_text_digit);

        lLPhone = findViewById(R.id.call_ly);
        tvCode = findViewById(R.id.tvCode);

        mCommentAdapter = new CommentNewAdapter(ProductDetailsNewActivity.this, mArrComment);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mCommentAdapter);

        viewPager = findViewById(R.id.viewPager);
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
            indicatorBannerImages.setStrokeColor(ContextCompat.getColor(ProductDetailsNewActivity.this, R.color.gray_light));
            indicatorBannerImages.setPageColor(ContextCompat.getColor(ProductDetailsNewActivity.this, R.color.gray));
            indicatorBannerImages.setFillColor(ContextCompat.getColor(ProductDetailsNewActivity.this, R.color.blue_button));
        } else {
            indicatorBannerImages.setStrokeColor(ProductDetailsNewActivity.this.getResources().getColor(R.color.gray_light));
            indicatorBannerImages.setPageColor(ProductDetailsNewActivity.this.getResources().getColor(R.color.gray));
            indicatorBannerImages.setFillColor(ProductDetailsNewActivity.this.getResources().getColor(R.color.blue_button));
        }

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
            if (arrGalleries != null)
                return arrGalleries.size();
            return 0;
        }
    }

    private void getProductDetails(final String productId) {
        ModelManager.getFoodById(self, productId, true,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                        String json = (String) object;
                        product = ParserUtility.parseFood(json);
                        ProductDetailResponse shopDetailResponse = new Gson().fromJson(json, ProductDetailResponse.class);
                        Log.e("prod_details: ","_  "+ shopDetailResponse.status);

                        rclViewSimilar.setHasFixedSize(true);
                        rclViewSimilar.setLayoutManager(new LinearLayoutManager(ProductDetailsNewActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        SimilarFoodAdapterNew shopAdapterNew = new SimilarFoodAdapterNew(rclViewSimilar, ProductDetailsNewActivity.this, shopDetailResponse.data.similarFood);
                        rclViewSimilar.setAdapter(shopAdapterNew);

                        // set color and size adapter
                        recycleColors.setHasFixedSize(true);
                        recycleSize.setHasFixedSize(true);

                        ArrayList<ModelSingleItem> mColors = new ArrayList();
                        mColors.add(new ModelSingleItem("S",false));
                        mColors.add(new ModelSingleItem("M",false));
                        mColors.add(new ModelSingleItem("L",false));
                        mColors.add(new ModelSingleItem("XL",false));

                        ArrayList<ModelSingleItem> mSizes = new ArrayList();
                        mSizes.add(new ModelSingleItem("Black",true));
                        mSizes.add(new ModelSingleItem("Blue",false));
                        mSizes.add(new ModelSingleItem("Red",false));
                        mSizes.add(new ModelSingleItem("White",false));

                        mColorAdapt = new SingleSelectItemAdapter(ProductDetailsNewActivity.this, mSizes);
                        mSizeAdap = new SingleSelectItemAdapter(ProductDetailsNewActivity.this, mColors);

                        recycleColors.setAdapter(mColorAdapt);
                        recycleSize.setAdapter(mSizeAdap);

                        if (product != null) {
                            showProductDetails(product);
                            btnAddtoCard.setOnClickListener(v -> {  sendAction(Constant.SHOW_TAB_CART);  });
                            ivAddToCart.setOnClickListener(v -> {
                                if (GlobalValue.myAccount == null) {
                                    showDialogLogin();
                                } else {
                                    checkAddCard = true;
                                    if (!checkShopIsDifference()) {
                                        showAddToBasketDialog();
                                    } else {
                                        showToastMessage(getResources().getString(R.string.just_can_order_from_one_restaurant));
                                    }

                                }

                            });
                            tvBtnAddcomment.setOnClickListener(v -> {
                                if (GlobalValue.myAccount == null) {
                                    showDialogLogin();
                                } else {
                                    showDialogRating();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendAction(String action) {
        Intent intent = new Intent(this, MainTabActivity.class);
        intent.setAction(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
    }
    private void showProductDetails(Menu product) {
        if (product.getExtraOptions().size() > 0) {
            rltOption.setVisibility(View.VISIBLE);
        } else {
            rltOption.setVisibility(View.GONE);
        }
        arrGalleries = product.getArrGalleries();
        setBanner();

        Glide.with(this).load(product.getShop().getImage()).into(imgShop);

        tvNameFood.setText(product.getName());
        tvShopName.setText(product.getShop().getShopName());

        tvTotalComment.setText(StringUtility.replaceArabicNumbers(String.valueOf(product.getRateNumber())));
        tvDescription.setText(product.getDescription());
        tvRating.setText("" + Float.parseFloat(Math.floor(product.getRateValue() / 4) + ""));

         tvPrice.setText(product.getPrice() + " " + getString(R.string.currency));
         tvPrice2.setText(product.getPrice() + " " + getString(R.string.currency));

         phoneNumber = product.getShop().getPhone();
         tvCode.setText(product.getCode());

        if (product.isFavourite()) imgFav.setImageResource(R.drawable.ic_heart_b);
        else imgFav.setImageResource(R.drawable.heart_blue_outline_icon);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclOptions.setLayoutManager(linearLayoutManager);
        optionsAdapter = new OptionsAdapter(getBaseContext(), product.getExtraOptions());
        rclOptions.setAdapter(optionsAdapter);
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

    private boolean checkShopIsDifference() {
        if (GlobalValue.arrMyMenuShop == null) {
            GlobalValue.arrMyMenuShop = new ArrayList<Shop>();
        }
        if (product.getShop() != null) {
            for (int i = 0; i < GlobalValue.arrMyMenuShop.size(); i++) {
                Shop item = GlobalValue.arrMyMenuShop.get(i);
                if (item.getShopId() != product.getShop().getShopId()) {
                    return true;
                }
            }
        }
        return false;
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
                        if (menu.getExtraOptions() != null && food.getExtraOptions() != null) {
                            String a = createOptionString(menu.getExtraOptions());
                            String b = createOptionString(food.getExtraOptions());
                            if (a.equals(b)) {
                                isExistedMenu = true;
                            }
                        }
                    }
                }

                if (isExistedMenu) {
                    CustomToast.showCustomAlert(this, getResources().getString(R.string.item_is_existed_my_cart), Toast.LENGTH_SHORT);
//                    onBackPressed();
                } else {
                    if (food.getExtraOptions().size() == 0) {
                        food.setOrderNumber(1);
                        GlobalValue.arrMyMenuShop.get(shopIndex).addFoodOrder(food);
                        CustomToast.showCustomAlert(this, getResources().getString(R.string.add_to_cart), Toast.LENGTH_SHORT);

                        if (isFastSearch) {
                         //   onBackPressed();
                        } else {
                            gotoShopDetail(true);
                        }
                        Intent intent = new Intent();
                        intent.setAction(Constant.GET_CART_INFO);
                        sendBroadcast(intent);
                    } else {
                        String checkOptiom2 = createOptionString(food.getExtraOptions());
                        if (!checkOptiom2.equals("")) {
                            food.setOrderNumber(1);
                            GlobalValue.arrMyMenuShop.get(shopIndex).addFoodOrder(food);
                            CustomToast.showCustomAlert(this, getResources().getString(R.string.add_to_cart), Toast.LENGTH_SHORT);

                            if (isFastSearch) {
                           //     onBackPressed();
                            } else {
                                gotoShopDetail(true);
                            }
                            Intent intent = new Intent();
                            intent.setAction(Constant.GET_CART_INFO);
                            sendBroadcast(intent);
                        } else {
                            showDialogOption(food.getExtraOptions());
                        }
                    }
                }
            } else {
                String checkOptiom = createOptionString(food.getExtraOptions());
                if (food.getExtraOptions().size() == 0) {
                    food.setOrderNumber(1);
                    shop = food.getShop();
                    shop.addFoodOrder(food);
                    GlobalValue.arrMyMenuShop.add(shop);
                    CustomToast.showCustomAlert(self, getResources().getString(R.string.add_to_cart), Toast.LENGTH_SHORT);
                    if (isFastSearch) {
                       // onBackPressed();
                    } else {
                        gotoShopDetail(true);
                    }
                    Intent intent = new Intent();
                    intent.setAction(Constant.GET_CART_INFO);
                    sendBroadcast(intent);
                } else {
                    if (!checkOptiom.equals("")) {
                        food.setOrderNumber(1);
                        shop = food.getShop();
                        shop.addFoodOrder(food);
                        GlobalValue.arrMyMenuShop.add(shop);
                        CustomToast.showCustomAlert(self, getResources().getString(R.string.add_to_cart), Toast.LENGTH_SHORT);
                        if (isFastSearch) {
                        //    onBackPressed();
                        } else {
                            gotoShopDetail(true);
                        }
                        Intent intent = new Intent();
                        intent.setAction(Constant.GET_CART_INFO);
                        sendBroadcast(intent);
                    } else {
                        showDialogOption(food.getExtraOptions());
                    }
                }
            }
        } else {
            CustomToast.showCustomAlert(self, "Have error! Please try again!",
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

    private void getComments(String id) {
        ModelManager.getFoodsComments(self, id, page, false,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        totalPage = ParserUtility.parseTotalPage(json);
                        mArrComment.addAll(ParserUtility.parseComments(json));
                        mCommentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private void clearData() {
        if (mArrComment.size() > 0) {
            mArrComment.clear();
        }
        page = 1;
    }

    private void loadmoreData() {
        page += 1;
    }

    private void showDialogRating() {
        Dialog  rateDialog = new Dialog(this);
        rateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rateDialog.setContentView(R.layout.rating_dialog);
        rateDialog.getWindow().setGravity(Gravity.BOTTOM);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        self.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(rateDialog.getWindow().getAttributes());
        lp.width = 6 * (displaymetrics.widthPixels / 7);
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        rateDialog.getWindow().setAttributes(lp);
        rateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RatingBar rateBar = (RatingBar) rateDialog.findViewById(R.id.rating_bar);
        EditText edtReview = (EditText) rateDialog.findViewById(R.id.edtReview);
        RelativeLayout tvConfirm = (RelativeLayout) rateDialog.findViewById(R.id.rl_submit);

        tvConfirm.setOnClickListener( v -> {
            String edText = edtReview.getText().toString().trim();

            // call submit api
            if (!NetworkUtil.checkNetworkAvailable(this)) {
                Toast.makeText(this, R.string.message_network_is_unavailable, Toast.LENGTH_LONG).show();
            } else {
                addReview(rateBar, edText);
            }

            rateDialog.dismiss();
        });

        rateDialog.show();
    }
    private String mUser = "";
    private void addReview(RatingBar rateBar, String edText) {
        // Get values.
        if (GlobalValue.myAccount != null) {
            mUser = GlobalValue.myAccount.getUserName();
        }

        String mRate = (rateBar.getProgress() * 2) + "";

        // Call add api.
        if (edText.isEmpty()) { edText = ""; }

        ModelManager.addFoodReview(self, String.valueOf(product.getShopId()),  String.valueOf(product.getId()), mRate, mUser,
                edText, true, new ModelManagerListener() {

                    @Override
                    public void onError(VolleyError error) {
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(Object object) {
                        try {
                            String json = (String) object;
                            if (!json.isEmpty()) {
                                JSONObject obj = new JSONObject(json);
                                if (obj.getString(WebServiceConfig.KEY_STATUS).equals(WebServiceConfig.KEY_STATUS_SUCCESS)) {
                                    Toast.makeText( self,  R.string.message_adding_new_comment_successfully, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(
                                            self,
                                            R.string.error_adding_new_comment,
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(
                                        self,
                                        R.string.error_adding_new_comment,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
        // }
    }


    private String createOptionString(ArrayList<ExtraOptions> listExtraOption) {
        StringBuilder option = new StringBuilder();
        for (ExtraOptions extraOption : listExtraOption) {
            for (OptionsItem optionItem : extraOption.getOptionsItems()) {
                if (optionItem.isChecked()) {
                    option.append(optionItem.getParentName()).append(":").append(optionItem.getOptionName()).append(",");
                    break;
                }
            }
        }
        if (option.length() > 1) {
            option = new StringBuilder(option.substring(0, option.length() - 1));
        }
        Log.e("kevin", "createOptionString: " + option);
        return option.toString();
    }


}

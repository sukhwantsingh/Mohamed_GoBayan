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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.LoginActivity;
import com.libyasolutions.libyamarketplace.adapter.CommentNewAdapter;
import com.libyasolutions.libyamarketplace.adapter.OptionsAdapter;
import com.libyasolutions.libyamarketplace.adapter.OptionsAdapterDialog;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
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
import com.libyasolutions.libyamarketplace.util.CustomToast;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.StringUtility;
import com.libyasolutions.libyamarketplace.widget.CirclePageIndicator;

import java.util.ArrayList;

public class ProductDetailsNewActivity extends BaseActivity {

    private Menu product;
    private Shop shop;
    private ImageView btnBack, btnAddtoCard, imgFood, ivAddToCart;
    private TextView tvNameFood, tvDescription, tvTotalComment, tvPrice;
    private ImageView tvBtnAddcomment;
    private RatingBar rtbRating;
    private String productId = "0";
    private RecyclerView recyclerView;
    private boolean isFastSearch = false;
    private int page = 1, totalPage = 1;
    private ArrayList<Comment> mArrComment = new ArrayList<>();
    private CommentNewAdapter mCommentAdapter;
    private CirclePageIndicator indicatorBannerImages;
    private ViewPager viewPager;
    private ArrayList<Banner> arrGalleries;
    FragmentPagerAdapter pagerAdapter;
    private ImageView imgShop;
    private ImageView ivChat;
    private RecyclerView rclOptions;
    private OptionsAdapter optionsAdapter;
    private ImageView imgOption;
    private Dialog mDialog;
    private RelativeLayout rltOption;
    private Dialog loginDialog;
    private boolean checkAddCard;
    private TextView tvPhone, tvCode;


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
        imgOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        imgShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(GlobalValue.KEY_SHOP_ID, product.getShop().getShopId());
                Intent intent = new Intent(ProductDetailsNewActivity.this, ShopDetailsNew.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = tvPhone.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    Uri link = Uri.parse("tel: " + phoneNumber);
                    startActivity(new Intent(Intent.ACTION_DIAL, link));
                } else {
                    showToastMessage(R.string.phone_number_empty);
                }
            }
        });
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
        self.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
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


        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAddCard) {
                    addToCart(product);
                }
                mDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

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
        rltOption = findViewById(R.id.rltOption);
        imgOption = findViewById(R.id.imgOption);
        rclOptions = findViewById(R.id.rclOptions);
        imgShop = findViewById(R.id.imgShop);
        ivChat = findViewById(R.id.iv_chat);
        indicatorBannerImages = findViewById(R.id.indicatorBannerImages);
        btnBack = findViewById(R.id.btnBack);
        btnAddtoCard = findViewById(R.id.btnAddToCart);
        ivAddToCart = findViewById(R.id.ivAddToCart);
        imgFood = findViewById(R.id.imgFood);
        tvNameFood = findViewById(R.id.tvNameFood);
        tvNameFood.setSelected(true);

        tvDescription = findViewById(R.id.tvDescription);
        tvTotalComment = findViewById(R.id.tvTotalComment);
        tvBtnAddcomment = findViewById(R.id.tvBtnAddcomment);
        rtbRating = findViewById(R.id.rtbRating);
        recyclerView = findViewById(R.id.rclView);
        tvPrice = findViewById(R.id.tvPrice);

        tvPhone = findViewById(R.id.tvPhone);
        tvCode = findViewById(R.id.tvCode);

        mCommentAdapter = new CommentNewAdapter(ProductDetailsNewActivity.this, mArrComment);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
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
            indicatorBannerImages.setFillColor(ContextCompat.getColor(ProductDetailsNewActivity.this, R.color.red));
        } else {
            indicatorBannerImages.setStrokeColor(ProductDetailsNewActivity.this.getResources().getColor(R.color.gray_light));
            indicatorBannerImages.setPageColor(ProductDetailsNewActivity.this.getResources().getColor(R.color.gray));
            indicatorBannerImages.setFillColor(ProductDetailsNewActivity.this.getResources().getColor(R.color.red));
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
                        if (product != null) {
                            showProductDetails(product);
                            btnAddtoCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (GlobalValue.myAccount == null) {
                                        showDialogLogin();
                                    } else {
                                        checkAddCard = true;
                                        if (!checkShopIsDifference()) {
                                            addToCart(product);
                                        } else {
                                            showToastMessage(getResources().getString(R.string.just_can_order_from_one_restaurant));
                                        }
                                    }
                                }
                            });
                            ivAddToCart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (GlobalValue.myAccount == null) {
                                        showDialogLogin();
                                    } else {
                                        checkAddCard = true;
                                        if (!checkShopIsDifference()) {
                                            addToCart(product);
                                        } else {
                                            showToastMessage(getResources().getString(R.string.just_can_order_from_one_restaurant));
                                        }
                                    }

                                }
                            });
                            tvBtnAddcomment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (GlobalValue.myAccount == null) {
                                        showDialogLogin();
                                    } else {
                                        Bundle b = new Bundle();
                                        b.putString(GlobalValue.KEY_SHOP_ID, product.getShopId() + "");
                                        b.putString(GlobalValue.KEY_FOOD_ID, product.getId() + "");
                                        gotoActivity(ProductDetailsNewActivity.this, AddReviewActivity.class, b);
                                    }
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
        tvTotalComment.setText(StringUtility.replaceArabicNumbers(String.valueOf(product.getRateNumber())) + " " + getResources().getString(R.string.reviews));
        tvDescription.setText(product.getDescription());
        rtbRating.setMax(5);
        rtbRating.setRating(Float.parseFloat(Math
                .floor(product.getRateValue() / 4) + ""));
        tvPrice.setText(String.valueOf(product.getPrice()) + " " + getString(R.string.currency));
        tvPhone.setText(product.getShop().getPhone());
        tvCode.setText(product.getCode());
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
                    CustomToast.showCustomAlert(this, getResources().getString(R.string.item_is_existed_my_cart),
                            Toast.LENGTH_SHORT);
                    onBackPressed();
                } else {
                    if (food.getExtraOptions().size() == 0) {
                        food.setOrderNumber(1);
                        GlobalValue.arrMyMenuShop.get(shopIndex).addFoodOrder(food);
                        CustomToast.showCustomAlert(this, getResources().getString(R.string.add_to_cart),
                                Toast.LENGTH_SHORT);

                        if (isFastSearch) {
                            onBackPressed();
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
                            CustomToast.showCustomAlert(this, getResources().getString(R.string.add_to_cart),
                                    Toast.LENGTH_SHORT);

                            if (isFastSearch) {
                                onBackPressed();
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
                        onBackPressed();
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
                            onBackPressed();
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

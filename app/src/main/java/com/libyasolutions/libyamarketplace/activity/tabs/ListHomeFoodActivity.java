package com.libyasolutions.libyamarketplace.activity.tabs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.libyasolutions.libyamarketplace.BaseActivityV2;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ListFoodAdapterNew;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.ConstantApp;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.dialog.CityIdDialog;
import com.libyasolutions.libyamarketplace.dialog.DepartmentDialog;
import com.libyasolutions.libyamarketplace.dialog.DistanceDialog;
import com.libyasolutions.libyamarketplace.dialog.SortByDialog;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.object.CategorySearch;
import com.libyasolutions.libyamarketplace.object.Conversation;
import com.libyasolutions.libyamarketplace.object.DepartmentCategory;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.util.GPSTracker;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ListHomeFoodActivity extends BaseActivityV2
        implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    public static final int DELAY_MILLIS_2000 = 2000;

    @BindView(R.id.iv_sort)
    ImageView ivSort;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.tv_city_name)
    TextView tvCityName;
    @BindView(R.id.container_choose_cities)
    ConstraintLayout containerSearchByChooseCities;
    @BindView(R.id.btn_search_by_all_cities)
    Button btnSearchByAllCities;
    @BindView(R.id.btn_search_by_my_location)
    Button btnSearchByMyLocation;
    @BindView(R.id.btn_search_by_choose_department)
    Button btnSearchByChooseDepartment;
    @BindView(R.id.btn_search_by_all_department)
    Button btnSearchByAllDepartment;

    @BindView(R.id.tvShopQuality)
    TextView tvShopQuality;
    @BindView(R.id.rclViewShop)
    RecyclerView rclViewShop;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.iv_show_more)
    ImageView ivShowMore;

    @BindView(R.id.iv_arrow_bottom)
    ImageView ivArrowBottomCity;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar)
    AppBarLayout appBar;

    @BindView(R.id.iv_expand_app_bar)
    ImageView ivExpandAppBar;

    private ListFoodAdapterNew listFoodAdapterNew;
    private MySharedPreferences mySharedPreferences;

    private List<Menu> menuList = new ArrayList<>();
    private List<DepartmentCategory> departmentCategoryList = new ArrayList<>();
    private List<CategorySearch> categorySearchList = new ArrayList<>();

    private int page = 1;
    private boolean isLoadingMore = false;

    private String cityId = "";
    private String distance = "45";
    private String lat = "";
    private String lon = "";
    private String sortType = "desc";
    private String sortBy = "date";
    private boolean isProgress = true;

    private DatabaseReference ref;
    private ArrayList<Conversation> listConversation;

    int chatCount = 0;

    @Override
    protected void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_food_home;
    }

    @Override
    protected void initData() {
        getLatLong();
        mySharedPreferences = new MySharedPreferences(this);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void configView() {
        rclViewShop.setHasFixedSize(true);
        rclViewShop.setLayoutManager(new LinearLayoutManager(this));
        listFoodAdapterNew = new ListFoodAdapterNew(rclViewShop, ListHomeFoodActivity.this, menuList);
        rclViewShop.setAdapter(listFoodAdapterNew);
        searchListFoodHome(1, isProgress);

        // refresh layout
        refreshLayout.setOnRefreshListener(() -> {
            isLoadingMore = false;
            page = 1;
            searchListFoodHome(page, false);
        });

        // load more
        listFoodAdapterNew.setOnLoadMoreListener(() -> {
            menuList.add(null);
            listFoodAdapterNew.notifyItemInserted(menuList.size() - 1);
            new Handler().postDelayed(() -> {
                menuList.remove(menuList.size() - 1);
                listFoodAdapterNew.notifyItemRemoved(menuList.size());

                isLoadingMore = true;
                page++;
                searchListFoodHome(page, false);
            }, DELAY_MILLIS_2000);
        });

        getChatCount();
    }

    @Override
    public void onResume() {
        super.onResume();
       appBar.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        appBar.removeOnOffsetChangedListener(this);
    }

    @OnClick(R.id.iv_search_by_distance)
    void onLocClick() {
        chooseBack();
    }

    @OnClick(R.id.img_top_cart)
    void goToCart() {
      //sendAction(Constant.SHOW_TAB_CART);
        gotoActivity(MainCartActivity.class);

    }

    @OnClick(R.id.imgHome)
    void goToHome() {
        chooseBack();
    }

    @OnClick(R.id.iv_expand_app_bar)
    void expandAppBar() {
        appBar.setExpanded(true, true);
        ivExpandAppBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnBack)
    void chooseBack() {
        onBackPressed();
    }

    @OnClick(R.id.iv_show_more)
    void chooseShowMore() {
        isLoadingMore = true;
        page++;
        searchListFoodHome(page, isProgress);
    }

    @OnClick({R.id.container_search, R.id.btn_search})
    void chooseSearch() {
        isLoadingMore = false;
        page = 1;
        searchListFoodHome(page, isProgress);
    }

    @OnClick(R.id.iv_search_by_date_name_rating)
    void chooseSearchByDateNamRating() {
        chooseSearchByAllCities();
        SortByDialog dialog = SortByDialog.newInstance(sortBy);
        dialog.show(getSupportFragmentManager(), dialog.getTag());
        dialog.setOnSortByListener(new SortByDialog.OnSortByListener() {
            @Override
            public void onSortByDate() {
                sortBy = ConstantApp.SORT_BY_DATE;
                chooseSearch();
            }

            @Override
            public void onSortByName() {
                sortBy = ConstantApp.SORT_BY_NAME;
                chooseSearch();
            }

            @Override
            public void onSortByRating() {
                sortBy = ConstantApp.SORT_BY_RATING;
                chooseSearch();
            }
        });
    }

    @OnClick(R.id.container_choose_cities)
    void chooseSearchByCities() {
        refreshLocationData();

        CityIdDialog dialog = CityIdDialog.newInstance();
        dialog.show(getSupportFragmentManager(), dialog.getTag());
        dialog.setOnSearchByCityIdListener((idCity, cityName) -> {
            if (idCity != null && cityName != null) {
                cityId = idCity;
                tvCityName.setText(cityName);
            }

            setupCityIdSelected();
            setupCityAllUnSelected();
            setupMyLocationUnSelected();
        });
    }

    @OnClick(R.id.btn_search_by_all_cities)
    void chooseSearchByAllCities() {
        refreshLocationData();
        refreshCityData();

        setupCityAllSelected();
        setupCityIdUnSelected();
        setupMyLocationUnSelected();
    }

    @OnClick(R.id.btn_search_by_my_location)
    void chooseSearchByMyLocation() {
        getLatLong();
        distance = "45";
        tvCityName.setText(getString(R.string.list_shop_home_text_choose_cities));

        setupMyLocationSelected();
        setupCityIdUnSelected();
        setupCityAllUnSelected();
    }

    @OnClick(R.id.container_sort)
    void chooseSearchBySort() {
        if (sortType.equals(ConstantApp.SORT_TYPE_ASC)) {
            ivSort.setImageResource(R.drawable.ic_arrows_exchange_alt_v);
            sortType = ConstantApp.SORT_TYPE_DESC;
        } else if (sortType.equals(ConstantApp.SORT_TYPE_DESC)) {
            ivSort.setImageResource(R.drawable.ic_arrow_exchange_alt_v_up);
            sortType = ConstantApp.SORT_TYPE_ASC;
        }
        chooseSearch();
    }

//    @OnClick(R.id.iv_search_by_distance)
    void chooseSearchByDistance() {
        getLatLong();

        // show
        DistanceDialog dialog = DistanceDialog.newInstance(distance);
        dialog.show(getSupportFragmentManager(), dialog.getTag());

        // listener
        dialog.setOnSearchByDistanceListener(distanceValue -> distance = distanceValue);
    }

    @OnClick(R.id.btn_search_by_choose_department)
    void chooseSearchByDepartment() {
        if (!NetworkUtil.checkNetworkAvailable(this)) {
            showToast(R.string.no_connection);
            return;
        }

        ModelManager.getListDepartmentCategory(this, true, new ModelManagerListener() {
            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, "error api" + error.getMessage());
                showToast(ErrorNetworkHandler.processError(error));
            }

            @Override
            public void onSuccess(Object object) {
                if (ParserUtility.isSuccess(object.toString())) {
                    departmentCategoryList.clear();
                    List<DepartmentCategory> list =
                            ParserUtility.parseDepartmentCategory(object.toString());
                    if (list != null && list.size() > 0) {
                        departmentCategoryList.addAll(list);
                    } else {
                        Log.e(TAG, "some error in parse json");
                    }
                    setupDepartmentDialog();
                } else {
                    showToast(ParserUtility.getMessage(object.toString()));
                }
            }
        });
    }

    private void setupDepartmentDialog() {
        DepartmentDialog dialog = DepartmentDialog.newInstance(departmentCategoryList);
        dialog.show(getSupportFragmentManager(), dialog.getTag());

        dialog.setOnSearchByDepartmentListener(list -> {
            formatList(list);
            page = 1;
            isLoadingMore = false;
            searchListFoodHome(page, true);
        });


        setupDepartmentIdSelected();
        setupDepartmentAllUnSelected();
    }

    @OnClick(R.id.btn_search_by_all_department)
    void chooseSearchByAllDepartment() {
        setupDepartmentAllSelected();
        setupDepartmentIdUnSelected();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_profile:
                if (GlobalValue.myAccount != null) {
                    sendAction(Constant.SHOW_TAB_PROFILE);
                } else {
                    showDialogLogin();
                }
                break;
            case R.id.tab_cart:
                sendAction(Constant.SHOW_TAB_CART);
                break;
            case R.id.tab_search:
                sendAction(Constant.SHOW_TAB_SEARCH);
                break;
            case R.id.tab_home:
                sendAction(Constant.SHOW_TAB_HOME);
                break;
        }
    }

    private void sendAction(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
        finish();
    }

    private void searchListFoodHome(int page, boolean isProgress) {
        if (!NetworkUtil.checkNetworkAvailable(this)) {
            showToast(R.string.message_network_is_unavailable);
            refreshLayout.setRefreshing(false);
            return;
        }

        String searchText = "";
        if (edtSearch != null) {
            searchText = edtSearch.getText().toString().trim();
        }
        ModelManager.getListFoodBySearch(this, searchText,
                new Gson().toJson(categorySearchList), cityId,
                page, ConstantApp.OPEN_ALL, distance, sortBy, sortType, lat, lon, isProgress,
                new ModelManagerListener() {

                    @Override
                    public void onError(VolleyError error) {
                        Log.e(TAG, "error api" + error.getMessage());
                        if (refreshLayout != null) {
                            refreshLayout.setRefreshing(false);
                        }

                        categorySearchList.clear();
                        listFoodAdapterNew.notifyDataSetChanged();

                        showToast(ErrorNetworkHandler.processError(error));
                    }

                    @Override
                    public void onSuccess(Object object) {
                        Log.e(TAG, "success api" + object.toString());
                        if (refreshLayout != null) {
                            refreshLayout.setRefreshing(false);
                        }

                        if (ParserUtility.isSuccess(object.toString())) {
                            List<Menu> arr = ParserUtility.parseListFoodInSearch(object.toString());

                            if (!isLoadingMore) {
                                menuList.clear();
                                String mCount = "<b>"+ParserUtility.ParseCount(object.toString())+"</b>";
                                tvShopQuality.setText(Html.fromHtml(getString(R.string.product_found, mCount)));
                            }
                            if (arr.size() > 0) {
                                ivShowMore.setVisibility(View.VISIBLE);
                                menuList.addAll(ParserUtility.parseListFoodInSearch(object.toString()));
                                listFoodAdapterNew.setLoaded();
                            } else {
                                ivShowMore.setVisibility(View.GONE);
                                showToast(R.string.have_no_more_date);
                            }
                            categorySearchList.clear();
                        } else {
                            menuList.clear();
                            showToast(ParserUtility.getMessage(object.toString()));
                        }

                        listFoodAdapterNew.notifyDataSetChanged();
                    }
                });
    }

    private void getLatLong() {
        if (Constant.isFakeLocation) {
            lat = GlobalValue.glatlng.latitude + "";
            lon = GlobalValue.glatlng.longitude + "";
        } else {
            GPSTracker gps = new GPSTracker(this);
            if (gps.canGetLocation()) {
                lat = gps.getLatitude() + "";
                lon = gps.getLongitude() + "";
            }
        }
    }

    private void formatList(List<CategorySearch> list) {
        for (CategorySearch categorySearch : list) {
            if (categorySearch.getCategory().length() > 1) {
                String categoryId = categorySearch.getCategory()
                        .substring(0, categorySearch.getCategory().length() - 1);
                categorySearch.setCategory(categoryId);
            }
            categorySearchList.add(categorySearch);
        }
    }

    private void refreshLocationData() {
        lat = "";
        lon = "";
        distance = "45";
    }

    private void refreshCityData() {
        cityId = ConstantApp.ALL_CITIES;
        tvCityName.setText(getString(R.string.list_shop_home_text_choose_cities));
    }

    private void setupCityIdSelected() {
        containerSearchByChooseCities.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_red_button_with_border));
        tvCityName.setTextColor(getResources().getColor(R.color.white));
        ivArrowBottomCity.setImageResource(R.drawable.ic_arrow_down_2);
    }

    private void setupCityIdUnSelected() {
        containerSearchByChooseCities.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_white_corner_6));
        tvCityName.setTextColor(getResources().getColor(R.color.black));
        ivArrowBottomCity.setImageResource(R.drawable.ic_arrow_down_1);
    }

    private void setupCityAllSelected() {
        btnSearchByAllCities.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_red_button_with_border));
        btnSearchByAllCities.setTextColor(getResources().getColor(R.color.white));
    }

    private void setupCityAllUnSelected() {
        btnSearchByAllCities.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_white_corner_6));
        btnSearchByAllCities.setTextColor(getResources().getColor(R.color.black));
    }

    private void setupMyLocationSelected() {
        btnSearchByMyLocation.setBackground(
                ContextCompat.getDrawable(
                        this, R.drawable.bg_red_button_with_border));
        btnSearchByMyLocation.setTextColor(getResources().getColor(R.color.white));
    }

    private void setupMyLocationUnSelected() {
        btnSearchByMyLocation.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_white_corner_6));
        btnSearchByMyLocation.setTextColor(getResources().getColor(R.color.black));
    }

    private void setupDepartmentIdSelected() {
        btnSearchByChooseDepartment.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_red_button_with_border));
        btnSearchByChooseDepartment.setTextColor(getResources().getColor(R.color.white));
    }

    private void setupDepartmentIdUnSelected() {
        btnSearchByChooseDepartment.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_white_corner_6));
        btnSearchByChooseDepartment.setTextColor(getResources().getColor(R.color.black));
    }

    private void setupDepartmentAllSelected() {
        btnSearchByAllDepartment.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_red_button_with_border));
        btnSearchByAllDepartment.setTextColor(getResources().getColor(R.color.white));
    }

    private void setupDepartmentAllUnSelected() {
        btnSearchByAllDepartment.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_white_corner_6));
        btnSearchByAllDepartment.setTextColor(getResources().getColor(R.color.black));
    }

    private void getNotificationCount() {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            Account account = mySharedPreferences.getUserInfo();
            String userId = "";
            if (account != null) {
                userId = account.getId();
            }
            if (userId.isEmpty()) {
                return;
            }
//            ModelManager.notificationCount(this, userId, false,
//                    new ModelManagerListener() {
//                        @Override
//                        public void onError(VolleyError error) {
//                            showToast(ErrorNetworkHandler.processError(error));
//                        }
//
//                        @Override
//                        public void onSuccess(Object object) {
//                            if (ParserUtility.isSuccess(object.toString())) {
//                                String newOrderCount = ParserUtility.getNewOrderCount(object.toString());
//                                String newStatusChangedCount = ParserUtility.getNewStatusChangedCount(object.toString());
//
//                                int notificationOrderSum = Integer.parseInt(newOrderCount) + Integer.parseInt(newStatusChangedCount) + chatCount;
//                                if (notificationOrderSum != 0) {
//                                    if (tvOrderSum != null) {
//                                        tvOrderSum.setVisibility(View.VISIBLE);
//                                        tvOrderSum.setText(String.valueOf(notificationOrderSum));
//                                    }
//                                }
//
//                            } else {
//                                showToast(ParserUtility.getMessage(object.toString()));
//                            }
//                        }
//                    });
        } else {
            showToast(R.string.no_connection);
        }
    }

    private void showDialogLogin() {
        Dialog loginDialog = new Dialog(this);
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loginDialog.setContentView(R.layout.dialog_confirm);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            ivExpandAppBar.setVisibility(View.GONE);
        } else {
            ivExpandAppBar.setVisibility(View.VISIBLE);
        }
    }

    private void getChatCount() {
        if (!NetworkUtil.checkNetworkAvailable(this)) {
            showToast(R.string.no_connection);
            return;
        }

        if (mySharedPreferences != null) {
            Account account = mySharedPreferences.getUserInfo();
            if (account == null) {
                return;
            }
            String userId = account.getId();
            if (userId == null) {
                return;
            }

            ref.child("user").child(userId).child("chatWith").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listConversation = new ArrayList<>();

                    for (final DataSnapshot child : dataSnapshot.getChildren()) {
                        for (DataSnapshot chidData : child.getChildren()) {
                            final Map<String, Object> listData = (Map<String, Object>) chidData.getValue();
                            final Conversation conversation = new Conversation();

                            Object status = listData.get("status");
                            if (status != null && listData.get("datePost") != null) {
                                Object datePost = listData.get("datePost");
                                conversation.setDatePost(datePost.toString());
                                conversation.setLastMessage(listData.get("lastMessage") + "");
                                conversation.setImageReceiver(listData.get("imageReceiver") + "");
                                conversation.setImageSender(listData.get("imageSender") + "");
                                conversation.setReceiverName(listData.get("receiverName") + "");
                                conversation.setSenderName(listData.get("senderName") + "");
                                conversation.setSenderId(listData.get("senderId") + "");
                                conversation.setReceiverId(listData.get("receiverId") + "");
                                conversation.setStatus(listData.get("status") + "");
                                conversation.setStatusOnline("0");
                                conversation.setUnread(listData.get("unread") + "");
                                conversation.setShopId(listData.get("shopId") + "");
                                conversation.setShopName(listData.get("shopName") + "");
                                conversation.setBuyerId(listData.get("buyerId") + "");
                                conversation.setCountShop(listData.get("countShop") + "");
                                listConversation.add(conversation);
                                Log.e("data frag", "data frag:" + child.getChildrenCount());
//                                keyConversation.add(chidData.getKey());
                            } else if (listData.get("datePost") != null) {
                                Object datePost = listData.get("datePost");
                                conversation.setDatePost(datePost.toString());
                                conversation.setLastMessage(listData.get("lastMessage") + "");
                                conversation.setImageReceiver(listData.get("imageReceiver") + "");
                                conversation.setImageSender(listData.get("imageSender") + "");
                                conversation.setReceiverName(listData.get("receiverName") + "");
                                conversation.setSenderName(listData.get("senderName") + "");
                                conversation.setSenderId(listData.get("senderId") + "");
                                conversation.setReceiverId(listData.get("receiverId") + "");
                                conversation.setStatusOnline("0");
                                conversation.setUnread(listData.get("unread") + "");
                                conversation.setShopId(listData.get("shopId") + "");
                                conversation.setShopName(listData.get("shopName") + "");
                                conversation.setBuyerId(listData.get("buyerId") + "");
                                conversation.setCountShop(listData.get("countShop") + "");
                                listConversation.add(conversation);
                                Log.e("data frag", "data frag:" + child.getChildrenCount());
                            }

                        }
                    }

                    Collections.sort(listConversation, new Comparator<Conversation>() {
                        @Override
                        public int compare(Conversation o1, Conversation o2) {
                            int value = Integer.parseInt(o2.getDatePost()) - Integer.parseInt(o1.getDatePost());
                            if (value != 0) {
                                return value;
                            }
                            value = Integer.parseInt(o2.getUnread()) - Integer.parseInt(o1.getUnread());
                            if (value != 0) {
                                return value;
                            }
//                            value = o1.getSenderName().compareTo(o2.getSenderName());
                            return value;
                        }
                    });

                    chatCount = 0;
                    Log.e(TAG, "size: " + listConversation.size());
                    for (Conversation conversation1 : listConversation) {
                        Log.d(TAG, "un read:" + conversation1.getUnread());
                        if (conversation1.getUnread() != null) {
                            if (Integer.parseInt(conversation1.getUnread()) > 0) {
                                chatCount++;
                            }
                        }
                    }

                    getNotificationCount();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast(databaseError.getMessage());
                }
            });
        }

    }
}

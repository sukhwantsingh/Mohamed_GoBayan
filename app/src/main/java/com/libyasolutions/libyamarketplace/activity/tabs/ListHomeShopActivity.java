package com.libyasolutions.libyamarketplace.activity.tabs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.BaseActivityV2;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ShopAdapterNew;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.ConstantApp;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.databinding.ActivityListShopHomeBinding;
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
import com.libyasolutions.libyamarketplace.object.Shop;
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

/**
 * Create by Nguyen Dinh Doan
 */
public class ListHomeShopActivity extends BaseActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    private static final int DELAY_LOAD_MORE_2000 = 2000;

    private ShopAdapterNew shopAdapterNew;
    private MySharedPreferences mySharedPreferences;

    private List<Shop> shopList = new ArrayList<>();
    private List<DepartmentCategory> departmentCategoryList = new ArrayList<>();
    private List<CategorySearch> categorySearchList = new ArrayList<>();

    private int page = 1;
    private boolean isLoadingMore = false;

    private String cityId = "";
    private String distance = "45";
    private String latitude = "";
    private String longitude = "";
    private String sortType = "desc"; //  sort ascending
    private String sortBy = "date";

    private DatabaseReference ref;
    private ArrayList<Conversation> listConversation;

    int chatCount = 0;

    private ActivityListShopHomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListShopHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initData();
        initControls();
    }
    protected void initData() {
        setSupportActionBar(binding.toolbar);

        getLatLong();
        mySharedPreferences = new MySharedPreferences(this);
        ref = FirebaseDatabase.getInstance().getReference();

        binding.rclViewShop.setHasFixedSize(true);
        binding.rclViewShop.setLayoutManager(new LinearLayoutManager(this));
        shopAdapterNew = new ShopAdapterNew(binding.rclViewShop, shopList, this);
        binding.rclViewShop.setAdapter(shopAdapterNew);
        searchListShopHome(1, true);

        // tab listener
        binding.tabProfile.setOnClickListener(this);
        binding.tabCart.setOnClickListener(this);
        binding.tabSearch.setOnClickListener(this);
        binding.tabHome.setOnClickListener(this);

        // refresh data
        binding.refreshLayout.setOnRefreshListener(() -> {
            isLoadingMore = false;
            page = 1;

            searchListShopHome(page, false);
        });

        // load more data
        shopAdapterNew.setOnLoadMoreListener(() -> {
            shopList.add(null);
            shopAdapterNew.notifyItemInserted(shopList.size() - 1);
            new Handler().postDelayed(() -> {
                shopList.remove(shopList.size() - 1);
                shopAdapterNew.notifyItemRemoved(shopList.size());

                isLoadingMore = true;
                page++;

                searchListShopHome(page, false);
            }, DELAY_LOAD_MORE_2000);
        });

        // show or hide icon expand app bar
        getChatCount();

    }

    private void initControls() {
        binding.ivExpandAppBar.setOnClickListener(v->{
           binding.appBar.setExpanded(true, true);
            binding.ivExpandAppBar.setVisibility(View.GONE);
        });

        binding.btnBack.setOnClickListener(v->{  chooseBack();   });
        binding.ivShowMore.setOnClickListener(v->{   chooseShowMore();  });
        binding.containerSearch.setOnClickListener(v->{   chooseSearch();  });
        binding.btnSearch.setOnClickListener(v->{   binding.containerSearch.performClick();  });
        binding.ivSearchByDateNameRating.setOnClickListener(v->{  chooseSearchByDateNameRating();  });
        binding.containerChooseCities.setOnClickListener(v->{   chooseSearchByCities();   });
        binding.btnSearchByAllCities.setOnClickListener(v->{  chooseSearchByAllCities();    });
        binding.btnSearchByMyLocation.setOnClickListener(v->{   chooseSearchByMyLocation(); });
        binding.containerSort.setOnClickListener(v->{   chooseSearchBySort();  });
        binding.ivSearchByDistance.setOnClickListener(v->{   chooseSearchByDistance();  });
        binding.btnSearchByChooseDepartment.setOnClickListener(v->{   chooseSearchByDepartment();  });
        binding.btnSearchByAllDepartment.setOnClickListener(v->{  chooseSearchByAllDepartment();   });

    }

    void chooseBack() {
        onBackPressed();
    }
    void chooseShowMore() {
        isLoadingMore = true;
        page++;

        searchListShopHome(page, true);
    }
    void chooseSearch() {
        isLoadingMore = false;
        page = 1;

        searchListShopHome(page, true);
    }
    void chooseSearchByDateNameRating() {
        SortByDialog dialog = SortByDialog.newInstance(sortBy);
        dialog.show(getSupportFragmentManager(), dialog.getTag());
        dialog.setOnSortByListener(new SortByDialog.OnSortByListener() {
            @Override
            public void onSortByDate() {
                sortBy = ConstantApp.SORT_BY_DATE;
            }

            @Override
            public void onSortByName() {
                sortBy = ConstantApp.SORT_BY_NAME;
            }

            @Override
            public void onSortByRating() {
                sortBy = ConstantApp.SORT_BY_RATING;
            }
        });
    }
    void chooseSearchByCities() {
        refreshLocationData();

        CityIdDialog dialog = CityIdDialog.newInstance();
        dialog.show(getSupportFragmentManager(), dialog.getTag());
        dialog.setOnSearchByCityIdListener((idCity, cityName) -> {
            if (idCity != null && cityName != null) {
                cityId = idCity;
                binding.tvCityName.setText(cityName);
            }

            setupCityIdSelected();
            setupDistanceUnSelected();
            setupCityAllUnSelected();
            setupMyLocationUnSelected();
        });
    }
    void chooseSearchByAllCities() {
        refreshLocationData();
        refreshCityData();

        setupCityAllSelected();
        setupCityIdUnSelected();
        setupDistanceUnSelected();
        setupMyLocationUnSelected();
    }
    void chooseSearchByMyLocation() {
        getLatLong();
        distance = "45";
        binding.tvCityName.setText(getString(R.string.list_shop_home_text_choose_cities));

        setupMyLocationSelected();
        setupDistanceSelected();
        setupCityIdUnSelected();
        setupCityAllUnSelected();
    }
    void chooseSearchBySort() {
        if (sortType.equals(ConstantApp.SORT_TYPE_ASC)) {
            binding.ivSort.setImageResource(R.drawable.ic_sort_desending);
            sortType = ConstantApp.SORT_TYPE_DESC;
        } else if (sortType.equals(ConstantApp.SORT_TYPE_DESC)) {
            binding.ivSort.setImageResource(R.drawable.ic_sort_assending);
            sortType = ConstantApp.SORT_TYPE_ASC;
        }
    }
    void chooseSearchByDistance() {
        getLatLong();

        // show
        DistanceDialog dialog = DistanceDialog.newInstance(distance);
        dialog.show(getSupportFragmentManager(), dialog.getTag());

        // listener
        dialog.setOnSearchByDistanceListener(distanceValue -> distance = distanceValue);
    }
    void chooseSearchByDepartment() {

        if (!NetworkUtil.checkNetworkAvailable(this)) {
            showToast(getString(R.string.no_connection));
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


    @Override
    public void onResume() {
        super.onResume();

        if (GlobalValue.arrMyMenuShop.size() != 0) {
            binding.tabCart.setBackgroundColor(Color.BLUE);
        } else {
            binding.tabCart.setBackgroundColor(getResources().getColor(R.color.background_new));
        }

        binding.appBar.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.appBar.removeOnOffsetChangedListener(this);
    }



    private void setupDepartmentDialog() {
        DepartmentDialog dialog = DepartmentDialog.newInstance(departmentCategoryList);
        dialog.show(getSupportFragmentManager(), dialog.getTag());

        dialog.setOnSearchByDepartmentListener(list -> {
            formatList(list);
            page = 1;
            isLoadingMore = false;
            searchListShopHome(page, true);
        });


        setupDepartmentIdSelected();
        setupDepartmentAllUnSelected();
    }


    private void searchListShopHome(int page, boolean isProgress) {
        if (!NetworkUtil.checkNetworkAvailable(this)) {
            showToast(getString(R.string.message_network_is_unavailable));
           binding.refreshLayout.setRefreshing(false);
            return;
        }

        String searchText = "";
        if (binding.edtSearch != null) {
            searchText = binding.edtSearch.getText().toString().trim();
        }
        ModelManager.getListShopBySearch(this, searchText,
                new Gson().toJson(categorySearchList), cityId,
                page, ConstantApp.OPEN_ALL, distance, sortBy, sortType,
                latitude, longitude, isProgress,
                new ModelManagerListener() {

                    @Override
                    public void onError(VolleyError error) {
                        Log.e(TAG, "error api" + error.getMessage());
                        if (binding.refreshLayout != null) {
                            binding.refreshLayout.setRefreshing(false);
                        }

                        categorySearchList.clear();
                        shopAdapterNew.notifyDataSetChanged();
                        showToast(ErrorNetworkHandler.processError(error));
                    }

                    @Override
                    public void onSuccess(Object object) {
                        Log.e(TAG, "success api" + object.toString());
                        binding.refreshLayout.setRefreshing(false);

                        if (ParserUtility.isSuccess(object.toString())) {
                            List<Shop> list = ParserUtility.getListShop(object.toString());

                            if (!isLoadingMore) {
                                shopList.clear();
                                String shopCount = String.valueOf(ParserUtility.ParseCount(object.toString()));
                                binding.tvShopQuality.setText(getString(R.string.shop_found, shopCount));
                            }

                            if (list.size() > 0) {
                                binding.ivShowMore.setVisibility(View.VISIBLE);
                                shopList.addAll(list);
                                shopAdapterNew.setLoaded();
                            } else {
                                binding.ivShowMore.setVisibility(View.GONE);
                                showToast(getString(R.string.have_no_more_date));
                            }
                            categorySearchList.clear();
                        } else {
                            shopList.clear();
                            showToast(ParserUtility.getMessage(object.toString()));
                        }
                        shopAdapterNew.notifyDataSetChanged();
                    }
                });
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

    private void getLatLong() {
        if (Constant.isFakeLocation) {
            latitude = GlobalValue.glatlng.latitude + "";
            longitude = GlobalValue.glatlng.longitude + "";
        } else {
            GPSTracker gps = new GPSTracker(this);
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude() + "";
                longitude = gps.getLongitude() + "";
            }
        }
    }

    private void refreshLocationData() {
        latitude = "";
        longitude = "";
        distance = "45";
    }

    private void refreshCityData() {
        cityId = ConstantApp.ALL_CITIES;
        binding.tvCityName.setText(getString(R.string.list_shop_home_text_choose_cities));
    }

    private void setupCityIdSelected() {
        binding. containerChooseCities.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_red_button_with_border));
        binding. tvCityName.setTextColor(getResources().getColor(R.color.white));
        binding. ivArrowBottom.setImageResource(R.drawable.ic_arrow_down_2);
    }

    private void setupCityIdUnSelected() {
        binding.  containerChooseCities.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_white_corner_6));
        binding. tvCityName.setTextColor(getResources().getColor(R.color.black));
        binding.ivArrowBottom.setImageResource(R.drawable.ic_arrow_down_1);
    }

    private void setupDistanceSelected() {
        binding.ivSearchByDistance.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_red_button_with_border));
        binding.ivSearchByDistance.setImageResource(R.drawable.ic_location_search_white);
        binding.ivSearchByDistance.setEnabled(true);
    }

    private void setupDistanceUnSelected() {
        binding.ivSearchByDistance.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_white_corner_6));
        binding.ivSearchByDistance.setImageResource(R.drawable.ic_location_search_black);
        binding.ivSearchByDistance.setEnabled(false);
    }

    private void setupCityAllSelected() {
        binding.btnSearchByAllCities.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_red_button_with_border));
        binding.btnSearchByAllCities.setTextColor(getResources().getColor(R.color.white));
    }

    private void setupCityAllUnSelected() {
        binding.btnSearchByAllCities.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_white_corner_6));
        binding.btnSearchByAllCities.setTextColor(getResources().getColor(R.color.black));
    }

    private void setupMyLocationSelected() {

        binding.btnSearchByMyLocation.setBackground(
                ContextCompat.getDrawable(
                        this, R.drawable.bg_red_button_with_border));
        binding.btnSearchByMyLocation.setTextColor(getResources().getColor(R.color.white));
    }

    private void setupMyLocationUnSelected() {
        binding.btnSearchByMyLocation.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_white_corner_6));
        binding.btnSearchByMyLocation.setTextColor(getResources().getColor(R.color.black));
    }

    private void setupDepartmentIdSelected() {

        binding.btnSearchByChooseDepartment.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_red_button_with_border));
        binding.btnSearchByChooseDepartment.setTextColor(getResources().getColor(R.color.white));
    }

    private void setupDepartmentIdUnSelected() {
        binding.btnSearchByChooseDepartment.setBackground(
                ContextCompat.getDrawable(this,
                        R.drawable.bg_white_corner_6));
        binding.btnSearchByChooseDepartment.setTextColor(getResources().getColor(R.color.black));
    }

    private void setupDepartmentAllSelected() {
        binding.btnSearchByAllDepartment.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_red_button_with_border));
        binding.btnSearchByAllDepartment.setTextColor(getResources().getColor(R.color.white));
    }

    private void setupDepartmentAllUnSelected() {
        binding.btnSearchByAllDepartment.setBackground(ContextCompat.getDrawable(this,  R.drawable.bg_white_corner_6));
        binding.btnSearchByAllDepartment.setTextColor(getResources().getColor(R.color.black));
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

            ModelManager.notificationCount(this, userId, false,
                    new ModelManagerListener() {
                        @Override
                        public void onError(VolleyError error) {
                            showToast(ErrorNetworkHandler.processError(error));
                        }

                        @Override
                        public void onSuccess(Object object) {
                            if (ParserUtility.isSuccess(object.toString())) {
                                String newOrderCount = ParserUtility.getNewOrderCount(object.toString());
                                String newStatusChangedCount = ParserUtility.getNewStatusChangedCount(object.toString());

                                int notificationOrderSum = Integer.parseInt(newOrderCount) + Integer.parseInt(newStatusChangedCount) + chatCount;
                                if (notificationOrderSum != 0) {
                                    binding.tvOrderNewNumberTab.setVisibility(View.VISIBLE);
                                    binding.tvOrderNewNumberTab.setText(String.valueOf(notificationOrderSum));
                                }

                            } else {
                                showToast(ParserUtility.getMessage(object.toString()));
                            }
                        }
                    });
        } else {
            showToast(getResources().getString(R.string.no_connection));
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
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset ) {
        if (verticalOffset == 0) {
            binding.ivExpandAppBar.setVisibility(View.GONE);
        } else {
            binding.ivExpandAppBar.setVisibility(View.VISIBLE);
        }
    }

    private void getChatCount() {
        if (!NetworkUtil.checkNetworkAvailable(this)) {
            showToast(getString(R.string.no_connection));
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

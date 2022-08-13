package com.libyasolutions.libyamarketplace.activity.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Html;

import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.FilterScreenActivity;
import com.libyasolutions.libyamarketplace.adapter.ShopAdapterNew;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.ConstantApp;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.databinding.ActivityListShopHomeBinding;
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


public class ListHomeShopActivity extends BaseActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    private static final int DELAY_LOAD_MORE_2000 = 2000;
    private final int REQ_CODE_FILTER = 1127;

    private ShopAdapterNew shopAdapterNew;
    private MySharedPreferences mySharedPreferences;

    private List<Shop> shopList = new ArrayList<>();
    private List<DepartmentCategory> departmentCategoryList = new ArrayList<>();
    private List<CategorySearch> categorySearchList = new ArrayList<>();

    private int page = 1;
    private boolean isLoadingMore = false;

    private String cityId = ConstantApp.ALL_CITIES;
    private String distance = "45";
    private String latitude = "";
    private String longitude = "";
    private String sortType = "desc"; //  sort ascending
    private String sortBy = "date";

    private String minPrice ="", maxPrice = "";

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
        mySharedPreferences = new MySharedPreferences(this);
        ref = FirebaseDatabase.getInstance().getReference();

       checkIntentForValues();

        binding.rclViewShop.setHasFixedSize(true);
        binding.rclViewShop.setLayoutManager(new LinearLayoutManager(this));
        shopAdapterNew = new ShopAdapterNew(binding.rclViewShop, shopList, this);
        binding.rclViewShop.setAdapter(shopAdapterNew);

       searchListShopHome(1, true);

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

    private void checkIntentForValues() {
        String chkCity = getIntent().getStringExtra("cityId");
        if(null != chkCity){
            cityId = chkCity;
            distance = getIntent().getStringExtra("distance");
            minPrice = getIntent().getStringExtra("minPrice");
            maxPrice = getIntent().getStringExtra("maxPrice");
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
            categorySearchList = FilterScreenActivity.Companion.getCategorySearchList();

        }
        // else getLatLong();
    }

    private void initControls() {
        binding.btnBack.setOnClickListener(v->{  chooseBack();   });
        binding.imgHome.setOnClickListener(v->{  chooseBack();  });
        binding.ivShowMore.setOnClickListener(v->{   chooseShowMore();  });
        binding.containerSearch.setOnClickListener(v->{   chooseSearch();  });

        binding.ivSearchByDateNameRating.setOnClickListener(v-> {
            chooseSearchByDateNameRating();  });


        binding.containerSort.setOnClickListener(v-> {
              chooseSearchBySort();
        });
        binding.ivSearchByDistance.setOnClickListener(v->{
          chooseBack();
        });

        binding.imageFilter.setOnClickListener(v->{
            FilterScreenActivity.Companion.setComingFrom("SHOP");
            gotoActivityForResult(this, FilterScreenActivity.class, REQ_CODE_FILTER);
        });

        binding.imgTopCart.setOnClickListener(v->{
            gotoActivity(MainCartActivity.class);
        });

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
                binding.containerSearch.performClick();
            }

            @Override
            public void onSortByName() {
                sortBy = ConstantApp.SORT_BY_NAME;
                binding.containerSearch.performClick();
            }

            @Override
            public void onSortByRating() {
                sortBy = ConstantApp.SORT_BY_RATING;
                binding.containerSearch.performClick();
            }
        });


    }


    void chooseSearchBySort() {
        if (sortType.equals(ConstantApp.SORT_TYPE_ASC)) {
            binding.ivSort.setImageResource(R.drawable.ic_arrows_exchange_alt_v);
            sortType = ConstantApp.SORT_TYPE_DESC;
        } else if (sortType.equals(ConstantApp.SORT_TYPE_DESC)) {
           // binding.ivSort.setImageResource(R.drawable.ic_sort_assending);
            binding.ivSort.setImageResource(R.drawable.ic_arrow_exchange_alt_v_up);
            sortType = ConstantApp.SORT_TYPE_ASC;
        }
        binding.containerSearch.performClick();
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


    private void searchListShopHome(int page, boolean isProgress) {
        if (!NetworkUtil.checkNetworkAvailable(this)) {
            showToast(getString(R.string.message_network_is_unavailable));
            binding.refreshLayout.setRefreshing(false);
            return;
        }

        String searchText =  binding.edtSearch.getText().toString().trim();
        ModelManager.getListShopBySearch(this, searchText,
                new Gson().toJson(categorySearchList), cityId,
                page, ConstantApp.OPEN_ALL, distance, sortBy, sortType,
                latitude, longitude, isProgress,
                new ModelManagerListener() {
                    @Override
                    public void onError(VolleyError error) {
                        Log.e(TAG, "error api" + error.getMessage());
                        binding.refreshLayout.setRefreshing(false);

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
                                String shopCount = "<b>"+ParserUtility.ParseCount(object.toString())+"</b>";
                                binding.tvShopQuality.setText(Html.fromHtml(getString(R.string.shop_found, shopCount)));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_FILTER) {
            if (resultCode == RESULT_OK) {
                if(null != data) {
                    cityId = data.getStringExtra("cityId");
                    distance = data.getStringExtra("distance");
                    minPrice = data.getStringExtra("minPrice");
                    maxPrice = data.getStringExtra("maxPrice");
                    latitude = data.getStringExtra("latitude");
                    longitude = data.getStringExtra("longitude");
                    categorySearchList = FilterScreenActivity.Companion.getCategorySearchList();

                    chooseSearch();
                }
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
//
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
//                                    binding.tvOrderNewNumberTab.setVisibility(View.VISIBLE);
//                                    binding.tvOrderNewNumberTab.setText(String.valueOf(notificationOrderSum));
//                                }
//
//                            } else {
//                                showToast(ParserUtility.getMessage(object.toString()));
//                            }
//                        }
//                    });
        } else {
            showToast(getResources().getString(R.string.no_connection));
        }
    }



    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset ) {

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

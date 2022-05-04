package com.libyasolutions.libyamarketplace.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.LoginActivity;
import com.libyasolutions.libyamarketplace.activity.tabs.MainTabActivity;
import com.libyasolutions.libyamarketplace.adapter.ListFoodAdapterNew;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.ConstantApp;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.object.Conversation;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.widget.TextViewFontEnglish;
import com.libyasolutions.libyamarketplace.widget.TextViewFontH1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListFoodActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.lblShopName)
    TextViewFontH1 lblShopName;
    @BindView(R.id.lblMenuName)
    TextViewFontH1 lblMenuName;
    @BindView(R.id.layoutHeader)
    RelativeLayout layoutHeader;
    @BindView(R.id.container_search)
    FrameLayout containerSearch;
    @BindView(R.id.iv_sort)
    ImageView ivSort;
    @BindView(R.id.container_sort)
    FrameLayout containerSort;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.tvShopQuality)
    TextViewFontEnglish tvShopQuality;
    @BindView(R.id.lsvFood)
    RecyclerView lsvFood;
    @BindView(R.id.tab_home)
    LinearLayout tabHome;
    @BindView(R.id.tab_search)
    LinearLayout tabSearch;
    @BindView(R.id.tab_cart)
    LinearLayout tabCart;
    @BindView(R.id.tab_profile)
    LinearLayout tabProfile;
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;
    @BindView(R.id.iv_show_more)
    ImageView ivShowMore;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_order_new_number_tab)
    TextView tvOrderSum;

    private ListFoodAdapterNew foodAdapterNew;
    private List<Menu> menuFoodList = new ArrayList<>();
    private MySharedPreferences mySharedPreferences;

    private int shopId = -1;
    private String categoryId = "-1";
    private String sortBy = "desc";
    private boolean isLoadingMore = false;
    private int page = 1;

    // TODO ?: fix
    public static BaseActivity self;

    private DatabaseReference ref;
    private ArrayList<Conversation> listConversation;

    int chatCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        mySharedPreferences = new MySharedPreferences(this);
        ref = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_list_food);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initData();
        initControl();
        getChatCount();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalValue.arrMyMenuShop != null && GlobalValue.arrMyMenuShop.size() != 0) {
            tabCart.setBackgroundColor(Color.BLUE);
        } else {
            tabCart.setBackgroundColor(getResources().getColor(R.color.background_new));
        }
    }

    private void searchFoodList(int page, boolean isProgress) {
        if (!NetworkUtil.checkNetworkAvailable(this)) {
            showToastMessage(R.string.message_network_is_unavailable);
            return;
        }

        if (shopId == -1 || categoryId.equals("-1")) {
            showToastMessage(R.string.have_error);
            return;
        }

        String keyword = edtSearch.getText().toString().trim();
        ModelManager.getListFoodByShopAndCategory(this, page, shopId,
                categoryId, sortBy, keyword, isProgress,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(Object object) {
                        Log.e(TAG, "success api" + object.toString());
                        refreshLayout.setRefreshing(false);


                        if (ParserUtility.isSuccess(object.toString())) {
                            List<Menu> list = ParserUtility.parseListFoodInSearch(object.toString());

                            if (!isLoadingMore) {
                                menuFoodList.clear();
                                tvShopQuality.setText(getString(R.string.product_found,
                                        String.valueOf(ParserUtility.ParseCount(object.toString()))));
                            }

                            if (list.size() > 0) {
                                ivShowMore.setVisibility(View.VISIBLE);
                                menuFoodList.addAll(list);
                                foodAdapterNew.setLoaded();
                            } else {
                                showToastMessage(getResources().getString(R.string.have_no_date));
                                ivShowMore.setVisibility(View.GONE);
                            }

                        } else {
                            menuFoodList.clear();
                            showToastMessage(ParserUtility.getMessage(object.toString()));
                        }
                        foodAdapterNew.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.e(TAG, "error api" + error.getMessage());
                        menuFoodList.clear();
                        foodAdapterNew.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);

                        showToastMessage(ErrorNetworkHandler.processError(error));
                        foodAdapterNew.notifyDataSetChanged();
                    }
                });
    }

    private void initControl() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lsvFood.setLayoutManager(layoutManager);
        foodAdapterNew = new ListFoodAdapterNew(lsvFood,this, menuFoodList);
        lsvFood.setAdapter(foodAdapterNew);
        searchFoodList(1, true);

        refreshLayout.setOnRefreshListener(() -> {
            isLoadingMore = false;
            page = 1;
            searchFoodList(page, false);
        });

        foodAdapterNew.setOnLoadMoreListener(() -> {
            isLoadingMore = true;
            page ++;
            searchFoodList(page, false);
        });

        tabProfile.setOnClickListener(this);
        tabCart.setOnClickListener(this);
        tabSearch.setOnClickListener(this);
        tabHome.setOnClickListener(this);
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

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(GlobalValue.KEY_SHOP_ID)
                    && b.containsKey(GlobalValue.KEY_CATEGORY_ID)) {
                shopId = b.getInt(GlobalValue.KEY_SHOP_ID);
                categoryId = b.getString(GlobalValue.KEY_CATEGORY_ID);
            }

            if (b.containsKey(GlobalValue.KEY_SHOP_NAME)) {
                String shopName = b.getString(GlobalValue.KEY_SHOP_NAME);
                lblShopName.setText(shopName);
            }

            if (b.containsKey(GlobalValue.KEY_CATEGORY_NAME)) {
                String categoryName = b.getString(GlobalValue.KEY_CATEGORY_NAME);
                lblMenuName.setText(categoryName);
            }
        }

    }

    @OnClick(R.id.iv_show_more)
    void chooseShowMore() {
        isLoadingMore = true;
        page++;
        searchFoodList(page, true);
    }

    @OnClick(R.id.container_search)
    void searchListFoodByShopAndMenu() {
        isLoadingMore = false;
        page = 1;
        searchFoodList(page, true);
    }

    @OnClick(R.id.container_sort)
    void chooseSort() {
        if (sortBy.equals(ConstantApp.SORT_TYPE_ASC)) {
            ivSort.setImageResource(R.drawable.ic_sort_desending);
            sortBy = ConstantApp.SORT_TYPE_DESC;
        } else if (sortBy.equals(ConstantApp.SORT_TYPE_DESC)) {
            ivSort.setImageResource(R.drawable.ic_sort_assending);
            sortBy = ConstantApp.SORT_TYPE_ASC;
        }
    }

    @OnClick(R.id.btnBack)
    void chooseBack() {
        onBackPressed();
    }

    @OnClick(R.id.lblShopName)
    void chooseShopName() {
        gotoShopDetail(shopId);
    }

    @OnClick(R.id.tab_profile)
    void chooseTabProfile() {
        sendAction(Constant.SHOW_TAB_PROFILE);
    }

    @OnClick(R.id.tab_cart)
    void chooseTabCart() {
        sendAction(Constant.SHOW_TAB_CART);
    }

    @OnClick(R.id.tab_search)
    void chooseTabSearch() {
        sendAction(Constant.SHOW_TAB_SEARCH);
    }

    @OnClick(R.id.tab_home)
    void chooseTabHome() {
        sendAction(Constant.SHOW_TAB_HOME);
    }

    private void sendAction(String action) {
        Intent intent = new Intent(this, MainTabActivity.class);
        intent.setAction(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
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
                            showToastMessage(ErrorNetworkHandler.processError(error));
                        }

                        @Override
                        public void onSuccess(Object object) {
                            if (ParserUtility.isSuccess(object.toString())) {
                                String newOrderCount = ParserUtility.getNewOrderCount(object.toString());
                                String newStatusChangedCount = ParserUtility.getNewStatusChangedCount(object.toString());

                                int notificationOrderSum = Integer.parseInt(newOrderCount) + Integer.parseInt(newStatusChangedCount) + chatCount;
                                if (notificationOrderSum != 0) {
                                    if (tvOrderSum != null) {
                                    tvOrderSum.setVisibility(View.VISIBLE);
                                    tvOrderSum.setText(String.valueOf(notificationOrderSum));

                                    }
                                }

                            } else {
                                showToastMessage(ParserUtility.getMessage(object.toString()));
                            }
                        }
                    });
        } else {
            showToastMessage(R.string.no_connection);
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

    private void getChatCount() {
        if (!NetworkUtil.checkNetworkAvailable(this)) {
            showToastMessage(R.string.no_connection);
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
                    showToastMessage(databaseError.getMessage());
                }
            });
        }

    }
}

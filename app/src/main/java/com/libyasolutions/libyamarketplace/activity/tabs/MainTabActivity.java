package com.libyasolutions.libyamarketplace.activity.tabs;

import android.app.Activity;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.AboutUsActivity;
import com.libyasolutions.libyamarketplace.activity.FeedBackActivity;
import com.libyasolutions.libyamarketplace.activity.ListChatActivity;
import com.libyasolutions.libyamarketplace.activity.ManageOrderActivity;
import com.libyasolutions.libyamarketplace.activity.ShopManagementActivity;
import com.libyasolutions.libyamarketplace.activity.SplashActivity;
import com.libyasolutions.libyamarketplace.activity.tabs.user.EditProfileActivity;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.object.Conversation;
import com.libyasolutions.libyamarketplace.object.MessageEvent;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.service.MessageService;
import com.libyasolutions.libyamarketplace.service.ServiceManager;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {

    private static final String TAG = "MainTabActivity";

    public static final int TAB_HOME = 0;
    public static final int TAB_SEARCH = 1;
    public static final int TAB_MY_CART = 2;
    public static final int TAB_ACCOUNT = 3;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    TabHost tabHost = null;
    private Activity self;
//    private NestedScrollView lnlLayoutLeft;
    private LinearLayout lnlInfor, lnlFeedback,
            lnlOrder, lnlLogout,
            lnlAboutUs, layoutChat,
            layoutShopManagement, layoutManageOrder,
            layoutConvertToShopOwner;
    private FrameLayout lnlAccount;
    private TextView tvAmount, tvName;
    private ImageView btnClose;
    private TextView tvOrderSumCount;
    private TextView tvOrderNewCount;
    private TextView tvStatusOrderChangedCount;
    private TextView tvChatCount;
    private final static int REQUEST_CODE = 99;

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter filter;
    private Dialog mDialog, loginDialog;
    private MySharedPreferences mySharedPreferences;
    private DatabaseReference ref;
    private ArrayList<Conversation> listConversation;

    int chatCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);
        mySharedPreferences = new MySharedPreferences(this);
        ref = FirebaseDatabase.getInstance().getReference();

        if (getIntent() != null) {
            String message = getIntent().getStringExtra("EXTRA_MESSAGE");
            if (message != null) {
                if (GlobalValue.myAccount == null) {
                    Account account = mySharedPreferences.getUserInfo();
                    if (account != null) {
                        account.setRole(Constant.ROLE_SHOP_OWNER);
                        mySharedPreferences.saveUserInfo(account);
                    }

                    Intent intent = new Intent(this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("REQUEST_SHOP_OWNER_ACCEPT"));

        self = this;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        lnlLayoutLeft = findViewById(R.id.lnlLayoutLeft);
//        lnlLayoutLeft.setClickable(true);
        initView();
        if (GlobalValue.myAccount != null) {
            tvName.setText(GlobalValue.myAccount.getFull_name());
        }
        initControls();
        initTabPages();
        if (GlobalValue.arrMyMenuShop == null) {
            GlobalValue.arrMyMenuShop = new ArrayList<Shop>();
        }
        mListener();
        processNextScreen();
        getChatCount();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("APPROVE_SHOP_OWNER");
            if (message.equals("APPROVE_SHOP_OWNER")) {
                showTabShopOwner();
                hideTabConvertToShopOwner();
                Account account = GlobalValue.myAccount;
                account.setRole(Constant.ROLE_SHOP_OWNER);
                mySharedPreferences.saveUserInfo(account);
            }
        }
    };


    private void processNextScreen() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case Constant.SHOW_TAB_PROFILE:
                        drawer.openDrawer(Gravity.START);
                        break;
                    case Constant.SHOW_TAB_SEARCH:
                        tabHost.setCurrentTab(TAB_SEARCH);
                        break;
                    case Constant.SHOW_TAB_CART:
                        tabHost.setCurrentTab(TAB_MY_CART);
                        break;
                    case Constant.SHOW_TAB_HOME:
                        tabHost.setCurrentTab(TAB_HOME);
                        break;
                }
            }
        }
    }

    private void mListener() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.SHOW_TAB_PROFILE)) {
                    drawer.openDrawer(Gravity.START);
                }
                if (intent.getAction().equals(Constant.SHOW_TAB_CART)) {
                    tabHost.setCurrentTab(TAB_MY_CART);
                }
                if (intent.getAction().equals(Constant.SHOW_TAB_SEARCH)) {
                    tabHost.setCurrentTab(TAB_SEARCH);
                }
                if (intent.getAction().equals(Constant.SHOW_TAB_HOME)) {
                    tabHost.setCurrentTab(TAB_HOME);
                }
                if (intent.getAction().equals(Constant.GET_CART_INFO)) {
                    if (GlobalValue.arrMyMenuShop.size() != 0) {
                        tabHost.getTabWidget().getChildTabViewAt(TAB_MY_CART).setBackgroundColor(Color.BLUE);
                    } else {
                        tabHost.getTabWidget().getChildTabViewAt(TAB_MY_CART).setBackgroundColor(getResources().getColor(R.color.background_new));
                    }
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction(Constant.SHOW_TAB_PROFILE);
        filter.addAction(Constant.SHOW_TAB_CART);
        filter.addAction(Constant.SHOW_TAB_SEARCH);
        filter.addAction(Constant.SHOW_TAB_HOME);
        filter.addAction(Constant.GET_CART_INFO);
        registerReceiver(broadcastReceiver, filter);
    }

    private void initControls() {
        final Intent intent = getIntent();
        if (intent.getIntExtra("CODE", 0) == 99) {
            drawer.openDrawer(Gravity.START);
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.START);
            }
        });
        lnlAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalValue.myAccount != null) {
                    drawer.openDrawer(Gravity.START);
                } else {
                    showDialogLogin();
                }
            }
        });
        lnlAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(Gravity.START);
                Intent intent = new Intent(MainTabActivity.this, AboutUsActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        lnlOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalValue.myAccount != null) {
                    drawer.closeDrawer(Gravity.START);
                    // TODO: doannd
                    Intent intent = new Intent(MainTabActivity.this, OrderHistoryActivityV2.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
                } else {
                    showDialogLogin();
                }
            }
        });
        lnlInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalValue.myAccount != null) {
                    drawer.closeDrawer(Gravity.START);
                    Intent intent = new Intent(MainTabActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                } else {
                    showDialogLogin();

                }

            }
        });
        lnlFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalValue.myAccount != null) {
                    drawer.closeDrawer(Gravity.START);
                    Intent intent1 = new Intent(MainTabActivity.this, FeedBackActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                } else {
                    showDialogLogin();
                }

            }
        });
        layoutChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalValue.myAccount != null) {
                    gotoActivity(ListChatActivity.class);
                } else {
                    showDialogLogin();
                }
            }
        });
        layoutShopManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(ShopManagementActivity.class);
            }
        });
        layoutManageOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(ManageOrderActivity.class);
            }
        });
        lnlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalValue.myAccount != null) {
                    showLogoutConfirmDialog();
                } else {
                    showDialogLogin();
                }
            }
        });

        /**
         * Create by Nguyen Dinh Doan 2019-10-01
         */
        layoutConvertToShopOwner.setOnClickListener(view -> {
            if (mySharedPreferences.getUserInfo().getWaitApproveShopOwner() == 1 ||
                mySharedPreferences.getUserInfo().getIsCannotSendRequest().equals("1")) {
                ConvertShopOwnerActivity.startActivity(this);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
            } else {
                mDialog = new Dialog(this);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.setContentView(R.layout.dialog_convert_to_shop_onwer);

                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCancelable(false);

                Window window = mDialog.getWindow();
                if (window != null) {
                    int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    window.setLayout(width, height);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }

                Button btnNo = mDialog.findViewById(R.id.btn_no);
                Button btnYes = mDialog.findViewById(R.id.btn_yes);

                // choose no
                btnNo.setOnClickListener(view1 -> {
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                });

                // choose convert to shop owner
                btnYes.setOnClickListener(view2 -> {
                    ConvertShopOwnerActivity.startActivity(this);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
                    // hide dialog
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                });

                mDialog.show();
            }

        });
    }

    private void showToast(int messageResId) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        lnlInfor = findViewById(R.id.lnlInfor);
        lnlFeedback = findViewById(R.id.lnlFeedback);
        lnlLogout = findViewById(R.id.lnlLogout);
        lnlOrder = findViewById(R.id.lnlOrder);
        tvName = findViewById(R.id.tvName);
        btnClose = findViewById(R.id.btnClose);
        lnlAccount = findViewById(R.id.lnlAccount);
        lnlAboutUs = findViewById(R.id.lnlAboutUs);
        layoutChat = findViewById(R.id.layout_chat);
        layoutShopManagement = findViewById(R.id.layout_shop_management);
        layoutManageOrder = findViewById(R.id.layout_manage_order);
        layoutConvertToShopOwner = findViewById(R.id.layout_convert_to_shop_owner);
        tvOrderSumCount = findViewById(R.id.tv_order_sum_count);
        tvOrderNewCount = findViewById(R.id.tv_new_order_count);
        tvStatusOrderChangedCount = findViewById(R.id.tv_status_order_changed_count);
        tvChatCount = findViewById(R.id.tv_chat_count);

        if (GlobalValue.myAccount != null) {
            String userRole = GlobalValue.myAccount.getRole();
            if (userRole != null && userRole.equals(Constant.ROLE_SHOP_OWNER)) {
                showTabShopOwner();
                hideTabConvertToShopOwner();
            } else if (userRole != null && userRole.equals(Constant.ROLE_CUSTOMER)) {
                hideTabShopOwner();
                showTabConvertToShopOwner();
            } else {
                Log.e(TAG, "Warning user not role");
                hideTabShopOwner();
                showTabConvertToShopOwner();
            }
        } else {
            Log.e(TAG, "Account null");
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

                                if (!newOrderCount.equals("0")) {
                                    tvOrderNewCount.setVisibility(View.VISIBLE);
                                    tvOrderNewCount.setText(newOrderCount);
                                }

                                if (!newStatusChangedCount.equals("0")) {
                                    tvStatusOrderChangedCount.setVisibility(View.VISIBLE);
                                    tvStatusOrderChangedCount.setText(newStatusChangedCount);
                                }

                                int notificationOrderSum = Integer.parseInt(newOrderCount) + Integer.parseInt(newStatusChangedCount) + chatCount;
                                if (notificationOrderSum != 0) {
                                    tvOrderSumCount.setVisibility(View.VISIBLE);
                                    tvOrderSumCount.setText(String.valueOf(notificationOrderSum));
                                }

                            } else {
                                showToast(ParserUtility.getMessage(object.toString()));
                            }
                        }
                    });
        } else {
            showToast(R.string.no_connection);
        }
    }

    private void hideTabConvertToShopOwner() {
        layoutConvertToShopOwner.setVisibility(View.GONE);
    }

    private void showTabConvertToShopOwner() {
        layoutConvertToShopOwner.setVisibility(View.VISIBLE);
    }

    private void showTabShopOwner() {
        layoutShopManagement.setVisibility(View.VISIBLE);
        layoutManageOrder.setVisibility(View.VISIBLE);
    }

    private void hideTabShopOwner() {
        layoutShopManagement.setVisibility(View.GONE);
        layoutManageOrder.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshContent();
        if (GlobalValue.arrMyMenuShop.size() != 0) {
            tabHost.getTabWidget().getChildTabViewAt(TAB_MY_CART).setBackgroundColor(Color.BLUE);
        } else {
            tabHost.getTabWidget().getChildTabViewAt(TAB_MY_CART).setBackgroundColor(getResources().getColor(R.color.background_new));
        }
    }

    private void refreshContent() {
//        updateIconSelected();
        if (tabHost.getCurrentTab() == TAB_ACCOUNT) {
            MainUserActivity activity = (MainUserActivity) getLocalActivityManager()
                    .getActivity(GlobalValue.KEY_TAB_ACCOUNT);
            activity.refreshContent();
        }
        if (GlobalValue.arrMyMenuShop.size() != 0) {
            tabHost.getTabWidget().getChildTabViewAt(TAB_MY_CART).setBackgroundColor(Color.BLUE);
        } else {
            tabHost.getTabWidget().getChildTabViewAt(TAB_MY_CART).setBackgroundColor(getResources().getColor(R.color.background_new));
        }
    }

    public void showFragment(int fragmentIndex) {
        if (tabHost.getCurrentTab() == TAB_ACCOUNT) {
            MainUserActivity activity = (MainUserActivity) getLocalActivityManager()
                    .getActivity(GlobalValue.KEY_TAB_ACCOUNT);
            activity.showFragment(fragmentIndex);
        }
    }

    private void initTabPages() {

        tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec(GlobalValue.KEY_TAB_HOME)
                .setIndicator(createTabIndicator(R.drawable.ic_hom0e, this.getString(R.string.home_upper_case)))
                .setContent(new Intent(this, HomeActivity.class)));

        tabHost.addTab(tabHost
                .newTabSpec(GlobalValue.KEY_TAB_SEARCH)
                .setIndicator(
                        createTabIndicator(R.drawable.ic_sear8ch, this.getString(R.string.search_upper_case)))
                .setContent(new Intent(this, SearchActivity.class)));

        tabHost.addTab(tabHost
                .newTabSpec(GlobalValue.KEY_TAB_MY_MENU)
                .setIndicator(
                        createTabIndicator(R.drawable.ic_cart_6, this.getString(R.string.my_cart_upper_case)))
                .setContent(new Intent(this, MainCartActivity.class)));
        tabHost.addTab(tabHost
                .newTabSpec(GlobalValue.KEY_TAB_ACCOUNT)
                .setIndicator(
                        createTabIndicator(R.drawable.ic_cart_6, this.getString(R.string.my_cart_upper_case)))
                .setContent(new Intent(this, MainUserActivity.class)));

        tabHost.getTabWidget().getChildAt(3).setVisibility(View.GONE);


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub
//                updateIconSelected();
                if (tabId.equals(GlobalValue.KEY_TAB_MY_MENU)) {
                    new MySharedPreferences(MainTabActivity.this).setFirstOpenSearchScreen(true);
                    MainCartActivity activity = (MainCartActivity) getLocalActivityManager()
                            .getActivity(GlobalValue.KEY_TAB_MY_MENU);
                    activity.refreshContent();

                } else if (tabId.equals(GlobalValue.KEY_TAB_HOME)) {
                    new MySharedPreferences(MainTabActivity.this).setFirstOpenSearchScreen(true);
                } else if (tabId.equals(GlobalValue.KEY_TAB_SEARCH)) {

                }
            }
        });

        tabHost.setCurrentTab(TAB_HOME);
//        updateIconSelected();
    }

    public void updateIconSelected() {
        TabWidget tabwidget = tabHost.getTabWidget();
        //check login or not

        if (tabHost.getCurrentTab() == TAB_HOME) {
            tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_hom0e);
            ((TextView) tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.text))
                    .setTextColor(Color.WHITE);
            tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_sear8ch);
            ((TextView) tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
            tabwidget.getChildTabViewAt(TAB_MY_CART).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_cart_6);
            ((TextView) tabwidget.getChildTabViewAt(TAB_MY_CART).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
            tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_account_gray_64);
            ((TextView) tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));


        } else if (tabHost.getCurrentTab() == TAB_SEARCH) {
            tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_hom0e);
            ((TextView) tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
            tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_sear8ch);
            ((TextView) tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.text))
                    .setTextColor(Color.WHITE);
            tabwidget.getChildTabViewAt(TAB_MY_CART).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_cart_6);
            ((TextView) tabwidget.getChildTabViewAt(TAB_MY_CART).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
            tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_account_gray_64);
            ((TextView) tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
        } else if (tabHost.getCurrentTab() == TAB_MY_CART) {
            tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_hom0e);
            ((TextView) tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
            tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_sear8ch);
            ((TextView) tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
            tabwidget.getChildTabViewAt(TAB_MY_CART).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_cart_6);
            ((TextView) tabwidget.getChildTabViewAt(TAB_MY_CART).findViewById(R.id.text))
                    .setTextColor(Color.WHITE);
            tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_account_gray_64);
            ((TextView) tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
        } else if (tabHost.getCurrentTab() == TAB_ACCOUNT) {

            tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_hom0e);
            ((TextView) tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
            tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_sear8ch);
            ((TextView) tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
            tabwidget.getChildTabViewAt(TAB_MY_CART).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_cart_6);
            ((TextView) tabwidget.getChildTabViewAt(TAB_MY_CART).findViewById(R.id.text))
                    .setTextColor(getResources().getColor(R.color.gray_home_bottom));
            tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_account_white_64);
            ((TextView) tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.text))
                    .setTextColor(Color.WHITE);
        }
    }


    private View createTabIndicator(int resource, String text) {
        View tabIndicator = getLayoutInflater()
                .inflate(R.layout.view_tab, null);
        ImageView image = (ImageView) tabIndicator.findViewById(R.id.imgIcon);
        image.setBackgroundResource(resource);
        TextView content = (TextView) tabIndicator.findViewById(R.id.text);
        content.setText(text);
        return tabIndicator;
    }

    public void gotoActivity(Class<?> cla) {
        Intent intent = new Intent(this, cla);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
    }

    public void gotoActivity(Class<?> cla, Bundle bundle) {
        Intent intent = new Intent(this, cla);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        showQuitDialog();
    }

    private void showQuitDialog() {

//        DialogUtility.showYesNoDialog(self, R.string.message_quit_app, R.string.yes, R.string.no, new OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                if (GlobalValue.myAccount != null) {
////                    GlobalValue.myAccount = null;
////                }
////                new MySharedPreferences(self).setCacheUserInfo("");
////                new MySharedPreferences(getApplicationContext()).clearAccount();
////                finishAffinity();
////
//////                System.exit(0);
////            }
////        });
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_confirm);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        self.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = 6 * (displaymetrics.widthPixels / 7);
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvTitle = mDialog.findViewById(R.id.tvTitle);
        TextView tvContent = mDialog.findViewById(R.id.tvContent);
        TextView tvCancel = mDialog.findViewById(R.id.tvCancel);
        TextView tvConfirm = mDialog.findViewById(R.id.tvConfirm);
        tvContent.setText(R.string.message_quit_app);
        tvConfirm.setText(R.string.yes);
        tvCancel.setText(R.string.no);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalValue.myAccount != null) {
                    GlobalValue.myAccount = null;
                }
                new MySharedPreferences(self).setCacheUserInfo("");
                new MySharedPreferences(getApplicationContext()).clearAccount();
                finishAffinity();
                mDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });


        mDialog.show();
    }

    public void showLogoutConfirmDialog() {

//        AlertDialog.Builder build = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog)).setTitle("Log out")
//                .setMessage(getString(R.string.message_log_out))
//                .setNegativeButton(getString(R.string.yes), new OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        if (GlobalValue.myAccount != null)
//                            GlobalValue.myAccount = null;
//                        new MySharedPreferences(getApplicationContext()).clearAccount();
//                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        getApplicationContext().startActivity(intent);
//                    }
//                }).setPositiveButton(getString(R.string.no), new OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        dialog.dismiss();
//                    }
//                });
//
//        build.show();
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_confirm);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        self.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = 6 * (displaymetrics.widthPixels / 7);
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvTitle = mDialog.findViewById(R.id.tvTitle);
        TextView tvContent = mDialog.findViewById(R.id.tvContent);
        TextView tvCancel = mDialog.findViewById(R.id.tvCancel);
        TextView tvConfirm = mDialog.findViewById(R.id.tvConfirm);
        tvContent.setText(R.string.logout);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                mDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });


        mDialog.show();


    }

    private void logout() {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            String fcmId = mySharedPreferences.getStringValue(Constant.TOKEN_FCM, "");
            ModelManager.logout(mySharedPreferences.getUserInfo().getId(), fcmId, this,
                    true, new ModelManagerListener() {
                        @Override
                        public void onError(VolleyError error) {
                            Toast.makeText(MainTabActivity.this,
                                    ErrorNetworkHandler.processError(error), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(Object object) {
                            if (ParserUtility.isSuccess(object.toString())) {
                                if (GlobalValue.myAccount != null) {
                                    GlobalValue.myAccount = null;
                                }
                                new MySharedPreferences(getApplicationContext()).clearAccount();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                                ServiceManager.getInstance(getBaseContext()).unregisterService(MessageService.class);

                            } else {
                                Toast.makeText(MainTabActivity.this,
                                        ParserUtility.getMessage(object.toString()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        unregisterReceiver(broadcastReceiver);
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




                        }
                    }

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

                    if (chatCount > 0) {
                        tvChatCount.setVisibility(View.VISIBLE);
                        tvChatCount.setText(String.valueOf(chatCount));
                    } else {
                        tvChatCount.setVisibility(View.GONE);
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
package com.libyasolutions.libyamarketplace.activity.tabs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.user.AccountFragment;
import com.libyasolutions.libyamarketplace.activity.tabs.user.LoginFragment;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

public class MainUserActivity extends FragmentActivity {

    public static final int LOGIN_PAGE = 0;
    public static final int MYACC_PAGE = 1;
    public static final int REGISTER_PAGE = 2;
    public static final int HISTORY_PAGE = 3;
    public static final int FEEDBACK_PAGE = 4;
    public Fragment[] listFragments;
    private FragmentManager fm;
    public int curFragment;
    public int preFragment;
    private MySharedPreferences mySharedPreferences;

    private boolean isLoadNew = true;

    public boolean isLoadNew() {
        return isLoadNew;
    }

    public void setLoadNew(boolean isLoadNew) {
        this.isLoadNew = isLoadNew;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set view
        mySharedPreferences = new MySharedPreferences(this);
        setContentView(R.layout.activity_main_tab_user);
        initUI();
        initControl();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initUI() {
        fm = getSupportFragmentManager();
        listFragments = new Fragment[5];
        listFragments[LOGIN_PAGE] = fm.findFragmentById(R.id.fragmentLogin);
        listFragments[REGISTER_PAGE] = fm
                .findFragmentById(R.id.fragmentRegister);
        listFragments[MYACC_PAGE] = fm.findFragmentById(R.id.fragmentMyAcc);
        listFragments[HISTORY_PAGE] = fm.findFragmentById(R.id.fragmentHistory);

    }

    private void initControl() {
        refreshContent();
    }

    public void refreshContent() {
        if (GlobalValue.myAccount == null) {
            LoginFragment fm = (LoginFragment) listFragments[LOGIN_PAGE];
            fm.refreshContent();
            showFragment(LOGIN_PAGE);
        } else {
            AccountFragment fm = (AccountFragment) listFragments[MYACC_PAGE];
            fm.refreshContent();
            showFragment(MYACC_PAGE);
        }
//        ((MainTabActivity) this.getParent()).updateIconSelected();
    }

    public void showFragment(int fragmentIndex) {
        preFragment = curFragment;
        curFragment = fragmentIndex;
        FragmentTransaction transaction = fm.beginTransaction();

        for (int i = 0; i < listFragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(listFragments[i]);
            } else {
                transaction.hide(listFragments[i]);
            }
        }

        transaction.commit();
    }

    public void gotoFragment(int fragment) {
        preFragment = curFragment;
        curFragment = fragment;
        Log.e("huy-log", "current-fragment 1:" + curFragment);
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.setCustomAnimations(R.anim.push_left_in,
                R.anim.push_left_out);
        for (Fragment item : listFragments) {
            transaction.hide(item);
        }
        transaction.show(listFragments[fragment]);
        transaction.commit();
    }

    public void backFragment(int fragment) {
        preFragment = curFragment;
        curFragment = fragment;
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.setCustomAnimations(R.anim.push_right_in,
                R.anim.push_right_out);
        transaction.hide(listFragments[preFragment]);
        transaction.show(listFragments[fragment]);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (curFragment != MYACC_PAGE && curFragment != LOGIN_PAGE) {
            backFragment(preFragment);
        } else {
            if (curFragment == LOGIN_PAGE) {
                this.getParent().onBackPressed();
            }
        }

    }

    public void showLogoutConfirmDialog() {

        AlertDialog.Builder build = new Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog)).setTitle("Log out")
                .setMessage(getString(R.string.message_log_out))
                .setNegativeButton(getString(R.string.yes), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }).setPositiveButton(getString(R.string.no), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

        build.show();
    }

    private void logout() {
        if (NetworkUtil.checkNetworkAvailable(this)) {
            String fcmId = mySharedPreferences.getStringValue(Constant.TOKEN_FCM, "");
            ModelManager.logout(mySharedPreferences.getUserInfo().getId(), fcmId, this,
                    true, new ModelManagerListener() {
                        @Override
                        public void onError(VolleyError error) {
                            Toast.makeText(MainUserActivity.this,
                                    ErrorNetworkHandler.processError(error), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(Object object) {
                            if (ParserUtility.isSuccess(object.toString())) {
                                if (GlobalValue.myAccount != null)
                                    GlobalValue.myAccount = null;
                                new MySharedPreferences(getApplicationContext()).clearAccount();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                getApplicationContext().startActivity(intent);
                            } else {
                                Toast.makeText(MainUserActivity.this,
                                        ParserUtility.getMessage(object.toString()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }
}

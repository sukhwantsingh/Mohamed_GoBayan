package com.libyasolutions.libyamarketplace.activity.tabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.cart.DeliveryInfoFragment;
import com.libyasolutions.libyamarketplace.activity.tabs.cart.ShopCartFragment;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

public class MainCartActivity extends FragmentActivity {

    public static final int PAGE_SHOP_CART = 0;
    public static final int PAGE_CONFIRM = 1;

    public Fragment[] listFragments;
    private FragmentManager fm;
    public int curFragment = -1;
    public int preFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab_cart);
        initUI();
        initControl();
        checkAccount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (curFragment == PAGE_SHOP_CART)
            refreshContent();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            refreshContent();
//        }
    }

    private void checkAccount() {
        if (GlobalValue.myAccount == null)
            GlobalValue.myAccount = ParserUtility.convertJsonStringtoAccount(new MySharedPreferences(this).getCachUserInfo());
    }

    private void initUI() {
        fm = getSupportFragmentManager();
        listFragments = new Fragment[2];
        listFragments[PAGE_SHOP_CART] = fm
                .findFragmentById(R.id.fragmentShopCart);
        listFragments[PAGE_CONFIRM] = fm
                .findFragmentById(R.id.fragmentDeliveryInfo);
    }

    private void initControl() {
        refreshContent();
    }

    public void refreshContent() {
        ShopCartFragment fm = (ShopCartFragment) listFragments[PAGE_SHOP_CART];
        fm.refreshContent();
        if (curFragment != PAGE_SHOP_CART)
            showFragment(PAGE_SHOP_CART);
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
        if (curFragment != PAGE_SHOP_CART) {
            backFragment(preFragment);
        } else {
            this.getParent().onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data
                    .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", "Payment OK. Response :"
                            + confirm.toJSONObject().toString(4));
                    Toast toast = Toast.makeText(this,
                            getString(R.string.payment_successfully), Toast.LENGTH_LONG);
                    toast.setGravity(
                            Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    if (curFragment == PAGE_CONFIRM) {
                        DeliveryInfoFragment fm = (DeliveryInfoFragment) listFragments[PAGE_CONFIRM];
                        fm.sendOrderInfoWithPaypalMethod();
                    }


                } catch (JSONException e) {
                    Log.e("paymentExample",
                            "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample",
                    "An invalid payment was submitted. Please see the docs.");
        }
    }
}

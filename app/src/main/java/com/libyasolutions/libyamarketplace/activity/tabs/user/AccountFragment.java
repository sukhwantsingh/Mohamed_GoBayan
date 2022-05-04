package com.libyasolutions.libyamarketplace.activity.tabs.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseFragment;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.MainUserActivity;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.config.WebServiceConfig;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.util.DialogUtility;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountFragment extends BaseFragment {
    private View view;
    private RelativeLayout btnMyAccount, btnPreferences, btnFavourites, btnOderHistory, btnFeedback, btnLogout;
    private TextView lblUsername, lblPreferencesContent, lblFavouriteContent, lblHistoriesContent;
    private MainUserActivity self;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_account, container, false);
        self = (MainUserActivity) getActivity();
        initUI(view);
        initControl();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            self = (MainUserActivity) getActivity();
            setData();
        }
    }

    @Override
    public void refreshContent() {
        super.refreshContent();
        reloadUserInfo();
    }

    private void setData() {
        if (GlobalValue.myAccount != null) {
            lblUsername.setText(GlobalValue.myAccount.getFull_name());
            lblPreferencesContent.setText(GlobalValue.myAccount.getPreferenceData(self));
            lblHistoriesContent.setText(GlobalValue.myAccount.getNumberOfOrders() + getString(R.string.orders_in_histories));
            String favouriteContent = GlobalValue.myAccount.getNumberOfFavouriteShops() + " "+getString(R.string.shops)+" &" + GlobalValue.myAccount.getNumberOfFavouriteProducts() + getString(R.string.products);
            lblFavouriteContent.setText(favouriteContent);
        }
    }

    private void reloadUserInfo() {
        ModelManager.getUserInforById(getCurrentActivity(), GlobalValue.myAccount.getId(), false,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        Account account = ParserUtility.parseAccount(json);
                        account.setPassword(GlobalValue.myAccount.getPassword());
                        if (account != null) {
                            GlobalValue.myAccount = account;
                            GlobalValue.myAccount.setType(Account.LOGIN_TYPE_NORMAL);
                            new MySharedPreferences(getCurrentActivity()).setCacheUserInfo(ParserUtility.convertAccountToJsonString(GlobalValue.myAccount));
                        }
                        setData();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        ErrorNetworkHandler.processError(error);
                    }
                });
    }


    private void initControl() {
        // TODO Auto-generated method stub
        btnOderHistory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (NetworkUtil.checkNetworkAvailable(self)) {
                    self.setLoadNew(true);
                    self.gotoFragment(MainUserActivity.HISTORY_PAGE);
                } else {
                    Toast.makeText(self,
                            R.string.message_network_is_unavailable,
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        btnFeedback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                self.gotoFragment(MainUserActivity.FEEDBACK_PAGE);
            }
        });
        btnPreferences.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                gotoActivity(PreferencesActivity.class);
            }
        });

        btnFavourites.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoActivity(FavoriteActivity.class);
            }
        });

        btnMyAccount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //  self.gotoFragment(MainUserActivity.INFO_PAGE);
                gotoActivity(EditProfileActivity.class);
            }
        });
        btnLogout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                self.showLogoutConfirmDialog();
            }
        });
    }

    private void checkUserRole() {
        if (GlobalValue.myAccount.getRole().equals(Account.ROLE_SHOP_OWNER)) {

        }
    }

    private void onClickRegisterShopOwner() {
        if (GlobalValue.myAccount.isUser()) {
            if (NetworkUtil.checkNetworkAvailable(self)) {
                //call api update register shop owner
                ModelManager.registerShopOwner(self, GlobalValue.myAccount.getId(), true, new ModelManagerListener() {
                    @Override
                    public void onError(VolleyError error) {
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(Object object) {
                        String response = object.toString();
                        checkResponse(response);
                    }
                });
            } else {
                Toast.makeText(self,
                        R.string.message_network_is_unavailable,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            DialogUtility.alert(self, R.string.message_permission_register_shop_owner);
        }
    }

    private void checkResponse(String response) {
        JSONObject json = null;
        try {
            json = new JSONObject(response);
            if (json.getString(WebServiceConfig.KEY_STATUS).equalsIgnoreCase(
                        WebServiceConfig.KEY_STATUS_SUCCESS)) {
                    DialogUtility.alert(self, R.string.message_send_request_successfully);
            } else {
                String message = json.getString(WebServiceConfig.KEY_MESSAGE);
                DialogUtility.alert(self, message);
            }
        } catch (JSONException e) {
            DialogUtility.alert(self, R.string.message_error_undefined);
        }
    }

    // Intent intent = new Intent(this, HomeActivity.class);
    // startActivity(intent);
    private void initUI(View view) {
        // TODO Auto-generated method stub
        btnMyAccount = (RelativeLayout) view.findViewById(R.id.btnMyAccount);
        btnPreferences = (RelativeLayout) view.findViewById(R.id.btnPreferences);
        btnFavourites = (RelativeLayout) view.findViewById(R.id.btnFavourites);
        btnOderHistory = (RelativeLayout) view.findViewById(R.id.btnHistoryOrder);
        btnFeedback = (RelativeLayout) view.findViewById(R.id.btnFeedback);
        btnLogout = (RelativeLayout) view.findViewById(R.id.btnLogout);

        //----
        lblUsername = (TextView) view.findViewById(R.id.lblUserName);
        lblPreferencesContent = (TextView) view.findViewById(R.id.lblPreferenceContent);
        lblFavouriteContent = (TextView) view.findViewById(R.id.lblFavouriteContent);
        lblHistoriesContent = (TextView) view.findViewById(R.id.lblOrderContent);

    }
}

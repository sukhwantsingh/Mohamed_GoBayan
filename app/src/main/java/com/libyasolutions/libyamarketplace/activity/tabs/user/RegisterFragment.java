package com.libyasolutions.libyamarketplace.activity.tabs.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import android.content.Context;
import com.libyasolutions.libyamarketplace.BaseFragment;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.MainUserActivity;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.WebServiceConfig;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.util.CustomToast;
import com.libyasolutions.libyamarketplace.util.Logger;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.StringUtility;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class RegisterFragment extends BaseFragment implements OnClickListener {

    public static final String MESSAGE_SUCCESS = "success";
    private View view;
    private EditText txtUser, txtPass, txtRePass, txtEmail, txtFullName,
            txtPhone, txtAddress, txtCity, txtZipCode;
    private Button btnRegister;
    private ImageView btnBack;
    private Account account = null;
    private MySharedPreferences mySharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_register, container, false);
        mySharedPreferences = new MySharedPreferences(getActivity());
        initUI(view);
        initControls();
        return view;
    }

    private void initUI(View view) {
        txtUser = (EditText) view.findViewById(R.id.txtUserName);
        txtPass = (EditText) view.findViewById(R.id.txtPassWord);
        txtRePass = (EditText) view.findViewById(R.id.txtRePassWord);
        txtEmail = (EditText) view.findViewById(R.id.txtEmail);
        txtFullName = (EditText) view.findViewById(R.id.txtFullName);
        txtPhone = (EditText) view.findViewById(R.id.txtPhone);
        txtAddress = (EditText) view.findViewById(R.id.txtAddress);
        txtCity = (EditText) view.findViewById(R.id.txtCity);
        txtZipCode = (EditText) view.findViewById(R.id.txtZipcode);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
    }

    private void initControls() {
        btnRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnRegister) {
            String message = validateForm();
            if (!message.equals(MESSAGE_SUCCESS)) {
                CustomToast.showCustomAlert(getCurrentActivity(), message,
                        Toast.LENGTH_SHORT);
            } else {
                if (account != null) {
                    if (NetworkUtil.checkNetworkAvailable(getCurrentActivity())) {
                        register(account);
                    } else {
                        Toast.makeText(getCurrentActivity(),
                                R.string.message_network_is_unavailable,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

        } else if (v == btnBack) {
            // hidden keyboard :
            hiddenKeyboard();
            MainUserActivity activity = (MainUserActivity) getCurrentActivity();
            activity.backFragment(activity.preFragment);
        }
    }

    private String validateForm() {
        String message = MESSAGE_SUCCESS;

        String username = txtUser.getText().toString();
        String pass = txtPass.getText().toString();
        String repass = txtRePass.getText().toString();
        String email = txtEmail.getText().toString();
        String fullname = txtFullName.getText().toString();
        String phone = txtPhone.getText().toString();
        String address = txtAddress.getText().toString();
        String city = txtCity.getText().toString();
        String zipCode = txtZipCode.getText().toString();
        // username
        if (username.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_UserName_empty);
            return message;
        }
        // password
        if (pass.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_Password_empty);
            return message;
        }
        // repassword
        if (repass.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_RePassword_empty);
            return message;
        } else {
            if (!repass.equals(pass)) {
                message = getCurrentActivity().getResources().getString(
                        R.string.error_RePassword_empty);
                return message;
            }
        }
        // email
        if (email.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_Email_empty);
            return message;
        } else {
            if (!StringUtility.isEmailValid(email)) {
                message = getCurrentActivity().getResources().getString(
                        R.string.error_Email_wrong);
                return message;
            }
        }
        // fullname
        if (fullname.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_FullName);
            return message;
        }

        // phone
        if (phone.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_Phone);
            return message;
        }

        // address
        if (address.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_Address);
            return message;
        }
        // city
        if (city.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_city);
            return message;
        }
        // zipcode
        if (zipCode.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_zip_code);
            return message;
        }

        account = new Account();
        account.setUserName(username.trim());
        account.setPassword(pass.trim());
        account.setEmail(email.trim());
        account.setPhone(phone.trim());
        account.setFull_name(fullname.trim());
        account.setAddress(address.trim());
        account.setCity(city.trim());
        account.setZipCode(zipCode.trim());
        account.setType("0");

        return message;
    }

    private void register(Account acc) {
        String data = createAccountJson(acc);
        Logger.e("ACCOUNT INFO", "ACCOUNT INFO : " + data);

        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String ime;

        if (Build.VERSION.SDK_INT >= 26) {
            ime = telephonyManager.getImei();
        } else {
            ime = telephonyManager.getDeviceId();
        }
        String fcmId = mySharedPreferences.getStringValue(Constant.TOKEN_FCM, "");

        ModelManager.register(getCurrentActivity(), data
                , ime, fcmId, "1", true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        String strJson = (String) object;
                        if (ParserUtility.isSuccess(strJson)) {
                            CustomToast.showCustomAlert(getCurrentActivity(),
                                    checkResult(strJson), Toast.LENGTH_SHORT);
                            MainUserActivity activity = (MainUserActivity) getCurrentActivity();
                            activity.refreshContent();
                        } else {
                            CustomToast.showCustomAlert(getCurrentActivity(),
                                    checkResult(strJson), Toast.LENGTH_SHORT);
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String createAccountJson(Account acc) {
        JSONObject json = new JSONObject();
        try {
            json.put(WebServiceConfig.KEY_ACCOUNT_USER_NAME, acc.getUserName());
            json.put(WebServiceConfig.KEY_ACCOUNT_PASSWORD, acc.getPassword());
            json.put(WebServiceConfig.KEY_ACCOUNT_EMAIL, acc.getEmail());
            json.put(WebServiceConfig.KEY_ACCOUNT_FULL_NAME, acc.getFull_name());
            json.put(WebServiceConfig.KEY_ACCOUNT_PHONE, acc.getPhone());
            json.put(WebServiceConfig.KEY_ACCOUNT_ADDRESS, acc.getAddress());
            json.put(WebServiceConfig.KEY_ACCOUNT_CITY, acc.getCity());
            json.put(WebServiceConfig.KEY_ACCOUNT_ZIP_CODE, acc.getZipCode());
            json.put("type", acc.getType());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json.toString();
    }

    private String checkResult(String strjson) {
        JSONObject json = null;
        String message = "";
        try {
            json = new JSONObject(strjson);
            if (json.getString(WebServiceConfig.KEY_STATUS).equals(
                    WebServiceConfig.KEY_STATUS_SUCCESS)) {
                message = getCurrentActivity().getString(
                        R.string.message_success);
            } else {
                message = json.getString(WebServiceConfig.KEY_MESSAGE);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return message;
    }
}

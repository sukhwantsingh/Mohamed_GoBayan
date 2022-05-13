package com.libyasolutions.libyamarketplace.activity.tabs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import android.content.Context;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
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

public class RegisterActivity extends BaseActivity {
    public static final String MESSAGE_SUCCESS = "success";
    private EditText txtUser, txtPass, txtRePass, txtEmail, txtFullName,
            txtPhone, txtAddress, txtCity, txtZipCode;
    private Button btnRegister;
    private Account account = null;
    private TextView btnBack;
    private MySharedPreferences mySharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        mySharedPreferences = new MySharedPreferences(this);
        initUI();
        initControl();
    }

    private void initUI() {
        txtUser = (EditText) findViewById(R.id.txtUserName);
        txtPass = (EditText) findViewById(R.id.txtPassWord);
        txtRePass = (EditText) findViewById(R.id.txtRePassWord);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtFullName = (EditText) findViewById(R.id.txtFullName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtCity = (EditText) findViewById(R.id.txtCity);
        txtZipCode = (EditText) findViewById(R.id.txtZipcode);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnBack=findViewById(R.id.btnBack);
    }

    private void initControl() {
        btnRegister.setOnClickListener(v -> {
            String message = validateForm();
            if (!message.equals(MESSAGE_SUCCESS)) {
                CustomToast.showCustomAlert(RegisterActivity.this, message,
                        Toast.LENGTH_SHORT);
            } else {
                if (account != null) {
                    if (NetworkUtil.checkNetworkAvailable(RegisterActivity.this)) {
                        register(account);
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                R.string.message_network_is_unavailable,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnBack.setOnClickListener(v -> onBackPressed());
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
            message = getResources().getString(
                    R.string.error_UserName_empty);
            return message;
        }
        // password
        if (pass.isEmpty()) {
            message = getResources().getString(
                    R.string.error_Password_empty);
            return message;
        }
        // repassword
        if (repass.isEmpty()) {
            message = getResources().getString(
                    R.string.error_RePassword_empty);
            return message;
        } else {
            if (!repass.equals(pass)) {
                message = getResources().getString(
                        R.string.error_RePassword_empty);
                return message;
            }
        }
        // email
        if (email.isEmpty()) {
            message = getResources().getString(
                    R.string.error_Email_empty);
            return message;
        } else {
            if (!StringUtility.isEmailValid(email)) {
                message = getResources().getString(
                        R.string.error_Email_wrong);
                return message;
            }
        }
        // fullname
        if (fullname.isEmpty()) {
            message = getResources().getString(
                    R.string.error_FullName);
            return message;
        }

        // phone
        if (phone.isEmpty()) {
            message = getResources().getString(
                    R.string.error_Phone);
            return message;
        }

        // address
        if (address.isEmpty()) {
            message = getResources().getString(
                    R.string.error_Address);
            return message;
        }
        // city
        if (city.isEmpty()) {
            message = getResources().getString(
                    R.string.error_city);
            return message;
        }
        // zipcode
//        if (zipCode.isEmpty()) {
//            message = getResources().getString(
//                    R.string.error_zip_code);
//            return message;
//        }

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

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String ime;

        if (Build.VERSION.SDK_INT >= 26) {
            ime = telephonyManager.getImei();
        } else {
            ime = telephonyManager.getDeviceId();
        }
        String fcmId = mySharedPreferences.getStringValue(Constant.TOKEN_FCM, "");

        ModelManager.register(RegisterActivity.this, data,
                ime, fcmId, "1", true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        String strJson = (String) object;
                        if (ParserUtility.isSuccess(strJson)) {
                            CustomToast.showCustomAlert(RegisterActivity.this,
                                    checkResult(strJson), Toast.LENGTH_SHORT);
                            gotoActivity(LoginActivity.class);
                        } else {
                            CustomToast.showCustomAlert(RegisterActivity.this,
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
                message = getResources().getString(
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

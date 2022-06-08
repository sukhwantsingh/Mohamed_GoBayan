package com.libyasolutions.libyamarketplace.activity.tabs.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.MainTabActivity;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.config.WebServiceConfig;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.StringUtility;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class EditProfileActivity extends BaseActivity implements OnClickListener {

    public static final String MESSAGE_SUCCESS = "success";
    private EditText txtPass, txtEmail, txtFullName,
            txtPhone, txtAddress, txtCity, txtZipCode;
    private EditText txtUserName;
    private TextView btnUpdate,lblTitle;
    private ImageView btnBack;
    private Account accountTemp = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initUI();
        initControls();
        initData();
    }

    private void initData() {
         lblTitle.setText("");

         if (GlobalValue.myAccount != null) {
            try {
                accountTemp = GlobalValue.myAccount.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (accountTemp != null) {
                txtUserName.setText(accountTemp.getUserName());
                txtFullName.setText(accountTemp.getFull_name());
                txtEmail.setText(accountTemp.getEmail());
                txtPhone.setText(accountTemp.getPhone());
                txtAddress.setText(accountTemp.getAddress());
                txtCity.setText(accountTemp.getCity());
                txtZipCode.setText(accountTemp.getZipCode());
            }
        }
        hiddenKeyboard();
    }

    private void initUI() {
        lblTitle =  findViewById(R.id.lblTitle);
        txtUserName =  findViewById(R.id.txtUserName);
        txtPass = findViewById(R.id.txtPassWord);
        txtEmail =  findViewById(R.id.txtEmail);
        txtFullName = findViewById(R.id.txtFullName);
        txtPhone =  findViewById(R.id.txtPhone);
        txtAddress =  findViewById(R.id.txtAddress);
        txtCity =findViewById(R.id.txtCity);
        txtZipCode =  findViewById(R.id.txtZipcode);
        btnBack = findViewById(R.id.imageBack);
        btnUpdate =  findViewById(R.id.btnRegister);
        btnUpdate.setText(getString(R.string.update));

    }

    private void initControls() {
        btnUpdate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnUpdate) {
            String message = validateForm();
            if (!message.equals(MESSAGE_SUCCESS)) {
                Toast.makeText(self,
                        message,
                        Toast.LENGTH_LONG).show();
            } else {
                if (accountTemp != null) {
                    if (NetworkUtil.checkNetworkAvailable(self)) {
                        updateProfile();
                    } else {
                        Toast.makeText(self,
                                R.string.message_network_is_unavailable,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

        } else if (v == btnBack) {
            Intent intent = new Intent(EditProfileActivity.this, MainTabActivity.class);
            intent.putExtra("CODE", 99);
            startActivity(intent);
        }
    }

    private String validateForm() {
        String message = MESSAGE_SUCCESS;

        String username = txtUserName.getText().toString();
        String pass = txtPass.getText().toString();
        String email = txtEmail.getText().toString();
        String fullname = txtFullName.getText().toString();
        String phone = txtPhone.getText().toString();
        String address = txtAddress.getText().toString();
        String city = txtCity.getText().toString();
        String zipCode = txtZipCode.getText().toString();

        // username
        if (username.isEmpty()) {
            message = self.getResources().getString(
                    R.string.error_UserName_empty);
            return message;
        }
        // password
        if (!pass.isEmpty()) {
            message = self.getResources().getString(
                    R.string.error_Password_empty);
            return message;
        }

        // email
        if (email.isEmpty()) {
            message = self.getResources().getString(
                    R.string.error_Email_empty);
            return message;
        } else {
            if (!StringUtility.isEmailValid(email)) {
                message = self.getResources().getString(
                        R.string.error_Email_wrong);
                return message;
            }
        }
        // fullname
        if (fullname.isEmpty()) {
            message = self.getResources().getString(
                    R.string.error_FullName);
            return message;
        }

        // phone
        if (phone.isEmpty()) {
            message = self.getResources().getString(
                    R.string.error_Phone);
            return message;
        }

        // address
        if (address.isEmpty()) {
            message = self.getResources().getString(
                    R.string.error_Address);
            return message;
        }
        // city
        if (city.isEmpty()) {
            message = self.getResources().getString(
                    R.string.error_city);
            return message;
        }
        // zipcode
        if (zipCode.isEmpty()) {
            message = self.getResources().getString(
                    R.string.error_zip_code);
            return message;
        }

        accountTemp.setPassword(pass.trim());
        accountTemp.setEmail(email.trim());
        accountTemp.setPhone(phone.trim());
        accountTemp.setFull_name(fullname.trim());
        accountTemp.setAddress(address.trim());
        accountTemp.setCity(city.trim());
        accountTemp.setZipCode(zipCode.trim());

        return message;
    }

    private boolean isNewUpdate() {
        if (!accountTemp.getFull_name().equals(GlobalValue.myAccount.getFull_name()))
            return true;
        else if (!accountTemp.getEmail().equals(GlobalValue.myAccount.getEmail()))
            return true;
        else if (!accountTemp.getPhone().equals(GlobalValue.myAccount.getPhone()))
            return true;
        else if (!accountTemp.getAddress().equals(GlobalValue.myAccount.getAddress()))
            return true;
        else if (!accountTemp.getCity().equals(GlobalValue.myAccount.getCity()))
            return true;
        else if (!accountTemp.getZipCode().equals(GlobalValue.myAccount.getZipCode()))
            return true;
        else if (!accountTemp.getPassword().isEmpty())
            return true;
        else
            return false;
    }

    private void updateProfile() {
        if (!isNewUpdate()) {
            Toast.makeText(self, R.string.message_nothing_changed_for_updating, Toast.LENGTH_LONG).show();
            return;
        }

        ModelManager.updateProfile(self, accountTemp, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        String strJson = (String) object;
                        if (ParserUtility.isSuccess(strJson)) {
                            Toast.makeText(self, checkResult(strJson), Toast.LENGTH_LONG).show();
                            //saved cache data
                            GlobalValue.myAccount.setFull_name(accountTemp.getFull_name());
                            GlobalValue.myAccount.setEmail(accountTemp.getEmail());
                            GlobalValue.myAccount.setPhone(accountTemp.getPhone());
                            GlobalValue.myAccount.setAddress(accountTemp.getAddress());
                            GlobalValue.myAccount.setCity(accountTemp.getCity());
                            GlobalValue.myAccount.setZipCode(accountTemp.getZipCode());
                            new MySharedPreferences(self).setCacheUserInfo(ParserUtility.convertAccountToJsonString(GlobalValue.myAccount));

                        } else {
                            Toast.makeText(self, checkResult(strJson), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }


    private String checkResult(String strjson) {
        JSONObject json = null;
        String message = "";
        try {
            json = new JSONObject(strjson);
            if (json.getString(WebServiceConfig.KEY_STATUS).equals(
                    WebServiceConfig.KEY_STATUS_SUCCESS)) {
                message = self.getString(
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

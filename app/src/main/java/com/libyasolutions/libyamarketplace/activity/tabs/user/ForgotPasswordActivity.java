package com.libyasolutions.libyamarketplace.activity.tabs.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.LoginActivity;
import com.libyasolutions.libyamarketplace.config.WebServiceConfig;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.StringUtility;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class ForgotPasswordActivity extends BaseActivity implements OnClickListener {

    public static final String MESSAGE_SUCCESS = "success";
    private EditText txtEmail;
    private Button btnSend;
    private ImageView btnBack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initUI();
        initControls();
    }

    private void initData() {
        hiddenKeyboard();
    }

    private void initUI() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnBack = (ImageView) findViewById(R.id.btnBack);
    }

    private void initControls() {
        btnSend.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnSend) {
            String message = validateForm();
            if (!message.equals(MESSAGE_SUCCESS)) {
                Toast.makeText(self,
                        message,
                        Toast.LENGTH_LONG).show();
            } else {
                if (NetworkUtil.checkNetworkAvailable(self)) {
                    sendForgetPasswordRequest();
                } else {
                    Toast.makeText(self,
                            R.string.message_network_is_unavailable,
                            Toast.LENGTH_LONG).show();
                }
            }

        } else if (v == btnBack) {
            onBackPressed();
        }
    }

    private String validateForm() {
        String message = MESSAGE_SUCCESS;
        String email = txtEmail.getText().toString();


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

        return message;
    }

    private void sendForgetPasswordRequest() {
        String email = txtEmail.getText().toString();
        ModelManager.forgotPassword(self, email, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        String strJson = (String) object;
                        if (ParserUtility.isSuccess(strJson)) {
                            Toast.makeText(self, checkResult(strJson), Toast.LENGTH_LONG).show();
                            gotoActivity(LoginActivity.class);
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

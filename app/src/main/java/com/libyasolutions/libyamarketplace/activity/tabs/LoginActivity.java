package com.libyasolutions.libyamarketplace.activity.tabs;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.view.ContextThemeWrapper;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import android.content.Context;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.BuildConfig;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.user.ForgotPasswordActivity;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.config.WebServiceConfig;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.service.MessageService;
import com.libyasolutions.libyamarketplace.service.ServiceManager;
import com.libyasolutions.libyamarketplace.util.CustomToast;
import com.libyasolutions.libyamarketplace.util.Logger;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.MyUtilityKt;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

public class LoginActivity extends BaseActivity {
    private EditText txtUser, txtPass;
    private TextView btnLogin, btnRegister;
    private String userName = "";
    private String passWord = "";
    private Account account = null;
    private MySharedPreferences mySharedPreferences;
    private CallbackManager callbackManager;
    private LinearLayout btnLoginFacebook;
    private TextView ivContinueAsGuest;
    private TextView btnResetPassword;
    private TextView changeLanguageOption;


    // ======= LOGIN BY FACEBOOK ===========

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        // Init UI
        // Prepare service
        self = LoginActivity.this;
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(self.getApplicationContext());
        mySharedPreferences = new MySharedPreferences(this);
        getKeyHash();
        initUI();
        initControls();

    }

    private void initUI() {
        changeLanguageOption = (TextView) findViewById(R.id.changeLangOption);
        txtUser = (EditText) findViewById(R.id.txtUserName);
        txtPass = (EditText) findViewById(R.id.txtPassWord);
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        btnResetPassword = findViewById(R.id.btnForgetPassword);
        ivContinueAsGuest = findViewById(R.id.iv_continue_as_guest);

        // show pre values
        changeLanguageOption.setText(MyUtilityKt.pref(this).getLanguageDisplayCode());

    }

    private void initControls() {
        changeLanguageOption.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(LoginActivity.this);

            builderSingle.setTitle(getResources().getString(R.string.select_language));

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LoginActivity.this,  android.R.layout.select_dialog_singlechoice);
            arrayAdapter.add("English");
            arrayAdapter.add("عربي");


            builderSingle.setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String language_code;
                if(which == 0) { language_code = "en";  }
                else { language_code = "ar";   }

                MyUtilityKt.changeAppLang(this, language_code);
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                finishAffinity();
            });
            builderSingle.show();
        });

        btnLogin.setOnClickListener(v -> {
            if (txtUser.getText().toString().isEmpty()
                    || txtPass.getText().toString().isEmpty()) {
                CustomToast.showCustomAlert(
                        LoginActivity.this, getString(
                                R.string.message_input_user_pass),
                        Toast.LENGTH_SHORT);
            } else {
                userName = txtUser.getText().toString();
                passWord = txtPass.getText().toString();
                // check network
                if (NetworkUtil.checkNetworkAvailable(LoginActivity.this)) {
                    login(userName, passWord);
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.message_network_is_unavailable), Toast.LENGTH_LONG).show();
                }

            }

        });
        btnRegister.setOnClickListener(v -> gotoActivity(RegisterActivity.class));
        btnLoginFacebook.setOnClickListener(v -> loginFacebook());
        btnResetPassword.setOnClickListener(v -> onClickForgetPassword());
        ivContinueAsGuest.setOnClickListener(v -> {
            gotoActivity(MainTabActivity.class);
            finish();
        });
    }


    public void refreshContent() {
        txtUser.setText("");
        txtPass.setText("");
//        txtUser.setText("tester");
//        txtPass.setText("123456");
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    private void onClickForgetPassword() {
        // Toast.makeText(getCurrentActivity(), "Click into Forget password!", Toast.LENGTH_LONG).show();
        gotoActivity(ForgotPasswordActivity.class);
    }

    private void login(String userName2, String passWord2) {
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

        ModelManager.login(this, userName2, passWord2, ime, fcmId, "1",true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        Account account = ParserUtility.parseAccount(json);
                        if (account != null) {
                            new MySharedPreferences(self).saveUserInfo(account);
                            GlobalValue.myAccount = account;
                            GlobalValue.myAccount.setPassword(passWord);
                            GlobalValue.myAccount.setType(Account.LOGIN_TYPE_NORMAL);
                            new MySharedPreferences(self).setCacheUserInfo(ParserUtility.convertAccountToJsonString(GlobalValue.myAccount));
                            gotoActivity(MainTabActivity.class);

                            if (!ServiceManager.getInstance(getApplicationContext()).isMyServiceRunning(MessageService.class)) {
                                ServiceManager.getInstance(getApplicationContext()).registerService(MessageService.class);
                            }
                        } else {
                            CustomToast.showCustomAlert(
                                    LoginActivity.this,
                                    getResources().getString(R.string.message_login_false),
                                    Toast.LENGTH_SHORT);

                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        ErrorNetworkHandler.processError(error);
                    }
                });
    }

    // ======= LOGIN BY FACEBOOK ===========

    // Call login Facebook
    public void loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email"));
        LoginManager.getInstance()
                .registerCallback(callbackManager, fbCallback);
        LoginManager.getInstance().logOut();
        if (AccessToken.getCurrentAccessToken() != null) {
            getUserFacebookData(AccessToken.getCurrentAccessToken());
        }
    }

    private final FacebookCallback<LoginResult> fbCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            getUserFacebookData(loginResult.getAccessToken());
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {
            Log.e("ERRO", e.getMessage());

        }
    };

    private void getUserFacebookData(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        try {
                            account = new Account();
                            account.setUserName(object.getString("email"));
                            account.setPassword("");
                            account.setEmail(object.getString("email"));
                            account.setPhone("");
                            account.setFull_name(object.getString("first_name") + " " + object.getString("first_name"));
                            account.setAddress("");
                            register(account);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields",
                "id,first_name,last_name,email,picture.type(large),gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 android.content.Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
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

    private void getKeyHash() {
        PackageInfo info;
        try {
            info = self.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder build = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog)).setTitle("Log out")
                .setMessage(getString(R.string.message_log_out))
                .setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
                        System.exit(0);
                    }
                }).setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

        build.show();
    }

    private void register(Account acc) {
        acc.setType(Account.LOGIN_TYPE_SOCIAL);
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

        ModelManager.register(LoginActivity.this, data,
                ime, fcmId, "1",
                true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        String json = (String) object;
                        GlobalValue.myAccount = ParserUtility.parseAccount(json);
                        GlobalValue.myAccount.setType(Account.LOGIN_TYPE_SOCIAL);
                        new MySharedPreferences(self).saveUserInfo(account);
                        new MySharedPreferences(self).setCacheUserInfo(ParserUtility.convertAccountToJsonString(GlobalValue.myAccount));
                        gotoActivity(MainTabActivity.class);
//                        MainUserActivity activity = (MainUserActivity) getCurrentActivity();
//                        activity.refreshContent();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

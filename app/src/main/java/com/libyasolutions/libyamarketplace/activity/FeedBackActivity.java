package com.libyasolutions.libyamarketplace.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.util.CustomToast;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener {
    private ImageView btnBack;
    private EditText edtDes;
    private TextView btnSend;
    public static final String MESSAGE_SUCCESS = "success";
    private String type = "2";
    private Activity context = this;
    private String shopName;
    private int shopId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shopName = extras.getString(GlobalValue.KEY_SHOP_NAME);
            shopId = extras.getInt(GlobalValue.KEY_SHOP_ID);
        }
        initUI();
        btnBack.setOnClickListener(v -> {
            onBackPressed();
//            Intent intent = new Intent(FeedBackActivity.this, MainTabActivity.class);
//            intent.putExtra("CODE", 99);
//            startActivity(intent);
        });

    }

    private void initUI() {
        btnSend = (TextView) findViewById(R.id.btnSend);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        edtDes = (EditText) findViewById(R.id.edtDesFB);
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    private String validateForm() {
        String message = MESSAGE_SUCCESS;

        String des = edtDes.getText().toString();

        if (des.isEmpty()) {
            message = this.getResources().getString(
                    R.string.error_des_empty);
            return message;
        }

        return message;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnSend) {
            String message = validateForm();
            if (!message.equals(MESSAGE_SUCCESS)) {
                CustomToast.showCustomAlert(this, message,
                        Toast.LENGTH_SHORT);
            } else {
                if (NetworkUtil.checkNetworkAvailable(this)) {
                    send();
                } else {
                    Toast.makeText(this,
                            R.string.message_network_is_unavailable,
                            Toast.LENGTH_LONG).show();
                }

            }
            return;
        }
    }

    private void send() {
        // TODO Auto-generated method stub
        ModelManager.putFeedBack(this,
                GlobalValue.myAccount.getId() + "", "Content is inappropriate", edtDes.getText().toString(), type, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        CustomToast.showCustomAlert(context,
                                getString(R.string.message_success),
                                Toast.LENGTH_SHORT);
                        onBackPressed();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

package com.libyasolutions.libyamarketplace.activity.tabs.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseFragment;
import com.libyasolutions.libyamarketplace.JsonParser;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.MainUserActivity;
import com.libyasolutions.libyamarketplace.adapter.SpinnerSimpleAdapter;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.util.CustomToast;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

public class FeedBackFragment extends BaseFragment implements OnClickListener {
    private ImageView btnBack;
    private EditText edtTitle, edtDes;
    private TextView btnSend;
    public static final String MESSAGE_SUCCESS = "success";
    private Spinner spnType;
    private String type = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        self = getActivity();
        initUI(view);
        initControlUI();
        initData();
        return view;
    }

    private void initData() {
        // TODO Auto-generated method stub

    }

    private void initControlUI() {
        // TODO Auto-generated method stub
        SpinnerSimpleAdapter typeAdapter = new SpinnerSimpleAdapter(self,
                android.R.layout.simple_spinner_item, self.getResources()
                .getStringArray(R.array.arr_feedback_type));
        spnType.setAdapter(typeAdapter);
    }

    private void initUI(View view) {
        // TODO Auto-generated method stub
        btnSend = (TextView) view.findViewById(R.id.btnSend);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
        edtTitle = (EditText) view.findViewById(R.id.edtTitleFB);
        edtDes = (EditText) view.findViewById(R.id.edtDesFB);
        spnType = (Spinner) view.findViewById(R.id.spnType);
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0) {
                    type = "1";
                } else if (position == 1) {
                    type = "3";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    private String validateForm() {
        String message = MESSAGE_SUCCESS;

        String des = edtDes.getText().toString();
        String title = edtTitle.getText().toString();

        // username
        if (title.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_title_empty);
            return message;
        }

        if (des.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_des_empty);
            return message;
        }

        return message;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnBack) {
            // hidden keyboard :
            hiddenKeyboard();
            //back to previous page
            MainUserActivity activity = (MainUserActivity) getCurrentActivity();
            activity.backFragment(MainUserActivity.MYACC_PAGE);
            return;
        }
        if (v == btnSend) {
            String message = validateForm();
            if (!message.equals(MESSAGE_SUCCESS)) {
                CustomToast.showCustomAlert(getCurrentActivity(), message,
                        Toast.LENGTH_SHORT);
            } else {
                if (NetworkUtil.checkNetworkAvailable(getCurrentActivity())) {
                    send();
                } else {
                    Toast.makeText(getCurrentActivity(),
                            R.string.message_network_is_unavailable,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void send() {
        // TODO Auto-generated method stub
        ModelManager.putFeedBack(getCurrentActivity(),
                GlobalValue.myAccount.getId() + "", edtTitle.getText()
                        .toString(), edtDes.getText().toString(), type, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String resutl = (String) object;

                        if (JsonParser.isSuccess(resutl)) {
                            edtDes.setText("");
                            edtTitle.setText("");
                            CustomToast.showCustomAlert(getActivity(),
                                    getString(R.string.message_success),
                                    Toast.LENGTH_SHORT);
                            MainUserActivity activity = (MainUserActivity) getCurrentActivity();
                            activity.backFragment(MainUserActivity.MYACC_PAGE);
                        } else {
                            Toast.makeText(self, JsonParser.getMessage(resutl), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

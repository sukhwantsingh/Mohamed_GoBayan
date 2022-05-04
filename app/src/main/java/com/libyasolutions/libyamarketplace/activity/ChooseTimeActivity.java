package com.libyasolutions.libyamarketplace.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.GMTAdapter;
import com.libyasolutions.libyamarketplace.adapter.ShopTimeAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.interfaces.OnShopTimeListener;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.OpenHour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ChooseTimeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private Button btnCancel, btnOK;

    private RecyclerView rcvTime;
    private ShopTimeAdapter shopTimeAdapter;
    private ArrayList<OpenHour> listTimes = new ArrayList<>();

    private EditText edtGMT;
    private String gmt;
    private ArrayList<String> listGMT = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        initViews();
        initShopTime();
        initData();
        getListGMT();
        initControl();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);

        edtGMT = findViewById(R.id.edt_gmt);

        btnOK = findViewById(R.id.btn_ok);
        btnCancel = findViewById(R.id.btn_no);

        rcvTime = findViewById(R.id.rcv_time);
        rcvTime.setHasFixedSize(true);
        rcvTime.setLayoutManager(new LinearLayoutManager(self));

    }

    private void initData() {
        gmt = getIntent().getStringExtra(Constant.GMT);
        edtGMT.setText(gmt);

        shopTimeAdapter = new ShopTimeAdapter(self, listTimes);
        rcvTime.setAdapter(shopTimeAdapter);

        shopTimeAdapter.setOnShopTimeListener(new OnShopTimeListener() {
            @Override
            public void onOpenAMClick(View view, int position) {
                showTimePickerDialog("OpenAM", (TextView) view, position);
            }

            @Override
            public void onOpenPMClick(View view, int position) {
                showTimePickerDialog("OpenPM", (TextView) view, position);
            }

            @Override
            public void onCloseAMClick(View view, int position) {
                showTimePickerDialog("CloseAM", (TextView) view, position);
            }

            @Override
            public void onClosePMClick(View view, int position) {
                showTimePickerDialog("ClosePM", (TextView) view, position);
            }
        });
    }

    private void getListGMT() {
        ModelManager.getListGMT(self, true, new ModelManagerListener() {
            @Override
            public void onError(VolleyError error) {
                showToastMessage(getResources().getString(R.string.have_error));
            }

            @Override
            public void onSuccess(Object object) {
                String json = (String) object;
                if (ParserUtility.isSuccess(json)) {
                    String gmt = ParserUtility.getListGMT(json);
                    listGMT.addAll(Arrays.asList(gmt.split(",")));

                }
            }
        });
    }

    private void initShopTime() {
        Intent intent = getIntent();
        ArrayList<OpenHour> arrTemp = intent.getParcelableArrayListExtra(Constant.LIST_TIME);
        if (arrTemp != null && arrTemp.size() != 0) {
            listTimes.addAll(arrTemp);
        } else {
            listTimes.add(new OpenHour(1, getString(R.string.monday), "08:00 AM", "12:00 AM", "03:00 PM", "11:00 PM"));
            listTimes.add(new OpenHour(2, getString(R.string.tuesday), "08:00 AM", "12:00 AM", "03:00 PM", "11:00 PM"));
            listTimes.add(new OpenHour(3, getString(R.string.wednesday), "08:00 AM", "12:00 AM", "03:00 PM", "11:00 PM"));
            listTimes.add(new OpenHour(4, getString(R.string.thursday), "08:00 AM", "12:00 AM", "03:00 PM", "11:00 PM"));
            listTimes.add(new OpenHour(5, getString(R.string.friday), "08:00 AM", "12:00 AM", "03:00 PM", "11:00 PM"));
            listTimes.add(new OpenHour(6, getString(R.string.saturday), "08:00 AM", "12:00 AM", "03:00 PM", "11:00 PM"));
            listTimes.add(new OpenHour(7, getString(R.string.sunday), "08:00 AM", "12:00 AM", "03:00 PM", "11:00 PM"));
        }
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        edtGMT.setOnClickListener(this);
    }

    private boolean validate() {
        if (edtGMT.getText().toString().trim().length() == 0) {
            showToastMessage(getResources().getString(R.string.please_select_gmt));
            return false;
        }
        return true;
    }

    private void showGMTDialog() {
        View view = getLayoutInflater().inflate(R.layout.layout_dialog_gmt, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setView(view);

        ListView lvGMT = view.findViewById(R.id.lv_time_zone);
        GMTAdapter gmtAdapter = new GMTAdapter(self, listGMT);
        lvGMT.setAdapter(gmtAdapter);

        final Dialog dialog = builder.create();
        dialog.show();

        lvGMT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gmt = listGMT.get(position);
                edtGMT.setText(gmt);
                dialog.dismiss();
            }
        });
    }

    private void showTimePickerDialog(final String strTime, final TextView textView, final int position) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(self, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time = "";
                if (selectedHour > 12) {
                    int diff = selectedHour - 12;
                    String minute = (selectedMinute < 10 ? ("0" + selectedMinute) : selectedMinute) + "";
                    time = diff < 10 ? ("0" + diff) : diff + "";
                    time += ":" + minute + " PM";
                } else {
                    String minute = (selectedMinute < 10 ? ("0" + selectedMinute) : selectedMinute) + "";
                    time = selectedHour < 10 ? ("0" + selectedHour) : selectedHour + "";
                    time += ":" + minute + " AM";
//                    time = selectedHour < 10 ? ("0" + selectedHour) : selectedHour + ":" + (selectedMinute < 10 ? ("0" + selectedMinute) : selectedMinute) + " AM";
                }
                Log.e("kevin", "onTimeSet: " + time);
                textView.setText(time);

                switch (strTime) {
                    case "OpenAM":
                        listTimes.get(position).setOpenAM(time);
                        break;
                    case "OpenPM":
                        listTimes.get(position).setOpenPM(time);
                        break;
                    case "CloseAM":
                        listTimes.get(position).setCloseAM(time);
                        break;
                    case "ClosePM":
                        listTimes.get(position).setClosePM(time);
                        break;
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private String createJson() {
        JSONArray jsonArray = new JSONArray();
        for (OpenHour shopTimeObj : listTimes) {
            JSONObject object = new JSONObject();
            try {
                object.put("dateId", shopTimeObj.getDateId() + "");
                object.put("openAM", shopTimeObj.getOpenAM());
                object.put("closeAM", shopTimeObj.getCloseAM());
                object.put("openPM", shopTimeObj.getOpenPM());
                object.put("closePM", shopTimeObj.getClosePM());
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("kevin", "json time: " + jsonArray);
        return jsonArray.toString();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_ok:
                Log.e("kevin", "json time: " + createJson());
                if (validate()) {
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(Constant.LIST_TIME, listTimes);
                    intent.putExtra(Constant.SHOP_TIME, createJson());
                    intent.putExtra(Constant.GMT, gmt);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.btn_no:
                onBackPressed();
                break;
            case R.id.edt_gmt:
                showGMTDialog();
                break;
        }
    }
}

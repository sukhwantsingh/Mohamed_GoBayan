package com.libyasolutions.libyamarketplace.activity.tabs.cart;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseFragment;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.LoginActivity;
import com.libyasolutions.libyamarketplace.activity.tabs.MainCartActivity;
import com.libyasolutions.libyamarketplace.adapter.PaymentMethodAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.config.WebServiceConfig;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.object.ExtraOptions;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.OptionsItem;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.DialogUtility;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;
import com.libyasolutions.libyamarketplace.util.StringUtility;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

@SuppressLint("NewApi")
public class DeliveryInfoFragment extends BaseFragment implements
        OnClickListener {

    private static final int DELIVERY_METHOD = 1;
    private static final int PAYPAL_METHOD = 2;
    private static final int BANKING_METHOD = 3;

    private View view;
    private ImageView btnBack;
    private EditText txtBuyerName, txtBuyerEmail, txtBuyerPhone, txtBuyerAddress, txtBuyerCity, txtBuyerZipCode;
    private EditText txtReceiverName, txtReceiverEmail, txtReceiverPhone, txtReceiverAddress, txtReceiverCity, txtReceiverZipCode;
    private CheckBox ckbSameAsBuyerInfo;
    private Button btnContinue;
    private TextView lblTotal;
    private Spinner spnPaymentMethods;
    private LinearLayout layoutDeliveryInformation;
    private PaymentMethodAdapter paymentMethodAdapter;
    private int selectedPaymentMethod = 0;
    private String data;
    private double total = 0;
    private String restaurantPhone;
    private Dialog mDialog;
    private EditText edtAddRequirement;

    protected static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(Constant.PAYPAL_CLIENT_APP_ID)
            //.defaultUserEmail(Constant.PAYPAL_RECEIVE_EMAIL_ID)
            .acceptCreditCards(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details_order, container, false);
        initUI(view);
        startPaypalService();
        initControls();
        return view;
    }

    private void initUI(View view) {
        layoutDeliveryInformation = view.findViewById(R.id.layoutDeliveryInformation);
        btnContinue = view.findViewById(R.id.btnContinue);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
        lblTotal = (TextView) view.findViewById(R.id.lblTotal);
        spnPaymentMethods = (Spinner) view.findViewById(R.id.spnPaymentMethods);

        txtBuyerName = (EditText) view.findViewById(R.id.txtBuyerName);
        txtBuyerEmail = (EditText) view.findViewById(R.id.txtBuyerEmail);
        txtBuyerPhone = (EditText) view.findViewById(R.id.txtBuyerPhone);
        txtBuyerAddress = (EditText) view.findViewById(R.id.txtBuyerAddress);
        txtBuyerCity = (EditText) view.findViewById(R.id.txtBuyerCity);
        txtBuyerZipCode = (EditText) view.findViewById(R.id.txtBuyerZipcode);
        //Receiver info
        txtReceiverName = (EditText) view.findViewById(R.id.txtReceiverName);
        txtReceiverEmail = (EditText) view.findViewById(R.id.txtReceiverEmail);
        txtReceiverPhone = (EditText) view.findViewById(R.id.txtReceiverPhone);
        txtReceiverAddress = (EditText) view.findViewById(R.id.txtReceiverAddress);
        txtReceiverCity = (EditText) view.findViewById(R.id.txtReceiverCity);
        txtReceiverZipCode = (EditText) view.findViewById(R.id.txtReceiverZipcode);

        ckbSameAsBuyerInfo = (CheckBox) view.findViewById(R.id.ckbSameAsBuyerInfo);
        edtAddRequirement = view.findViewById(R.id.edt_add_requirement);

    }

    private void initControls() {
        btnBack.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        ckbSameAsBuyerInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateReceiverInformation(b);
            }
        });
        spnPaymentMethods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 1) {
                    selectedPaymentMethod = position + 5;
                    layoutDeliveryInformation.setVisibility(View.GONE);
                } else {
                    selectedPaymentMethod = position + 1;
                    layoutDeliveryInformation.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshContent();
        }
    }

    private void setPaymentMethodData() {
        paymentMethodAdapter = new PaymentMethodAdapter(getCurrentActivity(),
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.arr_payment_methods));
        spnPaymentMethods.setAdapter(paymentMethodAdapter);
        spnPaymentMethods.setSelection(0);
    }

    private void showTotalPrice() {
        total = (double) 0;
        double totalOfShop = 0;
        double VATOfShop = 0;
        double shipTotal = 0;
        double ShipPriceOfShop = 0;

        for (int i = 0; i < GlobalValue.arrMyMenuShop.size(); i++) {

            Shop shop = GlobalValue.arrMyMenuShop.get(i);
            if (shop.getArrOrderFoods().size() == 0) {
                GlobalValue.arrMyMenuShop.remove(i);
            } else {
                totalOfShop += shop.getTotalPrice();
                VATOfShop += shop.getCurrentTotalVAT();
                if ((shop.getTotalPrice() + shop.getCurrentTotalVAT()) >= shop.getMinPriceForDelivery()) {
                    ShipPriceOfShop = 0;
                } else {
                    ShipPriceOfShop += shop.getDeliveryPrice();
                }
                shipTotal += ShipPriceOfShop;
            }
            total = totalOfShop + VATOfShop + shipTotal;
            lblTotal.setText(StringUtility.replaceArabicNumbers(String.format("%.1f", total)) + " " + getString(R.string.currency));
        }
    }

    @Override
    public void refreshContent() {
        txtBuyerName.setText(GlobalValue.myAccount.getFull_name());
        txtBuyerEmail.setText(GlobalValue.myAccount.getEmail());
        txtBuyerPhone.setText(GlobalValue.myAccount.getPhone());
        txtBuyerAddress.setText(GlobalValue.myAccount.getAddress());
        txtBuyerCity.setText(GlobalValue.myAccount.getCity());
        txtBuyerZipCode.setText(GlobalValue.myAccount.getZipCode());
        //show total Price
        showTotalPrice();
        //set payment method data
        setPaymentMethodData();
        ckbSameAsBuyerInfo.setSelected(false);
        //updateReceiverInformation(ckbSameAsBuyerInfo.isSelected());
    }

    private void updateReceiverInformation(boolean selectedSameAsBuyer) {
        if (selectedSameAsBuyer) {
            txtReceiverName.setText(GlobalValue.myAccount.getFull_name());
            txtReceiverEmail.setText(GlobalValue.myAccount.getEmail());
            txtReceiverPhone.setText(GlobalValue.myAccount.getPhone());
            txtReceiverAddress.setText(GlobalValue.myAccount.getAddress());
            txtReceiverCity.setText(GlobalValue.myAccount.getCity());
            txtReceiverZipCode.setText(GlobalValue.myAccount.getZipCode());
        } else {
            txtReceiverName.setText("");
            txtReceiverEmail.setText("");
            txtReceiverPhone.setText("");
            txtReceiverAddress.setText("");
            txtReceiverCity.setText("");
            txtReceiverZipCode.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnContinue) {
            onClickOrder();

        } else if (v == btnBack) {
            // hidden keyboard :
            hiddenKeyboard();
            MainCartActivity activity = (MainCartActivity) getCurrentActivity();
            activity.onBackPressed();
        }
    }

    private void onClickOrder() {
        // TODO Auto-generated method stub
        //check receiverName

        if (selectedPaymentMethod==6){
            data=createOfferJson(GlobalValue.arrMyMenuShop,"","","","","","", "");
        }else {
            String receiverName = txtReceiverName.getText().toString();
            if (StringUtility.isEmpty(receiverName)) {
                Toast.makeText(getCurrentActivity(),
                        getString(R.string.message_empty_delivery_information), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            //check receiver email
            String receiverEmail = txtReceiverEmail.getText().toString();
            if (StringUtility.isEmpty(receiverEmail)) {
                Toast.makeText(getCurrentActivity(),
                        getString(R.string.message_empty_delivery_information), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            //check receiver phone
            String receiverPhone = txtReceiverPhone.getText().toString();
            if (StringUtility.isEmpty(receiverPhone)) {
                Toast.makeText(getCurrentActivity(),
                        getString(R.string.message_empty_delivery_information), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            //check receiver address
            String receiverAddress = txtReceiverAddress.getText().toString();
            if (StringUtility.isEmpty(receiverAddress)) {
                Toast.makeText(getCurrentActivity(),
                        getString(R.string.message_empty_delivery_information), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            //check receiver city
            String receiverCity = txtReceiverCity.getText().toString();
            if (StringUtility.isEmpty(receiverCity)) {
                Toast.makeText(getCurrentActivity(),
                        getString(R.string.message_empty_delivery_information), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            //check receiver zip code
            String receiverZipCode = txtReceiverZipCode.getText().toString();
            if (StringUtility.isEmpty(receiverZipCode)) {
                Toast.makeText(getCurrentActivity(),
                        getString(R.string.message_empty_delivery_information), Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            String orderRequirement = edtAddRequirement.getText().toString();

            data = createOfferJson(GlobalValue.arrMyMenuShop, receiverName, receiverEmail, receiverPhone, receiverAddress, receiverCity, receiverZipCode, orderRequirement);
        }

        Log.e("Huy -test ", "Log json data =" + data);
        if (NetworkUtil.checkNetworkAvailable(getCurrentActivity())) {
            if (selectedPaymentMethod != PAYPAL_METHOD) {
                sendListOrder(data, selectedPaymentMethod);
            } else {
                requestPaypalPayment(total, "paypal payment", "USD");
            }
        } else {
            Toast.makeText(getCurrentActivity(),
                    R.string.message_network_is_unavailable,
                    Toast.LENGTH_LONG).show();
        }

    }

    public void sendOrderInfoWithPaypalMethod() {
        sendListOrder(data, PAYPAL_METHOD);
    }

    private void sendListOrder(String data, final int paymentMethod) {
        ModelManager.sendListOrder(getCurrentActivity(), data, paymentMethod,
                true, new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String strJson = (String) object;
                        try {
                            JSONObject json = new JSONObject(strJson);
                            if (json.getString(WebServiceConfig.KEY_STATUS)
                                    .equals(WebServiceConfig.KEY_STATUS_SUCCESS)) {
                                Log.e("DeliveryInfoFragment", "sendListOrder success");
                                if (paymentMethod != BANKING_METHOD) {
                                    showCallDialog();
                                } else {
                                    showBankInfo(getCurrentActivity().getString(
                                            R.string.message_success));
                                }
                                restaurantPhone = GlobalValue.arrMyMenuShop.get(0).getPhone();
                                GlobalValue.arrMyMenuShop.clear();
                                (getCurrentActivity()).onBackPressed();

                            } else {
                                showCallDialog();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showCallDialog() {
        mDialog = new Dialog(self);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_confirm);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        self.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = 6 * (displaymetrics.widthPixels / 7);
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvTitle = mDialog.findViewById(R.id.tvTitle);
        TextView tvContent = mDialog.findViewById(R.id.tvContent);
        TextView tvCancel = mDialog.findViewById(R.id.tvCancel);
        TextView tvConfirm = mDialog.findViewById(R.id.tvConfirm);
        tvConfirm.setText(R.string.call);
        tvCancel.setText(R.string.done);
        tvContent.setText(R.string.call_restaurant);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRestaurant();
                mDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Constant.GET_CART_INFO);
                self.sendBroadcast(intent);
                mDialog.dismiss();
            }
        });
        mDialog.show();

    }

    private void callRestaurant() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + restaurantPhone));
        startActivity(intent);
    }

    public static void alert(final Context context, String title, String message) {
        @SuppressLint("RestrictedApi") android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent();
                intent.setAction(Constant.GET_CART_INFO);
                context.sendBroadcast(intent);
                arg0.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showBankInfo(String message) {
        alert(getCurrentActivity(), getString(R.string.title_bank_info), Html.fromHtml(message).toString());
    }

    // ============ PAYPAL ==============
    protected void requestPaypalPayment(double value, String content,
                                        String currency) {

        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(value),
                currency, content, PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(self, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, 1);
    }

    protected void startPaypalService() {
        Intent intent = new Intent(getCurrentActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        getCurrentActivity().startService(intent);
    }

    private String createOfferJson(ArrayList<Shop> arrShops, String receiverName, String receiverEmail,
                                   String receiverPhone, String receiverAddress, String receiverCity,
                                   String receiverZipcode, String orderRequirement) {
        JSONObject jsonOrder = new JSONObject();
        JSONArray jsonFoods = new JSONArray();
        JSONObject jsonFood = null;
        Double price = null;

        try {
            for (Shop shop : arrShops) {

                for (Menu menu : shop.getArrOrderFoods()) {

                    price = menu.getPrice()
                            - (menu.getPrice() * menu.getPercentDiscount() / 100);

                    jsonFood = new JSONObject();
                    jsonFood.put(WebServiceConfig.KEY_ORDER_SHOP, menu.getShopId());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_FOOD, menu.getId());
                    jsonFood.put("option", createOptionString(menu.getExtraOptions()));
                    jsonFood.put(WebServiceConfig.KEY_ORDER_NUMBER_FOOD, menu.getOrderNumber());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_PRICE_FOOD, round(price, 2));
                    jsonFoods.put(jsonFood);

                }
            }

            jsonOrder.put(WebServiceConfig.KEY_ORDER_ACCOUT_ID, GlobalValue.myAccount.getId());
            jsonOrder.put("billing_name", receiverName);
            jsonOrder.put("billing_email", receiverEmail);
            jsonOrder.put("billing_phone", receiverPhone);
            jsonOrder.put("billing_address", receiverAddress);
            jsonOrder.put("billing_city", receiverCity);
            jsonOrder.put("order_requirement ", orderRequirement);
            jsonOrder.put("billing_zip_code", receiverZipcode);
            jsonOrder.put(WebServiceConfig.KEY_ORDER_ITEM, jsonFoods);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonOrder.toString();
    }

    public static double round(double number, int digit) {
        if (digit > 0) {
            int temp = 1, i;
            for (i = 0; i < digit; i++)
                temp = temp * 10;
            number = number * temp;
            number = Math.round(number);
            number = number / temp;
            return number;
        } else
            return 0.0;
    }

    private String createOptionString(ArrayList<ExtraOptions> listExtraOption) {
        StringBuilder option = new StringBuilder();
        for (ExtraOptions extraOption : listExtraOption) {
            for (OptionsItem optionItem : extraOption.getOptionsItems()) {
                if (optionItem.isChecked()) {
                    option.append(optionItem.getParentName()).append(":").append(optionItem.getOptionName()).append(",");
                    break;
                }
            }
        }
        if (option.length() > 1) {
            option = new StringBuilder(option.substring(0, option.length() - 1));
        }
        Log.e("kevin", "createOptionString: " + option);
        return option.toString();
    }

}

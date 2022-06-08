package com.libyasolutions.libyamarketplace.activity.tabs.cart;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.libyasolutions.libyamarketplace.BaseFragment;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.tabs.MainCartActivity;
import com.libyasolutions.libyamarketplace.activity.tabs.MainTabActivity;
import com.libyasolutions.libyamarketplace.adapter.ShopCartAdapterNew;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.CustomToast;
import com.libyasolutions.libyamarketplace.util.StringUtility;

@SuppressLint("NewApi")
public class ShopCartFragment extends BaseFragment implements OnClickListener, ShopCartAdapterNew.ShopCartListener {

    private TextView tvPriceShipping, tvPriceVAT, tvGone,btnOrder;
    private TextView lblSum;
    private RecyclerView lsvShops;
    private ShopCartAdapterNew shopCartAdapterNew;
    private double total, totalVAt, totalShipping;
    private MainCartActivity self;
    private View view;
    private LinearLayout llRecycler;
    private Dialog mDialog;
    private ImageView btnBack;
    private TextView continue_shop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list_shop_cart, container,
                false);
        self = (MainCartActivity) getActivity();
        initUI(view);
        initData();
        return view;
    }

    private void initUI(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        continue_shop = view.findViewById(R.id.continue_shop);
        llRecycler = view.findViewById(R.id.llRecycler);
        tvGone = view.findViewById(R.id.tvGone);
        btnOrder = view.findViewById(R.id.btnOrder);
        lblSum = view.findViewById(R.id.tvTotalPrice);
        lsvShops = view.findViewById(R.id.lsvShop);
        tvPriceShipping = view.findViewById(R.id.tvPriceShipping);
        tvPriceVAT = view.findViewById(R.id.tvPriceVAT);
        btnOrder.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        continue_shop.setOnClickListener(this);
        lsvShops.setNestedScrollingEnabled(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshContent();
        }
    }

    private void initData() {
        shopCartAdapterNew = new ShopCartAdapterNew(GlobalValue.arrMyMenuShop, self, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(self);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lsvShops.setLayoutManager(layoutManager);
        lsvShops.setAdapter(shopCartAdapterNew);

    }

    @Override
    public void refreshContent() {
        total = (double) 0;
        totalVAt = (double) 0;
        totalShipping = (double) 0;

        if (GlobalValue.arrMyMenuShop.size() > 0) {
            shopCartAdapterNew.notifyDataSetChanged();
            double totalOfShop = 0;
            double VATOfShop = 0;
            double ShipPriceOfShop = 0;
            double shipTotal = 0;

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
            }
            total = totalOfShop + VATOfShop + ShipPriceOfShop;
            totalVAt = VATOfShop;
            totalShipping = shipTotal;
            tvGone.setVisibility(View.GONE);
            llRecycler.setVisibility(View.VISIBLE);

        } else {
            tvGone.setVisibility(View.VISIBLE);
            llRecycler.setVisibility(View.GONE);
        }

        lblSum.setText(StringUtility.replaceArabicNumbers(String.format("%.1f", total))+" "+getString(R.string.currency));
        tvPriceVAT.setText(StringUtility.replaceArabicNumbers(String.format("%.1f", totalVAt)));
        tvPriceShipping.setText(StringUtility.replaceArabicNumbers(String.format("%.1f", totalShipping)));
    }

    @Override
    public void onClick(View v) {
        if (v == btnOrder) { onBtnOrderClick(); }
        else if (v == btnBack) { if(null != getActivity()) getActivity().finish(); }
        else if (v == continue_shop) { sendAction(Constant.SHOW_TAB_HOME);  }
    }
    private void sendAction(String action) {
        Intent intent = new Intent(getActivity(), MainTabActivity.class);
        intent.setAction(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
       if(null != getActivity()) getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
    }
    private void onBtnOrderClick() {
        if (GlobalValue.myAccount == null) {
            CustomToast.showCustomAlert(self,
                    self.getString(R.string.message_no_login),
                    Toast.LENGTH_SHORT);
        } else {

            if (GlobalValue.arrMyMenuShop.size() > 0) {
                ((MainCartActivity) self).gotoFragment(MainCartActivity.PAGE_CONFIRM);
            } else {
                CustomToast.showCustomAlert(self,
                        self.getString(R.string.message_no_item_menu),
                        Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void deleteItem(int position) {
        showConfirmDeleteDialog(position);
    }

    private void showConfirmDeleteDialog(final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(self);
//        builder.setTitle(getResources().getString(R.string.app_name));
//        builder.setMessage(getResources().getString(R.string.msg_confirm_delete));
//        builder.setCancelable(false);
//        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                GlobalValue.arrMyMenuShop.remove(position);
//                refreshContent();
//                Intent intent = new Intent();
//                intent.setAction(Constant.GET_CART_INFO);
//                self.sendBroadcast(intent);
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
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
        tvContent.setText(self.getResources().getString(R.string.msg_confirm_delete));
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalValue.arrMyMenuShop.remove(position);
                refreshContent();
                Intent intent = new Intent();
                intent.setAction(Constant.GET_CART_INFO);
                self.sendBroadcast(intent);
                mDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshContent();
    }
}

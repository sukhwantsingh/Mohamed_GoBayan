package com.libyasolutions.libyamarketplace.objectorder;

import com.libyasolutions.libyamarketplace.object.ExtraOptions;

import java.util.ArrayList;

public class MenuOrder {
    private String MenuId;
    private String MenuImage;
    private String Amount;
    private String TotalPrice;
    private String priceVat;
    private String priceShipping;
    private ArrayList<ExtraOptions> extraOptions;


    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getPriceVat() {
        return priceVat;
    }

    public void setPriceVat(String priceVat) {
        this.priceVat = priceVat;
    }

    public String getPriceShipping() {
        return priceShipping;
    }

    public void setPriceShipping(String priceShipping) {
        this.priceShipping = priceShipping;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getMenuImage() {
        return MenuImage;
    }

    public void setMenuImage(String menuImage) {
        MenuImage = menuImage;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public ArrayList<ExtraOptions> getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(ArrayList<ExtraOptions> extraOptions) {
        this.extraOptions = extraOptions;
    }
}

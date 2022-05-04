package com.libyasolutions.libyamarketplace.objectorder;

import java.util.ArrayList;

public class ShopOrder {
    private String shopid;
    private String shopImage;
    private ArrayList<MenuOrder> menuOrders;

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public ArrayList<MenuOrder> getMenuOrders() {
        return menuOrders;
    }

    public void setMenuOrders(ArrayList<MenuOrder> menuOrders) {
        this.menuOrders = menuOrders;
    }
}

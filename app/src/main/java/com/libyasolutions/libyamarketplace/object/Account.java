package com.libyasolutions.libyamarketplace.object;

import android.annotation.SuppressLint;
import android.content.Context;

import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.util.MySharedPreferences;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class Account implements Cloneable {

    public static final String ROLE_USER = "0";
    public static final String ROLE_SHOP_OWNER = "1";
    public static final String ROLE_ADMIN = "2";
    public static final String ROLE_CHEF = "3";
    public static final String ROLE_DELIVERY = "4";
    public static final String ROLE_MODERATOR = "5";
    public static final String LOGIN_TYPE_NORMAL = "0";
    public static final String LOGIN_TYPE_SOCIAL = "1";

    private String id = "";
    private String userName = "";
    private String email = "";
    private String full_name = "";
    private String phone = "";
    private String address = "";
    private String city = "";
    private String zipCode = "";
    private String password = "";
    private String role = "0";
    private String redirectLink = "";
    private String type = "";
    private int numberOfFavouriteShops = 0, numberOfFavouriteProducts = 0, numberOfOrders = 0;
    private String avatar;
    private int waitApproveShopOwner;
    private String isCannotSendRequest;

    public String getIsCannotSendRequest() {
        return isCannotSendRequest;
    }

    public void setIsCannotSendRequest(String isCannotSendRequest) {
        this.isCannotSendRequest = isCannotSendRequest;
    }

    public Account(int waitApproveShopOwner) {
        this.waitApproveShopOwner = waitApproveShopOwner;
    }

    public Account() {
    }

    public int getWaitApproveShopOwner() {
        return waitApproveShopOwner;
    }

    public void setWaitApproveShopOwner(int waitApproveShopOwner) {
        this.waitApproveShopOwner = waitApproveShopOwner;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private SettingPreferences preferences;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCoe) {
        this.zipCode = zipCoe;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRedirectLink() {
        return redirectLink;
    }

    public void setRedirectLink(String redirectLink) {
        this.redirectLink = redirectLink;
    }

    public boolean isUser() {
        return redirectLink.isEmpty();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isUserRole() {
        return role == ROLE_USER;
    }

    public boolean isSocialLogin() {
        return type.equals(LOGIN_TYPE_SOCIAL);
    }

    public SettingPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SettingPreferences preferences) {
        this.preferences = preferences;
    }

    public int getNumberOfFavouriteShops() {
        return numberOfFavouriteShops;
    }

    public void setNumberOfFavouriteShops(int numberOfFavouriteShops) {
        this.numberOfFavouriteShops = numberOfFavouriteShops;
    }

    public int getNumberOfFavouriteProducts() {
        return numberOfFavouriteProducts;
    }

    public void setNumberOfFavouriteProducts(int numberOfFavouriteFoods) {
        this.numberOfFavouriteProducts = numberOfFavouriteFoods;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public String getPreferenceData(Context context) {
        MySharedPreferences pref = new MySharedPreferences(context);
        if (preferences == null) {
            return "Default Setting";
        }

        StringBuilder data = new StringBuilder();
        //get category
        if (!pref.getCacheCategories().isEmpty()) {
            ArrayList<Category> arr = ParserUtility.parseListCategories(pref.getCacheCategories());
            for (Category category : arr) {
                if (category.getId().equalsIgnoreCase(preferences.getCategoryId())) {
                    data.append(category.getName()).append(", ");
                }
            }
        }
        //get city
        if (!pref.getCacheCities().isEmpty()) {
            ArrayList<City> arr = ParserUtility.parseListCity(pref.getCacheCities());
            for (City city : arr) {
                if (city.getId().equalsIgnoreCase(preferences.getCityID())) {
                    data.append(city.getName()).append(", ");
                }
            }
        }
        //get type shop or products
        if (preferences.isShopType())
            data.append("Shops, ");
        else
            data.append("Products, ");

        //get param open or all
        if (preferences.isOpenShopList())
            data.append("Open, ");
        else
            data.append("All, ");

        // get distance
        if (!preferences.getDistance().equalsIgnoreCase("0"))
            data.append(preferences.getDistance()).append("km");
        else
            data.append("All");

        return data.toString();
    }

    public Account clone() throws CloneNotSupportedException {
        return (Account) super.clone();
    }
}

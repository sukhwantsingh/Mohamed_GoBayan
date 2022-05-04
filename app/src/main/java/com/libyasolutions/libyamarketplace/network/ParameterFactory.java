/*
 * Name: $RCSfile: ParameterFactory.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 2:45:36 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package com.libyasolutions.libyamarketplace.network;

import android.content.Context;

import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.util.StringUtility;
import com.libyasolutions.libyamarketplace.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * ParameterFactory class builds parameters for web service apis
 */
public final class ParameterFactory {

    private static final String PARAM_KEY_CATEGORY = "m_id";
    private static final String PARAM_KEY_CITY = "c_id";
    private static final String PARAM_KEY_SHOP = "s_id";
    private static final String PARAM_KEY_OFFER = "o_id";
    private static final String PARAM_KEY_FOOD = "f_id";
    private static final String PARAM_KEY_LATITUDE = "lat";
    private static final String PARAM_KEY_LONGITUDE = "long";
    private static final String PARAM_KEY_USER_NAME = "user";
    private static final String PARAM_KEY_PASSWORD = "pass";
    private static final String PARAM_KEY_DATA = "data";

    public static Map<String, String> createSearchShopByCityParams(
            Context context, String cityId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_CITY, cityId);

        return parameters;
    }

    public static Map<String, String> createSearchShopByCategoryParams(
            Context context, String categoryId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_CATEGORY, categoryId);

        return parameters;
    }

    public static Map<String, String> createSearchShopByCategoryAndCityParams(
            Context context, String categoryId, String cityId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_CATEGORY, categoryId);
        parameters.put(PARAM_KEY_CITY, cityId);
        return parameters;
    }

    public static Map<String, String> createShopIdParams(Context context,
                                                         String shopId, int page) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_SHOP, shopId);
        String userID = GlobalValue.myAccount != null ? GlobalValue.myAccount.getId() : "";
        parameters.put("user_id", userID);
        parameters.put("now", Utils.getCurrentTimestamp());
        parameters.put("page",page+"");

        return parameters;
    }

    public static Map<String, String> createSearchShopIdParams(Context context,
                                                               String key, String city_id, String cate_id, String page) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", key);
        parameters.put("c_id", city_id);
        parameters.put("m_id", cate_id);
        parameters.put("page", page);
        return parameters;
    }

    public static Map<String, String> createSearchMenuIdParams(Context context,
                                                               String key, String city_id, String cate_id, String page) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("keyword", key);
        parameters.put("c_id", city_id);
        parameters.put("m_id", cate_id);
        parameters.put("page", page);
        return parameters;
    }

    public static Map<String, String> getListOrderById(Context context,
                                                       String shopId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("account", shopId);

        return parameters;
    }

    public static Map<String, String> getListOrderDetailById(Context context,
                                                             String shopId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("o_id", shopId);

        return parameters;
    }

    public static Map<String, String> createCategoryIdParams(Context context,
                                                             String categoryId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_CATEGORY, categoryId);

        return parameters;
    }

    public static Map<String, String> createShopIdAndCategoryIdParams(
            Context context, int page, String shopId, String categoryId, String sortBy, String keyword) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_SHOP, shopId);
        parameters.put(PARAM_KEY_CATEGORY, categoryId);
        parameters.put("page", page + "");
        parameters.put("sort_by", sortBy);
        parameters.put("sort_type", "date");
        parameters.put("keyword", keyword);

        return parameters;
    }

    public static Map<String, String> createOfferIdParams(Context context,
                                                          String offerId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_OFFER, offerId);

        return parameters;
    }

    public static Map<String, String> createFoodIdParams(Context context,
                                                         String foodId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_FOOD, foodId);
        String userID = GlobalValue.myAccount != null ? GlobalValue.myAccount.getId() : "";
        parameters.put("user_id", userID);

        return parameters;
    }

    public static Map<String, String> createUserIdParams(Context context,
                                                         String id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", id);

        return parameters;
    }

    public static Map<String, String> createLongLatParams(Context context,
                                                          String longitude, String latitude, String page) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_LONGITUDE, longitude);
        parameters.put(PARAM_KEY_LATITUDE, latitude);
        parameters.put("now", Utils.getCurrentTimestamp());
        parameters.put("page", page);

        return parameters;
    }

    public static Map<String, String> createLoginParams(Context context,
                                                        String username, String password,
                                                        String ime, String gcm_id, String typeDevice) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_USER_NAME, username);
        parameters.put(PARAM_KEY_PASSWORD, password);
        parameters.put("ime", ime);
        parameters.put("gcm_id", gcm_id);
        parameters.put("typeDevice", typeDevice);

        return parameters;
    }

    public static Map<String, String> createDataOrderParams(Context context,
                                                            String data, int paymentMehthod) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_DATA, data);
        parameters.put("paymentMethod", paymentMehthod
                + "");

        return parameters;
    }

    public static Map<String, String> createDataRegisterParams(Context context,
                                                               String data,  String ime, String gcm_id, String typeDevice) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(PARAM_KEY_DATA, data);
        parameters.put("ime", ime);
        parameters.put("gcm_id", gcm_id);
        parameters.put("typeDevice", typeDevice);

        return parameters;
    }

    public static Map<String, String> putFeedBackParams(Context context,
                                                        String id, String title, String des, String type) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("account_id", id);
        parameters.put("title", title);
        parameters.put("description", des);
        parameters.put("type", type);
        return parameters;
    }

    public static Map<String, String> updateInforUserParams(
            String id, String newpassword, String email, String fullName, String phone,
            String address, String city, String zipcode) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("email", email);
        parameters.put("full_name", fullName);
        parameters.put("phone", phone);
        parameters.put("address", address);
        parameters.put("city", city);
        parameters.put("zip_code", zipcode);
        if (!StringUtility.isEmpty(newpassword))
            parameters.put("pass", newpassword);

        return parameters;
    }

    public static Map<String, String> updatePassUserParams(Context context,
                                                           String id, String pass) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("pass", pass);

        return parameters;
    }
}
package com.libyasolutions.libyamarketplace.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.libyasolutions.libyamarketplace.PacketUtility;
import com.libyasolutions.libyamarketplace.object.Account;

public class MySharedPreferences {
    private static final String AVATAR = "AVATAR";
    private static final String ROLE = "ROLE";
    private final String BAPTISMAL_CLASS_PREFERENCES = "BAPTISMAL_CLASS_PREFERENCES";

    private final String VERSION_NAME = "VERSION_NAME";
    private final String LANGUAGE = "LANGUAGE";
    private final String FONT_SIZE = "FONT_SIZE";
    private final String FONT = "FONT";
    private final String REGISTRATION_ID = "REGISTRATION_ID";
    private final String VERSION_DATA = "VERSION_DATA";
    private final String CACHE_USER_INFO = "CACHE_USER_INFO";
    private final String CACHE_CITY_LIST = "CACHE_CITY_LIST";
    private final String CACHE_CATEGORY_LIST = "CACHE_CATEGORY_LIST";
    private final String FIRST_OPEN_SEARCH_SCREEN = "FIRST_OPEN_SEARCH_SCREEN";
    private final String ID = "id";
    private final String EMAIL = "email";
    private final String NAME = "user_name";
    private final String FULL_NAME = "full_name";
    private final String ADDRESS = "address";
    private final String WAIT_APPROVE_SHOP_OWNER = "WAIT_APPROVE_SHOP_OWNER";
    private final String IS_CANNOT_SEND_REQUEST = "isCannotSendRequest";

    // ================================================================
    private Context context;
    private static MySharedPreferences instance;

    public MySharedPreferences(Context context) {
        this.context = context;
    }

    public static MySharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new MySharedPreferences(context);
            instance.context = context;
        }
        return instance;
    }

    // ======================== UTILITY FUNCTIONS ========================
    public void putVersionName() {
        putStringValue(VERSION_NAME, PacketUtility.getVersionName(context));
    }

    private String getVersionName() {
        return getStringValue(VERSION_NAME);
    }

    public boolean checkNewVersion() {
        return !PacketUtility.getVersionName(context).equals(getVersionName());
    }

    public void putLanguage(String language) {
        putStringValue(LANGUAGE, language);
    }

    public String getLanguage() {
        return getStringValue(LANGUAGE,"en");
    }

    public String getLanguageDisplayCode(){
        String cd = getStringValue(LANGUAGE,"en");
        if(cd.equalsIgnoreCase("en"))
            return "E";
       else return "AR";
    }

    public void putFontSize(float fontSize) {
        putFloatValue(FONT_SIZE, fontSize);
    }

    public float getFontSize() {
        return getFloatValue(FONT_SIZE);
    }

    public void putFont(String font) {
        putStringValue(FONT, font);
    }

    public String getFont() {
        return getStringValue(FONT);
    }

    //public void cached
    public void setCacheUserInfo(String userJson) {
        putStringValue(CACHE_USER_INFO, userJson);
    }

    public String getCachUserInfo() {
        return getStringValue(CACHE_USER_INFO);
    }

    public void setCacheCategories(String categoryJson) {
        putStringValue(CACHE_CATEGORY_LIST, categoryJson);
    }

    public String getCacheCategories() {
        return getStringValue(CACHE_CATEGORY_LIST);
    }

    public void setCacheCities(String citiesJson) {
        putStringValue(CACHE_CITY_LIST, citiesJson);
    }

    public String getCacheCities() {
        return getStringValue(CACHE_CITY_LIST);
    }

    //check is first open search screen
    public boolean isFirstOpenSearchScreen() {
        return getBooleanValue(FIRST_OPEN_SEARCH_SCREEN);
    }

    public void setFirstOpenSearchScreen(boolean isFirst) {
        putBooleanValue(FIRST_OPEN_SEARCH_SCREEN, isFirst);
    }

    public void saveUserInfo(Account account) {
        putStringValue(ID, account.getId());
        putStringValue(EMAIL, account.getEmail());
        putStringValue(NAME, account.getUserName());
        putStringValue(FULL_NAME, account.getFull_name());
        putStringValue(ADDRESS, account.getAddress());
        putStringValue(AVATAR, account.getAvatar());
        putStringValue(ROLE, account.getRole());
        putIntValue(WAIT_APPROVE_SHOP_OWNER, account.getWaitApproveShopOwner());
        putStringValue(IS_CANNOT_SEND_REQUEST, account.getIsCannotSendRequest());
    }

    public Account getUserInfo() {
        if (getStringValue(NAME, "").equals("")) {
            return null;
        } else {
            Account user = new Account();
            user.setId(getStringValue(ID, ""));
            user.setUserName(getStringValue(NAME, ""));
            user.setEmail(getStringValue(EMAIL, ""));
            user.setFull_name(getStringValue(FULL_NAME, ""));
            user.setAddress(getStringValue(ADDRESS, ""));
            user.setAvatar(getStringValue(AVATAR, ""));
            user.setRole(getStringValue(ROLE, ""));
            user.setWaitApproveShopOwner(getIntValue(WAIT_APPROVE_SHOP_OWNER));
            user.setIsCannotSendRequest(getStringValue(IS_CANNOT_SEND_REQUEST));
            return user;
        }
    }

    public void clearAccount() {
        putStringValue(ID, "");
        putStringValue(NAME, "");
        putStringValue(EMAIL, "");
        putStringValue(FULL_NAME, "");
        putStringValue(ADDRESS, "");
    }


    // ======================== CORE FUNCTIONS ========================

    /**
     * Save a long integer to SharedPreferences
     *
     * @param key
     * @param n
     */
    public void putLongValue(String key, long n) {
        // SmartLogger.log(TAG, "Set long integer value");
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, n);
        editor.commit();
    }

    /**
     * Read a long integer to SharedPreferences
     *
     * @param key
     * @return
     */
    public long getLongValue(String key) {
        // SmartLogger.log(TAG, "Get long integer value");
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        return pref.getLong(key, 0);
    }

    /**
     * Save an integer to SharedPreferences
     *
     * @param key
     * @param n
     */
    public void putIntValue(String key, int n) {
        // SmartLogger.log(TAG, "Set integer value");
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, n);
        editor.commit();
    }

    /**
     * Read an integer to SharedPreferences
     *
     * @param key
     * @return
     */
    public int getIntValue(String key) {
        // SmartLogger.log(TAG, "Get integer value");
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        return pref.getInt(key, 0);
    }

    /**
     * Save an string to SharedPreferences
     *
     * @param key
     * @param s
     */
    public void putStringValue(String key, String s) {
        // SmartLogger.log(TAG, "Set string value");
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, s);
        editor.commit();
    }

    /**
     * Read an string to SharedPreferences
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        // SmartLogger.log(TAG, "Get string value");
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        return pref.getString(key, "");
    }

    /**
     * Read an string to SharedPreferences
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringValue(String key, String defaultValue) {
        // SmartLogger.log(TAG, "Get string value");
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        return pref.getString(key, defaultValue);
    }

    /**
     * Save an boolean to SharedPreferences
     *
     * @param key
     * @param b
     */
    public void putBooleanValue(String key, Boolean b) {
        // SmartLogger.log(TAG, "Set boolean value");
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    /**
     * Read an boolean to SharedPreferences
     *
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        // SmartLogger.log(TAG, "Get boolean value");
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        return pref.getBoolean(key, false);
    }

    /**
     * Save an float to SharedPreferences
     *
     * @param key
     * @param f
     */
    public void putFloatValue(String key, float f) {
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, f);
        editor.commit();
    }

    /**
     * Read an float to SharedPreferences
     *
     * @param key
     * @return
     */
    public float getFloatValue(String key) {
        SharedPreferences pref = context.getSharedPreferences(BAPTISMAL_CLASS_PREFERENCES, 0);
        return pref.getFloat(key, 0.0f);
    }
}

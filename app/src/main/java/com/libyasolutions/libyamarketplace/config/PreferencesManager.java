package com.libyasolutions.libyamarketplace.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.libyasolutions.libyamarketplace.object.UserObj;

public class PreferencesManager {
    private String TAG = getClass().getSimpleName();

    private final String REAL_ESTATE_PREFERENCES = "com.apichat.realestate.preference";

    private static PreferencesManager instance;

    private Context context;

    // ==============================================================

    private final String USER_ID = "USER_ID";
    private final String USER_NAME = "USER_NAME";
    private final String PASSWORD = "PASSWORD";
    private final String EMAIL = "EMAIL";
    private final String NAME = "NAME";
    private final String ADDRESS = "ADDRESS";
    private final String PHONE = "PHONE";
    private final String SKYPE = "SKYPE";
    private final String SEX = "SEX";
    private final String HOST = "HOST";
    private final String IMAGE = "IMAGE";
    private final String TYPE = "TYPE";
    private final String WEBSITE = "WEBSITE";
    private final String CITY_ID = "CITY_ID";
    private final String COUNTRY_ID = "COUNTRY_ID";
    private final String STATE_ID = "STATE_ID";
    private final String COUNTRY_CODE = "COUNTRY_CODE";
    private final String IS_VERIFIED = "IS_VERIFIED";
    private final String SUBSCRIPTION_TYPE = "SUBSCRIPTION_TYPE";
    private final String ALREADY_HAVE_CARD = "ALREADY_HAVE_CARD";
    private final String LAST_4_CARD_NUMBER = "LAST_4_CARD_NUMBER";

    private final String FACEBOOK_TOKEN = "FACEBOOK_TOKEN";

    private final String FACEBOOK_NAME = "FACEBOOK_NAME";
    private final String FACEBOOK_EMAIL = "FACEBOOK_NAME";
    private final String FACEBOOK_ID = "FACEBOOK_ID";
    private final String FACEBOOK_FIRST_NAME = "FACEBOOK_FIRST_NAME";
    private final String FACEBOOK_LAST_NAME = "FACEBOOK_LAST_NAME";
    private final String FACEBOOK_IMAGE = "FACEBOOK_IMAGE";


    private PreferencesManager() {
    }

    /**
     * Constructor
     *
     * @param context
     * @return
     */
    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager();
            instance.context = context;
        }
        return instance;
    }

    public PreferencesManager(Context context) {
        this.context = context;
    }

    // ======================== MANAGER FUNCTIONS ==========================

    public void saveUserInfo(UserObj user) {
        putStringValue(USER_ID, user.getUser_id());
        putStringValue(USER_NAME, user.getUser_name());
        putStringValue(PASSWORD, user.getPassword());
        putStringValue(EMAIL, user.getEmail());
        putStringValue(NAME, user.getName());
        putStringValue(ADDRESS, user.getAddress());
        putBooleanValue(SEX, user.isSex());
        putStringValue(PHONE, user.getPhone());
        putStringValue(SKYPE, user.getSkype());
        putStringValue(IMAGE, user.getImage());
        putStringValue(TYPE, user.getType());
        putStringValue(WEBSITE, user.getWebsite());
        putStringValue(COUNTRY_ID, user.getCountryId());
        putStringValue(CITY_ID, user.getCityId());
        putStringValue(STATE_ID, user.getStateId());
        putStringValue(COUNTRY_CODE, user.getCountryCode());
        putStringValue(IS_VERIFIED, user.getIsVerified());
        putStringValue(SUBSCRIPTION_TYPE, user.getSubscriptionType());
        putStringValue(ALREADY_HAVE_CARD, user.getAlreadyHaveCard());
        putStringValue(LAST_4_CARD_NUMBER, user.getLast4());
    }

    public void savePass(String pass) {
        putStringValue("PASS", pass);
    }


    public UserObj getUserInfo() {
        if (getStringValue(USER_ID, "").equalsIgnoreCase("")) {
            return null;
        }
        UserObj user = new UserObj();
        user.setUser_id(getStringValue(USER_ID, ""));
        user.setUser_name(getStringValue(USER_NAME, ""));
        user.setPassword(getStringValue(PASSWORD, ""));
        user.setEmail(getStringValue(EMAIL, ""));
        user.setName(getStringValue(NAME, ""));
        user.setAddress(getStringValue(ADDRESS, ""));
        user.setSex(getBooleanValue(SEX));
        user.setPhone(getStringValue(PHONE, ""));
        user.setSkype(getStringValue(SKYPE, ""));
        user.setImage(getStringValue(IMAGE, ""));
        user.setType(getStringValue(TYPE, ""));
        user.setWebsite(getStringValue(WEBSITE, ""));
        user.setCountryId(getStringValue(COUNTRY_ID, ""));
        user.setCityId(getStringValue(CITY_ID, ""));
        user.setStateId(getStringValue(STATE_ID, ""));
        user.setCountryCode(getStringValue(COUNTRY_CODE, ""));
        user.setIsVerified(getStringValue(IS_VERIFIED, ""));
        user.setSubscriptionType(getStringValue(SUBSCRIPTION_TYPE, ""));
        user.setAlreadyHaveCard(getStringValue(ALREADY_HAVE_CARD, ""));
        user.setLast4(getStringValue(LAST_4_CARD_NUMBER, ""));
        return user;

    }

    public void clearUser() {
        putStringValue(USER_ID, "");
        putStringValue(USER_NAME, "");
        putStringValue(PASSWORD, "");
        putStringValue(EMAIL, "");
        putStringValue(NAME, "");
        putStringValue(ADDRESS, "");
        putBooleanValue(SEX, false);
        putBooleanValue(HOST, false);
        putStringValue(PHONE, "");
        putStringValue(SKYPE, "");
        putStringValue(IMAGE, "");
        putStringValue(TYPE, "");
        putStringValue(WEBSITE, "");
        putStringValue(COUNTRY_ID, "");
        putStringValue(CITY_ID, "");
        putStringValue(STATE_ID, "");
        putStringValue(COUNTRY_CODE, "");
        putStringValue(IS_VERIFIED, "");
        putStringValue(SUBSCRIPTION_TYPE, "");
        putStringValue(ALREADY_HAVE_CARD, "");
    }

    public void clearPass() {
        putStringValue("PASS", "");
    }

    public void setFacebookToken(String fbToken) {
        putStringValue(FACEBOOK_TOKEN, fbToken);
    }

    public void setFbAccount(String name, String firstName, String lastName,
                             String email, String id, String avatar) {
        putStringValue(FACEBOOK_ID, id);
        putStringValue(FACEBOOK_NAME, name);
        putStringValue(FACEBOOK_FIRST_NAME, firstName);
        putStringValue(FACEBOOK_LAST_NAME, lastName);
        putStringValue(FACEBOOK_EMAIL, email);
        putStringValue(FACEBOOK_IMAGE, avatar);
    }

    // ======================== UTILITY FUNCTIONS ========================

    /**
     * Save a long integer to SharedPreferences
     *
     * @param key
     * @param n
     */
    public void putLongValue(String key, long n) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
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
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getLong(key, 0);
    }

    /**
     * Save an integer to SharedPreferences
     *
     * @param key
     * @param n
     */
    public void putIntValue(String key, int n) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
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
        // SmartLog.log(TAG, "Get integer value");
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getInt(key, 0);
    }

    /**
     * Save an string to SharedPreferences
     *
     * @param key
     * @param s
     */
    public void putStringValue(String key, String s) {
        // SmartLog.log(TAG, "Set string value");
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
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
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
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
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getString(key, defaultValue);
    }

    /**
     * Save an boolean to SharedPreferences
     *
     * @param key
     */
    public void putBooleanValue(String key, Boolean b) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
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
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getBoolean(key, false);
    }

    /**
     * Save an float to SharedPreferences
     *
     * @param key
     */
    public void putFloatValue(String key, float f) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
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
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getFloat(key, 0.0f);
    }

}

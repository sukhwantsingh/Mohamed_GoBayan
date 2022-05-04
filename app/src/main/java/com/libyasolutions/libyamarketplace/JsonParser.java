package com.libyasolutions.libyamarketplace;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GL62 on 8/15/2017.
 */

public class JsonParser {
    private static String SUCCESS = "success";
    private static String STATUS = "status";
    private static String ERROR = "error";
    private static String MESSAGE = "message";

    public static boolean isSuccess(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.getString(STATUS).equals(SUCCESS)) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static String getMessage(String json){
        String message =  "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            message = jsonObject.getString(MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}

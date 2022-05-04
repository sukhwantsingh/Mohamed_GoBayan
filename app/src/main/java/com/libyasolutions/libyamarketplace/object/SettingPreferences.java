package com.libyasolutions.libyamarketplace.object;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingPreferences {

    public static final String SORT_BY_RATING = "rate";
    public static final String SORT_BY_NAME = "name";
    public static final String SORT_BY_DATE = "date";
    public static final String ALL = "ALL";
    public static final String OPEN = "OPEN";
    public static final String SORT_TYPE_DESC = "desc";
    public static final String SORT_TYPE_ASC = "asc";
    public static final String TYPE_SHOP = "SHOP";
    public static final String TYPE_PRODUCTS = "PRODUCT";

    private String id;
    private String userId;
    private String cityID = "0", categoryId = "0";
    private String type = TYPE_SHOP;
    private String status = ALL;
    private String sortType = SORT_TYPE_ASC;
    private String sortBy = SORT_BY_NAME;
    private String distance = "0";

    public SettingPreferences() {

    }

    public SettingPreferences(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            cityID = jsonObj.getString("cityID");
            categoryId = jsonObj.getString("categoryId");
            type = jsonObj.getString("type");
            status = jsonObj.getString("status");
            sortType = jsonObj.getString("sortType");
            sortBy = jsonObj.getString("sortBy");
            distance = jsonObj.getString("distance");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cityID", cityID);
            jsonObject.put("categoryId", categoryId);
            jsonObject.put("type", type);
            jsonObject.put("status", status);
            jsonObject.put("sortType", sortType);
            jsonObject.put("sortBy", sortBy);
            jsonObject.put("distance", distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public int selectedCityIndex(ArrayList<City> arrCities) {
        int index = 0;
        City city = null;
        if (arrCities.size() > 0) {
            for (int i = 0; i < arrCities.size(); i++) {
                city = arrCities.get(i);
                if (city.getId().equals(cityID)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public int selectedCategoryIndex(ArrayList<Category> arrCategories) {
        int index = 0;
        Category category = null;
        if (arrCategories.size() > 0) {
            for (int i = 0; i < arrCategories.size(); i++) {
                category = arrCategories.get(i);
                if (category.getId().equals(categoryId)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public int selectedSortByIndex() {
        if (sortBy.equals(SORT_BY_RATING)) {
            return 0;
        } else if (sortBy.equals(SORT_BY_NAME)) {
            return 1;
        } else if (sortBy.equals(SORT_BY_DATE))
            return 2;
        else
            return 0;
    }

    public int selectedSortTypeIndex() {
        if (sortType.equals(SORT_TYPE_DESC)) {
            return 0;
        } else
            return 1;
    }

    public boolean isOpenShopList() {
        return status.equals(OPEN);
    }

    public boolean isShopType() {
        return type.equals(TYPE_SHOP);
    }
}

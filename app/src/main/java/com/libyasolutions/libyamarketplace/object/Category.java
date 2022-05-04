package com.libyasolutions.libyamarketplace.object;

import java.util.ArrayList;

public class Category {

    private String id;
    private String name;
    private String description;
    private String image;
    private int parentId;
    private boolean isCheck;

    private ArrayList<ExtraOptions> arrExtraOptions;

    public ArrayList<ExtraOptions> getArrExtraOptions() {
        return arrExtraOptions;
    }

    public void setArrExtraOptions(ArrayList<ExtraOptions> arrExtraOptions) {
        this.arrExtraOptions = arrExtraOptions;
    }

    public Category() {

    }

    public Category(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

}

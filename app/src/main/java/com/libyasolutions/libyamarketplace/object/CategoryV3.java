package com.libyasolutions.libyamarketplace.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CategoryV3 implements Parcelable {
    private String departmentPosition;
    private String departmentId;
    private String departmentName;
    private String menuId;
    private String menuName;
    private boolean isChecked;
    private boolean isHeader;

    public CategoryV3() {
    }

    public CategoryV3(String departmentId, String departmentName, boolean isHeader) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.isHeader = isHeader;
    }

    public String getDepartmentPosition() {
        return departmentPosition;
    }

    public void setDepartmentPosition(String departmentPosition) {
        this.departmentPosition = departmentPosition;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public CategoryV3(String departmentPosition, String departmentId, String departmentName, String menuId, String menuName) {
        this.departmentPosition = departmentPosition;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.menuId = menuId;
        this.menuName = menuName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.departmentPosition);
        dest.writeString(this.departmentId);
        dest.writeString(this.departmentName);
        dest.writeString(this.menuId);
        dest.writeString(this.menuName);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isHeader ? (byte) 1 : (byte) 0);
    }

    protected CategoryV3(Parcel in) {
        this.departmentPosition = in.readString();
        this.departmentId = in.readString();
        this.departmentName = in.readString();
        this.menuId = in.readString();
        this.menuName = in.readString();
        this.isChecked = in.readByte() != 0;
        this.isHeader = in.readByte() != 0;
    }

    public static final Creator<CategoryV3> CREATOR = new Creator<CategoryV3>() {
        @Override
        public CategoryV3 createFromParcel(Parcel source) {
            return new CategoryV3(source);
        }

        @Override
        public CategoryV3[] newArray(int size) {
            return new CategoryV3[size];
        }
    };
}

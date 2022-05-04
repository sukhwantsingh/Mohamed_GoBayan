package com.libyasolutions.libyamarketplace.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DepartmentCategory implements Parcelable {
    private String departmentId;
    private String departmentName;
    private String departmentThumbnail;
    private List<CategoryV2> categoryV2List;
    private boolean isChecked;

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

    public String getDepartmentThumbnail() {
        return departmentThumbnail;
    }

    public void setDepartmentThumbnail(String departmentThumbnail) {
        this.departmentThumbnail = departmentThumbnail;
    }

    public List<CategoryV2> getCategoryV2List() {
        return categoryV2List;
    }

    public void setCategoryV2List(List<CategoryV2> categoryV2List) {
        this.categoryV2List = categoryV2List;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.departmentId);
        dest.writeString(this.departmentName);
        dest.writeString(this.departmentThumbnail);
        dest.writeList(this.categoryV2List);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    public DepartmentCategory() {
    }

    protected DepartmentCategory(Parcel in) {
        this.departmentId = in.readString();
        this.departmentName = in.readString();
        this.departmentThumbnail = in.readString();
        this.categoryV2List = new ArrayList<CategoryV2>();
        in.readList(this.categoryV2List, CategoryV2.class.getClassLoader());
        this.isChecked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<DepartmentCategory> CREATOR = new Parcelable.Creator<DepartmentCategory>() {
        @Override
        public DepartmentCategory createFromParcel(Parcel source) {
            return new DepartmentCategory(source);
        }

        @Override
        public DepartmentCategory[] newArray(int size) {
            return new DepartmentCategory[size];
        }
    };
}

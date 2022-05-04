package com.libyasolutions.libyamarketplace.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryV2 implements Parcelable {
    private String menuId;
    private String menuName;
    private String menuDescription;
    private String menuThumbnail;
    private String status;
    private List<OptionV2> optionV2List;

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

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public String getMenuThumbnail() {
        return menuThumbnail;
    }

    public void setMenuThumbnail(String menuThumbnail) {
        this.menuThumbnail = menuThumbnail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OptionV2> getOptionV2List() {
        return optionV2List;
    }

    public void setOptionV2List(List<OptionV2> optionV2List) {
        this.optionV2List = optionV2List;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryV2)) return false;
        CategoryV2 that = (CategoryV2) o;
        return Objects.equals(getMenuId(), that.getMenuId()) &&
                Objects.equals(getMenuName(), that.getMenuName()) &&
                Objects.equals(getMenuDescription(), that.getMenuDescription()) &&
                Objects.equals(getMenuThumbnail(), that.getMenuThumbnail()) &&
                Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getOptionV2List(), that.getOptionV2List());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMenuId(), getMenuName(), getMenuDescription(), getMenuThumbnail(), getStatus(), getOptionV2List());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.menuId);
        dest.writeString(this.menuName);
        dest.writeString(this.menuDescription);
        dest.writeString(this.menuThumbnail);
        dest.writeString(this.status);
        dest.writeList(this.optionV2List);
    }

    public CategoryV2() {
    }

    protected CategoryV2(Parcel in) {
        this.menuId = in.readString();
        this.menuName = in.readString();
        this.menuDescription = in.readString();
        this.menuThumbnail = in.readString();
        this.status = in.readString();
        this.optionV2List = new ArrayList<OptionV2>();
        in.readList(this.optionV2List, OptionV2.class.getClassLoader());
    }

    public static final Parcelable.Creator<CategoryV2> CREATOR = new Parcelable.Creator<CategoryV2>() {
        @Override
        public CategoryV2 createFromParcel(Parcel source) {
            return new CategoryV2(source);
        }

        @Override
        public CategoryV2[] newArray(int size) {
            return new CategoryV2[size];
        }
    };
}

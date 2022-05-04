package com.libyasolutions.libyamarketplace.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OptionV2 implements Parcelable {
    private String optionId;
    private String optionName;
    private String optionDesc;
    private List<OptionItemV2> optionItemV2List;

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionDesc() {
        return optionDesc;
    }

    public void setOptionDesc(String optionDesc) {
        this.optionDesc = optionDesc;
    }

    public List<OptionItemV2> getOptionItemV2List() {
        return optionItemV2List;
    }

    public void setOptionItemV2List(List<OptionItemV2> optionItemV2List) {
        this.optionItemV2List = optionItemV2List;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OptionV2)) return false;
        OptionV2 optionV2 = (OptionV2) o;
        return Objects.equals(getOptionId(), optionV2.getOptionId()) &&
                Objects.equals(getOptionName(), optionV2.getOptionName()) &&
                Objects.equals(getOptionDesc(), optionV2.getOptionDesc()) &&
                Objects.equals(getOptionItemV2List(), optionV2.getOptionItemV2List());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOptionId(), getOptionName(), getOptionDesc(), getOptionItemV2List());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.optionId);
        dest.writeString(this.optionName);
        dest.writeString(this.optionDesc);
        dest.writeList(this.optionItemV2List);
    }

    public OptionV2() {
    }

    protected OptionV2(Parcel in) {
        this.optionId = in.readString();
        this.optionName = in.readString();
        this.optionDesc = in.readString();
        this.optionItemV2List = new ArrayList<OptionItemV2>();
        in.readList(this.optionItemV2List, OptionItemV2.class.getClassLoader());
    }

    public static final Parcelable.Creator<OptionV2> CREATOR = new Parcelable.Creator<OptionV2>() {
        @Override
        public OptionV2 createFromParcel(Parcel source) {
            return new OptionV2(source);
        }

        @Override
        public OptionV2[] newArray(int size) {
            return new OptionV2[size];
        }
    };
}

package com.libyasolutions.libyamarketplace.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class OptionItemV2 implements Parcelable {
    private String optionItemId;
    private String optionItemName;

    public String getOptionItemId() {
        return optionItemId;
    }

    public void setOptionItemId(String optionItemId) {
        this.optionItemId = optionItemId;
    }

    public String getOptionItemName() {
        return optionItemName;
    }

    public void setOptionItemName(String optionItemName) {
        this.optionItemName = optionItemName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OptionItemV2)) return false;
        OptionItemV2 that = (OptionItemV2) o;
        return Objects.equals(getOptionItemId(), that.getOptionItemId()) &&
                Objects.equals(getOptionItemName(), that.getOptionItemName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOptionItemId(), getOptionItemName());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.optionItemId);
        dest.writeString(this.optionItemName);
    }

    public OptionItemV2() {
    }

    protected OptionItemV2(Parcel in) {
        this.optionItemId = in.readString();
        this.optionItemName = in.readString();
    }

    public static final Parcelable.Creator<OptionItemV2> CREATOR = new Parcelable.Creator<OptionItemV2>() {
        @Override
        public OptionItemV2 createFromParcel(Parcel source) {
            return new OptionItemV2(source);
        }

        @Override
        public OptionItemV2[] newArray(int size) {
            return new OptionItemV2[size];
        }
    };
}

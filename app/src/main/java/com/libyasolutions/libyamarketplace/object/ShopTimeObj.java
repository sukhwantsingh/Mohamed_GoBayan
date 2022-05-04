package com.libyasolutions.libyamarketplace.object;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopTimeObj implements Parcelable {
    private String dateId;
    private String dayOfWeek;
    private String openAM,closeAM;
    private String openPM, closePM;

    public ShopTimeObj(String dateId, String dayOfWeek, String openAM, String closeAM, String openPM, String closePM) {
        this.dateId = dateId;
        this.dayOfWeek = dayOfWeek;
        this.openAM = openAM;
        this.closeAM = closeAM;
        this.openPM = openPM;
        this.closePM = closePM;
    }

    protected ShopTimeObj(Parcel in) {
        dateId = in.readString();
        dayOfWeek = in.readString();
        openAM = in.readString();
        closeAM = in.readString();
        openPM = in.readString();
        closePM = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateId);
        dest.writeString(dayOfWeek);
        dest.writeString(openAM);
        dest.writeString(closeAM);
        dest.writeString(openPM);
        dest.writeString(closePM);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShopTimeObj> CREATOR = new Creator<ShopTimeObj>() {
        @Override
        public ShopTimeObj createFromParcel(Parcel in) {
            return new ShopTimeObj(in);
        }

        @Override
        public ShopTimeObj[] newArray(int size) {
            return new ShopTimeObj[size];
        }
    };

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }

    public String getOpenAM() {
        return openAM;
    }

    public void setOpenAM(String openAM) {
        this.openAM = openAM;
    }

    public String getCloseAM() {
        return closeAM;
    }

    public void setCloseAM(String closeAM) {
        this.closeAM = closeAM;
    }

    public String getOpenPM() {
        return openPM;
    }

    public void setOpenPM(String openPM) {
        this.openPM = openPM;
    }

    public String getClosePM() {
        return closePM;
    }

    public void setClosePM(String closePM) {
        this.closePM = closePM;
    }
}

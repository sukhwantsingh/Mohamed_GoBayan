package com.libyasolutions.libyamarketplace.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class OpenHour implements Parcelable {

    private int dateId;
    private String dateName;
    private int shopId;
    private String openAM;
    private String closeAM;
    private String openPM;
    private String closePM;
    private String open_AM1 = "";
    private String close_PM2 = "";

    public OpenHour() {
    }

    public OpenHour(int dateId, String dateName, String openAM, String closeAM, String openPM, String closePM) {
        this.dateId = dateId;
        this.dateName = dateName;
        this.openAM = openAM;
        this.closeAM = closeAM;
        this.openPM = openPM;
        this.closePM = closePM;
    }

    protected OpenHour(Parcel in) {
        dateId = in.readInt();
        dateName = in.readString();
        shopId = in.readInt();
        openAM = in.readString();
        closeAM = in.readString();
        openPM = in.readString();
        closePM = in.readString();
        open_AM1 = in.readString();
        close_PM2 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dateId);
        dest.writeString(dateName);
        dest.writeInt(shopId);
        dest.writeString(openAM);
        dest.writeString(closeAM);
        dest.writeString(openPM);
        dest.writeString(closePM);
        dest.writeString(open_AM1);
        dest.writeString(close_PM2);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OpenHour> CREATOR = new Creator<OpenHour>() {
        @Override
        public OpenHour createFromParcel(Parcel in) {
            return new OpenHour(in);
        }

        @Override
        public OpenHour[] newArray(int size) {
            return new OpenHour[size];
        }
    };

    public String getOpen_AM1() {
        return open_AM1;
    }

    public void setOpen_AM1(String open_AM1) {
        this.open_AM1 = open_AM1;
    }

    public String getClose_PM2() {
        return close_PM2;
    }

    public void setClose_PM2(String close_PM2) {
        this.close_PM2 = close_PM2;
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
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

    public boolean isCloseInNextDay() {
        Calendar calOpen = Calendar.getInstance();
        calOpen.setTimeInMillis(Long.parseLong(this.openAM) * 1000);

        Calendar calClose = Calendar.getInstance();
        calClose.setTimeInMillis(Long.parseLong(this.closePM) * 1000);
        return calClose.get(Calendar.DAY_OF_MONTH) > calOpen.get(Calendar.DAY_OF_MONTH);
    }
}



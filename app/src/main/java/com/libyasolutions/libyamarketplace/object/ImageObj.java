package com.libyasolutions.libyamarketplace.object;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageObj implements Parcelable {
    private String id = "", imageURL = "";
    private Bitmap bitmap;

    public ImageObj() {
    }

    protected ImageObj(Parcel in) {
        id = in.readString();
        imageURL = in.readString();
        try {
            bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageURL);
        dest.writeParcelable(bitmap, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageObj> CREATOR = new Creator<ImageObj>() {
        @Override
        public ImageObj createFromParcel(Parcel in) {
            return new ImageObj(in);
        }

        @Override
        public ImageObj[] newArray(int size) {
            return new ImageObj[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

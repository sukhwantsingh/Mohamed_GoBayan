package com.libyasolutions.libyamarketplace.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Menu implements Parcelable {

    private int id;
    private String name, localName;
    private String code;
    private int shopId;
    private String categoryId;
    private double price, percentDiscount;
    private String image, vat, ship;
    private String thumbnail;
    private String description;
    private int orderNumber;
    private float rateValue = 0;
    private int rateNumber = 0;
    private String category;
    private Shop shop;
    private ArrayList<Banner> arrGalleries;
    private boolean isFavourite = false;
    private String shopOwnerId;
    private String shopOwnerName;
    private String shopOwnerImage;
    private String shopFacebook, shopInstagram, shopTwitter;
    private int countShop;
    private String status;
    private String available;
    private String option;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public Menu() {
        arrGalleries = new ArrayList<>();
    }

    protected Menu(Parcel in) {
        id = in.readInt();
        name = in.readString();
        localName = in.readString();
        code = in.readString();
        shopId = in.readInt();
        categoryId = in.readString();
        price = in.readDouble();
        percentDiscount = in.readDouble();
        image = in.readString();
        vat = in.readString();
        ship = in.readString();
        thumbnail = in.readString();
        description = in.readString();
        orderNumber = in.readInt();
        rateValue = in.readFloat();
        rateNumber = in.readInt();
        category = in.readString();
        shop = in.readParcelable(Shop.class.getClassLoader());
        isFavourite = in.readByte() != 0;
        shopOwnerId = in.readString();
        shopOwnerName = in.readString();
        shopOwnerImage = in.readString();
        shopFacebook = in.readString();
        shopInstagram = in.readString();
        shopTwitter = in.readString();
        countShop = in.readInt();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(localName);
        dest.writeString(code);
        dest.writeInt(shopId);
        dest.writeString(categoryId);
        dest.writeDouble(price);
        dest.writeDouble(percentDiscount);
        dest.writeString(image);
        dest.writeString(vat);
        dest.writeString(ship);
        dest.writeString(thumbnail);
        dest.writeString(description);
        dest.writeInt(orderNumber);
        dest.writeFloat(rateValue);
        dest.writeInt(rateNumber);
        dest.writeString(category);
        dest.writeParcelable(shop, flags);
        dest.writeByte((byte) (isFavourite ? 1 : 0));
        dest.writeString(shopOwnerId);
        dest.writeString(shopOwnerName);
        dest.writeString(shopOwnerImage);
        dest.writeString(shopFacebook);
        dest.writeString(shopInstagram);
        dest.writeString(shopTwitter);
        dest.writeInt(countShop);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private ArrayList<ExtraOptions> extraOptions;

    public ArrayList<ExtraOptions> getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(ArrayList<ExtraOptions> extraOptions) {
        this.extraOptions = extraOptions;
    }

    public int getCountShop() {
        return countShop;
    }

    public void setCountShop(int countShop) {
        this.countShop = countShop;
    }

    public String getShopFacebook() {
        return shopFacebook;
    }

    public void setShopFacebook(String shopFacebook) {
        this.shopFacebook = shopFacebook;
    }

    public String getShopInstagram() {
        return shopInstagram;
    }

    public void setShopInstagram(String shopInstagram) {
        this.shopInstagram = shopInstagram;
    }

    public String getShopTwitter() {
        return shopTwitter;
    }

    public void setShopTwitter(String shopTwitter) {
        this.shopTwitter = shopTwitter;
    }

    public String getShopOwnerId() {
        return shopOwnerId;
    }

    public void setShopOwnerId(String shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getShopOwnerImage() {
        return shopOwnerImage;
    }

    public void setShopOwnerImage(String shopOwnerImage) {
        this.shopOwnerImage = shopOwnerImage;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    public double getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(double percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getTotalPrice() {
        return orderNumber * price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public double getCurrentPrice() {
        return price - (price * percentDiscount / 100);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRateValue() {
        return rateValue;
    }

    public void setRateValue(float rateValue) {
        this.rateValue = rateValue;
    }

    public int getRateNumber() {
        return rateNumber;
    }

    public void setRateNumber(int rateNumber) {
        this.rateNumber = rateNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public ArrayList<Banner> getArrGalleries() {
        return arrGalleries;
    }

    public void setArrGalleries(ArrayList<Banner> arrGalleries) {
        this.arrGalleries = arrGalleries;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }
}

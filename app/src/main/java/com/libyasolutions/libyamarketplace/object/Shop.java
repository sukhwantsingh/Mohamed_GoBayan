package com.libyasolutions.libyamarketplace.object;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Shop implements Parcelable {
    private int shopId;
    private String shopName;
    private int cityId;
    private String cityName;
    private String address;
    private String category = "";
    private String image, thumbnail;
    private String description;
    private String phone;
    private Double longitude;
    private Double latitude;
    private OpenHour openHour;// open hour of today
    private ArrayList<OpenHour> arrOpenHour;// all open hour in week
    private ArrayList<Banner> arrBanner;
    private ArrayList<Offer> arrOffer;
    private int status;
    private Bitmap bmImage;
    private boolean isOpen = true;

    private String isVerified = "0";
    private String isFeatured = "0";
    private boolean isFavourite = false;
    private String facebook;
    private String twitter;
    private String email;
    private String live_chat;
    private String website = "";

    private double shopVAT = 0;
    private double deliveryPrice = 0;
    private double minPriceForDelivery = 0;
    // rate
    private float rateValue = 0;
    private int rateNumber = 0;
    // process order
    private ArrayList<Menu> arrOrderFoods;
    private double totalPrice = 0;
    private int currentItemsNumber = 0;

    private String shopOwnerId;
    private String shopOwnerImage;
    private String shopOwnerName;

    private String shopFacebook, shopInstagram, shopTwitter;
    private int countShop;

    private String categoryId;
    private String shopTime;
    private String gmt;

    protected Shop(Parcel in) {
        shopId = in.readInt();
        shopName = in.readString();
        cityId = in.readInt();
        address = in.readString();
        category = in.readString();
        image = in.readString();
        thumbnail = in.readString();
        description = in.readString();
        phone = in.readString();
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        status = in.readInt();
        try {
            bmImage = in.readParcelable(Bitmap.class.getClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        }
        isOpen = in.readByte() != 0;
        isVerified = in.readString();
        isFeatured = in.readString();
        isFavourite = in.readByte() != 0;
        facebook = in.readString();
        twitter = in.readString();
        email = in.readString();
        live_chat = in.readString();
        website = in.readString();
        shopVAT = in.readDouble();
        deliveryPrice = in.readDouble();
        minPriceForDelivery = in.readDouble();
        rateValue = in.readFloat();
        rateNumber = in.readInt();
        totalPrice = in.readDouble();
        currentItemsNumber = in.readInt();
        shopOwnerId = in.readString();
        shopOwnerImage = in.readString();
        shopOwnerName = in.readString();
        shopFacebook = in.readString();
        shopInstagram = in.readString();
        shopTwitter = in.readString();
        cityName = in.readString();
        countShop = in.readInt();
        categoryId = in.readString();
        shopTime = in.readString();
        gmt = in.readString();
        listImage = in.createTypedArrayList(ImageObj.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shopId);
        dest.writeString(shopName);
        dest.writeInt(cityId);
        dest.writeString(address);
        dest.writeString(category);
        dest.writeString(image);
        dest.writeString(thumbnail);
        dest.writeString(description);
        dest.writeString(phone);
        dest.writeString(cityName);
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        dest.writeInt(status);
        dest.writeParcelable(bmImage, flags);
        dest.writeByte((byte) (isOpen ? 1 : 0));
        dest.writeString(isVerified);
        dest.writeString(isFeatured);
        dest.writeByte((byte) (isFavourite ? 1 : 0));
        dest.writeString(facebook);
        dest.writeString(twitter);
        dest.writeString(email);
        dest.writeString(live_chat);
        dest.writeString(website);
        dest.writeDouble(shopVAT);
        dest.writeDouble(deliveryPrice);
        dest.writeDouble(minPriceForDelivery);
        dest.writeFloat(rateValue);
        dest.writeInt(rateNumber);
        dest.writeDouble(totalPrice);
        dest.writeInt(currentItemsNumber);
        dest.writeString(shopOwnerId);
        dest.writeString(shopOwnerImage);
        dest.writeString(shopOwnerName);
        dest.writeString(shopFacebook);
        dest.writeString(shopInstagram);
        dest.writeString(shopTwitter);
        dest.writeInt(countShop);
        dest.writeString(categoryId);
        dest.writeString(shopTime);
        dest.writeString(gmt);
        dest.writeTypedList(listImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getGmt() {
        return gmt;
    }

    public void setGmt(String gmt) {
        this.gmt = gmt;
    }

    public String getShopTime() {
        return shopTime;
    }

    public void setShopTime(String shopTime) {
        this.shopTime = shopTime;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    private ArrayList<ImageObj> listImage;

    public ArrayList<ImageObj> getListImage() {
        return listImage;
    }

    public void setListImage(ArrayList<ImageObj> listImage) {
        this.listImage = listImage;
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

    public String getShopOwnerImage() {
        return shopOwnerImage;
    }

    public void setShopOwnerImage(String shopOwnerImage) {
        this.shopOwnerImage = shopOwnerImage;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public Shop() {
        arrOrderFoods = new ArrayList<Menu>();
        listImage = new ArrayList<>();
        arrBanner = new ArrayList<>();
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

    public ArrayList<Menu> getArrOrderFoods() {
        return arrOrderFoods;
    }

    public void setArrOrderFoods(ArrayList<Menu> arrOrderFoods) {
        this.arrOrderFoods = arrOrderFoods;
    }

    public double getShopVAT() {
        return shopVAT;
    }

    public void setShopVAT(double shopVAT) {
        this.shopVAT = shopVAT;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public double getMinPriceForDelivery() {
        return minPriceForDelivery;
    }

    public void setMinPriceForDelivery(double minPriceForDelivery) {
        this.minPriceForDelivery = minPriceForDelivery;
    }

    public int getShopId() {
        return shopId;
    }

    public ArrayList<OpenHour> getArrOpenHour() {
        return arrOpenHour;
    }

    public void setArrOpenHour(ArrayList<OpenHour> arrOpenHour) {
        this.arrOpenHour = arrOpenHour;
    }

    public ArrayList<Banner> getArrBanner() {
        return arrBanner;
    }

    public void setArrBanner(ArrayList<Banner> arrBanner) {
        this.arrBanner = arrBanner;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getCategory() {
        return category.isEmpty() ? phone : category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public OpenHour getOpenHour() {
        return openHour;
    }

    public void setOpenHour(OpenHour openHour) {
        this.openHour = openHour;
    }

    public ArrayList<Offer> getArrOffer() {
        return arrOffer;
    }

    public void setArrOffer(ArrayList<Offer> arrOffer) {
        this.arrOffer = arrOffer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Bitmap getBmImage() {
        return bmImage;
    }

    public void setBmImage(Bitmap bmImage) {
        this.bmImage = bmImage;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void addFoodOrder(Menu food) {
        arrOrderFoods.add(food);
        updateTotalPriceWithoutVATandDelivery();
    }

    public void removeFoodOrder(Menu food) {
        int size = arrOrderFoods.size();
        Menu food1 = null;
        for (int i = size - 1; i >= 0; i--) {
            food1 = arrOrderFoods.get(i);
            if (food1.getId() == food.getId()) {
                arrOrderFoods.remove(i);
                break;
            }
        }
        updateTotalPriceWithoutVATandDelivery();
    }

    public void updateQuantityOfFood(int indexFood, int quantity) {
        arrOrderFoods.get(indexFood).setOrderNumber(quantity);
        updateTotalPriceWithoutVATandDelivery();
    }

    public double getCurrentTotalVAT() {
        return totalPrice * shopVAT / 100;
    }

    public int getNumberItems() {
        return currentItemsNumber;
    }

    public void setNumberItems(int currentItemsNumber) {
        this.currentItemsNumber = currentItemsNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getcurrentShipping() {
        if (totalPrice > minPriceForDelivery)
            return 0;
        else
            return deliveryPrice;
    }

    public void updateTotalPriceWithoutVATandDelivery() {
        totalPrice = 0;
        currentItemsNumber = 0;
        for (Menu food : arrOrderFoods) {
            totalPrice += (food.getPrice() - (food.getPrice()
                    * food.getPercentDiscount() / 100))
                    * food.getOrderNumber();
            currentItemsNumber += food.getOrderNumber();

        }
    }

    public boolean isVerified() {
        return isVerified.equalsIgnoreCase("1");
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public boolean isFeatured() {
        return isFeatured.equalsIgnoreCase("1");
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLive_chat() {
        return live_chat;
    }

    public void setLive_chat(String live_chat) {
        this.live_chat = live_chat;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

}
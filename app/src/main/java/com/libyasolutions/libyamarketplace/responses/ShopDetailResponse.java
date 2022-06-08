package com.libyasolutions.libyamarketplace.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopDetailResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("data")
    @Expose
    public Data data;
    @SerializedName("message")
    @Expose
    public String message;
    public class ShopBanner {

        @SerializedName("banner_id")
        @Expose
        public String bannerId;
        @SerializedName("banner_name")
        @Expose
        public String bannerName;
        @SerializedName("banner_image")
        @Expose
        public String bannerImage;

    }

    public class SimilarProduct {

        @SerializedName("shop_id")
        @Expose
        public String shopId;
        @SerializedName("shop_name")
        @Expose
        public String shopName;
        @SerializedName("shop_address")
        @Expose
        public String shopAddress;
        @SerializedName("shop_city")
        @Expose
        public String shopCity;
        @SerializedName("shop_tel")
        @Expose
        public String shopTel;
        @SerializedName("shop_website")
        @Expose
        public String shopWebsite;
        @SerializedName("shop_description")
        @Expose
        public String shopDescription;
        @SerializedName("shop_image")
        @Expose
        public String shopImage;
        @SerializedName("shop_thumbnail")
        @Expose
        public String shopThumbnail;
        @SerializedName("shop_latitude")
        @Expose
        public String shopLatitude;
        @SerializedName("shop_longitude")
        @Expose
        public String shopLongitude;
        @SerializedName("shop_openHour")
        @Expose
        public String shopOpenHour;
        @SerializedName("shop_banners")
        @Expose
        public List<ShopBanner> shopBanners = null;
        @SerializedName("shop_lastOrder")
        @Expose
        public String shopLastOrder;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("rate")
        @Expose
        public String rate;
        @SerializedName("rate_times")
        @Expose
        public String rateTimes;
        @SerializedName("isVerified")
        @Expose
        public String isVerified;
        @SerializedName("isFeatured")
        @Expose
        public String isFeatured;
        @SerializedName("facebook")
        @Expose
        public String facebook;
        @SerializedName("twitter")
        @Expose
        public String twitter;
        @SerializedName("instagram")
        @Expose
        public String instagram;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("live_chat")
        @Expose
        public String liveChat;
        @SerializedName("website")
        @Expose
        public String website;
        @SerializedName("location_hotline")
        @Expose
        public String locationHotline;
        @SerializedName("name_category")
        @Expose
        public String nameCategory;
        @SerializedName("number_product")
        @Expose
        public String numberProduct;
        @SerializedName("openHour")
        @Expose
        public List<OpenHour> openHour = null;
        @SerializedName("isFavourite")
        @Expose
        public Integer isFavourite;
        @SerializedName("is_open")
        @Expose
        public Integer isOpen;
        @SerializedName("shopOwnerId")
        @Expose
        public String shopOwnerId;
        @SerializedName("shopOwnerImage")
        @Expose
        public String shopOwnerImage;
        @SerializedName("shopOwnerName")
        @Expose
        public String shopOwnerName;
        @SerializedName("countShops")
        @Expose
        public Integer countShops;
        @SerializedName("followedShopStatus")
        @Expose
        public Integer followedShopStatus;

    }

    public class OpenHour {

        @SerializedName("date_id")
        @Expose
        public String dateId;
        @SerializedName("date_name")
        @Expose
        public String dateName;
        @SerializedName("open_AM")
        @Expose
        public Integer openAM;
        @SerializedName("close_AM")
        @Expose
        public Integer closeAM;
        @SerializedName("open_PM")
        @Expose
        public Integer openPM;
        @SerializedName("close_PM")
        @Expose
        public Integer closePM;
        @SerializedName("open_AM1")
        @Expose
        public String openAM1;
        @SerializedName("close_AM2")
        @Expose
        public String closeAM2;
        @SerializedName("open_PM1")
        @Expose
        public String openPM1;
        @SerializedName("close_PM2")
        @Expose
        public String closePM2;

    }
    public class Data {

        @SerializedName("shop_id")
        @Expose
        public String shopId;
        @SerializedName("shop_name")
        @Expose
        public String shopName;
        @SerializedName("chat_privacy")
        @Expose
        public String chat_privacy;
        @SerializedName("shop_address")
        @Expose
        public String shopAddress;
        @SerializedName("shop_city")
        @Expose
        public String shopCity;
        @SerializedName("shop_tel")
        @Expose
        public String shopTel;
        @SerializedName("shop_website")
        @Expose
        public String shopWebsite;
        @SerializedName("shop_description")
        @Expose
        public String shopDescription;
        @SerializedName("shop_image")
        @Expose
        public String shopImage;
        @SerializedName("shop_thumbnail")
        @Expose
        public String shopThumbnail;
        @SerializedName("shop_latitude")
        @Expose
        public String shopLatitude;
        @SerializedName("shop_longitude")
        @Expose
        public String shopLongitude;
        @SerializedName("shop_openHour")
        @Expose
        public String shopOpenHour;
        @SerializedName("shop_banners")
        @Expose
        public List<ShopBanner> shopBanners = null;
        @SerializedName("shop_lastOrder")
        @Expose
        public String shopLastOrder;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("rate")
        @Expose
        public String rate;
        @SerializedName("rate_times")
        @Expose
        public String rateTimes;
        @SerializedName("isVerified")
        @Expose
        public String isVerified;
        @SerializedName("isFeatured")
        @Expose
        public String isFeatured;
        @SerializedName("facebook")
        @Expose
        public String facebook;
        @SerializedName("twitter")
        @Expose
        public String twitter;
        @SerializedName("instagram")
        @Expose
        public String instagram;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("live_chat")
        @Expose
        public String liveChat;
        @SerializedName("website")
        @Expose
        public String website;
        @SerializedName("location_hotline")
        @Expose
        public String locationHotline;
        @SerializedName("name_category")
        @Expose
        public String nameCategory;
        @SerializedName("number_product")
        @Expose
        public String numberProduct;
        @SerializedName("openHour")
        @Expose
        public List<OpenHour> openHour = null;
        @SerializedName("isFavourite")
        @Expose
        public Integer isFavourite;
        @SerializedName("is_open")
        @Expose
        public Integer isOpen;
        @SerializedName("shopOwnerId")
        @Expose
        public String shopOwnerId;
        @SerializedName("shopOwnerImage")
        @Expose
        public String shopOwnerImage;
        @SerializedName("shopOwnerName")
        @Expose
        public String shopOwnerName;
        @SerializedName("countShops")
        @Expose
        public Integer countShops;
        @SerializedName("followedShopStatus")
        @Expose
        public Integer followedShopStatus;
        @SerializedName("similar_products")
        @Expose
        public List<SimilarProduct> similarProducts = null;



    }
}

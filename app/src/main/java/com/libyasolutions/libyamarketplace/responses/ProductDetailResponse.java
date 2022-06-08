package com.libyasolutions.libyamarketplace.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("data")
    @Expose
    public Data data;
    @SerializedName("message")
    @Expose
    public String message;

    public class ExtraOption {

        @SerializedName("optionId")
        @Expose
        public String optionId;
        @SerializedName("optionName")
        @Expose
        public String optionName;
        @SerializedName("optionItem")
        @Expose
        public List<OptionItem> optionItem = null;

    }

    public class Gallery {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("image")
        @Expose
        public String image;

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

    }
    public class OptionItem {

        @SerializedName("optionItemId")
        @Expose
        public String optionItemId;
        @SerializedName("optionItemName")
        @Expose
        public String optionItemName;
        @SerializedName("optionName")
        @Expose
        public String optionName;

    }
    public class ShopTransportFee {

        @SerializedName("shipping_fee")
        @Expose
        public Integer shippingFee;
        @SerializedName("minimum")
        @Expose
        public Integer minimum;

    }

    public class SimilarFood {

        @SerializedName("food_id")
        @Expose
        public String foodId;
        @SerializedName("food_code")
        @Expose
        public String foodCode;
        @SerializedName("food_name")
        @Expose
        public String foodName;
        @SerializedName("food_price")
        @Expose
        public String foodPrice;
        @SerializedName("food_percent_discount")
        @Expose
        public Integer foodPercentDiscount;
        @SerializedName("food_shop")
        @Expose
        public String foodShop;
        @SerializedName("food_menu")
        @Expose
        public String foodMenu;
        @SerializedName("food_thumbnail")
        @Expose
        public String foodThumbnail;
        @SerializedName("food_description")
        @Expose
        public String foodDescription;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("rate")
        @Expose
        public String rate;
        @SerializedName("rate_times")
        @Expose
        public String rateTimes;
        @SerializedName("favouriteFoodStatus")
        @Expose
        public Integer favouriteFoodStatus;

    }

    public class Data {

        @SerializedName("food_id")
        @Expose
        public String foodId;
        @SerializedName("food_code")
        @Expose
        public String foodCode;
        @SerializedName("food_name")
        @Expose
        public String foodName;
        @SerializedName("food_price")
        @Expose
        public String foodPrice;
        @SerializedName("food_percent_discount")
        @Expose
        public Integer foodPercentDiscount;
        @SerializedName("food_shop")
        @Expose
        public String foodShop;
        @SerializedName("food_menu")
        @Expose
        public String foodMenu;
        @SerializedName("food_thumbnail")
        @Expose
        public String foodThumbnail;
        @SerializedName("food_description")
        @Expose
        public String foodDescription;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("rate")
        @Expose
        public String rate;
        @SerializedName("rate_times")
        @Expose
        public String rateTimes;
        @SerializedName("name_category")
        @Expose
        public String nameCategory;
        @SerializedName("gallery")
        @Expose
        public List<Gallery> gallery = null;
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
        @SerializedName("shop_lastOrder")
        @Expose
        public String shopLastOrder;
        @SerializedName("shop_status")
        @Expose
        public String shopStatus;
        @SerializedName("shop_rate")
        @Expose
        public String shopRate;
        @SerializedName("shop_rate_times")
        @Expose
        public String shopRateTimes;
        @SerializedName("shop_isVerified")
        @Expose
        public String shopIsVerified;
        @SerializedName("shop_isFeatured")
        @Expose
        public String shopIsFeatured;
        @SerializedName("shop_facebook")
        @Expose
        public String shopFacebook;
        @SerializedName("shop_twitter")
        @Expose
        public String shopTwitter;
        @SerializedName("shop_email")
        @Expose
        public String shopEmail;
        @SerializedName("shop_live_chat")
        @Expose
        public String shopLiveChat;
        @SerializedName("shop_instagram")
        @Expose
        public String shopInstagram;
        @SerializedName("openHour")
        @Expose
        public List<OpenHour> openHour = null;
        @SerializedName("isFavourite")
        @Expose
        public Integer isFavourite;
        @SerializedName("shop_transport_fee")
        @Expose
        public ShopTransportFee shopTransportFee;
        @SerializedName("shop_name_category")
        @Expose
        public String shopNameCategory;
        @SerializedName("shop_vat")
        @Expose
        public Integer shopVat;
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
        @SerializedName("extraOption")
        @Expose
        public List<ExtraOption> extraOption = null;
        @SerializedName("favouriteFoodStatus")
        @Expose
        public Integer favouriteFoodStatus;
        @SerializedName("similar_food")
        @Expose
        public List<SimilarFood> similarFood = null;

    }
}

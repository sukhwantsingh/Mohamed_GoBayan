package com.libyasolutions.libyamarketplace.config;

public final class WebServiceConfig {

    public static final String HOST = "http://uat.hicominfo.com:8888/";
    public static final String WEB_PATH = "hakim-multiplerestaurants/";
    public static final String FOLDER = "backend/api/";
//	public static final String FULL_PATH = HOST + WEB_PATH + FOLDER;

    // public static final String FILES_API = "https://hicominfo.com/libya-marketplace/backend/index.php/api/";
//    public static final String FULL_PATH = "https://hicominfo.com/libya-marketplace/backend/index.php/api/";

   // public static final String FULL_PATH = "http://uat.hicominfo.com:8888/libya-marketplace-phase2/backend/api/";
   // public static final String FILES_API = "http://uat.hicominfo.com:8888/libya-marketplace-phase2/backend/api/";

   // public static final String FULL_PATH = "http://libyamarketplace.com/backend/api/";
  //  public static final String FILES_API = "http://libyamarketplace.com/backend/api/";

    public static final String FULL_PATH = "http://gobayan.com/backend/api/";
    public static final String FILES_API = "http://gobayan.com/backend/api/";

    // action
    // public static final String ACTION_GET_SETTING = "getListShop";
    public static final String ACTION_SEND_MESSAGE_IPHOEN = "sendMessage";
    public static final String ACTION_GET_ALL_SHOP = "getListShop";
    public static final String ACTION_GET_SHOP = "getListShopBySearch";
    public static final String ACTION_GET_Menu = "getListFoodBySearch";
    public static final String ACTION_GET_OFFER_OF_DAY = "getListPromotionOfDay";
    public static final String ACTION_GET_ORDER = "getOrderHistoryById";
    public static final String ACTION_GET_DETAIL_ORDER = "getOrderDetailByOrder";
    public static final String ACTION_GET_OFFER_BY_SHOP = "getListPromotionByShop";
    public static final String ACTION_GET_OFFER_BY_ID = "getPromotionById";
    public static final String ACTION_GET_ALL_CITY = "getListCity";
    public static final String ACTION_GET_ALL_CATEGORIES = "getListCategory";
    public static final String ACTION_GET_CATEGORY_BY_ID = "getCategoryById";
    public static final String ACTION_GET_SHOP_BY_ID = "getShopById";
    public static final String ACTION_GET_SHOP_BY_CATEGORY_AND_CITY = "getListShopByCityAndCategory";
    public static final String ACTION_GET_SHOP_BY_CATEGORY = "getListShopByCategory";
    public static final String ACTION_GET_SHOP_BY_CITY = "getListShopByCity";
    public static final String ACTION_GET_BANNER_BY_SHOP = "getListBannerByShop";
    public static final String ACTION_GET_CATEGORY_BY_SHOP = "getListCategoryByShop";
    public static final String ACTION_GET_FOOD_BY_SHOP_AND_CATEGORY = "getListFoodByShopAndMenu";
    public static final String ACTION_GET_FOOD_BY_ID = "getFoodById";
    public static final String ACTION_GET_OPEN_HOUR_BY_SHOP = "getListOpenHourByShop";
    public static final String ACTION_GET_LIST_FOOD_OF_DAY = "getListFoodOfDay";
    public static final String ACTION_GET_LIST_FOOD_BY_PROMOTION = "getListFoodByPromotion";
    public static final String ACTION_SEND_LIST_ORDER = "sendOrder";

    public static final String ACTION_REGISTER = "register";
    public static final String ACTION_FEEDBACK = "getFeedback";
    public static final String ACTION_GET_USER_INFOR = "getUserProfileById";
    public static final String ACTION_UPDATEINFOR = "getUpdateUserInfo";
    public static final String ACTION_UPDATEPASS = "getUpdatePass";
    public static final String ACTION_REGISTER_SHOP_OWNER = "requestShopOwner";

    public static final String ACTION_LOGIN = "login";

    public static final String ACTION_SEARCH_SHOP = "getListShopBySearch";
    public static final String ACTION_SEARCH_FOOD = "getListFoodBySearch";
    public static final String ACTION_GET_LIST_DEPARTMENT_CATEGORY = "getListDepartmentCategory";

    public static final String ACTION_GET_LIST_SHOP_BY_ACCOUNT = "getListShopByAccount";
    public static final String ACTION_GET_MY_PRODUCT= "showMyProducts";

    //	 http://fruitysolution.vn:8888/multiple-restaurant-v1/backend/api/getOrderGroup?account=1414741099
    public static final String ACTION_GET_HISTORY_ORDERS = "getOrderGroup";

    // http://fruitysolution.vn:8888/multiple-restaurant-v1/backend/api/getOrderGroupDetail?group_code=1432528957-1414741099
    public static final String ACTION_GET_ORDER_DETAILS = "getOrderGroupDetail";
    public static final String ACTION_GET_COMMENTS = "getListComment";
    public static final String ACTION_ADD_FOOD_REVIEW = "commentFood";
    public static final String ACTION_GET_DEFAULT_LOCATION = "getDefaultLocation";
    public static final String ACTION_UPDATE_USER_PREFERENCES = "updatePreferences";
    public static final String ACTION_GET_UPDATE_FAVOURITE = "updateFavorites";
    public static final String ACTION_SHOW_FAVOURITE = "showFavorites";
    public static final String ACTION_FORGOT_PASSWORD = "forgotPassword";
    public static final String ACTION_GET_LIST_GMT = "gmtList";

    public static final String ACTION_ADD_NEW_SHOP = "shop";
    public static final String ACTION_ADD_NEW_PRODUCT = "product";
    public static final String ACTION_GET_ORDER_BY_SHOP = "showOrderByShop";
    public static final String ACTION_GET_ORDER_STATUS = "getOrderStatusToUpdate";
    public static final String ACTION_UPDATE_ORDER_STATUS = "updateStatusOrderByShop";
    public static final String ACTION_GET_ALL_CATEGORY_BY_SHOP = "getAllCategoryByShop";

    public static final String ACTION_GET_ALL_POSTS = "getListPost";
    public static final String ACTION_ADD_UPDATE_POST = "shopPost";
    public static final String ACTION_DELETE_POST = "deletePost";


    // =============================Param=======================================

    // ==============================KEY========================================

    // key
    public static final String KEY_DATA = "data";
    public static final String KEY_STATUS = "status";
    public static final String KEY_STATUS_SUCCESS = "success";
    public static final String KEY_STATUS_ERROR = "error";
    public static final String KEY_MESSAGE = "message";

    // account
    public static final String KEY_ACCOUNT_ID = "id";
    public static final String KEY_ACCOUNT_USER_NAME = "user_name";
    public static final String KEY_ACCOUNT_FULL_NAME = "full_name";
    public static final String KEY_ACCOUNT_PASSWORD = "password";
    public static final String KEY_ACCOUNT_EMAIL = "email";
    public static final String KEY_ACCOUNT_PHONE = "phone";
    public static final String KEY_ACCOUNT_ADDRESS = "address";
    public static final String KEY_ACCOUNT_CITY = "city";
    public static final String KEY_ACCOUNT_ZIP_CODE = "zip_code";
    public static final String KEY_ACCOUNT_ROLE = "role";
    public static final String KEY_ACCOUNT_REDIRECT_LINK = "redirect";
    public static final String KEY_ACCOUNT_PREFERENCES = "preferences";
    public static final String KEY_ACCOUNT_NUMBER_FAVOURITE_SHOPS = "favorite_shops_count";
    public static final String KEY_ACCOUNT_NUMBER_FAVOURITE_PRODUCTS = "favorite_products_count";
    public static final String KEY_ACCOUNT_NUMBER_ORDERS = "count_order";
    // offers
    public static final String KEY_OFFER_ID = "promotion_id";
    public static final String KEY_SHOP_ID = "shop_id";
    public static final String KEY_OFFER_DESCRIPTION = "promotion_description";
    public static final String KEY_OFFER_IMAGE = "promotion_thumbnail";
    public static final String KEY_OFFER_END_DATE = "promotion_end_date";
    public static final String KEY_OFFER_END_TIME = "promotion_end_time";

    // shop
    public static final String KEY_SHOP_NAME = "shop_name";
    public static final String KEY_SHOP_ADDRESS = "shop_address";
    public static final String KEY_SHOP_CITY = "shop_city";
    public static final String KEY_SHOP_IMAGE = "shop_thumbnail";
    public static final String KEY_SHOP_PHONE = "shop_tel";
    public static final String KEY_SHOP_PHONE_ALTERNATE = "shop_tel2";
    public static final String KEY_SHOP_DESCRIPTION = "shop_description";
    public static final String KEY_SHOP_LATITUDE = "shop_latitude";
    public static final String KEY_SHOP_LONGTITUDE = "shop_longitude";
    public static final String KEY_SHOP_OPEN_HOUR = "shop_openHourInDay";
    public static final String KEY_SHOP_BANNERS = "shop_banners";
    public static final String KEY_SHOP_ALL_OPEN_HOUR = "shop_Time";
    public static final String KEY_SHOP_OPEN_HOUR_DATE_ID = "date_id";
    public static final String KEY_SHOP_OPEN_HOUR_DATE_NAME = "date_name";
    public static final String KEY_SHOP_OPEN_HOUR_OPEN_AM = "open_AM";
    public static final String KEY_SHOP_OPEN_HOUR_CLOSE_AM = "close_AM";
    public static final String KEY_SHOP_OPEN_HOUR_OPEN_PM = "open_PM";
    public static final String KEY_SHOP_OPEN_HOUR_CLOSE_PM = "close_PM";
    public static final String KEY_SHOP_RATE = "rate";
    public static final String KEY_SHOP_RATE_TIMES = "rate_times";

    // categories
    public static final String KEY_CATEGORY_ID = "menu_id";
    public static final String KEY_CATEGORY_NAME = "menu_name";
    public static final String KEY_CATEGORY_DESCRIPTION = "menu_description";
    public static final String KEY_CATEGORY_IMAGE = "menu_thumbnail";

    // city
    public static final String KEY_CITY_ID = "city_id";
    public static final String KEY_CITY_NAME = "city_name";
    public static final String KEY_CITY_POST_CODE = "city_post_code";

    // banner
    public static final String KEY_BANNER_ID = "banner_id";
    public static final String KEY_BANNER_NAME = "banner_name";
    public static final String KEY_BANNER_IMAGE = "banner_image";

    // food
    public static final String KEY_FOOD_ID = "food_id";
    public static final String KEY_FOOD_CODE = "food_code";
    public static final String KEY_REMAINING_QUANTITY = "remaining_quantity";
    public static final String KEY_FOOD_NAME = "food_name";
    public static final String KEY_FOOD_PRICE = "food_price";
    public static final String KEY_FOOD_PERCENT_DISCOUNT = "food_percent_discount";
    public static final String KEY_FOOD_SHOP = "food_shop";
    public static final String KEY_FOOD_MENU = "food_menu";
    public static final String KEY_FOOD_DESCRIPTION = "food_description";
    public static final String KEY_FOOD_IMAGE = "food_thumbnail";
    public static final String KEY_FOOD_RATE = "rate";
    public static final String KEY_FOOD_RATE_COUNT = "rate_times";

    // order

    // $data='{"account_id":"1396947160",
    // "address" : "",
    // "item":[
    // {"shop_id":29,"food_id":58,"number":2,"price":4},
    // {"shop_id":26,"food_id":51,"number":2,"price":4},
    // {"shop_id":26,"food_id":54,"number":3,"price":4}
    // ]}';
    public static final String KEY_ORDER_ACCOUT_ID = "account_id";
    public static final String KEY_ORDER_ADDRESS = "address";
    public static final String KEY_ORDER_ITEM = "item";
    public static final String KEY_ORDER_SHOP = "shop_id";
    public static final String KEY_ORDER_FOOD = "food_id";
    public static final String KEY_ORDER_NUMBER_FOOD = "number";
    public static final String KEY_ORDER_PRICE_FOOD = "price";
    // setting
    public static final String KEY_VAT = "VAT";
    public static final String KEY_SHIP = "transportFee";
    public static final String ACTION_GET_SETTING = "getVatAndShip";
    public static final String KEY_WAIT_APPROVE_SHOP_OWNER = "wait_approve_shop_owner";
    public static final String ACTION_LOGOUT = "logout";
    public static final String ACTION_DELETE_PRODUCT = "deleteProduct";
    public static final String ACTION_UPDATE_IS_READ = "updateIsReadOrder";
    public static final String ACTION_NOTIFICATION_COUNT = "notificationCount";
}

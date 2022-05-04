package com.libyasolutions.libyamarketplace.modelmanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.config.WebServiceConfig;
import com.libyasolutions.libyamarketplace.network.HttpError;
import com.libyasolutions.libyamarketplace.network.HttpGet;
import com.libyasolutions.libyamarketplace.network.HttpListener;
import com.libyasolutions.libyamarketplace.network.HttpPost;
import com.libyasolutions.libyamarketplace.network.HttpRequest;
import com.libyasolutions.libyamarketplace.network.ParameterFactory;
import com.libyasolutions.libyamarketplace.object.Account;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.SettingPreferences;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.Utils;

import org.json.JSONArray;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class ModelManager {
    private static String TAG = "ModelManager";
    private static final int CONNECT_TIMEOUT = 60;

    public static void getListShop(final Context context, double longitude,
                                   double latitude, int page, boolean isProgress,
                                   final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_ALL_SHOP;

        String mlong = longitude + "";
        String mlat = latitude + "";
        String mPage = page + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createLongLatParams(context, mlong, mlat, mPage);

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListShopBySearch(final Context context,
                                           String keyword, String categoryId, String cityId, int page,
                                           String open, String distance, String sortBy, String sortType,
                                           String lat, String lon, boolean isProgress,
                                           final ModelManagerListener listener) {

        if (keyword == null || categoryId == null || cityId == null || open == null
                || distance == null || sortBy == null || sortType == null || lat == null
                || lon == null) {
            return;
        }

        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_SEARCH_SHOP;

        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("c_id", cityId);
        params.put("m_id", categoryId);
        params.put("page", page + "");
        params.put("open", open);
        params.put("distance", distance);
        params.put("sort_name", sortBy);
        params.put("sort_type", sortType);
        params.put("lat", lat);
        params.put("long", lon);
        params.put("now", Utils.getCurrentTimestamp());

        new HttpGet(context, url, params, isProgress, respones -> {
            if (respones != null) {
                listener.onSuccess(respones.toString());
            } else {
                listener.onError(null);
            }
        }, listener::onError);
    }

    public static void getListFoodBySearch(final Context context,
                                           String keyword, String categoryId, String cityId, int page,
                                           String open, String distance, String sortBy, String sortType,
                                           String lat, String lon, boolean isProgress,
                                           final ModelManagerListener listener) {

        if (keyword == null || categoryId == null || cityId == null || open == null
                || distance == null || sortBy == null || sortType == null || lat == null
                || lon == null ) {
            return;
        }

        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_SEARCH_FOOD;

        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("c_id", cityId);
        params.put("m_id", categoryId);
        params.put("page", page + "");
        params.put("open", open);
        params.put("distance", distance);
        params.put("sort_name", sortBy);
        params.put("sort_type", sortType);
        params.put("lat", lat);
        params.put("long", lon);
        params.put("now", Utils.getCurrentTimestamp());

        new HttpGet(context, url, params, isProgress, respones -> {
            if (respones != null) {
                listener.onSuccess(respones.toString());
            } else {
                listener.onError(null);
            }
        }, listener::onError);
    }


    public static void getListOrder(final Context context, String id, int page,

                                    boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_HISTORY_ORDERS;
        Map<String, String> params = new HashMap<>();
        params.put("account", id);
        params.put("page", page + "");
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListOrder2(final Context context, String accountId, int page,
                                        String sortType, String sortBy, String keyword, String status,
                                        boolean isProgress,
                                        final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_HISTORY_ORDERS;

        Map<String, String> params = new HashMap<>();
        params.put("account", accountId);
        params.put("page", page + "");
        params.put("sortBy", sortBy);
        params.put("sortType", sortType);
        params.put("keyword", keyword);
        params.put("status", status);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListDetailOrder(final Context context, String id,
                                          boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_ORDER_DETAILS;
        Map<String, String> params = new HashMap<>();
        params.put("group_code", id);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListCity(final Context context, boolean isProgress,
                                   final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_ALL_CITY;
        Map<String, String> params = new HashMap<>();
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListCategory(final Context context,
                                       boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_ALL_CATEGORIES;
        Map<String, String> params = new HashMap<>();
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getShopById(final Context context, int shopId,
                                   boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_SHOP_BY_ID;
        String shop = shopId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createShopIdParams(context, shop, 1);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getOpenHourByShop(final Context context, int shopId, int page,
                                         boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_OPEN_HOUR_BY_SHOP;
        String shop = shopId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createShopIdParams(context, shop, 1);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }


    public static void getListCategoryByShop(final Context context, int shopId, int page,
                                             boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_CATEGORY_BY_SHOP;
        String shop = shopId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createShopIdParams(context, shop, page);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getAllCategoryByShop(final Context context, int shopId, boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_CATEGORY_BY_SHOP;
        Map<String, String> params = new HashMap<>();
        params.put("s_id", shopId + "");
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getOfferById(final Context context, int offerId,
                                    boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_OFFER_BY_ID;
        String offer = offerId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createOfferIdParams(context, offer);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListFoodByShopAndCategory(final Context context, int page,
                                                    int shopId, String categoryId, String sortBy, String keyword, boolean isProgress,
                                                    final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_FOOD_BY_SHOP_AND_CATEGORY;
        String shop = shopId + "";
        String category = categoryId + "";

        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createShopIdAndCategoryIdParams(context, page, shop, category, sortBy, keyword);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListFoodOfDay(final Context context,
                                        double longitude, double latitude, int page, boolean isProgress,
                                        final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_LIST_FOOD_OF_DAY;
        String mlong = longitude + "";
        String mlat = latitude + "";
        String mPage = page + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createLongLatParams(context, mlong, mlat, mPage);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListFoodByPromotion(final Context context, int offer,
                                              boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_LIST_FOOD_BY_PROMOTION;
        String mpromotion = offer + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createOfferIdParams(context, mpromotion);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getFoodById(final Context context, String foodId,
                                   boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_FOOD_BY_ID;
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createFoodIdParams(context, foodId);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getUserInforById(final Context context, String id,
                                        boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_USER_INFOR;
        Map<String, String> params = ParameterFactory
                .createUserIdParams(context, id);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void login(final Context context, String username,
                             String pass, String ime, String gcm_id, String typeDevice ,
                             boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_LOGIN;
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createLoginParams(context, username, pass, ime, gcm_id, typeDevice);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void register(final Context context, String data,
                                String ime, String gcm_id, String typeDevice,
                                boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_REGISTER;
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createDataRegisterParams(context, data, ime, gcm_id, typeDevice);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void logout(String userId, String token, Context context, boolean isProgress,
                              final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_LOGOUT;

        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("deviceToken", token);

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void putFeedBack(final Context context, String id,
                                   String title, String des, String type, boolean isProgress,
                                   final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_FEEDBACK;
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .putFeedBackParams(context, id, title, des, type);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void updateProfile(final Context context, Account acc,
                                     boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_UPDATEINFOR;
        Map<String, String> params = ParameterFactory
                .updateInforUserParams(acc.getId(), acc.getPassword(), acc.getEmail(), acc.getFull_name(), acc.getPhone(),
                        acc.getAddress(), acc.getCity(), acc.getZipCode());
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void registerShopOwner(final Context context, String id,
                                           boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_REGISTER_SHOP_OWNER;
        Map<String, String> params = new HashMap<>();
        params.put("user_id", id);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void registerShopOwner2(final Context context, String id,
                                         String shopName, String shopCategory,
                                         String shopCity, String userName,
                                         String userPhone,
                                         boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_REGISTER_SHOP_OWNER;
        Map<String, String> params = new HashMap<>();
        params.put("userId", id);
        params.put("shopName", shopName);
        params.put("categoryDescription", shopCategory);
        params.put("cityId", shopCity);
        params.put("shopOwnerName", userName);
        params.put("phone", userPhone);
        new HttpPost(context, url, params, HttpRequest.REQUEST_STRING_PARAMS, true, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }


    public static void sendListOrder(final Context context, String data,
                                     int paymentMethod, boolean isProgress,
                                     final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH + WebServiceConfig.ACTION_SEND_LIST_ORDER;
        Map<String, String> params = (Map<String, String>) ParameterFactory.createDataOrderParams(context, data, paymentMethod);

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getOrderHistory(final Context context, String accountId,
                                       boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_HISTORY_ORDERS;
        Map<String, String> params = new HashMap<>();
        params.put("account", accountId);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getOrderDetails(final Context context,
                                       String orderGroupId, boolean isProgress,
                                       final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_ORDER_DETAILS;
        Map<String, String> params = new HashMap<>();
        params.put("group_code", orderGroupId);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }


    public static void getFoodsComments(final Context context, String id,
                                        int page, boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_COMMENTS;

        Map<String, String> params = new HashMap<>();
        params.put("objectType", "food");
        params.put("objectId", id);
        params.put("page", page + "");
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getShopsComments(final Context context, String id,
                                        int page, boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_COMMENTS;

        Map<String, String> params = new HashMap<>();
        params.put("objectType", "shop");
        params.put("objectId", id);
        params.put("page", page + "");
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void addFoodReview(final Context context, String shopId,
                                     String foodId, String rate, String userId, String content, boolean isProgress,
                                     final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_ADD_FOOD_REVIEW;

        Map<String, String> params = new HashMap<>();
        params.put("s_id", shopId);
        params.put("food_id", foodId);
        params.put("rate", rate);
        params.put("account_id", userId);
        params.put("title", "dummy");
        params.put("content", content);
//        new HttpGet(context, url, params, isProgress, new HttpListener() {
//            @Override
//            public void onHttpRespones(Object respones) {
//                if (respones != null) {
//                    listener.onSuccess(respones.toString());
//                } else {
//                    listener.onError(null);
//                }
//            }
//        }, new HttpError() {
//            @Override
//            public void onHttpError(VolleyError volleyError) {
//                listener.onError(volleyError);
//            }
//        });

        new HttpPost(context, url, params, HttpRequest.REQUEST_STRING_PARAMS, true, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getDefaultLocation(final Context context, boolean isProgress,
                                          final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_DEFAULT_LOCATION;

        Map<String, String> params = new HashMap<>();

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getFavourite(final Context context, String userID, boolean isProgress,
                                    final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_SHOW_FAVOURITE;

        Map<String, String> params = new HashMap<>();
        params.put("user_id", userID);
        params.put("now", Utils.getCurrentTimestamp());

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void updatePreferences(final Context context, SettingPreferences preferences, boolean isProgress,
                                         final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_UPDATE_USER_PREFERENCES;

        Map<String, String> params = new HashMap<>();
        params.put("user_id", preferences.getUserId());
        params.put("preferences", preferences.getJsonString());

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void updateFavouriteShop(final Context context, String userID, String shopID, boolean isFavourite, boolean isProgress,
                                           final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_UPDATE_FAVOURITE;

        Map<String, String> params = new HashMap<>();
        params.put("user_id", userID);
        params.put("object_id", shopID);
        params.put("type", "shop");
        params.put("action", isFavourite ? "add" : "remove");

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void updateFavouriteProduct(final Context context, String userID, String productID, boolean isFavourite, boolean isProgress,
                                              final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_UPDATE_FAVOURITE;

        Map<String, String> params = new HashMap<>();
        params.put("user_id", userID);
        params.put("object_id", productID);
        params.put("type", "product");
        params.put("action", isFavourite ? "add" : "remove");

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void forgotPassword(final Context context, String email, boolean isProgress,
                                      final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_FORGOT_PASSWORD;

        Map<String, String> params = new HashMap<>();
        params.put("email", email);


        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void sendMessageIphone(Context context, String sender_id, String receiver_id, String message, String buyer_id, String count_shop,
                                         String sender_name, String shop_id, String shop_name, boolean isProgress, final ModelManagerListener modelManagerListener) {
        final String url = WebServiceConfig.FULL_PATH + WebServiceConfig.ACTION_SEND_MESSAGE_IPHOEN;

        Map<String, String> params = new HashMap<>();
        params.put("sender_id", sender_id);
        params.put("receive_id", receiver_id);
        params.put("message", message);
        params.put("buyer_id", buyer_id);
        params.put("count_shop", count_shop);
        params.put("sender_name", sender_name);
        params.put("shop_id", shop_id);
        params.put("shop_name", shop_name);


        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    modelManagerListener.onSuccess(respones.toString());
                } else {
                    modelManagerListener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                modelManagerListener.onError(volleyError);
            }
        });
    }

    public static void getListGMT(final Context context, boolean isProgress,
                                  final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_LIST_GMT;

        Map<String, String> params = new HashMap<>();

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListShopByAccount(final Context context, String accountId, int page, boolean isProgress,
                                            final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_LIST_SHOP_BY_ACCOUNT;

        Map<String, String> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("page", page + "");
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getMyProducts(final Context context, String shopId, String sortBy, String keyword, int page, boolean isProgress,
                                     final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_MY_PRODUCT;

        Map<String, String> params = new HashMap<>();
        params.put("s_id", shopId);
        params.put("page", page + "");
        params.put("sort_by", sortBy);
        params.put("sort_type", "date");
        params.put("keyword", keyword);

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void addNewShop(Context context, String accountId, Shop shop, String status, String deleteGalleryId, File mainImage, File thumbnailImage, ArrayList<File> listFile, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_ADD_NEW_SHOP;

        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image");
        MultipartBody.Builder builderNew = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("shop_id", shop.getShopId() + "")
                .addFormDataPart("account_id", accountId)
                .addFormDataPart("category_id", shop.getCategoryId())
                .addFormDataPart("city_id", shop.getCityId() + "")
                .addFormDataPart("name", shop.getShopName())
                .addFormDataPart("phone", shop.getPhone())
                .addFormDataPart("address", shop.getAddress())
                .addFormDataPart("gmt", shop.getGmt())
                .addFormDataPart("hotline", shop.getPhone())
                .addFormDataPart("latitude", shop.getLatitude() + "")
                .addFormDataPart("longitude", shop.getLongitude() + "")
                .addFormDataPart("description", shop.getDescription())
                .addFormDataPart("facebook", shop.getFacebook())
                .addFormDataPart("twitter", shop.getTwitter())
                .addFormDataPart("instagram", shop.getShopInstagram())
                .addFormDataPart("website", shop.getWebsite())
                .addFormDataPart("email", "")
                .addFormDataPart("livechat", "")
                .addFormDataPart("openHours", shop.getShopTime())
                .addFormDataPart("status", status)
                .addFormDataPart("deleted_gallery", deleteGalleryId);


        if (mainImage != null) {
            builderNew.addFormDataPart("image", mainImage.getName(), RequestBody.create(MEDIA_TYPE_PNG, mainImage));
        }
        if (thumbnailImage != null) {
            builderNew.addFormDataPart("thumbnail", thumbnailImage.getName(), RequestBody.create(MEDIA_TYPE_PNG, thumbnailImage));
        }
        if (listFile.size() != 0) {
            for (int i = 0; i < listFile.size(); i++) {
                builderNew.addFormDataPart("gallery[" + i + "]", listFile.get(i).getName(), RequestBody.create(MEDIA_TYPE_PNG, listFile.get(i)));
            }
        }
        RequestBody requestBody = builderNew.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("url", message);
            }
        });

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                listener.onSuccess(response.body().string());
            } else
                listener.onError(null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(null);
        }

    }

    public static void addNewProduct(Context context, String accountId, Menu product, String extraOption, String deleteGalleryId, File mainImage, File thumbnailImage, ArrayList<File> listFile, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_ADD_NEW_PRODUCT;
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image");

        MultipartBody.Builder builderNew = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("shop_id", product.getShopId() + "")
                .addFormDataPart("product_id", product.getId() == 0 ? "" : product.getId() + "")
                .addFormDataPart("category_id", product.getCategoryId() + "")
                .addFormDataPart("account_id", accountId)
                .addFormDataPart("name", product.getName())
                .addFormDataPart("code", product.getCode())
                .addFormDataPart("price", product.getPrice() + "")
                .addFormDataPart("status", product.getStatus())
                .addFormDataPart("description", product.getDescription())
                .addFormDataPart("available", product.getStatus())
                .addFormDataPart("extra_options", extraOption)
                .addFormDataPart("deleted_gallery", deleteGalleryId);


        if (mainImage != null) {
            builderNew.addFormDataPart("image", mainImage.getName(), RequestBody.create(MEDIA_TYPE_PNG, mainImage));
        }
        if (thumbnailImage != null) {
            builderNew.addFormDataPart("thumbnail", thumbnailImage.getName(), RequestBody.create(MEDIA_TYPE_PNG, thumbnailImage));
        }
        if (listFile.size() != 0) {
            for (int i = 0; i < listFile.size(); i++) {
                builderNew.addFormDataPart("gallery[" + i + "]", listFile.get(i).getName(), RequestBody.create(MEDIA_TYPE_PNG, listFile.get(i)));
            }
        }
        RequestBody requestBody = builderNew.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("url", message);
            }
        });

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                listener.onSuccess(response.body().string());
            } else
                listener.onError(null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(null);
        }

    }

    public static void deleteProduct(final Context context, String userId, String productId, boolean isProgress,
                                       final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_DELETE_PRODUCT;

        Map<String, String> params = new HashMap<>();
        params.put("product_id", productId);
        params.put("user_id", userId);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void updateIsReadOrder(final Context context, String userId, String orderId, boolean isProgress,
                                     final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_UPDATE_IS_READ;

        Map<String, String> params = new HashMap<>();
        params.put("order_id", orderId);
        params.put("user_id", userId);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void notificationCount(final Context context, String userId , boolean isProgress,
                                         final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_NOTIFICATION_COUNT;

        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void showOrderByShop(final Context context, String accountId, int page, boolean isProgress,
                                       final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_ORDER_BY_SHOP;

        Map<String, String> params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("page", page + "");
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void showOrderByShop2(final Context context, String accountId, int page,
                                        String sortType, String sortBy, String keyword, String status,
                                        boolean isProgress,
                                       final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_ORDER_BY_SHOP;

        Map<String, String> params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("page", page + "");
        params.put("sortBy", sortBy);
        params.put("sortType", sortType);
        params.put("keyword", keyword);
        params.put("status", status);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getOrderStatus(final Context context, String userId, String shopId, String orderId, boolean isProgress,
                                      final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_ORDER_STATUS;

        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("shop_id", shopId);
        params.put("order_id", orderId);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void updateOrderStatusByShop(final Context context, String userId, String shopId, String orderId, String status, boolean isProgress,
                                               final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_UPDATE_ORDER_STATUS;

        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("shop_id", shopId);
        params.put("order_id", orderId);
        params.put("status", status);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListDepartmentCategory(final Context context,
                                       boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.FULL_PATH
                + WebServiceConfig.ACTION_GET_LIST_DEPARTMENT_CATEGORY;

        Map<String, String> params = new HashMap<>();
        new HttpGet(context, url, params, isProgress, respones -> {
            if (respones != null) {
                listener.onSuccess(respones.toString());
            } else {
                listener.onError(null);
            }
        }, listener::onError);
    }

}
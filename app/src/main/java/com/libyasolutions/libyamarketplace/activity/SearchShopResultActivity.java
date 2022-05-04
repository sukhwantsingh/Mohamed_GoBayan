package com.libyasolutions.libyamarketplace.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ShopAdapter;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.pulltorefresh.PullToRefreshBase;
import com.libyasolutions.libyamarketplace.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.libyasolutions.libyamarketplace.pulltorefresh.PullToRefreshListView;
import com.libyasolutions.libyamarketplace.util.GPSTracker;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class SearchShopResultActivity extends BaseActivity implements
        OnClickListener {

    private ArrayList<Shop> arrShop = new ArrayList<>();
    private PullToRefreshListView lsvShop;
    private ListView lsvActually;
    private TextView tvNameShop;
    private Activity self;
    private ShopAdapter shopAdapter;
    private ImageView btnBack;
    private GPSTracker gps;
    private int page = 1;
    private String searchKey = "";
    private String categoryId = "";
    private String cityId = "";
    private String open = "";
    private String distance = "";
    private String sortBy = "";
    private String sortType = "";
    private String lat = "";
    private String lon = "";
    private String departmentId = "";
    private boolean isMore = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        gps = new GPSTracker(self);
        setContentView(R.layout.activity_search_shop_result);
        setData();
        getParamSearch();
        initUI();
        initControl();
    }

    private void initUI() {
        lsvShop = (PullToRefreshListView) findViewById(R.id.lsvShop);
        lsvActually=lsvShop.getRefreshableView();
        btnBack = (ImageView) findViewById(R.id.btnBack);
        tvNameShop = findViewById(R.id.tvNameShop);
    }

    private void initControl() {

        lsvShop.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long arg3) {
                // TODO Auto-generated method stub
                Shop shop = arrShop.get(index - 1);
                gotoShopDetailActivity(shop.getShopId());
            }
        });

        lsvShop.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                String label = DateUtils.formatDateTime(self,
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                page = 1;
                isMore = true;
                getDataSearch(page, true);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                if (isMore) {
                    page++;
                }
                getDataSearch(page, true);
            }
        });

        btnBack.setOnClickListener(this);

    }


    private void getParamSearch() {
        // get data search ;
        Bundle b = getIntent().getExtras();
        searchKey = b.getString(GlobalValue.KEY_SEARCH);
        cityId = b.getString(GlobalValue.KEY_CITY_ID);
        categoryId = b.getString(GlobalValue.KEY_CATEGORY_ID);
        open = b.getString(GlobalValue.KEY_OPEN);
        distance = b.getString(GlobalValue.KEY_DISTANCE);
        sortBy = b.getString(GlobalValue.KEY_SORT_BY);
        sortType = b.getString(GlobalValue.KEY_SORT_TYPE);
        lat = b.getString(GlobalValue.KEY_LAT);
        lon = b.getString(GlobalValue.KEY_LONG);
    }

    private void setData() {
        getDataSearch(page, false);
    }

    private void getDataSearch(final int page, boolean isPull) {
        Log.e("RRR", "run time");
        if (page <= 1) {
            arrShop.clear();
        }

        ModelManager.getListShopBySearch(self, searchKey, categoryId, cityId,
                page, open, distance, sortBy, sortType, lat, lon, isPull,
                new ModelManagerListener() {

                    @Override
                    public void onError(VolleyError error) {
                        Log.e("RRR", error.getMessage());
                        // TODO Auto-generated method stub
                        lsvShop.onRefreshComplete();
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(Object object) {
                        Log.e("suscess", "suscess");
                        String json = (String) object;
                        List<Shop> arr = ParserUtility.getListShop(json);

                        if (arr.size() > 0) {
                            isMore = true;
                            arrShop.addAll(arr);
                            setDataShopToList(arrShop);
                        } else {
                            isMore = false;
                            Toast.makeText(self, getResources().getString(R.string.have_no_more_date),
                                    Toast.LENGTH_SHORT).show();

                        }
                        lsvShop.onRefreshComplete();
                    }
                });
    }

//    private void updateLayoutOfList(boolean isResultSearch) {
//        if (isResultSearch) {
//            lsvShop.setVisibility(View.VISIBLE);
//            lblNoData.setVisibility(View.GONE);
//        } else {
//            lsvShop.setVisibility(View.GONE);
//            lblNoData.setVisibility(View.VISIBLE);
//        }
//    }

//    private void setDataToMap(final ArrayList<Shop> arrShops) {
//        if (markerRestaurantMap == null) {
//            markerRestaurantMap = new HashMap<String, Shop>();
//        } else {
//            if (!markerRestaurantMap.isEmpty()) {
//                markerRestaurantMap.clear();
//            }
//            googleMap.clear();
//        }
//
//        Marker marker;
//        Bitmap bitmap = null;
//        for (final Shop shop : arrShops) {
//
//            LatLng item = new LatLng(shop.getLatitude(), shop.getLongitude());
//            marker = googleMap.addMarker(new MarkerOptions().position(item)
//                    .title(shop.getShopName()));
//            try {
//                // set marker icon
////                bitmap = ImageUtil.createBitmapFromUrl(shop.getImage());
////                shop.setBmImage(bitmap);
////                // resize
////                int size = ImageUtil.getSizeBaseOnDensity(self, 30);
////                bitmap = ImageUtil.getResizedBitmap(bitmap, size, size);
//                // ser Bitmap to marker
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_address_map));
//            } catch (Exception e) {
//                Log.e("HUY",e.getMessage());
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_address_map));
//            }
//
//            markerRestaurantMap.put(marker.getId(), shop);
//
//        }
//        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
//
//        googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
//
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                // TODO Auto-generated method stub
//                String title = marker.getTitle();
//
//                for (int i = 0; i < arrShops.size(); i++) {
//                    if (arrShop.get(i).getShopName().equals(title)) {
//                        gotoShopDetailActivity(arrShop.get(i).getShopId());
//                        break;
//                    }
//                }
//            }
//        });
//
//        //set map to around current location
//        //check fake location
//        if (Constant.isFakeLocation) {
//            setLocationLatLong(GlobalValue.glatlng.latitude, GlobalValue.glatlng.longitude);
//            Toast.makeText(self, R.string.message_demo_location, Toast.LENGTH_LONG).show();
//        } else {
//            setLocationLatLong(gps.getLatitude(), gps.getLongitude());
//        }
//
//    }

    private void setDataShopToList(ArrayList<Shop> arrShops) {
        shopAdapter = new ShopAdapter(self, arrShops);
        lsvShop.setAdapter(shopAdapter);
        shopAdapter.notifyDataSetChanged();
    }

//    private class CustomInfoWindowAdapter implements InfoWindowAdapter {
//
//        private View v;
//
//        public CustomInfoWindowAdapter() {
//            v = getLayoutInflater().inflate(R.layout.map_detail, null);
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            if (currentMaker != null && !currentMaker.isInfoWindowShown()) {
//                currentMaker.showInfoWindow();
//            }
//            return null;
//        }
//
//        @Override
//        public View getInfoWindow(final Marker marker) {
//            currentMaker = marker;
//            Logger.d("MapMaker iD ", marker.getId());
//            Logger.d("MapMaker", markerRestaurantMap.toString());
//
//            Shop shop = markerRestaurantMap.get(marker.getId());
//            TextView lblName = (TextView) v.findViewById(R.id.lblShopName);
//            TextView lblAddress = (TextView) v.findViewById(R.id.lblAddress);
//            TextView lblCategory = (TextView) v.findViewById(R.id.lblCategoryName);
//            lblName.setSelected(true);
//            if (shop != null) {
//                lblName.setText(shop.getShopName());
//                lblAddress.setText(shop.getAddress());
//                lblCategory.setText(shop.getCategory());
//
//            } else {
//                Logger.d("AAA", "Restaurant is null");
//            }
//
//            return v;
//        }
//    }

    public void gotoActive(Shop shop, Class<?> cls) {
        // startActivity(intent);
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("id", shop.getShopId());
        bundle.putString("name", shop.getShopName());
        returnIntent.putExtra("result", bundle);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void gotoShopDetailActivity(int shopId) {
        Bundle bundle = new Bundle();
        bundle.putInt(GlobalValue.KEY_SHOP_ID, shopId);
        gotoActivity(self, ShopDetailActivity.class, bundle);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnBack) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

}

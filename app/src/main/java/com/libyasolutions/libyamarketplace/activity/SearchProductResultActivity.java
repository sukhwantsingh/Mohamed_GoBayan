package com.libyasolutions.libyamarketplace.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.adapter.ListFoodAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.pulltorefresh.PullToRefreshBase;
import com.libyasolutions.libyamarketplace.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.libyasolutions.libyamarketplace.pulltorefresh.PullToRefreshListView;
import com.libyasolutions.libyamarketplace.util.GPSTracker;
import com.libyasolutions.libyamarketplace.util.ImageUtil;
import com.libyasolutions.libyamarketplace.util.Logger;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class SearchProductResultActivity extends BaseActivity implements OnClickListener {
    public static final String SEARCH_SCREEN = "searchActivity";
    private GoogleMap googleMap;
    private ArrayList<Shop> arrShop = new ArrayList<Shop>();
    private ArrayList<Menu> arrProducts = new ArrayList<Menu>();
    private Marker currentMaker;
    private PullToRefreshListView lsvShop;
    private ListView lsvActually;
    private TextView lblNoData;
    private Activity self;
    private ListFoodAdapter productAdapter;
    private Button btnChooseMap, btnChooseList;
    private boolean isChooseList = true;
    private LinearLayout layoutMap, layoutList;
    private ImageView btnBack;

    private GPSTracker gps = null;

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
    private boolean isMore = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_search_products_result);
        gps = new GPSTracker(this);
        getParamSearch();
        initUI();
        initControl();
        updateUI();
        initGoogleMap();
        setData();

    }

    private void initUI() {
        lsvShop = (PullToRefreshListView) findViewById(R.id.lsvProducts);
        lsvActually = lsvShop.getRefreshableView();
        lblNoData = (TextView) findViewById(R.id.lblNoData);
        btnChooseMap = (Button) findViewById(R.id.btnChooseMap);
        btnChooseList = (Button) findViewById(R.id.btnChooseList);
        layoutList = (LinearLayout) findViewById(R.id.layoutList);
        layoutMap = (LinearLayout) findViewById(R.id.layoutMap);
        btnBack = (ImageView) findViewById(R.id.btnBack);
    }

    private void initControl() {

        lsvActually.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long arg3) {
                // TODO Auto-generated method stub
                Menu food = arrProducts.get(index - 1);
                Bundle b = new Bundle();
                b.putString(GlobalValue.KEY_FOOD_ID, food.getId() + "");
                b.putString(GlobalValue.KEY_NAVIGATE_TYPE, "FAST");
                GlobalValue.KEY_LOCAL_NAME = food.getLocalName();
                b.putString(GlobalValue.KEY_FROM_SCREEN, SEARCH_SCREEN);
                gotoActivity(self, ProductDetailActivity.class, b);
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

        btnChooseList.setOnClickListener(this);
        btnChooseMap.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }

    private void updateUI() {
        //check if search distance
        if (distance.equals("0.0")) {
            layoutList.setVisibility(View.VISIBLE);
            btnChooseList.setVisibility(View.GONE);
            layoutMap.setVisibility(View.GONE);
            btnChooseMap.setVisibility(View.GONE);
        } else {
            btnChooseList.setVisibility(View.VISIBLE);
            btnChooseMap.setVisibility(View.VISIBLE);
            if (isChooseList) {
                layoutList.setVisibility(View.VISIBLE);
                btnChooseList.setBackgroundResource(R.drawable.ic_list_selected);
                layoutMap.setVisibility(View.GONE);
                btnChooseMap.setBackgroundResource(R.drawable.ic_map_unselected);
            } else {
                layoutList.setVisibility(View.GONE);
                btnChooseList.setBackgroundResource(R.drawable.ic_list_unselected);
                layoutMap.setVisibility(View.VISIBLE);
                btnChooseMap.setBackgroundResource(R.drawable.ic_map_selected);
            }
        }
    }

    private void initGoogleMap() {
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();

        } else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            fm.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;
                    googleMap.setMyLocationEnabled(true);
                }
            });
            googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
            googleMap
                    .setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            // TODO Auto-generated method stub
                            String title = marker.getTitle();
                            for (int i = 0; i < arrShop.size(); i++) {
                                if (arrShop.get(i).getShopName().equals(title)) {
                                    gotoShopDetailActivity(arrShop.get(i)
                                            .getShopId());
                                    break;
                                }
                            }
                        }
                    });

            //check fake location
            if (Constant.isFakeLocation) {
                setLocationLatLong(GlobalValue.glatlng.latitude, GlobalValue.glatlng.longitude);
            } else {
                setLocationLatLong(gps.getLatitude(), gps.getLongitude());
            }
        }
    }

    private void setLocationLatLong(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory
                .zoomTo(12));// zoom : 2-21
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
        getDataSearch(page, true);
    }

    private void getDataSearch(int page, boolean isPull) {
        if (page <= 1) {
            arrShop.clear();
            arrProducts.clear();
        }

        ModelManager.getListFoodBySearch(self, searchKey, categoryId, cityId,
                page, open, distance, sortBy, sortType, lat, lon, !isPull,
                new ModelManagerListener() {

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        lsvShop.onRefreshComplete();
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(Object object) {
                        String json = (String) object;
                        ArrayList<Menu> arr = ParserUtility.parseListFoodInSearch(json);
                        if (arr.size() > 0) {
                            isMore = true;
                            arrProducts.addAll(arr);
                            setDataToMap(getListNewShops(getListShopsInProductList(arr)));
                            setDataProductsToList(arrProducts);
                        } else {
                            isMore = false;
                            Toast.makeText(self, getResources().getString(R.string.have_no_more_date),
                                    Toast.LENGTH_SHORT).show();
                        }
                        lsvShop.onRefreshComplete();
                        //check show list
                        updateLayoutOfList(arrProducts.size() > 0);
                    }
                });
    }

    private void updateLayoutOfList(boolean isResultSearch) {
        if (isResultSearch) {
            lsvShop.setVisibility(View.VISIBLE);
            lblNoData.setVisibility(View.GONE);
        } else {
            lsvShop.setVisibility(View.GONE);
            lblNoData.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<Shop> getListShopsInProductList(ArrayList<Menu> arr) {
        ArrayList<Shop> shops = new ArrayList<>();
        Shop shop;
        boolean isExists = false;
        for (Menu menu : arr) {
            shop = menu.getShop();
            isExists = false;
            for (int j = 0; j < shops.size(); j++) {
                if (shop.getShopId() == shops.get(j).getShopId()) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                shops.add(shop);
            }
        }
        return shops;
    }

    private ArrayList<Shop> getListNewShops(ArrayList<Shop> arr) {
        ArrayList<Shop> shops = new ArrayList<>();
        boolean isExists = false;
        for (Shop shop : arr) {
            for (int j = 0; j < arrShop.size(); j++) {
                if (shop.getShopId() == arrShop.get(j).getShopId()) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                shops.add(shop);
            }
        }
        return shops;
    }

    private void setDataToMap(final ArrayList<Shop> arrShops) {
        if (arrShop.size() == 0)
            googleMap.clear();

        Marker marker;
        Bitmap bitmap = null;
        for (final Shop shop : arrShops) {

            LatLng item = new LatLng(shop.getLatitude(), shop.getLongitude());
            marker = googleMap.addMarker(new MarkerOptions().position(item)
                    .title(shop.getShopName()));
            try {
                // set marker icon
                bitmap = ImageUtil.createBitmapFromUrl(shop.getThumbnail());
                shop.setBmImage(bitmap);
                // resize
                int size = ImageUtil.getSizeBaseOnDensity(self, 30);
                bitmap = ImageUtil.getResizedBitmap(bitmap, size, size);
                // ser Bitmap to marker
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
            } catch (Exception e) {
                marker.setIcon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_address_map));
            }
        }
        //add new shops to list
        arrShop.addAll(arrShops);
        googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                // TODO Auto-generated method stub
                String title = marker.getTitle();

                for (int i = 0; i < arrShops.size(); i++) {
                    if (arrShop.get(i).getShopName().equals(title)) {
                        gotoShopDetailActivity(arrShop.get(i).getShopId());
                        break;
                    }
                }
            }
        });

        //check fake location
        if (Constant.isFakeLocation) {
            setLocationLatLong(GlobalValue.glatlng.latitude, GlobalValue.glatlng.longitude);
            Toast.makeText(self, R.string.message_demo_location, Toast.LENGTH_LONG).show();
        } else {
            setLocationLatLong(gps.getLatitude(), gps.getLongitude());
        }
    }

    private void setDataProductsToList(ArrayList<Menu> arrProducts) {
        productAdapter = new ListFoodAdapter(self, arrProducts);
        productAdapter.notifyDataSetChanged();
        lsvActually.setAdapter(productAdapter);
    }

    private class CustomInfoWindowAdapter implements InfoWindowAdapter {

        private View v;

        public CustomInfoWindowAdapter() {
            v = getLayoutInflater().inflate(R.layout.map_detail, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (currentMaker != null && !currentMaker.isInfoWindowShown()) {
                currentMaker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            currentMaker = marker;

            Shop shop = null;
            for (int i = 0; i < arrShop.size(); i++) {
                if (arrShop.get(i).getShopName().equals(currentMaker.getTitle())) {
                    shop = arrShop.get(i);
                    break;
                }
            }
            TextView lblName = (TextView) v.findViewById(R.id.lblShopName);
            TextView lblAddress = (TextView) v.findViewById(R.id.lblAddress);
            TextView lblCategory = (TextView) v.findViewById(R.id.lblCategoryName);
            lblName.setSelected(true);
            if (shop != null) {
                lblName.setText(shop.getShopName());
                lblAddress.setText(shop.getAddress());
                lblCategory.setText(shop.getCategory());

            } else {
                Logger.d("AAA", "Restaurant is null");
            }

            return v;
        }
    }

    public void gotoShopDetailActivity(int shopId) {
        Bundle bundle = new Bundle();
        bundle.putInt(GlobalValue.KEY_SHOP_ID, shopId);
        gotoActivity(self, ShopDetailActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnChooseList) {
            isChooseList = true;
            updateUI();
        } else if (v == btnChooseMap) {
            isChooseList = false;
            updateUI();
        } else if (v == btnBack) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

}

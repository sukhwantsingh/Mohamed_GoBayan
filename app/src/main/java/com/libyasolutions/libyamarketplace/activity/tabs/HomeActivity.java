package com.libyasolutions.libyamarketplace.activity.tabs;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidquery.AQuery;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.activity.ShopDetailsNew;
import com.libyasolutions.libyamarketplace.adapter.ListFoodHomeAdapter;
import com.libyasolutions.libyamarketplace.config.Constant;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Menu;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.GPSTracker;
import com.libyasolutions.libyamarketplace.util.Logger;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

@SuppressLint("NewApi")
public class HomeActivity extends BaseActivity implements LocationListener {

    private GoogleMap googleMap;
    private TextView btnShops, btnMenus;
    private TextView lblBestDeal, lblBestShop;
    private Marker currentMaker;
    private HashMap<String, Shop> markerRestaurantMap = new HashMap<String, Shop>();
    private AQuery aq;
    private ArrayList<Shop> arrShop = new ArrayList<Shop>();
    private ArrayList<Menu> arrFood;
    private RecyclerView lsvOffer;
    private Activity self;
    private ListFoodHomeAdapter foodAdapter;
    private CardView layoutOffers;
    private GPSTracker gps;
    Handler handler;
    private String jsonShops, jsonMenus;

    private boolean isShowFakeMessage = true;

    public static final String HOME_SCREEN = "homeActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_home);
        aq = new AQuery(this);
        gps = new GPSTracker(self);
        handler = new Handler();
        initUI();
        initControl();
//        initGoogleMap();

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (Constant.isFakeLocation) {
            getDefaultLocation();
        } else {
            refreshContent();
        }
    }

    private void initUI() {
        lsvOffer = findViewById(R.id.lsvOffers);
        btnShops = findViewById(R.id.btnShops);
        btnMenus = findViewById(R.id.btnMenus);
        btnMenus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, ListHomeFoodActivity.class);
//                intent.putExtra("ARRAYSHOP", jsonMenus);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        btnShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, ListHomeShopActivity.class);
//                intent.putExtra("ARRAYSHOP", jsonShops);
                startActivity(intent);
            }
        });
//        lblBestDeal = (TextView) findViewById(R.id.lblBestDealAround);
//        lblBestShop = (TextView) findViewById(R.id.lblBestShopAround);
//        layoutOffers = (CardView) findViewById(R.id.layoutOffers);
    }

    private void initControl() {
        arrFood = new ArrayList<Menu>();
        foodAdapter = new ListFoodHomeAdapter(self, arrFood);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        lsvOffer.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.HORIZONTAL, false));
        lsvOffer.setHasFixedSize(true);
        lsvOffer.setAdapter(foodAdapter);
//        lsvOffer.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
//                                    long arg3) {
//                Menu menu = arrFood.get(index);
//                Bundle b = new Bundle();
//                b.putString(GlobalValue.KEY_FOOD_ID, menu.getId() + "");
//                b.putString(GlobalValue.KEY_NAVIGATE_TYPE, "FAST");
//                b.putString(GlobalValue.KEY_FROM_SCREEN, HOME_SCREEN);
//                GlobalValue.KEY_LOCAL_NAME = menu.getLocalName();
//                gotoActivity(self, ProductDetailsNewActivity.class, b);
//            }
//        });


    }

    private void setLocationLatLong(double longitude, double latitude) {
        //Logger.e("FakeLocation","zoom to face location :"+longitude+";"+latitude);
        LatLng currentLocation = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        googleMap.animateCamera(CameraUpdateFactory
                .zoomTo(GlobalValue.ZOOM_SIZE));// zoom : 2-21
    }

    private void setData(double latitude, double longitude) {
        getOfferData(latitude, longitude);
//        getListShop(latitude, longitude);

    }

    private void getDefaultLocation() {
        ModelManager.getDefaultLocation(this, false, new ModelManagerListener() {

            @Override
            public void onError(VolleyError error) {
                refreshContent();
                Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Object object) {
                String json = (String) object;
                GlobalValue.glatlng = ParserUtility.parseDefaultLocation(json);
                refreshContent();
            }
        });
    }

    private void getListShop(double latitude, double longitude) {
        ModelManager.getListShop(this, longitude, latitude, 1,
                false, new ModelManagerListener() {

                    @Override
                    public void onError(VolleyError error) {
                        arrShop.clear();
//                        lblBestShop.setText(arrShop.size() + " "
//                                + getString(R.string.home_shop_title));
//                        updateMarkerGoogle(arrShop);
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(Object object) {
                        String json = (String) object;
                        jsonShops = object.toString();

                        arrShop.clear();
                        arrShop.addAll(ParserUtility.getListShop(json));
//                        lblBestShop.setText(arrShop.size() + " "
//                                + getString(R.string.home_shop_title));
//                        updateMarkerGoogle(arrShop);

                    }
                });
    }

//    private void updateMarkerGoogle(ArrayList<Shop> arr) {
//        googleMap.clear();
//        markerRestaurantMap.clear();
//        Bitmap bitmap = null;
//        for (final Shop shop : arr) {
//            LatLng item = new LatLng(shop.getLatitude(), shop.getLongitude());
//            Marker marker = googleMap.addMarker(new MarkerOptions().position(
//                    item).title(shop.getShopName()));
//            try {
//                // set marker icon
//                bitmap = ImageUtil.createBitmapFromUrl(shop.getImage());
//                shop.setBmImage(bitmap);
//                // resize
//                int size = ImageUtil.getSizeBaseOnDensity(self, 35);
//                bitmap = ImageUtil.getResizedBitmap(bitmap, size, size);
//                // ser Bitmap to marker
//                marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
//            } catch (Exception e) {
//                marker.setIcon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.ic_address_map));
//            }
//            markerRestaurantMap.put(marker.getId(), shop);
//
//        }
//
//        //for fake location
//        if (Constant.isFakeLocation)
//            setLocationLatLong(GlobalValue.glatlng.longitude, GlobalValue.glatlng.latitude);
//
//    }

    private void getOfferData(double latitude, double longitude) {
        ModelManager.getListFoodOfDay(this, longitude,
                latitude, 1, false, new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        String json = (String) object;
                        jsonMenus = object.toString();
                        arrFood.clear();
                        arrFood.addAll(ParserUtility.parseListFood(json));
//                        if (arrFood.size() > 0) {
//                            layoutOffers.setVisibility(View.VISIBLE);
//                        } else {
//                            layoutOffers.setVisibility(View.GONE);
//                        }
//                        lblBestDeal.setText(arrFood.size() + " "
//                                + getString(R.string.home_menu_title));
                        foodAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        arrFood.clear();
                        foodAdapter.notifyDataSetChanged();
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
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
            Logger.d("MapMaker iD ", marker.getId());
            Logger.d("MapMaker", markerRestaurantMap.toString());

            Shop shop = markerRestaurantMap.get(marker.getId());
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

    public void refreshContent() {
        if (Constant.isFakeLocation) {
            setData(GlobalValue.glatlng.latitude, GlobalValue.glatlng.longitude);
            Toast.makeText(self, R.string.message_demo_location, Toast.LENGTH_LONG).show();
        } else {
            refreshMyLocation();
            setData(gps.getLatitude(), gps.getLongitude());
        }
    }


    public void gotoShopDetailActivity(int shopId) {
        Bundle bundle = new Bundle();
        bundle.putInt(GlobalValue.KEY_SHOP_ID, shopId);
        Intent intent = new Intent(HomeActivity.this, ShopDetailsNew.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        ((MainTabActivity) getParent()).gotoActivity(ShopDetailActivity.class,
//                bundle);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        this.getParent().onBackPressed();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        handler.removeCallbacks(runGoogleUpdateLocation);
    }

    private void refreshMyLocation() {
        Location location = null;
        if (googleMap != null) {
            location = googleMap.getMyLocation();

            if (location == null) {
                if (gps.canGetLocation()) {
                    location = gps.getLocation();
                }
                handler.postDelayed(runGoogleUpdateLocation, 30 * 1000);
            }
        }
        if (location != null)
            setLocationLatLong(location.getLongitude(), location.getLatitude());

    }

    Runnable runGoogleUpdateLocation = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (NetworkUtil.checkNetworkAvailable(self))
                refreshMyLocation();
        }
    };

    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

    }

}

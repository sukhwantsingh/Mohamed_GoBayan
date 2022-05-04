package com.libyasolutions.libyamarketplace.activity;

import java.util.HashMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.libyasolutions.libyamarketplace.BaseActivity;
import com.libyasolutions.libyamarketplace.R;
import com.libyasolutions.libyamarketplace.config.GlobalValue;
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager;
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener;
import com.libyasolutions.libyamarketplace.network.ParserUtility;
import com.libyasolutions.libyamarketplace.object.Shop;
import com.libyasolutions.libyamarketplace.util.NetworkUtil;

@SuppressLint("NewApi")
public class MapDetailActivity extends BaseActivity implements OnClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<String, Shop> markerRestaurantMap;
    private int shopId = -1;
    private Shop shop;
    private ImageView btnBack;
    private TextView lblShopName, lblAddress;
    public static BaseActivity self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_map_of_shop);
        initUI();
        initControl();
        initMap();
        setData();

    }

    private void initUI() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        lblShopName = (TextView) findViewById(R.id.lblShopName);
        lblAddress = (TextView) findViewById(R.id.lblAddress);
    }

    private void initControl() {
        btnBack.setOnClickListener(this);
        lblShopName.setOnClickListener(this);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setData() {
        // arrShop = new ArrayList<Shop>();
//        showToastMessage(getString(R.string.message_demo_location));
        Bundle b = getIntent().getExtras();
        if (b != null) {
            shopId = b.getInt(GlobalValue.KEY_SHOP_ID);

        }

        if (shopId != -1) {
            if (!NetworkUtil.checkNetworkAvailable(this)) {
                Toast.makeText(this, R.string.message_network_is_unavailable, Toast.LENGTH_LONG).show();
            } else
                ModelManager.getShopById(self, shopId, true,
                        new ModelManagerListener() {

                            @Override
                            public void onSuccess(Object object) {
                                // TODO Auto-generated method stub
                                String json = (String) object;
                                shop = ParserUtility.parseShop(json);
                                if (shop != null) {
                                    lblShopName.setText(shop.getShopName());
                                    lblAddress.setText(shop.getAddress());
                                    setDataToMap();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                // TODO Auto-generated method stub
                                Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                            }
                        });
        }

    }

    private void setDataToMap() {
        if (markerRestaurantMap == null) {
            markerRestaurantMap = new HashMap<String, Shop>();
        } else {
            if (!markerRestaurantMap.isEmpty()) {
                markerRestaurantMap.clear();
            }
            mMap.clear();
        }

        Marker marker;
        if (shop != null) {
            LatLng item = new LatLng(shop.getLatitude(), shop.getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(item)
                    .title(shop.getShopName()));
            try {
                marker.setIcon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_address_map));
            } catch (Exception e) {
                marker.setIcon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_address_map));
            }
            markerRestaurantMap.put(marker.getId(), shop);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(item));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnBack) {
            onBackPressed();
        } else if (v == lblShopName) {
            gotoShopDetail(shopId);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // Đã tải thành công thì tắt Dialog Progress đi
                if (ActivityCompat.checkSelfPermission(MapDetailActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MapDetailActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);


            }
        });
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}

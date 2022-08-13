package com.libyasolutions.libyamarketplace.activity.tabs

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.VolleyError
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.model.Marker
import com.libyasolutions.libyamarketplace.BaseActivity
import com.libyasolutions.libyamarketplace.R
import com.libyasolutions.libyamarketplace.`object`.Menu
import com.libyasolutions.libyamarketplace.`object`.Shop
import com.libyasolutions.libyamarketplace.activity.FilterScreenActivity
import com.libyasolutions.libyamarketplace.activity.ShopDetailsNew
import com.libyasolutions.libyamarketplace.activity.tabs.homeRes.adapter.AdapProductNearbyYou
import com.libyasolutions.libyamarketplace.activity.tabs.homeRes.adapter.AdapRecommendedProducts
import com.libyasolutions.libyamarketplace.activity.tabs.homeRes.adapter.AdapRecommendedShops
import com.libyasolutions.libyamarketplace.activity.tabs.homeRes.adapter.AdapShopsNearbyYou
import com.libyasolutions.libyamarketplace.activity.tabs.user.EditProfileActivity
import com.libyasolutions.libyamarketplace.adapter.ListFoodHomeAdapter
import com.libyasolutions.libyamarketplace.config.Constant
import com.libyasolutions.libyamarketplace.config.GlobalValue
import com.libyasolutions.libyamarketplace.databinding.ActivityHomeBinding
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener
import com.libyasolutions.libyamarketplace.network.ParserUtility
import com.libyasolutions.libyamarketplace.util.GPSTracker
import com.libyasolutions.libyamarketplace.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_setting_.*
import kotlinx.android.synthetic.main.view_tab.*

@SuppressLint("NewApi")
class HomeActivity : BaseActivity(), LocationListener {

    private var currentMaker: Marker? = null
    private val markerRestaurantMap: HashMap<String, Shop> = HashMap<String, Shop>()
    private val arrShop: ArrayList<Shop> = ArrayList<Shop>()
    private var arrFood: ArrayList<Menu>? = null

    private val self: Activity? = null
    private var foodAdapter: ListFoodHomeAdapter? = null
    private var gps: GPSTracker? = null
    var handler: Handler? = null

    lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        self = this
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        binding.lifecycleOwner = this

        gps = GPSTracker(self)
        handler = Handler(Looper.getMainLooper())

        initViews();
        initControl()

    }

    private fun initViews() {
        binding.run {
            tvRecycleShopRecommend.adapter = AdapRecommendedShops(this@HomeActivity,null)
            tvRecycleProductRecommend.adapter = AdapRecommendedProducts(this@HomeActivity,null)
            tvRecycleShopNearby.adapter = AdapShopsNearbyYou(this@HomeActivity,null)
            tvRecycleProductNearby.adapter = AdapProductNearbyYou(this@HomeActivity,null)
        }

        if (GlobalValue.myAccount != null) {
            binding.tvUserName.text = GlobalValue.myAccount.full_name
            Glide.with(this).load(GlobalValue.myAccount.avatar).error(R.drawable.ic_user_photo)
                .placeholder(R.drawable.ic_user_photo).into(binding.defaultImg)
        }
    }

    override fun onResume() {
        super.onResume()
        if (Constant.isFakeLocation) {
            defaultLocation
        } else {
            refreshContent()
        }
    }


    private fun initControl() {
        arrFood = ArrayList()
        foodAdapter = ListFoodHomeAdapter(self, arrFood)

        binding.run {
            btnMenus.setOnClickListener {  gotoActivity(ListHomeFoodActivity::class.java) }
            btnShops.setOnClickListener {  gotoActivity(ListHomeShopActivity::class.java) }

            defaultImg.setOnClickListener {
                // top image icon click
                if (GlobalValue.myAccount != null) {
                    gotoActivity(EditProfileActivity::class.java)
                } else {
                    showDialogLogin()
                }
            }

            ivHome.setOnClickListener {  sendAction(Constant.SHOW_TAB_HOME)  }
            ivSetting.setOnClickListener {
                if (GlobalValue.myAccount != null) {
                    sendAction(Constant.SHOW_TAB_PROFILE)
                } else {
                    showDialogLogin()
                }
            }
            ivCart.setOnClickListener {
             // sendAction(Constant.SHOW_TAB_CART)
                gotoActivity(MainCartActivity::class.java)
            }
            filterImage.setOnClickListener {
            //   sendAction(Constant.SHOW_TAB_SEARCH)
                 FilterScreenActivity.comingFrom = "HOME"
                 gotoActivity(FilterScreenActivity::class.java)
            }

        }


//        lsvOffer.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.HORIZONTAL, false));
//        lsvOffer.setHasFixedSize(true);
//        lsvOffer.setAdapter(foodAdapter);
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

    private fun setData(latitude: Double, longitude: Double) {
        getOfferData(latitude, longitude)
        //        getListShop(latitude, longitude);
    }

    private val defaultLocation: Unit
        private get() {
            ModelManager.getDefaultLocation(this, false, object : ModelManagerListener {
                override fun onError(error: VolleyError) {
                    refreshContent()
                    Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG)
                        .show()
                }

                override fun onSuccess(`object`: Any) {
                    val json = `object` as String
                    GlobalValue.glatlng = ParserUtility.parseDefaultLocation(json)
                    refreshContent()
                }
            })
        }

    private fun getListShop(latitude: Double, longitude: Double) {
        ModelManager.getListShop(this, longitude, latitude, 1,
            false, object : ModelManagerListener {
                override fun onError(error: VolleyError) {
                    arrShop.clear()
                    Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG)
                        .show()
                }

                override fun onSuccess(`object`: Any) {
                    val json = `object` as String
                    arrShop.clear()
                    arrShop.addAll(ParserUtility.getListShop(json))
                }
            })
    }

    private fun getOfferData(latitude: Double, longitude: Double) {
        ModelManager.getListFoodOfDay(this, longitude,
            latitude, 1, false, object : ModelManagerListener {
                override fun onSuccess(`object`: Any) {
                    val json = `object` as String
                    arrFood!!.clear()
                    arrFood!!.addAll(ParserUtility.parseListFood(json))
                    foodAdapter?.notifyDataSetChanged()
                }

                override fun onError(error: VolleyError) {
                    // TODO Auto-generated method stub
                    arrFood!!.clear()
                    foodAdapter?.notifyDataSetChanged()
                    Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

/*    private inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        private val v: View = layoutInflater.inflate(R.layout.map_detail, null)
        override fun getInfoContents(marker: Marker): View {
            if (currentMaker != null && !currentMaker!!.isInfoWindowShown) {
                currentMaker!!.showInfoWindow()
            }
            return null
        }

        override fun getInfoWindow(marker: Marker): View {
            currentMaker = marker
            Logger.d("MapMaker iD ", marker.id)
            Logger.d("MapMaker", markerRestaurantMap.toString())
            val shop: Shop? = markerRestaurantMap[marker.id]
            val lblName: TextView = v.findViewById<TextView>(R.id.lblShopName)
            val lblAddress: TextView = v.findViewById<TextView>(R.id.lblAddress)
            val lblCategory: TextView = v.findViewById<TextView>(R.id.lblCategoryName)
            lblName.setSelected(true)
            if (shop != null) {
                lblName.setText(shop.getShopName())
                lblAddress.setText(shop.getAddress())
                lblCategory.setText(shop.getCategory())
            } else {
                Logger.d("AAA", "Restaurant is null")
            }
            return v
        }

    }*/

    fun refreshContent() {
        if (Constant.isFakeLocation) {
            setData(GlobalValue.glatlng.latitude, GlobalValue.glatlng.longitude)
            Toast.makeText(self, R.string.message_demo_location, Toast.LENGTH_LONG).show()
        } else {
            refreshMyLocation()
            setData(gps?.getLatitude()?: 0.0, gps?.getLongitude()?: 0.0)
        }
    }

    fun gotoShopDetailActivity(shopId: Int) {
        val bundle = Bundle()
        bundle.putInt(GlobalValue.KEY_SHOP_ID, shopId)
        val intent = Intent(this@HomeActivity, ShopDetailsNew::class.java)
        intent.putExtras(bundle)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun sendAction(action: String) {
        val intent = Intent()
        intent.action = action
        sendBroadcast(intent)
    }

    override fun onBackPressed() {
        this.parent.onBackPressed()
    }

    override fun onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy()
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        super.onPause()
        handler!!.removeCallbacks(runGoogleUpdateLocation)
    }

    private fun refreshMyLocation() {
//        Location location = null;
//        if (googleMap != null) {
//            location = googleMap.getMyLocation();

//            if (location == null) {
//                if (gps.canGetLocation()) {
//                    location = gps.getLocation();
//                }
//                handler.postDelayed(runGoogleUpdateLocation, 30 * 1000);
//            }
//        }
//        if (location != null)
//            setLocationLatLong(location.getLongitude(), location.getLatitude());
    }

    var runGoogleUpdateLocation =
        Runnable { if (NetworkUtil.checkNetworkAvailable(self)) refreshMyLocation() }

    override fun onLocationChanged(arg0: Location) {}

    companion object {
        const val HOME_SCREEN = "homeActivity"
    }
}
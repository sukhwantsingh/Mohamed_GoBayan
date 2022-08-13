package com.libyasolutions.libyamarketplace.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.android.volley.VolleyError
import com.libyasolutions.libyamarketplace.BaseActivity
import com.libyasolutions.libyamarketplace.R
import com.libyasolutions.libyamarketplace.`object`.CategorySearch
import com.libyasolutions.libyamarketplace.`object`.DepartmentCategory
import com.libyasolutions.libyamarketplace.activity.tabs.ListHomeFoodActivity
import com.libyasolutions.libyamarketplace.activity.tabs.ListHomeShopActivity
import com.libyasolutions.libyamarketplace.config.Constant
import com.libyasolutions.libyamarketplace.config.ConstantApp
import com.libyasolutions.libyamarketplace.config.GlobalValue
import com.libyasolutions.libyamarketplace.databinding.ActivityFilterScreenBinding
import com.libyasolutions.libyamarketplace.dialog.CityIdDialog
import com.libyasolutions.libyamarketplace.dialog.DepartmentDialog
import com.libyasolutions.libyamarketplace.modelmanager.ErrorNetworkHandler
import com.libyasolutions.libyamarketplace.modelmanager.ModelManager
import com.libyasolutions.libyamarketplace.modelmanager.ModelManagerListener
import com.libyasolutions.libyamarketplace.network.ParserUtility
import com.libyasolutions.libyamarketplace.util.GPSTracker
import com.libyasolutions.libyamarketplace.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_filter_screen.*


class FilterScreenActivity : BaseActivity() {

    lateinit var binding: ActivityFilterScreenBinding

    companion object {
        var comingFrom: String? = null

        val departmentCategoryList: ArrayList<DepartmentCategory> = ArrayList()
        val categorySearchList: ArrayList<CategorySearch> = ArrayList()

    }


    private var goToWhere = ""

    private var latitude = ""
    private var longitude = ""
    private var cityId = ConstantApp.ALL_CITIES

    private var minPrice = ""
    private var maxPrice = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter_screen)
        binding.lifecycleOwner = this

        initViews()
        initControls()
    }

    private fun initControls() {
        binding.run {
            sbDistance.isEnabled = false

            imageView.setOnClickListener { finish() }

            // Search for
            tvShop.setOnClickListener {
                tvShop.selectView(tvProducts)
                goToWhere = "SHOP"
            }
            tvProducts.setOnClickListener {
                tvProducts.selectView(tvShop)
                goToWhere = "PRODUCT"
            }

            // Departments
            tvAllDeparts.setOnClickListener {
                tvAllDeparts.selectView(tvChooseDepart)
                chooseSearchByDepartment(false)
            }
            tvChooseDepart.setOnClickListener {
                tvChooseDepart.selectView(tvAllDeparts)
                chooseSearchByDepartment(true)
            }

            // Locations
            tvAllCities.setOnClickListener {
                tvAllCities.selectView(tvSelectCity)
                sbDistance.isEnabled = false
                tvMyLocationh.setBackgroundResource(R.drawable.bg_white_corner_6)
                tvMyLocationh.setTextColor(ContextCompat.getColor(this@FilterScreenActivity, R.color.black ))
                refreshLocationData()

                cityId = ConstantApp.ALL_CITIES
                binding.tvSelectCity.text = getString(R.string.select_city)

            }
            tvSelectCity.setOnClickListener {
                tvSelectCity.selectView(tvAllCities)
                sbDistance.isEnabled = false
                tvMyLocationh.setBackgroundResource(R.drawable.bg_white_corner_6)
                tvMyLocationh.setTextColor(ContextCompat.getColor(this@FilterScreenActivity, R.color.black ))
                refreshLocationData()

                val dialog = CityIdDialog.newInstance()
                dialog.show(supportFragmentManager, dialog.tag)

                dialog.setOnSearchByCityIdListener { idCity: String, cityName: String? ->
                    if (cityName != null) {
                        cityId = idCity
                        binding.tvSelectCity.text = cityName
                    }
                }

            }

            llMyLocation.setOnClickListener {
                null.selectView(tvAllCities, tvSelectCity)
                tvMyLocationh.setBackgroundResource(R.drawable.round_upper_corner_blue_bg)
                tvMyLocationh.setTextColor(ContextCompat.getColor(this@FilterScreenActivity, R.color.white ))
                sbDistance.isEnabled = true

                getLatLong()
                distance = "45"
                tvSelectCity.text = getString(R.string.select_city)
                configLocationView()
            }

            // Any price
            tvAnyPrice.setOnClickListener {
                tvAnyPrice.selectView()
                tvhSp.setBackgroundResource(R.drawable.bg_white_corner_6)
                tvhSp.setTextColor(ContextCompat.getColor(this@FilterScreenActivity, R.color.black ))

                edtMin.isEnabled = false; edtMin.setText("")
                edtMax.isEnabled = false; edtMax.setText("")
            }

            rlSelectPrice.setOnClickListener {
                null.selectView(tvAnyPrice)

                tvhSp.setBackgroundResource(R.drawable.round_upper_corner_blue_bg)
                tvhSp.setTextColor(ContextCompat.getColor(this@FilterScreenActivity, R.color.white ))

                edtMin.isEnabled = true
                edtMax.isEnabled = true

            }

            // apply filter
            btnApplyFilter.setOnClickListener {
                // Pending to apply filter
                if (binding.edtMin.isEnabled) {
                    minPrice = binding.edtMin.text.toString().trim()
                    maxPrice = binding.edtMax.text.toString().trim()
                } else {
                    minPrice = ""; maxPrice = ""; }

                // go to action
                if (comingFrom.equals("HOME", ignoreCase = true)) {

                    val spIntent = if (goToWhere.equals("SHOP", ignoreCase = true)) {
                        Intent(this@FilterScreenActivity, ListHomeShopActivity::class.java)
                    } else if (goToWhere.equals("PRODUCT", ignoreCase = true)) {
                        Intent(this@FilterScreenActivity, ListHomeFoodActivity::class.java)
                    } else Intent()

                    spIntent.apply {
                        putExtra("cityId", cityId)
                        putExtra("distance", distance)
                        putExtra("minPrice", minPrice)
                        putExtra("maxPrice", maxPrice)
                        putExtra("longitude", longitude)
                        putExtra("latitude", latitude)
                    }

                    startActivity(spIntent)
                    finish()
                } else {
                    val shopIntent = Intent().apply {
                        putExtra("cityId", cityId)
                        putExtra("distance", distance)
                        putExtra("minPrice", minPrice)
                        putExtra("maxPrice", maxPrice)
                        putExtra("longitude", longitude)
                        putExtra("latitude", latitude)

                    }
                    setResult(RESULT_OK, shopIntent)
                    finish()
                }

            }

        }
    }

    private fun initViews() {
        comingFrom?.let { cmFrom ->
            binding.run {
                goToWhere = when (cmFrom) {
                    "HOME" -> {
                        visibleViews(tvhSearchFor, llSearchfor); "SHOP"
                    }
                    "SHOP" -> {
                        null?.goneViews(tvhSearchFor, llSearchfor); "SHOP"
                    }
                    "PRODUCT" -> {
                        null?.goneViews(tvhSearchFor, llSearchfor); "PRODUCT"
                    }

                    else -> ""

                }
            }
        }

        categorySearchList.clear()
        configLocationView()
    }


    private fun getLatLong() {
        if (Constant.isFakeLocation) {
            latitude = GlobalValue.glatlng.latitude.toString() + ""
            longitude = GlobalValue.glatlng.longitude.toString() + ""
        } else {
            val gps = GPSTracker(this)
            if (gps.canGetLocation()) {
                latitude = gps.latitude.toString() + ""
                longitude = gps.longitude.toString() + ""
            }
        }
    }

    private var distance = "45"
    private fun configLocationView() {
        binding.run {
            sbDistance.max = 450
            sbDistance.setPadding(10, 0, 10, 0)
            sbDistance.progress = (distance.toDouble() * 10).toInt()
            if (distance.toDouble() == 0.0) {
                tvDistance.text =
                    getString(R.string.value_distance_all, getString(R.string.text_0_km))
            } else {
                tvDistance.text = getString(R.string.value_distance, distance)
            }

            // listener
            sbDistance.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                    distance =
                        (progress.toDouble() / 10).toString() + "" // use it where you want to use

                    if (progress == 0) {
                        tvDistance.text =
                            getString(R.string.value_distance_all, getString(R.string.text_0_km))
                    } else {
                        tvDistance.text = getString(R.string.value_distance, distance)
                    }

                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }
    }

    private fun chooseSearchByDepartment(need: Boolean) {
        if (need) {
            if (!NetworkUtil.checkNetworkAvailable(this)) {
                showToast(getString(R.string.no_connection))
                return
            }
            ModelManager.getListDepartmentCategory(this, true, object : ModelManagerListener {
                override fun onError(error: VolleyError) {
                    Log.e(TAG, "error api" + error.message)
                    showToast(ErrorNetworkHandler.processError(error))
                }

                override fun onSuccess(mRes: Any) {
                    if (ParserUtility.isSuccess(mRes.toString())) {
                        departmentCategoryList.clear()
                        val list: List<DepartmentCategory>? =
                            ParserUtility.parseDepartmentCategory(mRes.toString())
                        if (list != null && list.isNotEmpty()) {
                            departmentCategoryList.addAll(list)
                        } else {
                            Log.e(TAG, "some error in parse json")
                        }
                        setupDepartmentDialog()
                    } else {
                        showToast(ParserUtility.getMessage(mRes.toString()))
                    }
                }
            })
        }
    }

    private fun setupDepartmentDialog() {
        val dialog = DepartmentDialog.newInstance(departmentCategoryList)
        dialog.show(supportFragmentManager, dialog.tag)

        dialog.setOnSearchByDepartmentListener { list: List<CategorySearch?>? ->
            formatList(list)
            //  page = 1
            //   isLoadingMore = false
            //  searchListShopHome(page, true)
        }
    }

    private fun formatList(list: List<CategorySearch?>?) {
        list?.let {

            for (categorySearch in list) {
                if ((categorySearch?.category?.length ?: 0) > 1) {
                    val categoryId =
                        categorySearch?.category?.substring(0, categorySearch.category.length - 1)
                    categorySearch?.category = categoryId
                }
                categorySearch?.let { cs -> categorySearchList.add(cs) }

            }
        }

    }

    private fun View?.selectView(vararg unSelectView: View?, isDrawable: Boolean = false) {
        // for unSelected
        unSelectView.forEach {
            it?.let {
                it.setBackgroundResource(R.drawable.bg_white_corner_6)
                when (it) {
                    is TextView -> { it.setTextColor(ContextCompat.getColor(this@FilterScreenActivity, R.color.black ))}
                }
            }

        }

        // for selected
        this?.let {
            it.setBackgroundResource(R.drawable.blue_fill_round_gradient)
            when (it) {
                is TextView -> {
                    it.setTextColor(
                        ContextCompat.getColor(
                            this@FilterScreenActivity,
                            R.color.white
                        )
                    )
                    if (isDrawable) it.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_icon_metro_shop_whitye,
                        0,
                        0,
                        0
                    )
                }
                else -> Unit
            }
        }

    }

    private fun RadioButton.selectRadioView(vararg unSelectRb: RadioButton?) {
        unSelectRb.forEach { it?.isChecked = false }
        this.isChecked = true

    }

    private fun View?.goneViews(vararg unSelectRb: View?) {
        unSelectRb.forEach { it?.visibility = View.GONE }
        this?.visibility = View.VISIBLE
    }

    private fun visibleViews(vararg unSelectRb: View?) {
        unSelectRb.forEach {
            it?.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        comingFrom = null
    }

    private fun refreshLocationData() {
        latitude = ""
        longitude = ""
        distance = "45"

        configLocationView()
    }
}
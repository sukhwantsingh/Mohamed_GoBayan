package com.libyasolutions.libyamarketplace.activity.tabs.cart

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.libyasolutions.libyamarketplace.BaseActivity
import com.libyasolutions.libyamarketplace.R
import com.libyasolutions.libyamarketplace.activity.tabs.cart.adapter.AdapterSelectedProductsOrder
import com.libyasolutions.libyamarketplace.databinding.ActivityOrderDetailsAfterDeliveryInfoBinding

class OrderDetailsAfterDeliveryInfo : BaseActivity() {

    lateinit var binding: ActivityOrderDetailsAfterDeliveryInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_details_after_delivery_info)
        binding.lifecycleOwner = this

        initViews()

    }

    private fun initViews() {
        binding.run {
            recycleProducts.adapter = AdapterSelectedProductsOrder(recycleProducts,this@OrderDetailsAfterDeliveryInfo,null)


            // init controls
            ivBack.setOnClickListener { finish() }
            btnPayment.setOnClickListener { gotoActivity(PaymentAfterOrderDetails::class.java) }

        }

    }


}
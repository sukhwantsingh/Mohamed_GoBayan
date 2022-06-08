package com.libyasolutions.libyamarketplace.activity.tabs.cart

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.libyasolutions.libyamarketplace.BaseActivity
import com.libyasolutions.libyamarketplace.R
import com.libyasolutions.libyamarketplace.activity.tabs.MainTabActivity
import com.libyasolutions.libyamarketplace.databinding.ActivityOrderDetailsAfterDeliveryInfoBinding
import com.libyasolutions.libyamarketplace.databinding.ActivityPaymentAfterOrderDetailsBinding
import kotlinx.android.synthetic.main.activity_payment_after_order_details.*

class PaymentAfterOrderDetails : BaseActivity() {

    lateinit var binding: ActivityPaymentAfterOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_payment_after_order_details)
        binding.lifecycleOwner = this

        initViews()

    }

    private fun initViews() {
        binding.run {
            ivBack.setOnClickListener { finish() }
            btnPlaceOrder.setOnClickListener {
                backActivity(MainTabActivity::class.java)
                finishAffinity()
            }


        }

    }


}
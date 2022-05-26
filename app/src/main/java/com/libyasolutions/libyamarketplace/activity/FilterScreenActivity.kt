package com.libyasolutions.libyamarketplace.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.libyasolutions.libyamarketplace.R
import com.libyasolutions.libyamarketplace.databinding.ActivityFilterScreenBinding

class FilterScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivityFilterScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_filter_screen)
        binding.lifecycleOwner = this

        initViews()
    }

    private fun initViews() {
        binding.imageView.setOnClickListener { finish() }

    }


}
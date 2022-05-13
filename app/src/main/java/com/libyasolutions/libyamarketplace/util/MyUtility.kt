package com.libyasolutions.libyamarketplace.util

import android.app.Activity
import android.content.res.Configuration
import androidx.fragment.app.Fragment
import java.util.*


fun Activity.pref() = MySharedPreferences.getInstance(this)
fun Fragment.pref() = MySharedPreferences.getInstance(requireContext())

fun Activity.changeAppLang(langCode: String){
    val locale = Locale(langCode)
    Locale.setDefault(locale)
    val config = Configuration()
    config.locale = locale
    baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    MySharedPreferences.getInstance(this).putLanguage(langCode)
}


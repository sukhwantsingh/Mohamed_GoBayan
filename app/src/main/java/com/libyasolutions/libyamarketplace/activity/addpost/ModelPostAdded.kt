package com.libyasolutions.libyamarketplace.activity.addpost


import androidx.annotation.Keep

@Keep
data class ModelPostAdded(
    val `data`: String?,
    val message: String?,
    val status: String?
)
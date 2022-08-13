package com.libyasolutions.libyamarketplace.activity.addpost


import android.text.TextUtils.split
import androidx.annotation.Keep

@Keep
data class ModelPosts(
    val data: Data?,
    val message: String?,
    val status: String?
) {
    @Keep
    data class Data(
        val posts: ArrayList<Post?>?
    ) {
        @Keep
        data class Post(
            val description: String?,
            val id: String?,
            val image: String?,
            val created_at: String?,
            val status: String?
        ){

            fun postDate() = if(created_at?.trim()?.contains(" ") == true)created_at.trim().split(" ")[0] else created_at?.trim().orEmpty()

        }
    }
}
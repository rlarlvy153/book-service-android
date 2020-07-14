package com.kakao.book_service_android.network.model.search

import com.google.gson.annotations.SerializedName

data class SimpleBook(

    @SerializedName("title")
    val title: String = "",

    @SerializedName("subtitle")
    val subTitle: String = "",

    @SerializedName("isbn13")
    val isbn13: String = "",

    @SerializedName("price")
    val price: String = "",

    @SerializedName("image")
    val imageUrl: String = "",

    @SerializedName("url")
    val bookUrl: String = ""
)

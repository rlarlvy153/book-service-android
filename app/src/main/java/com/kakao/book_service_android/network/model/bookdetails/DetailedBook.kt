package com.kakao.book_service_android.network.model.bookdetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DetailedBook(

    @SerializedName("error")
    val error: Int = 0,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("subtitle")
    val subtitle: String = "",

    @SerializedName("authors")
    val authors: String = "",

    @SerializedName("publisher")
    val publisher: String = "",

    @SerializedName("isbn10")
    val isbn10: String = "",

    @SerializedName("isbn13")
    val isbn13: String = "",

    @SerializedName("pages")
    val pages: Int = 0,

    @SerializedName("year")
    val year: Int = 0,

    @SerializedName("rating")
    val rating: Int = 0,

    @SerializedName("desc")
    val desc: String = "",

    @SerializedName("price")
    val price: String = "",

    @SerializedName("image")
    val imageUrl: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("pdf")
    val pdfJsonMap: LinkedHashMap<String, String>?
) : Serializable
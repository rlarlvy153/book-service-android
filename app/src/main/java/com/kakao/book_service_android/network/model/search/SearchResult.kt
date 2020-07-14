package com.kakao.book_service_android.network.model.search

import com.google.gson.annotations.SerializedName

data class SearchResult(

    @SerializedName("total")
    var total: Int,

    @SerializedName("page")
    var page: Int,

    @SerializedName("books")
    var books: ArrayList<SimpleBook>
) {
    constructor() : this(0, 0, ArrayList())
}
package com.kakao.book_service_android.network.bookapi

import com.kakao.book_service_android.network.model.bookdetails.DetailedBook
import com.kakao.book_service_android.network.model.search.SearchResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface BookApiInterface {

    @GET("search/{search_query}")
    fun searchBooks(@Path("search_query") searchQuery: String): Observable<SearchResult>

    @GET("search/{search_query}/{page}")
    fun searchBooksPage(@Path("search_query") searchQuery: String, @Path("page") page: Int): Observable<SearchResult>

    @GET("books/{isbn13}")
    fun bookDetails(@Path("isbn13") isbn13: String): Observable<DetailedBook>
}

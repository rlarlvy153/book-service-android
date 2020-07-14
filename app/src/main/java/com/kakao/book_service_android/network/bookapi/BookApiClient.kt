package com.kakao.book_service_android.network.bookapi

import com.kakao.book_service_android.network.model.bookdetails.DetailedBook
import com.kakao.book_service_android.network.model.search.SearchResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BookApiClient(private val bookApiInterface: BookApiInterface) {

    fun searchBooksPage(searchQuery: String, page: Int): Observable<SearchResult> {

        return bookApiInterface.searchBooksPage(searchQuery, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun bookDetails(isbn13: String): Observable<DetailedBook> {

        return bookApiInterface.bookDetails(isbn13)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
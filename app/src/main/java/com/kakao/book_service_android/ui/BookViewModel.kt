package com.kakao.book_service_android.ui

import androidx.lifecycle.MutableLiveData
import com.kakao.book_service_android.AppComponents
import com.kakao.book_service_android.R
import com.kakao.book_service_android.network.bookapi.BookApiClient
import com.kakao.book_service_android.network.model.bookdetails.DetailedBook
import com.kakao.book_service_android.network.model.search.SearchResult
import com.kakao.book_service_android.network.model.search.SimpleBook
import com.kakao.book_service_android.ui.search.SearchConstant
import io.reactivex.Observable
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.max


class BookViewModel : BaseViewModel(), KoinComponent {

    private var lastSearchedQuery = ""

    private val bookApiClient: BookApiClient by inject()

    private val searchedResultMap = HashMap<String, SearchResult>()

    var errorMessage = MutableLiveData<String>()

    val bookmarkList = MutableLiveData<LinkedList<DetailedBook>>()

    var searchedResult = MutableLiveData<ArrayList<SimpleBook>>()

    init {
        bookmarkList.value = LinkedList()
        searchedResult.value = ArrayList()
    }

    val isSearchingLiveData = MutableLiveData(false)

    fun searchMoreBooks() {
        searchNext()
    }

    fun searchNewBooks(searchQuery: String) {
        if (searchQuery == lastSearchedQuery) {
            return
        }
        lastSearchedQuery = searchQuery

        resetSearchedHistory()

        searchNext()
    }

    private fun resetSearchedHistory() {
        searchedResultMap.clear()
    }

    private fun getShouldSearchQuery(query: String): List<String> {
        val queryList = query.split(SearchConstant.SEARCH_DELIMITER)

        return queryList.filter {
            if (!searchedResultMap.containsKey(it)) { //처음 찾는것이면 추가
                return@filter true
            }

            val currentBookNum = searchedResultMap[it]?.books?.size ?: 0
            val totalBookNum = searchedResultMap[it]?.total ?: 0

            if (currentBookNum < totalBookNum) { //덜 찾았으면 추가
                return@filter true
            }

            return@filter false
        }
    }

    private fun initQueryResultMap(shouldSearchQuery: List<String>) {
        for (query in shouldSearchQuery) {
            if (!searchedResultMap.containsKey(query)) {
                searchedResultMap[query] = SearchResult(0, 0, ArrayList())
            }
        }
    }

    private fun searchNext() {
        val shouldSearchQuery = getShouldSearchQuery(lastSearchedQuery)
        Timber.d(shouldSearchQuery.toString())
        initQueryResultMap(shouldSearchQuery)

        searchBooksWithList(shouldSearchQuery)
    }

    private fun searchBooksWithList(queryList: List<String>) {

        val requestList = queryList.map {
            var page: Int

            searchedResultMap[it].let { model ->
                page = (model?.page ?: 0) + 1
            }

            bookApiClient.searchBooksPage(it, page)
        }

        if (requestList.isEmpty()) {
            return
        }

        isSearchingLiveData.value = true

        val zippedRequest = Observable.zip(requestList) {
            for (result in it.withIndex()) {
                val resultSearchedModel = (result.value as SearchResult)
                val queriedString = queryList[result.index]
                addSearchedMapResult(queriedString, resultSearchedModel)
            }

            SearchResult().apply {
                for (eachSearchResult in it) {
                    if (eachSearchResult is SearchResult) {
                        total += eachSearchResult.total
                        page = max(page, eachSearchResult.page)
                        books.addAll(eachSearchResult.books)
                    }
                }
            }

        }.subscribe(
            {
                if (it.page == 1) {
                    searchedResult.value = it.books
                } else {
                    searchedResult.value?.addAll(it.books)
                    searchedResult.value = searchedResult.value
                }
                isSearchingLiveData.value = false
            },
            {
                sendErrorMessage(AppComponents.applicationContext.getString(R.string.error_message))

                isSearchingLiveData.value = false
            }
        )

        disposable.add(zippedRequest)
    }

    private fun addSearchedMapResult(query: String, searchedResultModel: SearchResult) {
        searchedResultMap[query]?.total = searchedResultModel.total
        searchedResultMap[query]?.page = searchedResultModel.page
        searchedResultMap[query]?.books?.addAll(searchedResultModel.books)
    }

    fun isBookMarked(isbn13: String): Boolean {

        return bookmarkList.value?.any { it.isbn13 == isbn13 } ?: false
    }

    fun insertBookMarkIfNot(book: DetailedBook) {
        if (bookmarkList.value?.contains(book) == true) {
            return
        }
        bookmarkList.value?.add(book)
        bookmarkList.value = bookmarkList.value
    }

    fun removeBookMark(book: DetailedBook) {
        bookmarkList.value?.remove(book)
        bookmarkList.value = bookmarkList.value
    }

    private fun sendErrorMessage(text: String) {
        errorMessage.value = text
        errorMessage.value = ""
    }
}
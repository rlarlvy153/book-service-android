package com.kakao.book_service_android.ui.booksdetails

import androidx.lifecycle.MutableLiveData
import com.kakao.book_service_android.AppComponents
import com.kakao.book_service_android.R
import com.kakao.book_service_android.network.bookapi.BookApiClient
import com.kakao.book_service_android.network.model.bookdetails.DetailedBook
import com.kakao.book_service_android.ui.BaseViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class DetailedBookViewModel : BaseViewModel(), KoinComponent {

    var detailedBookLiveData = MutableLiveData<DetailedBook>()

    var isSearching = MutableLiveData<Boolean>()

    var errorMessage = MutableLiveData<String>()

    private lateinit var detailedBook: DetailedBook

    private val bookApiClient: BookApiClient by inject()

    fun fetchDetailedBookInfoWithBook(book: DetailedBook) {
        isSearching.value = true
        detailedBookLiveData.value = book
        isSearching.value = false
    }

    fun fetchDetailedBookInfo(isbn13: String) {
        isSearching.value = true

        val bookDetails = bookApiClient.bookDetails(isbn13)
            .subscribe({ result ->
                if (result.error != 0) {
                    isSearching.value = false

                    sendErrorMessage(AppComponents.applicationContext.getString(R.string.error_message))

                    return@subscribe
                }

                detailedBook = result
                detailedBookLiveData.value = result

                isSearching.value = false

            }, {
                isSearching.value = false

                sendErrorMessage(AppComponents.applicationContext.getString(R.string.error_message))

                return@subscribe
            })

        disposable.add(bookDetails)
    }

    private fun sendErrorMessage(text: String) {
        errorMessage.value = text
        errorMessage.value = ""
    }
}
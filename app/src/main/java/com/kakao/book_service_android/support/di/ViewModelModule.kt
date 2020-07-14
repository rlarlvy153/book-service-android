package com.kakao.book_service_android.support.di

import com.kakao.book_service_android.ui.BookViewModel
import com.kakao.book_service_android.ui.booksdetails.DetailedBookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { BookViewModel() }

    viewModel { DetailedBookViewModel() }
}
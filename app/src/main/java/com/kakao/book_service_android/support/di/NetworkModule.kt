package com.kakao.book_service_android.support.di

import com.kakao.book_service_android.network.AppRetrofitBuilder
import com.kakao.book_service_android.network.NetworkConfig
import com.kakao.book_service_android.network.bookapi.BookApiClient
import com.kakao.book_service_android.network.bookapi.BookApiInterface
import org.koin.dsl.module

val networkModule = module {

    single {
        BookApiClient(
            AppRetrofitBuilder(NetworkConfig.API_BASE_URL)
                .build()
                .create(BookApiInterface::class.java)
        )
    }
}
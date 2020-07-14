package com.kakao.book_service_android

import com.kakao.book_service_android.support.di.networkModule
import com.kakao.book_service_android.support.di.viewModelModule

val koinModulesList = listOf(viewModelModule, networkModule)

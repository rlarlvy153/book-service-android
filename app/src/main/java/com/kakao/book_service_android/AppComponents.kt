package com.kakao.book_service_android

import android.content.Context

object AppComponents {

    lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }
}
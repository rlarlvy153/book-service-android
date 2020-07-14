package com.kakao.book_service_android.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppRetrofitBuilder(private val baseUrl: String, private val interceptor: Interceptor? = null) {

    fun build(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun createClient(): OkHttpClient {
        val httpLogging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLogging)
            .addNetworkInterceptor(StethoInterceptor())
            .connectTimeout(NetworkConfig.ALL_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NetworkConfig.ALL_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.ALL_TIMEOUT, TimeUnit.SECONDS)

        interceptor?.let {
            okHttpClient.addInterceptor(it)
        }

        return okHttpClient.build()
    }
}
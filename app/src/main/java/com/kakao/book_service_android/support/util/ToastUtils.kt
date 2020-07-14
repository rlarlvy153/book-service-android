package com.kakao.book_service_android.support.util

import android.widget.Toast
import com.kakao.book_service_android.AppComponents

object ToastUtils {

    fun showToast(text: String) {
        Toast.makeText(AppComponents.applicationContext, text, Toast.LENGTH_SHORT).show()
    }
}
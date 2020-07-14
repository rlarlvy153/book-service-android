package com.kakao.book_service_android.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.kakao.book_service_android.R
import com.kakao.book_service_android.network.model.bookdetails.DetailedBook
import com.kakao.book_service_android.support.util.ToastUtils
import com.kakao.book_service_android.ui.booksdetails.DetailedActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {

    private val bookViewModel: BookViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        observeEvents()

        initViewPager()
    }

    private fun initViewPager() {
        mainViewPager.adapter = MainTabAdapter(supportFragmentManager)
        mainTab.setupWithViewPager(mainViewPager)
    }

    private fun observeEvents() {
        bookViewModel.errorMessage.observe(this, Observer {
            if (it.isNullOrBlank()) {
                return@Observer
            }
            ToastUtils.showToast(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {

            val detailedBook = data.getSerializableExtra(DetailedActivity.BOOK_DATA) as DetailedBook
            val bookmarked = data.getBooleanExtra(DetailedActivity.IS_BOOK_MARKED, false)

            if (bookmarked) {
                bookViewModel.insertBookMarkIfNot(detailedBook)
            } else {
                bookViewModel.removeBookMark(detailedBook)
            }
        }
    }
}
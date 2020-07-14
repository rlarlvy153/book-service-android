package com.kakao.book_service_android.ui.booksdetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.kakao.book_service_android.R
import com.kakao.book_service_android.network.model.bookdetails.DetailedBook
import com.kakao.book_service_android.support.util.ToastUtils
import kotlinx.android.synthetic.main.activity_detailed.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class DetailedActivity : AppCompatActivity(), KoinComponent {

    companion object {
        const val ISBN13 = "isbn13"
        const val IS_BOOK_MARKED = "bookmarked"
        const val BOOK_DATA = "book_data"
    }

    private val detailedBookViewModel: DetailedBookViewModel by inject()

    private lateinit var currentBook: DetailedBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detailed)

        observeEvents()

        setActivityIntentInfo()
    }

    private fun setActivityIntentInfo() {
        val isbn13 = intent.getStringExtra(ISBN13)
        val clickedBook = intent.getSerializableExtra(BOOK_DATA)
        val isBookmarked = intent.getBooleanExtra(IS_BOOK_MARKED, false)
        bookmarkCheck.isChecked = isBookmarked

        isbn13?.let {
            detailedBookViewModel.fetchDetailedBookInfo(isbn13)
        }

        clickedBook?.let {
            if (it is DetailedBook) {
                detailedBookViewModel.fetchDetailedBookInfoWithBook(it)
            }
        }
    }

    private fun observeEvents() {
        detailedBookViewModel.detailedBookLiveData.observe(this, Observer {
            currentBook = it

            setDetailedBookInfo(it)
        })

        detailedBookViewModel.isSearching.observe(this, Observer {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        detailedBookViewModel.errorMessage.observe(this, Observer {
            if (it.isNullOrBlank()) {
                return@Observer
            }
            ToastUtils.showToast(it)
        })
    }

    private fun showLoading() {
        nowLoading.visibility = View.VISIBLE
        detailedResultContainer.visibility = View.GONE

        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun hideLoading() {
        nowLoading.visibility = View.GONE
        detailedResultContainer.visibility = View.VISIBLE

        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun setDetailedBookInfo(detailedBookModel: DetailedBook) {

        setDetailedTitle(detailedBookModel.title)

        setDetailedSubtitle(detailedBookModel.subtitle)

        setDetailedAuthors(detailedBookModel.authors)

        setDetailedPublisher(detailedBookModel.publisher)

        setDetailedIsbn10(detailedBookModel.isbn10)

        setDetailedIsbn13(detailedBookModel.isbn13)

        setDetailedPage(detailedBookModel.pages.toString())

        setDetailedYear(detailedBookModel.year.toString())

        setDetailedRating(detailedBookModel.rating.toString())

        setDetailedDesc(detailedBookModel.desc)

        setDetailedPrice(detailedBookModel.price)

        setDetailedUrl(detailedBookModel.url)

        setDetailedImage(detailedBookModel.imageUrl)

        setDetailedPdf(detailedBookModel.pdfJsonMap)
    }

    private fun setDetailedTitle(title: String) {
        detailedTitle.text = title
    }

    private fun setDetailedSubtitle(subTitle: String) {
        detailedSubTitle.text = subTitle
    }

    private fun setDetailedAuthors(authors: String) {
        detailedAuthors.text = authors
    }

    private fun setDetailedPublisher(publisher: String) {
        detailedPublisher.text = publisher
    }

    private fun setDetailedIsbn10(isbn10: String) {
        detailedIsbn10.text = isbn10
    }

    private fun setDetailedIsbn13(isbn13: String) {
        detailedIsbn13.text = isbn13
    }

    private fun setDetailedPage(page: String) {
        detailedPage.text = page
    }

    private fun setDetailedYear(year: String) {
        detailedYear.text = year
    }

    private fun setDetailedRating(rating: String) {
        detailedRating.text = rating
    }

    private fun setDetailedDesc(desc: String) {
        detailedDesc.text = desc
    }

    private fun setDetailedPrice(price: String) {
        detailedPrice.text = price
    }

    private fun setDetailedUrl(url: String) {
        detailedUrl.text = url
    }

    private fun setDetailedImage(imageUrl: String) {
        Glide.with(this).load(imageUrl)
            .placeholder(R.drawable.image_loading)
            .error(R.drawable.empty_image)
            .into(detailedImage)
    }

    private fun setDetailedPdf(pdfMap: LinkedHashMap<String, String>?) {
        if (pdfMap == null) {
            return
        }

        var concatPdfWithHeader = ""

        for (key in pdfMap.keys) {
            concatPdfWithHeader += "$key : ${pdfMap[key]}\n"
        }

        detailedPdf.text = concatPdfWithHeader
    }

    override fun onBackPressed() {

        val result = Intent()
        result.putExtra(BOOK_DATA, currentBook)
        result.putExtra(IS_BOOK_MARKED, bookmarkCheck.isChecked)

        setResult(RESULT_OK, result)
        finish()
    }
}
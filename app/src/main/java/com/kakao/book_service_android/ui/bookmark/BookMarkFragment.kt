package com.kakao.book_service_android.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.book_service_android.R
import com.kakao.book_service_android.network.model.bookdetails.DetailedBook
import com.kakao.book_service_android.ui.BookViewModel
import com.kakao.book_service_android.ui.booksdetails.DetailedActivity
import kotlinx.android.synthetic.main.fragment_book_mark.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class BookMarkFragment : Fragment(), KoinComponent {

    private val bookViewModel: BookViewModel by sharedViewModel()

    companion object {
        const val CALL_BY_BOOK_MARKED = 1

        val instance = BookMarkFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book_mark, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initBookMarkRecyclerView()

        observeEvents()
    }

    private fun initBookMarkRecyclerView() {

        val onBookmarkedItemClickListener = object : BookMarkListAdapter.OnItemClickListener {
            override fun onItemClick(clickedBook: DetailedBook) {
                val detailedIntent = Intent(activity, DetailedActivity::class.java)
                detailedIntent.putExtra(DetailedActivity.BOOK_DATA, clickedBook)
                detailedIntent.putExtra(DetailedActivity.IS_BOOK_MARKED, bookViewModel.isBookMarked(clickedBook.isbn13))

                activity?.startActivityForResult(detailedIntent, CALL_BY_BOOK_MARKED)
            }
        }

        bookMarkListRecyclerView.run {
            addItemDecoration(
                DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            )
            layoutManager = LinearLayoutManager(context)
            adapter = BookMarkListAdapter(onBookmarkedItemClickListener)
        }
    }

    private fun observeEvents() {

        bookViewModel.bookmarkList.observe(viewLifecycleOwner, Observer {
            val adapter = bookMarkListRecyclerView.adapter
            if (adapter is BookMarkListAdapter) {
                adapter.bookMarkList = ArrayList(it)
            }
        })
    }
}
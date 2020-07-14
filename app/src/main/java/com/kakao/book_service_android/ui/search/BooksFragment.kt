package com.kakao.book_service_android.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.book_service_android.AppComponents
import com.kakao.book_service_android.R
import com.kakao.book_service_android.network.model.search.SimpleBook
import com.kakao.book_service_android.support.util.ToastUtils
import com.kakao.book_service_android.ui.BookViewModel
import com.kakao.book_service_android.ui.booksdetails.DetailedActivity
import kotlinx.android.synthetic.main.fragment_book_search.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent
import java.util.*

class BooksFragment : Fragment(), KoinComponent {

    companion object {
        const val CALL_BY_SIMPLE_BOOK_DATA = 2
        const val MAX_SEARCH_HISTORY = 10

        val instance = BooksFragment()
    }

    private val bookViewModel: BookViewModel by sharedViewModel()

    private var searchHistory = LinkedList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        observeEvents()

        initSearchBtnListener()
    }

    private fun initSearchBtnListener() {
        searchButton.setOnClickListener {
            if (searchEdit.text.toString().isBlank()) {
                ToastUtils.showToast(AppComponents.applicationContext.getString(R.string.search_query_empty))
                return@setOnClickListener
            }

            val splitAndTrimmed = searchEdit.text.toString().split(SearchConstant.SEARCH_DELIMITER).joinToString { it.trim() }

            searchEdit.setText(splitAndTrimmed)

            if (searchEdit.text.toString().count { it == SearchConstant.SEARCH_DELIMITER } > SearchConstant.MAX_QUERY - 1) {
                ToastUtils.showToast(AppComponents.applicationContext.getString(R.string.exceed_max_query))
                return@setOnClickListener
            }

            val searchQuery = searchEdit.text.toString()

            fetchSearchHistory(searchQuery)

            bookViewModel.searchNewBooks(searchQuery)
        }
    }

    private fun fetchSearchHistory(query: String) {
        if (searchHistory.contains(query)) {
            searchHistory.remove(query)
        }
        searchHistory.addFirst(query)

        if(searchHistory.size > MAX_SEARCH_HISTORY){
            searchHistory.removeLast()
        }

        context?.let {
            searchEdit.setAdapter(ArrayAdapter(it, android.R.layout.simple_dropdown_item_1line, searchHistory))
        }
    }

    private fun initRecyclerView() {
        val onSearchedItemClickListener = object : SearchedBooksAdapter.OnItemClickListener {
            override fun onItemClick(clickedBook: SimpleBook) {
                val detailedIntent = Intent(activity, DetailedActivity::class.java)
                detailedIntent.putExtra(DetailedActivity.ISBN13, clickedBook.isbn13)
                detailedIntent.putExtra(DetailedActivity.IS_BOOK_MARKED, bookViewModel.isBookMarked(clickedBook.isbn13))
                activity?.startActivityForResult(detailedIntent, CALL_BY_SIMPLE_BOOK_DATA)
            }
        }

        val recyclerViewAdapter = SearchedBooksAdapter(onSearchedItemClickListener)

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                val itemTotalCount = recyclerViewAdapter.itemCount - 1
                if (lastVisibleItemPosition == itemTotalCount) {
                    bookViewModel.searchMoreBooks()
                }
            }
        }

        searchResultRecyclerView.run {
            addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
            addOnScrollListener(scrollListener)
            layoutManager = LinearLayoutManager(activity)
            adapter = recyclerViewAdapter
        }
    }

    private fun observeEvents() {
        bookViewModel.searchedResult.observe(viewLifecycleOwner, Observer {
            val adapter = searchResultRecyclerView.adapter
            if (adapter is SearchedBooksAdapter) {
                adapter.bookList = it
            }
        })

        bookViewModel.isSearchingLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                showLoading()
            } else {
                hindLoading()
            }
        })
    }

    private fun showLoading() {
        searchButton.visibility = View.GONE

        searchProgress.visibility = View.VISIBLE
    }

    private fun hindLoading() {
        searchButton.visibility = View.VISIBLE

        searchProgress.visibility = View.GONE
    }
}
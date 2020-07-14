package com.kakao.book_service_android.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kakao.book_service_android.R
import com.kakao.book_service_android.network.model.search.SimpleBook
import kotlinx.android.synthetic.main.book_list_item.view.*

class SearchedBooksAdapter(val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<SearchedBooksAdapter.BookListViewHolder>() {

    var bookList = ArrayList<SimpleBook>()
        set(shops) {
            field = shops

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        return BookListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false))
    }

    override fun getItemCount() = bookList.size

    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    interface OnItemClickListener {
        fun onItemClick(clickedBook: SimpleBook)
    }

    inner class BookListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(simpleBook: SimpleBook) {
            bindBookTitle(simpleBook.title)

            bindBookSubTitle(simpleBook.subTitle)

            bindBookIsbn13(simpleBook.isbn13)

            bindBookPrice(simpleBook.price)

            bindBookUrl(simpleBook.bookUrl)

            bindImage(simpleBook.imageUrl)

            initListener(simpleBook)
        }

        private fun initListener(simpleBook: SimpleBook) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(simpleBook)
            }
        }

        private fun bindBookTitle(title: String) {
            itemView.bookTitle.text = title
        }

        private fun bindBookSubTitle(subTitle: String) {
            itemView.bookSubTitle.text = subTitle
        }

        private fun bindBookIsbn13(isbn13: String) {
            itemView.bookIsbn13.text = isbn13
        }

        private fun bindBookPrice(price: String) {
            itemView.bookPrice.text = price
        }

        private fun bindBookUrl(url: String) {
            itemView.bookUrl.text = url
        }

        private fun bindImage(imageUrl: String) {
            Glide.with(itemView.context).load(imageUrl)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.empty_image)
                .into(itemView.bookImage)
        }
    }
}
package com.kakao.book_service_android.ui.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kakao.book_service_android.R
import com.kakao.book_service_android.network.model.bookdetails.DetailedBook
import kotlinx.android.synthetic.main.book_mark_list_item.view.*

class BookMarkListAdapter(val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<BookMarkListAdapter.BookMarkListViewHolder>() {

    var bookMarkList = ArrayList<DetailedBook>()
        set(bookMarkList) {
            field = bookMarkList

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkListViewHolder {
        return BookMarkListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_mark_list_item, parent, false))
    }

    override fun getItemCount() = bookMarkList.size

    override fun onBindViewHolder(holder: BookMarkListViewHolder, position: Int) {
        holder.bind(bookMarkList[position])
    }

    interface OnItemClickListener {
        fun onItemClick(clickedBook: DetailedBook)
    }

    inner class BookMarkListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(bookMarkedBook: DetailedBook) {
            bindBookMarkTitle(bookMarkedBook.title)

            bindBookMarkSubTitle(bookMarkedBook.subtitle)

            bindBookMarkIsbn13(bookMarkedBook.isbn13)

            bindBookMarkPrice(bookMarkedBook.price)

            bindBookMarkUrl(bookMarkedBook.url)

            bindBookMarkImage(bookMarkedBook.imageUrl)

            initListener(bookMarkedBook)
        }

        private fun initListener(bookMarkedBook: DetailedBook) {
            itemView.setOnClickListener {

                itemClickListener.onItemClick(bookMarkedBook)
            }
        }

        private fun bindBookMarkImage(imageUrl: String) {
            Glide.with(itemView.context).load(imageUrl)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.empty_image)
                .into(itemView.bookMarkImage)
        }

        private fun bindBookMarkTitle(title: String) {
            itemView.bookMarkTitle.text = title
        }

        private fun bindBookMarkSubTitle(subTitle: String) {
            itemView.bookMarkSubTitle.text = subTitle
        }

        private fun bindBookMarkIsbn13(isbn13: String) {
            itemView.bookMarkIsbn13.text = isbn13
        }

        private fun bindBookMarkPrice(price: String) {
            itemView.bookMarkPrice.text = price
        }

        private fun bindBookMarkUrl(url: String) {
            itemView.bookMarkUrl.text = url
        }
    }
}
package com.kakao.book_service_android.ui

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kakao.book_service_android.AppComponents
import com.kakao.book_service_android.R
import com.kakao.book_service_android.ui.bookmark.BookMarkFragment
import com.kakao.book_service_android.ui.search.BooksFragment

class MainTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentTitleList = AppComponents.applicationContext.resources.getStringArray(R.array.book_tab_items)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> BookMarkFragment.instance
            else -> BooksFragment.instance
        }
    }

    override fun getCount() = fragmentTitleList.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }
}
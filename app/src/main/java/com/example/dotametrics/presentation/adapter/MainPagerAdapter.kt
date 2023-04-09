package com.example.dotametrics.presentation.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.dotametrics.R
import com.example.dotametrics.presentation.view.main.FavoritesFragment
import com.example.dotametrics.presentation.view.main.SearchFragment

class MainPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SearchFragment.newInstance()
            1 -> FavoritesFragment.newInstance()
            else -> throw RuntimeException("Incorrect position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount() = 2

    companion object {
        private val TAB_TITLES = arrayOf(
            R.string.tab_main_1,
            R.string.tab_main_2
        )
    }
}
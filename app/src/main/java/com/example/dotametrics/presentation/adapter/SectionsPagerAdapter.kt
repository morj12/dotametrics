package com.example.dotametrics.presentation.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.dotametrics.R
import com.example.dotametrics.presentation.view.account.HeroesFragment
import com.example.dotametrics.presentation.view.account.PeersFragment
import com.example.dotametrics.presentation.view.account.StatsFragment
import com.example.dotametrics.presentation.view.account.TotalsFragment

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> StatsFragment.newInstance(position + 1)
            1 -> HeroesFragment.newInstance()
            2 -> PeersFragment.newInstance()
            3 -> TotalsFragment.newInstance()
            else -> throw RuntimeException("Incorrect position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount() = TAB_TITLES.size

    companion object {
        private val TAB_TITLES = arrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3,
            R.string.tab_text_4
        )
    }
}
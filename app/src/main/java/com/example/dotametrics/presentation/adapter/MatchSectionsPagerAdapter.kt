package com.example.dotametrics.presentation.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.dotametrics.R
import com.example.dotametrics.presentation.view.match.MatchStatsFragment
import com.example.dotametrics.presentation.view.account.PeersFragment
import com.example.dotametrics.presentation.view.match.MatchOverviewFragment

class MatchSectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> MatchOverviewFragment.newInstance()
            1 -> MatchStatsFragment.newInstance()
            2 -> PeersFragment.newInstance()
            else -> throw RuntimeException("Incorrect position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount() = 2

    companion object {
        private val TAB_TITLES = arrayOf(
            R.string.tab_match_1,
            R.string.tab_match_2,
            R.string.tab_match_3,
        )
    }
}
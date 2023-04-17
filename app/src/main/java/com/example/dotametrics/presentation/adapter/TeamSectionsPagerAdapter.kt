package com.example.dotametrics.presentation.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.dotametrics.R
import com.example.dotametrics.presentation.view.team.TeamHeroesFragment
import com.example.dotametrics.presentation.view.team.TeamMatchesFragment
import com.example.dotametrics.presentation.view.team.TeamPlayersFragment

class TeamSectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TeamPlayersFragment.newInstance()
            1 -> TeamMatchesFragment.newInstance()
            2 -> TeamHeroesFragment.newInstance()
            else -> throw RuntimeException("Incorrect position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount() = 3

    companion object {
        private val TAB_TITLES = arrayOf(
            R.string.tab_team_1,
            R.string.tab_team_2,
            R.string.tab_team_3
        )
    }
}
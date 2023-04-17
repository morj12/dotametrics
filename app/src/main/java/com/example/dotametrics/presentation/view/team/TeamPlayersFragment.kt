package com.example.dotametrics.presentation.view.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dotametrics.R

class TeamPlayersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_players, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TeamPlayersFragment()
    }
}
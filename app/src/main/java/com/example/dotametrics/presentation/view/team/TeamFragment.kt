package com.example.dotametrics.presentation.view.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dotametrics.databinding.FragmentTeamBinding
import com.example.dotametrics.domain.entity.remote.teams.TeamsResult
import com.example.dotametrics.presentation.adapter.TeamSectionsPagerAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamFragment : Fragment() {
    private var team: TeamsResult? = null

    private val viewModel: TeamViewModel by viewModels()

    private var _binding: FragmentTeamBinding? = null
    private val binding: FragmentTeamBinding
        get() = _binding ?: throw RuntimeException("FragmentTeamBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            team = it.getParcelable("team")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
        if (team != null) {
            viewModel.setTeam(team!!)
        }
        observe()
    }

    private fun initTabs() {
        val teamPagerAdapter = TeamSectionsPagerAdapter(requireContext(), childFragmentManager)
        binding.viewPagerTeam.adapter = teamPagerAdapter
        binding.viewPagerTeam.offscreenPageLimit = 3
        binding.tabsTeam.setupWithViewPager(binding.viewPagerTeam)
    }

    private fun showData(team: TeamsResult) = with(binding) {
        com.bumptech.glide.Glide.with(root)
            .load(team.logoUrl)
            .apply(com.example.dotametrics.util.GlideManager.requestOptions(root.context))
            .into(teamImage)
        tvTeamName.text = team.name
        tvTeamRating.text = root.context.getString(com.example.dotametrics.R.string.team_rating, team.rating.toString())
        tvTeamWinsNumber.text = team.wins.toString()
        tvTeamLosesNumber.text = team.losses.toString()
        if (team.wins != null && team.losses != null) {
            val winrate =
                team.wins!!.toDouble() / (team.losses!! + team.wins!!).toDouble() * 100
            tvTeamWinrateNumber.text = "${kotlin.String.format("%.2f", winrate)}%"
        }
    }

    private fun observe() {
        viewModel.team.observe(viewLifecycleOwner) {
            showData(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
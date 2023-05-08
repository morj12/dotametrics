package com.example.dotametrics.presentation.view.team

import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.data.remote.model.teams.TeamsResult
import com.example.dotametrics.databinding.ActivityTeamBinding
import com.example.dotametrics.presentation.adapter.TeamSectionsPagerAdapter
import com.example.dotametrics.presentation.view.DrawerActivity
import com.example.dotametrics.util.GlideManager.requestOptions
import com.google.android.material.snackbar.Snackbar

class TeamActivity : DrawerActivity() {

    private lateinit var binding: ActivityTeamBinding

    private val viewModel: TeamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allocateActivityTitle(getString(R.string.teams))

        initTabs()
        val team = intent.getParcelableExtra<TeamsResult>("team")
        if (team != null) {
            viewModel.setTeam(team)
        }
        observe()
    }

    private fun initTabs() {
        val teamPagerAdapter = TeamSectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPagerTeam.adapter = teamPagerAdapter
        binding.viewPagerTeam.offscreenPageLimit = 3
        binding.tabsTeam.setupWithViewPager(binding.viewPagerTeam)
    }

    private fun showData(team: TeamsResult) = with(binding) {
        Glide.with(root)
            .load(team.logoUrl)
            .apply(requestOptions(root.context))
            .into(teamImage)
        tvTeamName.text = team.name
        tvTeamRating.text = root.context.getString(R.string.team_rating, team.rating.toString())
        tvTeamWinsNumber.text = team.wins.toString()
        tvTeamLosesNumber.text = team.losses.toString()
        if (team.wins != null && team.losses != null) {
            val winrate =
                team.wins!!.toDouble() / (team.losses!! + team.wins!!).toDouble() * 100
            tvTeamWinrateNumber.text = "${String.format("%.2f", winrate)}%"
        }
    }

    private fun observe() {
        viewModel.team.observe(this) {
            showData(it)
        }
        viewModel.error.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }
}
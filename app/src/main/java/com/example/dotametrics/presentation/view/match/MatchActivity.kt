package com.example.dotametrics.presentation.view.match

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.R
import com.example.dotametrics.databinding.ActivityMatchBinding
import com.example.dotametrics.presentation.adapter.MatchSectionsPagerAdapter
import com.example.dotametrics.presentation.view.DrawerActivity
import com.example.dotametrics.util.*
import com.google.android.material.snackbar.Snackbar

class MatchActivity : DrawerActivity() {

    private lateinit var binding: ActivityMatchBinding

    private val viewModel: MatchViewModel by lazy {
        ViewModelProvider(this)[MatchViewModel::class.java]
    }

    private var id: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val from = intent.getStringExtra("from")
        if (from == "teams") {
            allocateActivityTitle(getString(R.string.teams))
        } else {
            allocateActivityTitle(getString(R.string.players))
        }

        initTabs()

        id = intent.getLongExtra("id", 0L)
        if (id != 0L) {
            viewModel.matchId = id.toString()
            initConstants()
            binding.tvMatchResult.startLoading(binding.pbTvMatchResult)
            observe()
        }
    }

    private fun initConstants() {
        viewModel.loadRegions()
        viewModel.loadItems()
        viewModel.loadAbilityIds()
        viewModel.loadAbilities()
    }

    private fun loadData() {
        val regionsLoaded = ConstData.regions.isNotEmpty()
        val itemsLoaded = ConstData.items.isNotEmpty()

        if (regionsLoaded && itemsLoaded) {
            if (viewModel.result.value == null) viewModel.loadMatch()
        } else {
            if (!regionsLoaded) viewModel.loadRegions()
            if (!itemsLoaded) viewModel.loadItems()
        }
    }

    private fun initTabs() {
        val sectionsPagerAdapter = MatchSectionsPagerAdapter(this, supportFragmentManager)
        binding.matchViewPager.adapter = sectionsPagerAdapter
        binding.matchViewPager.offscreenPageLimit = 3
        binding.matchTabs.setupWithViewPager(binding.matchViewPager)
    }

    private fun observe() = with(binding) {
        viewModel.constRegions.observe(this@MatchActivity) {
            loadData()
        }
        viewModel.constItems.observe(this@MatchActivity) {
            loadData()
        }
        viewModel.result.observe(this@MatchActivity) {
            tvMatchDuration.text = Datetime.getStringTime(it.duration!!)
            tvMatchDatetime.text = Datetime.getDateTime(it.startTime!!)
            tvMatchRadiantPoints.text = it.radiantScore.toString()
            tvMatchDirePoints.text = it.direScore.toString()
            tvMatchResult.text =
                if (it.radiantWin!!) root.context.getString(R.string.radiant_won)
                else root.context.getString(R.string.dire_won)
            tvMatchLobbyType.text =
                getString(
                    LobbyTypeMapper.getLobbyResource(
                        ConstData.lobbies.first { lobby -> it.lobbyType == lobby.id }.name!!,
                        this@MatchActivity
                    )
                )
            tvMatchRegion.text = ConstData.regions[it.region]
            binding.tvMatchResult.stopLoading(binding.pbTvMatchResult)
        }
        viewModel.error.observe(this@MatchActivity) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

}

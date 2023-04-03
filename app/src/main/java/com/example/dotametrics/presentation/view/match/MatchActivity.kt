package com.example.dotametrics.presentation.view.match

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.R
import com.example.dotametrics.databinding.ActivityMatchBinding
import com.example.dotametrics.presentation.adapter.MatchSectionsPagerAdapter
import com.example.dotametrics.util.ConstData
import com.example.dotametrics.util.Datetime
import com.example.dotametrics.util.LobbyTypeMapper
import com.google.android.material.snackbar.Snackbar

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding

    private val viewModel: MatchViewModel by lazy {
        ViewModelProvider(this)[MatchViewModel::class.java]
    }

    private var id: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabs()

        id = intent.getLongExtra("id", 0L)
        if (id != 0L) {
            viewModel.matchId = id.toString()
            initConstants()
            observe()
        }
    }

    private fun initConstants() {
        viewModel.loadRegions()
        viewModel.loadItems()
    }

    private fun loadData() {
        val regionsLoaded = ConstData.regions.isNotEmpty()
        val itemsLoaded = ConstData.items.isNotEmpty()

        if (regionsLoaded && itemsLoaded) {
            viewModel.loadMatch(id.toString())
        } else {
            if (!regionsLoaded) viewModel.loadRegions()
            if (!itemsLoaded) viewModel.loadItems()
        }
    }

    private fun initTabs() {
        val sectionsPagerAdapter = MatchSectionsPagerAdapter(this, supportFragmentManager)
        binding.matchViewPager.adapter = sectionsPagerAdapter
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
            /**
             * skills: skill builds
             */
        }
        viewModel.error.observe(this@MatchActivity) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

}

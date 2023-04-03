package com.example.dotametrics.presentation.view.match

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.R
import com.example.dotametrics.databinding.ActivityMatchBinding
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

        id = intent.getLongExtra("id", 0L)
        if (id != 0L) {
            viewModel.matchId = id.toString()
            viewModel.loadRegions()
            observe()
        }
    }

    private fun observe() = with(binding) {
        viewModel.constRegions.observe(this@MatchActivity) {
            viewModel.loadMatch(id.toString())
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
             * maybe: parse button
             *
             * main: picture, name, rank, kda, items, perm buffs
             * stats: lvl, gold, last hits, denies, gpm, xpm, dmg, dmg towers
             * skills: skill builds
             */
        }
        viewModel.error.observe(this@MatchActivity) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

}

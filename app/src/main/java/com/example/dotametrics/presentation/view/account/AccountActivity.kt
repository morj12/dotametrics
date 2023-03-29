package com.example.dotametrics.presentation.view.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.data.model.players.PlayersResult
import com.example.dotametrics.data.model.players.wl.WLResult
import com.example.dotametrics.databinding.ActivityAccountBinding
import com.example.dotametrics.presentation.adapter.SectionsPagerAdapter
import com.google.android.material.snackbar.Snackbar

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    private val viewModel: AccountViewModel by lazy {
        ViewModelProvider(this)[AccountViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabs()
        val id = intent.getIntExtra("id", 0)
        if (id != 0) {
            viewModel.userId = id.toString()
            viewModel.loadUser(id.toString())
        }
        observe()
    }

    private fun initTabs() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    private fun observe() {
        viewModel.result.observe(this) {
            showData(it)
        }
        viewModel.wl.observe(this) {
            showData(it)
        }
        viewModel.error.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showData(player: PlayersResult) = with(binding) {
        Glide.with(this@AccountActivity)
            .load(player.profile?.avatarfull)
            .placeholder(R.drawable.ic_person)
            .into(profileImage)
        tvAccountName.text = player.profile?.personaname
        tvAccountId.text = player.profile?.accountId.toString()
        ivAccountDotaplus.visibility = if (player.profile?.plus == true) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
        setRank(player)
    }


    private fun showData(player: WLResult) = with(binding) {
        tvAccountWinsNumber.text = player.win.toString()
        tvAccountLosesNumber.text = player.lose.toString()
        if (player.win != null && player.lose != null) {
            val winrate =
                player.win!!.toDouble() / (player.lose!! + player.win!!).toDouble() * 100
            tvAccountWinrateNumber.text = "${String.format("%.2f", winrate)}%"
        }
    }

    private fun setRank(player: PlayersResult) = with(binding) {
        val id = when {
            player.rankTier == null -> R.drawable.r00
            player.leaderboardRank == null -> resources.getIdentifier(
                "r${player.rankTier}",
                "drawable",
                packageName
            )
            player.leaderboardRank!!.toInt() <= 100 -> {
                tvAccountPosition.text = player.leaderboardRank
                R.drawable.r81
            }
            else -> {
                tvAccountPosition.text = player.leaderboardRank
                R.drawable.r82
            }
        }
        binding.ivAccountRank.setImageResource(id)
    }

}
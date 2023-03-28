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
import com.example.dotametrics.presentation.view.adapter.SectionsPagerAdapter
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
        if (id != 0) viewModel.loadUser(id.toString())
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

    private fun showData(player: PlayersResult) =
        with(binding) {
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
        }


    private fun showData(player: WLResult) =
        with(binding) {
            tvAccountWinsNumber.text = player.win.toString()
            tvAccountLosesNumber.text = player.lose.toString()
            if (player.win != null && player.lose != null) {
                val winrate =
                    player.win!!.toDouble() / (player.lose!! + player.win!!).toDouble() * 100
                tvAccountWinrateNumber.text = "${String.format("%.2f", winrate)}%"
            }
        }

}
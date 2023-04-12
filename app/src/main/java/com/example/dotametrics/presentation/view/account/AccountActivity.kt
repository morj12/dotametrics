package com.example.dotametrics.presentation.view.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.data.db.dbmodel.PlayerDbModel
import com.example.dotametrics.data.model.players.PlayersResult
import com.example.dotametrics.data.model.players.wl.WLResult
import com.example.dotametrics.databinding.ActivityAccountBinding
import com.example.dotametrics.presentation.adapter.SectionsPagerAdapter
import com.example.dotametrics.util.GlideRequestOptions.requestOptions
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    private val viewModel: AccountViewModel by viewModels {
        AccountViewModel.AccountViewModelFactory(applicationContext as App)
    }

    private var isFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabs()
        initConstants()
        initListeners()

        val id = intent.getLongExtra("id", 0L)
        if (id != 0L) {
            viewModel.userId = id.toString()
            if (viewModel.result.value == null && viewModel.wl.value == null) viewModel.loadUser()
            binding.profileImage.startLoading(binding.pbProfileImage)
            binding.tvAccountLosesNumber.startLoading(binding.pbTvAccountLoses)
            viewModel.checkFavorite(id)
        }
        observe()
    }

    private fun initTabs() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.viewPager.offscreenPageLimit = 3
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    private fun initConstants() {
        viewModel.loadHeroes()
        viewModel.loadLobbyTypes()
    }

    private fun initListeners() {
        binding.ivAccountFav.setOnClickListener {
            val currentPlayer = viewModel.result.value
            if (currentPlayer != null) {
                if (!isFav) {
                    insertPlayer(true)
                } else {
                    viewModel.deletePlayer(currentPlayer.profile!!.accountId!!.toLong())
                }
            }
        }
    }

    private fun insertPlayer(observe: Boolean) {
        val currentPlayer = viewModel.result.value!!
        viewModel.insertPlayer(
            PlayerDbModel(
                currentPlayer.profile!!.accountId!!.toLong(),
                currentPlayer.profile!!.personaname,
                currentPlayer.profile!!.avatarfull
            ),
            observe
        )
    }

    private fun observe() {
        viewModel.result.observe(this) {
            showData(it)
            binding.profileImage.stopLoading(binding.pbProfileImage)
            updateFavorite()
        }
        viewModel.wl.observe(this) {
            showData(it)
            binding.tvAccountLosesNumber.stopLoading(binding.pbTvAccountLoses)
        }
        viewModel.error.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.isFav.observe(this) {
            isFav = it
            if (isFav) {
                updateFavorite()
                binding.ivAccountFav.setImageResource(R.drawable.ic_fav)
            } else {
                binding.ivAccountFav.setImageResource(R.drawable.ic_fav_border)
            }
        }
    }

    private fun updateFavorite() {
        if (viewModel.result.value != null && isFav)
            insertPlayer(false)
    }

    private fun showData(player: PlayersResult) = with(binding) {
        Glide.with(this@AccountActivity)
            .load(player.profile?.avatarfull)
            .apply(requestOptions())
            .into(profileImage)
        tvAccountName.text = player.profile?.personaname
        setRank(player)
        tvAccountId.text = player.profile?.accountId.toString()
        Glide.with(this@AccountActivity)
            .load("https://flagcdn.com/w80/${player.profile?.loccountrycode?.lowercase()}.png")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(ivAccountFlag)
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
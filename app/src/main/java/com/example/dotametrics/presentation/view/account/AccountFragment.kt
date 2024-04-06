package com.example.dotametrics.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dotametrics.R
import com.example.dotametrics.databinding.FragmentAccountBinding
import com.example.dotametrics.domain.entity.local.PlayerDbModel
import com.example.dotametrics.domain.entity.remote.players.PlayersResult
import com.example.dotametrics.domain.entity.remote.players.wl.WLResult
import com.example.dotametrics.presentation.adapter.SectionsPagerAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.util.GlideManager
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding ?: throw RuntimeException("FragmentAccountBinding is null")

    private val viewModel: AccountViewModel by viewModels<AccountViewModel>()

    private val constViewModel: ConstViewModel by activityViewModels()

    private var isFav = false

    private var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getLong("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabs()
        initConstants()
        initListeners()

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
        val sectionsPagerAdapter = SectionsPagerAdapter(requireContext(), childFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.viewPager.offscreenPageLimit = 3
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    private fun initConstants() {
        constViewModel.loadHeroes()
        constViewModel.loadLobbyTypes()
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
        viewModel.result.observe(viewLifecycleOwner) {
            showData(it)
            binding.profileImage.stopLoading(binding.pbProfileImage)
            updateFavorite()
        }
        viewModel.wl.observe(viewLifecycleOwner) {
            showData(it)
            binding.tvAccountLosesNumber.stopLoading(binding.pbTvAccountLoses)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        constViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.isFav.observe(viewLifecycleOwner) {
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
        Glide.with(this@AccountFragment)
            .load(player.profile?.avatarfull)
            .apply(GlideManager.requestOptions(root.context))
            .into(profileImage)
        tvAccountName.text = player.profile?.personaname
        setRank(player)
        tvAccountId.text = player.profile?.accountId.toString()
        Glide.with(this@AccountFragment)
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
                requireActivity().packageName
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
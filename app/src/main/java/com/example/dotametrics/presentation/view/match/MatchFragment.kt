package com.example.dotametrics.presentation.view.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.dotametrics.R
import com.example.dotametrics.databinding.FragmentMatchBinding
import com.example.dotametrics.domain.ConstData
import com.example.dotametrics.presentation.adapter.MatchSectionsPagerAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.util.Datetime
import com.example.dotametrics.util.LobbyTypeMapper
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MatchFragment : Fragment() {

    private var _binding: FragmentMatchBinding? = null
    private val binding: FragmentMatchBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchBinding is null")

    private val viewModel: MatchViewModel by viewModels()

    private val constViewModel: ConstViewModel by activityViewModels()

    private var id: Long? = null

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
        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()

        if (id != 0L) {
            viewModel.matchId = id.toString()
            initConstants()
            binding.tvMatchResult.startLoading(binding.pbTvMatchResult)
            observe()
        }
    }

    private fun initConstants() {
        constViewModel.loadRegions()
        constViewModel.loadItems()
        constViewModel.loadAbilityIds()
        constViewModel.loadAbilities()
        constViewModel.loadLobbyTypes()
    }

    private fun loadData() {
        val regionsLoaded = ConstData.regions.isNotEmpty()
        val itemsLoaded = ConstData.items.isNotEmpty()
        val lobbiesLoaded = ConstData.lobbies.isNotEmpty()

        if (regionsLoaded && itemsLoaded && lobbiesLoaded) {
            if (viewModel.result.value == null) viewModel.loadMatch()
        } else {
            if (!regionsLoaded) constViewModel.loadRegions()
            if (!itemsLoaded) constViewModel.loadItems()
            if (!lobbiesLoaded) constViewModel.loadLobbyTypes()
        }
    }

    private fun initTabs() {
        val sectionsPagerAdapter = MatchSectionsPagerAdapter(requireContext(), childFragmentManager)
        binding.matchViewPager.adapter = sectionsPagerAdapter
        binding.matchViewPager.offscreenPageLimit = 3
        binding.matchTabs.setupWithViewPager(binding.matchViewPager)
    }

    private fun observe() = with(binding) {
        constViewModel.constRegions.observe(viewLifecycleOwner) {
            loadData()
        }
        constViewModel.constItems.observe(viewLifecycleOwner) {
            loadData()
        }
        constViewModel.constLobbyTypes.observe(viewLifecycleOwner) {
            loadData()
        }
        viewModel.result.observe(viewLifecycleOwner) {
            tvMatchDuration.text = Datetime.getStringTime(it.duration!!)
            tvMatchDatetime.text = Datetime.formatDate(it.startTime!!)
            tvMatchRadiantPoints.text = it.radiantScore.toString()
            tvMatchDirePoints.text = it.direScore.toString()
            tvMatchResult.text =
                if (it.radiantWin!!) root.context.getString(R.string.radiant_won)
                else root.context.getString(R.string.dire_won)
            tvMatchLobbyType.text =
                getString(
                    LobbyTypeMapper().getLobbyResource(
                        ConstData.lobbies.first { lobby -> it.lobbyType == lobby.id }.name!!,
                        requireContext()
                    )
                )
            tvMatchRegion.text = ConstData.regions[it.region]
            binding.tvMatchResult.stopLoading(binding.pbTvMatchResult)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        constViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
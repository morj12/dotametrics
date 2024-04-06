package com.example.dotametrics.presentation.view.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.players.matches.MatchesResult
import com.example.dotametrics.databinding.FragmentMatchesBinding
import com.example.dotametrics.presentation.adapter.MatchesResultAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.domain.ConstData
import com.example.dotametrics.presentation.view.match.MatchFragment
import com.example.dotametrics.util.LobbyTypeMapper
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchesFragment : Fragment() {

    private var _binding: FragmentMatchesBinding? = null
    private val binding: FragmentMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchesBinding is null")

    private lateinit var adapter: MatchesResultAdapter

    private val viewModel: AccountViewModel by viewModels<AccountViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    private val constViewModel: ConstViewModel by activityViewModels()

    private val openMatch: (MatchesResult) -> Unit = {
        val bundle = Bundle().apply {
            putLong("id", it.matchId ?: 0)
        }
        findNavController().navigate(R.id.action_accountFragment_to_matchFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcMatches.startLoading(binding.pbRcMatches)
        initRecyclerView()
        initButtons()
        observe()
    }

    private fun loadData(reload: Boolean = false) {
        val heroesLoaded = ConstData.heroes.isNotEmpty()
        val lobbiesLoaded = ConstData.lobbies.isNotEmpty()

        if (heroesLoaded && lobbiesLoaded) {
            if (viewModel.matches.value == null || reload) {
                viewModel.loadMatches()
                viewModel.loadFilteredWLResults()
            }
            observeMatches()
        } else {
            if (!heroesLoaded) constViewModel.loadHeroes()
            if (!lobbiesLoaded) constViewModel.loadLobbyTypes()
        }
    }

    private fun initLobbyFilter() {
        val data = mutableListOf(getString(R.string.any_lobby))
        data.addAll(ConstData.lobbies.map {
            requireContext().getString(
                LobbyTypeMapper().getLobbyResource(it.name!!, requireContext())
            )
        })

        binding.spinnerLobby.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_item,
            data
        )
        binding.spinnerLobby.setSelection(0, false)
    }

    private fun initHeroFilter() {
        val data = mutableListOf("Hero")
        data.addAll(ConstData.heroes.map { it.localizedName!! })
        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_item,
            data
        )
        binding.spinnerHeroId.adapter = adapter
    }

    private fun initRecyclerView() = with(binding) {
        adapter = MatchesResultAdapter()
        rcMatches.layoutManager = LinearLayoutManager(activity)
        rcMatches.adapter = adapter
        adapter.onItemClickedListener = openMatch
    }

    private fun initButtons() {
        binding.btApplyFilter.setOnClickListener {
            val position = binding.spinnerLobby.selectedItemPosition
            val lobbyType = if (position == 0) null else ConstData.lobbies[position - 1].id
            viewModel.lobbyType = lobbyType
            val heroPosition = binding.spinnerHeroId.selectedItemPosition
            val heroId = if (heroPosition == 0) null else ConstData.heroes[heroPosition - 1].id
            viewModel.heroId = heroId
            loadData(true)
        }
    }

    private fun observe() {
        constViewModel.heroes.observe(viewLifecycleOwner) {
            loadData()
            initHeroFilter()
        }
        constViewModel.constLobbyTypes.observe(viewLifecycleOwner) {
            loadData()
            initLobbyFilter()
        }
        constViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.filteredWl.observe(viewLifecycleOwner) { player ->
            with(binding) {
                tvAccountWinsNumberFilter.text = player.win.toString()
                tvAccountLosesNumberFilter.text = player.lose.toString()
                if (player.win != null && player.lose != null) {
                    val winrate =
                        player.win!!.toDouble() / (player.lose!! + player.win!!).toDouble() * 100
                    tvAccountWinrateNumberFilter.text = "${String.format("%.2f", winrate)}%"
                }
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observeMatches() {
        viewModel.matches.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcMatches.scrollToPosition(0)
                binding.rcMatches.stopLoading(binding.pbRcMatches)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): MatchesFragment {
            return MatchesFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
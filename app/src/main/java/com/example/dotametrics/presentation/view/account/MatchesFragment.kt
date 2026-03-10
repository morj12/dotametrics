package com.example.dotametrics.presentation.view.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.paging.LoadState
import com.example.dotametrics.R
import com.example.dotametrics.databinding.FragmentMatchesBinding
import com.example.dotametrics.domain.ConstData
import com.example.dotametrics.domain.entity.remote.players.matches.MatchesResult
import com.example.dotametrics.presentation.adapter.MatchesResultAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.util.LobbyTypeMapper
import com.example.dotametrics.util.showError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        initRecyclerView()
        initButtons()
        observe()
        observeMatches()
    }

    private fun loadData() {
        val heroesLoaded = ConstData.heroes.isNotEmpty()
        val lobbiesLoaded = ConstData.lobbies.isNotEmpty()

        if (heroesLoaded && lobbiesLoaded) {
            viewModel.loadMatches()
            viewModel.loadFilteredWLResults()
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    val isInitialLoading = loadStates.refresh is LoadState.Loading
                    pbRcMatches.isVisible = isInitialLoading
                    rcMatches.isVisible = !isInitialLoading

                    val errorState = loadStates.refresh as? LoadState.Error
                        ?: loadStates.append as? LoadState.Error
                        ?: loadStates.prepend as? LoadState.Error

                    errorState?.let {
                        showError(it.error.message ?: "", root)
                    }
                }
            }
        }
    }

    private fun initButtons() {
        binding.btApplyFilter.setOnClickListener {
            val position = binding.spinnerLobby.selectedItemPosition
            val lobbyType = if (position == 0) null else ConstData.lobbies[position - 1].id
            viewModel.lobbyType = lobbyType
            val heroPosition = binding.spinnerHeroId.selectedItemPosition
            val heroId = if (heroPosition == 0) null else ConstData.heroes[heroPosition - 1].id
            viewModel.heroId = heroId
            loadData()
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
    }

    private fun observeMatches() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.matchesFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
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

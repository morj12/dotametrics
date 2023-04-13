package com.example.dotametrics.presentation.view.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.data.model.players.matches.MatchesResult
import com.example.dotametrics.databinding.FragmentMatchesBinding
import com.example.dotametrics.presentation.adapter.MatchesResultAdapter
import com.example.dotametrics.presentation.view.match.MatchActivity
import com.example.dotametrics.util.ConstData
import com.example.dotametrics.util.LobbyTypeMapper
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar

class MatchesFragment : Fragment() {

    private var _binding: FragmentMatchesBinding? = null
    private val binding: FragmentMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchesBinding is null")

    private lateinit var adapter: MatchesResultAdapter

    private val viewModel: AccountViewModel by activityViewModels {
        AccountViewModel.AccountViewModelFactory((context?.applicationContext as App))
    }

    private val openMatch: (MatchesResult) -> Unit = {
        val intent = Intent(requireActivity(), MatchActivity::class.java)
        intent.putExtra("id", it.matchId)
        startActivity(intent)
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
                viewModel.loadMatches { observeMatches() }
                viewModel.loadFilteredWLResults()
            } else {
                observeMatches()
            }
        } else {
            if (!heroesLoaded) viewModel.loadHeroes()
            if (!lobbiesLoaded) viewModel.loadLobbyTypes()
        }
    }

    private fun initLobbyFilter() {
        val data = mutableListOf(getString(R.string.any_lobby))
        data.addAll(ConstData.lobbies.map {
            requireContext().getString(
                LobbyTypeMapper.getLobbyResource(it.name!!, requireContext())
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
        viewModel.constHeroes.observe(viewLifecycleOwner) {
            loadData()
            initHeroFilter()
        }
        viewModel.constLobbyTypes.observe(viewLifecycleOwner) {
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
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observeMatches() {
        viewModel.matches.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcMatches.scrollToPosition(0)
            }
            binding.rcMatches.stopLoading(binding.pbRcMatches)
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
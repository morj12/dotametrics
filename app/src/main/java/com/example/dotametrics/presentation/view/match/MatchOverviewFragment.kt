package com.example.dotametrics.presentation.view.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.matches.Players
import com.example.dotametrics.databinding.FragmentMatchOverviewBinding
import com.example.dotametrics.presentation.adapter.MatchOverviewPlayerAdapter
import com.example.dotametrics.presentation.view.account.AccountFragment
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchOverviewFragment : Fragment() {

    private var _binding: FragmentMatchOverviewBinding? = null
    private val binding: FragmentMatchOverviewBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchOverviewBinding is null")

    private lateinit var radiantAdapter: MatchOverviewPlayerAdapter
    private lateinit var direAdapter: MatchOverviewPlayerAdapter

    private val viewModel: MatchViewModel  by viewModels<MatchViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    private val openAccount: (Players) -> Unit = {
        val bundle = Bundle().apply {
            putLong("id", it.accountId ?: 0)
        }
        findNavController().navigate(R.id.action_matchFragment_to_accountFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcMatchOverviewRadiant.startLoading(binding.pbRcMatchOverviewRadiant)
        binding.rcMatchOverviewDire.startLoading(binding.pbRcMatchOverviewDire)
        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() = with(binding) {
        radiantAdapter = MatchOverviewPlayerAdapter()
        rcMatchOverviewRadiant.layoutManager = LinearLayoutManager(activity)
        rcMatchOverviewRadiant.adapter = radiantAdapter

        radiantAdapter.onItemClickedListener = openAccount

        direAdapter = MatchOverviewPlayerAdapter()
        rcMatchOverviewDire.layoutManager = LinearLayoutManager(activity)
        rcMatchOverviewDire.adapter = direAdapter

        direAdapter.onItemClickedListener = openAccount
    }

    private fun observe() {
        viewModel.result.observe(viewLifecycleOwner) { match ->
            radiantAdapter.submitList(match.players.sortedBy { it.playerSlot }
                .filter { player -> player.playerSlot!! < 100 })
            direAdapter.submitList(match.players.sortedBy { it.playerSlot }
                .filter { player -> player.playerSlot!! >= 100 })
            binding.rcMatchOverviewRadiant.stopLoading(binding.pbRcMatchOverviewRadiant)
            binding.rcMatchOverviewDire.stopLoading(binding.pbRcMatchOverviewDire)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MatchOverviewFragment()
    }
}
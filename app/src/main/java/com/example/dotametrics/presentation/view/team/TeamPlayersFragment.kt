package com.example.dotametrics.presentation.view.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.teams.players.TeamPlayersResult
import com.example.dotametrics.databinding.FragmentTeamPlayersBinding
import com.example.dotametrics.presentation.adapter.TeamPlayersAdapter
import com.example.dotametrics.presentation.view.account.AccountFragment
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamPlayersFragment : Fragment() {

    private var _binding: FragmentTeamPlayersBinding? = null
    private val binding: FragmentTeamPlayersBinding
        get() = _binding ?: throw RuntimeException("FragmentTeamPlayersBinding is null")

    private val openAccount: (TeamPlayersResult) -> Unit = {
        val bundle = Bundle().apply {
            putLong("id", it.accountId ?: 0)
        }
        findNavController().navigate(R.id.action_teamFragment_to_accountFragment, bundle)
    }

    private val viewModel: TeamViewModel by viewModels<TeamViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    private lateinit var adapter: TeamPlayersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamPlayersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.players.value == null) viewModel.loadPlayers()
        binding.rcTeamPlayers.startLoading(binding.pbTeamPlayers)
        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = TeamPlayersAdapter()
        rcTeamPlayers.layoutManager = LinearLayoutManager(requireActivity())
        rcTeamPlayers.adapter = adapter
        adapter.onItemClickedListener = openAccount
    }

    private fun observe() {
        viewModel.players.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.rcTeamPlayers.stopLoading(binding.pbTeamPlayers)
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
        fun newInstance() = TeamPlayersFragment()
    }
}
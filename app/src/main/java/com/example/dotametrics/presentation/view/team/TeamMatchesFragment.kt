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
import com.example.dotametrics.domain.entity.remote.teams.matches.TeamMatchesResult
import com.example.dotametrics.databinding.FragmentTeamMatchesBinding
import com.example.dotametrics.presentation.adapter.TeamMatchesAdapter
import com.example.dotametrics.presentation.view.match.MatchFragment
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamMatchesFragment : Fragment() {

    private var _binding: FragmentTeamMatchesBinding? = null
    private val binding: FragmentTeamMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentTeamMatchesBinding is null")

    private val viewModel: TeamViewModel by viewModels<TeamViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    private val openMatch: (TeamMatchesResult) -> Unit = {
        val bundle = Bundle().apply {
            putLong("id", it.matchId ?: 0)
        }
        findNavController().navigate(R.id.action_teamFragment_to_matchFragment, bundle)
    }

    private lateinit var adapter: TeamMatchesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.matches.value == null) viewModel.loadMatches()
        binding.rcTeamMatches.startLoading(binding.pbTeamMatches)
        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = TeamMatchesAdapter()
        rcTeamMatches.layoutManager = LinearLayoutManager(requireActivity())
        rcTeamMatches.adapter = adapter
        adapter.onItemClickedListener = openMatch
    }

    private fun observe() {
        viewModel.matches.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.rcTeamMatches.stopLoading(binding.pbTeamMatches)
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
        fun newInstance() = TeamMatchesFragment()
    }
}
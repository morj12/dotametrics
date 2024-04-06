package com.example.dotametrics.presentation.view.teamsearch

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.R
import com.example.dotametrics.databinding.FragmentTeamSearchBinding
import com.example.dotametrics.domain.entity.remote.teams.TeamsResult
import com.example.dotametrics.presentation.adapter.TeamSearchAdapter
import com.example.dotametrics.presentation.view.team.TeamFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamSearchFragment : Fragment() {

    private var _binding: FragmentTeamSearchBinding? = null
    private val binding: FragmentTeamSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentTeamSearchBinding is null")

    private val viewModel: TeamSearchViewModel by viewModels()

    private lateinit var adapter: TeamSearchAdapter

    private val openTeam: (TeamsResult) -> Unit = {
        val bundle = Bundle().apply {
            putParcelable("team", it)
        }
        findNavController().navigate(R.id.action_teamSearchFragment_to_teamFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.teams.value == null) viewModel.loadTeams()
        initRecyclerView()
        initListeners()
        observe()
    }

    private fun initRecyclerView() {
        adapter = TeamSearchAdapter()
        val orientation: Int = binding.root.resources.configuration.orientation
        val layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(requireContext(), 2)
        } else {
            LinearLayoutManager(requireContext())
        }
        binding.rcTeams.layoutManager = layoutManager
        binding.rcTeams.adapter = adapter
        adapter.onItemClickedListener = openTeam
    }

    private fun initListeners() = with(binding) {
        btSearchTeams.setOnClickListener {
            binding.btSearchTeams.visibility = View.INVISIBLE
            binding.pbSearchTeams.visibility = View.VISIBLE
            viewModel.filterTeams(
                if (binding.edTeams.text.isNotBlank())
                    edTeams.text.toString()
                else null
            )
        }
        edTeams.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.btSearchTeams.visibility = View.INVISIBLE
                binding.pbSearchTeams.visibility = View.VISIBLE
                viewModel.filterTeams(
                    if (binding.edTeams.text.isNotBlank())
                        edTeams.text.toString()
                    else null
                )
            }
            true
        }
    }

    private fun loadData() {
        viewModel.filterTeams()
    }

    private fun observe() {
        viewModel.teams.observe(viewLifecycleOwner) {
            loadData()
        }
        viewModel.filteredTeams.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcTeams.scrollToPosition(0)
                binding.btSearchTeams.visibility = View.VISIBLE
                binding.pbSearchTeams.visibility = View.INVISIBLE
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            binding.btSearchTeams.visibility = View.VISIBLE
            binding.pbSearchTeams.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
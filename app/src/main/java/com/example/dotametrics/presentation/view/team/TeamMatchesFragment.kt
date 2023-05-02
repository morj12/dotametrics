package com.example.dotametrics.presentation.view.team

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.App
import com.example.dotametrics.data.model.teams.matches.TeamMatchesResult
import com.example.dotametrics.databinding.FragmentTeamMatchesBinding
import com.example.dotametrics.presentation.adapter.TeamMatchesAdapter
import com.example.dotametrics.presentation.view.match.MatchActivity
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar

class TeamMatchesFragment : Fragment() {

    private var _binding: FragmentTeamMatchesBinding? = null
    private val binding: FragmentTeamMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentTeamMatchesBinding is null")

    private val viewModel: TeamViewModel by activityViewModels()

    private val openMatch: (TeamMatchesResult) -> Unit = {
        val intent = Intent(requireActivity(), MatchActivity::class.java)
        intent.putExtra("id", it.matchId)
        intent.putExtra("from", "teams")
        startActivity(intent)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = TeamMatchesFragment()
    }
}
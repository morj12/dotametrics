package com.example.dotametrics.presentation.view.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.databinding.FragmentTeamHeroesBinding
import com.example.dotametrics.presentation.adapter.TeamHeroesAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamHeroesFragment : Fragment() {

    private var _binding: FragmentTeamHeroesBinding? = null
    private val binding: FragmentTeamHeroesBinding
        get() = _binding ?: throw RuntimeException("FragmentTeamHeroesBinding is null")

    private val viewModel: TeamViewModel by viewModels<TeamViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    private val constViewModel: ConstViewModel by activityViewModels()

    private lateinit var adapter: TeamHeroesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamHeroesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        constViewModel.loadHeroes()
        binding.rcTeamHeroes.startLoading(binding.pbTeamHeroes)
        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = TeamHeroesAdapter()
        rcTeamHeroes.layoutManager = LinearLayoutManager(requireActivity())
        rcTeamHeroes.adapter = adapter
    }

    private fun observe() {
        constViewModel.heroes.observe(viewLifecycleOwner) {
            loadData()
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        constViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun loadData() {
        if (viewModel.heroes.value == null) viewModel.loadHeroes()
        viewModel.heroes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.rcTeamHeroes.stopLoading(binding.pbTeamHeroes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = TeamHeroesFragment()
    }
}
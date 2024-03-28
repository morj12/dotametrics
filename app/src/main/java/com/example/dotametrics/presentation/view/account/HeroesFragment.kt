package com.example.dotametrics.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.databinding.FragmentHeroesBinding
import com.example.dotametrics.presentation.adapter.PlayerHeroesAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeroesFragment : Fragment() {

    private var _binding: FragmentHeroesBinding? = null
    private val binding: FragmentHeroesBinding
        get() = _binding ?: throw RuntimeException("FragmentHeroesBinding is null")

    private lateinit var adapter: PlayerHeroesAdapter

    private val viewModel: AccountViewModel by activityViewModels()

    private val constViewModel: ConstViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcPlayerHeroes.startLoading(binding.pbRcPlayerHeroes)
        initRecyclerView()
        observe()
    }

    private fun loadData() {
        if (viewModel.heroes.value == null) viewModel.loadPlayerHeroesResults()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = PlayerHeroesAdapter()
        rcPlayerHeroes.layoutManager = LinearLayoutManager(activity)
        rcPlayerHeroes.adapter = adapter
    }

    private fun observe() {
        constViewModel.heroes.observe(viewLifecycleOwner) {
            loadData()
        }
        viewModel.heroes.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcPlayerHeroes.scrollToPosition(0)
            }
            binding.rcPlayerHeroes.stopLoading(binding.pbRcPlayerHeroes)
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

    companion object {
        @JvmStatic
        fun newInstance() = HeroesFragment()
    }
}
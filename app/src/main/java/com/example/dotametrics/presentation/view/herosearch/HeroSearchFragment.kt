package com.example.dotametrics.presentation.view.herosearch

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
import com.example.dotametrics.R
import com.example.dotametrics.databinding.FragmentHeroSearchBinding
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.presentation.adapter.HeroSearchAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.presentation.view.hero.HeroFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeroSearchFragment : Fragment() {

    private var _binding: FragmentHeroSearchBinding? = null
    private val binding: FragmentHeroSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentHeroSearchBinding is null")

    private val viewModel: HeroSearchViewModel by viewModels()

    private val constViewModel: ConstViewModel by viewModels()

    private lateinit var adapter: HeroSearchAdapter

    private val openHero: (HeroResult) -> Unit = {
        val bundle = Bundle().apply {
            putParcelable("hero", it)
        }
        findNavController().navigate(R.id.action_heroSearchFragment_to_heroFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        constViewModel.loadHeroes()
        initRecyclerView()
        initListeners()
        observe()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = HeroSearchAdapter()
        val orientation: Int = binding.root.resources.configuration.orientation
        val layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(requireContext(), 5)
        } else {
            GridLayoutManager(requireContext(), 3)
        }
        binding.rcHeroes.layoutManager = layoutManager
        binding.rcHeroes.adapter = adapter
        adapter.onItemClickedListener = openHero
    }

    private fun initListeners() = with(binding) {
        btSearchHeroes.setOnClickListener {
            binding.btSearchHeroes.visibility = View.INVISIBLE
            binding.pbSearchHeroes.visibility = View.VISIBLE
            viewModel.filterHeroes(
                if (binding.edHeroes.text.isNotBlank())
                    edHeroes.text.toString()
                else null
            )
        }
        edHeroes.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.btSearchHeroes.visibility = View.INVISIBLE
                binding.pbSearchHeroes.visibility = View.VISIBLE
                viewModel.filterHeroes(
                    if (binding.edHeroes.text.isNotBlank())
                        edHeroes.text.toString()
                    else null
                )
            }
            true
        }
    }

    private fun loadData() {
        viewModel.filterHeroes()
    }

    private fun observe() {
        constViewModel.heroes.observe(viewLifecycleOwner) {
            loadData()
        }
        viewModel.filteredHeroes.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcHeroes.scrollToPosition(0)
                binding.btSearchHeroes.visibility = View.VISIBLE
                binding.pbSearchHeroes.visibility = View.INVISIBLE
            }
        }
        constViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            binding.btSearchHeroes.visibility = View.VISIBLE
            binding.pbSearchHeroes.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
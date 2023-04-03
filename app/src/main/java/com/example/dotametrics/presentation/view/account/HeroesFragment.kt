package com.example.dotametrics.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.databinding.FragmentHeroesBinding
import com.example.dotametrics.presentation.adapter.PlayerHeroesAdapter
import com.example.dotametrics.util.ConstData
import com.google.android.material.snackbar.Snackbar

class HeroesFragment : Fragment() {

    private var _binding: FragmentHeroesBinding? = null
    private val binding: FragmentHeroesBinding
        get() = _binding ?: throw RuntimeException("FragmentHeroesBinding is null")

    private lateinit var adapter: PlayerHeroesAdapter

    private val viewModel: AccountViewModel by lazy {
        ViewModelProvider(requireActivity())[AccountViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observe()
    }

    private fun loadData() {
        if (ConstData.heroes.isNotEmpty())
            viewModel.loadPlayerHeroesResults()
        else viewModel.loadHeroes()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = PlayerHeroesAdapter()
        rcPlayerHeroes.layoutManager = LinearLayoutManager(activity)
        rcPlayerHeroes.adapter = adapter
    }

    private fun observe() {
        viewModel.constHeroes.observe(viewLifecycleOwner) {
            loadData()
        }
        viewModel.heroes.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcPlayerHeroes.scrollToPosition(0)
            }
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
        fun newInstance() = HeroesFragment()
    }
}
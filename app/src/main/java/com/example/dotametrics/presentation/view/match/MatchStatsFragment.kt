package com.example.dotametrics.presentation.view.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.databinding.FragmentMatchStatsBinding
import com.example.dotametrics.presentation.adapter.MatchStatsPlayerAdapter
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar

class MatchStatsFragment : Fragment() {

    private var _binding: FragmentMatchStatsBinding? = null
    private val binding: FragmentMatchStatsBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchStatsBinding is null")

    private lateinit var radiantAdapter: MatchStatsPlayerAdapter
    private lateinit var direAdapter: MatchStatsPlayerAdapter

    private val viewModel: MatchViewModel by lazy {
        ViewModelProvider(requireActivity())[MatchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcMatchStatsRadiant.startLoading(binding.pbRcMatchStatsRadiant)
        binding.rcMatchStatsDire.startLoading(binding.pbRcMatchStatsDire)
        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() = with(binding) {
        radiantAdapter = MatchStatsPlayerAdapter()
        rcMatchStatsRadiant.layoutManager = LinearLayoutManager(activity)
        rcMatchStatsRadiant.adapter = radiantAdapter

        direAdapter = MatchStatsPlayerAdapter()
        rcMatchStatsDire.layoutManager = LinearLayoutManager(activity)
        rcMatchStatsDire.adapter = direAdapter
    }

    private fun observe() {
        viewModel.result.observe(viewLifecycleOwner) { match ->
            radiantAdapter.submitList(match.players.sortedBy { it.playerSlot }
                .filter { player -> player.playerSlot!! < 100 })
            direAdapter.submitList(match.players.sortedBy { it.playerSlot }
                .filter { player -> player.playerSlot!! >= 100 })
            binding.rcMatchStatsRadiant.stopLoading(binding.pbRcMatchStatsRadiant)
            binding.rcMatchStatsDire.stopLoading(binding.pbRcMatchStatsDire)
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
        fun newInstance() = MatchStatsFragment()
    }
}
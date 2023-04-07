package com.example.dotametrics.presentation.view.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.databinding.FragmentMatchSkillsBinding
import com.example.dotametrics.presentation.adapter.MatchSkillsPlayerAdapter
import com.example.dotametrics.util.ConstData
import com.google.android.material.snackbar.Snackbar

class MatchSkillsFragment : Fragment() {

    private var _binding: FragmentMatchSkillsBinding? = null
    private val binding: FragmentMatchSkillsBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchSkillsBinding is null")

    private lateinit var radiantAdapter: MatchSkillsPlayerAdapter
    private lateinit var direAdapter: MatchSkillsPlayerAdapter

    private val viewModel: MatchViewModel by lazy {
        ViewModelProvider(requireActivity())[MatchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchSkillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observe()
    }

    private fun loadData() {
        val abilityIdsLoaded = ConstData.abilityIds.isNotEmpty()
        val abilitiesLoaded = ConstData.abilities.isNotEmpty()

        if (abilityIdsLoaded && abilitiesLoaded) {
            viewModel.result.observe(viewLifecycleOwner) { match ->
                radiantAdapter.submitList(match.players.sortedBy { it.playerSlot }
                    .filter { player -> player.playerSlot!! < 100 })
                direAdapter.submitList(match.players.sortedBy { it.playerSlot }
                    .filter { player -> player.playerSlot!! >= 100 })
            }
        } else {
            if (!abilityIdsLoaded) viewModel.loadAbilityIds()
            if (!abilitiesLoaded) viewModel.loadAbilities()
        }
    }

    private fun initRecyclerView() = with(binding) {
        radiantAdapter = MatchSkillsPlayerAdapter(requireActivity() as AppCompatActivity)
        rcMatchSkillsRadiant.layoutManager = LinearLayoutManager(activity)
        rcMatchSkillsRadiant.adapter = radiantAdapter

        direAdapter = MatchSkillsPlayerAdapter(requireActivity() as AppCompatActivity)
        rcMatchOverviewDire.layoutManager = LinearLayoutManager(activity)
        rcMatchOverviewDire.adapter = direAdapter
    }

    private fun observe() {
        viewModel.result.observe(viewLifecycleOwner) {
            loadData()
        }
        viewModel.constAbilityIds.observe(viewLifecycleOwner) {
            loadData()
        }
        viewModel.constAbilities.observe(viewLifecycleOwner) {
            loadData()
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
        fun newInstance() = MatchSkillsFragment()
    }
}
package com.example.dotametrics.presentation.view.patch

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.App
import com.example.dotametrics.data.remote.model.constants.patch.PatchNotesResult
import com.example.dotametrics.databinding.FragmentPatchNotesBinding
import com.example.dotametrics.presentation.adapter.PatchNotesHeroesAdapter
import com.example.dotametrics.presentation.adapter.PatchNotesItemsAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.data.ConstData
import com.google.android.material.snackbar.Snackbar

class PatchNotesFragment : Fragment() {

    private var _binding: FragmentPatchNotesBinding? = null
    private val binding: FragmentPatchNotesBinding
        get() = _binding ?: throw RuntimeException("FragmentPatchNotesBinding is null")

    private val viewModel: PatchViewModel by activityViewModels()

    private val constViewModel: ConstViewModel by activityViewModels {
        ConstViewModel.ConstViewModelFactory((context?.applicationContext as App))
    }

    private lateinit var heroesAdapter: PatchNotesHeroesAdapter
    private lateinit var itemsAdapter: PatchNotesItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatchNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        constViewModel.loadItems()
        constViewModel.loadHeroes()
        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() = with(binding) {
        heroesAdapter = PatchNotesHeroesAdapter()
        rcPatchnotesHeroes.layoutManager = LinearLayoutManager(requireActivity())
        rcPatchnotesHeroes.adapter = heroesAdapter

        itemsAdapter = PatchNotesItemsAdapter()
        rcPatchnotesItems.layoutManager = LinearLayoutManager(requireActivity())
        rcPatchnotesItems.adapter = itemsAdapter
    }

    private fun observe() {
        constViewModel.heroes.observe(viewLifecycleOwner) {
            checkData()
        }
        constViewModel.constItems.observe(viewLifecycleOwner) {
            checkData()
        }
        constViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun checkData() {
        val heroesLoaded = ConstData.heroes.isNotEmpty()
        val itemsLoaded = ConstData.items.isNotEmpty()

        if (heroesLoaded && itemsLoaded) {
            viewModel.currentPatchNotes.observe(viewLifecycleOwner) {
                if (it == null) {
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    loadData(it)
                }
            }
        } else {
            if (!heroesLoaded) constViewModel.loadHeroes()
            if (!itemsLoaded) constViewModel.loadItems()
        }
    }

    private fun loadData(patch: Pair<String, PatchNotesResult>) = with(binding) {
        tvPatchnotesName.text = patch.first
        val builder = StringBuilder()
        patch.second.general.forEach { builder.append("<li>$it</li>") }
        tvPatchnotesGeneralList.text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_LEGACY)

        heroesAdapter.submitList(patch.second.heroes.map { it.toPair() })
        itemsAdapter.submitList(patch.second.items.map { it.toPair() })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PatchNotesFragment()
    }
}
package com.example.dotametrics.presentation.view.patch

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.patch.PatchNotesResult
import com.example.dotametrics.databinding.FragmentPatchSeriesBinding
import com.example.dotametrics.presentation.adapter.PatchSeriesAdapter
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatchSeriesFragment : Fragment() {

    private var _binding: FragmentPatchSeriesBinding? = null
    private val binding: FragmentPatchSeriesBinding
        get() = _binding ?: throw RuntimeException("FragmentPatchSeriesBinding is null")

    private lateinit var adapter: PatchSeriesAdapter

    private val viewModel: PatchViewModel by activityViewModels()

    private val setSeries: ((Pair<String, PatchNotesResult>) -> Unit) = {
        viewModel.setPatch(it)
        openPatchNotes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatchSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcPatches.startLoading(binding.pbRcPatches)
        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = PatchSeriesAdapter()
        val orientation: Int = binding.root.resources.configuration.orientation
        val layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(requireActivity(), 2)
        } else {
            LinearLayoutManager(requireActivity())
        }
        rcPatches.layoutManager = layoutManager
        rcPatches.adapter = adapter
        adapter.onItemClickedListener = setSeries
    }

    private fun loadData(map: Map<String, PatchNotesResult>) {
        adapter.submitList(map.map { it.toPair() })
    }

    private fun observe() {
        viewModel.currentSeries.observe(viewLifecycleOwner) {
            loadData(it)
            binding.rcPatches.stopLoading(binding.pbRcPatches)
        }
    }

    private fun openPatchNotes() {
        findNavController().navigate(R.id.action_patchSeriesFragment_to_patchNotesFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
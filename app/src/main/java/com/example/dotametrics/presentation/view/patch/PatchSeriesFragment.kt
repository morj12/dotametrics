package com.example.dotametrics.presentation.view.patch

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.R
import com.example.dotametrics.data.remote.model.constants.patch.PatchNotesResult
import com.example.dotametrics.databinding.FragmentPatchSeriesBinding
import com.example.dotametrics.presentation.adapter.PatchSeriesAdapter

class PatchSeriesFragment : Fragment() {

    private var _binding: FragmentPatchSeriesBinding? = null
    private val binding: FragmentPatchSeriesBinding
        get() = _binding ?: throw RuntimeException("FragmentPatchSeriesBinding is null")

    private lateinit var adapter: PatchSeriesAdapter

    private val viewModel: PatchViewModel by activityViewModels()

    private val setSeries: ((Pair<String, PatchNotesResult>) -> Unit) = {
        viewModel.setPatch(it)
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
            if (it == null) {
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                loadData(it)
            }
        }
        viewModel.currentPatchNotes.observe(viewLifecycleOwner) {
            if (it != null) openPatchNotes()
        }
    }

    private fun openPatchNotes() {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            )
            .replace(R.id.patch_placeholder, PatchNotesFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PatchSeriesFragment()
    }
}
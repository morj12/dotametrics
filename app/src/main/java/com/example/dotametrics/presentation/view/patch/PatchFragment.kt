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
import com.example.dotametrics.domain.entity.remote.constants.patch.PatchResult
import com.example.dotametrics.databinding.FragmentPatchBinding
import com.example.dotametrics.presentation.adapter.PatchAdapter
import com.example.dotametrics.domain.ConstData
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatchFragment : Fragment() {

    private var _binding: FragmentPatchBinding? = null
    private val binding: FragmentPatchBinding
        get() = _binding ?: throw RuntimeException("FragmentPatchBinding is null")

    private lateinit var adapter: PatchAdapter

    private val viewModel: PatchViewModel by activityViewModels()

    private val setSeries: (PatchResult) -> Unit = { patch ->
        patch.name?.let { patchName ->
            val name = patchName.replace(".", "_")
            val series = ConstData.patchNotes.filter { it.key.contains(name) }
            if (series.isEmpty()) {
                Snackbar.make(
                    this.binding.root,
                    getString(R.string.no_patch_info),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                viewModel.setSeries(series)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadPatches()
        viewModel.loadPatchNotes()
        initRecyclerView()
        observe()

    }

    private fun checkData() {
        val patches = ConstData.patches.isNotEmpty()
        val patchNotes = ConstData.patchNotes.isNotEmpty()

        if (patches && patchNotes) {
            showData()
        } else {
            if (!patches) viewModel.loadPatches()
            if (!patchNotes) viewModel.loadPatchNotes()
        }
    }

    private fun initRecyclerView() = with(binding) {
        adapter = PatchAdapter()
        val orientation: Int = binding.root.resources.configuration.orientation
        val layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(requireActivity(), 2)
        } else {
            LinearLayoutManager(requireActivity())
        }
        rcPatchSeries.layoutManager = layoutManager
        rcPatchSeries.adapter = adapter
        adapter.onItemClickedListener = setSeries
    }

    private fun showData() {
        adapter.submitList(ConstData.patches)
    }

    private fun observe() {
        viewModel.patches.observe(viewLifecycleOwner) {
            checkData()
        }
        viewModel.patchNotes.observe(viewLifecycleOwner) {
            checkData()
        }
        viewModel.currentSeries.observe(viewLifecycleOwner) {
            if (it != null) openSeries()
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun openSeries() {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            )
            .replace(R.id.patch_placeholder, PatchSeriesFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PatchFragment()
    }
}
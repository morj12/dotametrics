package com.example.dotametrics.presentation.view.account

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.databinding.FragmentTotalsBinding
import com.example.dotametrics.presentation.adapter.TotalsAdapter
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TotalsFragment : Fragment() {

    private var _binding: FragmentTotalsBinding? = null
    private val binding: FragmentTotalsBinding
        get() = _binding ?: throw RuntimeException("FragmentTotalsBinding is null")

    private lateinit var adapter: TotalsAdapter

    private val viewModel: AccountViewModel by viewModels<AccountViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcTotals.startLoading(binding.pbRcTotals)
        loadData()
        initRecyclerView()
        observe()
    }

    private fun loadData() {
        if (viewModel.totals.value == null) viewModel.loadTotals()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = TotalsAdapter()
        val orientation: Int = binding.root.resources.configuration.orientation
        val layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(requireActivity(), 2)
        } else {
            LinearLayoutManager(requireActivity())
        }
        rcTotals.layoutManager = layoutManager
        rcTotals.adapter = adapter
    }

    private fun observe() {
        viewModel.totals.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcTotals.scrollToPosition(0)
            }
            binding.rcTotals.stopLoading(binding.pbRcTotals)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = TotalsFragment()
    }
}
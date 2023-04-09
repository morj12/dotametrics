package com.example.dotametrics.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.App
import com.example.dotametrics.databinding.FragmentTotalsBinding
import com.example.dotametrics.presentation.adapter.TotalsAdapter
import com.google.android.material.snackbar.Snackbar

class TotalsFragment : Fragment() {

    private var _binding: FragmentTotalsBinding? = null
    private val binding: FragmentTotalsBinding
        get() = _binding ?: throw RuntimeException("FragmentTotalsBinding is null")

    private lateinit var adapter: TotalsAdapter

    private val viewModel: AccountViewModel by activityViewModels {
        AccountViewModel.AccountViewModelFactory((context?.applicationContext as App))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initRecyclerView()
        observe()
    }

    private fun loadData() {
        viewModel.loadTotals()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = TotalsAdapter()
        rcTotals.layoutManager = LinearLayoutManager(activity)
        rcTotals.adapter = adapter
    }

    private fun observe() {
        viewModel.totals.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcTotals.scrollToPosition(0)
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
        fun newInstance() = TotalsFragment()
    }
}
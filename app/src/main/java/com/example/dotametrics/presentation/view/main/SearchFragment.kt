package com.example.dotametrics.presentation.view.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.search.SearchResult
import com.example.dotametrics.databinding.FragmentSearchBinding
import com.example.dotametrics.presentation.adapter.SearchResultAdapter
import com.example.dotametrics.presentation.view.account.AccountFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding is null")

    private lateinit var adapter: SearchResultAdapter

    private val viewModel: MainViewModel by activityViewModels()

    private val openAccount: (SearchResult) -> Unit = {
        val bundle = Bundle().apply {
            putLong("id", it.accountId ?: 0)
        }
        findNavController().navigate(R.id.action_mainFragment_to_accountFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observe()
        initListeners()
    }

    private fun initRecyclerView() {
        adapter = SearchResultAdapter()
        val orientation: Int = binding.root.resources.configuration.orientation
        val layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(requireActivity(), 2)
        } else {
            LinearLayoutManager(requireActivity())
        }
        binding.rcMain.layoutManager = layoutManager
        binding.rcMain.adapter = adapter
        adapter.onItemClickedListener = openAccount
    }

    private fun observe() {
        viewModel.results.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcMain.scrollToPosition(0)
                binding.btSearch.visibility = View.VISIBLE
                binding.pbSearch.visibility = View.INVISIBLE
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            binding.btSearch.visibility = View.VISIBLE
            binding.pbSearch.visibility = View.INVISIBLE
        }
    }

    private fun initListeners() {
        binding.edMainUsername.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            true
        }
        binding.btSearch.setOnClickListener { search() }
    }


    private fun search() {
        val user = binding.edMainUsername.text.toString()
        if (user.isNotBlank()) {
            viewModel.search(user)
            binding.btSearch.visibility = View.INVISIBLE
            binding.pbSearch.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}
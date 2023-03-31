package com.example.dotametrics.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.databinding.FragmentPeersBinding
import com.example.dotametrics.presentation.adapter.PeersAdapter
import com.google.android.material.snackbar.Snackbar

class PeersFragment : Fragment() {

    private var _binding: FragmentPeersBinding? = null
    private val binding: FragmentPeersBinding
        get() = _binding ?: throw RuntimeException("FragmentPeersBinding is null")

    private lateinit var adapter: PeersAdapter

    private val viewModel: AccountViewModel by lazy {
        ViewModelProvider(requireActivity())[AccountViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initRecyclerView()
        observe()
    }

    private fun loadData() {
        viewModel.loadPeers()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = PeersAdapter()
        rcPeers.layoutManager = LinearLayoutManager(activity)
        rcPeers.adapter = adapter
    }

    private fun observe() {
        viewModel.peers.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcPeers.scrollToPosition(0)
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
        fun newInstance() = PeersFragment()
    }
}
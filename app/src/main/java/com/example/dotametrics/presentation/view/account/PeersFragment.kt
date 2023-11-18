package com.example.dotametrics.presentation.view.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.App
import com.example.dotametrics.data.remote.model.players.peers.PeersResult
import com.example.dotametrics.databinding.FragmentPeersBinding
import com.example.dotametrics.presentation.adapter.PeersAdapter
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar

class PeersFragment : Fragment() {

    private var _binding: FragmentPeersBinding? = null
    private val binding: FragmentPeersBinding
        get() = _binding ?: throw RuntimeException("FragmentPeersBinding is null")

    private lateinit var adapter: PeersAdapter

    private val viewModel: AccountViewModel by activityViewModels {
        AccountViewModel.AccountViewModelFactory((context?.applicationContext as App))
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
        binding.rcPeers.startLoading(binding.pbRcPeers)
        loadData()
        initRecyclerView()
        observe()
    }

    private fun loadData() {
        if (viewModel.peers.value == null) viewModel.loadPeers()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = PeersAdapter()
        rcPeers.layoutManager = LinearLayoutManager(activity)
        rcPeers.adapter = adapter
        adapter.onItemClickedListener = openAccount
    }

    private val openAccount: (PeersResult) -> Unit = {
        val intent = Intent(requireActivity(), AccountActivity::class.java)
        intent.putExtra("id", it.accountId)
        startActivity(intent)
    }

    private fun observe() {
        viewModel.peers.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcPeers.scrollToPosition(0)
            }
            binding.rcPeers.stopLoading(binding.pbRcPeers)
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
        fun newInstance() = PeersFragment()
    }
}
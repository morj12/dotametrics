package com.example.dotametrics.presentation.view.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.App
import com.example.dotametrics.data.model.players.matches.MatchesResult
import com.example.dotametrics.databinding.FragmentMatchesBinding
import com.example.dotametrics.presentation.adapter.MatchesResultAdapter
import com.example.dotametrics.presentation.view.match.MatchActivity
import com.example.dotametrics.util.ConstData
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar

class MatchesFragment : Fragment() {

    private var _binding: FragmentMatchesBinding? = null
    private val binding: FragmentMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchesBinding is null")

    private lateinit var adapter: MatchesResultAdapter

    private val viewModel: AccountViewModel by activityViewModels {
        AccountViewModel.AccountViewModelFactory((context?.applicationContext as App))
    }

    private val openMatch: (MatchesResult) -> Unit = {
        val intent = Intent(requireActivity(), MatchActivity::class.java)
        intent.putExtra("id", it.matchId)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcMatches.startLoading(binding.pbRcMatches)
        initRecyclerView()
        observe()
    }

    private fun loadData() {
        val heroesLoaded = ConstData.heroes.isNotEmpty()
        val lobbiesLoaded = ConstData.lobbies.isNotEmpty()

        if (heroesLoaded && lobbiesLoaded) {
            viewModel.loadMatches(::observeMatches)
        } else {
            if (!heroesLoaded) viewModel.loadHeroes()
            if (!lobbiesLoaded) viewModel.loadLobbyTypes()
        }
    }

    private fun initRecyclerView() = with(binding) {
        adapter = MatchesResultAdapter()
        rcMatches.layoutManager = LinearLayoutManager(activity)
        rcMatches.adapter = adapter
        adapter.onItemClickedListener = openMatch
    }

    private fun observe() {
        viewModel.constHeroes.observe(viewLifecycleOwner) {
            loadData()
        }
        viewModel.constLobbyTypes.observe(viewLifecycleOwner) {
            loadData()
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observeMatches() {
        viewModel.matches.observe(viewLifecycleOwner) {
            if (it.size > 0) {
                adapter.submitList(it) {
                    binding.rcMatches.scrollToPosition(0)
                }
                binding.rcMatches.stopLoading(binding.pbRcMatches)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): MatchesFragment {
            return MatchesFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
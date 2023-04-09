package com.example.dotametrics.presentation.view.match

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.data.model.matches.Players
import com.example.dotametrics.databinding.FragmentMatchOverviewBinding
import com.example.dotametrics.presentation.adapter.MatchOverviewPlayerAdapter
import com.example.dotametrics.presentation.view.account.AccountActivity
import com.google.android.material.snackbar.Snackbar

class MatchOverviewFragment : Fragment() {

    private var _binding: FragmentMatchOverviewBinding? = null
    private val binding: FragmentMatchOverviewBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchOverviewBinding is null")

    private lateinit var radiantAdapter: MatchOverviewPlayerAdapter
    private lateinit var direAdapter: MatchOverviewPlayerAdapter

    private val viewModel: MatchViewModel by lazy {
        ViewModelProvider(requireActivity())[MatchViewModel::class.java]
    }

    private val openAccount: (Players) -> Unit = {
        val intent = Intent(requireActivity(), AccountActivity::class.java)
        intent.putExtra("id", it.accountId)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() = with(binding) {
        radiantAdapter = MatchOverviewPlayerAdapter()
        rcMatchOverviewRadiant.layoutManager = LinearLayoutManager(activity)
        rcMatchOverviewRadiant.adapter = radiantAdapter

        radiantAdapter.onItemClickedListener = openAccount

        direAdapter = MatchOverviewPlayerAdapter()
        rcMatchOverviewDire.layoutManager = LinearLayoutManager(activity)
        rcMatchOverviewDire.adapter = direAdapter

        direAdapter.onItemClickedListener = openAccount
    }

    private fun observe() {
        viewModel.result.observe(viewLifecycleOwner) { match ->
            radiantAdapter.submitList(match.players.sortedBy { it.playerSlot }
                .filter { player -> player.playerSlot!! < 100 })
            direAdapter.submitList(match.players.sortedBy { it.playerSlot }
                .filter { player -> player.playerSlot!! >= 100 })
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
        fun newInstance() = MatchOverviewFragment()
    }
}
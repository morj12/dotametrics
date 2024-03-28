package com.example.dotametrics.presentation.view.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotametrics.domain.entity.local.PlayerDbModel
import com.example.dotametrics.databinding.FragmentFavoritesBinding
import com.example.dotametrics.presentation.adapter.FavoriteAdapter
import com.example.dotametrics.presentation.view.account.AccountActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding ?: throw RuntimeException("FragmentFavoritesBinding is null")

    private lateinit var adapter: FavoriteAdapter

    private val viewModel: MainViewModel by activityViewModels()

    private val openAccount: (PlayerDbModel) -> Unit = {
        val intent = Intent(requireActivity(), AccountActivity::class.java)
        intent.putExtra("id", it.id)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observe()
    }

    private fun initRecyclerView() {
        adapter = FavoriteAdapter()
        val orientation: Int = binding.root.resources.configuration.orientation
        val layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(requireActivity(), 2)
        } else {
            LinearLayoutManager(requireActivity())
        }
        binding.rcFav.layoutManager = layoutManager
        binding.rcFav.adapter = adapter
        adapter.onItemClickedListener = openAccount
    }

    private fun observe() {
        viewModel.players.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.rcFav.scrollToPosition(0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoritesFragment()
    }
}
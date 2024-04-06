package com.example.dotametrics.presentation.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dotametrics.databinding.FragmentMainBinding
import com.example.dotametrics.presentation.adapter.MainPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
    }

    private fun initTabs() {
        val mainPagerAdapter = MainPagerAdapter(requireContext(), childFragmentManager)
        binding.viewPagerMain.adapter = mainPagerAdapter
        binding.viewPagerMain.offscreenPageLimit = 3
        binding.tabsMain.setupWithViewPager(binding.viewPagerMain)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
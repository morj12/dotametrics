package com.example.dotametrics.presentation.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.dotametrics.databinding.ActivityMainBinding
import com.example.dotametrics.presentation.adapter.MainPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        initTabs()
    }

    private fun initTabs() {
        val mainPagerAdapter = MainPagerAdapter(this, supportFragmentManager)
        binding.viewPagerMain.adapter = mainPagerAdapter
        binding.tabsMain.setupWithViewPager(binding.viewPagerMain)
    }

}
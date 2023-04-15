package com.example.dotametrics.presentation.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.dotametrics.databinding.ActivityMainBinding
import com.example.dotametrics.presentation.adapter.MainPagerAdapter
import com.example.dotametrics.presentation.view.DrawerActivity

class MainActivity : DrawerActivity() {

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
        binding.viewPagerMain.offscreenPageLimit = 3
        binding.tabsMain.setupWithViewPager(binding.viewPagerMain)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
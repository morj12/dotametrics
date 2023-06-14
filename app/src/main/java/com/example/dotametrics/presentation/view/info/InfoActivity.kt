package com.example.dotametrics.presentation.view.info

import android.os.Bundle
import com.example.dotametrics.R
import com.example.dotametrics.databinding.ActivityInfoBinding
import com.example.dotametrics.presentation.view.DrawerActivity

class InfoActivity : DrawerActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allocateActivityTitle(getString(R.string.about))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
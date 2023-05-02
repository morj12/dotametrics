package com.example.dotametrics.presentation.view.patch

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.databinding.ActivityPatchBinding
import com.example.dotametrics.presentation.view.DrawerActivity

class PatchActivity : DrawerActivity() {

    private lateinit var binding: ActivityPatchBinding

    private val viewModel: PatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allocateActivityTitle(getString(R.string.patches))

        openPatches()
    }

    private fun openPatches() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.patch_placeholder, PatchFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.patch_placeholder)) {
            is PatchFragment -> {
                finishAffinity()
            }
            is PatchSeriesFragment -> {
                viewModel.clearCurrentSeries()
            }
            is PatchNotesFragment -> {
                viewModel.clearCurrentPatch()
            }
        }

    }
}
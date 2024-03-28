package com.example.dotametrics.presentation.view.herosearch

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.databinding.ActivityHeroSearchBinding
import com.example.dotametrics.presentation.adapter.HeroSearchAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.presentation.view.DrawerActivity
import com.example.dotametrics.presentation.view.hero.HeroActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeroSearchActivity : DrawerActivity() {

    private lateinit var binding: ActivityHeroSearchBinding

    private val viewModel: HeroSearchViewModel by viewModels()

    private val constViewModel: ConstViewModel by viewModels()

    private lateinit var adapter: HeroSearchAdapter

    private val openHero: (HeroResult) -> Unit = {
        val intent = Intent(this, HeroActivity::class.java)
        intent.putExtra("hero", it)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allocateActivityTitle(getString(R.string.heroes))

        constViewModel.loadHeroes()
        initRecyclerView()
        initListeners()
        observe()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = HeroSearchAdapter()
        val orientation: Int = binding.root.resources.configuration.orientation
        val layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this@HeroSearchActivity, 5)
        } else {
            GridLayoutManager(this@HeroSearchActivity, 3)
        }
        binding.rcHeroes.layoutManager = layoutManager
        binding.rcHeroes.adapter = adapter
        adapter.onItemClickedListener = openHero
    }

    private fun initListeners() = with(binding) {
        btSearchHeroes.setOnClickListener {
            binding.btSearchHeroes.visibility = View.INVISIBLE
            binding.pbSearchHeroes.visibility = View.VISIBLE
            viewModel.filterHeroes(
                if (binding.edHeroes.text.isNotBlank())
                    edHeroes.text.toString()
                else null
            )
        }
        edHeroes.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.btSearchHeroes.visibility = View.INVISIBLE
                binding.pbSearchHeroes.visibility = View.VISIBLE
                viewModel.filterHeroes(
                    if (binding.edHeroes.text.isNotBlank())
                        edHeroes.text.toString()
                    else null
                )
            }
            true
        }
    }

    private fun loadData() {
        viewModel.filterHeroes()
    }

    private fun observe() {
        constViewModel.heroes.observe(this) {
            loadData()
        }
        viewModel.filteredHeroes.observe(this) {
            adapter.submitList(it) {
                binding.rcHeroes.scrollToPosition(0)
                binding.btSearchHeroes.visibility = View.VISIBLE
                binding.pbSearchHeroes.visibility = View.INVISIBLE
            }
        }
        constViewModel.error.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            binding.btSearchHeroes.visibility = View.VISIBLE
            binding.pbSearchHeroes.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
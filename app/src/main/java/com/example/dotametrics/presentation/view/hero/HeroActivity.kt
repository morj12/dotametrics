package com.example.dotametrics.presentation.view.hero

import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.databinding.ActivityHeroBinding
import com.example.dotametrics.presentation.view.DrawerActivity
import com.example.dotametrics.util.AttrMapper
import com.example.dotametrics.util.GlideRequestOptions.requestOptions
import com.google.android.material.snackbar.Snackbar

class HeroActivity : DrawerActivity() {

    private lateinit var binding: ActivityHeroBinding

    private val viewModel: HeroViewModel by viewModels {
        HeroViewModel.HeroViewModelFactory(applicationContext as App)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allocateActivityTitle(getString(R.string.heroes))

        initTabs()
        val hero = intent.getParcelableExtra<HeroResult>("hero")
        if (hero != null) {
            viewModel.setHero(hero)
        }
        observe()
    }

    private fun initTabs() {
    }

    private fun showData(hero: HeroResult) = with(binding) {
        Glide.with(root)
            .load("${URL}${hero.img}\"")
            .apply(requestOptions())
            .into(heroImage)
        tvHeroName.text = hero.localizedName
        heroMainAttr.text =
            getString(R.string.primary_attr, AttrMapper.mapAttr(root.context, hero.primaryAttr))
        heroStr.text = getString(R.string.str, "${hero.baseStr} + ${hero.strGain}")
        heroAgi.text = getString(R.string.agi, "${hero.baseAgi} + ${hero.agiGain}")
        heroInt.text = getString(R.string.intel, "${hero.baseInt} + ${hero.intGain}")
        heroResistance.text = getString(R.string.resist, hero.baseMr.toString())
        heroRange.text = getString(R.string.range, hero.attackRange.toString())
        heroAttackSpeed.text = getString(R.string.attack_speed, hero.baseAttackTime.toString())
        heroMoveSpeed.text = getString(R.string.move_speed, hero.moveSpeed.toString())
    }

    private fun observe() {
        viewModel.hero.observe(this) {
            showData(it)
        }
        viewModel.error.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val URL = "https://api.opendota.com"
    }
}
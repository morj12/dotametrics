package com.example.dotametrics.presentation.view.hero

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.abilities.AbilityResult
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.databinding.ActivityHeroBinding
import com.example.dotametrics.presentation.adapter.HeroSkillsAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.presentation.view.DrawerActivity
import com.example.dotametrics.util.AttrMapper
import com.example.dotametrics.data.ConstData
import com.example.dotametrics.util.GlideManager
import com.example.dotametrics.util.GlideManager.requestOptions
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar

class HeroActivity : DrawerActivity() {

    private lateinit var binding: ActivityHeroBinding

    private val viewModel: HeroViewModel by viewModels()

    private val constViewModel: ConstViewModel by viewModels {
        ConstViewModel.ConstViewModelFactory(applicationContext as App)
    }

    private lateinit var adapter: HeroSkillsAdapter

    private var hero: HeroResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allocateActivityTitle(getString(R.string.heroes))

        hero = intent.getParcelableExtra("hero")
        if (hero != null) {
            if (hero!!.name != null) {
                binding.heroImage.startLoading(binding.pbHeroImage)
                binding.rcHeroSkills.startLoading(binding.pbRcHeroSkills)
                viewModel.setHero(hero!!)
                loadConstants()
            }
        }
        initRecyclerView()
        observe()
    }

    private fun loadConstants() {
        constViewModel.loadLore()
        constViewModel.loadAghs()
        constViewModel.loadHeroAbilities()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = HeroSkillsAdapter()
        rcHeroSkills.layoutManager = LinearLayoutManager(this@HeroActivity)
        rcHeroSkills.adapter = adapter
    }

    private fun showData(hero: HeroResult) = with(binding) {
        Glide.with(root)
            .load("${GlideManager.HEROES_URL}/${hero.name?.replace(GlideManager.HEROES_URL_REPLACE, "")}.png")
            .apply(requestOptions(root.context))
            .into(heroImage)
        heroImage.stopLoading(binding.pbHeroImage)
        tvHeroName.text = hero.localizedName
        heroMainAttr.text =
            getString(R.string.primary_attr, AttrMapper().mapAttr(root.context, hero.primaryAttr))
        heroStr.text = getString(R.string.str, "${hero.baseStr} + ${hero.strGain}")
        heroAgi.text = getString(R.string.agi, "${hero.baseAgi} + ${hero.agiGain}")
        heroInt.text = getString(R.string.intel, "${hero.baseInt} + ${hero.intGain}")
        heroResistance.text = getString(R.string.resist, hero.baseMr.toString())
        heroRange.text = getString(R.string.range, hero.attackRange.toString())
        heroAttackSpeed.text = getString(R.string.attack_speed, hero.baseAttackTime.toString())
        heroMoveSpeed.text = getString(R.string.move_speed, hero.moveSpeed.toString())
    }

    private fun showLore() = with(binding) {
        val text = ConstData.lores.entries.firstOrNull { hero!!.name!!.contains(it.key) }?.value
        if (text != null) {
            tvHeroLore.text = text
        }
    }

    private fun showAghs() = with(binding) {
        val aghsInfo = ConstData.aghs.firstOrNull { it.heroId == hero!!.id }
        tvHeroAghsSkill.text = aghsInfo?.scepterSkillName
        tvHeroAghs.text = aghsInfo?.scepterDesc
        tvHeroShardSkill.text = aghsInfo?.shardSkillName
        tvHeroShard.text = aghsInfo?.shardDesc
    }

    private fun observe() {
        viewModel.hero.observe(this) {
            showData(it)
        }
        constViewModel.constLores.observe(this) {
            showLore()
        }
        constViewModel.constAghs.observe(this) {
            showAghs()
        }
        constViewModel.constHeroAbilities.observe(this) {
            constViewModel.loadAbilities()
        }
        constViewModel.constAbilities.observe(this) {
            if (ConstData.heroAbilities.isNotEmpty()) {
                showAbilities()
                showTalents()
            }
        }
        constViewModel.error.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showTalents() = with(binding) {
        val talents =
            ConstData.heroAbilities[hero!!.name]?.talents?.filter { talent ->
                ConstData.abilities.any { it.key == talent.name }
            }
        val leveledTalents = mutableListOf<Pair<AbilityResult?, AbilityResult?>>()
        if (talents != null) {
            for (i in 1..4) {
                leveledTalents.add(
                    ConstData.abilities[talents.firstOrNull { it.level == i }?.name]
                            to ConstData.abilities[talents.lastOrNull { it.level == i }?.name]
                )
            }
        }
        if (leveledTalents.isNotEmpty()) {
            heroTalent10A.text = leveledTalents[0].first?.dname
            heroTalent10B.text = leveledTalents[0].second?.dname
            heroTalent15A.text = leveledTalents[1].first?.dname
            heroTalent15B.text = leveledTalents[1].second?.dname
            heroTalent20A.text = leveledTalents[2].first?.dname
            heroTalent20B.text = leveledTalents[2].second?.dname
            heroTalent25A.text = leveledTalents[3].first?.dname
            heroTalent25B.text = leveledTalents[3].second?.dname
        }
    }

    private fun showAbilities() {
        val abilityNames = ConstData.heroAbilities[hero!!.name]?.abilities
        if (abilityNames != null) {
            val abilities = ConstData.abilities.filter { abilityNames.contains(it.key) }
            adapter.submitList(abilities
                .filter { !it.value.behavior.contains("Hidden") }
                .map { it.toPair() }
            ) {
                binding.rcHeroSkills.stopLoading(binding.pbRcHeroSkills)
            }
        }
    }
}
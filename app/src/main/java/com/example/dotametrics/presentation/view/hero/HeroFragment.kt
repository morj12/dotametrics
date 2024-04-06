package com.example.dotametrics.presentation.view.hero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dotametrics.R
import com.example.dotametrics.databinding.FragmentHeroBinding
import com.example.dotametrics.domain.ConstData
import com.example.dotametrics.domain.entity.remote.constants.abilities.AbilityResult
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.presentation.adapter.HeroSkillsAdapter
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.util.AttrMapper
import com.example.dotametrics.util.GlideManager
import com.example.dotametrics.util.startLoading
import com.example.dotametrics.util.stopLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeroFragment : Fragment() {

    private var hero: HeroResult? = null

    private var _binding: FragmentHeroBinding? = null
    private val binding: FragmentHeroBinding
        get() = _binding ?: throw RuntimeException("FragmentHeroBinding is null")

    private val viewModel: HeroViewModel by viewModels()

    private val constViewModel: ConstViewModel by viewModels()

    private var dataLoaded = false
    private var loreLoaded = false
    private var aghsLoaded = false
    private var abilitiesLoaded = false
    private var talentsLoaded = false

    private lateinit var adapter: HeroSkillsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hero = it.getParcelable("hero")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (hero!!.name != null) {
            binding.clHero.startLoading(binding.pbClHero)
            viewModel.setHero(hero!!)
            loadConstants()
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
        rcHeroSkills.layoutManager = LinearLayoutManager(requireContext())
        rcHeroSkills.adapter = adapter
    }

    private fun showData(hero: HeroResult) = with(binding) {
        Glide.with(root)
            .load("${GlideManager.HEROES_URL}/${hero.name?.replace(GlideManager.HEROES_URL_REPLACE, "")}.png")
            .apply(GlideManager.requestOptions(root.context))
            .into(heroImage)
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
        dataLoaded = true
        checkStopLoading()
    }

    private fun showLore() = with(binding) {
        val text = ConstData.lores.entries.firstOrNull { hero!!.name!!.contains(it.key) }?.value
        if (text != null) {
            tvHeroLore.text = text
            loreLoaded = true
            checkStopLoading()
        }
    }

    private fun checkStopLoading() {
        if (loreLoaded && aghsLoaded && abilitiesLoaded && aghsLoaded && talentsLoaded) {
            binding.clHero.stopLoading(binding.pbClHero)
            binding.clHero.visibility = View.VISIBLE
        }
    }

    private fun showAghs() = with(binding) {
        val aghsInfo = ConstData.aghs.firstOrNull { it.heroId == hero!!.id }
        tvHeroAghsSkill.text = aghsInfo?.scepterSkillName
        tvHeroAghs.text = aghsInfo?.scepterDesc
        tvHeroShardSkill.text = aghsInfo?.shardSkillName
        tvHeroShard.text = aghsInfo?.shardDesc
        aghsLoaded = true
        checkStopLoading()
    }

    private fun observe() {
        viewModel.hero.observe(viewLifecycleOwner) {
            showData(it)
        }
        constViewModel.constLores.observe(viewLifecycleOwner) {
            showLore()
        }
        constViewModel.constAghs.observe(viewLifecycleOwner) {
            showAghs()
        }
        constViewModel.constHeroAbilities.observe(viewLifecycleOwner) {
            constViewModel.loadAbilities()
        }
        constViewModel.constAbilities.observe(viewLifecycleOwner) {
            if (ConstData.heroAbilities.isNotEmpty()) {
                showAbilities()
                showTalents()
            }
        }
        constViewModel.error.observe(viewLifecycleOwner) {
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

            talentsLoaded = true
            checkStopLoading()
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
            }
            abilitiesLoaded = true
            checkStopLoading()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.dotametrics.data.model.matches

import com.google.gson.annotations.SerializedName

data class Benchmarks(
    @SerializedName("gold_per_min") var goldPerMin: GoldPerMin? = GoldPerMin(),
    @SerializedName("xp_per_min") var xpPerMin: XpPerMin? = XpPerMin(),
    @SerializedName("kills_per_min") var killsPerMin: KillsPerMin? = KillsPerMin(),
    @SerializedName("last_hits_per_min") var lastHitsPerMin: LastHitsPerMin? = LastHitsPerMin(),
    @SerializedName("hero_damage_per_min") var heroDamagePerMin: HeroDamagePerMin? = HeroDamagePerMin(),
    @SerializedName("hero_healing_per_min") var heroHealingPerMin: HeroHealingPerMin? = HeroHealingPerMin(),
    @SerializedName("tower_damage") var towerDamage: TowerDamage? = TowerDamage(),
    @SerializedName("stuns_per_min") var stunsPerMin: StunsPerMin? = StunsPerMin()
)
package com.example.dotametrics.data.remote.model.matches

import com.google.gson.annotations.SerializedName

data class Players(
    @SerializedName("player_slot") var playerSlot: Int? = null,
    @SerializedName("ability_upgrades_arr") var abilityUpgradesArr: ArrayList<Int> = arrayListOf(),
    @SerializedName("account_id") var accountId: Long? = null,
    @SerializedName("assists") var assists: Int? = null,
    @SerializedName("deaths") var deaths: Int? = null,
    @SerializedName("denies") var denies: Int? = null,
    @SerializedName("gold_per_min") var goldPerMin: Double? = null,
    @SerializedName("hero_damage") var heroDamage: Int? = null,
    @SerializedName("hero_id") var heroId: Int? = null,
    @SerializedName("item_0") var item0: Int? = null,
    @SerializedName("item_1") var item1: Int? = null,
    @SerializedName("item_2") var item2: Int? = null,
    @SerializedName("item_3") var item3: Int? = null,
    @SerializedName("item_4") var item4: Int? = null,
    @SerializedName("item_5") var item5: Int? = null,
    @SerializedName("item_neutral") var itemNeutral: Int? = null,
    @SerializedName("kills") var kills: Int? = null,
    @SerializedName("last_hits") var lastHits: Int? = null,
    @SerializedName("level") var level: Int? = null,
    @SerializedName("permanent_buffs") var permanentBuffs: ArrayList<PermanentBuffs> = arrayListOf(),
    @SerializedName("tower_damage") var towerDamage: Int? = null,
    @SerializedName("xp_per_min") var xpPerMin: Int? = null,
    @SerializedName("personaname") var personaname: String? = null,
    @SerializedName("total_gold") var totalGold: Int? = null,
    @SerializedName("rank_tier") var rankTier: Int? = null
)
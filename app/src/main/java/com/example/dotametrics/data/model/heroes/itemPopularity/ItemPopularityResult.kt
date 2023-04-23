package com.example.dotametrics.data.model.heroes.itemPopularity

import com.google.gson.annotations.SerializedName

class ItemPopularityResult(
    @SerializedName("start_game_items") var startGameItems: Map<String, Int>? = mapOf(),
    @SerializedName("early_game_items") var earlyGameItems: Map<String, Int>? = mapOf(),
    @SerializedName("mid_game_items") var midGameItems: Map<String, Int>? = mapOf(),
    @SerializedName("late_game_items") var lateGameItems: Map<String, Int>? = mapOf()
)
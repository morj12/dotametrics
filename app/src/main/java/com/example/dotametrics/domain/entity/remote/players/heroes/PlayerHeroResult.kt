package com.example.dotametrics.domain.entity.remote.players.heroes

import com.google.gson.annotations.SerializedName

data class PlayerHeroResult(
    @SerializedName("hero_id") var heroId: String? = null,
    @SerializedName("last_played") var lastPlayed: Long? = null,
    @SerializedName("games") var games: Int? = null,
    @SerializedName("win") var win: Int? = null
)
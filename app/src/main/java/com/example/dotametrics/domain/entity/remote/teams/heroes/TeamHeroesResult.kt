package com.example.dotametrics.domain.entity.remote.teams.heroes

import com.google.gson.annotations.SerializedName

data class TeamHeroesResult(
    @SerializedName("hero_id") var heroId: Int? = null,
    @SerializedName("games_played") var gamesPlayed: Int? = null,
    @SerializedName("wins") var wins: Int? = null
)
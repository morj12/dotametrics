package com.example.dotametrics.data.model.heroes.matchups

import com.google.gson.annotations.SerializedName

class MatchupsResult(
    @SerializedName("hero_id") var heroId: Int? = null,
    @SerializedName("games_played") var gamesPlayed: Int? = null,
    @SerializedName("wins") var wins: Int? = null
)
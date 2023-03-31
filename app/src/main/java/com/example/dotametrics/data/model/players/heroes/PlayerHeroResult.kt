package com.example.dotametrics.data.model.players.heroes

import com.google.gson.annotations.SerializedName

data class PlayerHeroResult(
    @SerializedName("hero_id") var heroId: String? = null,
    @SerializedName("last_played") var lastPlayed: Long? = null,
    @SerializedName("games") var games: Int? = null,
    @SerializedName("win") var win: Int? = null,
    @SerializedName("with_games") var withGames: Int? = null,
    @SerializedName("with_win") var withWin: Int? = null,
    @SerializedName("against_games") var againstGames: Int? = null,
    @SerializedName("against_win") var againstWin: Int? = null
)
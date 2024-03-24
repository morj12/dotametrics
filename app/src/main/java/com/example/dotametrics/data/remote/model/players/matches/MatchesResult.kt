package com.example.dotametrics.data.remote.model.players.matches

import com.google.gson.annotations.SerializedName

data class MatchesResult(
    @SerializedName("match_id") var matchId: Long? = null,
    @SerializedName("player_slot") var playerSlot: Int? = null,
    @SerializedName("radiant_win") var radiantWin: Boolean? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("game_mode") var gameMode: Int? = null,
    @SerializedName("lobby_type") var lobbyType: Int? = null,
    @SerializedName("hero_id") var heroId: Int? = null,
    @SerializedName("start_time") var startTime: Long? = null,
    @SerializedName("kills") var kills: Int? = null,
    @SerializedName("deaths") var deaths: Int? = null,
    @SerializedName("assists") var assists: Int? = null,
    @SerializedName("average_rank") var averageRank: Int? = null,
    @SerializedName("party_size") var partySize: Int? = null
)

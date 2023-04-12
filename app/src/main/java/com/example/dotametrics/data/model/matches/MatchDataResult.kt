package com.example.dotametrics.data.model.matches

import com.google.gson.annotations.SerializedName

data class MatchDataResult(
    @SerializedName("match_id") var matchId: Long? = null,
    @SerializedName("dire_score") var direScore: Int? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("game_mode") var gameMode: Int? = null,
    @SerializedName("human_players") var humanPlayers: Int? = null,
    @SerializedName("lobby_type") var lobbyType: Int? = null,
    @SerializedName("radiant_score") var radiantScore: Int? = null,
    @SerializedName("radiant_win") var radiantWin: Boolean? = null,
    @SerializedName("start_time") var startTime: Long? = null,
    @SerializedName("players") var players: ArrayList<Players> = arrayListOf(),
    @SerializedName("region") var region: Int? = null
)
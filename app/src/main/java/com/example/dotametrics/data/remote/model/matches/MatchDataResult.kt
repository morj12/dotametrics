package com.example.dotametrics.data.remote.model.matches

import com.google.gson.annotations.SerializedName

data class MatchDataResult(
    @SerializedName("match_id") var matchId: Long? = null,
    @SerializedName("dire_score") var direScore: Int? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("lobby_type") var lobbyType: Int? = null,
    @SerializedName("radiant_score") var radiantScore: Int? = null,
    @SerializedName("radiant_win") var radiantWin: Boolean? = null,
    @SerializedName("start_time") var startTime: Long? = null,
    @SerializedName("players") var players: ArrayList<Players> = arrayListOf(),
    @SerializedName("region") var region: Int? = null
) {
    fun isNull() = matchId == null && direScore == null && duration == null
            && lobbyType == null
            && radiantScore == null && radiantWin == null && startTime == null
            && region == null
}
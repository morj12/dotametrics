package com.example.dotametrics.domain.entity.remote.teams.matches

import com.google.gson.annotations.SerializedName

data class TeamMatchesResult(
    @SerializedName("match_id") var matchId: Long? = null,
    @SerializedName("radiant_win") var radiantWin: Boolean? = null,
    @SerializedName("radiant_score") var radiantScore: Int? = null,
    @SerializedName("dire_score") var direScore: Int? = null,
    @SerializedName("radiant") var radiant: Boolean? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("start_time") var startTime: Long? = null,
    @SerializedName("league_name") var leagueName: String? = null,
    @SerializedName("opposing_team_name") var opposingTeamName: String? = null,
    @SerializedName("opposing_team_logo") var opposingTeamLogo: String? = null
)
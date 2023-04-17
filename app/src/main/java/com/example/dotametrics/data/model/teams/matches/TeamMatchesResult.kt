package com.example.dotametrics.data.model.teams.matches

import com.google.gson.annotations.SerializedName

class TeamMatchesResult(
    @SerializedName("match_id") var matchId: Long? = null,
    @SerializedName("radiant_win") var radiantWin: Boolean? = null,
    @SerializedName("radiant_score") var radiantScore: Int? = null,
    @SerializedName("dire_score") var direScore: Int? = null,
    @SerializedName("radiant") var radiant: Boolean? = null,
    @SerializedName("duration") var duration: Long? = null,
    @SerializedName("start_time") var startTime: Long? = null,
    @SerializedName("leagueid") var leagueid: Long? = null,
    @SerializedName("league_name") var leagueName: String? = null,
    @SerializedName("cluster") var cluster: Int? = null,
    @SerializedName("opposing_team_id") var opposingTeamId: Int? = null,
    @SerializedName("opposing_team_name") var opposingTeamName: String? = null,
    @SerializedName("opposing_team_logo") var opposingTeamLogo: String? = null
)
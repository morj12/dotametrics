package com.example.dotametrics.data.model.matches

import com.google.gson.annotations.SerializedName

data class MatchDataResult(
    @SerializedName("match_id") var matchId: Long? = null,
    @SerializedName("barracks_status_dire") var barracksStatusDire: Int? = null,
    @SerializedName("barracks_status_radiant") var barracksStatusRadiant: Int? = null,
    @SerializedName("cluster") var cluster: Int? = null,
    @SerializedName("dire_score") var direScore: Int? = null,
    @SerializedName("dire_team_id") var direTeamId: String? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("engine") var engine: Int? = null,
    @SerializedName("first_blood_time") var firstBloodTime: Int? = null,
    @SerializedName("game_mode") var gameMode: Int? = null,
    @SerializedName("human_players") var humanPlayers: Int? = null,
    @SerializedName("leagueid") var leagueid: Int? = null,
    @SerializedName("lobby_type") var lobbyType: Int? = null,
    @SerializedName("match_seq_num") var matchSeqNum: Long? = null,
    @SerializedName("radiant_score") var radiantScore: Int? = null,
    @SerializedName("radiant_team_id") var radiantTeamId: String? = null,
    @SerializedName("radiant_win") var radiantWin: Boolean? = null,
    @SerializedName("skill") var skill: String? = null,
    @SerializedName("start_time") var startTime: Long? = null,
    @SerializedName("tower_status_dire") var towerStatusDire: Int? = null,
    @SerializedName("tower_status_radiant") var towerStatusRadiant: Int? = null,
    @SerializedName("version") var version: String? = null,
    @SerializedName("players") var players: ArrayList<Players> = arrayListOf(),
    @SerializedName("patch") var patch: Int? = null,
    @SerializedName("region") var region: Int? = null
)
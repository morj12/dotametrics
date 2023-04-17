package com.example.dotametrics.data.model.teams.players

import com.google.gson.annotations.SerializedName

class TeamPlayersResult(
    @SerializedName("account_id") var accountId: Long? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("games_played") var gamesPlayed: Int? = null,
    @SerializedName("wins") var wins: Int? = null,
    @SerializedName("is_current_team_member") var isCurrentTeamMember: Boolean? = null
)
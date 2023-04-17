package com.example.dotametrics.data.model.teams

import com.google.gson.annotations.SerializedName

data class TeamsResult(
    @SerializedName("team_id") var teamId: Int? = null,
    @SerializedName("rating") var rating: Double? = null,
    @SerializedName("wins") var wins: Int? = null,
    @SerializedName("losses") var losses: Int? = null,
    @SerializedName("last_match_time") var lastMatchTime: Long? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("tag") var tag: String? = null,
    @SerializedName("logo_url") var logoUrl: String? = null
)
package com.example.dotametrics.data.remote.model.players

import com.google.gson.annotations.SerializedName

data class PlayersResult(
    @SerializedName("rank_tier") var rankTier: Int? = null,
    @SerializedName("leaderboard_rank") var leaderboardRank: String? = null,
    @SerializedName("profile") var profile: Profile? = Profile()
) {
    fun isNull() = rankTier == null
            && leaderboardRank == null
            && profile == null
}
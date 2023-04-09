package com.example.dotametrics.data.model.players.peers

import com.google.gson.annotations.SerializedName

data class PeersResult(
    @SerializedName("account_id") var accountId: Long? = null,
    @SerializedName("last_played") var lastPlayed: Long? = null,
    @SerializedName("with_win") var withWin: Int? = null,
    @SerializedName("with_games") var withGames: Int? = null,
    @SerializedName("personaname") var personaname: String? = null,
    @SerializedName("avatarfull") var avatarfull: String? = null
)

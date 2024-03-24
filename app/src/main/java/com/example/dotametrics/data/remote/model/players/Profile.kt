package com.example.dotametrics.data.remote.model.players

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("account_id") var accountId: Int? = null,
    @SerializedName("personaname") var personaname: String? = null,
    @SerializedName("steamid") var steamid: String? = null,
    @SerializedName("avatarfull") var avatarfull: String? = null,
    @SerializedName("loccountrycode") var loccountrycode: String? = null
)

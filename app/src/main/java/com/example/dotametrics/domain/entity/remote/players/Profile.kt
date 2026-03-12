package com.example.dotametrics.domain.entity.remote.players

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("account_id") var accountId: Int? = null,
    @SerializedName("personaname") var name: String? = null,
    @SerializedName("steamid") var steamId: String? = null,
    @SerializedName("avatarfull") var avatar: String? = null,
    @SerializedName("loccountrycode") var countryCode: String? = null
)

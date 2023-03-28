package com.example.dotametrics.data.model.players

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("account_id") var accountId: Int? = null,
    @SerializedName("personaname") var personaname: String? = null,
    @SerializedName("plus") var plus: Boolean? = null,
    @SerializedName("steamid") var steamid: String? = null,
    @SerializedName("avatarfull") var avatarfull: String? = null,
    @SerializedName("profileurl") var profileurl: String? = null,
    @SerializedName("loccountrycode") var loccountrycode: String? = null
)

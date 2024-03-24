package com.example.dotametrics.domain.entity.remote.search

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("account_id") var accountId: Long? = null,
    @SerializedName("personaname") var personaname: String? = null,
    @SerializedName("avatarfull") var avatarfull: String? = null,
    @SerializedName("last_match_time") var lastMatchTime: String? = null,
)
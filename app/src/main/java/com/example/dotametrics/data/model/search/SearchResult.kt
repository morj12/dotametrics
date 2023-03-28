package com.example.dotametrics.data.model.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResult(
    @SerializedName("account_id") var accountId: Int? = null,
    @SerializedName("personaname") var personaname: String? = null,
    @SerializedName("avatarfull") var avatarfull: String? = null,
    @SerializedName("last_match_time") var lastMatchTime: String? = null,
    @SerializedName("similarity") var similarity: Double? = null
) : Parcelable
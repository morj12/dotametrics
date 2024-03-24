package com.example.dotametrics.domain.entity.remote.teams

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamsResult(
    @SerializedName("team_id") var teamId: Int? = null,
    @SerializedName("rating") var rating: Double? = null,
    @SerializedName("wins") var wins: Int? = null,
    @SerializedName("losses") var losses: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("logo_url") var logoUrl: String? = null
) : Parcelable
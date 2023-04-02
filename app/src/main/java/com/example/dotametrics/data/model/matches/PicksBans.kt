package com.example.dotametrics.data.model.matches

import com.google.gson.annotations.SerializedName

data class PicksBans(
    @SerializedName("is_pick") var isPick: Boolean? = null,
    @SerializedName("hero_id") var heroId: Int? = null,
    @SerializedName("team") var team: Int? = null,
    @SerializedName("order") var order: Int? = null
)
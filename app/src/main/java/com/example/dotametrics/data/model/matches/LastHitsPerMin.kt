package com.example.dotametrics.data.model.matches

import com.google.gson.annotations.SerializedName

data class LastHitsPerMin(
    @SerializedName("raw") var raw: Double? = null,
    @SerializedName("pct") var pct: Double? = null
)
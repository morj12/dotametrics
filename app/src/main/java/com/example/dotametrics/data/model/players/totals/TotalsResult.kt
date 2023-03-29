package com.example.dotametrics.data.model.players.totals

import com.google.gson.annotations.SerializedName

data class TotalsResult(
    @SerializedName("field") var field: String? = null,
    @SerializedName("n") var n: Int? = null,
    @SerializedName("sum") var sum: Double? = null
)

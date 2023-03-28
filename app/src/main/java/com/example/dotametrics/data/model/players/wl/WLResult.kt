package com.example.dotametrics.data.model.players.wl

import com.google.gson.annotations.SerializedName

data class WLResult(
    @SerializedName("win") var win: Int? = null,
    @SerializedName("lose") var lose: Int? = null
)

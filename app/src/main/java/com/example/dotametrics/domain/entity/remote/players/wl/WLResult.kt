package com.example.dotametrics.domain.entity.remote.players.wl

import com.google.gson.annotations.SerializedName

data class WLResult(
    @SerializedName("win") var win: Int? = null,
    @SerializedName("lose") var lose: Int? = null
) {
    fun isNull() = win == null && lose == null
}

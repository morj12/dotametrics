package com.example.dotametrics.data.remote.model.matches

import com.google.gson.annotations.SerializedName

data class PermanentBuffs(
    @SerializedName("permanent_buff") var permanentBuff: Int? = null,
    @SerializedName("stack_count") var stackCount: Int? = null,
    @SerializedName("grant_time") var grantTime: Int? = null
)
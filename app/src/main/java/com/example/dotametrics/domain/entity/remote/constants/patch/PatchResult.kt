package com.example.dotametrics.domain.entity.remote.constants.patch

import com.google.gson.annotations.SerializedName

data class PatchResult(
    @SerializedName("name") var name: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("id") var id: Int? = null
)
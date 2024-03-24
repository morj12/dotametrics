package com.example.dotametrics.domain.entity.remote.constants.lobbytypes

import com.google.gson.annotations.SerializedName

data class LobbyTypeResult(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
)

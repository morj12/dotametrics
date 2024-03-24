package com.example.dotametrics.data.remote.model.constants.lobbytypes

import com.google.gson.annotations.SerializedName

data class LobbyTypeResult(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
)

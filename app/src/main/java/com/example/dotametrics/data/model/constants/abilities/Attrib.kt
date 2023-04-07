package com.example.dotametrics.data.model.constants.abilities

import com.google.gson.annotations.SerializedName

data class Attrib(
    @SerializedName("key") var key: String? = null,
    @SerializedName("header") var header: String? = null,
    @SerializedName("value") var value: String? = null
)
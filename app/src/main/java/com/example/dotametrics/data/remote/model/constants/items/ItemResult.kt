package com.example.dotametrics.data.remote.model.constants.items

import com.google.gson.annotations.SerializedName

data class ItemResult(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("dname") var dname: String? = null
)
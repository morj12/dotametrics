package com.example.dotametrics.data.model.constants.items

import com.google.gson.annotations.SerializedName

data class ItemResult(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("img") var img: String? = null,
    @SerializedName("dname") var dname: String? = null
)
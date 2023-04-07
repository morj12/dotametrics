package com.example.dotametrics.data.model.constants.abilities

import com.google.gson.annotations.SerializedName

data class AbilityResult(
    @SerializedName("dname") var dname: String? = null,
    @SerializedName("img") var img: String? = null
)


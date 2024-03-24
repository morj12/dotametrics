package com.example.dotametrics.domain.entity.remote.constants.abilities

import com.google.gson.annotations.SerializedName

data class Talents(

    @SerializedName("name") var name: String? = null,
    @SerializedName("level") var level: Int? = null

)
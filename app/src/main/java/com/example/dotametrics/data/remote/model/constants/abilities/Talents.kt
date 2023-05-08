package com.example.dotametrics.data.remote.model.constants.abilities

import com.google.gson.annotations.SerializedName

data class Talents(

    @SerializedName("name") var name: String? = null,
    @SerializedName("level") var level: Int? = null

)
package com.example.dotametrics.data.remote.model.constants.abilities

import com.google.gson.annotations.SerializedName

data class HeroAbilitiesResult(
    @SerializedName("abilities") var abilities: ArrayList<String> = arrayListOf(),
    @SerializedName("talents") var talents: ArrayList<Talents> = arrayListOf()
)

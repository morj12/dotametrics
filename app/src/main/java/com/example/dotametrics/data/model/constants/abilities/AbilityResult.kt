package com.example.dotametrics.data.model.constants.abilities

import com.example.dotametrics.data.service.StringJsonAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class AbilityResult(
    @SerializedName("dname") var dname: String? = null,
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("behavior")
    var behavior: CustomArrayList<String> = CustomArrayList(),
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("attrib") var attrib: ArrayList<Attrib> = arrayListOf(),
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("mc")
    var mc: CustomArrayList<String> = CustomArrayList(),
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("cd")
    var cd: CustomArrayList<String> = CustomArrayList(),
    @SerializedName("img") var img: String? = null,
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("bkbpierce")
    var bkbPierce: CustomArrayList<String> = CustomArrayList(),
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("dmg_type")
    var dmgType: CustomArrayList<String> = CustomArrayList()
)

class CustomArrayList<T> : ArrayList<T>()


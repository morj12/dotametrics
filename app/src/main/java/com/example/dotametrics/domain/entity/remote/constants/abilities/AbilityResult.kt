package com.example.dotametrics.domain.entity.remote.constants.abilities

import com.example.dotametrics.data.remote.service.StringJsonAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class AbilityResult(
    @SerializedName("dname") var name: String? = null,
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("behavior")
    var behavior: CustomArrayList<String> = CustomArrayList(),
    @SerializedName("desc") var description: String? = null,
    @SerializedName("attrib") var attributes: ArrayList<Attrib> = arrayListOf(),
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("mc")
    var manaCost: CustomArrayList<String> = CustomArrayList(),
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("cd")
    var coolDown: CustomArrayList<String> = CustomArrayList(),
    @SerializedName("img") var img: String? = null,
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("bkbpierce")
    var bkbPierce: CustomArrayList<String> = CustomArrayList(),
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("dmg_type")
    var dmgType: CustomArrayList<String> = CustomArrayList()
)

class CustomArrayList<T> : ArrayList<T>()


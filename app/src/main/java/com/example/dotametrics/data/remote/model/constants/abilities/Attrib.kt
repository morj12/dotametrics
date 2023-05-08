package com.example.dotametrics.data.remote.model.constants.abilities

import com.example.dotametrics.data.remote.service.StringJsonAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class Attrib(
    @SerializedName("key") var key: String? = null,
    @SerializedName("header") var header: String? = null,
    @JsonAdapter(value = StringJsonAdapter::class)
    @SerializedName("value")
    var value: CustomArrayList<String> = CustomArrayList()
)
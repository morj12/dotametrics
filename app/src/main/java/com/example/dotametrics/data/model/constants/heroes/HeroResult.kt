package com.example.dotametrics.data.model.constants.heroes

import com.google.gson.annotations.SerializedName

data class HeroResult(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("localized_name") var localizedName: String? = null,
    @SerializedName("primary_attr") var primaryAttr: String? = null,
    @SerializedName("attack_type") var attackType: String? = null,
    @SerializedName("roles") var roles: ArrayList<String> = arrayListOf(),
    @SerializedName("img") var img: String? = null,
    @SerializedName("icon") var icon: String? = null,
    @SerializedName("str_gain") var strGain: Double? = null,
    @SerializedName("agi_gain") var agiGain: Double? = null,
    @SerializedName("int_gain") var intGain: Double? = null,
    @SerializedName("attack_range") var attackRange: Int? = null,
    @SerializedName("move_speed") var moveSpeed: Int? = null,
)

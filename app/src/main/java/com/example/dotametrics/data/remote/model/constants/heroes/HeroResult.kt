package com.example.dotametrics.data.remote.model.constants.heroes

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    @SerializedName("base_str") var baseStr: Double? = null,
    @SerializedName("base_agi") var baseAgi: Double? = null,
    @SerializedName("base_int") var baseInt: Double? = null,
    @SerializedName("base_attack_time") var baseAttackTime: Double? = null,
    @SerializedName("base_mr") var baseMr: Double? = null,
    @SerializedName("attack_range") var attackRange: Int? = null,
    @SerializedName("move_speed") var moveSpeed: Int? = null,
) : Parcelable

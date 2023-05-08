package com.example.dotametrics.data.remote.model.constants.aghs

import com.google.gson.annotations.SerializedName

data class AghsResult(
    @SerializedName("hero_name") var heroName: String? = null,
    @SerializedName("hero_id") var heroId: Int? = null,
    @SerializedName("has_scepter") var hasScepter: Boolean? = null,
    @SerializedName("scepter_desc") var scepterDesc: String? = null,
    @SerializedName("scepter_skill_name") var scepterSkillName: String? = null,
    @SerializedName("scepter_new_skill") var scepterNewSkill: Boolean? = null,
    @SerializedName("has_shard") var hasShard: Boolean? = null,
    @SerializedName("shard_desc") var shardDesc: String? = null,
    @SerializedName("shard_skill_name") var shardSkillName: String? = null,
    @SerializedName("shard_new_skill") var shardNewSkill: Boolean? = null
)

package com.example.dotametrics.domain.entity.remote.constants.aghs

import com.google.gson.annotations.SerializedName

data class AghsResult(
    @SerializedName("hero_id") var heroId: Int? = null,
    @SerializedName("scepter_desc") var scepterDescription: String? = null,
    @SerializedName("scepter_skill_name") var scepterSkillName: String? = null,
    @SerializedName("shard_desc") var shardDescription: String? = null,
    @SerializedName("shard_skill_name") var shardSkillName: String? = null,
)

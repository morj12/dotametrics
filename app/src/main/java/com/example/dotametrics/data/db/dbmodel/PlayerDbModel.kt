package com.example.dotametrics.data.db.dbmodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class PlayerDbModel(
    @PrimaryKey
    val id: Long,
    val name: String? = null,
    val avatar: String? = null
)
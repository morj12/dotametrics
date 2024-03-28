package com.example.dotametrics.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dotametrics.data.local.dao.PlayerDao
import com.example.dotametrics.domain.entity.local.PlayerDbModel

@Database(entities = [PlayerDbModel::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
}
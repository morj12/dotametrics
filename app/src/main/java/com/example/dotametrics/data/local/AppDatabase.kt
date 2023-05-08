package com.example.dotametrics.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dotametrics.data.local.dao.PlayerDao
import com.example.dotametrics.data.local.dbmodel.PlayerDbModel

@Database(entities = [PlayerDbModel::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao

    companion object {

        private const val DB_NAME = "dota_metrics_db"
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(application: Application): AppDatabase {
            INSTANCE?.let { return it }
            synchronized(this) {
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(
                    application.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }

    }
}
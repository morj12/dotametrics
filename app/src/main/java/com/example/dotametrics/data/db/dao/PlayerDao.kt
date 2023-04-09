package com.example.dotametrics.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dotametrics.data.db.dbmodel.PlayerDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: PlayerDbModel)

    @Query("SELECT * FROM player")
    fun getPlayers(): Flow<List<PlayerDbModel>>

    @Query("DELETE FROM player WHERE id = :id")
    suspend fun deletePlayer(id: Long)

    @Query("SELECT * FROM player where id = :id")
    suspend fun getPlayer(id: Long): PlayerDbModel?

}
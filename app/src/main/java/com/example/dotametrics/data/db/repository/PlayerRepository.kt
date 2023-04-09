package com.example.dotametrics.data.db.repository

import com.example.dotametrics.data.db.AppDatabase
import com.example.dotametrics.data.db.dbmodel.PlayerDbModel
import kotlinx.coroutines.flow.Flow

class PlayerRepository(database: AppDatabase) {

    private val dao = database.playerDao()

    suspend fun insertPlayer(playerDbModel: PlayerDbModel) {
        dao.insertPlayer(playerDbModel)
    }

    suspend fun deletePlayer(id: Long) {
        dao.deletePlayer(id)
    }

    fun getPlayers(): Flow<List<PlayerDbModel>> {
        return dao.getPlayers()
    }

    suspend fun getPlayer(id: Long): PlayerDbModel? {
        return dao.getPlayer(id)
    }

}
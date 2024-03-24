package com.example.dotametrics.data.local.repository

import com.example.dotametrics.data.local.AppDatabase
import com.example.dotametrics.data.local.dbmodel.PlayerDbModel
import com.example.dotametrics.domain.repository.IPlayerRepository
import kotlinx.coroutines.flow.Flow

class PlayerRepository(database: AppDatabase): IPlayerRepository {

    private val dao = database.playerDao()

    override suspend fun insertPlayer(playerDbModel: PlayerDbModel) {
        dao.insertPlayer(playerDbModel)
    }

    override suspend fun deletePlayer(id: Long) {
        dao.deletePlayer(id)
    }

    override fun getPlayers(): Flow<List<PlayerDbModel>> {
        return dao.getPlayers()
    }

    override suspend fun getPlayer(id: Long): PlayerDbModel? {
        return dao.getPlayer(id)
    }

}
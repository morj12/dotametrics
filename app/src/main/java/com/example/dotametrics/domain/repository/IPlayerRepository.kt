package com.example.dotametrics.domain.repository

import com.example.dotametrics.data.local.dbmodel.PlayerDbModel
import kotlinx.coroutines.flow.Flow

interface IPlayerRepository {
    suspend fun insertPlayer(playerDbModel: PlayerDbModel)
    suspend fun deletePlayer(id: Long)
    fun getPlayers(): Flow<List<PlayerDbModel>>
    suspend fun getPlayer(id: Long): PlayerDbModel?
}
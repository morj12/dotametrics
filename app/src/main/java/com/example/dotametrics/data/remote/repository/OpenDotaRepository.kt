package com.example.dotametrics.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.dotametrics.data.remote.paging.players.MatchDataSource
import com.example.dotametrics.data.remote.service.DotaService
import com.example.dotametrics.domain.entity.remote.BasicResponse
import com.example.dotametrics.domain.entity.remote.players.matches.MatchesResult
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import com.example.dotametrics.util.DotaLogger
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class OpenDotaRepository @Inject constructor(private val service: DotaService) :
    IOpenDotaRepository {

    private suspend fun <T> handleResponse(
        request: suspend () -> Response<T>
    ): BasicResponse<T> {
        return try {
            val response = request()
            if (response.isSuccessful) {
                BasicResponse(response.body(), "null")
            } else {
                val url = response.raw().request.url.toString()
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                DotaLogger.logApiError(url, errorBody)

                val isRateLimit = response.code() == 429
                BasicResponse(
                    null,
                    if (isRateLimit) "RATE_LIMIT_EXCEEDED" else "API Error: $errorBody",
                    isRateLimit
                )
            }
        } catch (e: Exception) {
            DotaLogger.logApiException(e.message, e)
            BasicResponse(null, e.message ?: "Exception")
        }
    }

    override suspend fun getSearchResults(name: String) = handleResponse {
        service.getSearchResults(name)
    }

    override suspend fun getPlayersResults(id: String) = handleResponse {
        service.getPlayersResults(id)
    }

    override suspend fun getWLResults(
        id: String,
        limit: Int?,
        lobbyType: Int?,
        heroId: Int?
    ) = handleResponse {
        service.getWLResults(id, limit, lobbyType, heroId)
    }

    override suspend fun getTotals(id: String) = handleResponse {
        service.getTotals(id)
    }

    override suspend fun getPlayerHeroesResults(id: String) = handleResponse {
        service.getPlayerHeroesResults(id)
    }

    override suspend fun getConstHeroes() = handleResponse {
        service.getConstHeroes()
    }

    override suspend fun getPeers(id: String) = handleResponse {
        service.getPeers(id)
    }

    override suspend fun getMatches(
        id: String,
        limit: Int,
        offset: Long,
        lobbyType: Int?,
        heroId: Int?
    ) = handleResponse {
        service.getMatches(id, limit, offset, lobbyType, heroId)
    }

    override suspend fun getConstLobbyTypes() = handleResponse {
        service.getConstLobbyTypes()
    }

    override suspend fun getMatchData(id: String) = handleResponse {
        service.getMatchData(id)
    }

    override suspend fun getRegions() = handleResponse {
        service.getRegions()
    }

    override suspend fun getItems() = handleResponse {
        service.getItems()
    }

    override suspend fun getAbilityIds() = handleResponse {
        service.getAbilityIds()
    }

    override suspend fun getAbilities() = handleResponse {
        service.getAbilities()
    }

    override suspend fun getTeams() = handleResponse {
        service.getTeams()
    }

    override suspend fun getTeamPlayers(id: String) = handleResponse {
        service.getTeamPlayers(id)
    }

    override fun loadPagingMatches(
        userId: String,
        lobbyType: Int?,
        heroId: Int?
    ): Flow<PagingData<MatchesResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = MatchDataSource.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MatchDataSource(service, userId, lobbyType, heroId) }
        ).flow
    }

    override suspend fun getTeamMatches(id: String) = handleResponse {
        service.getTeamMatches(id)
    }

    override suspend fun getTeamHeroes(id: String) = handleResponse {
        service.getTeamHeroes(id)
    }

    override suspend fun getAghs() = handleResponse {
        service.getAghs()
    }

    override suspend fun getHeroAbilities() = handleResponse {
        service.getHeroAbilities()
    }

    override suspend fun getHeroLore() = handleResponse {
        service.getHeroLore()
    }
}

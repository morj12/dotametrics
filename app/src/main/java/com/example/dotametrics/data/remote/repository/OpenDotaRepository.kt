package com.example.dotametrics.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.dotametrics.data.remote.paging.players.MatchDataSource
import com.example.dotametrics.data.remote.service.DotaService
import com.example.dotametrics.domain.entity.remote.BasicResponse
import com.example.dotametrics.domain.entity.remote.constants.abilities.AbilityResult
import com.example.dotametrics.domain.entity.remote.constants.abilities.HeroAbilitiesResult
import com.example.dotametrics.domain.entity.remote.constants.aghs.AghsResult
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.domain.entity.remote.constants.items.ItemResult
import com.example.dotametrics.domain.entity.remote.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.domain.entity.remote.matches.MatchDataResult
import com.example.dotametrics.domain.entity.remote.players.PlayersResult
import com.example.dotametrics.domain.entity.remote.players.heroes.PlayerHeroResult
import com.example.dotametrics.domain.entity.remote.players.matches.MatchesResult
import com.example.dotametrics.domain.entity.remote.players.peers.PeersResult
import com.example.dotametrics.domain.entity.remote.players.totals.TotalsResult
import com.example.dotametrics.domain.entity.remote.players.wl.WLResult
import com.example.dotametrics.domain.entity.remote.search.SearchResult
import com.example.dotametrics.domain.entity.remote.teams.TeamsResult
import com.example.dotametrics.domain.entity.remote.teams.heroes.TeamHeroesResult
import com.example.dotametrics.domain.entity.remote.teams.matches.TeamMatchesResult
import com.example.dotametrics.domain.entity.remote.teams.players.TeamPlayersResult
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OpenDotaRepository @Inject constructor(private val service: DotaService) :
    IOpenDotaRepository {

    override suspend fun getSearchResults(name: String): BasicResponse<List<SearchResult>> {
        val response = service.getSearchResults(name)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getPlayersResults(id: String): BasicResponse<PlayersResult> {
        val response = service.getPlayersResults(id)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getWLResults(
        id: String,
        limit: Int?,
        lobbyType: Int?,
        heroId: Int?
    ): BasicResponse<WLResult> {
        val response = service.getWLResults(id, limit, lobbyType, heroId)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getTotals(id: String): BasicResponse<List<TotalsResult>> {
        val response = service.getTotals(id)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getPlayerHeroesResults(id: String): BasicResponse<List<PlayerHeroResult>> {
        val response = service.getPlayerHeroesResults(id)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getConstHeroes(): BasicResponse<Map<String, HeroResult>> {
        val response = service.getConstHeroes()
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getPeers(id: String): BasicResponse<List<PeersResult>> {
        val response = service.getPeers(id)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getMatches(
        id: String,
        limit: Int,
        offset: Long,
        lobbyType: Int?,
        heroId: Int?
    ): BasicResponse<List<MatchesResult>> {
        val response = service.getMatches(id, limit, offset, lobbyType, heroId)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getConstLobbyTypes(): BasicResponse<Map<String, LobbyTypeResult>> {
        val response = service.getConstLobbyTypes()
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getMatchData(id: String): BasicResponse<MatchDataResult> {
        val response = service.getMatchData(id)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getRegions(): BasicResponse<Map<String, String>> {
        val response = service.getRegions()
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getItems(): BasicResponse<Map<String, ItemResult>> {
        val response = service.getItems()
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getAbilityIds(): BasicResponse<Map<String, String>> {
        val response = service.getAbilityIds()
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getAbilities(): BasicResponse<Map<String, AbilityResult>> {
        val response = service.getAbilities()
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getTeams(): BasicResponse<List<TeamsResult>> {
        val response = service.getTeams()
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getTeamPlayers(id: String): BasicResponse<List<TeamPlayersResult>> {
        val response = service.getTeamPlayers(id)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
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

    override suspend fun getTeamMatches(id: String): BasicResponse<List<TeamMatchesResult>> {
        val response = service.getTeamMatches(id)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getTeamHeroes(id: String): BasicResponse<List<TeamHeroesResult>> {
        val response = service.getTeamHeroes(id)
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getAghs(): BasicResponse<List<AghsResult>> {
        val response = service.getAghs()
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getHeroAbilities(): BasicResponse<Map<String, HeroAbilitiesResult>> {
        val response = service.getHeroAbilities()
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }

    override suspend fun getHeroLore(): BasicResponse<Map<String, String>> {
        val response = service.getHeroLore()
        return BasicResponse(response.body(), response.errorBody()?.string() ?: "null")
    }
}

package com.example.dotametrics.domain.repository

import androidx.paging.PagingData
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
import kotlinx.coroutines.flow.Flow

interface IOpenDotaRepository {
    suspend fun getSearchResults(name: String): BasicResponse<List<SearchResult>>
    suspend fun getPlayersResults(id: String): BasicResponse<PlayersResult>
    suspend fun getWLResults(
        id: String,
        limit: Int?,
        lobbyType: Int?,
        heroId: Int?
    ): BasicResponse<WLResult>

    suspend fun getTotals(id: String): BasicResponse<List<TotalsResult>>
    suspend fun getPlayerHeroesResults(id: String): BasicResponse<List<PlayerHeroResult>>
    suspend fun getConstHeroes(): BasicResponse<Map<String, HeroResult>>
    suspend fun getPeers(id: String): BasicResponse<List<PeersResult>>
    suspend fun getMatches(
        id: String,
        limit: Int,
        offset: Long,
        lobbyType: Int?,
        heroId: Int?
    ): BasicResponse<List<MatchesResult>>

    suspend fun getConstLobbyTypes(): BasicResponse<Map<String, LobbyTypeResult>>
    suspend fun getMatchData(id: String): BasicResponse<MatchDataResult>
    suspend fun getRegions(): BasicResponse<Map<String, String>>
    suspend fun getItems(): BasicResponse<Map<String, ItemResult>>
    suspend fun getAbilityIds(): BasicResponse<Map<String, String>>
    suspend fun getAbilities(): BasicResponse<Map<String, AbilityResult>>
    suspend fun getTeams(): BasicResponse<List<TeamsResult>>
    suspend fun getTeamPlayers(id: String): BasicResponse<List<TeamPlayersResult>>
    suspend fun getTeamMatches(id: String): BasicResponse<List<TeamMatchesResult>>
    suspend fun getTeamHeroes(id: String): BasicResponse<List<TeamHeroesResult>>
    suspend fun getAghs(): BasicResponse<List<AghsResult>>
    suspend fun getHeroAbilities(): BasicResponse<Map<String, HeroAbilitiesResult>>
    suspend fun getHeroLore(): BasicResponse<Map<String, String>>
    fun loadPagingMatches(
        userId: String,
        lobbyType: Int?,
        heroId: Int?
    ): Flow<PagingData<MatchesResult>>
}

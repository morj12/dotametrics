package com.example.dotametrics.data.remote.service

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
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface DotaService {

    @GET("search")
    suspend fun getSearchResults(@Query("q") name: String): Response<List<SearchResult>>

    @GET("players/{id}")
    suspend fun getPlayersResults(@Path("id") id: String): Response<PlayersResult>

    @GET("players/{id}/wl")
    suspend fun getWLResults(
        @Path("id") id: String,
        @Query("limit") limit: Int?,
        @Query("lobby_type") lobbyType: Int?,
        @Query("hero_id") heroId: Int?
    ): Response<WLResult>

    @GET("players/{id}/totals")
    suspend fun getTotals(@Path("id") id: String): Response<List<TotalsResult>>

    @GET("players/{id}/heroes")
    suspend fun getPlayerHeroesResults(@Path("id") id: String): Response<List<PlayerHeroResult>>

    @GET("constants/heroes")
    suspend fun getConstHeroes(): Response<Map<String, HeroResult>>

    @GET("players/{id}/peers")
    suspend fun getPeers(@Path("id") id: String): Response<List<PeersResult>>

    @GET("players/{id}/matches")
    suspend fun getMatches(
        @Path("id") id: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Long,
        @Query("lobby_type") lobbyType: Int?,
        @Query("hero_id") heroId: Int?
    ): Response<List<MatchesResult>>

    @GET("constants/lobby_type")
    suspend fun getConstLobbyTypes(): Response<Map<String, LobbyTypeResult>>

    @GET("matches/{id}")
    suspend fun getMatchData(@Path("id") id: String): Response<MatchDataResult>

    @GET("constants/region")
    suspend fun getRegions(): Response<Map<String, String>>

    @GET("constants/items")
    suspend fun getItems(): Response<Map<String, ItemResult>>

    @GET("constants/ability_ids")
    suspend fun getAbilityIds(): Response<Map<String, String>>

    @GET("constants/abilities")
    suspend fun getAbilities(): Response<Map<String, AbilityResult>>

    @GET("teams")
    suspend fun getTeams(): Response<List<TeamsResult>>

    @GET("teams/{id}/players")
    suspend fun getTeamPlayers(@Path("id") id: String): Response<List<TeamPlayersResult>>

    @GET("teams/{id}/matches")
    suspend fun getTeamMatches(@Path("id") id: String): Response<List<TeamMatchesResult>>

    @GET("teams/{id}/heroes")
    suspend fun getTeamHeroes(@Path("id") id: String): Response<List<TeamHeroesResult>>

    @GET("constants/aghs_desc")
    suspend fun getAghs(): Response<List<AghsResult>>

    @GET("constants/hero_abilities")
    suspend fun getHeroAbilities(): Response<Map<String, HeroAbilitiesResult>>

    @GET("constants/hero_lore")
    suspend fun getHeroLore(): Response<Map<String, String>>

}

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
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface DotaService {

    @GET("search")
    fun getSearchResults(@Query("q") name: String): Call<List<SearchResult>>

    @GET("players/{id}")
    fun getPlayersResults(@Path("id") id: String): Call<PlayersResult>

    @GET("players/{id}/wl")
    fun getWLResults(
        @Path("id") id: String,
        @Query("limit") limit: Int?,
        @Query("lobby_type") lobbyType: Int?,
        @Query("hero_id") heroId: Int?
    ): Call<WLResult>

    @GET("players/{id}/totals")
    fun getTotals(@Path("id") id: String): Call<List<TotalsResult>>

    @GET("players/{id}/heroes")
    fun getPlayerHeroesResults(@Path("id") id: String): Call<List<PlayerHeroResult>>

    @GET("constants/heroes")
    fun getConstHeroes(): Call<Map<String, HeroResult>>

    @GET("players/{id}/peers")
    fun getPeers(@Path("id") id: String): Call<List<PeersResult>>

    @GET("players/{id}/matches")
    fun getMatches(
        @Path("id") id: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Long,
        @Query("lobby_type") lobbyType: Int?,
        @Query("hero_id") heroId: Int?
    ): Call<List<MatchesResult>>

    @GET("constants/lobby_type")
    fun getConstLobbyTypes(): Call<Map<String, LobbyTypeResult>>

    @GET("matches/{id}")
    fun getMatchData(@Path("id") id: String): Call<MatchDataResult>

    @GET("constants/region")
    fun getRegions(): Call<Map<String, String>>

    @GET("constants/items")
    fun getItems(): Call<Map<String, ItemResult>>

    @GET("constants/ability_ids")
    fun getAbilityIds(): Call<Map<String, String>>

    @GET("constants/abilities")
    fun getAbilities(): Call<Map<String, AbilityResult>>

    @GET("teams")
    fun getTeams(): Call<List<TeamsResult>>

    @GET("teams/{id}/players")
    fun getTeamPlayers(@Path("id") id: String): Call<List<TeamPlayersResult>>

    @GET("teams/{id}/matches")
    fun getTeamMatches(@Path("id") id: String): Call<List<TeamMatchesResult>>

    @GET("teams/{id}/heroes")
    fun getTeamHeroes(@Path("id") id: String): Call<List<TeamHeroesResult>>

    @GET("constants/aghs_desc")
    fun getAghs(): Call<List<AghsResult>>

    @GET("constants/hero_abilities")
    fun getHeroAbilities(): Call<Map<String, HeroAbilitiesResult>>

    @GET("constants/hero_lore")
    fun getHeroLore(): Call<Map<String, String>>

}
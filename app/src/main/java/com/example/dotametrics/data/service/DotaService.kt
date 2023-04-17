package com.example.dotametrics.data.service

import com.example.dotametrics.data.model.constants.abilities.AbilityResult
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.model.constants.items.ItemResult
import com.example.dotametrics.data.model.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.data.model.constants.patch.PatchResult
import com.example.dotametrics.data.model.constants.patch.PatchNotesResult
import com.example.dotametrics.data.model.matches.MatchDataResult
import com.example.dotametrics.data.model.players.PlayersResult
import com.example.dotametrics.data.model.players.heroes.PlayerHeroResult
import com.example.dotametrics.data.model.players.matches.MatchesResult
import com.example.dotametrics.data.model.players.peers.PeersResult
import com.example.dotametrics.data.model.players.totals.TotalsResult
import com.example.dotametrics.data.model.players.wl.WLResult
import com.example.dotametrics.data.model.search.SearchResult
import com.example.dotametrics.data.model.teams.TeamsResult
import com.example.dotametrics.data.model.teams.heroes.TeamHeroesResult
import com.example.dotametrics.data.model.teams.matches.TeamMatchesResult
import com.example.dotametrics.data.model.teams.players.TeamPlayersResult
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

    @GET("constants/patch")
    fun getPatches(): Call<List<PatchResult>>

    @GET("constants/patchnotes")
    fun getPatchNotes(): Call<Map<String, PatchNotesResult>>

    @GET("teams")
    fun getTeams(): Call<List<TeamsResult>>

    @GET("teams/{id}/players")
    fun getTeamPlayers(@Path("id") id: String): Call<List<TeamPlayersResult>>

    @GET("teams/{id}/matches")
    fun getTeamMatches(@Path("id") id: String): Call<List<TeamMatchesResult>>

    @GET("teams/{id}/heroes")
    fun getTeamHeroes(@Path("id") id: String): Call<List<TeamHeroesResult>>

}
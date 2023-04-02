package com.example.dotametrics.data.service

import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.model.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.data.model.matches.MatchDataResult
import com.example.dotametrics.data.model.players.PlayersResult
import com.example.dotametrics.data.model.players.heroes.PlayerHeroResult
import com.example.dotametrics.data.model.players.matches.MatchesResult
import com.example.dotametrics.data.model.players.peers.PeersResult
import com.example.dotametrics.data.model.players.totals.TotalsResult
import com.example.dotametrics.data.model.players.wl.WLResult
import com.example.dotametrics.data.model.search.SearchResult
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
    fun getWLResults(@Path("id") id: String): Call<WLResult>

    @GET("players/{id}/totals")
    fun getTotals(@Path("id") id: String): Call<List<TotalsResult>>

    @GET("players/{id}/heroes")
    fun getPlayerHeroesResults(@Path("id") id: String): Call<List<PlayerHeroResult>>

    // Use map because of multiple classes with same data
    @GET("constants/heroes")
    fun getConstHeroes(): Call<Map<String, HeroResult>>

    @GET("players/{id}/peers")
    fun getPeers(@Path("id") id: String): Call<List<PeersResult>>

    @GET("players/{id}/matches")
    fun getMatches(
        @Path("id") id: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Long
    ): Call<List<MatchesResult>>

    @GET("constants/lobby_type")
    fun getConstLobbyTypes(): Call<Map<String, LobbyTypeResult>>

    @GET("matches/{id}")
    fun getMatchData(@Path("id") id: String): Call<MatchDataResult>

}
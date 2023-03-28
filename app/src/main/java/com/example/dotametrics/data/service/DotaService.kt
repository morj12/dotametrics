package com.example.dotametrics.data.service

import com.example.dotametrics.data.model.players.PlayersResult
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

}
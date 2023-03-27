package com.example.dotametrics.data.service

import com.example.dotametrics.data.model.search.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface DotaService {

    @GET("search")
    fun getSearchResults(@Query("q") name: String): Call<List<SearchResult>>

}
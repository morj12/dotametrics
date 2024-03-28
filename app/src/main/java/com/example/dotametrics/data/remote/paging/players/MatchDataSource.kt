package com.example.dotametrics.data.remote.paging.players

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.dotametrics.data.remote.service.DotaService
import com.example.dotametrics.domain.entity.remote.players.matches.MatchesResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchDataSource(
    private val service: DotaService,
    private val id: String,
    private val lobbyType: Int?,
    private val heroId: Int?,
    private val errorListener: (String) -> Unit
) :
    PageKeyedDataSource<Long, MatchesResult>() {

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, MatchesResult>) {
        try {
            service.getMatches(id, PAGE_SIZE, params.key, lobbyType, heroId)
                .enqueue(object : Callback<List<MatchesResult>> {
                    override fun onResponse(
                        call: Call<List<MatchesResult>>,
                        response: Response<List<MatchesResult>>
                    ) {
                        callback.onResult(response.body() ?: listOf(), params.key + PAGE_SIZE)
                    }

                    override fun onFailure(call: Call<List<MatchesResult>>, t: Throwable) {
                        errorListener(t.message.toString())
                    }
                })
        } catch (e: Exception) {
            Log.e("DOTA_RETROFIT", e.message.toString())
            callback.onResult(listOf(), params.key)
        }

    }

    override fun loadBefore(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, MatchesResult>
    ) {
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, MatchesResult>
    ) {
        try {
            service.getMatches(id, PAGE_SIZE, 0, lobbyType, heroId)
                .enqueue(object : Callback<List<MatchesResult>> {
                    override fun onResponse(
                        call: Call<List<MatchesResult>>,
                        response: Response<List<MatchesResult>>
                    ) {
                        callback.onResult(response.body() ?: listOf(), null, PAGE_SIZE.toLong())
                    }

                    override fun onFailure(call: Call<List<MatchesResult>>, t: Throwable) {
                        errorListener(t.message.toString())
                        Log.e("DOTA_RETROFIT", t.message.toString())
                    }
                })
        } catch (e: Exception) {
            errorListener(e.message.toString())
            Log.e("DOTA_RETROFIT", e.message.toString())
            callback.onResult(listOf(), null, PAGE_SIZE.toLong())
        }
    }

    companion object {

        const val PAGE_SIZE = 100

    }
}
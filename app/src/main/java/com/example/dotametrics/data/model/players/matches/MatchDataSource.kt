package com.example.dotametrics.data.model.players.matches

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.dotametrics.data.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchDataSource(
    private val id: String,
    private val lobbyType: Int?,
    private val heroId: Int?,
    private val errorListener: (String) -> Unit
) :
    PageKeyedDataSource<Long, MatchesResult>() {

    private val retrofit = RetrofitInstance.getService()

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, MatchesResult>) {
        retrofit.getMatches(id, PAGE_SIZE, params.key, lobbyType, heroId)
            .enqueue(object : Callback<List<MatchesResult>> {
                override fun onResponse(
                    call: Call<List<MatchesResult>>,
                    response: Response<List<MatchesResult>>
                ) {
                    Log.d("RETROFIT_CALL", "MatchDataSource: loadAfter")
                    callback.onResult(response.body() ?: listOf(), params.key + PAGE_SIZE)
                }

                override fun onFailure(call: Call<List<MatchesResult>>, t: Throwable) {
                    errorListener(t.message.toString())
                }

            })
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
        retrofit.getMatches(id, PAGE_SIZE, 0, lobbyType, heroId).enqueue(object : Callback<List<MatchesResult>> {
            override fun onResponse(
                call: Call<List<MatchesResult>>,
                response: Response<List<MatchesResult>>
            ) {
                Log.d("RETROFIT_CALL", "MatchDataSource: loadInitial")
                callback.onResult(response.body() ?: listOf(), null, PAGE_SIZE.toLong())
            }

            override fun onFailure(call: Call<List<MatchesResult>>, t: Throwable) {
                errorListener(t.message.toString())
            }

        })
    }

    companion object {

        const val PAGE_SIZE = 100

    }
}
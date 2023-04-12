package com.example.dotametrics.data.model.players.matches

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class MatchDataSourceFactory(
    private val id: String,
    private val lobbyType: Int?,
    private val heroId: Int?,
    private val errorListener: (String) -> Unit
) :
    DataSource.Factory<Long, MatchesResult>() {

    val mutableLiveData = MutableLiveData<MatchDataSource>()
    private lateinit var matchDataSource: MatchDataSource

    override fun create(): DataSource<Long, MatchesResult> {
        matchDataSource = MatchDataSource(id, lobbyType, heroId, errorListener)
        mutableLiveData.postValue(matchDataSource)
        return matchDataSource
    }
}
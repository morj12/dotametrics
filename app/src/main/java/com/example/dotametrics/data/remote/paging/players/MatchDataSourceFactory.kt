package com.example.dotametrics.data.remote.paging.players

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.dotametrics.data.remote.service.DotaService
import com.example.dotametrics.domain.entity.remote.players.matches.MatchesResult

class MatchDataSourceFactory(
    private val service: DotaService,
    private val id: String,
    private val lobbyType: Int?,
    private val heroId: Int?,
    private val errorListener: (String) -> Unit
) :
    DataSource.Factory<Long, MatchesResult>() {

    val mutableLiveData = MutableLiveData<MatchDataSource>()
    private lateinit var matchDataSource: MatchDataSource

    override fun create(): DataSource<Long, MatchesResult> {
        matchDataSource = MatchDataSource(service, id, lobbyType, heroId, errorListener)
        mutableLiveData.postValue(matchDataSource)
        return matchDataSource
    }
}
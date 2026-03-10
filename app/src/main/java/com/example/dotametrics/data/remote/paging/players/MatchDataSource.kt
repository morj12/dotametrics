package com.example.dotametrics.data.remote.paging.players

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dotametrics.data.remote.service.DotaService
import com.example.dotametrics.domain.entity.remote.players.matches.MatchesResult

class MatchDataSource(
    private val service: DotaService,
    private val id: String,
    private val lobbyType: Int?,
    private val heroId: Int?
) : PagingSource<Long, MatchesResult>() {

    override fun getRefreshKey(state: PagingState<Long, MatchesResult>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, MatchesResult> {
        return try {
            val offset = params.key ?: 0L
            val response = service.getMatches(id, params.loadSize, offset, lobbyType, heroId)
            val data = response.body() ?: emptyList()

            LoadResult.Page(
                data = data,
                prevKey = if (offset == 0L) null else offset - params.loadSize,
                nextKey = if (data.isEmpty()) null else offset + params.loadSize
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val PAGE_SIZE = 100
    }
}

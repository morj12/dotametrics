package com.example.dotametrics.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.dotametrics.domain.entity.remote.constants.abilities.AbilityResult
import com.example.dotametrics.domain.entity.remote.constants.abilities.HeroAbilitiesResult
import com.example.dotametrics.domain.entity.remote.constants.aghs.AghsResult
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.domain.entity.remote.constants.items.ItemResult
import com.example.dotametrics.domain.entity.remote.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.domain.entity.remote.constants.patch.PatchNotesResult
import com.example.dotametrics.domain.entity.remote.constants.patch.PatchResult
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
import com.example.dotametrics.data.remote.paging.players.MatchDataSource
import com.example.dotametrics.data.remote.paging.players.MatchDataSourceFactory
import com.example.dotametrics.data.remote.service.DotaService
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import com.example.dotametrics.domain.entity.remote.BasicResponse
import java.util.concurrent.Executors
import javax.inject.Inject

class OpenDotaRepository @Inject constructor(private val service: DotaService): IOpenDotaRepository {

    private lateinit var matchDataSource: LiveData<MatchDataSource>

    private var executor = Executors.newCachedThreadPool()

    override fun getSearchResults(name: String): BasicResponse<List<SearchResult>> {
        val call = service.getSearchResults(name).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getPlayersResults(id: String): BasicResponse<PlayersResult> {
        val call = service.getPlayersResults(id).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getWLResults(
        id: String,
        limit: Int?,
        lobbyType: Int?,
        heroId: Int?
    ): BasicResponse<WLResult> {
        val call = service.getWLResults(id, limit, lobbyType, heroId).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getTotals(id: String): BasicResponse<List<TotalsResult>> {
        val call = service.getTotals(id).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getPlayerHeroesResults(id: String): BasicResponse<List<PlayerHeroResult>> {
        val call = service.getPlayerHeroesResults(id).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getConstHeroes(): BasicResponse<Map<String, HeroResult>> {
        val call = service.getConstHeroes().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getPeers(id: String): BasicResponse<List<PeersResult>> {
        val call = service.getPeers(id).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getMatches(
        id: String,
        limit: Int,
        offset: Long,
        lobbyType: Int?,
        heroId: Int?
    ): BasicResponse<List<MatchesResult>> {
        val call = service.getMatches(id, limit, offset, lobbyType, heroId).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getConstLobbyTypes(): BasicResponse<Map<String, LobbyTypeResult>> {
        val call = service.getConstLobbyTypes().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getMatchData(id: String): BasicResponse<MatchDataResult> {
        val call = service.getMatchData(id).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getRegions(): BasicResponse<Map<String, String>> {
        val call = service.getRegions().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getItems(): BasicResponse<Map<String, ItemResult>> {
        val call = service.getItems().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getAbilityIds(): BasicResponse<Map<String, String>> {
        val call = service.getAbilityIds().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getAbilities(): BasicResponse<Map<String, AbilityResult>> {
        val call = service.getAbilities().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getPatches(): BasicResponse<List<PatchResult>> {
        val call = service.getPatches().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getPatchNotes(): BasicResponse<Map<String, PatchNotesResult>> {
        val call = service.getPatchNotes().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getTeams(): BasicResponse<List<TeamsResult>> {
        val call = service.getTeams().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getTeamPlayers(id: String): BasicResponse<List<TeamPlayersResult>> {
        val call = service.getTeamPlayers(id).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun loadPagingMatches(
        userId: String,
        lobbyType: Int?,
        heroId: Int?,
        errorListener: (String) -> Unit
    ): LiveData<PagedList<MatchesResult>> {
        val factory = MatchDataSourceFactory(service, userId, lobbyType, heroId, errorListener)
        matchDataSource = factory.mutableLiveData
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(MatchDataSource.PAGE_SIZE)
            .setPageSize(MatchDataSource.PAGE_SIZE)
            .setPrefetchDistance(MatchDataSource.PAGE_SIZE / 4)
            .build()

        return LivePagedListBuilder(factory, config).setFetchExecutor(executor).build()
    }

    override fun getTeamMatches(id: String): BasicResponse<List<TeamMatchesResult>> {
        val call = service.getTeamMatches(id).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getTeamHeroes(id: String): BasicResponse<List<TeamHeroesResult>> {
        val call = service.getTeamHeroes(id).execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getAghs(): BasicResponse<List<AghsResult>> {
        val call = service.getAghs().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getHeroAbilities(): BasicResponse<Map<String, HeroAbilitiesResult>> {
        val call = service.getHeroAbilities().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }

    override fun getHeroLore(): BasicResponse<Map<String, String>> {
        val call = service.getHeroLore().execute()
        return BasicResponse(call.body(), call.errorBody()?.string() ?: "null")
    }
}
package com.example.dotametrics.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.dotametrics.data.remote.model.constants.abilities.AbilityResult
import com.example.dotametrics.data.remote.model.constants.abilities.HeroAbilitiesResult
import com.example.dotametrics.data.remote.model.constants.aghs.AghsResult
import com.example.dotametrics.data.remote.model.constants.heroes.HeroResult
import com.example.dotametrics.data.remote.model.constants.items.ItemResult
import com.example.dotametrics.data.remote.model.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.data.remote.model.constants.patch.PatchNotesResult
import com.example.dotametrics.data.remote.model.constants.patch.PatchResult
import com.example.dotametrics.data.remote.model.matches.MatchDataResult
import com.example.dotametrics.data.remote.model.players.PlayersResult
import com.example.dotametrics.data.remote.model.players.heroes.PlayerHeroResult
import com.example.dotametrics.data.remote.model.players.matches.MatchesResult
import com.example.dotametrics.data.remote.model.players.peers.PeersResult
import com.example.dotametrics.data.remote.model.players.totals.TotalsResult
import com.example.dotametrics.data.remote.model.players.wl.WLResult
import com.example.dotametrics.data.remote.model.search.SearchResult
import com.example.dotametrics.data.remote.model.teams.TeamsResult
import com.example.dotametrics.data.remote.model.teams.heroes.TeamHeroesResult
import com.example.dotametrics.data.remote.model.teams.matches.TeamMatchesResult
import com.example.dotametrics.data.remote.model.teams.players.TeamPlayersResult
import com.example.dotametrics.data.remote.paging.players.MatchDataSource
import com.example.dotametrics.data.remote.paging.players.MatchDataSourceFactory
import com.example.dotametrics.data.remote.service.RetrofitInstance
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import com.example.dotametrics.domain.repository.response.BasicResponse
import java.util.concurrent.Executors

class OpenDotaRepository: IOpenDotaRepository {

    private val retrofit = RetrofitInstance.getService()

    private lateinit var matchDataSource: LiveData<MatchDataSource>

    private var executor = Executors.newCachedThreadPool()

    override fun getSearchResults(name: String): BasicResponse<List<SearchResult>> {
        val call = retrofit.getSearchResults(name).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getPlayersResults(id: String): BasicResponse<PlayersResult> {
        val call = retrofit.getPlayersResults(id).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getWLResults(
        id: String,
        limit: Int?,
        lobbyType: Int?,
        heroId: Int?
    ): BasicResponse<WLResult> {
        val call = retrofit.getWLResults(id, limit, lobbyType, heroId).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getTotals(id: String): BasicResponse<List<TotalsResult>> {
        val call = retrofit.getTotals(id).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getPlayerHeroesResults(id: String): BasicResponse<List<PlayerHeroResult>> {
        val call = retrofit.getPlayerHeroesResults(id).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getConstHeroes(): BasicResponse<Map<String, HeroResult>> {
        val call = retrofit.getConstHeroes().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getPeers(id: String): BasicResponse<List<PeersResult>> {
        val call = retrofit.getPeers(id).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getMatches(
        id: String,
        limit: Int,
        offset: Long,
        lobbyType: Int?,
        heroId: Int?
    ): BasicResponse<List<MatchesResult>> {
        val call = retrofit.getMatches(id, limit, offset, lobbyType, heroId).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getConstLobbyTypes(): BasicResponse<Map<String, LobbyTypeResult>> {
        val call = retrofit.getConstLobbyTypes().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getMatchData(id: String): BasicResponse<MatchDataResult> {
        val call = retrofit.getMatchData(id).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getRegions(): BasicResponse<Map<String, String>> {
        val call = retrofit.getRegions().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getItems(): BasicResponse<Map<String, ItemResult>> {
        val call = retrofit.getItems().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getAbilityIds(): BasicResponse<Map<String, String>> {
        val call = retrofit.getAbilityIds().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getAbilities(): BasicResponse<Map<String, AbilityResult>> {
        val call = retrofit.getAbilities().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getPatches(): BasicResponse<List<PatchResult>> {
        val call = retrofit.getPatches().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getPatchNotes(): BasicResponse<Map<String, PatchNotesResult>> {
        val call = retrofit.getPatchNotes().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getTeams(): BasicResponse<List<TeamsResult>> {
        val call = retrofit.getTeams().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getTeamPlayers(id: String): BasicResponse<List<TeamPlayersResult>> {
        val call = retrofit.getTeamPlayers(id).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun loadPagingMatches(
        userId: String,
        lobbyType: Int?,
        heroId: Int?,
        errorListener: (String) -> Unit
    ): LiveData<PagedList<MatchesResult>> {
        val factory = MatchDataSourceFactory(userId, lobbyType, heroId, errorListener)
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
        val call = retrofit.getTeamMatches(id).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getTeamHeroes(id: String): BasicResponse<List<TeamHeroesResult>> {
        val call = retrofit.getTeamHeroes(id).execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getAghs(): BasicResponse<List<AghsResult>> {
        val call = retrofit.getAghs().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getHeroAbilities(): BasicResponse<Map<String, HeroAbilitiesResult>> {
        val call = retrofit.getHeroAbilities().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }

    override fun getHeroLore(): BasicResponse<Map<String, String>> {
        val call = retrofit.getHeroLore().execute()
        return BasicResponse(call.body(), call.errorBody().toString())
    }
}
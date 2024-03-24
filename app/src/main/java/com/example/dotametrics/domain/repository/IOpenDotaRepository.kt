package com.example.dotametrics.domain.repository

import androidx.lifecycle.LiveData
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
import com.example.dotametrics.domain.entity.remote.BasicResponse

interface IOpenDotaRepository {
    fun getSearchResults(name: String): BasicResponse<List<SearchResult>>
    fun getPlayersResults(id: String): BasicResponse<PlayersResult>
    fun getWLResults(
        id: String,
        limit: Int?,
        lobbyType: Int?,
        heroId: Int?
    ): BasicResponse<WLResult>
    fun getTotals(id: String): BasicResponse<List<TotalsResult>>
    fun getPlayerHeroesResults(id: String): BasicResponse<List<PlayerHeroResult>>
    fun getConstHeroes(): BasicResponse<Map<String, HeroResult>>
    fun getPeers(id: String): BasicResponse<List<PeersResult>>
    fun getMatches(
        id: String,
        limit: Int,
        offset: Long,
        lobbyType: Int?,
        heroId: Int?
    ): BasicResponse<List<MatchesResult>>
    fun getConstLobbyTypes(): BasicResponse<Map<String, LobbyTypeResult>>
    fun getMatchData(id: String): BasicResponse<MatchDataResult>
    fun getRegions(): BasicResponse<Map<String, String>>
    fun getItems(): BasicResponse<Map<String, ItemResult>>
    fun getAbilityIds(): BasicResponse<Map<String, String>>
    fun getAbilities(): BasicResponse<Map<String, AbilityResult>>
    fun getPatches(): BasicResponse<List<PatchResult>>
    fun getPatchNotes(): BasicResponse<Map<String, PatchNotesResult>>
    fun getTeams(): BasicResponse<List<TeamsResult>>
    fun getTeamPlayers(id: String): BasicResponse<List<TeamPlayersResult>>
    fun getTeamMatches(id: String): BasicResponse<List<TeamMatchesResult>>
    fun getTeamHeroes(id: String): BasicResponse<List<TeamHeroesResult>>
    fun getAghs(): BasicResponse<List<AghsResult>>
    fun getHeroAbilities(): BasicResponse<Map<String, HeroAbilitiesResult>>
    fun getHeroLore(): BasicResponse<Map<String, String>>
    fun loadPagingMatches(
        userId: String,
        lobbyType: Int?,
        heroId: Int?,
        errorListener: (String) -> Unit
    ): LiveData<PagedList<MatchesResult>>
}
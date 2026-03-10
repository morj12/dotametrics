package com.example.dotametrics.presentation.view.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.local.PlayerDbModel
import com.example.dotametrics.domain.entity.remote.players.PlayersResult
import com.example.dotametrics.domain.entity.remote.players.heroes.PlayerHeroResult
import com.example.dotametrics.domain.entity.remote.players.matches.MatchesResult
import com.example.dotametrics.domain.entity.remote.players.peers.PeersResult
import com.example.dotametrics.domain.entity.remote.players.totals.TotalsResult
import com.example.dotametrics.domain.entity.remote.players.wl.WLResult
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import com.example.dotametrics.domain.repository.IPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val app: App,
    private val openDotaRepository: IOpenDotaRepository,
    private val playerRepository: IPlayerRepository
) : ViewModel() {

    var userId: String = ""
    var lobbyType: Int? = null
    var heroId: Int? = null

    private val _result = MutableLiveData<PlayersResult>()
    val result: LiveData<PlayersResult>
        get() = _result

    private val _wl = MutableLiveData<WLResult>()
    val wl: LiveData<WLResult>
        get() = _wl

    private val _filteredWl = MutableLiveData<WLResult>()
    val filteredWl: LiveData<WLResult>
        get() = _filteredWl

    private val _totals = MutableLiveData<List<TotalsResult>>()
    val totals: LiveData<List<TotalsResult>>
        get() = _totals

    private val _heroes = MutableLiveData<List<PlayerHeroResult>>()
    val heroes: LiveData<List<PlayerHeroResult>>
        get() = _heroes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _peers = MutableLiveData<List<PeersResult>>()
    val peers: LiveData<List<PeersResult>>
        get() = _peers

    private val _matchesFlow = MutableStateFlow<PagingData<MatchesResult>>(PagingData.empty())
    val matchesFlow: StateFlow<PagingData<MatchesResult>> = _matchesFlow

    private val _isFav = MutableLiveData<Boolean>()
    val isFav: LiveData<Boolean>
        get() = _isFav

    fun loadUser() {
        if (userId.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val res = openDotaRepository.getPlayersResults(userId)
                    if (res.error != "null") {
                        _error.value = res.error
                        Log.e("DOTA_RETROFIT", res.error)
                    } else {
                        res.data?.let { if (!it.isNull()) _result.value = it }
                    }
                } catch (e: Exception) {
                    _error.value = e.message.toString()
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }

                try {
                    val res = openDotaRepository.getWLResults(userId, 20, null, null)
                    if (res.error != "null") {
                        _error.value = res.error
                        Log.e("DOTA_RETROFIT", res.error)
                    } else {
                        res.data?.let { if (!it.isNull()) _wl.value = it }
                    }
                } catch (e: Exception) {
                    _error.value = e.message.toString()
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadFilteredWLResults() {
        if (userId.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val res = openDotaRepository.getWLResults(userId, null, lobbyType, heroId)
                    if (res.error != "null") {
                        _error.value = res.error
                        Log.e("DOTA_RETROFIT", res.error)
                    } else {
                        res.data?.let { if (!it.isNull()) _filteredWl.value = it }
                    }
                } catch (e: Exception) {
                    _error.value = e.message.toString()
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadTotals() {
        if (userId.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val res = openDotaRepository.getTotals(userId)
                    if (res.error != "null") {
                        _error.value = res.error
                        Log.e("DOTA_RETROFIT", res.error)
                    } else {
                        val usefulTotals = app.resources.getStringArray(R.array.totals_array)
                        _totals.value = res.data?.filter { usefulTotals.contains(it.field) }
                    }
                } catch (e: Exception) {
                    _error.value = e.message.toString()
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadPlayerHeroesResults() {
        if (userId.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val res = openDotaRepository.getPlayerHeroesResults(userId)
                    if (res.error != "null") {
                        _error.value = res.error
                        Log.e("DOTA_RETROFIT", res.error)
                    } else {
                        res.data?.let { _heroes.value = it }
                    }
                } catch (e: Exception) {
                    _error.value = e.message.toString()
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadPeers() {
        if (userId.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val res = openDotaRepository.getPeers(userId)
                    if (res.error != "null") {
                        _error.value = res.error
                        Log.e("DOTA_RETROFIT", res.error)
                    } else {
                        res.data?.let { _peers.value = it }
                    }
                } catch (e: Exception) {
                    _error.value = e.message.toString()
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadMatches() {
        if (userId.isNotBlank()) {
            viewModelScope.launch {
                openDotaRepository.loadPagingMatches(userId, lobbyType, heroId)
                    .cachedIn(viewModelScope)
                    .collect {
                        _matchesFlow.value = it
                    }
            }
        }
    }

    fun checkFavorite(id: Long) = viewModelScope.launch {
        val player = playerRepository.getPlayer(id)
        _isFav.value = player != null
    }

    fun insertPlayer(playerDbModel: PlayerDbModel, observe: Boolean) = viewModelScope.launch {
        playerRepository.insertPlayer(playerDbModel)
        if (observe) _isFav.value = true
    }

    fun deletePlayer(id: Long) = viewModelScope.launch {
        playerRepository.deletePlayer(id)
        _isFav.value = false
    }
}

package com.example.dotametrics.presentation.view.account

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
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
import kotlinx.coroutines.Dispatchers
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

    private val _matches = MutableLiveData<PagedList<MatchesResult>>()
    var matches: LiveData<PagedList<MatchesResult>> = _matches

    private val _isFav = MutableLiveData<Boolean>()
    val isFav: LiveData<Boolean>
        get() = _isFav

    fun loadUser() {
        if (userId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = openDotaRepository.getPlayersResults(userId)
                    if (result.error != "null") {
                        _error.postValue(result.error)
                        Log.e("DOTA_RETROFIT", result.error)
                    } else {
                        result.data?.let { if (!it.isNull()) _result.postValue(it) }
                    }
                } catch (e: Exception) {
                    _error.postValue(e.message.toString())
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }

            }
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = openDotaRepository.getWLResults(userId, 20, null, null)
                    if (result.error != "null") {
                        _error.postValue(result.error)
                        Log.e("DOTA_RETROFIT", result.error)
                    } else {
                        result.data?.let { if (!it.isNull()) _wl.postValue(it) }
                    }
                } catch (e: Exception) {
                    _error.postValue(e.message.toString())
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadFilteredWLResults() {
        if (userId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = openDotaRepository.getWLResults(userId, null, lobbyType, heroId)
                    if (result.error != "null") {
                        _error.postValue(result.error)
                        Log.e("DOTA_RETROFIT", result.error)
                    } else {
                        result.data?.let { if (!it.isNull()) _filteredWl.postValue(it) }
                    }
                } catch (e: Exception) {
                    _error.postValue(e.message.toString())
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadTotals() {
        if (userId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = openDotaRepository.getTotals(userId)
                    if (result.error != "null") {
                        _error.postValue(result.error)
                        Log.e("DOTA_RETROFIT", result.error)
                    } else {
                        val usefulTotals = app.resources.getStringArray(R.array.totals_array)
                        _totals.postValue(result.data?.filter { usefulTotals.contains(it.field) })
                    }
                } catch (e: Exception) {
                    _error.postValue(e.message.toString())
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadPlayerHeroesResults() {
        if (userId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = openDotaRepository.getPlayerHeroesResults(userId)
                    if (result.error != "null") {
                        _error.postValue(result.error)
                        Log.e("DOTA_RETROFIT", result.error)
                    } else {
                        result.data?.let { _heroes.postValue(it) }
                    }
                } catch (e: Exception) {
                    _error.postValue(e.message.toString())
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadPeers() {
        if (userId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = openDotaRepository.getPeers(userId)
                    if (result.error != "null") {
                        _error.postValue(result.error)
                        Log.e("DOTA_RETROFIT", result.error)
                    } else {
                        result.data?.let { _peers.postValue(it) }
                    }
                } catch (e: Exception) {
                    _error.postValue(e.message.toString())
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadMatches() {
        if (userId.isNotBlank()) {
            matches = openDotaRepository.loadPagingMatches(userId, lobbyType, heroId, errorListener)
        }
    }

    private val errorListener: ((String) -> Unit) = { _error.value = it }

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
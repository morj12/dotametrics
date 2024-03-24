package com.example.dotametrics.presentation.view.account

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.data.local.dbmodel.PlayerDbModel
import com.example.dotametrics.data.local.repository.PlayerRepository
import com.example.dotametrics.data.remote.model.players.PlayersResult
import com.example.dotametrics.data.remote.model.players.heroes.PlayerHeroResult
import com.example.dotametrics.data.remote.paging.players.MatchDataSource
import com.example.dotametrics.data.remote.paging.players.MatchDataSource.Companion.PAGE_SIZE
import com.example.dotametrics.data.remote.paging.players.MatchDataSourceFactory
import com.example.dotametrics.data.remote.model.players.matches.MatchesResult
import com.example.dotametrics.data.remote.model.players.peers.PeersResult
import com.example.dotametrics.data.remote.model.players.totals.TotalsResult
import com.example.dotametrics.data.remote.model.players.wl.WLResult
import com.example.dotametrics.data.remote.repository.OpenDotaRepository
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import com.example.dotametrics.domain.repository.IPlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class AccountViewModel(private val app: App) : ViewModel() {

    private val repository: IPlayerRepository = PlayerRepository(app.db)

    private val openDotaRepository: IOpenDotaRepository = OpenDotaRepository()

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

    private lateinit var matchDataSource: LiveData<MatchDataSource>
    private var executor = Executors.newCachedThreadPool()

    fun loadUser() {
        if (userId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getPlayersResults(userId)
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let { if (!it.isNull()) _result.postValue(it) }
                }
            }
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getWLResults(userId, 20, null, null)
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let { if (!it.isNull()) _wl.postValue(it) }
                }
            }
        }
    }

    fun loadFilteredWLResults() {
        if (userId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getWLResults(userId, null, lobbyType, heroId)
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let { if (!it.isNull()) _filteredWl.postValue(it) }
                }
            }
        }
    }

    fun loadTotals() {
        if (userId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getTotals(userId)
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    val usefulTotals = app.resources.getStringArray(R.array.totals_array)
                    _totals.postValue(result.data?.filter { usefulTotals.contains(it.field) })
                }
            }
        }
    }

    fun loadPlayerHeroesResults() {
        if (userId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getPlayerHeroesResults(userId)
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let { _heroes.postValue(it) }
                }
            }
        }
    }

    fun loadPeers() {
        if (userId.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getPeers(userId)
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let { _peers.postValue(it) }
                }
            }
        }
    }

    fun loadMatches(callback: () -> Unit) {
        // TODO: change later
        if (userId.isNotBlank()) {
            val factory = MatchDataSourceFactory(userId, lobbyType, heroId, errorListener)
            matchDataSource = factory.mutableLiveData
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PAGE_SIZE / 4)
                .build()

            matches = LivePagedListBuilder(factory, config).setFetchExecutor(executor).build()
            callback()
        }
    }

    private val errorListener: ((String) -> Unit) = { _error.value = it }

    fun checkFavorite(id: Long) = viewModelScope.launch {
        val player = repository.getPlayer(id)
        _isFav.value = player != null
    }

    fun insertPlayer(playerDbModel: PlayerDbModel, observe: Boolean) = viewModelScope.launch {
        repository.insertPlayer(playerDbModel)
        if (observe) _isFav.value = true
    }

    fun deletePlayer(id: Long) = viewModelScope.launch {
        repository.deletePlayer(id)
        _isFav.value = false
    }

    class AccountViewModelFactory(private val app: App) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AccountViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

}
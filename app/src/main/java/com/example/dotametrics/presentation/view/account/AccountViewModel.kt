package com.example.dotametrics.presentation.view.account

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.data.db.dbmodel.PlayerDbModel
import com.example.dotametrics.data.db.repository.PlayerRepository
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.model.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.data.model.players.PlayersResult
import com.example.dotametrics.data.model.players.heroes.PlayerHeroResult
import com.example.dotametrics.data.model.players.matches.MatchDataSource
import com.example.dotametrics.data.model.players.matches.MatchDataSource.Companion.PAGE_SIZE
import com.example.dotametrics.data.model.players.matches.MatchDataSourceFactory
import com.example.dotametrics.data.model.players.matches.MatchesResult
import com.example.dotametrics.data.model.players.peers.PeersResult
import com.example.dotametrics.data.model.players.totals.TotalsResult
import com.example.dotametrics.data.model.players.wl.WLResult
import com.example.dotametrics.data.service.RetrofitInstance
import com.example.dotametrics.util.ConstData
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class AccountViewModel(private val app: App) : ViewModel() {

    private var loadingHeroes = false
    private var loadingLobbies = false

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

    private val _constHeroes = MutableLiveData<Unit>()
    val constHeroes: LiveData<Unit>
        get() = _constHeroes

    private val _peers = MutableLiveData<List<PeersResult>>()
    val peers: LiveData<List<PeersResult>>
        get() = _peers

    private val _constLobbyTypes = MutableLiveData<Unit>()
    val constLobbyTypes: LiveData<Unit>
        get() = _constLobbyTypes

    private val _matches = MutableLiveData<PagedList<MatchesResult>>()
    var matches: LiveData<PagedList<MatchesResult>> = _matches

    private val _isFav = MutableLiveData<Boolean>()
    val isFav: LiveData<Boolean>
        get() = _isFav

    private lateinit var matchDataSource: LiveData<MatchDataSource>
    private var executor = Executors.newCachedThreadPool()

    private val retrofit = RetrofitInstance.getService()

    private val repository = PlayerRepository(app.db)

    fun loadUser() {
        if (userId.isNotBlank()) {
            retrofit.getPlayersResults(userId).enqueue(object : Callback<PlayersResult> {
                override fun onResponse(
                    call: Call<PlayersResult>,
                    response: Response<PlayersResult>
                ) {
                    Log.d("RETROFIT_CALL", "AccountViewModel: loadUser getPlayerResults")
                    _result.value = response.body()
                }

                override fun onFailure(call: Call<PlayersResult>, t: Throwable) {
                    _error.value = t.message.toString()
                }
            })
            retrofit.getWLResults(userId, 20, null, null).enqueue(object : Callback<WLResult> {
                override fun onResponse(call: Call<WLResult>, response: Response<WLResult>) {
                    Log.d("RETROFIT_CALL", "AccountViewModel: loadUser getWLResults")
                    _wl.value = response.body()
                }

                override fun onFailure(call: Call<WLResult>, t: Throwable) {
                    _error.value = t.message.toString()
                }

            })
        }
    }

    fun loadFilteredWLResults() {
        if (userId.isNotBlank()) {
            retrofit.getWLResults(userId, null, lobbyType, heroId)
                .enqueue(object : Callback<WLResult> {
                    override fun onResponse(call: Call<WLResult>, response: Response<WLResult>) {
                        Log.d("RETROFIT_CALL", "AccountViewModel: loadFilteredWLResults")
                        _filteredWl.value = response.body()
                    }

                    override fun onFailure(call: Call<WLResult>, t: Throwable) {
                        _error.value = t.message.toString()
                    }

                })
        }
    }

    fun loadTotals() {
        if (userId.isNotBlank()) {
            retrofit.getTotals(userId).enqueue(object : Callback<List<TotalsResult>> {
                override fun onResponse(
                    call: Call<List<TotalsResult>>,
                    response: Response<List<TotalsResult>>
                ) {
                    Log.d("RETROFIT_CALL", "AccountViewModel: loadTotals")
                    val usefulTotals = app.resources.getStringArray(R.array.totals_array)
                    _totals.value = response.body()?.filter { usefulTotals.contains(it.field) }
                }

                override fun onFailure(call: Call<List<TotalsResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                }

            })
        }
    }

    fun loadPlayerHeroesResults() {
        if (userId.isNotBlank()) {
            retrofit.getPlayerHeroesResults(userId)
                .enqueue(object : Callback<List<PlayerHeroResult>> {
                    override fun onResponse(
                        call: Call<List<PlayerHeroResult>>,
                        response: Response<List<PlayerHeroResult>>
                    ) {
                        Log.d("RETROFIT_CALL", "AccountViewModel: loadPlayerHeroesResults")
                        _heroes.value = response.body()
                    }

                    override fun onFailure(call: Call<List<PlayerHeroResult>>, t: Throwable) {
                        _error.value = t.message.toString()
                    }

                })
        }
    }

    fun loadHeroes() {
        if (loadingHeroes) return
        if (ConstData.heroes.isNotEmpty()) {
            _constHeroes.value = Unit
            loadingHeroes = false
        } else {
            loadingHeroes = true
            retrofit.getConstHeroes().enqueue(object : Callback<Map<String, HeroResult>> {
                override fun onResponse(
                    call: Call<Map<String, HeroResult>>,
                    response: Response<Map<String, HeroResult>>
                ) {
                    Log.d("RETROFIT_CALL", "AccountViewModel: loadHeroes")
                    val body = response.body()
                    if (body != null) {
                        ConstData.heroes = body.values.toList().sortedBy { it.localizedName }
                        _constHeroes.value = Unit
                        loadingHeroes = false
                    }
                }

                override fun onFailure(call: Call<Map<String, HeroResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingHeroes = false
                }
            })
        }
    }

    fun loadPeers() {
        if (userId.isNotBlank()) {
            retrofit.getPeers(userId)
                .enqueue(object : Callback<List<PeersResult>> {
                    override fun onResponse(
                        call: Call<List<PeersResult>>,
                        response: Response<List<PeersResult>>
                    ) {
                        Log.d("RETROFIT_CALL", "AccountViewModel: loadPeers")
                        _peers.value = response.body()
                    }

                    override fun onFailure(call: Call<List<PeersResult>>, t: Throwable) {
                        _error.value = t.message.toString()
                    }

                })
        }
    }

    fun loadMatches(callback: () -> Unit) {
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

    fun loadLobbyTypes() {
        if (loadingLobbies) return
        if (ConstData.lobbies.isNotEmpty()) {
            _constLobbyTypes.value = Unit
            loadingLobbies = false
        } else {
            loadingLobbies = true
            retrofit.getConstLobbyTypes().enqueue(object : Callback<Map<String, LobbyTypeResult>> {
                override fun onResponse(
                    call: Call<Map<String, LobbyTypeResult>>,
                    response: Response<Map<String, LobbyTypeResult>>
                ) {
                    Log.d("RETROFIT_CALL", "AccountViewModel: loadLobbyTypes")
                    val body = response.body()
                    if (body != null) {
                        val usefulLobbies = app.resources.getStringArray(R.array.lobbies_array)
                        ConstData.lobbies =
                            body.values.toList().filter { usefulLobbies.contains(it.name) }
                        _constLobbyTypes.value = Unit
                        loadingLobbies = false
                    }
                }

                override fun onFailure(call: Call<Map<String, LobbyTypeResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingLobbies = false
                }
            })
        }
    }

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
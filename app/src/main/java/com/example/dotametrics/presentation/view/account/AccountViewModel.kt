package com.example.dotametrics.presentation.view.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.model.players.PlayersResult
import com.example.dotametrics.data.model.players.heroes.PlayerHeroResult
import com.example.dotametrics.data.model.players.peers.PeersResult
import com.example.dotametrics.data.model.players.totals.TotalsResult
import com.example.dotametrics.data.model.players.wl.WLResult
import com.example.dotametrics.data.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel : ViewModel() {

    var userId: String = ""

    private val _result = MutableLiveData<PlayersResult>()
    val result: LiveData<PlayersResult>
        get() = _result

    private val _wl = MutableLiveData<WLResult>()
    val wl: LiveData<WLResult>
        get() = _wl

    private val _totals = MutableLiveData<List<TotalsResult>>()
    val totals: LiveData<List<TotalsResult>>
        get() = _totals

    private val _heroes = MutableLiveData<List<PlayerHeroResult>>()
    val heroes: LiveData<List<PlayerHeroResult>>
        get() = _heroes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _constHeroes = MutableLiveData<List<HeroResult>>()
    val constHeroes: LiveData<List<HeroResult>>
        get() = _constHeroes

    private val _peers = MutableLiveData<List<PeersResult>>()
    val peers: LiveData<List<PeersResult>>
        get() = _peers

    private val retrofit = RetrofitInstance.getService()

    fun loadUser(id: String) {
        if (id.isNotBlank()) {
            // main results
            retrofit.getPlayersResults(id).enqueue(object : Callback<PlayersResult> {
                override fun onResponse(
                    call: Call<PlayersResult>,
                    response: Response<PlayersResult>
                ) {
                    _result.value = response.body()
                }

                override fun onFailure(call: Call<PlayersResult>, t: Throwable) {
                    _error.value = t.message.toString()
                }
            })
            // winrate results
            retrofit.getWLResults(id).enqueue(object : Callback<WLResult> {
                override fun onResponse(call: Call<WLResult>, response: Response<WLResult>) {
                    _wl.value = response.body()
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
                    _totals.value = response.body()
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
                        _heroes.value = response.body()
                    }

                    override fun onFailure(call: Call<List<PlayerHeroResult>>, t: Throwable) {
                        _error.value = t.message.toString()
                    }

                })
        }
    }

    fun loadHeroes() {
        retrofit.getConstHeroes().enqueue(object : Callback<Map<String, HeroResult>> {
            override fun onResponse(
                call: Call<Map<String, HeroResult>>,
                response: Response<Map<String, HeroResult>>
            ) {
                _constHeroes.value = response.body()?.values?.toList()
            }

            override fun onFailure(call: Call<Map<String, HeroResult>>, t: Throwable) {
                Log.d("loadHeroes", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun loadPeers() {
        if (userId.isNotBlank()) {
            retrofit.getPeers(userId)
                .enqueue(object : Callback<List<PeersResult>> {
                    override fun onResponse(
                        call: Call<List<PeersResult>>,
                        response: Response<List<PeersResult>>
                    ) {
                        _peers.value = response.body()
                    }

                    override fun onFailure(call: Call<List<PeersResult>>, t: Throwable) {
                        _error.value = t.message.toString()
                    }

                })
        }
    }

}
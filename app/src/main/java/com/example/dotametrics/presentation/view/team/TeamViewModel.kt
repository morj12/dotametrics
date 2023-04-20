package com.example.dotametrics.presentation.view.team

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.App
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.model.teams.TeamsResult
import com.example.dotametrics.data.model.teams.heroes.TeamHeroesResult
import com.example.dotametrics.data.model.teams.matches.TeamMatchesResult
import com.example.dotametrics.data.model.teams.players.TeamPlayersResult
import com.example.dotametrics.data.service.RetrofitInstance
import com.example.dotametrics.util.ConstData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamViewModel(private val application: App) : ViewModel() {

    private var loadingHeroes = false

    private val _team = MutableLiveData<TeamsResult>()
    val team: LiveData<TeamsResult>
        get() = _team

    private val _constHeroes = MutableLiveData<Unit>()
    val constHeroes: LiveData<Unit>
        get() = _constHeroes

    private val _players = MutableLiveData<List<TeamPlayersResult>>()
    val players: LiveData<List<TeamPlayersResult>>
        get() = _players

    private val _matches = MutableLiveData<List<TeamMatchesResult>>()
    val matches: LiveData<List<TeamMatchesResult>>
        get() = _matches

    private val _heroes = MutableLiveData<List<TeamHeroesResult>>()
    val heroes: LiveData<List<TeamHeroesResult>>
        get() = _heroes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val retrofit = RetrofitInstance.getService()

    fun setTeam(team: TeamsResult) {
        _team.value = team
    }

    fun loadPlayers() {
        if (_team.value != null) {
            retrofit.getTeamPlayers(_team.value!!.teamId.toString())
                .enqueue(object : Callback<List<TeamPlayersResult>> {
                    override fun onResponse(
                        call: Call<List<TeamPlayersResult>>,
                        response: Response<List<TeamPlayersResult>>
                    ) {
                        Log.d("RETROFIT_CALL", "TeamViewModel: loadPlayers")
                        _players.value = response.body()?.filter { it.isCurrentTeamMember == true }
                    }

                    override fun onFailure(call: Call<List<TeamPlayersResult>>, t: Throwable) {
                        _error.value = t.message.toString()
                    }

                })
        }
    }

    fun loadMatches() {
        if (_team.value != null) {
            retrofit.getTeamMatches(_team.value!!.teamId.toString())
                .enqueue(object : Callback<List<TeamMatchesResult>> {
                    override fun onResponse(
                        call: Call<List<TeamMatchesResult>>,
                        response: Response<List<TeamMatchesResult>>
                    ) {
                        Log.d("RETROFIT_CALL", "TeamViewModel: loadMatches")
                        _matches.value = response.body()
                    }

                    override fun onFailure(call: Call<List<TeamMatchesResult>>, t: Throwable) {
                        _error.value = t.message.toString()
                    }

                })
        }
    }

    fun loadHeroes() {
        if (_team.value != null) {
            retrofit.getTeamHeroes(_team.value!!.teamId.toString())
                .enqueue(object : Callback<List<TeamHeroesResult>> {
                    override fun onResponse(
                        call: Call<List<TeamHeroesResult>>,
                        response: Response<List<TeamHeroesResult>>
                    ) {
                        Log.d("RETROFIT_CALL", "TeamViewModel: loadPlayers")
                        _heroes.value = response.body()
                    }

                    override fun onFailure(call: Call<List<TeamHeroesResult>>, t: Throwable) {
                        _error.value = t.message.toString()
                    }

                })
        }
    }

    fun loadConstHeroes() {
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
                    Log.d("RETROFIT_CALL", "TeamViewModel: loadConstHeroes")
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


    class TeamViewModelFactory(private val app: App) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TeamViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TeamViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

}
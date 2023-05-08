package com.example.dotametrics.presentation.view.team

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotametrics.data.remote.model.teams.TeamsResult
import com.example.dotametrics.data.remote.model.teams.heroes.TeamHeroesResult
import com.example.dotametrics.data.remote.model.teams.matches.TeamMatchesResult
import com.example.dotametrics.data.remote.model.teams.players.TeamPlayersResult
import com.example.dotametrics.data.remote.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamViewModel : ViewModel() {

    private val _team = MutableLiveData<TeamsResult>()
    val team: LiveData<TeamsResult>
        get() = _team

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
}
package com.example.dotametrics.presentation.view.team

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotametrics.domain.entity.remote.teams.TeamsResult
import com.example.dotametrics.domain.entity.remote.teams.heroes.TeamHeroesResult
import com.example.dotametrics.domain.entity.remote.teams.matches.TeamMatchesResult
import com.example.dotametrics.domain.entity.remote.teams.players.TeamPlayersResult
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(private val openDotaRepository: IOpenDotaRepository) :
    ViewModel() {

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

    fun setTeam(team: TeamsResult) {
        _team.value = team
    }

    fun loadPlayers() {
        if (_team.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = openDotaRepository.getTeamPlayers(_team.value!!.teamId.toString())
                    if (result.error != "null") {
                        _error.postValue(result.error)
                        Log.e("DOTA_RETROFIT", result.error)
                    } else {
                        result.data?.let {
                            _players.postValue(it.filter { p -> p.isCurrentTeamMember == true })
                        }
                    }
                } catch (e: Exception) {
                    _error.postValue(e.message.toString())
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadMatches() {
        if (_team.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = openDotaRepository.getTeamMatches(_team.value!!.teamId.toString())
                    if (result.error != "null") {
                        _error.postValue(result.error)
                        Log.e("DOTA_RETROFIT", result.error)
                    } else {
                        result.data?.let { _matches.postValue(it) }
                    }
                } catch (e: Exception) {
                    _error.postValue(e.message.toString())
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

    fun loadHeroes() {
        if (_team.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = openDotaRepository.getTeamHeroes(_team.value!!.teamId.toString())
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
}
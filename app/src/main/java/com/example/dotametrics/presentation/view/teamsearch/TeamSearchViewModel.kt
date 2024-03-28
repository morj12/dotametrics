package com.example.dotametrics.presentation.view.teamsearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotametrics.domain.entity.remote.teams.TeamsResult
import com.example.dotametrics.data.ConstData
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamSearchViewModel @Inject constructor(private val openDotaRepository: IOpenDotaRepository) :
    ViewModel() {

    private val _teams = MutableLiveData<Unit>()
    val teams: LiveData<Unit>
        get() = _teams

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _filteredTeams = MutableLiveData<List<TeamsResult>>()
    val filteredTeams: LiveData<List<TeamsResult>>
        get() = _filteredTeams

    fun loadTeams() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = openDotaRepository.getTeams()
                if (result.error != "null") {
                    _error.postValue(result.error)
                    Log.e("DOTA_RETROFIT", result.error)
                } else {
                    result.data?.let {
                        ConstData.teams = it
                        _teams.postValue(Unit)
                    }
                }
            } catch (e: Exception) {
                _error.postValue(e.message.toString())
                Log.e("DOTA_RETROFIT", e.message.toString())
            }
        }
    }

    fun filterTeams(name: String? = null) {
        if (name == null) {
            _filteredTeams.value = ConstData.teams
        } else {
            _filteredTeams.value =
                ConstData.teams.filter { it.name!!.lowercase().contains(name.lowercase()) }
        }
    }
}
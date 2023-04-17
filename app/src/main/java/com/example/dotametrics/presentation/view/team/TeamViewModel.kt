package com.example.dotametrics.presentation.view.team

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.App
import com.example.dotametrics.data.model.teams.TeamsResult
import com.example.dotametrics.data.service.RetrofitInstance

class TeamViewModel(private val application: App) : ViewModel() {

    private val _team = MutableLiveData<TeamsResult>()
    val team: LiveData<TeamsResult>
        get() = _team

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val retrofit = RetrofitInstance.getService()

    fun setTeam(team: TeamsResult) {
        _team.value = team
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
package com.example.dotametrics.presentation.view.teamsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotametrics.data.remote.model.teams.TeamsResult
import com.example.dotametrics.data.remote.service.RetrofitInstance
import com.example.dotametrics.data.ConstData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamSearchViewModel : ViewModel() {

    private val _teams = MutableLiveData<Unit>()
    val teams: LiveData<Unit>
        get() = _teams

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _filteredTeams = MutableLiveData<List<TeamsResult>>()
    val filteredTeams: LiveData<List<TeamsResult>>
        get() = _filteredTeams

    private val retrofit = RetrofitInstance.getService()

    fun loadTeams() {
        retrofit.getTeams().enqueue(object : Callback<List<TeamsResult>> {
            override fun onResponse(
                call: Call<List<TeamsResult>>,
                response: Response<List<TeamsResult>>
            ) {
                val body = response.body()
                if (body != null) {
                    ConstData.teams = body
                    _teams.value = Unit
                }
            }

            override fun onFailure(call: Call<List<TeamsResult>>, t: Throwable) {
                _error.value = t.message.toString()
            }

        })
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
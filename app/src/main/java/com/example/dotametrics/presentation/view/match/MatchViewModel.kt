package com.example.dotametrics.presentation.view.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotametrics.data.remote.model.matches.MatchDataResult
import com.example.dotametrics.data.remote.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchViewModel : ViewModel() {

    var loadingMatch = false

    var matchId: String = ""

    private val _result = MutableLiveData<MatchDataResult>()
    val result: LiveData<MatchDataResult>
        get() = _result

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val retrofit = RetrofitInstance.getService()

    fun loadMatch() {
        if (loadingMatch) return
        if (matchId.isNotBlank()) {
            loadingMatch = true
            retrofit.getMatchData(matchId).enqueue(object : Callback<MatchDataResult> {
                override fun onResponse(
                    call: Call<MatchDataResult>,
                    response: Response<MatchDataResult>
                ) {
                    _result.value = response.body()
                    loadingMatch = false
                }

                override fun onFailure(call: Call<MatchDataResult>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingMatch = false
                }
            })
        }
    }

}
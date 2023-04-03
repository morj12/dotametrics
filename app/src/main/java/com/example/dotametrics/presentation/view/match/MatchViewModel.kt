package com.example.dotametrics.presentation.view.match

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dotametrics.data.model.matches.MatchDataResult
import com.example.dotametrics.data.service.RetrofitInstance
import com.example.dotametrics.util.ConstData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchViewModel(val app: Application) : AndroidViewModel(app) {

    var matchId: String = ""

    private val _result = MutableLiveData<MatchDataResult>()
    val result: LiveData<MatchDataResult>
        get() = _result

    private val _constRegions = MutableLiveData<Unit>()
    val constRegions: LiveData<Unit>
        get() = _constRegions

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val retrofit = RetrofitInstance.getService()

    fun loadMatch(id: String) {
        if (id.isNotBlank()) {
            retrofit.getMatchData(id).enqueue(object : Callback<MatchDataResult> {
                override fun onResponse(
                    call: Call<MatchDataResult>,
                    response: Response<MatchDataResult>
                ) {
                    _result.value = response.body()
                }

                override fun onFailure(call: Call<MatchDataResult>, t: Throwable) {
                    _error.value = t.message.toString()
                }
            })
        }
    }

    fun loadRegions() {
        retrofit.getRegions().enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                val body = response.body()
                if (body != null) {
                    ConstData.regions = body.mapKeys { it.key.toInt() }
                    _constRegions.value = Unit
                }

            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                _error.value = t.message.toString()
            }

        })
    }
}
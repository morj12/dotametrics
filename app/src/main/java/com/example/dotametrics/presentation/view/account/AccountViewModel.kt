package com.example.dotametrics.presentation.view.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotametrics.data.model.players.PlayersResult
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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

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

}
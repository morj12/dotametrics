package com.example.dotametrics.presentation.view.match

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dotametrics.data.model.constants.abilities.AbilityResult
import com.example.dotametrics.data.model.constants.items.ItemResult
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

    private val _constItems = MutableLiveData<Unit>()
    val constItems: LiveData<Unit>
        get() = _constItems

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _constAbilityIds = MutableLiveData<Unit>()
    val constAbilityIds: LiveData<Unit>
        get() = _constAbilityIds

    private val _constAbilities = MutableLiveData<Unit>()
    val constAbilities: LiveData<Unit>
        get() = _constAbilities

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

    fun loadItems() {
        retrofit.getItems().enqueue(object : Callback<Map<String, ItemResult>> {
            override fun onResponse(
                call: Call<Map<String, ItemResult>>,
                response: Response<Map<String, ItemResult>>
            ) {
                val body = response.body()
                if (body != null) {
                    ConstData.items = body.values.toList()
                    _constItems.value = Unit
                }
            }

            override fun onFailure(call: Call<Map<String, ItemResult>>, t: Throwable) {
                _error.value = t.message.toString()
            }

        })
    }

    fun loadAbilityIds() {
        retrofit.getAbilityIds().enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                val body = response.body()
                if (body != null) {
                    ConstData.abilityIds = body
                    _constAbilityIds
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                _error.value = t.message.toString()
            }

        })
    }

    fun loadAbilities() {
        retrofit.getAbilities().enqueue(object : Callback<Map<String, AbilityResult>> {
            override fun onResponse(
                call: Call<Map<String, AbilityResult>>,
                response: Response<Map<String, AbilityResult>>
            ) {
                val body = response.body()
                if (body != null) {
                    ConstData.abilities = body
                    _constAbilityIds
                }
            }

            override fun onFailure(call: Call<Map<String, AbilityResult>>, t: Throwable) {
                _error.value = t.message.toString()
            }

        })
    }

}
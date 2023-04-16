package com.example.dotametrics.presentation.view.match

import android.app.Application
import android.util.Log
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

    var loadingItems = false
    var loadingRegions = false
    var loadingAbilityIds = false
    var loadingAbilities = false
    var loadingMatch = false

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

    fun loadMatch() {
        if (loadingMatch) return
        if (matchId.isNotBlank()) {
            loadingMatch = true
            retrofit.getMatchData(matchId).enqueue(object : Callback<MatchDataResult> {
                override fun onResponse(
                    call: Call<MatchDataResult>,
                    response: Response<MatchDataResult>
                ) {
                    Log.d("RETROFIT_CALL", "MatchViewModel: loadMatch")
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

    fun loadRegions() {
        if (loadingRegions) return
        if (ConstData.regions.isNotEmpty()) {
            _constRegions.value = Unit
            loadingRegions = false
        } else {
            loadingRegions = true
            retrofit.getRegions().enqueue(object : Callback<Map<String, String>> {
                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    Log.d("RETROFIT_CALL", "MatchViewModel: loadRegions")
                    val body = response.body()
                    if (body != null) {
                        ConstData.regions = body.mapKeys { it.key.toInt() }
                        _constRegions.value = Unit
                        loadingRegions = false
                    }
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingRegions = false
                }

            })
        }
    }

    fun loadItems() {
        if (loadingItems) return
        if (ConstData.items.isNotEmpty()) {
            _constItems.value = Unit
            loadingItems = false
        } else {
            loadingItems = true
            retrofit.getItems().enqueue(object : Callback<Map<String, ItemResult>> {
                override fun onResponse(
                    call: Call<Map<String, ItemResult>>,
                    response: Response<Map<String, ItemResult>>
                ) {
                    Log.d("RETROFIT_CALL", "MatchViewModel: loadItems")
                    val body = response.body()
                    if (body != null) {
                        ConstData.items = body
                        _constItems.value = Unit
                        loadingItems = false
                    }
                }

                override fun onFailure(call: Call<Map<String, ItemResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingItems = false
                }

            })
        }
    }

    fun loadAbilityIds() {
        if (loadingAbilityIds) return
        if (ConstData.abilityIds.isNotEmpty()) {
            _constAbilityIds.value = Unit
            loadingAbilityIds = false
        } else {
            loadingAbilityIds = true
            retrofit.getAbilityIds().enqueue(object : Callback<Map<String, String>> {
                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    Log.d("RETROFIT_CALL", "MatchViewModel: loadAbilityIds")
                    val body = response.body()
                    if (body != null) {
                        ConstData.abilityIds = body
                        _constAbilityIds.value = Unit
                        loadingAbilityIds = false
                    }
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingAbilityIds = false
                }

            })
        }
    }

    fun loadAbilities() {
        if (loadingAbilities) return
        if (ConstData.abilities.isNotEmpty()) {
            _constAbilities.value = Unit
            loadingAbilities = false
        } else {
            loadingAbilities = true
            retrofit.getAbilities().enqueue(object : Callback<Map<String, AbilityResult>> {
                override fun onResponse(
                    call: Call<Map<String, AbilityResult>>,
                    response: Response<Map<String, AbilityResult>>
                ) {
                    Log.d("RETROFIT_CALL", "MatchViewModel: loadAbilities")
                    val body = response.body()
                    if (body != null) {
                        ConstData.abilities = body
                        _constAbilityIds.value = Unit
                        loadingAbilities = false
                    }
                }

                override fun onFailure(call: Call<Map<String, AbilityResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingAbilities = false
                }

            })
        }
    }

}
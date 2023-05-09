package com.example.dotametrics.presentation.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.data.remote.model.constants.abilities.AbilityResult
import com.example.dotametrics.data.remote.model.constants.abilities.HeroAbilitiesResult
import com.example.dotametrics.data.remote.model.constants.aghs.AghsResult
import com.example.dotametrics.data.remote.model.constants.heroes.HeroResult
import com.example.dotametrics.data.remote.model.constants.items.ItemResult
import com.example.dotametrics.data.remote.model.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.data.remote.service.RetrofitInstance
import com.example.dotametrics.data.ConstData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConstViewModel(private val app: App) : ViewModel() {

    private var loadingHeroes = false
    private var loadingLobbies = false
    private var loadingRegions = false
    private var loadingItems = false
    private var loadingAbilityIds = false
    private var loadingAbilities = false
    private var loadingLore = false
    private var loadingAghs = false
    private var loadingHeroAbilities = false

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _heroes = MutableLiveData<Unit>()
    val heroes: LiveData<Unit>
        get() = _heroes

    private val _constLobbyTypes = MutableLiveData<Unit>()
    val constLobbyTypes: LiveData<Unit>
        get() = _constLobbyTypes

    private val _constRegions = MutableLiveData<Unit>()
    val constRegions: LiveData<Unit>
        get() = _constRegions

    private val _constItems = MutableLiveData<Unit>()
    val constItems: LiveData<Unit>
        get() = _constItems

    private val _constAbilityIds = MutableLiveData<Unit>()
    val constAbilityIds: LiveData<Unit>
        get() = _constAbilityIds

    private val _constAbilities = MutableLiveData<Unit>()
    val constAbilities: LiveData<Unit>
        get() = _constAbilities

    private val _constLores = MutableLiveData<Unit>()
    val constLores: LiveData<Unit>
        get() = _constLores

    private val _constAghs = MutableLiveData<Unit>()
    val constAghs: LiveData<Unit>
        get() = _constAghs

    private val _constHeroAbilities = MutableLiveData<Unit>()
    val constHeroAbilities: LiveData<Unit>
        get() = _constHeroAbilities

    private val retrofit = RetrofitInstance.getService()

    fun loadHeroes() {
        if (loadingHeroes) return
        if (ConstData.heroes.isNotEmpty()) {
            _heroes.value = Unit
            loadingHeroes = false
        } else {
            loadingHeroes = true
            retrofit.getConstHeroes().enqueue(object : Callback<Map<String, HeroResult>> {
                override fun onResponse(
                    call: Call<Map<String, HeroResult>>,
                    response: Response<Map<String, HeroResult>>
                ) {
                    val body = response.body()
                    if (body != null) {
                        ConstData.heroes = body.values.toList().sortedBy { it.localizedName }
                        _heroes.value = Unit
                        loadingHeroes = false
                    }
                }

                override fun onFailure(call: Call<Map<String, HeroResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingHeroes = false
                }
            })
        }
    }

    fun loadLobbyTypes() {
        if (loadingLobbies) return
        if (ConstData.lobbies.isNotEmpty()) {
            _constLobbyTypes.value = Unit
            loadingLobbies = false
        } else {
            loadingLobbies = true
            retrofit.getConstLobbyTypes().enqueue(object : Callback<Map<String, LobbyTypeResult>> {
                override fun onResponse(
                    call: Call<Map<String, LobbyTypeResult>>,
                    response: Response<Map<String, LobbyTypeResult>>
                ) {
                    val body = response.body()
                    if (body != null) {
                        val usefulLobbies = app.resources.getStringArray(R.array.lobbies_array)
                        ConstData.lobbies =
                            body.values.toList().filter { usefulLobbies.contains(it.name) }
                        _constLobbyTypes.value = Unit
                        loadingLobbies = false
                    }
                }

                override fun onFailure(call: Call<Map<String, LobbyTypeResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingLobbies = false
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
                    val body = response.body()
                    if (body != null) {
                        ConstData.abilities = body
                        _constAbilities.value = Unit
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

    fun loadLore() {
        if (loadingLore) return
        if (ConstData.lores.isNotEmpty()) {
            _constLores.value = Unit
            loadingLore = false
        } else {
            retrofit.getHeroLore().enqueue(object : Callback<Map<String, String>> {
                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    val body = response.body()
                    if (body != null) {
                        ConstData.lores = body
                        _constLores.value = Unit
                        loadingLore = false
                    }
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingLore = false
                }
            })
        }
    }

    fun loadAghs() {
        if (loadingAghs) return
        if (ConstData.aghs.isNotEmpty()) {
            _constAghs.value = Unit
            loadingAghs = false
        } else {
            retrofit.getAghs().enqueue(object : Callback<List<AghsResult>> {
                override fun onResponse(
                    call: Call<List<AghsResult>>,
                    response: Response<List<AghsResult>>
                ) {
                    val body = response.body()
                    if (body != null) {
                        ConstData.aghs = body
                        _constAghs.value = Unit
                        loadingAghs = false
                    }
                }

                override fun onFailure(call: Call<List<AghsResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingAghs = false
                }
            })
        }
    }

    fun loadHeroAbilities() {
        if (loadingHeroAbilities) return
        if (ConstData.heroAbilities.isNotEmpty()) {
            _constHeroAbilities.value = Unit
            loadingHeroAbilities = false
        } else {
            retrofit.getHeroAbilities()
                .enqueue(object : Callback<Map<String, HeroAbilitiesResult>> {
                    override fun onResponse(
                        call: Call<Map<String, HeroAbilitiesResult>>,
                        response: Response<Map<String, HeroAbilitiesResult>>
                    ) {
                        val body = response.body()
                        if (body != null) {
                            ConstData.heroAbilities = body
                            _constHeroAbilities.value = Unit
                            loadingHeroAbilities = false
                        }
                    }

                    override fun onFailure(
                        call: Call<Map<String, HeroAbilitiesResult>>,
                        t: Throwable
                    ) {
                        _error.value = t.message.toString()
                        loadingHeroAbilities = false
                    }
                })
        }
    }

    class ConstViewModelFactory(private val app: App) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConstViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ConstViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}
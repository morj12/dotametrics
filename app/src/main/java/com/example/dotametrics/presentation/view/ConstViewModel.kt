package com.example.dotametrics.presentation.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.domain.ConstData
import com.example.dotametrics.domain.entity.remote.constants.abilities.AbilityResult
import com.example.dotametrics.domain.entity.remote.constants.abilities.HeroAbilitiesResult
import com.example.dotametrics.domain.entity.remote.constants.aghs.AghsResult
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.domain.entity.remote.constants.items.ItemResult
import com.example.dotametrics.domain.entity.remote.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConstViewModel @Inject constructor(
    private val app: App,
    private val openDotaRepository: IOpenDotaRepository
) : ViewModel() {

    private var loadingHeroes = false
    private var loadingLobbies = false
    private var loadingRegions = false
    private var loadingItems = false
    private var loadingAbilityIds = false
    private var loadingAbilities = false
    private var loadingAghs = false
    private var loadingHeroAbilities = false

    private val _heroes = MutableLiveData<List<HeroResult>>(emptyList())
    val heroes: LiveData<List<HeroResult>>
        get() = _heroes

    private val _constLobbyTypes = MutableLiveData<List<LobbyTypeResult>>(emptyList())
    val constLobbyTypes: LiveData<List<LobbyTypeResult>>
        get() = _constLobbyTypes

    private val _constRegions = MutableLiveData<Map<Int, String>>(emptyMap())
    val constRegions: LiveData<Map<Int, String>>
        get() = _constRegions

    private val _constItems = MutableLiveData<Map<String, ItemResult>>(emptyMap())
    val constItems: LiveData<Map<String, ItemResult>>
        get() = _constItems

    private val _constAbilityIds = MutableLiveData<Map<String, String>>(emptyMap())
    val constAbilityIds: LiveData<Map<String, String>>
        get() = _constAbilityIds

    private val _constAbilities = MutableLiveData<Map<String, AbilityResult>>(emptyMap())
    val constAbilities: LiveData<Map<String, AbilityResult>>
        get() = _constAbilities

    private val _constAghs = MutableLiveData<List<AghsResult>>(emptyList())
    val constAghs: LiveData<List<AghsResult>>
        get() = _constAghs

    private val _constHeroAbilities = MutableLiveData<Map<String, HeroAbilitiesResult>>(emptyMap())
    val constHeroAbilities: LiveData<Map<String, HeroAbilitiesResult>>
        get() = _constHeroAbilities

    fun loadHeroes() {
        if (loadingHeroes) return
        if (ConstData.heroes.isNotEmpty()) {
            _heroes.value = ConstData.heroes
            loadingHeroes = false
        } else {
            loadingHeroes = true
            viewModelScope.launch {
                val result = openDotaRepository.getConstHeroes()
                if (result.error == "null") {
                    result.data?.let { heroMap ->
                        ConstData.heroes = heroMap.values.toList().sortedBy { it.localizedName }
                        _heroes.value = ConstData.heroes
                    }
                }
                loadingHeroes = false
            }
        }
    }

    fun loadLobbyTypes() {
        if (loadingLobbies) return
        if (ConstData.lobbies.isNotEmpty()) {
            _constLobbyTypes.value = ConstData.lobbies
            loadingLobbies = false
        } else {
            loadingLobbies = true
            viewModelScope.launch {
                val result = openDotaRepository.getConstLobbyTypes()
                if (result.error == "null") {
                    result.data?.let {
                        val usefulLobbies = app.resources.getStringArray(R.array.lobbies_array)
                        ConstData.lobbies =
                            it.values.toList().filter { v -> usefulLobbies.contains(v.name) }
                        _constLobbyTypes.value = ConstData.lobbies
                    }
                }
                loadingLobbies = false
            }
        }
    }

    fun loadRegions() {
        if (loadingRegions) return
        if (ConstData.regions.isNotEmpty()) {
            _constRegions.value = ConstData.regions
            loadingRegions = false
        } else {
            loadingRegions = true
            viewModelScope.launch {
                val result = openDotaRepository.getRegions()
                if (result.error == "null") {
                    result.data?.let {
                        ConstData.regions = it.mapKeys { k -> k.key.toInt() }
                        _constRegions.value = ConstData.regions
                    }
                }
                loadingRegions = false
            }
        }
    }

    fun loadItems() {
        if (loadingItems) return
        if (ConstData.items.isNotEmpty()) {
            _constItems.value = ConstData.items
            loadingItems = false
        } else {
            loadingItems = true
            viewModelScope.launch {
                val result = openDotaRepository.getItems()
                if (result.error == "null") {
                    result.data?.let {
                        ConstData.items = it
                        _constItems.value = ConstData.items
                    }
                }
                loadingItems = false
            }
        }
    }

    fun loadAbilityIds() {
        if (loadingAbilityIds) return
        if (ConstData.abilityIds.isNotEmpty()) {
            _constAbilityIds.value = ConstData.abilityIds
            loadingAbilityIds = false
        } else {
            loadingAbilityIds = true
            viewModelScope.launch {
                val result = openDotaRepository.getAbilityIds()
                if (result.error == "null") {
                    result.data?.let {
                        ConstData.abilityIds = it
                        _constAbilityIds.value = ConstData.abilityIds
                    }
                }
                loadingAbilityIds = false
            }
        }
    }

    fun loadAbilities() {
        if (loadingAbilities) return
        if (ConstData.abilities.isNotEmpty()) {
            _constAbilities.value = ConstData.abilities
            loadingAbilities = false
        } else {
            loadingAbilities = true
            viewModelScope.launch {
                val result = openDotaRepository.getAbilities()
                if (result.error == "null") {
                    result.data?.let {
                        ConstData.abilities = it
                        _constAbilities.value = ConstData.abilities
                    }
                }
                loadingAbilities = false
            }
        }
    }

    fun loadAghs() {
        if (loadingAghs) return
        if (ConstData.aghs.isNotEmpty()) {
            _constAghs.value = ConstData.aghs
            loadingAghs = false
        } else {
            loadingAghs = true
            viewModelScope.launch {
                val result = openDotaRepository.getAghs()
                if (result.error == "null") {
                    result.data?.let {
                        ConstData.aghs = it
                        _constAghs.value = ConstData.aghs
                    }
                }
                loadingAghs = false
            }
        }
    }

    fun loadHeroAbilities() {
        if (loadingHeroAbilities) return
        if (ConstData.heroAbilities.isNotEmpty()) {
            _constHeroAbilities.value = ConstData.heroAbilities
            loadingHeroAbilities = false
        } else {
            loadingHeroAbilities = true
            viewModelScope.launch {
                val result = openDotaRepository.getHeroAbilities()
                if (result.error == "null") {
                    result.data?.let {
                        ConstData.heroAbilities = it
                        _constHeroAbilities.value = ConstData.heroAbilities
                    }
                }
                loadingHeroAbilities = false
            }
        }
    }
}

package com.example.dotametrics.presentation.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dotametrics.App
import com.example.dotametrics.R
import com.example.dotametrics.data.ConstData
import com.example.dotametrics.data.remote.repository.OpenDotaRepository
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConstViewModel(private val app: App) : ViewModel() {

    private val openDotaRepository: IOpenDotaRepository = OpenDotaRepository()

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

    fun loadHeroes() {
        if (loadingHeroes) return
        if (ConstData.heroes.isNotEmpty()) {
            _heroes.value = Unit
            loadingHeroes = false
        } else {
            loadingHeroes = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getConstHeroes()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        ConstData.heroes = it.values.toList().sortedBy { it.localizedName }
                        _heroes.postValue(Unit)
                    }
                }
                loadingHeroes = false
            }
        }
    }

    fun loadLobbyTypes() {
        if (loadingLobbies) return
        if (ConstData.lobbies.isNotEmpty()) {
            _constLobbyTypes.value = Unit
            loadingLobbies = false
        } else {
            loadingLobbies = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getConstLobbyTypes()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        val usefulLobbies = app.resources.getStringArray(R.array.lobbies_array)
                        ConstData.lobbies =
                            it.values.toList().filter { v -> usefulLobbies.contains(v.name) }
                        _constLobbyTypes.postValue(Unit)
                    }
                }
                loadingLobbies = false
            }
        }
    }

    fun loadRegions() {
        if (loadingRegions) return
        if (ConstData.regions.isNotEmpty()) {
            _constRegions.value = Unit
            loadingRegions = false
        } else {
            loadingRegions = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getRegions()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        ConstData.regions = it.mapKeys { k -> k.key.toInt() }
                        _constRegions.postValue(Unit)
                    }
                }
                loadingRegions = false
            }
        }
    }

    fun loadItems() {
        if (loadingItems) return
        if (ConstData.items.isNotEmpty()) {
            _constItems.value = Unit
            loadingItems = false
        } else {
            loadingItems = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getItems()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        ConstData.items = it
                        _constItems.postValue(Unit)
                    }
                }
                loadingItems = false
            }
        }
    }

    fun loadAbilityIds() {
        if (loadingAbilityIds) return
        if (ConstData.abilityIds.isNotEmpty()) {
            _constAbilityIds.value = Unit
            loadingAbilityIds = false
        } else {
            loadingAbilityIds = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getAbilityIds()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        ConstData.abilityIds = it
                        _constAbilityIds.postValue(Unit)
                    }
                }
                loadingAbilityIds = false
            }
        }
    }

    fun loadAbilities() {
        if (loadingAbilities) return
        if (ConstData.abilities.isNotEmpty()) {
            _constAbilities.value = Unit
            loadingAbilities = false
        } else {
            loadingAbilities = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getAbilities()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        ConstData.abilities = it
                        _constAbilities.postValue(Unit)
                    }
                }
                loadingAbilities = false
            }
        }
    }

    fun loadLore() {
        if (loadingLore) return
        if (ConstData.lores.isNotEmpty()) {
            _constLores.value = Unit
            loadingLore = false
        } else {
            loadingLore = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getHeroLore()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        ConstData.lores = it
                        _constLores.postValue(Unit)
                    }
                }
                loadingLore = false
            }
        }
    }

    fun loadAghs() {
        if (loadingAghs) return
        if (ConstData.aghs.isNotEmpty()) {
            _constAghs.value = Unit
            loadingAghs = false
        } else {
            loadingAghs = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getAghs()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        ConstData.aghs = it
                        _constAghs.postValue(Unit)
                    }
                }
                loadingAghs = false
            }
        }
    }

    fun loadHeroAbilities() {
        if (loadingHeroAbilities) return
        if (ConstData.heroAbilities.isNotEmpty()) {
            _constHeroAbilities.value = Unit
            loadingHeroAbilities = false
        } else {
            loadingHeroAbilities = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getHeroAbilities()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        ConstData.heroAbilities = it
                        _constHeroAbilities.postValue(Unit)
                    }
                }
                loadingHeroAbilities = false
            }
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
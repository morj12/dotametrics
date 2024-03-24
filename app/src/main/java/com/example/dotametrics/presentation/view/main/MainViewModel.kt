package com.example.dotametrics.presentation.view.main

import androidx.lifecycle.*
import com.example.dotametrics.App
import com.example.dotametrics.data.local.repository.PlayerRepository
import com.example.dotametrics.data.remote.model.search.SearchResult
import com.example.dotametrics.data.remote.repository.OpenDotaRepository
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import com.example.dotametrics.domain.repository.IPlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val app: App) : ViewModel() {

    private val playerRepository: IPlayerRepository = PlayerRepository(app.db)

    private val openDotaRepository: IOpenDotaRepository = OpenDotaRepository()

    private val _results = MutableLiveData<List<SearchResult>>()
    val results: LiveData<List<SearchResult>>
        get() = _results

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    val players = playerRepository.getPlayers().asLiveData()

    fun search(user: String) {
        if (user.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getSearchResults(user)
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let { _results.postValue(it) }
                }
            }
        }
    }

    class MainViewModelFactory(private val app: App) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

}
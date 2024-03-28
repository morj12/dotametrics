package com.example.dotametrics.presentation.view.main

import android.util.Log
import androidx.lifecycle.*
import com.example.dotametrics.domain.entity.remote.search.SearchResult
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import com.example.dotametrics.domain.repository.IPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val openDotaRepository: IOpenDotaRepository,
    private val playerRepository: IPlayerRepository
) : ViewModel() {

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
                try {
                    val result = openDotaRepository.getSearchResults(user)
                    if (result.error != "null") {
                        _error.postValue(result.error)
                        Log.e("DOTA_RETROFIT", result.error)
                    } else {
                        result.data?.let { _results.postValue(it) }
                    }
                } catch (e: Exception) {
                    _error.postValue(e.message.toString())
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

}
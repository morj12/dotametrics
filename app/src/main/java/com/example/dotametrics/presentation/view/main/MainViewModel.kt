package com.example.dotametrics.presentation.view.main

import androidx.lifecycle.*
import com.example.dotametrics.domain.entity.remote.search.SearchResult
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import com.example.dotametrics.domain.repository.IPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val openDotaRepository: IOpenDotaRepository,
    playerRepository: IPlayerRepository
) : ViewModel() {

    private val _results = MutableLiveData<List<SearchResult>>()
    val results: LiveData<List<SearchResult>>
        get() = _results

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    val players = playerRepository.getPlayers().asLiveData()

    fun search(user: String) {
        if (user.isNotBlank()) {
            viewModelScope.launch {
                val result = openDotaRepository.getSearchResults(user)
                if (result.error == "null") {
                    result.data?.let { _results.value = it }
                } else if (result.error.contains("rate_limit_exceeded", ignoreCase = true)) {
                    _error.value = "rate_limit_exceeded"
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

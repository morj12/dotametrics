package com.example.dotametrics.presentation.view.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotametrics.domain.entity.remote.matches.MatchDataResult
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(private val openDotaRepository: IOpenDotaRepository) : ViewModel() {

    private var loadingMatch = false

    var matchId: String = ""

    private val _result = MutableLiveData<MatchDataResult>()
    val result: LiveData<MatchDataResult>
        get() = _result

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun loadMatch() {
        if (loadingMatch) return
        if (matchId.isNotBlank()) {
            loadingMatch = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = openDotaRepository.getMatchData(matchId)
                    if (result.error != "null") {
                        _error.postValue(result.error)
                        Log.e("DOTA_RETROFIT", result.error)
                    } else {
                        result.data?.let { if (!it.isNull()) _result.postValue(it) }
                    }
                    loadingMatch = false
                } catch (e: Exception) {
                    _error.postValue(e.message.toString())
                    Log.e("DOTA_RETROFIT", e.message.toString())
                }
            }
        }
    }

}
package com.example.dotametrics.presentation.view.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotametrics.domain.entity.remote.matches.MatchDataResult
import com.example.dotametrics.data.remote.repository.OpenDotaRepository
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MatchViewModel : ViewModel() {

    private val openDotaRepository: IOpenDotaRepository = OpenDotaRepository()

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
                val result = openDotaRepository.getMatchData(matchId)
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let { if (!it.isNull()) _result.postValue(it) }
                }
                loadingMatch = false
            }
        }
    }

}
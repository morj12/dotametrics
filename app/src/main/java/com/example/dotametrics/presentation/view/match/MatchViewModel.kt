package com.example.dotametrics.presentation.view.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotametrics.domain.entity.remote.matches.MatchDataResult
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(private val openDotaRepository: IOpenDotaRepository) : ViewModel() {

    private var loadingMatch = false

    var matchId: String = ""

    private val _result = MutableLiveData<MatchDataResult>()
    val result: LiveData<MatchDataResult>
        get() = _result

    fun loadMatch() {
        if (loadingMatch) return
        if (matchId.isNotBlank()) {
            loadingMatch = true
            viewModelScope.launch {
                val result = openDotaRepository.getMatchData(matchId)
                if (result.error == "null") {
                    result.data?.let { if (!it.isNull()) _result.value = it }
                }
                loadingMatch = false
            }
        }
    }
}

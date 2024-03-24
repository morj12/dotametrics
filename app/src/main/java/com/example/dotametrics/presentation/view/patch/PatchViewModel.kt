package com.example.dotametrics.presentation.view.patch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotametrics.data.remote.model.constants.patch.PatchNotesResult
import com.example.dotametrics.data.ConstData
import com.example.dotametrics.data.remote.repository.OpenDotaRepository
import com.example.dotametrics.domain.repository.IOpenDotaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PatchViewModel : ViewModel() {

    private val openDotaRepository: IOpenDotaRepository = OpenDotaRepository()

    private var loadingPatches = false
    private var loadingPatchNotes = false

    private val _patches = MutableLiveData<Unit>()
    val patches: LiveData<Unit>
        get() = _patches

    private val _patchNotes = MutableLiveData<Unit>()
    val patchNotes: LiveData<Unit>
        get() = _patchNotes

    private val _currentSeries = MutableLiveData<Map<String, PatchNotesResult>>()
    val currentSeries: LiveData<Map<String, PatchNotesResult>>
        get() = _currentSeries

    private val _currentPatchNotes = MutableLiveData<Pair<String, PatchNotesResult>>()
    val currentPatchNotes: LiveData<Pair<String, PatchNotesResult>>
        get() = _currentPatchNotes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun loadPatches() {
        if (loadingPatches) return
        if (ConstData.patches.isNotEmpty()) {
            _patches.value = Unit
            loadingPatches = false
        } else {
            loadingPatches = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getPatches()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        ConstData.patches = it.reversed()
                        _patches.postValue(Unit)
                    }
                }
                loadingPatches = false
            }
        }
    }

    fun loadPatchNotes() {
        if (loadingPatchNotes) return
        if (ConstData.patchNotes.isNotEmpty()) {
            _patchNotes.value = Unit
            loadingPatchNotes = false
        } else {
            loadingPatchNotes = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = openDotaRepository.getPatchNotes()
                if (result.error != "null") {
                    _error.postValue(result.error)
                } else {
                    result.data?.let {
                        ConstData.patchNotes = it
                        _patches.postValue(Unit)
                    }
                }
                loadingPatchNotes = false
            }
        }
    }

    fun setSeries(series: Map<String, PatchNotesResult>) {
        this._currentSeries.value = series
    }

    fun clearCurrentSeries() {
        this._currentSeries.value = null
    }

    fun setPatch(patch: Pair<String, PatchNotesResult>) {
        this._currentPatchNotes.value = patch
    }

    fun clearCurrentPatch() {
        this._currentPatchNotes.value = null
    }
}
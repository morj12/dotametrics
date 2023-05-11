package com.example.dotametrics.presentation.view.patch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotametrics.data.remote.model.constants.patch.PatchResult
import com.example.dotametrics.data.remote.model.constants.patch.PatchNotesResult
import com.example.dotametrics.data.remote.service.RetrofitInstance
import com.example.dotametrics.data.ConstData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatchViewModel : ViewModel() {

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

    private val retrofit = RetrofitInstance.getService()

    fun loadPatches() {
        if (loadingPatches) return
        if (ConstData.patches.isNotEmpty()) {
            _patches.value = Unit
            loadingPatches = false
        } else {
            loadingPatches = true
            retrofit.getPatches().enqueue(object : Callback<List<PatchResult>> {
                override fun onResponse(
                    call: Call<List<PatchResult>>,
                    response: Response<List<PatchResult>>
                ) {
                    val body = response.body()
                    if (body != null) {
                        ConstData.patches = body.reversed()
                        _patches.value = Unit
                        loadingPatches = false
                    }
                }

                override fun onFailure(call: Call<List<PatchResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingPatches = false
                }
            }
            )
        }
    }

    fun loadPatchNotes() {
        if (loadingPatchNotes) return
        if (ConstData.patchNotes.isNotEmpty()) {
            _patchNotes.value = Unit
            loadingPatchNotes = false
        } else {
            loadingPatchNotes = true
            retrofit.getPatchNotes().enqueue(object : Callback<Map<String, PatchNotesResult>> {
                override fun onResponse(
                    call: Call<Map<String, PatchNotesResult>>,
                    response: Response<Map<String, PatchNotesResult>>
                ) {
                    val body = response.body()
                    if (body != null) {
                        ConstData.patchNotes = body
                        _patchNotes.value = Unit
                        loadingPatchNotes = false
                    }
                }

                override fun onFailure(call: Call<Map<String, PatchNotesResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingPatchNotes = false
                }
            })
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
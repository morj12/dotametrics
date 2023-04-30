package com.example.dotametrics.presentation.view.patch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.App
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.model.constants.items.ItemResult
import com.example.dotametrics.data.model.constants.patch.PatchResult
import com.example.dotametrics.data.model.constants.patch.PatchNotesResult
import com.example.dotametrics.data.service.RetrofitInstance
import com.example.dotametrics.util.ConstData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatchViewModel(private val application: App) : ViewModel() {

    private var loadingPatches = false
    private var loadingPatchNotes = false
    private var loadingHeroes = false
    private var loadingItems = false

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

    private val _constHeroes = MutableLiveData<Unit>()
    val constHeroes: LiveData<Unit>
        get() = _constHeroes

    private val _constItems = MutableLiveData<Unit>()
    val constItems: LiveData<Unit>
        get() = _constItems

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
                    Log.d("RETROFIT_CALL", "PatchViewModel: loadPatches")
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
                    Log.d("RETROFIT_CALL", "PatchViewModel: loadPatchDetails")
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
            }
            )
        }
    }

    fun loadHeroes() {
        if (loadingHeroes) return
        if (ConstData.heroes.isNotEmpty()) {
            _constHeroes.value = Unit
            loadingHeroes = false
        } else {
            loadingHeroes = true
            retrofit.getConstHeroes().enqueue(object : Callback<Map<String, HeroResult>> {
                override fun onResponse(
                    call: Call<Map<String, HeroResult>>,
                    response: Response<Map<String, HeroResult>>
                ) {
                    Log.d("RETROFIT_CALL", "AccountViewModel: loadHeroes")
                    val body = response.body()
                    if (body != null) {
                        ConstData.heroes = body.values.toList().sortedBy { it.localizedName }
                        _constHeroes.value = Unit
                        loadingHeroes = false
                    }
                }

                override fun onFailure(call: Call<Map<String, HeroResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingHeroes = false
                }
            })
        }
    }

    fun loadItems() {
        if (loadingItems) return
        if (ConstData.items.isNotEmpty()) {
            _constItems.value = Unit
            loadingItems = false
        } else {
            loadingItems = true
            retrofit.getItems().enqueue(object : Callback<Map<String, ItemResult>> {
                override fun onResponse(
                    call: Call<Map<String, ItemResult>>,
                    response: Response<Map<String, ItemResult>>
                ) {
                    Log.d("RETROFIT_CALL", "MatchViewModel: loadItems")
                    val body = response.body()
                    if (body != null) {
                        ConstData.items = body
                        _constItems.value = Unit
                        loadingItems = false
                    }
                }

                override fun onFailure(call: Call<Map<String, ItemResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                    loadingItems = false
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

    class PatchViewModelFactory(private val app: App) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PatchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PatchViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}
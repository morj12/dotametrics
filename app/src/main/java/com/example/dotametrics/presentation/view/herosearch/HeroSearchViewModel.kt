package com.example.dotametrics.presentation.view.herosearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.App
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.service.RetrofitInstance
import com.example.dotametrics.util.ConstData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeroSearchViewModel(private val app: App) : ViewModel() {

    private var loadingHeroes = false

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _heroes = MutableLiveData<Unit>()
    val heroes: LiveData<Unit>
        get() = _heroes

    private val _filteredHeroes = MutableLiveData<List<HeroResult>>()
    val filteredHeroes: LiveData<List<HeroResult>>
        get() = _filteredHeroes

    private val retrofit = RetrofitInstance.getService()

    fun loadHeroes() {
        if (loadingHeroes) return
        if (ConstData.heroes.isNotEmpty()) {
            _heroes.value = Unit
            loadingHeroes = false
        } else {
            loadingHeroes = true
            retrofit.getConstHeroes().enqueue(object : Callback<Map<String, HeroResult>> {
                override fun onResponse(
                    call: Call<Map<String, HeroResult>>,
                    response: Response<Map<String, HeroResult>>
                ) {
                    Log.d("RETROFIT_CALL", "HeroSearchViewModel: loadHeroes")
                    val body = response.body()
                    if (body != null) {
                        ConstData.heroes = body.values.toList().sortedBy { it.localizedName }
                        _heroes.value = Unit
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

    fun filterHeroes(name: String? = null) {
        if (name == null) {
            _filteredHeroes.value = ConstData.heroes
        } else {
            _filteredHeroes.value =
                ConstData.heroes.filter {
                    it.localizedName!!.lowercase().contains(name.lowercase())
                }
        }
    }

    class HeroSearchViewModelFactory(private val app: App) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HeroSearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HeroSearchViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}
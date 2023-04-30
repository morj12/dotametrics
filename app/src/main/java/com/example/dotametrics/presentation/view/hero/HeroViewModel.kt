package com.example.dotametrics.presentation.view.hero

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.App
import com.example.dotametrics.data.model.constants.abilities.AbilityResult
import com.example.dotametrics.data.model.constants.abilities.HeroAbilitiesResult
import com.example.dotametrics.data.model.constants.aghs.AghsResult
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.model.players.PlayersResult
import com.example.dotametrics.data.model.players.wl.WLResult
import com.example.dotametrics.data.service.RetrofitInstance
import com.example.dotametrics.util.ConstData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeroViewModel(private val app: App) : ViewModel() {

    private val _hero = MutableLiveData<HeroResult>()
    val hero: LiveData<HeroResult>
        get() = _hero

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _constLores = MutableLiveData<Unit>()
    val constLores: LiveData<Unit>
        get() = _constLores

    private val _constAghs = MutableLiveData<Unit>()
    val constAghs: LiveData<Unit>
        get() = _constAghs

    private val _constHeroAbilities = MutableLiveData<Unit>()
    val constHeroAbilities: LiveData<Unit>
        get() = _constHeroAbilities

    private val _constAbilities = MutableLiveData<Unit>()
    val constAbilities: LiveData<Unit>
        get() = _constAbilities

    private val retrofit = RetrofitInstance.getService()

    fun setHero(hero: HeroResult) {
        _hero.value = hero
    }

    fun loadLore() {
        if (_hero.value != null) {
            retrofit.getHeroLore().enqueue(object : Callback<Map<String, String>> {

                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    Log.d("RETROFIT_CALL", "HeroViewModel: loadLore")
                    val body = response.body()
                    if (body != null) {
                        ConstData.lores = body
                        _constLores.value = Unit
                    }
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    _error.value = t.message.toString()
                }
            })
        }
    }

    fun loadAghs() {
        if (_hero.value != null) {
            retrofit.getAghs().enqueue(object : Callback<List<AghsResult>> {
                override fun onResponse(
                    call: Call<List<AghsResult>>,
                    response: Response<List<AghsResult>>
                ) {
                    Log.d("RETROFIT_CALL", "HeroViewModel: loadAghs")
                    val body = response.body()
                    if (body != null) {
                        ConstData.aghs = body
                        _constAghs.value = Unit
                    }
                }

                override fun onFailure(call: Call<List<AghsResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                }
            })
        }
    }


    fun loadAbilities() {
        if (_hero.value != null) {
            retrofit.getAbilities().enqueue(object : Callback<Map<String, AbilityResult>> {
                override fun onResponse(
                    call: Call<Map<String, AbilityResult>>,
                    response: Response<Map<String, AbilityResult>>
                ) {
                    Log.d("RETROFIT_CALL", "HeroViewModel: loadAbilities")
                    val body = response.body()
                    if (body != null) {
                        ConstData.abilities = body
                        _constAbilities.value = Unit
                    }
                }

                override fun onFailure(call: Call<Map<String, AbilityResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                }
            })
        }
    }


    fun loadHeroAbilities() {
        if (_hero.value != null) {
            retrofit.getHeroAbilities().enqueue(object : Callback<Map<String,HeroAbilitiesResult>> {
                override fun onResponse(
                    call: Call<Map<String,HeroAbilitiesResult>>,
                    response: Response<Map<String,HeroAbilitiesResult>>
                ) {
                    Log.d("RETROFIT_CALL", "HeroViewModel: loadHeroAbilities")
                    val body = response.body()
                    if (body != null) {
                        ConstData.heroAbilities = body
                        _constHeroAbilities.value = Unit
                    }
                }

                override fun onFailure(call: Call<Map<String,HeroAbilitiesResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                }
            })
        }
    }

    class HeroViewModelFactory(private val app: App) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HeroViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HeroViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

}
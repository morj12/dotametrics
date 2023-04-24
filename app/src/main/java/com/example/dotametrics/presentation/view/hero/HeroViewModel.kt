package com.example.dotametrics.presentation.view.hero

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dotametrics.App
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.service.RetrofitInstance

class HeroViewModel(private val app: App): ViewModel() {

    private val _hero = MutableLiveData<HeroResult>()
    val hero: LiveData<HeroResult>
        get() = _hero

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val retrofit = RetrofitInstance.getService()

    fun setHero(hero: HeroResult) {
        _hero.value = hero
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
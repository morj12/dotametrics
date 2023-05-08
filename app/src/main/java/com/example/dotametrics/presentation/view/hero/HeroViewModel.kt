package com.example.dotametrics.presentation.view.hero

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotametrics.data.remote.model.constants.heroes.HeroResult

class HeroViewModel : ViewModel() {

    private val _hero = MutableLiveData<HeroResult>()
    val hero: LiveData<HeroResult>
        get() = _hero

    fun setHero(hero: HeroResult) {
        _hero.value = hero
    }

}
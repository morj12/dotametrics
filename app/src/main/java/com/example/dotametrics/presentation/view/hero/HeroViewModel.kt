package com.example.dotametrics.presentation.view.hero

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HeroViewModel @Inject constructor() : ViewModel() {

    private val _hero = MutableLiveData<HeroResult>()
    val hero: LiveData<HeroResult>
        get() = _hero

    fun setHero(hero: HeroResult) {
        _hero.value = hero
    }

}
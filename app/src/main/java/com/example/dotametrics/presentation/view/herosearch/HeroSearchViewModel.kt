package com.example.dotametrics.presentation.view.herosearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.domain.ConstData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HeroSearchViewModel @Inject constructor() : ViewModel() {

    private val _filteredHeroes = MutableLiveData<List<HeroResult>>()
    val filteredHeroes: LiveData<List<HeroResult>>
        get() = _filteredHeroes

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
}
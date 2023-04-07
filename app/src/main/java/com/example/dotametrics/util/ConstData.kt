package com.example.dotametrics.util

import com.example.dotametrics.data.model.constants.abilities.AbilityResult
import com.example.dotametrics.data.model.constants.heroes.HeroResult
import com.example.dotametrics.data.model.constants.items.ItemResult
import com.example.dotametrics.data.model.constants.lobbytypes.LobbyTypeResult

object ConstData {

    var heroes = listOf<HeroResult>()
    var lobbies = listOf<LobbyTypeResult>()
    var regions = mapOf<Int, String>()
    var items = listOf<ItemResult>()
    var abilityIds = mapOf<String, String>()
    var abilities = mapOf<String, AbilityResult>()

}
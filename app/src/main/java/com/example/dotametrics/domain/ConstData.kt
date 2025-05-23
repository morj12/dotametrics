package com.example.dotametrics.domain

import com.example.dotametrics.domain.entity.remote.constants.abilities.AbilityResult
import com.example.dotametrics.domain.entity.remote.constants.abilities.HeroAbilitiesResult
import com.example.dotametrics.domain.entity.remote.constants.aghs.AghsResult
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.domain.entity.remote.constants.items.ItemResult
import com.example.dotametrics.domain.entity.remote.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.domain.entity.remote.teams.TeamsResult

object ConstData {

    var heroes = listOf<HeroResult>()
    var lobbies = listOf<LobbyTypeResult>()
    var regions = mapOf<Int, String>()
    var items = mapOf<String, ItemResult>()
    var abilityIds = mapOf<String, String>()
    var abilities = mapOf<String, AbilityResult>()
    var teams = listOf<TeamsResult>()
    var lores = mapOf<String, String>()
    var aghs = listOf<AghsResult>()
    var heroAbilities = mapOf<String, HeroAbilitiesResult>()

}
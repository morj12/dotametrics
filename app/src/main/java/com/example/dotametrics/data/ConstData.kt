package com.example.dotametrics.data

import com.example.dotametrics.data.remote.model.constants.abilities.AbilityResult
import com.example.dotametrics.data.remote.model.constants.abilities.HeroAbilitiesResult
import com.example.dotametrics.data.remote.model.constants.aghs.AghsResult
import com.example.dotametrics.data.remote.model.constants.heroes.HeroResult
import com.example.dotametrics.data.remote.model.constants.items.ItemResult
import com.example.dotametrics.data.remote.model.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.data.remote.model.constants.patch.PatchResult
import com.example.dotametrics.data.remote.model.constants.patch.PatchNotesResult
import com.example.dotametrics.data.remote.model.teams.TeamsResult

object ConstData {

    var heroes = listOf<HeroResult>()
    var lobbies = listOf<LobbyTypeResult>()
    var regions = mapOf<Int, String>()
    var items = mapOf<String, ItemResult>()
    var abilityIds = mapOf<String, String>()
    var abilities = mapOf<String, AbilityResult>()
    var patches = listOf<PatchResult>()
    var patchNotes = mapOf<String, PatchNotesResult>()
    var teams = listOf<TeamsResult>()
    var lores = mapOf<String, String>()
    var aghs = listOf<AghsResult>()
    var heroAbilities = mapOf<String, HeroAbilitiesResult>()

}
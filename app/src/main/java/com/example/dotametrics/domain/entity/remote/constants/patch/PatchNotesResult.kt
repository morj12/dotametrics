package com.example.dotametrics.domain.entity.remote.constants.patch

data class PatchNotesResult(
    var general: ArrayList<String> = arrayListOf(),
    var items: Map<String, ArrayList<String>> = mapOf(),
    var heroes: Map<String, ArrayList<String>> = mapOf()
)
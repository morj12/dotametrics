package com.example.dotametrics.data.remote.model.constants.patch

data class PatchNotesResult(
    var general: ArrayList<String> = arrayListOf(),
    var items: Map<String, ArrayList<String>> = mapOf(),
    var heroes: Map<String, ArrayList<String>> = mapOf()
)
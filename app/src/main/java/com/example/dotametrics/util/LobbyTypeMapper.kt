package com.example.dotametrics.util

import android.content.Context

object LobbyTypeMapper {

    fun getLobbyResource(field: String, context: Context) =
        context.resources.getIdentifier(field, "string", context.packageName)
}
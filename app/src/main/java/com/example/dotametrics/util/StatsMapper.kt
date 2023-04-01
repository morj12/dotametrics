package com.example.dotametrics.util

import android.content.Context

object StatsMapper {

    fun getStatsResource(field: String, context: Context) =
        context.resources.getIdentifier(field, "string", context.packageName)

}
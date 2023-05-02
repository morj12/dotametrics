package com.example.dotametrics.util

import android.content.Context

class StatsMapper {

    fun getStatsResource(field: String, context: Context) =
        context.resources.getIdentifier(field, "string", context.packageName)

}
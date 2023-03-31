package com.example.dotametrics.util

import android.content.Context
import android.util.Log
import com.example.dotametrics.R

object StatsMapper {

    fun getStatsResource(field: String, context: Context): Int {
        val resId =
            if (field == "throw") {
                R.string.totals_throw
            } else
                context.resources.getIdentifier(field, "string", context.packageName)
        return resId
    }

}
package com.example.dotametrics.util

import android.content.Context
import com.example.dotametrics.R

class AttrMapper {

    fun mapAttr(context: Context, attr: String?): String {
        return when (attr) {
            "str" -> context.getString(R.string.str_name)
            "agi" -> context.getString(R.string.agi_name)
            "int" -> context.getString(R.string.int_name)
            "all" -> context.getString(R.string.all_name)
            else -> "Unknown"
        }
    }
}
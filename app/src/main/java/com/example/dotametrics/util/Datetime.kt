package com.example.dotametrics.util

object Datetime {

    fun formatTime(datetime: String?): String {
        if (datetime == null) return ""
        return datetime.slice(0 until datetime.indexOf("T"))
    }

}
package com.example.dotametrics.util

import java.text.SimpleDateFormat
import java.util.*

object Datetime {

    private const val DEFAULT_TIME_FORMAT = "yyyy-MM-dd"

    fun formatTime(datetime: String?): String {
        if (datetime == null) return ""
        return datetime.slice(0 until datetime.indexOf("T"))
    }

    fun getDateTime(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        return SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault()).format(date)
    }

}
package com.example.dotametrics.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object Datetime {

    private const val ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    fun formatDate(datetime: String?): String {
        if (datetime == null) return ""
        val date = SimpleDateFormat(ISO_8601_FORMAT, Locale.getDefault()).parse(datetime)
        return DateUtils.getRelativeTimeSpanString(date.time).toString()
    }

    fun formatDate(timestamp: Long): String {
        val datetime = getDateTime(timestamp)
        val date = SimpleDateFormat(ISO_8601_FORMAT, Locale.getDefault()).parse(datetime)
        return DateUtils.getRelativeTimeSpanString(date.time).toString()
    }

    private fun getDateTime(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        return SimpleDateFormat(ISO_8601_FORMAT, Locale.getDefault()).format(date)
    }

    fun getStringTime(duration: Int): String {
        val hours = duration / 3600
        val minutes = (duration % 3600) / 60
        val seconds = duration % 60

        return if (hours > 0) String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else String.format("%02d:%02d", minutes, seconds)
    }

}
package com.example.dotametrics.util

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object DotaLogger {
    private const val TAG_API_ERROR = "DOTA_API_ERROR"
    private const val TAG_API_EXCEPTION = "DOTA_API_EXCEPTION"
    private const val TAG_CRASH = "DOTA_CRASH"

    private val _uiEvents = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val uiEvents = _uiEvents.asSharedFlow()

    fun logApiError(url: String, errorBody: String, isRateLimit: Boolean = false) {
        if (isRateLimit) {
            _uiEvents.tryEmit("RATE_LIMIT_EXCEEDED")
        } else {
            Log.e(TAG_API_ERROR, "API Error at $url: $errorBody")
        }
    }

    fun logApiException(message: String?, throwable: Throwable? = null) {
        Log.e(TAG_API_EXCEPTION, "Request Exception: $message", throwable)
    }

    fun logCrash(threadName: String, throwable: Throwable) {
        Log.e(TAG_CRASH, "FATAL CRASH in thread $threadName: ${throwable.message}", throwable)
    }
}

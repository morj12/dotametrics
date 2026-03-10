package com.example.dotametrics.util

class GlobalExceptionHandler : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        DotaLogger.logCrash(thread.name, throwable)
        
        defaultHandler?.uncaughtException(thread, throwable)
    }
}

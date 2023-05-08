package com.example.dotametrics

import android.app.Application
import com.example.dotametrics.data.local.AppDatabase

class App : Application() {
    val db by lazy { AppDatabase.getDatabase(this) }
}
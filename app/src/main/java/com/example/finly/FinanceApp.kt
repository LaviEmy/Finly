package com.example.finly

import android.app.Application
import com.example.finly.data.local.AppDatabase

class FinanceApp : Application() {
    val database by lazy {
        AppDatabase.getDatabase(this)
    }
}
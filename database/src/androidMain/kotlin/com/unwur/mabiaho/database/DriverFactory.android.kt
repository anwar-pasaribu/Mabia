package com.unwur.mabiaho.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual object DriverFactory {
    lateinit var appContext: Context
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            MabiaDatabase.Schema,
            appContext,
            "mabia.db"
        )
    }
}
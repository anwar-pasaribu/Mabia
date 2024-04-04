package com.unwur.mabiaho.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual object DriverFactory {
    actual fun createDriver(dbName: String): SqlDriver {
        return NativeSqliteDriver(MabiaDatabase.Schema, dbName)
    }
}
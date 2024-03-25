package com.unwur.mabiaho.database

import app.cash.sqldelight.db.SqlDriver

expect object DriverFactory {
    fun createDriver(): SqlDriver
}

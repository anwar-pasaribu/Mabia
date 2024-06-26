package com.unwur.mabiaho.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual object DriverFactory {
    actual fun createDriver(dbName: String): SqlDriver {
        val databasePath = File(System.getProperty("java.io.tmpdir"), dbName)
        val driver: SqlDriver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}")
//        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        MabiaDatabase.Schema.create(driver)
        return driver
    }
}
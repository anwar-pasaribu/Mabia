package com.unwur.mabiaho.database

const val MABIA_DB_NAME = "mabia.db"

fun createDatabase(): MabiaDatabase {
    return MabiaDatabase(
        DriverFactory.createDriver(MABIA_DB_NAME)
    )
}
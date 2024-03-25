package com.unwur.mabiaho.database


fun createDatabase(): MabiaDatabase {
    return MabiaDatabase(
        DriverFactory.createDriver()
    )
}
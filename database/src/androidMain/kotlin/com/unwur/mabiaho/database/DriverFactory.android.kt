package com.unwur.mabiaho.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

import java.lang.ref.WeakReference

actual object DriverFactory {

    private var appContextRef: WeakReference<Context>? = null

    fun initAndroid(appContext: Context) {
        appContextRef = WeakReference(appContext)
    }

    actual fun createDriver(dbName: String): SqlDriver {
        val appContext = appContextRef?.get() ?: throw IllegalStateException("App Context not initialized or already released")
        return AndroidSqliteDriver(
            MabiaDatabase.Schema,
            appContext,
            dbName
        )
    }
}

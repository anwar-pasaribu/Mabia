package com.unwur.mabiaho

import android.app.Application
import com.unwur.mabiaho.database.DriverFactory
import di.letsKoinStart

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO THIS NOT SUPPOSED TO BE HERE
        DriverFactory.appContext = this
        initKoin()
    }

    private fun initKoin() {
        letsKoinStart()
    }
}
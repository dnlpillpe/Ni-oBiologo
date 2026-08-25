package com.educalab.ninobiologo

import android.app.Application

class NinoBiologoApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

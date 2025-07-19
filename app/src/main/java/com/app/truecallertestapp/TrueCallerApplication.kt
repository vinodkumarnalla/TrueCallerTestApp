package com.app.truecallertestapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TrueCallerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
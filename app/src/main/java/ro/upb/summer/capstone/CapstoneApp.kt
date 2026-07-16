package ro.upb.summer.capstone

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class. Currently minimal. Session 2 adds Hilt initialization here.
 */
@HiltAndroidApp
class CapstoneApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Intentionally empty. Session 2 wires Hilt. Session 4 wires App Check.
    }
}
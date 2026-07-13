package ro.upb.summer.capstone

import android.app.Application

/**
 * Application class. Currently minimal. Session 2 adds Hilt initialization here.
 */
class CapstoneApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Intentionally empty. Session 2 wires Hilt. Session 4 wires App Check.
    }
}
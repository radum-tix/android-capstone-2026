package ro.upb.summer.capstone

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class. Currently minimal. Session 2 adds Hilt initialization here.
 */
@HiltAndroidApp
class CapstoneApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.appCheck.installAppCheckProviderFactory(
            appCheckProviderFactoryInstance()
        )
    }
}
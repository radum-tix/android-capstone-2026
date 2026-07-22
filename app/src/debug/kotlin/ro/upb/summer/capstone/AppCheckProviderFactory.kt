package ro.upb.summer.capstone

import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

fun appCheckProviderFactoryInstance(): AppCheckProviderFactory {
    return DebugAppCheckProviderFactory.getInstance()
}
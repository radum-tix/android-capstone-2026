package ro.upb.summer.capstone

import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

fun appCheckProviderFactoryInstance(): AppCheckProviderFactory {
    return PlayIntegrityAppCheckProviderFactory.getInstance()
}
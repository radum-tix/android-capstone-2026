package ro.upb.summer.capstone.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.crashlytics
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ro.upb.summer.capstone.BuildConfig
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    val signedIn: Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            trySend(firebaseAuth.currentUser != null)
        }

        firebaseAuth.addAuthStateListener(listener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    val isCurrentlySignedIn: Boolean
        get() = firebaseAuth.currentUser != null

    suspend fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .await()
        Firebase.crashlytics.setUserId(firebaseAuth.currentUser?.uid ?: "")
        Firebase.crashlytics.log("Signed in")
    }

    suspend fun signInWithGoogle(context: Context) {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)
        val response = credentialManager.getCredential(context, request)
        val credential = response.credential

        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(firebaseCredential).await()
        } else {
            error("Unexpected credential type: ${credential.type}")
        }
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
    }
}
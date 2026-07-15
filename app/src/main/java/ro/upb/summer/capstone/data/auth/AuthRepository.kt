package ro.upb.summer.capstone.data.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface AuthRepository {
    val signedIn: Flow<Boolean>

    suspend fun signIn(email: String, password: String)
    suspend fun signInWithGoogle()
}

class AuthRepositoryImpl : AuthRepository {
    override val signedIn: Flow<Boolean> = flowOf(false)

    override suspend fun signIn(email: String, password: String) {

    }

    override suspend fun signInWithGoogle() {

    }

}
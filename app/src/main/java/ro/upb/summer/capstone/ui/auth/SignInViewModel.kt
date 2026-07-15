package ro.upb.summer.capstone.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ro.upb.summer.capstone.data.auth.AuthRepository
import ro.upb.summer.capstone.data.auth.AuthRepositoryImpl

class SignInViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {
    private val _state = MutableStateFlow<SignInState>(SignInState.Idle)
    val state: StateFlow<SignInState>
        get() = _state

    val signedIn: StateFlow<Boolean> =
        authRepository.signedIn.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            false
        )

    fun onSignIn(email: String, password: String) {
        if (email.isBlank()) {
            //TODO: maybe show an error?
            return
        }
        if (password.isBlank()) {
            //TODO: maybe show a different error?
            return
        }

        viewModelScope.launch {
            try {
                authRepository.signIn(email, password)
            } catch (exception: Exception) {
                //TODO: show an error
            }
        }
    }

    fun onSignInWithGoogle() {

    }
}
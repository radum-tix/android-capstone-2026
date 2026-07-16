package ro.upb.summer.capstone.ui.auth

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ro.upb.summer.capstone.data.auth.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
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

    fun onSignInWithGoogle(context: Context) {
        viewModelScope.launch {
            try {
                authRepository.signInWithGoogle(context)
            } catch (exception: Exception) {
                //TODO: show an error
            }
        }
    }
}
package ro.upb.summer.capstone.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ro.upb.summer.capstone.data.auth.AuthRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {
    private val _isSignedIn = authRepository.signedIn
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            authRepository.isCurrentlySignedIn
        )
    val isSignedIn: StateFlow<Boolean>
        get() = _isSignedIn
}
package com.calmvest.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _signInState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val signInState: StateFlow<AuthUiState> = _signInState.asStateFlow()

    private val _signUpState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val signUpState: StateFlow<AuthUiState> = _signUpState.asStateFlow()

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _signInState.update { AuthUiState.Error("Please fill in all fields") }
            return
        }
        viewModelScope.launch {
            _signInState.update { AuthUiState.Loading }
            // TODO: replace with real auth call
            delay(800)
            _signInState.update { AuthUiState.Success }
        }
    }

    fun signUp(firstName: String, lastName: String, email: String, password: String) {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            _signUpState.update { AuthUiState.Error("Please fill in all fields") }
            return
        }
        if (password.length < 8) {
            _signUpState.update { AuthUiState.Error("Password must be at least 8 characters") }
            return
        }
        viewModelScope.launch {
            _signUpState.update { AuthUiState.Loading }
            // TODO: replace with real sign-up call
            delay(1000)
            _signUpState.update { AuthUiState.Success }
        }
    }

    fun resetSignInState() {
        _signInState.update { AuthUiState.Idle }
    }

    fun resetSignUpState() {
        _signUpState.update { AuthUiState.Idle }
    }
}

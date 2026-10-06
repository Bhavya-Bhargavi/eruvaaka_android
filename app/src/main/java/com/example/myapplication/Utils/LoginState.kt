package com.example.myapplication.Utils

sealed class LoginState {
    data object Idle : LoginState()

    data object Loading : LoginState()

    data class Success(
        val message: String,
        val mobile: String, val otp: String
    ) : LoginState()

    data class Error(
        val message: String
    ) : LoginState()
}
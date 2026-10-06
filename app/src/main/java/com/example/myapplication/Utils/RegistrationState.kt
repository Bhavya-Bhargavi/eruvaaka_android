package com.example.myapplication.Utils

sealed class RegistrationState {
    data object Idle : RegistrationState()

    data object Loading : RegistrationState()

    data class Success(val message: String,  val mobile: String) : RegistrationState()

    data class Error(val message: String) : RegistrationState()
}

package com.example.myapplication.Utils

import com.example.myapplication.Model.Response.VerifyOtpResponse

sealed class VerifyOtpState {
    data object Idle : VerifyOtpState()

    data object Loading : VerifyOtpState()

    data class Success(
        val response: VerifyOtpResponse
    ) : VerifyOtpState()

    data class Error(
        val message: String
    ) : VerifyOtpState()
}

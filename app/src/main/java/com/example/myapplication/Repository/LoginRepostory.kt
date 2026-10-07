package com.example.myapplication.Repository

import com.example.myapplication.Model.Request.ContactRequest
import com.example.myapplication.Model.Request.LoginRequest
import com.example.myapplication.Model.Request.VerifyOtpRequest
import com.example.myapplication.Model.Response.ContactResponse
import com.example.myapplication.Model.Response.VerifyOtpResponse
import com.example.myapplication.Model.Response.LoginResponse
import com.example.myapplication.Network.ApiInterface
import com.google.gson.JsonElement


class LoginRepository(
    private val apiInterface: ApiInterface
) {

    suspend fun loginUser(
        request: LoginRequest
    ): Result<LoginResponse> {

        return try {

            val response = apiInterface.loginUser(request)

            if (response.isSuccessful) {

                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response from server")
                )

            } else {

                Result.failure(
                    Exception(
                        "Login failed: ${response.code()}"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    /*suspend fun verifyOtp(phone: String, otp: String): VerifyOtpResponse {

        val request = VerifyOtpRequest(
            phone = phone,
            otp = otp
        )

        return apiInterface.verifyOtp(request)
    }*/

    suspend fun verifyOtp(phone: String, otp: String): Result<VerifyOtpResponse> {

        return try {

            val request = VerifyOtpRequest(
                phone = phone,
                otp = otp
            )

            val response = apiInterface.verifyOtp(request)

            if (response.isSuccessful) {

                response.body()?.let {

                    Result.success(it)

                } ?: Result.failure(
                    Exception("Empty response from server")
                )

            } else {

                Result.failure(Exception(
                        "OTP verification failed: ${response.code()}"))
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun submitContact(
        url: String,
        request: ContactRequest
    ): Result<ContactResponse> {

        return try {

            val response = apiInterface.submitContact(url, request)

            if (response.isSuccessful) {

                response.body()?.let {

                    Result.success(it)

                } ?: Result.failure(
                    Exception("Empty response from server")
                )

            } else {

                Result.failure(
                    Exception(
                        "Contact submission failed: ${response.code()}"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun logout(token: String): Result<JsonElement> {
        return try {
            val authHeader = if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token"
            val response = apiInterface.logout(authHeader)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errStr = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Logout failed: $errStr"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
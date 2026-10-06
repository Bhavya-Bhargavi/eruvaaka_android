package com.example.myapplication.Repository

import com.example.myapplication.Model.Request.RegistrationRequest
import com.example.myapplication.Model.Response.RegistrationResponse
import com.example.myapplication.Network.ApiInterface

class RegistrationRepository(private val pApiInterface: ApiInterface) {

    suspend fun registerUser(request: RegistrationRequest): Result<RegistrationResponse> {

        return try {

            val response = pApiInterface.registerUser("Bearer Public",request)

            if (response.isSuccessful) {

                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response from server")
                )

            } else {

                Result.failure(
                    Exception(
                        "Registration failed: ${response.code()}"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
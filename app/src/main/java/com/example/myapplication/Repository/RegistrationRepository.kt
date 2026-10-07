package com.example.myapplication.Repository

import android.util.Log
import com.example.myapplication.Model.Request.RegistrationRequest
import com.example.myapplication.Model.Response.RegistrationResponse
import com.example.myapplication.Network.ApiInterface
import org.json.JSONObject

class RegistrationRepository(private val pApiInterface: ApiInterface) {

    private fun parseErrorMessage(errorJson: String): String? {
        return try {
            val json = JSONObject(errorJson)
            val msg = json.optString("message", "")
            if (msg.isNotBlank()) msg else json.optString("error", "").ifBlank { null }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun registerUser(request: RegistrationRequest): Result<RegistrationResponse> {
        return try {
            val response = pApiInterface.registerUser(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errStr = response.errorBody()?.string() ?: response.message()
                Log.e("RegistrationRepository", "Registration failed HTTP ${response.code()}: $errStr")
                val userMsg = parseErrorMessage(errStr) ?: "Registration failed (${response.code()})"
                Result.failure(Exception(userMsg))
            }
        } catch (e: Exception) {
            Log.e("RegistrationRepository", "Registration exception", e)
            Result.failure(e)
        }
    }
}

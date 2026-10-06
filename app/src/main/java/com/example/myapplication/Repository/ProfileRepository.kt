package com.example.myapplication.Repository

import android.util.Log
import com.example.myapplication.Model.Request.UpdateProfileRequest
import com.example.myapplication.Model.Request.UpdateProfileResponse
import com.example.myapplication.Model.Request.UserProfile
import com.example.myapplication.Network.ApiInterface
import org.json.JSONObject
import retrofit2.Response

class ProfileRepository(private val api: ApiInterface) {

    private fun formatToken(rawToken: String): String {
        val trimmed = rawToken.trim()
        return if (trimmed.startsWith("Bearer ", ignoreCase = true)) {
            trimmed
        } else {
            "Bearer $trimmed"
        }
    }

    private fun parseErrorMessage(errorJson: String): String? {
        return try {
            val json = JSONObject(errorJson)
            val msg = json.optString("message", "")
            if (msg.isNotBlank()) msg else json.optString("error", "").ifBlank { null }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getUserProfile(token: String): Result<UserProfile> {
        return try {
            val authHeader = formatToken(token)
            val response = api.getUserProfile(authHeader)
            Result.success(response.user)
        } catch (e: Exception) {
            Log.e("ProfileRepository", "getUserProfile error", e)
            Result.failure(e)
        }
    }

    suspend fun updateProfile(
        token: String,
        request: UpdateProfileRequest
    ): Result<UpdateProfileResponse> {
        val authHeader = formatToken(token)

        val calls: List<Pair<String, suspend () -> Response<UpdateProfileResponse>>> = listOf(
            "POST api/users/updateProfile" to { api.updateProfilePost(authHeader, request) },
            "POST api/users/profile" to { api.updateProfileUserProfilePost(authHeader, request) }
        )

        var lastErrorMessage = "Profile update failed"

        for ((endpointName, call) in calls) {
            try {
                Log.d("ProfileRepository", "Executing $endpointName with auth header: $authHeader")
                val response = call()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Log.d("ProfileRepository", "$endpointName succeeded: ${body.message}")
                        return Result.success(body)
                    }
                } else {
                    val errorStr = response.errorBody()?.string() ?: ""
                    Log.e("ProfileRepository", "$endpointName returned HTTP ${response.code()}: $errorStr")

                    if (response.code() == 401 || response.code() == 403) {
                        val userMsg = parseErrorMessage(errorStr) ?: "Session expired or invalid token. Please log in again."
                        return Result.failure(Exception(userMsg))
                    }

                    lastErrorMessage = parseErrorMessage(errorStr) ?: "HTTP ${response.code()}: $errorStr"
                }
            } catch (e: Exception) {
                Log.e("ProfileRepository", "$endpointName thrown exception", e)
                lastErrorMessage = e.message ?: "Network error"
            }
        }

        return Result.failure(Exception(lastErrorMessage))
    }
}

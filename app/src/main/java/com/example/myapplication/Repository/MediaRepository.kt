package com.example.myapplication.Repository

import android.util.Log
import com.example.myapplication.Model.Response.NewsResponse
import com.example.myapplication.Network.ApiInterface
import com.google.gson.JsonElement
import okhttp3.ResponseBody

class MediaRepository(private val api: ApiInterface) {

    private fun formatToken(rawToken: String): String {
        val trimmed = rawToken.trim()
        return if (trimmed.startsWith("Bearer ", ignoreCase = true)) {
            trimmed
        } else {
            "Bearer $trimmed"
        }
    }

    suspend fun getNews(limit: Int? = null): Result<NewsResponse> {
        return try {
            val response = api.getNews(limit)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch news: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEPapers(): Result<JsonElement> {
        return try {
            val response = api.getEPapers()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch epapers: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEMagazines(): Result<JsonElement> {
        return try {
            val response = api.getEMagazines()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch emagazines: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadEPaper(token: String, publicationId: Int): Result<ResponseBody> {
        return try {
            val authHeader = formatToken(token)
            val response = api.downloadEPaper(authHeader, publicationId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errStr = response.errorBody()?.string() ?: response.message()
                Log.e("MediaRepository", "downloadEPaper failed: HTTP ${response.code()} - $errStr")
                Result.failure(Exception("Failed to download epaper: HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("MediaRepository", "downloadEPaper exception", e)
            Result.failure(e)
        }
    }

    suspend fun downloadEMagazine(token: String, publicationId: Int): Result<ResponseBody> {
        return try {
            val authHeader = formatToken(token)
            val response = api.downloadEMagazine(authHeader, publicationId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errStr = response.errorBody()?.string() ?: response.message()
                Log.e("MediaRepository", "downloadEMagazine failed: HTTP ${response.code()} - $errStr")
                Result.failure(Exception("Failed to download emagazine: HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("MediaRepository", "downloadEMagazine exception", e)
            Result.failure(e)
        }
    }
}

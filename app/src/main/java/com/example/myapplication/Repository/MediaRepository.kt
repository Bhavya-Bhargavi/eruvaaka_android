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

    suspend fun downloadFromUrl(url: String, token: String? = null): Result<ResponseBody> {
        return try {
            val authHeader = token?.let { formatToken(it) }
            val response = api.downloadFileUrl(authHeader, url)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errStr = response.errorBody()?.string() ?: response.message()
                Log.e("MediaRepository", "downloadFromUrl failed ($url): HTTP ${response.code()} - $errStr")
                Result.failure(Exception("Failed to download from URL: HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("MediaRepository", "downloadFromUrl exception ($url)", e)
            Result.failure(e)
        }
    }

    suspend fun downloadEPaper(token: String, publicationId: Int): Result<ResponseBody> {
        val authHeader = formatToken(token)

        val candidateUrls = listOf(
            "storage/app/private/eruvaaka1.pdf",
            "storage/private/eruvaaka1.pdf",
            "android_api/public/storage/app/private/eruvaaka1.pdf",
            "api/epapers/$publicationId/download",
            "storage/app/private/eruvaaka1.pdf",
            "api/download/eruvaaka1.pdf"
        )

        for (url in candidateUrls) {
            try {
                Log.d("MediaRepository", "Trying download EPaper candidate URL: $url")
                val response = api.downloadFileUrl(authHeader, url)
                if (response.isSuccessful && response.body() != null) {
                    val contentType = response.headers()["Content-Type"] ?: ""
                    Log.d("MediaRepository", "Success on EPaper $url, Content-Type: $contentType")
                    return Result.success(response.body()!!)
                } else {
                    Log.e("MediaRepository", "Failed EPaper $url: HTTP ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MediaRepository", "Error on EPaper candidate $url", e)
            }
        }

        try {
            val response = api.downloadEPaperApi(authHeader, publicationId)
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!)
            }
            Log.e("MediaRepository", "downloadEPaperApi HTTP ${response.code()}")
        } catch (e: Exception) {
            Log.e("MediaRepository", "downloadEPaperApi exception", e)
        }

        return try {
            val response = api.downloadEPaper(authHeader, publicationId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errStr = response.errorBody()?.string() ?: response.message()
                Log.e("MediaRepository", "downloadEPaper legacy failed: HTTP ${response.code()} - $errStr")
                Result.failure(Exception("Failed to download epaper: HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("MediaRepository", "downloadEPaper legacy exception", e)
            Result.failure(e)
        }
    }

    suspend fun downloadEMagazine(token: String, publicationId: Int): Result<ResponseBody> {
        val authHeader = formatToken(token)

        val candidateUrls = listOf(
            "storage/app/private/eruvaaka1.pdf",
            "storage/private/eruvaaka1.pdf",
            "android_api/public/storage/app/private/eruvaaka1.pdf",
            "api/emagazines/$publicationId/download",
            "storage/app/private/1.pdf",
            "api/download/eruvaaka1.pdf"
        )

        for (url in candidateUrls) {
            try {
                Log.d("MediaRepository", "Trying download EMagazine candidate URL: $url")
                val response = api.downloadFileUrl(authHeader, url)
                if (response.isSuccessful && response.body() != null) {
                    val contentType = response.headers()["Content-Type"] ?: ""
                    Log.d("MediaRepository", "Success on EMagazine $url, Content-Type: $contentType")
                    return Result.success(response.body()!!)
                } else {
                    Log.e("MediaRepository", "Failed EMagazine $url: HTTP ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MediaRepository", "Error on EMagazine candidate $url", e)
            }
        }

        try {
            val response = api.downloadEMagazineApi(authHeader, publicationId)
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!)
            }
            Log.e("MediaRepository", "downloadEMagazineApi HTTP ${response.code()}")
        } catch (e: Exception) {
            Log.e("MediaRepository", "downloadEMagazineApi exception", e)
        }

        return try {
            val response = api.downloadEMagazine(authHeader, publicationId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errStr = response.errorBody()?.string() ?: response.message()
                Log.e("MediaRepository", "downloadEMagazine legacy failed: HTTP ${response.code()} - $errStr")
                Result.failure(Exception("Failed to download emagazine: HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("MediaRepository", "downloadEMagazine legacy exception", e)
            Result.failure(e)
        }
    }
}

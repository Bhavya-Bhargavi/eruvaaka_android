package com.example.myapplication.Repository

import com.example.myapplication.Model.Response.NewsResponse
import com.example.myapplication.Network.ApiInterface
import com.google.gson.JsonElement
import okhttp3.ResponseBody
import retrofit2.Response

class MediaRepository(private val api: ApiInterface) {

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
            val response = api.downloadEPaper("Bearer $token", publicationId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to download epaper: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadEMagazine(token: String, publicationId: Int): Result<ResponseBody> {
        return try {
            val response = api.downloadEMagazine("Bearer $token", publicationId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to download emagazine: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

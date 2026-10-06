package com.example.myapplication.Repository

import com.example.myapplication.Model.Request.CommentRequest
import com.example.myapplication.Model.Response.ForumResponse
import com.example.myapplication.Network.ApiInterface
import com.google.gson.JsonElement

class ForumRepository(private val api: ApiInterface) {

    suspend fun getForums(): Result<ForumResponse> {
        return try {
            val response = api.getForums()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch forums: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getForumDetails(forumId: Int): Result<JsonElement> {
        return try {
            val response = api.getForumDetails(forumId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch forum details: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getForumComments(forumId: Int): Result<JsonElement> {
        return try {
            val response = api.getForumComments(forumId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch forum comments: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun postForumComment(token: String, forumId: Int, body: String): Result<JsonElement> {
        return try {
            val response = api.postForumComment("Bearer $token", forumId, CommentRequest(body))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to post comment: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

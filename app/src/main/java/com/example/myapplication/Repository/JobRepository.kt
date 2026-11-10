package com.example.myapplication.Repository

import com.example.myapplication.Model.Response.JobResponse
import com.example.myapplication.Network.ApiInterface
import com.google.gson.JsonElement

class JobRepository(private val api: ApiInterface) {

    suspend fun getJobs(): Result<JobResponse> {
        return try {
            val response = api.getJobs()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch jobs: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobDetails(jobId: Int): Result<JsonElement> {
        return try {
            val response = api.getJobDetails(jobId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch job details: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

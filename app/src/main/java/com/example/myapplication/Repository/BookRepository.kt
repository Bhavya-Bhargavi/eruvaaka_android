package com.example.myapplication.Repository

import com.example.myapplication.Model.Response.BookResponse
import com.example.myapplication.Network.ApiInterface
import com.google.gson.JsonElement

class BookRepository(private val api: ApiInterface) {

    suspend fun getBooks(): Result<BookResponse> {
        return try {
            val response = api.getBooks()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch books: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookDetails(bookId: Int): Result<JsonElement> {
        return try {
            val response = api.getBookDetails(bookId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch book details: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

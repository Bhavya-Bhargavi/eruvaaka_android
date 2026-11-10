package com.example.myapplication.Repository

import com.example.myapplication.Model.Response.OfferResponse
import com.example.myapplication.Model.Response.ProductResponse
import com.example.myapplication.Network.ApiInterface

class ProductRepository(private val api: ApiInterface) {

    suspend fun getProducts(): Result<ProductResponse> {
        return try {
            val response = api.getProducts()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch products: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getActiveOffers(): Result<OfferResponse> {
        return try {
            val response = api.getActiveOffers()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch active offers: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

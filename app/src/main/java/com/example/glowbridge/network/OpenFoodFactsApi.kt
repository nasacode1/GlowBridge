package com.example.glowbridge.network

import retrofit2.http.GET
import retrofit2.http.Query

// Define the API endpoints
interface OpenFoodFactsApi {
    @GET("api/v0/product")
    suspend fun getProductDetails(
        @Query("code") barcode: String
    ): ProductResponse
}
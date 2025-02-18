package com.example.glowbridge.network

import com.example.glowbridge.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): Response<ProductResponse>
}

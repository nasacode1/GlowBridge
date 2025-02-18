package com.example.glowbridge.data.repository

import android.util.Log
import com.example.glowbridge.network.RetrofitInstance
import com.example.glowbridge.data.model.ProductResponse
import retrofit2.HttpException

class ProductRepository {
    suspend fun getProduct(barcode: String): ProductResponse {
        try {
            val response = RetrofitInstance.api.getProduct(barcode)

            // Log full response to see status and body
            Log.d("API Response", "Response code: ${response.code()}, Message: ${response.message()}")

            if (response.isSuccessful) {
                Log.d("API Response", "Product data: ${response.body()}")
                return response.body() ?: throw Exception("Empty response body")
            } else {
                Log.e("API Error", "Failed request: ${response.code()} - ${response.message()}")
                throw HttpException(response)
            }
        } catch (e: Exception) {
            Log.e("API Exception", "Error: ${e.message}")
            throw e
        }
    }
}

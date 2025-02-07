package com.example.glowbridge.data.repository

import com.example.glowbridge.data.model.ProductResponse
import com.example.glowbridge.network.RetrofitInstance

class ProductRepository {
    suspend fun getProduct(barcode: String): ProductResponse {
        return RetrofitInstance.api.getProduct(barcode)
    }
}

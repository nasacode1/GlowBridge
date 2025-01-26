package com.example.glowbridge.network

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("product") val product: Product?,
    @SerializedName("status") val status: Int,
    @SerializedName("status_verbose") val statusVerbose: String
)

data class Product(
    @SerializedName("product_name") val productName: String?,
    @SerializedName("nutriscore_grade") val nutriscoreGrade: String?
)

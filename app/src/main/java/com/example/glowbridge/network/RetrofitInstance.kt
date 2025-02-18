package com.example.glowbridge.network

import okhttp3.OkHttpClient
import kotlin.lazy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://world.openfoodfacts.org/"

    val api: OpenFoodFactsApi by lazy {
        val okHttpClient = OkHttpClient.Builder()
            // Customize your client here, for example, adding timeouts, interceptors, etc.
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Explicitly passing the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }
}

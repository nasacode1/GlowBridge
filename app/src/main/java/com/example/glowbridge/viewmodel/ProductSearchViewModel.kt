package com.example.glowbridge.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glowbridge.data.model.ProductResponse
import com.example.glowbridge.data.repository.ProductRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductSearchViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _product = MutableStateFlow<ProductResponse?>(null)
    val product: StateFlow<ProductResponse?> = _product


    fun searchProduct(barcode: String) {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "Searching for barcode: $barcode")
                val response = repository.getProduct(barcode)
                _product.value = response
                Log.d("ViewModel", "Product data received: $response")
            } catch (e: Exception) {
                Log.e("ViewModel Error", "Error: ${e.message}")
            }
        }
    }
}

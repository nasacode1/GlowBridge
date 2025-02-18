package com.example.glowbridge.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.glowbridge.viewmodel.ProductSearchViewModel

@Composable
fun SearchByBarcodeScreen(viewModel: ProductSearchViewModel) {
    Log.d("DEBUG", "SearchByBarcodeScreen is composed")

    val productState by viewModel.product.collectAsState(initial = null)
    var barcode by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(100.dp),
        verticalArrangement = Arrangement.Center){
        OutlinedTextField(
            value = barcode,
            onValueChange = {barcode = it },
            label = { Text("Enter Barcode") }
        )
        Log.e("DEBUG", "Search Button Composable Loaded")

        Button(
            onClick = {
                Log.e("DEBUG", "Button Clicked")
                if(barcode.isNotBlank()){
                    viewModel.searchProduct(barcode)
                    Log.e("Api call", "api call was made")
                } else {
                    Log.e("empty barcode", "Barcode cannot be empty")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        if(productState != null){
            productState?.product?.let { product ->
                Text("Product Name: ${product.product_name ?: "Unknown"}")
                Text("Additives: ${product.additives ?: "Unknown"}")
                Text("Sugars: ${product.sugars ?: "Unknown"}")
                Text("Nutri-score: ${product.nutriscore_grade ?: "Unknown"}")
                Log.e("productstate", product.product_name?: "unknown")
            }
        }

    }
}

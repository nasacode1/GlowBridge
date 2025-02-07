package com.example.glowbridge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.glowbridge.viewmodel.ProductSearchViewModel

@Composable
fun ProductSearchScreen() {
    val viewModel: ProductSearchViewModel = viewModel()
    val productState by viewModel.product.collectAsState()

    var barcode by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = barcode,
            onValueChange = { barcode = it },
            label = { Text("Enter Barcode") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.searchProduct(barcode) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        productState?.product?.let { product ->
            Text("Product Name: ${product.product_name ?: "Unknown"}")
            Text("Additives: ${product.additives ?: "Unknown"}")
            Text("Sugars: ${product.sugars ?: "Unknown"}")
            Text("Nutri-score: ${product.nutriscore_grade ?: "Unknown"}")
        }
    }
}

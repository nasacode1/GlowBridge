package com.example.glowbridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.glowbridge.ui.navigation.BottomNavigationBar
import com.example.glowbridge.ui.navigation.NavGraph
import com.example.glowbridge.network.RetrofitInstance
import com.example.glowbridge.ui.theme.GlowBridgeTheme
import com.example.glowbridge.viewmodel.ProductSearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.activity.viewModels
import androidx.compose.material3.CenterAlignedTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val viewModel: ProductSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlowBridgeTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(title = { Text(text = "GlowBridge") })
                    }
                    ,
                     bottomBar = {
                        if(currentRoute != "Welcome") {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavGraph(navController = navController)
                }
            }
        }
    }
}

@Composable
fun ProductSearchScreen(viewModel: ProductSearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var barcode by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf<String?>(null) }
    var nutriScore by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = barcode,
            onValueChange = { barcode = it },
            label = { Text("Enter Barcode") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.searchProduct(barcode) }) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(32.dp))

        productName?.let {
            Text("Product Name: $it", style = MaterialTheme.typography.bodyLarge)
        }
        nutriScore?.let {
            Text("NutriScore: $it", style = MaterialTheme.typography.bodyLarge)
        }
        errorMessage?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GlowBridgeTheme {
    }
}
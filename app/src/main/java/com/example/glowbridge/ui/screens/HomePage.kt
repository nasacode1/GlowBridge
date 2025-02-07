package com.example.glowbridge.ui.screens

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun HomePage(){
    Column(
        modifier = Modifier.fillMaxSize().padding(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(80.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Spacer(modifier = Modifier.weight(1f))
            ScanFoodButton()
            LogFoodButton()
//        Text(text = "Hey, welcome to Glow Bridge.")
//       SubmitButton()
        }
    }
}

@Composable
fun ScanFoodButton(){
    IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Filled.SoupKitchen, contentDescription = "Scan food", modifier = Modifier.size(50.dp))
    }
}

@Composable
fun LogFoodButton(){
    IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Log Food", modifier = Modifier.size(50.dp))
    }
}

@Composable
fun WebViewWithEmbeddedCode(){
    val embeddedCode = """
       <!-- Calendly link widget begin -->
       <link href="https://assets.calendly.com/assets/external/widget.css" rel="stylesheet">
       <script src="https://assets.calendly.com/assets/external/widget.js" type="text/javascript" async></script>
       <a href="" onclick="Calendly.initPopupWidget({url: 'https://calendly.com/maanasa-2022-vitstudent/30min'});return false;">Schedule time with me</a>
       <!-- Calendly link widget end -->
    """.trimIndent()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

                // Assign a custom WebViewClient
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        // Allow the WebView to handle the Calendly popups and URLs
                        return false  // Return false to let WebView handle the URL loading
                    }
                }

                loadDataWithBaseURL(null, embeddedCode, "text/html", "UTF-8", null)
            }
        },
        modifier = Modifier.heightIn(max = 300.dp) // Optional: Adjust height
    )
}

@Composable
fun CardMinimalExample() {
    Card(
        modifier = Modifier.size(width =300.dp, height = 100.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        SubmitButton()
    }
}

@Composable
fun SubmitButton() {
    val context = LocalContext.current
    Button(onClick = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://calendly.com/maanasa-2022-vitstudent/30min"))
        context.startActivity(intent)
    }) {
        Text(text = "Click to book an appointment with nutritionists")
    }
}



package com.example.glowbridge.ui.screens

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun HomePage(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(text = "Hey, welcome to Glow Bridge.")
        Spacer(modifier = Modifier.height(40.dp))
        SubmitButton()

    }}


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



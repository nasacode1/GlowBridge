package com.example.glowbridge.ui.screens

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun MeetingPageScreen(htmlContent: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(message: android.webkit.ConsoleMessage?): Boolean {
                        android.util.Log.d("WebView", message?.message() ?: "No message")
                        return true
                    }
                }
                loadDataWithBaseURL("about:blank", htmlContent,"text/html", "UTF-8", null)
            }

        }
    )
}

@Composable
fun calendlyEmbed(){
    val htmlData = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Calendly</title>
                </head>
                <body>
                    <!-- Calendly inline widget begin -->
                    <div class="calendly-inline-widget" 
                        data-url="https://calendly.com/maanasa-2022-vitstudent" 
                        style="min-width:320px;height:700px;">
                    </div>
                    <script type="text/javascript" src="https://assets.calendly.com/assets/external/widget.js" async></script>
                    <!-- Calendly inline widget end -->
                </body>
                </html>
            """.trimIndent()
    MeetingPageScreen(htmlData)
}

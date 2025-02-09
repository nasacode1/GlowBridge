package com.example.glowbridge.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.unit.sp
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Dining
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

@Composable
fun HomePage(onScanSuccess: () -> Unit){
    Column(
        modifier = Modifier
            .padding(10.dp, 110.dp),
    ) {
        Text(text = "Health articles", fontSize = 20.sp)
        Spacer(modifier = Modifier.size(20.dp))
        SimpleLazyRow()
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = "Nutrient Lookup", fontSize = 20.sp)
        Spacer(modifier = Modifier.size(10.dp))
        nutrientLookupSection()
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = "Tracking & Insights", fontSize = 20.sp)
        Spacer(modifier = Modifier.size(10.dp))
        trackingInsightsSection()
        Row(
            modifier = Modifier
                .padding(80.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            ScanFoodButton(
                onClick = {
                    onScanSuccess()
                }
            )
            LogFoodButton()
//       SubmitButton()
        }
    }
}

@Composable
fun nutrientLookupSection(){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly){
        OutlinedCard(onClick = { /*TODO*/ }, modifier = Modifier.size(100.dp)) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ){
                Column {
                    Spacer(modifier = Modifier.size(15.dp))
                    Icon(imageVector = Icons.Outlined.CameraAlt, contentDescription = "Camera icon",
                        Modifier
                            .size(20.dp)
                            .align(Alignment.CenterHorizontally))
                    Text(text = "Scan food",
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center)
                }
            }
        }

        OutlinedCard(onClick = { /*TODO*/ }, modifier = Modifier.size(100.dp)) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ){
                Column {
                    Spacer(modifier = Modifier.size(15.dp))
                    Icon(imageVector = Icons.Outlined.QrCodeScanner, contentDescription = "QR code scanner icon",
                        Modifier
                            .size(20.dp)
                            .align(Alignment.CenterHorizontally))
                    Text(text = "Search by barcodde",
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}


@Composable
fun trackingInsightsSection(){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly){
        OutlinedCard(onClick = { /*TODO*/ }, modifier = Modifier.size(100.dp)) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ){
                Column {
                    Spacer(modifier = Modifier.size(15.dp))
                    Icon(imageVector = Icons.Outlined.Dining, contentDescription = "Log food icon",
                        Modifier
                            .size(20.dp)
                            .align(Alignment.CenterHorizontally))
                    Text(text = "Log food",
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center)
                }
            }
        }

        OutlinedCard(onClick = { /*TODO*/ }, modifier = Modifier.size(100.dp)) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ){
                Column {
                    Spacer(modifier = Modifier.size(15.dp))
                    Icon(imageVector = Icons.Outlined.BarChart, contentDescription = "Daily insights icon",
                        Modifier
                            .size(20.dp)
                            .align(Alignment.CenterHorizontally))
                    Text(text = "Daily Insights",
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}
fun openWebPage(context: Context, url: String) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    val chooser = Intent.createChooser(intent, "Open with")

    if (intent.resolveActivity(context.packageManager) != null) {
        Log.d("Webpage", "Opening now...")
        context.startActivity(chooser)
    }
    else{
        Log.d("Webpage", "Action not found")
    }
}

fun fetchOgImage(url: String, callback: (String?) -> Unit) {
    Thread {
        try {
            val doc = Jsoup.connect(url).get()
            val ogImage = doc.select("meta[property=og:image]").attr("content")
            callback(ogImage)
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }.start()
}

fun fetchOgTitle(url: String, callback: (String?) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val doc = Jsoup.connect(url).timeout(5000).get() // Set timeout
            val ogTitle = doc.select("meta[property=og:title]").attr("content").ifEmpty { null }
            withContext(Dispatchers.Main) {
                callback(ogTitle) // Ensure callback runs on main thread
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                callback(null) // Return null if an error occurs
            }
        }
    }
}


@Composable
fun SimpleLazyRow(
) {
    val context= LocalContext.current
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        var articleUrl1 = "https://timesofindia.indiatimes.com/life-style/health-fitness/health-news/the-role-of-anti-inflammatory-foods-in-pcos-management/articleshow/118052630.cms"
        item {
            var imageUrl by remember { mutableStateOf<String?>(null) }
            var title by remember { mutableStateOf<String?>(null) }
            LaunchedEffect(articleUrl1) {
                fetchOgImage(articleUrl1) { fetchedUrl ->
                    imageUrl = fetchedUrl
                }
                fetchOgTitle(articleUrl1){fetchedTitle ->
                    title = fetchedTitle
                }
            }
            Card(
            onClick = {
                    openWebPage(context, articleUrl1)
                },
                modifier = Modifier.size(width = 300.dp, height = 240.dp)
            ) {
                Box(Modifier.fillMaxSize()) {
                    if(imageUrl != null){
                        Image(painter = rememberAsyncImagePainter(model = imageUrl),
                            contentDescription ="Article image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillHeight
                        )
                        Text(text = title ?:"Loading..", modifier = Modifier.border(3, shape = RectangleShape))
                        
                    }
                    else{
                        Text("Clickable", Modifier.align(Alignment.Center))
                    }
                }
            }
        }
        var articleUrl2 = "https://timesofindia.indiatimes.com/life-style/health-fitness/health-news/the-truth-about-juice-cleanses-how-they-worsen-gut-health-and-metabolism-reveals-study/articleshow/118034912.cms"
        item {
            var imageUrl by remember { mutableStateOf<String?>(null) }
            LaunchedEffect(articleUrl2) {
                fetchOgImage(articleUrl2) { fetchedUrl ->
                    imageUrl = fetchedUrl
                }
            }
            Card(
                onClick = {
                    openWebPage(context, articleUrl2)
                },
                modifier = Modifier.size(width = 300.dp, height = 240.dp)
            ) {
                Box(Modifier.fillMaxSize()) {
                    if(imageUrl != null){
                        Image(painter = rememberAsyncImagePainter(model = imageUrl),
                            contentDescription ="Article image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillHeight
                        )
                    }
                    else{
                        Text("Clickable", Modifier.align(Alignment.Center))
                    }
                }
            }
        }
        var articleUrl3 = "https://timesofindia.indiatimes.com/life-style/health-fitness/health-news/105-year-old-woman-shares-2-simple-secrets-to-living-a-long-life/articleshow/118007525.cms"
        item {
            var imageUrl by remember { mutableStateOf<String?>(null) }
            LaunchedEffect(articleUrl3) {
                fetchOgImage(articleUrl3) { fetchedUrl ->
                    imageUrl = fetchedUrl
                }
            }
            Card(
                onClick = {
                    openWebPage(context, articleUrl3)
                },
                modifier = Modifier.size(width = 300.dp, height = 240.dp)
            ) {
                Box(Modifier.fillMaxSize()) {
                    if(imageUrl != null){
                        Image(painter = rememberAsyncImagePainter(model = imageUrl),
                            contentDescription ="Article image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillHeight
                        )
                    }
                    else{
                        Text("Clickable", Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
fun ScanFoodButton(onClick: () -> Unit){
    IconButton(onClick = {  onClick()}) {
        Icon(imageVector = Icons.Filled.SoupKitchen, contentDescription = "Scan food", modifier = Modifier.size(50.dp))
    }
}

@Composable
fun LogFoodButton(){
    IconButton(onClick = {  }) {
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
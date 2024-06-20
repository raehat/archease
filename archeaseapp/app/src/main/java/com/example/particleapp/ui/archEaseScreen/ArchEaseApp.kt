package com.example.particleapp.ui.archEaseScreen

import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.particleapp.ui.archEaseScreen.homescreen.BuyCryptoScreen
import com.example.particleapp.ui.archEaseScreen.homescreen.HomeScreen
import com.example.particleapp.ui.archEaseScreen.homescreen.MyAccountScreen
import com.example.particleapp.ui.archEaseScreen.homescreen.PayByAddressScreen
import com.example.particleapp.ui.archEaseScreen.homescreen.PaymentCompleted
import com.example.particleapp.ui.archEaseScreen.homescreen.SelectDestinationChainScreen
import com.example.particleapp.ui.archEaseScreen.homescreen.SellCryptoScreen
import com.example.particleapp.ui.archEaseScreen.homescreen.SwitchChainScreen
import com.example.particleapp.ui.archEaseScreen.homescreen.ViewSentTransactionsScreen
import com.example.particleapp.ui.archEaseScreen.homescreen.ViewUnclaimedTransactionsScreen
import com.example.particleapp.ui.archEaseScreen.loginscreen.LoginScreen
import com.example.particleapp.ui.archEaseScreen.splashscreen.SplashScreen
import com.example.particleapp.ui.theme.ParticleAppTheme
import com.example.particleapp.utils.QRScanner


class ArchEaseApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel : ArchEaseViewModel = ViewModelProvider(this)[ArchEaseViewModel::class.java]
        setContent {
            ParticleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val context = LocalContext.current
//                    val webView = remember { WebView(context) }
//                    webView.getSettings().setAllowFileAccessFromFileURLs(true); // Allow access to file:// URLs
//                    webView.getSettings().setAllowUniversalAccessFromFileURLs(true); //
//                    webView.apply {
//                        settings.javaScriptEnabled = true
//                        webViewClient = WebViewClient()
//                        loadUrl("file:///android_asset/index.html")
//                    }
//                    webView.addJavascriptInterface(JavaScriptInterface(), "AndroidInterface")

//                    webView.webViewClient = object : WebViewClient() {
//                        override fun shouldInterceptRequest(
//                            view: WebView,
//                            request: WebResourceRequest
//                        ): WebResourceResponse? {
//                            if (request.url.toString().startsWith("file:///android_asset/")) {
//                                try {
//                                    // Load script from assets directory
//                                    val assetPath = request.url.toString()
//                                        .replaceFirst("file:///android_asset/".toRegex(), "")
//                                    val stream = assets.open(assetPath)
//                                    return WebResourceResponse("text/javascript", "UTF-8", stream)
//                                } catch (e: IOException) {
//                                    e.printStackTrace()
//                                }
//                            }
//                            return super.shouldInterceptRequest(view, request)
//                        }
//                    }


//                    val sumFunction = """
//                    """.trimIndent()
//
//                    webView.evaluateJavascript(sumFunction) {}

//                    webView.webChromeClient = object : WebChromeClient() {
//                        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
//                            consoleMessage?.let {
//                                Log.d("WebViewConsole", "${it.message()} -- From line ${it.lineNumber()} of ${it.sourceId()}")
//                            }
//                            return super.onConsoleMessage(consoleMessage)
//                        }
//                    }
//
//
//                    webView.webViewClient = object : WebViewClient() {
//                        override fun onPageFinished(view: WebView?, url: String?) {
//                            super.onPageFinished(view, url)
//
////                             Now execute your JavaScript function
////                            webView.evaluateJavascript("window.sum();") { value ->
////                                val result = value.replace("\"", "") // Strip quotes
////                                println("Result: $result")
////                            }
//                            view?.loadUrl("module::sum();")
//                        }
//                    }

                    val showToast : (String) -> Unit = {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                    val navController = rememberNavController()
                    val data = "true"
                    WebView(LocalContext.current).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                        loadUrl("file:///android_asset/index.html")
                    }

                    NavHost(navController = navController, startDestination = Screen.SplashScreen) {
                        composable(route = Screen.SplashScreen) {
                            ColumnScreen { SplashScreen(navController) }
                        }
                        composable(route = Screen.LoginScreen) {
                            ColumnScreen { LoginScreen(navController, viewModel, showToast, LocalContext.current) }
                        }
                        composable(route = Screen.HomeScreen) {
                            HomeScreen(navController, viewModel)
                        }
                        composable(route = Screen.MyAccountScreen) {
                            MyAccountScreen(navController, viewModel, showToast, LocalContext.current)
                        }
                        composable(route = Screen.SwitchChainScreen) {
                            SwitchChainScreen(navController, viewModel, showToast)
                        }
                        composable(route = Screen.PayByAddressScreen) {
                            PayByAddressScreen(navController, viewModel, showToast, LocalContext.current)
                        }
                        composable(route = Screen.SelectDestinationChainScreen) {
                            SelectDestinationChainScreen(navController, viewModel)
                        }
                        composable(route = Screen.QrScanner) {
                            QRScanner(navController, viewModel)
                        }
                        composable(route = Screen.PaymentCompletedScreen) {
                            PaymentCompleted(navController, viewModel)
                        }
                        composable(route = Screen.ViewSentTxScreen) {
                            ViewSentTransactionsScreen(navController, viewModel, showToast, LocalContext.current)
                        }
                        composable(route = Screen.ViewUnclaimedTxScreen) {
                            ViewUnclaimedTransactionsScreen(navController, viewModel, showToast, LocalContext.current)
                        }
                        composable(route = Screen.SellCryptoScreen) {
                            SellCryptoScreen(navController, viewModel, showToast)
                        }
                        composable(route = Screen.BuyCryptoScreen) {
                            BuyCryptoScreen(navController, viewModel, showToast, LocalContext.current)
                        }
                    }
                }
            }
        }
    }
}

private class JavaScriptInterface {
    @JavascriptInterface
    fun onResult(result: Int) {
        // Handle the result from JavaScript here
            // For example, display the result in a Toast or update the UI
        println("baba $result")
    }
}
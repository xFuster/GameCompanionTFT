package com.example.gamecompanion.Activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.example.gamecompanion.R
import com.example.gamecompanion.models.OAuthTokens
import com.example.gamecompanion.services.NetworkManager
import com.example.gamecompanion.services.UserManagerService
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class TwitchLoginActivity : AppCompatActivity() {

    private val TAG = "TwitchLoginActivity"
    private lateinit var webView: WebView
    private var OAUTH_CLIENT_ID = "c4rbom4h3nli13i1cvm4iaqo89lp0r"
    private var OAUTH_CLIENT_SECRET = "trqrjqje4o4gvwg3gcopcr2dh5hs90"
    private var OAUTH_CLIENT_REDIRECT = "http://localhost"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twitch_login)
        initViews()
        loadOAuthUrl()
    }

    private fun initViews(){
        webView = findViewById(R.id.webView)
    }

    private fun loadOAuthUrl(){
        val uri = Uri.parse("https://id.twitch.tv/oauth2/authorize")
            .buildUpon()
            .appendQueryParameter("client_id", OAUTH_CLIENT_ID)
            .appendQueryParameter("redirect_uri",OAUTH_CLIENT_REDIRECT)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", listOf("user:edit","user:read:email").joinToString(" "))
            .build()

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if(request?.url?.toString()?.startsWith(OAUTH_CLIENT_REDIRECT) == true){
                    // Login Success
                    Log.i(TAG,"Login succes with url: ${request?.url}")
                    request.url.getQueryParameter("code")?.let {
                        webView.visibility = View.GONE
                        getAccessTokens(it)
                    } ?: run{
                        // TODO: Handle Error
                        Log.i("Pepe","Liada en otro sitio")
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        webView.loadUrl(uri.toString())
    }

    private fun getAccessTokens(authCode : String){
        // Configure JSON Parsing
        val httpClient = NetworkManager.createHttpClient()

        lifecycleScope.launch {

            withContext(Dispatchers.IO) {
                try {
                    val response: OAuthTokens = httpClient.post<OAuthTokens>("https://id.twitch.tv/oauth2/token") {
                        parameter("client_id", OAUTH_CLIENT_ID)
                        parameter("client_secret", OAUTH_CLIENT_SECRET)
                        parameter("code", authCode)
                        parameter("grant_type", "authorization_code")
                        parameter("redirect_uri", OAUTH_CLIENT_REDIRECT)
                    }
                    UserManagerService(this@TwitchLoginActivity).saveAccessToken(response.accessToken)
                    // Close
                    finish()
                }catch (t:Throwable){
                    Log.i("Pepe","Liada en otro sitio")
                }
            }
        }

    }
}
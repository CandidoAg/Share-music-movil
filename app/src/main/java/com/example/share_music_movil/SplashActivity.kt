package com.example.share_music_movil

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.share_music_movil.Connectors.UserService
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


internal class SplashActivity : AppCompatActivity() {
    private var msharedPreferences: SharedPreferences? = null

    private var queue: RequestQueue? = null

    private val CLIENT_ID = "75d293c5215d4a08ab6e54974abd81b0"
    private val REDIRECT_URI = "com.sharemusic://callback"
    private val REQUEST_CODE = 1337
    private val SCOPES =
        "user-read-recently-played,user-library-modify,user-library-read,playlist-modify-public,playlist-modify-private,user-read-email,user-read-private,playlist-read-private,playlist-read-collaborative"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_splash)
        authenticateSpotify()
        msharedPreferences = getSharedPreferences("SPOTIFY", 0)
        queue = Volley.newRequestQueue(this)
    }


    private fun waitForUserInfo() {
        val userService = UserService(queue!!, msharedPreferences!!)
        userService[object : VolleyCallBack {
            override fun onSuccess() {
                val user = userService.user
                Log.d("STARTING", "GOT USER INFORMATION")
                getSharedPreferences("SPOTIFY", 0).edit().putString("userid", user!!.id).commit()
                startMainActivity()
            }
        }]
    }

    private fun startMainActivity() {
        val newintent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(newintent)
    }


    private fun authenticateSpotify() {
        val builder: AuthorizationRequest.Builder =
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
        builder.setScopes(arrayOf<String>(SCOPES))
        val request: AuthorizationRequest = builder.build()
        Log.d("STARTING", "AUTENTIFICATING")
        AuthorizationClient.openLoginActivity(this@SplashActivity, REQUEST_CODE, request)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response: AuthorizationResponse =
                AuthorizationClient.getResponse(resultCode, intent)
            when (response.getType()) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d("STARTING", "GOT AUTH TOKEN")
                    getSharedPreferences("SPOTIFY", 0).edit().putString("token", response.getAccessToken()).apply()
                    waitForUserInfo()
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.d("STARTING", "FAIL: "+response.error)

                }
                else -> {
                }
            }
        }
    }
}
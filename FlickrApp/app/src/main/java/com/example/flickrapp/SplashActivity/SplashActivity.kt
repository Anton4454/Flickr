package com.example.flickrapp.SplashActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Adapter
import com.airbnb.lottie.LottieAnimationView
import com.example.flickrapp.JsonResponse.Auth
import com.example.flickrapp.JsonResponse.Response
import com.example.flickrapp.Photo.MainActivity
import com.example.flickrapp.R
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class SplashActivity : AppCompatActivity() {
    private var mHandler = Handler()
    private lateinit var jsonImages: Response
    private var auth = Auth()
    private lateinit var lottieAnimationView:LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lottieAnimationView = findViewById(R.id.lottie_splash)
        downloadByUrl(auth.getRecent())
    }

    private fun downloadByUrl(stringUrl: String) {
        GlobalScope.launch {
            var response: String =
                URL(auth.getRecent())
                    .readText()
            val gson = Gson()
            jsonImages = gson.fromJson(response, Response::class.java)
            mHandler.postDelayed(parseIsReady, 0)
        }
    }

    private val parseIsReady: Runnable = object : Runnable {
        override fun run() {
            if (jsonImages.photos?.photo?.size != 100) {
                mHandler.postDelayed(this, 100)
            } else {
                val intent = Intent(baseContext, MainActivity::class.java)
                intent.putExtra("response_list", jsonImages)
                startActivity(intent)
                overridePendingTransition(R.transition.fade_in_transition, R.transition.fade_out_transition)
            }
        }
    }
}
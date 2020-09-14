package com.example.flickrapp.RecyclerAdapter

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.flickrapp.R

class PhotoPageActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        imageView = findViewById(R.id.photoFullSize)

        val stringUrl: String = intent.getStringExtra("PHOTO")
        Glide
            .with(this)
            .load(
                stringUrl
            )
            .override(1080, 2400)
            .into(imageView)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        }

        return super.onOptionsItemSelected(item)
    }
}
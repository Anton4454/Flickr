package com.example.flickrapp

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide

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
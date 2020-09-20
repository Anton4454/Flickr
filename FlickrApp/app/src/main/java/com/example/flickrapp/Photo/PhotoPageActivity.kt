package com.example.flickrapp.Photo

import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.flickrapp.R
import com.squareup.picasso.Picasso


class PhotoPageActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var stringUrl: String
    val VIEW_NAME_HEADER_IMAGE = "detail:header:image"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        imageView = findViewById(R.id.photoFullSize)

        stringUrl = intent.getStringExtra("PHOTO")

        ViewCompat.setTransitionName(imageView, VIEW_NAME_HEADER_IMAGE);

        loadItem()
    }

    private fun loadItem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            loadThumbnail()
        } else {
            loadFullSizeImage()
        }
    }

    private fun loadThumbnail() {
        Picasso.with(imageView.context)
            .load(stringUrl)
            .noFade()
            .into(imageView);
    }

    private fun loadFullSizeImage() {
        Picasso.with(imageView.getContext())
            .load(stringUrl)
            .noFade()
            .noPlaceholder()
            .into(imageView)
    }

    @RequiresApi(21)
    private fun addTransitionListener(): Boolean {
        val transition: Transition? = window.sharedElementEnterTransition
        if (transition != null) {
            transition.addListener(object : Transition.TransitionListener {
                override fun onTransitionEnd(transition: Transition) {
                    loadFullSizeImage()

                    transition.removeListener(this)
                }

                override fun onTransitionStart(transition: Transition?) {
                }

                override fun onTransitionCancel(transition: Transition) {
                    transition.removeListener(this)
                }

                override fun onTransitionPause(transition: Transition?) {
                }

                override fun onTransitionResume(transition: Transition?) {
                }
            })
            return true
        }

        return false
    }

}
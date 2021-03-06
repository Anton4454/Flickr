package com.example.flickrapp.RecyclerAdapter

import android.content.Intent
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flickrapp.JsonResponse.Auth
import com.example.flickrapp.JsonResponse.PhotoItem
import com.example.flickrapp.R
import com.example.flickrapp.JsonResponse.Response
import com.example.flickrapp.Photo.PhotoPageActivity
import kotlinx.android.synthetic.main.album_layout.view.*

class RecyclerAdapter(private val images: Response?) :
    RecyclerView.Adapter<RecyclerAdapter.PhotoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        var layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
            R.layout.album_layout,
            parent,
            false
        )
        return PhotoHolder(view)
    }

    override fun getItemCount(): Int = 100

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val itemPhoto = images?.photos?.photo?.get(position)
        holder.bindPhoto(itemPhoto)
    }

    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private lateinit var stringUrl: String
        private val auth = Auth()

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val showPhotoIntent = Intent(context, PhotoPageActivity::class.java)
            showPhotoIntent.putExtra(PHOTO_KEY, stringUrl)
            context.startActivity(showPhotoIntent)
        }

        companion object {
            val PHOTO_KEY = "PHOTO"
        }

        fun bindPhoto(photoItem: PhotoItem?) {
            this.stringUrl = auth.getPhotoUrl(photoItem)

            Glide
                .with(view.context)
                .load(
                    stringUrl
                )
                .override(300, 300)
                .into(view.album)

            if (photoItem?.title?.length == 0) {
                view.image_description.visibility = View.INVISIBLE
            } else {
                view.image_description.text = photoItem?.title
            }
        }
    }
}


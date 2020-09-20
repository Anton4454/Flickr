package com.example.flickrapp.JsonResponse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Response(

	val stat: String? = null,

	val photos: Photos? = null
) : Parcelable

@Parcelize
data class PhotoItem(

	val owner: String? = null,

	val server: String? = null,

	val ispublic: Int? = null,

	val isfriend: Int? = null,

	val farm: Int? = null,

	val id: String? = null,

	val secret: String? = null,

	val title: String? = null,

	val isfamily: Int? = null
) : Parcelable

@Parcelize
data class Photos(

	val page: Int? = null,

	val perpage: Int? = null,

	val total: String? = null,

	val pages: Int? = null,

	val photo: List<PhotoItem>? = null

) : Parcelable

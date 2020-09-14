package com.example.flickrapp

class Auth {
    private var KEY: String = "ad8ac362010b97cbee572388dd5ccc4d"
    private var SECRET: String = "6ec723c172ffbb74"

    fun getRecent(): String {
        return "https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=${KEY}&format=json&nojsoncallback=1"
    }

    fun getPhotos(query:String): String {
        return "https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=${KEY}&tags=${query}&format=json&nojsoncallback=1"
    }
}
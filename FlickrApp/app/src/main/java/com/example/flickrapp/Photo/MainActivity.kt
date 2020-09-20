package com.example.flickrapp.Photo

import ClickListener.RecyclerItemClickListener
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.util.Pair as AndroidPair
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.mauker.materialsearchview.MaterialSearchView
import com.airbnb.lottie.LottieAnimationView
import com.example.flickrapp.JsonResponse.Auth
import com.example.flickrapp.JsonResponse.Response
import com.example.flickrapp.R
import com.example.flickrapp.RecyclerAdapter.RecyclerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var jsonImages: Response
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: MaterialSearchView
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var toolbar: Toolbar
    private var mHandler = Handler()
    private var auth = Auth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        searchView = findViewById(R.id.search_view)
        lottieAnimationView = findViewById(R.id.lottieLoading)
        toolbar = findViewById(R.id.toolbar_main)
        searchView.setSuggestionBackground(R.color.suggestionColor)
        setSupportActionBar(toolbar)
        title = resources.getString(R.string.recent_pictures)

        var intent = intent
        jsonImages = intent.getParcelableExtra<Response>("response_list")
        adapter = RecyclerAdapter(jsonImages)
        createRecyclerView(adapter)




        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                title = query
                downloadByUrl(auth.getPhotos(query))
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchView.setSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewOpened() {
            }

            override fun onSearchViewClosed() {
            }
        })

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val showPhotoIntent = Intent(baseContext, PhotoPageActivity::class.java)
                        showPhotoIntent.putExtra(
                            RecyclerAdapter.PhotoHolder.PHOTO_KEY,
                            auth.getPhotoUrl(jsonImages.photos?.photo?.get(position))
                        )

                        baseContext.startActivity(showPhotoIntent)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )
    }


    private fun lottieStart() {
        lottieAnimationView.visibility = View.VISIBLE
        lottieAnimationView.resumeAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                searchView.openSearch()
                return true
            }

            R.id.themeSwitcher -> {
                val isNightTheme =
                    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                when (isNightTheme) {
                    Configuration.UI_MODE_NIGHT_YES ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    Configuration.UI_MODE_NIGHT_NO ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (searchView.isOpen) {
            searchView.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            val matches = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null && matches.size > 0) {
                val searchWrd = matches[0]
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false)
                }
            }

            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun downloadByUrl(stringUrl: String) {
        lottieStart()
        GlobalScope.launch {
            var response: String =
                URL(stringUrl)
                    .readText()
            val gson = Gson()
            jsonImages = gson.fromJson(response, Response::class.java)
            mHandler.postDelayed(parseIsReady, 0)
        }
    }

    private val parseIsReady: Runnable = object : Runnable {
        var i = 0;
        override fun run() {
            if (jsonImages.photos?.photo?.size != 100) {
                i++;
                if (i > 20) {
                    Snackbar.make(
                        recyclerView,
                        resources.getString(R.string.pict_not_found),
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(resources.getString(R.string.try_again)) {
                        searchView.openSearch()
                    }.show()
                    lottieAnimationView.visibility = View.GONE
                } else {
                    mHandler.postDelayed(this, 100)
                }
            } else {
                lottieAnimationView.visibility = View.GONE
                adapter = RecyclerAdapter(jsonImages)
                createRecyclerView(adapter)
            }
        }
    }

    private fun createRecyclerView(adapter: RecyclerAdapter) {
        linearLayoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        gridLayoutManager = GridLayoutManager(baseContext, 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                       /* val showPhotoIntent =
                            Intent(this@MainActivity, PhotoPageActivity::class.java)
                        showPhotoIntent.putExtra(
                            RecyclerAdapter.PhotoHolder.PHOTO_KEY,
                            auth.getPhotoUrl(jsonImages.photos?.photo?.get(position))
                        )

                        ActivityCompat.startActivity(this@MainActivity, showPhotoIntent, a)*/

                        val showPhotoIntent =
                            Intent(this@MainActivity, PhotoPageActivity::class.java)
                        showPhotoIntent.putExtra(
                            RecyclerAdapter.PhotoHolder.PHOTO_KEY,
                            auth.getPhotoUrl(jsonImages.photos?.photo?.get(position))
                        )

                        val photoPageActivity = PhotoPageActivity()
                        val pair = AndroidPair<View, String>(view?.findViewById(R.id.album), photoPageActivity.VIEW_NAME_HEADER_IMAGE)
                        val activityOptions: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this@MainActivity,
                                pair
                            )

                        ActivityCompat.startActivity(
                            this@MainActivity,
                            showPhotoIntent,
                            activityOptions.toBundle()
                        )
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )
    }

    override fun onPause() {
        super.onPause()
        searchView.clearSuggestions()
    }
}
package com.udacity.asteroidradar.main

import android.app.Application
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.local.LocalDatabase
import com.udacity.asteroidradar.model.local.RepoImp
import com.udacity.asteroidradar.model.remote.RemoteRepoImp
import com.udacity.asteroidradar.model.remote.ServiceApi
import com.udacity.asteroidradar.model.remote.retroBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var remoteRepoImp: RemoteRepoImp =
        RemoteRepoImp(retroBuilder.getRetroBuilder().create(ServiceApi::class.java))
    private var db = LocalDatabase.getInstance(application.applicationContext)
    private var localrepoImp = RepoImp(db)


    private val potdError = MutableLiveData("")
    val potdErrorStatus: LiveData<String>
        get() = potdError
    private var potd: Bitmap? = null
    private var _potdTitle = ""
    private var _potdExplanation = ""
    fun getPictureOfTheDayOffline(potdImage: ImageView, potdTitle: TextView) {
        if (potd != null) {
            potdImage.setImageBitmap(potd)
            potdTitle.text = _potdTitle
        }
    }

    fun getPictureOfTheDay(potdImage: ImageView, potdTitle: TextView) =
        viewModelScope.launch {
            if (potd != null) {
                potdImage.setImageBitmap(potd)
                potdTitle.text = _potdTitle
            } else {
                val request = remoteRepoImp.getPictureOfTheDay()
                if (request.isSuccessful) {
                    if (request.body() != null) {

                        val url = request.body()!!.url
                        potdTitle.text = request.body()!!.title
                        withContext(Dispatchers.IO) {
                            potd = Picasso.get().load(url).get()
                            withContext(Dispatchers.Main) {
                                potdImage.setImageBitmap(potd)
                            }
                            _potdTitle = request.body()!!.title
                            _potdExplanation = request.body()!!.explanation
                        }

                    }
                } else {
                    potdError.value = """Error ${request.code()}: ${request.message()}"""
                }
            }

        }

    fun getPotdTitle() = _potdTitle
    fun getPotdExplanation() = _potdExplanation
    fun clearptodError() {
        potdError.value = ""
    }


    private val getAsteroids = MutableLiveData("")
    val getAsteroidsStatus: LiveData<String>
        get() = getAsteroids


    private val dpIsUpToDate = MutableLiveData(false)

    private val _asteroids = MutableLiveData<List<Asteroid>>(emptyList())
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids


    fun getAsteroidsOffline() =
        viewModelScope.launch {
            if (dpIsUpToDate.value == false) {
                localrepoImp.deleteOutdateAsteroidData()
                dpIsUpToDate.value = true
            }
            val localAstroids = localrepoImp.getAsteroidData()
            if (!localAstroids.isNullOrEmpty()) {
                _asteroids.value = localAstroids


                getAsteroids.value = "success"

            } else {
                getAsteroids.value = "error"

            }


        }

    fun getTodayAsteroidsOffline() =
        viewModelScope.launch {

            val localTodayAstroids = localrepoImp.getTodayAsteroidData()
            if (!localTodayAstroids.isNullOrEmpty()) {
                _asteroids.value = localTodayAstroids
            }

        }


    fun getAsteroidsOnline() = viewModelScope.launch {
        val request = remoteRepoImp.getAsteroids()
        if (request.isSuccessful) {
            if (request.body() != null) {
                _asteroids.value = parseAsteroidsJsonResult(JSONObject(request.body()!!))

                asteroids.value?.forEach {
                    localrepoImp.insertAsteroidData(it)
                }

            }
            getAsteroids.value = "success"
        } else {
            getAsteroids.value = "error"
        }
    }


    fun clearGetAsteroids() {
        getAsteroids.value = ""
    }


}
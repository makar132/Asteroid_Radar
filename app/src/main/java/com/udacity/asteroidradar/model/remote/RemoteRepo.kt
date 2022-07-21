package com.udacity.asteroidradar.model.remote

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfTheDay
import org.json.JSONObject
import retrofit2.Response

interface RemoteRepo {
    suspend fun getPictureOfTheDay(ApiKey: String = Constants.API_KEY): Response<PictureOfTheDay>
    suspend fun getAsteroids(
        ApiKey: String = Constants.API_KEY
    ): Response<String>
}
package com.udacity.asteroidradar.model.remote

import com.udacity.asteroidradar.PictureOfTheDay
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {
    @GET("planetary/apod/")
    suspend fun getPictureOfTheDay(
        @Query("api_key") apiKey: String,
    ): Response<PictureOfTheDay>

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("api_key") apiKey: String
    ): Response<String>


}
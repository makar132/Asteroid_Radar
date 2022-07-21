package com.udacity.asteroidradar.model.remote

import com.udacity.asteroidradar.api.getToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RemoteRepoImp(private val api: ServiceApi) : RemoteRepo {
    override suspend fun getPictureOfTheDay(ApiKey: String) =
        withContext(Dispatchers.IO) {
            api.getPictureOfTheDay(ApiKey)
        }

    override suspend fun getAsteroids(ApiKey: String) =
        withContext(Dispatchers.IO) {
            api.getAsteroids(getToday(),ApiKey)
        }


}
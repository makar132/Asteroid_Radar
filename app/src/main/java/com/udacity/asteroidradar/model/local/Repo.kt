package com.udacity.asteroidradar.model.local

import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.model.Asteroid

interface Repo {

    suspend fun insertAsteroidData(asteroid: Asteroid)

    suspend fun getAsteroidData(): List<Asteroid>?
    suspend fun getTodayAsteroidData(today_date: String = getToday()): List<Asteroid>?

    suspend fun deleteOutdateAsteroidData(today_date: String = getToday())

}
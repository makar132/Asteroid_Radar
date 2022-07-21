package com.udacity.asteroidradar.model.local

import com.udacity.asteroidradar.model.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepoImp(private val db: LocalDatabase) : Repo {


    override suspend fun insertAsteroidData(asteroid: Asteroid) {
        withContext(Dispatchers.IO) {
            db.userDao().insertAsteroidData(asteroid)
        }
    }

    override suspend fun getAsteroidData() = withContext(Dispatchers.IO) {
        db.userDao().getAsteroidData()
    }

    override suspend fun getTodayAsteroidData(today_date: String) = withContext(Dispatchers.IO) {
        db.userDao().getTodayAsteroidData(today_date)
    }

    override suspend fun deleteOutdateAsteroidData(today_date: String) {
        db.userDao().deleteOutdateAsteroidData(today_date)
    }


}
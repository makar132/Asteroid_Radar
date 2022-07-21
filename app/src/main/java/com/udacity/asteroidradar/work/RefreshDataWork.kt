package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.model.local.LocalDatabase
import com.udacity.asteroidradar.model.local.RepoImp
import com.udacity.asteroidradar.model.remote.RemoteRepoImp
import com.udacity.asteroidradar.model.remote.ServiceApi
import com.udacity.asteroidradar.model.remote.retroBuilder
import org.json.JSONObject

class RefreshDataWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val database = LocalDatabase.getInstance(applicationContext)
        val localrepository = RepoImp(database)
        val remoterepository =
            RemoteRepoImp(retroBuilder.getRetroBuilder().create(ServiceApi::class.java))
        return try {
            val request = remoterepository.getAsteroids()
            if (request.isSuccessful) {
                if (request.body() != null) {
                    parseAsteroidsJsonResult(JSONObject(request.body() ?: "")).forEach {
                        localrepository.insertAsteroidData(it)
                    }
                }

            }
            localrepository.deleteOutdateAsteroidData()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }


    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}

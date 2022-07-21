package com.udacity.asteroidradar.api

import android.annotation.SuppressLint
import android.os.Build
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.model.Asteroid
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        if (nearEarthObjectsJson.optJSONArray(formattedDate) != null) {
            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                    .getJSONObject("kilometers").getDouble("estimated_diameter_max")

                val closeApproachData = asteroidJson
                    .getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                    .getDouble("astronomical")
                val isPotentiallyHazardous = asteroidJson
                    .getBoolean("is_potentially_hazardous_asteroid")

                val asteroid = Asteroid(
                    id, codename, formattedDate, absoluteMagnitude,
                    estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous
                )
                asteroidList.add(asteroid)
            }
        }
    }

    return asteroidList
}

@SuppressLint("WeekBasedYear")
private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance(Locale("en_US"))
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale("en_US"))
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}

@SuppressLint("WeekBasedYear")
fun getToday(): String {
    val currentTime = Calendar.getInstance(Locale("en_US")).time
    val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale("en_US"))
    } else {
        TODO("VERSION.SDK_INT < N")
    }
    return (dateFormat.format(currentTime))
}

@SuppressLint("WeekBasedYear")
fun getYasterday(): String {
    val calendar=Calendar.getInstance(Locale("en_US"))
        calendar.add(Calendar.DAY_OF_YEAR, -1)
    val currentTime = calendar.time
    val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale("en_US"))
    } else {
        TODO("VERSION.SDK_INT < N")
    }
    return (dateFormat.format(currentTime))
}



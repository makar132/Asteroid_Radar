package com.udacity.asteroidradar.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.model.Asteroid

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroidData(asteroid: Asteroid)

    @Query(value = "select * from asteroid_table Order by closeApproachDate asc")
    suspend fun getAsteroidData(): List<Asteroid>?

    @Query(value = "select * from asteroid_table where closeApproachDate =  :today_date  Order by closeApproachDate asc ")
    suspend fun getTodayAsteroidData(today_date: String): List<Asteroid>?

    @Query("delete from asteroid_table where closeApproachDate < :today_date")
    suspend fun deleteOutdateAsteroidData(today_date: String)


}
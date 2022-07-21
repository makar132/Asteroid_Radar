package com.udacity.asteroidradar.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.Converters


const val DATABASE_NAME = "database"

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun userDao(): Dao

    companion object {
        @Volatile
        private var instance: LocalDatabase? = null
        fun getInstance(context: Context): LocalDatabase {

            return instance ?: synchronized(Any()) {
                instance ?: buildDatabase(context).also { instance = it }

            }
        }

        private fun buildDatabase(context: Context): LocalDatabase {
            return Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        }

    }
}

package com.udacity.asteroidradar.model.remote

import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class retroBuilder {
    companion object {
        private const val BaseURL = Constants.BASE_URL
        fun getRetroBuilder(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
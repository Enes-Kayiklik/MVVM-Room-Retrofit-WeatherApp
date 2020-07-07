package com.enes.weather.api

import com.enes.weather.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitInstance: WeatherApi by lazy {
            retrofit.create(WeatherApi::class.java)
        }
    }
}
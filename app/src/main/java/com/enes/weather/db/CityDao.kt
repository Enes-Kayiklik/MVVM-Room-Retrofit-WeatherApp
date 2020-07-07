package com.enes.weather.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: City)

    @Delete
    fun deleteCity(city: City)

    @Query("SELECT * FROM city_names")
    fun getAllCity(): LiveData<List<City>>
}
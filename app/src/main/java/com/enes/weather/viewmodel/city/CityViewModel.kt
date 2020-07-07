package com.enes.weather.viewmodel.city

import androidx.lifecycle.ViewModel
import com.enes.weather.db.City
import com.enes.weather.db.CityDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel(val db: CityDatabase) : ViewModel() {

    fun upsert(city: City) = CoroutineScope(Dispatchers.IO).launch {
        db.cityDao.insertCity(city)
    }

    fun delete(city: City) = CoroutineScope(Dispatchers.IO).launch {
        db.cityDao.deleteCity(city)
    }

    fun getAllItems() = db.cityDao.getAllCity()
}
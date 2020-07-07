package com.enes.weather.viewmodel.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enes.weather.api.RetrofitInstance
import com.enes.weather.model.WeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    private val _weather = MutableLiveData<WeatherModel>()
    val weather: LiveData<WeatherModel>
        get() = _weather

    fun getWeatherProperty(city: String) {
        RetrofitInstance.retrofitInstance.getData(cityName = city)
            .enqueue(object : Callback<WeatherModel> {
                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    _weather.value = response.body()
                }
            })
    }
}
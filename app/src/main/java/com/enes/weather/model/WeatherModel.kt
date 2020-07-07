package com.enes.weather.model

data class WeatherModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<com.enes.weather.model.List>,
    val message: Int
)
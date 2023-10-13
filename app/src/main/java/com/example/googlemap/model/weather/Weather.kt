package com.example.googlemap.model.weather

data class Weather(
    val base: String,
    val cod: Int,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind:Wind,
)
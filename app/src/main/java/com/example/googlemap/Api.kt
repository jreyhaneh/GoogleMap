package com.example.googlemap

import com.example.googlemap.model.location.Location
import com.example.googlemap.model.weather.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {

    @GET
    suspend fun gatLocation(

        @Url url: String = "https://nominatim.openstreetmap.org/reverse",
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("format") format: String = "jsonv2"
    ): Response<Location>

    @GET
    suspend fun getWeather(

        @Url url: String = "https://api.openweathermap.org/data/2.5/weather",
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = "93a2b4204b2fd90dcf4bc839fdcb4552",
        @Query("lang") language: String = "fa"
    ): Response<Weather>


}
package com.example.googlemap.model.weather

import com.google.gson.annotations.SerializedName

data class Wind(
    val speed: Float,
    @SerializedName("deg")
    val degree: Int
)
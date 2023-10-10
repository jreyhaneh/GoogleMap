package com.example.googlemap.model.location

data class Location(
    val address: Address,
    val addresstype: String,
    val category: String,
    val display_name: String,
    val lat: String,
    val licence: String,
    val lon: String,
    val name: String
)
package com.example.googlemap

import com.google.android.gms.common.api.Api.Client
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

//


object RetrofitInstance {
    val gson = GsonBuilder()
        .setLenient()
        .create()


    val api: Api by lazy {
        Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
//            .addConverterFactory(ScalarsConverterFactory.create())
            .build().create(Api::class.java)

    }
}
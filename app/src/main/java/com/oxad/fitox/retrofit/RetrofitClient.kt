package com.oxad.fitox.retrofit

import com.google.gson.GsonBuilder

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://oxadmedia.in/" // Replace with your API URL

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}
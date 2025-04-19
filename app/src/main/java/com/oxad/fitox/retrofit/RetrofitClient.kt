package com.oxad.fitox.retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val BASE_URL = ApiHelper.getretroUrL() // Replace with your API URL

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    // Configure OkHttpClient with timeouts
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS) // Connection timeout
        .readTimeout(10, TimeUnit.SECONDS)    // Read timeout
        .writeTimeout(10, TimeUnit.SECONDS)   // Write timeout
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Attach OkHttpClient
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}

package com.example.proyectologin005d.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Esta configuración es EXCLUSIVA para las noticias de prueba
object PostRetrofit {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com"

    val api: PostApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApiService::class.java)
    }
}
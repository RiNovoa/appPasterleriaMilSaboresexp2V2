package com.example.proyectologin005d.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Configuración para conectar con tu Backend Spring Boot
object SpringRetrofit {
    // 10.0.2.2 es la IP especial que usa el emulador de Android para acceder a localhost de tu PC
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: SpringApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpringApiService::class.java)
    }
}

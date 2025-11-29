package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.model.Post
// 1. CAMBIO AQUÍ: Importamos PostRetrofit en vez de RetrofitInstance
import com.example.proyectologin005d.data.network.PostRetrofit

class NewsRepository {

    suspend fun getPosts(): List<Post> {
        // 2. CAMBIO AQUÍ: Llamamos a la API de PostRetrofit
        return PostRetrofit.api.getPosts()
    }
}
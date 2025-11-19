package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.model.Post
import com.example.proyectologin005d.data.network.RetrofitInstance

// Este repositorio se encarga de acceder a los datos usando Retrofit
class NewsRepository {

    // Función que obtiene los posts desde la API
    suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }
}

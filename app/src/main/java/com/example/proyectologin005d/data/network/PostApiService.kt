package com.example.proyectologin005d.data.network

import com.example.proyectologin005d.data.model.Post // Asegúrate que este import exista
import retrofit2.http.GET

interface PostApiService {
    @GET("/posts")
    suspend fun getPosts(): List<Post>
}
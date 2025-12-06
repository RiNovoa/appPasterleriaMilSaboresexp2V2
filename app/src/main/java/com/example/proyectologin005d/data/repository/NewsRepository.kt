package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.model.Post
import com.example.proyectologin005d.data.network.PostRetrofit

class NewsRepository {

    suspend fun getPosts(): List<Post> {
        return PostRetrofit.api.getPosts()
    }
}

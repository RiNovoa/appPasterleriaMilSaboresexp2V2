package com.example.proyectologin005d.data.model

// Esta clase representa un post obtenido desde la API, que usaremos como "Noticia"
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

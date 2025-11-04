package com.example.proyectologin005d.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contrasena: String,
    val role: String,
    val fechaNacimiento: String? = null,
    val direccion: String? = null
)

package com.example.proyectologin005d.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "usuarios")
@Serializable
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contrasena: String,
    val role: String,
    val fechaNacimiento: String? = null,
    val direccion: String? = null
)

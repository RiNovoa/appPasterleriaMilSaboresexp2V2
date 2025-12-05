package com.example.proyectologin005d.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resenas")
data class Resena(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val calificacion: Int,
    val comentario: String
)

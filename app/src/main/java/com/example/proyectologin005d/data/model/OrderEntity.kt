package com.example.proyectologin005d.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.proyectologin005d.data.database.Converters

@Entity(tableName = "ordenes")
@TypeConverters(Converters::class)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val total: Int,
    val itemsJson: String // Guardaremos los items como JSON string por simplicidad
)

package com.example.proyectologin005d.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "carrito")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productoId: Int,
    val cantidad: Int
)

data class CartWithProduct(
    @Embedded val cartItem: CartItemEntity,
    @Relation(
        parentColumn = "productoId",
        entityColumn = "id"
    )
    val product: Producto
)

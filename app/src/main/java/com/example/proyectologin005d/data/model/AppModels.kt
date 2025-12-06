package com.example.proyectologin005d.data.model

// Movidos desde CartViewModel para desacoplar la capa de datos de la capa de UI

data class CartItem(
    val product: Producto, 
    val quantity: Int
)

data class Order(
    val id: Int,
    val date: String,
    val items: List<CartItem>,
    val total: Int
)

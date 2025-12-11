package com.example.proyectologin005d.data.network

import com.example.proyectologin005d.data.model.Producto
import retrofit2.http.GET
import retrofit2.http.Path

// Define aquí los endpoints de tu Microservicio Spring Boot
interface SpringApiService {

    // Ejemplo: Obtener todos los productos desde http://localhost:8080/api/productos
    @GET("/api/productos")
    suspend fun obtenerProductos(): List<Producto>

    // Ejemplo: Obtener un producto por ID
    @GET("/api/productos/{id}")
    suspend fun obtenerProducto(@Path("id") id: Int): Producto
}
